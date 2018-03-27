package game.Map;

public class Point extends java.awt.Point {
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
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

    public void left(int a) {
        this.x -= a;
    }

    public void right(int a) {
        this.x += a;
    }

    public void down(int a) {
        this.y -= a;
    }

    public void up(int a) {
        this.y += a;
    }
}
