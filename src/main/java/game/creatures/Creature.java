package game.creatures;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import game.Game;
import game.GameEventListener;
import game.Tickable;
import game.target;
import graphic.Drawable;
import graphic.TexturePool;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Creature implements Tickable, Drawable, target {
    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void hit(int dmg) {

    }

    protected enum machineState {IDLE, ATTACK, MOVE}

    private machineState State = machineState.IDLE;

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

    protected target Target = null;

    protected String Type;
    protected int Age = 0;
    protected boolean dead = false;

    public Creature(int MaxHealth, String Type, String NickName){
        this.MaxHealth = MaxHealth;
        this.Health = MaxHealth;
        this.Type = Type;
        this.name = NickName;

        position = new Point();

        position.x = 30 + new Random().nextInt(30) - 15;
        position.y = 30 + new Random().nextInt(30) - 15;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setTextureName(String textureName){
        this.textureName = textureName;
    }

    public String getName(){
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

    public void getDamage(int dmg){
        int incomingDmg = Math.max(1, dmg - Armor);
        Health -= incomingDmg;

        if(Health <= 0){
            die();
        }
    }

    public int attack(){
        if(Target == null){
            State = machineState.IDLE;
        }

        int dmg = 0;
        if (Target.getPosition().distance(position) > 10) {
            Target = null;
            State = machineState.IDLE;
        }
        else if (Target.getPosition().distance(position) > 1){
            Roam();
        }
        else {
            dmg = Strength + new Random().nextInt(Luck);
            Target.hit(dmg);

            if(((Player)Target).isDead()){
                Target = null;
                State = machineState.IDLE;
            }
        }

        return dmg;
    }

    public void die(){
        dead = true;
        gameEventListener = null;
    }

    public boolean isDead(){
        return dead;
    }

    @Override
    public void Tick() {
        this.Age++;
        switch (State){
            case IDLE:
                Idle();
                break;
            case MOVE:
                Roam();
                break;
            case ATTACK:
                attack();
                break;
        }
        //Roam();
    }

    private void SearchTarget(){
        if(gameEventListener == null)
            return;

        ArrayList<Player> players = new ArrayList(((Game)gameEventListener).getPlayersList());

        if(Target == null) {
            for (Player p : players) {
                int distance = ((int) p.getPosition().distance(position));
                if (distance < sightRange){
                    Target = p;
                    State = machineState.ATTACK;
                    return;
                }
            }
        }/* else{
            State = machineState.IDLE;
        }*/
        State = machineState.MOVE;
    }

    private void Idle(){
        getDamage(1);
        SearchTarget();
    }

    private void Roam(){
        switch (new Random().nextInt(4)){
            case 0:
                position.x ++;
                position.y ++;
                break;
            case 1:
                position.x --;
                position.y ++;
                break;
            case 2:
                position.x ++;
                position.y --;
                break;
            case 3:
                position.x --;
                position.y --;
                break;
        }
        if(Target == null) {
            State = machineState.IDLE;
        }
    }

    public void setGameEventListener(GameEventListener listener){
        gameEventListener = listener;
    }

    @Override
    public void Draw(GL2 gl) {
        gl.glColor3f(1f, 1f, 1f);
        Texture t =  TexturePool.getInstance().getTexture(textureName);
        if(t!=null) {
            gl.glBindTexture(GL.GL_TEXTURE_2D,t.getTextureObject());
            gl.glEnable(GL.GL_BLEND);
            gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        }
        gl.glBegin(GL2.GL_POLYGON);
        //gl.glEnable(GL2.GL_TEXTURE_2D);

        gl.glTexCoord2f(1,1);
        gl.glVertex3f(position.x - 0.25f, position.y - 0.25f, 1);

        gl.glTexCoord2f(1,0);
        gl.glVertex3f(position.x - 0.25f, position.y - 0.75f, 1);

        gl.glTexCoord2f(0,0);
        gl.glVertex3f(position.x - 0.75f, position.y - 0.75f, 1);

        gl.glTexCoord2f(0,1);
        gl.glVertex3f(position.x - 0.75f, position.y - 0.25f, 1);

        gl.glEnd();
    }
}
