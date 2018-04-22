package game.Map;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import game.GameEventListener;
import game.Tickable;
import graphic.Drawable;
import graphic.JoglCanvas;
import util.Prop;

import java.util.Random;

public class Map implements Drawable, Tickable {
    private GameEventListener gameEventListener;

    private int size;
    private int chunkSize;
    //private Chunk[][] chunks;
    private int[][] heightMap;
    private Tile[][] tileSet;

    private Random r;

    public Map(int size, GameEventListener gameEventListener) {
        this.gameEventListener = gameEventListener;

        this.size = size;
        chunkSize = Prop.getInt("chunkSize");
        r = new Random(500);

        heightMap = new int[size][size];
        tileSet = new Tile[size][size];
        //chunks = new Chunk[size / chunkSize][size / chunkSize];

        heightMap[0][0] = heightMap[0][size - 1] = heightMap[size - 1][0] = heightMap[size - 1][size - 1] = 30;//r.nextInt(128);
        heightMap[size / 2 - 1][0] = 128;

        generateVerticalBorders(0, size - 1);
        generateHorizontalBorders(0, size - 1);
        generate(0, 0, size - 1, size - 1);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tileSet[i][j] = new Tile(heightMap[i][j]);
            }
        }

        /*for (int i = 0; i < size / chunkSize; i++) {
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
                    chunks[i][j].setNeighborChunk(chunks[i][j + 1], Direction.UP);
                } else {
                    chunks[i][j].setNeighborChunk(chunks[i][0], Direction.UP);
                }
                if (j != 0) {
                    chunks[i][j].setNeighborChunk(chunks[i][j - 1], Direction.DOWN);
                } else {
                    chunks[i][j].setNeighborChunk(chunks[i][chunks.length - 1], Direction.DOWN);
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
        for (int i = 0; i < chunks.length; i++) {
            for (int j = 0; j < chunks.length; j++) {
                chunks[i][j].setTilesNeighbors();
            }
        }*/
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
        generateHorizontalBorders(x, (x + y) / 2);
        generateHorizontalBorders((x + y) / 2, y);
    }

    private void generate(int i1, int i2, int i3, int i4) {
        int diff = i3 - i1;
        if (diff <= 1)
            return;
        int noise = Math.min(diff, 64);
        if (heightMap[(i1 + i3) / 2][(i2 + i4) / 2] == 0) {
            heightMap[(i1 + i3) / 2][(i2 + i4) / 2] = (heightMap[i1][i2] + heightMap[i1][i4] + heightMap[i3][i2] + heightMap[i3][i4]) / 4 + (r.nextInt(noise) - noise / 2);//center
        }
        if (heightMap[i1][(i2 + i4) / 2] == 0) {
            heightMap[i1][(i2 + i4) / 2] = (heightMap[i1][i2] + heightMap[i1][i4]) / 2 + (r.nextInt(noise) - noise / 2);//bottom
        }
        if (heightMap[i3][(i2 + i4) / 2] == 0) {
            heightMap[i3][(i2 + i4) / 2] = (heightMap[i3][i2] + heightMap[i3][i4]) / 2 + (r.nextInt(noise) - noise / 2);//top
        }
        if (heightMap[(i1 + i3) / 2][i2] == 0) {
            heightMap[(i1 + i3) / 2][i2] = (heightMap[i1][i2] + heightMap[i3][i2]) / 2 + (r.nextInt(noise) - noise / 2);//left
        }
        if (heightMap[(i1 + i3) / 2][i4] == 0) {
            heightMap[(i1 + i3) / 2][i4] = (heightMap[i1][i4] + heightMap[i3][i4]) / 2 + (r.nextInt(noise) - noise / 2);//right
        }

        generate(i1, i2, i3 - diff / 2, i4 - diff / 2);//низ лево
        generate(i1 + diff / 2, i2, i3, i4 - diff / 2);//верх лево
        generate(i1, i2 + diff / 2, i3 - diff / 2, i4);//низ право
        generate(i1 + diff / 2, i2 + diff / 2, i3, i4);//верх право

    }

    /*public Chunk getChunk(int x, int y) {
        return chunks[x][y];
    }*/

    @Override
    public void Draw(JoglCanvas canvas) {
        GL2 gl = null;
        try {
            gl = canvas.getGl();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (gl != null) {// && canvas.inCameraRect(new Rect(position.x * size, position.y * size, position.x * size + size, position.y * size + size))) {
            gl.glBegin(GL.GL_TRIANGLES);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    float h = tileSet[i][j].getHeight();

                    switch (tileSet[i][j].getType()) {
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
                    gl.glVertex2i(i, j);

                    gl.glTexCoord2f(0.0625f, 1 - 0.0625f);
                    gl.glVertex2i(i, j - 1);

                    gl.glTexCoord2f(0, 1 - 0.0625f);
                    gl.glVertex2i(i - 1, j - 1);


                    gl.glTexCoord2f(0.0625f, 1);
                    gl.glVertex2i(i, j);

                    gl.glTexCoord2f(0, 1);
                    gl.glVertex2i(i - 1, j);

                    gl.glTexCoord2f(0, 1 - 0.0625f);
                    gl.glVertex2i(i - 1, j - 1);
                }
            }
            gl.glEnd();
        }

        /*for (Chunk[] chunk : chunks) {
            for (int j = 0; j < chunks.length; j++) {
                chunk[j].Draw(canvas);
            }
        }

        for (Chunk[] chunk : chunks) {
            for (int j = 0; j < chunks.length; j++) {
                chunk[j].DrawCreatures(canvas);
            }
        }*/
    }

    @Override
    public void Tick() {
        /*for (Chunk[] chunk : chunks) {
            for (int i = 0; i < chunks.length; i++) {
                chunk[i].Tick();
            }
        }

        for (Chunk[] chunk : chunks) {
            for (int i = 0; i < chunks.length; i++) {
                chunk[i].resetCreatures();
            }
        }
        */
    }
}
