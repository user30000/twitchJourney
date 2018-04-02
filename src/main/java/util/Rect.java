package util;

import java.awt.geom.Point2D;

/**
 * Created by BigDuke on 02.04.2018.
 */
public class Rect {
    private Point2D dl;
    private Point2D ur;

    public Rect(float x1, float y1, float x2, float y2) {
        dl = new Point2D.Float(x1, y1);
        ur = new Point2D.Float(x2, y2);
    }

    public void setRect(float x1, float y1, float x2, float y2) {
        dl = new Point2D.Float(x1, y1);
        ur = new Point2D.Float(x2, y2);
    }

    public double getBottom() {
        return dl.getY();
    }

    public double getUp() {
        return ur.getY();
    }

    public double getRight() {
        return ur.getX();
    }

    public double getLeft() {
        return dl.getX();
    }

    public boolean included(Rect rect) {
        double thisLeft = dl.getX();
        double thisDown = dl.getY();
        double thisRight = ur.getX();
        double thisUp = ur.getY();

        double left = rect.dl.getX();
        double down = rect.dl.getY();
        double right = rect.ur.getX();
        double up = rect.ur.getY();

        if (left > thisRight)
            return false;
        if (right < thisLeft)
            return false;
        if (up < thisDown)
            return false;
        if (down > thisUp)
            return false;
        return true;
    }
}
