package game.Map;

import util.Direction;
import util.Prop;

import java.util.LinkedList;
import java.util.List;

public class Tile extends AStarNode {
    private int type;
    private int height;
    private int weight;
    private int visits;

    public Point position;

    //public int posX;
    //public int posY;

    private Tile upNeighbor;
    private Tile downNeighbor;
    private Tile rightNeighbor;
    private Tile leftNeighbor;

    private boolean reachable = true;

    private Chunk parentChunk;

    Tile(int height) {
        position = new Point(0, 0);
        this.height = height;
        if (height < 10) {//water
            type = 3;
            weight = 1000;
            reachable = false;
        } else if (height < 15) {//sand
            type = 0;
            weight = 2;
        } else if (height < 90) {//grass
            type = 2;
            weight = 1;
        } else {//mountains
            type = 1;
            weight = 3;
        }
        visits = 0;
    }

    public void setNeighborTile(Tile tile, Direction direction) {
        switch (direction) {
            case UP:
                upNeighbor = tile;
                break;
            case DOWN:
                downNeighbor = tile;
                break;
            case LEFT:
                leftNeighbor = tile;
                break;
            case RIGHT:
                rightNeighbor = tile;
                break;
        }
    }

    public Tile getNeighborTile(Direction direction) {
        switch (direction) {
            case UP:
                return upNeighbor;
            case DOWN:
                return downNeighbor;
            case LEFT:
                return leftNeighbor;
            case RIGHT:
                return rightNeighbor;
        }
        return null;
    }

    int getType() {
        return type;
    }

    //int getWeight(){return weight;}

    float getHeight() {
        return height;
    }

    public boolean isReachable() {
        return reachable;
    }

    void setParentChunk(Chunk chunk) {
        parentChunk = chunk;
    }

    void setPosition(int x, int y) {
        position.x = x;
        position.y = y;
    }

    public Point getGlobalPosition() {
        Point parentPosition = parentChunk.getPosition();
        int x = parentPosition.x * Prop.getInt("chunkSize") + position.x;
        int y = parentPosition.y * Prop.getInt("chunkSize") + position.y;
        return new Point(x, y);
    }

    @Override
    public float getCost(AStarNode node) {
        return weight;
    }

    @Override
    public float getEstimatedCost(AStarNode node) {
        return Math.abs(((Tile) node).height - this.height);
    }

    @Override
    public List<Tile> getNeighbors() {
        LinkedList<Tile> neighbors = new LinkedList<>();
        neighbors.add(upNeighbor);
        neighbors.add(downNeighbor);
        neighbors.add(leftNeighbor);
        neighbors.add(rightNeighbor);
        return neighbors;// parentChunk.getNeighbors(posX, posY);
    }
}
