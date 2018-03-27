package game.Map;

import util.Direction;

public class Point extends java.awt.Point {
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(Direction direction) {
        this.move(direction, 1);
    }

    public void left() {
        this.left(1);
    }

    public void right() {
        this.right(1);
    }

    public void down() {
        this.down(1);
    }

    public void up() {
        this.up(1);
    }

    public void left(int delta) {
        this.x -= delta;
    }

    public void right(int delta) {
        this.x += delta;
    }

    public void down(int delta) {
        this.y -= delta;
    }

    public void up(int delta) {
        this.y += delta;
    }

    public void move(Direction direction, int delta) {
        switch (direction) {
            case RIGHT:
                right(delta);
                break;
            case LEFT:
                left(delta);
                break;
            case DOWN:
                down(delta);
                break;
            case UP:
                up(delta);
                break;
        }
    }
}
