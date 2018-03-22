package game.Map;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import graphic.Drawable;

import java.util.Random;

public class Map implements Drawable {
    private int size;
    private Tile[][] tiles;
    private int[][] heightMap;

    Random r;

    public Map(int size) {
        this.size = size;

         r = new Random();

        heightMap = new int[size][size];
        heightMap[0][0] = 0;//r.nextInt(128);
        heightMap[0][size - 1] = 30;//r.nextInt(128);
        heightMap[size - 1][0] = 30;//r.nextInt(128);
        heightMap[size - 1][size - 1] = 30;//r.nextInt(128);
        //heightMap[size/2][size/2] = 128;

        generate(0, 0, size - 1, size - 1);

        tiles = new Tile[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Tile t = new Tile(heightMap[i][j]);
                tiles[i][j] = t;
            }
        }
    }

    private void generate(int i1, int i2, int i3, int i4) {
        int diff = i3 - i1;
        if(diff <= 1)
            return;
        int noise = Math.min(diff, 64);
        if (heightMap[(i3 + i1) / 2][(i4 + i2) / 2] == 0) {
            heightMap[(i3 + i1) / 2][(i4 + i2) / 2] = (heightMap[i1][i2] + heightMap[i1][i4] + heightMap[i3][i2] + heightMap[i3][i4]) / 4 + (new Random().nextInt(noise) - noise/2);//center
        }
        if (heightMap[i1][(i4 + i2) / 2] == 0) {
            heightMap[i1][(i4 + i2) / 2] = (heightMap[i1][i2] + heightMap[i1][i4]) / 2 + (r.nextInt(noise) - noise/2);//bottom
        }
        if (heightMap[i3][(i4 + i2) / 2] == 0) {
            heightMap[i3][(i4 + i2) / 2] = (heightMap[i3][i2] + heightMap[i3][i4]) / 2 + (r.nextInt(noise) - noise/2);//top
        }
        if (heightMap[(i3 + i1) / 2][i2] == 0) {
            heightMap[(i3 + i1) / 2][i2] = (heightMap[i1][i2] + heightMap[i3][i2]) / 2 + (r.nextInt(noise) - noise/2);//left
        }
        if (heightMap[(i3 + i1) / 2][i4] == 0) {
            heightMap[(i3 + i1) / 2][i4] = (heightMap[i1][i4] + heightMap[i3][i4]) / 2 + (r.nextInt(noise) - noise/2);//right
        }

        generate(i1, i2, i3 - diff/2, i4 - diff/2);//низ лево
        generate(i1 + diff/2, i2, i3, i4 - diff/2);//верх лево
        generate(i1, i2 + diff/2, i3 - diff/2, i4);//низ право
        generate(i1 + diff/2, i2 + diff/2, i3, i4);//верх право

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
}
