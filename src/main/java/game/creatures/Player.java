package game.creatures;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import game.target;
import graphic.TexturePool;

import java.awt.*;

public class Player extends Creature implements target {

    private String NickName;

    private int Level;
    private int Expirience;
    private int ExpForlevel = 100;

    private Player(int MaxHealth, String nickName) {
        super(MaxHealth, "Player", nickName);
        NickName = nickName.toLowerCase();
        Level = 1;
    }

    public Player(String nickName){
        this(100, nickName);
    }

    public int getHealth(){
        return Health;
    }

    public String Yell(){
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

    public String getNickName(){
        return NickName;
    }

    @Override
    public void Tick() {
        //getDamage(new Random().nextInt(5));
    }

    @Override
    public void Draw(GL2 gl){
        gl.glColor3f(1f, 1f, 1f);
        Texture t =  TexturePool.getInstance().getTexture("warrior");
        if(t!=null) {
            gl.glBindTexture(GL.GL_TEXTURE_2D,t.getTextureObject());
            gl.glEnable(GL.GL_BLEND);
            gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        }
        gl.glBegin(GL2.GL_POLYGON);
        //gl.glEnable(GL2.GL_TEXTURE_2D);

        gl.glTexCoord2f(1,1);
        gl.glVertex3i(position.x, position.y, 1);

        gl.glTexCoord2f(1,0);
        gl.glVertex3i(position.x, position.y - 1, 1);

        gl.glTexCoord2f(0,0);
        gl.glVertex3i(position.x - 1, position.y - 1, 1);

        gl.glTexCoord2f(0,1);
        gl.glVertex3i(position.x - 1, position.y, 1);

        gl.glEnd();
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void hit(int dmg) {
        getDamage(dmg);
    }
}
