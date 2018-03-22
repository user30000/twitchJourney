package game.Map;

class Tile {
    private int type;
    private int height;
    private int weight;
    private int visits;

    Tile(int height) {
        this.height = height;
        if(height < 10){
            type = 3;
        }
        else if(height < 15){
            type = 0;
        }
        else if (height < 90){
            type = 2;
        }
        else {
            type = 1;
        }
        visits = 0;
    }

    int getType() {
        return type;
    }

    float getHeight() {
        return height;
    }
}
