import chatBot.TwitchChat;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import game.Game;
import graphic.JoglCanvas;
import graphic.TexturePool;
import org.jibble.pircbot.IrcException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class twitchJourney {
    public static void main(String[] args) {
        try {
            TexturePool.getInstance().declareTexture("D:\\projects\\program\\DR07C.png", "mapTexture");
            TexturePool.getInstance().declareTexture("D:\\projects\\program\\warrior_1.png", "warrior");
            TexturePool.getInstance().declareTexture("D:\\projects\\program\\zombie_1.png", "zombie");
        }catch (Exception e){
            System.out.println(e);
        }

        GLProfile glprofile = GLProfile.getDefault();
        GLCapabilities glcapabilities = new GLCapabilities(glprofile);

        GL2 gl = null;

        JoglCanvas canvas = new JoglCanvas(glcapabilities, 800, 500);
        JFrame frame = new JFrame("Mini JOGL Demo (breed)");
        frame.getContentPane().add(canvas, BorderLayout.CENTER);
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        canvas.requestFocus();

        TwitchChat chatConnector = new TwitchChat("user30000");
        chatConnector.log(chatConnector.getNick());
        chatConnector.log(String.valueOf(chatConnector.isConnected()));

        try {
            //chatConnector
            chatConnector.connect("irc.chat.twitch.tv", 6667, Prop.getProp("oauth"));
        } catch (IOException | IrcException e) {
            e.printStackTrace();
        }

        chatConnector.joinChannel("#user30000");

        Game game = new Game(chatConnector);
        game.setgListener(canvas);
        chatConnector.setGameEventListener(game);
        canvas.setGameEventListener(game);

        try {
            gl = canvas.getGl();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread gameThread = new Thread(game);
        gameThread.start();
    }
}
