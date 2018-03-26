package game.creatures;

import chatBot.outMessageListener;
import game.GameData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CreatureFactory {
    private int playerCount = 0;
    private int creatureCount = 0;
    private int counterTick = 0;
    private GameData data;
    private final outMessageListener chat;

    public CreatureFactory(GameData data, outMessageListener outMsg) {
        this.data = data;
        chat = outMsg;
    }

    public void SavePopulationPlayer() {
        counterTick++;

        List<Creature> added = new ArrayList<>();

        if (counterTick == 5 && playerCount < 10) {
            added.add(this.createPlayer());
        }

        if (creatureCount < 10) {
            added.add(this.createCreature());
        }

        added.forEach((c) -> {
            chat.Write("Родился: " + c.toString());
            this.data.getCreatures().put(c.getName(), c);
        });

        if (counterTick == 5) {
            counterTick = 0;
        }
    }

    private Creature createPlayer() {
        String Name = "Чувачок" + String.valueOf(new Random().nextInt());
        return new Player(Name);
    }

    private Creature createCreature() {
        String Name = "Монстряш" + String.valueOf(new Random().nextInt());
        Creature c = new Creature(15, "zombie", Name);
        c.setTextureName("zombie");
        return c;
    }

    public void Kill(String nickname) {
        Map<String, Creature> creatures = data.getCreatures();
        Creature removed = creatures.remove(nickname);
        if ( removed != null) {
            if(removed.isPlayer()) {
                this.playerCount--;
            } else {
                this.creatureCount--;
            }
            chat.Write("Убит: " + removed.toFullString());
        }
    }
}
