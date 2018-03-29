package game.creatures;

import chatBot.outMessageListener;
import game.GameEventListener;
import game.Map.Chunk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CreatureFactory {
    private int playerCount = 0;
    private int creatureCount = 0;
    private int counterTick = 0;
    private final outMessageListener chat;

    public CreatureFactory(outMessageListener outMsg) {
        chat = outMsg;
    }

    public void SavePopulationPlayer(Map<String, Creature> creatures, Chunk parentChunk, GameEventListener listener) {
        counterTick++;

        List<Creature> added = new ArrayList<>();

        if (counterTick == 5 && playerCount < 10) {
            added.add(this.createPlayer());
        }

        if (creatureCount < 10) {
            added.add(this.createCreature());
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

    public void CleanDead(Map<String, Creature> creatures) {
        creatures.entrySet().removeIf(e -> {
            Creature c = e.getValue();
            if (c.isDead()) {
                if (c.isPlayer()) {
                    playerCount--;
                } else {
                    creatureCount--;
                }
                if (chat != null) {
                    chat.Write("Умер: " + c.toFullString());
                }
                return true;
            }
            return false;
        });
    }

    private Creature createPlayer() {
        playerCount++;
        String Name = "Чувачок" + String.valueOf(new Random().nextInt());
        return new Player(Name);
    }

    private Creature createCreature() {
        String Name = "Монстряш" + String.valueOf(new Random().nextInt());
        creatureCount++;
        Creature c = new Creature(15, "zombie", Name);
        c.setTextureName("zombie");
        return c;
    }
}
