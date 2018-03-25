package game.Map;

import java.util.List;

class Tile extends AStarNode {
    private int type;
    private int height;
    private int weight;
    private int visits;

    public int posX;
    public int posY;

    public boolean reachable = true;

    private Chunk parentChunk;

    Tile(int height) {
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

    int getType() {
        return type;
    }

    //int getWeight(){return weight;}

    float getHeight() {
        return height;
    }

    void setParentChunk(Chunk chunk) {
        parentChunk = chunk;
    }

    void setPosition(int x, int y) {
        posX = x;
        posY = y;
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
    public List getNeighbors() {
        return parentChunk.getNeighbors(posX, posY);
    }
}
