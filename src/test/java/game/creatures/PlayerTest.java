package game.creatures;

import game.Map.Chunk;
import game.Map.Map;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void getHealth() {
        Player player = new Player("testnick", null);
        assertEquals(100, player.getHealth());
    }

    @Test
    void getNickName() {
        Player player = new Player("testnick", null);
        assertEquals("testnick", player.getNickName());
        player = new Player("Testnick", null);
        assertEquals("testnick", player.getNickName());
        player = new Player("Цезарь Петрович", null);
        assertEquals("цезарь петрович", player.getNickName());
    }
}