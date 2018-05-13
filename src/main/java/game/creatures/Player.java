package game.creatures;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import game.Game;
import game.Map.Chunk;
import game.target;
import graphic.JoglCanvas;
import graphic.TexturePool;

import java.util.List;
import java.util.Random;

public class Player extends Creature implements target {

    private String NickName;

    private int Level;
    private int Expirience;
    private int ExpForlevel = 100;

    private int Strength = 10;
    private int Agility;
    private int Stamina;
    private int Intellect;
    private int Will;
    private int Luck = 1;

    private int body;

    private Player(int MaxHealth, String nickName, Chunk parent) {
        super(MaxHealth, "Player", nickName);//, parent);
        NickName = nickName.toLowerCase();
        Level = 1;

        Armor = 2;

        body = new Random().nextInt(2);
    }

    public Player(String nickName, Chunk parent) {
        this(100, nickName, parent);
    }

    public int getHealth() {
        return Health;
    }

    public String Yell() {
        return "Меня зовут " + NickName + " и у меня " + Health + " здоровья";
    }

    public void addExp(int exp) {
        Expirience += exp;
        if (Expirience >= ExpForlevel) {
            Level++;
            Expirience = Expirience - ExpForlevel;
            ExpForlevel = ExpForlevel * ((Level + 1) / Level);
        }
    }

    public String getNickName() {
        return NickName;
    }

    @Override
    public void Tick() {
        super.Tick();
        //getDamage(new Random().nextInt(5));
    }

    @Override
    public Do IdleState() {
        if (gameEventListener == null)
            return Do.nothing();

        List<Creature> players = ((Game) gameEventListener).getCreatureList();//parentChuck.getCreatureList();

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

    @Override
    public Do AttackState() {
        return super.AttackState();
    }

    @Override
    public Do RoamState() {
        if (new Random().nextInt(1000) == 0) {
            gameEventListener.GameEvent(this, "focusCreature");
        }

        return super.RoamState();
    }

    @Override
    public void Draw(JoglCanvas canvas) {
        GL2 gl = null;
        try {
            gl = canvas.getGl();
        } catch (Exception e) {
            e.printStackTrace();
        }
        gl.glColor3f(1f, 1f, 0f);
        //Texture t = TexturePool.getInstance().getTexture("bodies");
        Texture t = TexturePool.getInstance().getTexture("bodies");

        if (t != null) {
            gl.glBindTexture(GL.GL_TEXTURE_2D, t.getTextureObject());
            gl.glEnable(GL.GL_BLEND);
            //gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
            gl.glBlendFuncSeparate(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA, GL.GL_ONE, GL.GL_ONE_MINUS_SRC_ALPHA);
        }

        gl.glBegin(GL2.GL_POLYGON);
        gl.glEnable(GL2.GL_TEXTURE_2D);
        //drawHead
        gl.glColor3f(1f, 0f, 0f);
        gl.glTexCoord2f(0.5f, 0.5f);
        gl.glVertex3i(position.x, position.y, 1);

        gl.glColor3f(0f, 0f, 0f);
        gl.glTexCoord2f(0.5f, 0);
        gl.glVertex3i(position.x, position.y - 1, 1);

        gl.glColor3f(1f, 1f, 0f);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3i(position.x - 1, position.y - 1, 1);

        gl.glColor3f(1f, 0f, 1f);
        gl.glTexCoord2f(0, 0.5f);
        gl.glVertex3i(position.x - 1, position.y, 1);

        gl.glEnd();

        gl.glColor3f(1f, 0f, 0f);
        gl.glBegin(GL2.GL_POLYGON);
        //gl.glEnable(GL2.GL_TEXTURE_2D);

        gl.glTexCoord2f((body + 1) * 0.5f, 1);
        gl.glVertex3i(position.x, position.y, 1);

        gl.glTexCoord2f((body + 1) * 0.5f, 0.5f);
        gl.glVertex3i(position.x, position.y - 1, 1);

        gl.glTexCoord2f((body) * 0.5f, 0.5f);
        gl.glVertex3i(position.x - 1, position.y - 1, 1);

        gl.glTexCoord2f((body) * 0.5f, 1);
        gl.glVertex3i(position.x - 1, position.y, 1);
        gl.glEnd();
    }

    @Override
    public void hit(int dmg) {
        getDamage(dmg);
    }
}
