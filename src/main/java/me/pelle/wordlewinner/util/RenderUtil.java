package me.pelle.wordlewinner.util;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2GL3;
import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.*;

public class RenderUtil {
    static GL2 gl;
    static Font font = new Font("SansSerif", Font.PLAIN, 100);
    static TextRenderer renderer = new TextRenderer(font);
    public static void drawRect(int x, int y, int xMax, int yMax, Color color) {
        gl.glPushMatrix();
        gl.glBegin(GL2GL3.GL_QUADS);
        gl.glColor4d(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, color.getAlpha()/255f);

        gl.glVertex2i(x, y);
        gl.glVertex2i(x, yMax);
        gl.glVertex2i(xMax, yMax);
        gl.glVertex2i(xMax, y);

        gl.glEnd();
        gl.glPopMatrix();
    }

    public static void drawText(String text, int x, int y, Color color, boolean shadow, double scale) {
        double factor = 100/scale;
        renderer.beginRendering((int) (Window.window.getWidth() * factor), (int) (630 * factor));
        renderer.setSmoothing(true);
        if (shadow) {
            renderer.setColor(new Color(0, 0, 0, 0.5f));
            renderer.draw(text, (int) ((x - 1) * factor), (int) ((631  - y) * factor));
        }
        renderer.setColor(color);
        renderer.draw(text, (int) (x * factor), (int) ((631  - y) * factor));
        renderer.endRendering();
    }
}
