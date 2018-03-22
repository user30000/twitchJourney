package game.creatures;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @org.junit.jupiter.api.Test
    void getHealth() {
        Player player = new Player("testnick");
        assertEquals(100, player.getHealth());
    }

    @org.junit.jupiter.api.Test
    void getNickName() {
        Player player = new Player("testnick");
        assertEquals("testnick", player.getNickName());
        player = new Player("Testnick");
        assertEquals("testnick", player.getNickName());
        player = new Player("Цезарь Петрович");
        assertEquals("цезарь петрович", player.getNickName());
    }
}