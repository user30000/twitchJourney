package game.Map;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import game.GameEventListener;
import game.Tickable;
import game.creatures.Creature;
import game.creatures.CreatureFactory;
import graphic.Drawable;
import graphic.JoglCanvas;
import util.Direction;
import util.Prop;
import util.Rect;

import java.util.*;

public class Chunk implements Drawable, Tickable {
    private GameEventListener gameEventListener;

    private int size;
    private int reachableTiles = 0;
    private Point position;

    private Chunk upNeighbor;
    private Chunk downNeighbor;
    private Chunk rightNeighbor;
    private Chunk leftNeighbor;

    private Tile[][] tiles;

    private final java.util.Map<String, Creature> creatures;
    private final CreatureFactory creatureFactory;

    private boolean spawner = true;
    private boolean reachable = false;

    public Chunk(int x, int y, GameEventListener gameEventListener) {
        this(new Point(x, y), gameEventListener);
    }

    public Chunk(Point position, GameEventListener gameEventListener) {
        this.gameEventListener = gameEventListener;
        this.position = position;

        size = Prop.getInt("chunkSize");

        creatures = Collections.synchronizedMap(new HashMap<String, Creature>());
        creatureFactory = new CreatureFactory(null);
    }

    void setTiles(Tile[][] tileSet) {
        tiles = new Tile[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(tileSet[i], 0, tiles[i], 0, size);
            int j = 0;
            for (Tile tile : tiles[i]) {
                tile.setParentChunk(this);
                tile.setPosition(i, j);
                j++;
                if (tile.isReachable()) {
                    reachableTiles++;
                }
            }
        }

        if (reachableTiles == 0) {
            spawner = false;
            reachable = false;
        } else {
            reachable = true;
        }
    }

    public void setTilesNeighbors() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (i != tiles.length - 1) {
                    tiles[i][j].setNeighborTile(tiles[i + 1][j], Direction.RIGHT);
                } else {
                    tiles[i][j].setNeighborTile(getNeighborChunk(Direction.RIGHT).tiles[0][j], Direction.RIGHT);
                }
                if (i != 0) {
                    tiles[i][j].setNeighborTile(tiles[i - 1][j], Direction.LEFT);
                } else {
                    tiles[i][j].setNeighborTile(getNeighborChunk(Direction.LEFT).tiles[tiles.length - 1][j], Direction.LEFT);
                }
                if (j != tiles.length - 1) {
                    tiles[i][j].setNeighborTile(tiles[i][j + 1], Direction.UP);
                } else {
                    tiles[i][j].setNeighborTile(getNeighborChunk(Direction.UP).tiles[i][0], Direction.UP);
                }
                if (j != 0) {
                    tiles[i][j].setNeighborTile(tiles[i][j - 1], Direction.DOWN);
                } else {
                    tiles[i][j].setNeighborTile(getNeighborChunk(Direction.DOWN).tiles[i][tiles.length - 1], Direction.DOWN);
                }
            }
        }
    }

    public Point getPosition() {
        return position;
    }

    void setNeighborChunk(Chunk chunk, Direction direction) {
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

    public boolean isReachable() {
        return reachable;
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

    public List<Direction> getPointFreeNeighbors(Point point) {
        LinkedList<Direction> neighbors = new LinkedList<>();
        Tile tile = tiles[point.x % size][point.y % size];

        if (tile.getNeighborTile(Direction.RIGHT).isReachable()) {
            neighbors.add(Direction.RIGHT);
        }
        if (tile.getNeighborTile(Direction.UP).isReachable()) {
            neighbors.add(Direction.UP);
        }
        if (tile.getNeighborTile(Direction.LEFT).isReachable()) {
            neighbors.add(Direction.LEFT);
        }
        if (tile.getNeighborTile(Direction.DOWN).isReachable()) {
            neighbors.add(Direction.DOWN);
        }
        return neighbors;
    }

    public synchronized void creatureRoaming(String creatureName, Direction direction) {
        creatures.get(creatureName).setParentChuck(getNeighborChunk(direction));
        creatureFactory.getRoamingcreature(creatures.get(creatureName));
    }

    void resetCreatures() {
        creatureFactory.resetCreatures(creatures, this);
    }

    public List<Creature> getPlayersList() {
        List<Creature> players = new ArrayList<>();
        creatures.forEach((key, c) -> {
            if (c.isPlayer()) {
                players.add(c);
            }
        });
        return players;
    }

    public List<Creature> getCreatureList() {
        List<Creature> creatureArrayList = new ArrayList<>();
        creatures.forEach((key, c) -> {
            if (!c.isPlayer()) {
                creatureArrayList.add(c);
            }
        });
        return creatureArrayList;
    }

    @Override
    public void Draw(JoglCanvas canvas) {
        GL2 gl = null;
        try {
            gl = canvas.getGl();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (gl != null && canvas.inCameraRect(new Rect(position.x * size, position.y * size, position.x * size + size, position.y * size + size))) {
            gl.glBegin(GL.GL_TRIANGLES);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    float h = tiles[i][j].getHeight();

                    switch (tiles[i][j].getType()) {
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
                    gl.glVertex2i(position.x * size + i, position.y * size + j);

                    gl.glTexCoord2f(0.0625f, 1 - 0.0625f);
                    gl.glVertex2i(position.x * size + i, position.y * size + j - 1);

                    gl.glTexCoord2f(0, 1 - 0.0625f);
                    gl.glVertex2i(position.x * size + i - 1, position.y * size + j - 1);


                    gl.glTexCoord2f(0.0625f, 1);
                    gl.glVertex2i(position.x * size + i, position.y * size + j);

                    gl.glTexCoord2f(0, 1);
                    gl.glVertex2i(position.x * size + i - 1, position.y * size + j);

                    gl.glTexCoord2f(0, 1 - 0.0625f);
                    gl.glVertex2i(position.x * size + i - 1, position.y * size + j - 1);
                }
            }
            gl.glEnd();
        }
    }

    void DrawCreatures(JoglCanvas canvas) {
        GL2 gl = null;
        try {
            gl = canvas.getGl();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (gl != null && canvas.inCameraRect(new Rect(position.x * size, position.y * size, position.x * size + size, position.y * size + size))) {
            gl.glTranslatef(position.x * size, position.y * size, 0);
            creatures.forEach((key, c) -> c.Draw(canvas));
            gl.glTranslatef(-position.x * size, -position.y * size, 0);
        }
    }

    public Tile getRandomReachableTile() {
        int randTile = new Random().nextInt(reachableTiles);
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j].isReachable()) {
                    if (randTile == 0) {
                        return tiles[i][j];
                    }
                    randTile--;
                }
            }
        }

        return null;
    }

    @Override
    public void Tick() {
        creatures.forEach((key, c) -> c.Tick());

        creatureFactory.CleanDead(creatures, this);
        if (spawner) {
            creatureFactory.SavePopulationPlayer(creatures, this, gameEventListener);
        }
    }
}
