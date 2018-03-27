package game.Map;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import graphic.Drawable;
import util.Direction;
import util.Prop;

public class Chunk implements Drawable {
    private int size;
    private int posX;
    private int posY;

    private Chunk upNeighbor;
    private Chunk downNeighbor;
    private Chunk rightNeighbor;
    private Chunk leftNeighbor;

    private Tile[][] tiles;

    public Chunk(int x, int y) {
        size = Integer.parseInt(Prop.getProp("chunkSize"));
        posX = x;
        posY = y;
    }

    public void setTiles(Tile[][] tileSet) {
        tiles = new Tile[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(tileSet[i], 0, tiles[i], 0, size);
            int j = 0;
            for (Tile tile : tiles[i]) {
                tile.setParentChunk(this);
                tile.setPosition(i, j);
                j++;
            }
        }

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (i != tiles.length - 1) {
                    tiles[i][j].setNeighborTile(tiles[i + 1][j], Direction.RIGHT);
                } else {
                    tiles[i][j].setNeighborTile(tiles[0][j], Direction.RIGHT);
                }
                if (i != 0) {
                    tiles[i][j].setNeighborTile(tiles[i - 1][j], Direction.LEFT);
                } else {
                    tiles[i][j].setNeighborTile(tiles[tiles.length - 1][j], Direction.LEFT);
                }
                if (j != tiles.length - 1) {
                    tiles[i][j].setNeighborTile(tiles[i][j + 1], Direction.DOWN);
                } else {
                    tiles[i][j].setNeighborTile(tiles[i][0], Direction.DOWN);
                }
                if (j != 0) {
                    tiles[i][j].setNeighborTile(tiles[i][j - 1], Direction.UP);
                } else {
                    tiles[i][j].setNeighborTile(tiles[i][tiles.length - 1], Direction.UP);
                }
            }
        }
    }

    public void setNeighborChunk(Chunk chunk, Direction direction) {
        switch (direction) {
            case UP:
                upNeighbor = chunk;
                break;
            case DOWN:
                downNeighbor = chunk;
                break;
            case LEFT:
                leftNeighbor = chunk;
                break;
            case RIGHT:
                rightNeighbor = chunk;
                break;
        }
    }

    public Chunk getNeighborChunk(Direction direction) {
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

    @Override
    public void Draw(GL2 gl) {
        gl.glBegin(GL.GL_TRIANGLES);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                float h = tiles[j][i].getHeight();

                switch (tiles[j][i].getType()) {
                    case 0:
                        gl.glColor3f((h + 64) / 128, (h + 64) / 128, 0f);
                        break;
                    case 1:
                        gl.glColor3f(h / 128, h / 128, h / 128);
                        break;
                    case 2:
                        gl.glColor3f(0f, h / 128, 0f);
                        break;
                    case 3:
                        gl.glColor3f(0f, 0f, (h + 64) / 128);
                        break;
                }

                gl.glTexCoord2f(0.0625f, 1);
                gl.glVertex2i(posY * size + i, posX * size + j);

                gl.glTexCoord2f(0.0625f, 1 - 0.0625f);
                gl.glVertex2i(posY * size + i, posX * size + j - 1);

                gl.glTexCoord2f(0, 1 - 0.0625f);
                gl.glVertex2i(posY * size + i - 1, posX * size + j - 1);


                gl.glTexCoord2f(0.0625f, 1);
                gl.glVertex2i(posY * size + i, posX * size + j);

                gl.glTexCoord2f(0, 1);
                gl.glVertex2i(posY * size + i - 1, posX * size + j);

                gl.glTexCoord2f(0, 1 - 0.0625f);
                gl.glVertex2i(posY * size + i - 1, posX * size + j - 1);
            }
        }
        gl.glEnd();
    }
}
