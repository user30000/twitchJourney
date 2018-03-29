package game.Map;

import com.jogamp.opengl.GL2;
import game.GameEventListener;
import game.Tickable;
import graphic.Drawable;
import util.Direction;
import util.Prop;

import java.util.Random;

public class Map implements Drawable, Tickable {
    private GameEventListener gameEventListener;

    private int size;
    private int chunkSize;
    private Chunk[][] chunks;
    private int[][] heightMap;

    private Random r;

    public Map(int size, GameEventListener gameEventListener) {
        this.gameEventListener = gameEventListener;

        this.size = size;
        chunkSize = Integer.parseInt(Prop.getProp("chunkSize"));
        r = new Random(500);

        heightMap = new int[size][size];
        chunks = new Chunk[size / chunkSize][size / chunkSize];

        heightMap[0][0] = heightMap[0][size - 1] = heightMap[size - 1][0] = heightMap[size - 1][size - 1] = 30;//r.nextInt(128);
        heightMap[size / 2 - 1][0] = 128;

        generateVerticalBorders(0, size - 1);
        generateHorizontalBorders(0, size - 1);
        generate(0, 0, size - 1, size - 1);

        for (int i = 0; i < size / chunkSize; i++) {
            for (int j = 0; j < size / chunkSize; j++) {
                chunks[i][j] = new Chunk(i, j, gameEventListener);
            }
        }

        for (int i = 0; i < chunks.length; i++) {
            for (int j = 0; j < chunks.length; j++) {
                if (i != chunks.length - 1) {
                    chunks[i][j].setNeighborChunk(chunks[i + 1][j], Direction.RIGHT);
                } else {
                    chunks[i][j].setNeighborChunk(chunks[0][j], Direction.RIGHT);
                }
                if (i != 0) {
                    chunks[i][j].setNeighborChunk(chunks[i - 1][j], Direction.LEFT);
                } else {
                    chunks[i][j].setNeighborChunk(chunks[chunks.length - 1][j], Direction.LEFT);
                }
                if (j != chunks.length - 1) {
                    chunks[i][j].setNeighborChunk(chunks[i][j + 1], Direction.DOWN);
                } else {
                    chunks[i][j].setNeighborChunk(chunks[i][0], Direction.DOWN);
                }
                if (j != 0) {
                    chunks[i][j].setNeighborChunk(chunks[i][j - 1], Direction.UP);
                } else {
                    chunks[i][j].setNeighborChunk(chunks[i][chunks.length - 1], Direction.UP);
                }
            }
        }

        for (int i = 0; i < chunks.length; i++) {
            for (int j = 0; j < chunks.length; j++) {
                Tile[][] tileSet = new Tile[chunkSize][chunkSize];
                for (int k = 0; k < chunkSize; k++) {
                    for (int l = 0; l < chunkSize; l++) {
                        tileSet[k][l] = new Tile(heightMap[i * chunkSize + k][j * chunkSize + l]);
                    }
                }
                chunks[i][j].setTiles(tileSet);
            }
        }
    }

    private void generateVerticalBorders(int x, int y) {
        int diff = y - x;
        if (diff <= 1) {
            return;
        }
        int noise = Math.min(diff, 64);

        if (heightMap[0][(x + y) / 2] == 0) {
            heightMap[size - 1][(x + y) / 2] = heightMap[0][(x + y) / 2] = (heightMap[0][x] + heightMap[0][y]) / 2 + (r.nextInt(noise) - noise / 2);//bottom
        }
        generateVerticalBorders(x, (y + x) / 2);
        generateVerticalBorders((y + x) / 2, y);
    }

    private void generateHorizontalBorders(int x, int y) {
        int diff = y - x;
        if (diff <= 1) {
            return;
        }
        int noise = Math.min(diff, 64);

        if (heightMap[(x + y) / 2][0] == 0) {
            heightMap[(x + y) / 2][size - 1] = heightMap[(x + y) / 2][0] = (heightMap[x][0] + heightMap[y][0]) / 2 + (r.nextInt(noise) - noise / 2);//bottom
        }
        generateHorizontalBorders(x, (y + x) / 2);
        generateHorizontalBorders((y + x) / 2, y);
    }

    private void generate(int i1, int i2, int i3, int i4) {
        int diff = i3 - i1;
        if (diff <= 1)
            return;
        int noise = Math.min(diff, 64);
        if (heightMap[(i3 + i1) / 2][(i4 + i2) / 2] == 0) {
            heightMap[(i3 + i1) / 2][(i4 + i2) / 2] = (heightMap[i1][i2] + heightMap[i1][i4] + heightMap[i3][i2] + heightMap[i3][i4]) / 4 + (r.nextInt(noise) - noise / 2);//center
        }
        if (heightMap[i1][(i4 + i2) / 2] == 0) {
            heightMap[i1][(i4 + i2) / 2] = (heightMap[i1][i2] + heightMap[i1][i4]) / 2 + (r.nextInt(noise) - noise / 2);//bottom
        }
        if (heightMap[i3][(i4 + i2) / 2] == 0) {
            heightMap[i3][(i4 + i2) / 2] = (heightMap[i3][i2] + heightMap[i3][i4]) / 2 + (r.nextInt(noise) - noise / 2);//top
        }
        if (heightMap[(i3 + i1) / 2][i2] == 0) {
            heightMap[(i3 + i1) / 2][i2] = (heightMap[i1][i2] + heightMap[i3][i2]) / 2 + (r.nextInt(noise) - noise / 2);//left
        }
        if (heightMap[(i3 + i1) / 2][i4] == 0) {
            heightMap[(i3 + i1) / 2][i4] = (heightMap[i1][i4] + heightMap[i3][i4]) / 2 + (r.nextInt(noise) - noise / 2);//right
        }

        generate(i1, i2, i3 - diff / 2, i4 - diff / 2);//низ лево
        generate(i1 + diff / 2, i2, i3, i4 - diff / 2);//верх лево
        generate(i1, i2 + diff / 2, i3 - diff / 2, i4);//низ право
        generate(i1 + diff / 2, i2 + diff / 2, i3, i4);//верх право

    }

    public Chunk getChunk(int x, int y) {
        return chunks[x][y];
    }

    @Override
    public void Draw(GL2 gl) {
        for (Chunk[] chunk : chunks) {
            for (int j = 0; j < chunks.length; j++) {
                    chunk[j].Draw(gl);
            }
        }

        for (Chunk[] chunk : chunks) {
            for (int j = 0; j < chunks.length; j++) {
                    chunk[j].DrawCreatures(gl);
            }
        }
    }

    @Override
    public void Tick() {
        for (Chunk[] chunk : chunks) {
            for (int i = 0; i < chunks.length; i++) {
                chunk[i].Tick();
            }
        }
    }
}
