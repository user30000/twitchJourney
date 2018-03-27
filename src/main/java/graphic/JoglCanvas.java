package graphic;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import game.GameEventListener;

import java.util.ArrayList;

public class JoglCanvas extends GLCanvas implements GLEventListener, graphListener {
    private GL2 gl;
    private GLU glu;

    ArrayList<myVertex> vertexes;
    private GameEventListener gameEventListener;
    static float red = 0;
    static float blue = 1;
    static float green = 0;

    public JoglCanvas(GLCapabilities capabilities, int width, int height) {
        super(capabilities);
        setSize(width, height);
        addGLEventListener(this);

        vertexes = new ArrayList<>();
        vertexes.add(new myVertex(5, 5));
        vertexes.add(new myVertex(5, -5));
        vertexes.add(new myVertex(-5, 5));
        vertexes.add(new myVertex(-5, -5));
    }

    public void init(GLAutoDrawable drawable) {
        gl = (GL2) drawable.getGL();
        drawable.setGL(gl);

        glu = new GLU();

        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        gl.glViewport(0, 0, 500, 300);
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(-1, 64, -1.0, 64);

        gl.glClearDepth(5.0f); // Set depth's clear-value to farthest
        gl.glEnable(GL2.GL_DEPTH_TEST); // Enables depth-buffer for hidden
        // surface removal
        //gl.glDepthFunc(GL2.GL_LEQUAL); // The type of depth testing to do
        //gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);   // nice perspective view, avoid texture distortion.
        gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
        //gl.glShadeModel(GL2.GL_SMOOTH); // Enable smooth shading of color
        //gl.glDisable(GL2.GL_DITHER); // Disable dithering for better performance
        gl.glEnable(GL2.GL_TEXTURE_2D);

        TexturePool.getInstance().loadTexture(gl);

        // Start animator (which should be a field).
        FPSAnimator animator = new FPSAnimator(drawable, 60);
        animator.start();

        //gl.glBitmap();
    }

    public void dispose(GLAutoDrawable glAutoDrawable) {
        GL gl = glAutoDrawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();
        gl.glViewport(0, 0, width, height);
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void display(GLAutoDrawable drawable) {
        gl = (GL2) drawable.getGL();

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        Texture t = TexturePool.getInstance().getTexture("mapTexture");
        if (t != null) {
            gl.glBindTexture(GL.GL_TEXTURE_2D, t.getTextureObject());
        }

        //gl.glBegin(GL.GL_TRIANGLES);
        if (gameEventListener != null) {
            gameEventListener.GameEvent(gl, "");
        }
        //gl.glEnd();
    }

    private void setCamera(GL2 gl, GLU glu, float distance) {
        // Change to projection matrix.
        gl.glMatrixMode(GLMatrixFunc.GL_MATRIX_MODE);
        gl.glLoadIdentity();

        // Perspective.
        float widthHeightRatio = (float) getWidth() / (float) getHeight();
        glu.gluPerspective(45, 1, 1, 100);
        glu.gluLookAt(0, 0, distance, 0, 0, 0, 0, 1, 0);

        // Change back to model view matrix.
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public GL2 getGl() throws Exception {
        if (gl != null)
            return gl;
        else throw new Exception("GL не был инициализирован");
    }

    public void setGameEventListener(GameEventListener listener) {
        this.gameEventListener = listener;
    }

    //@Override
    public void AwesomeEvent(String command) {
        red = 0;
        blue = 0;
        green = 0;
        switch (command) {
            case "red":
                red = 1f;
                break;
            case "blue":
                blue = 1f;
                break;
            case "green":
                green = 1f;
                break;
        }
    }
}