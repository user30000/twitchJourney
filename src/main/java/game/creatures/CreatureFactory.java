package game.creatures;

import chatBot.outMessageListener;
import game.GameEventListener;
import game.Map.Chunk;
import util.Prop;

import java.util.*;

public class CreatureFactory {
    static private int playerCount = 0;
    static private int creatureCount = 0;
    static private int playersMaximum = Prop.getInt("playersMaximum");
    static private int creaturesMaximum = Prop.getInt("creaturesMaximum");

    private int counterTick = 0;
    private final outMessageListener chat;

    static private List<Creature> roamingList;

    public CreatureFactory(outMessageListener outMsg) {
        if (roamingList == null) {
            roamingList = new LinkedList<Creature>();
        }
        chat = outMsg;
    }

    public void getRoamingcreature(Creature creature) {
        roamingList.add(creature);
    }

    public void resetCreatures(Map<String, Creature> creatures, Chunk parentChunk) {
        roamingList.forEach(c -> {
            if (c.parentChuck == parentChunk) {
                creatures.put(c.getName(), c);
                //c.setParentChuck(parentChunk);
            }
        });

        roamingList.removeIf(c -> c.parentChuck == parentChunk);
    }

    public void SavePopulationPlayer(Map<String, Creature> creatures, Chunk parentChunk, GameEventListener listener) {
        counterTick++;

        List<Creature> added = new ArrayList<>();

        if (counterTick == 5 && playerCount < playersMaximum) {
            added.add(this.createPlayer(parentChunk));
        }

        if (creatureCount < creaturesMaximum) {
            added.add(this.createCreature(parentChunk));
        }

        added.forEach((c) -> {
            if (chat != null) {
                chat.Write("Родился: " + c.toString());
            }
            creatures.put(c.getName(), c);
            c.setParentChuck(parentChunk);
            c.setGameEventListener(listener);
        });

        if (counterTick == 5) {
            counterTick = 0;
        }
    }

    public void CleanDead(Map<String, Creature> creatures, Chunk parentChunk) {
        creatures.entrySet().removeIf(e -> {
            Creature c = e.getValue();
            if (c.isDead()) {
                if (c.isPlayer()) {
                    playerCount--;
                } else {
                    creatureCount--;
                }
                if (chat != null && c.isDead()) {
                    chat.Write("Умер: " + c.toFullString());
                }
                return true;
            }
            if(c.parentChuck != parentChunk){
                return true;
            }
            return false;
        });
    }

    private Creature createPlayer(Chunk chunk) {
        playerCount++;
        String Name = "Чувачок" + String.valueOf(new Random().nextInt());
        return new Player(Name, chunk);
    }

    private Creature createCreature(Chunk chunk) {
        String Name = "Монстряш" + String.valueOf(new Random().nextInt());
        creatureCount++;
        Creature c = new Creature(15, "zombie", Name, chunk);
        c.setTextureName("zombie");
        return c;
    }
}
