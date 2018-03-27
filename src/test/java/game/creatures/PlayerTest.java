package game.creatures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void getHealth() {
        Player player = new Player("testnick");
        assertEquals(100, player.getHealth());
    }

    @Test
    void getNickName() {
        Player player = new Player("testnick");
        assertEquals("testnick", player.getNickName());
        player = new Player("Testnick");
        assertEquals("testnick", player.getNickName());
        player = new Player("Цезарь Петрович");
        assertEquals("цезарь петрович", player.getNickName());
    }
}