package game.creatures;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import game.Game;
import game.GameEventListener;
import game.Map.Point;
import game.Map.Tile;
import game.Tickable;
import game.target;
import graphic.Drawable;
import graphic.TexturePool;
import util.FunctionalFiniteStateMachine;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Creature extends FunctionalFiniteStateMachine implements Tickable, Drawable, target {
    private Random r;

    protected int sightRange = 3;

    protected Point position;

    protected String name;
    protected String textureName;

    private int MaxHealth;
    protected int Health;

    private int Armor = 0;

    private int Strength = 3;
    private int Agility;
    private int Stamina;
    private int Intellect;
    private int Will;
    private int Luck = 1;

    private GameEventListener gameEventListener;

    private List daWay;

    protected target Target = null;

    protected String Type;
    protected int Age = 0;
    protected boolean dead = false;

    public Creature(int MaxHealth, String Type, String NickName) {
        super("IdleState");
        this.MaxHealth = MaxHealth;
        this.Health = MaxHealth;
        this.Type = Type;
        this.name = NickName;
        this.r = new Random();

        position = new Point(30 + r.nextInt(30) - 15, 30 + r.nextInt(30) - 15);
    }

    public void setTextureName(String textureName) {
        this.textureName = textureName;
    }

    public String getName() {
        return name;
    }

    public boolean isPlayer() {
        return this.Type.equals("Player");
    }

    public String toFullString() {
        return this.Type + " " + this.name + " в возрасте " + this.Age + " лет";
    }

    public String toString() {
        return this.Type + " " + this.name;
    }

    public void getDamage(int dmg) {
        int incomingDmg = Math.max(1, dmg - Armor);
        Health -= incomingDmg;

        if (Health <= 0) {
            die();
        }
    }

    public void die() {
        dead = true;
        gameEventListener = null;
    }

    public boolean isDead() {
        return dead;
    }

    @Override
    public void Tick() {
        this.Age++;
        super.step();
    }

    public Do IdleState() {
        getDamage(1);
        if (gameEventListener == null)
            return Do.nothing();

        List<Creature> players = ((Game) gameEventListener).getPlayersList();

        if (Target == null) {
            for (Creature p : players) {
                int distance = ((int) p.getPosition().distance(position));
                if (distance < sightRange) {
                    Target = p;
                    return Do.swap_to("AttackState");
                }
            }
        }
        return Do.swap_to("RoamState");
    }

    public Do RoamState() {
        if (r.nextBoolean()) {
            position.right();
        } else {
            position.left();
        }

        if (r.nextBoolean()) {
            position.up();
        } else {
            position.down();
        }

        if (Target == null) {
            return Do.swap_to("IdleState");
        } else {
            return Do.nothing();
        }
    }

    public Do AttackState() {
        int dmg = 0;
        if (Target == null || Target.getPosition().distance(position) > 10) {
            Target = null;
            return Do.swap_to("IdleState");
        }

        if (Target.getPosition().distance(position) > 1) {
            return Do.swap_and_do("RoamState");
        }

        dmg = Strength + r.nextInt(Luck);
        Target.hit(dmg);

        if (((Player) Target).isDead()) {
            Target = null;
        }

        return Do.swap_to("IdleState");
    }


    public void setGameEventListener(GameEventListener listener) {
        gameEventListener = listener;
    }

    public void setDaWay(List way) {
        if (way == null)
            return;
        daWay = new LinkedList(way);
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void hit(int dmg) {
    }

    @Override
    public void Draw(GL2 gl) {
        gl.glColor3f(1f, 1f, 1f);
        Texture t = TexturePool.getInstance().getTexture(textureName);
        if (t != null) {
            gl.glBindTexture(GL.GL_TEXTURE_2D, t.getTextureObject());
            gl.glEnable(GL.GL_BLEND);
            gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        }
        gl.glBegin(GL2.GL_POLYGON);
        //gl.glEnable(GL2.GL_TEXTURE_2D);

        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(position.x - 0.25f, position.y - 0.25f, 1);

        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(position.x - 0.25f, position.y - 0.75f, 1);

        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(position.x - 0.75f, position.y - 0.75f, 1);

        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(position.x - 0.75f, position.y - 0.25f, 1);

        gl.glEnd();

        if (daWay != null) {
            gl.glFlush();
            gl.glBegin(GL2.GL_LINE_STRIP);
            gl.glColor3f(1, 0, 0);

            for (Object tile : daWay) {
                Tile b = (Tile) tile;
                gl.glVertex3i(b.posX, b.posY, 3);
            }
            gl.glEnd();
        }
    }
}
