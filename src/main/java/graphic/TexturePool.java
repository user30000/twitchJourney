package graphic;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class TexturePool {
    private static TexturePool instance;

    private TexturePool() {
        pool = new HashMap<>();
        texturePath = new HashMap<>();
    }

    private HashMap<String, Texture> pool;
    private HashMap<String, String> texturePath;

    public static TexturePool getInstance() {
        if (instance == null) {
            instance = new TexturePool();
        }
        return instance;
    }

    public void declareTexture(String filePath, String textureName) {
        texturePath.put(textureName, filePath);
    }

    void loadTexture(GL2 gl) {
        texturePath.forEach((textureName, texturePath) -> {
            File file = new File(texturePath);
            try {
                TextureData data = TextureIO.newTextureData(gl.getGLProfile(), file, false, "png");
                if (!pool.containsKey(textureName)) {
                    pool.put(textureName, TextureIO.newTexture(data));
                }
                //return TextureIO.newTexture(data);
            } catch (IOException exc) {
                exc.printStackTrace();
                System.exit(1);
            }
        });
        //return null;
    }

    public Texture getTexture(String textureName) {
        if (pool.containsKey(textureName)) {
            return pool.get(textureName);
        }

        return null;
    }
}
