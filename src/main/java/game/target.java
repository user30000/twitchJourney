package game;

import game.Map.Point;

public interface target {
    Point getPosition();
    void hit(int dmg);
}
