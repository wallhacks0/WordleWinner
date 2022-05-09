package me.pelle.wordlewinner.util;


import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.TextRenderer;

import javax.swing.*;
import java.awt.*;

public class Window {
    static GLWindow window;
    static TextRenderer renderer;
    public Window init() {
        GLProfile.initSingleton();
        GLProfile profile = GLProfile.getDefault();
        GLCapabilities capabilities = new GLCapabilities(profile);
        window = GLWindow.create(capabilities);
        window.setSize(1000, 600);
        window.setTitle("Drugs");
        window.addGLEventListener(new WindowEventListener());

        FPSAnimator fpsAnimator = new FPSAnimator(window, 60);
        fpsAnimator.start();

        Font font = new Font("SansSerif", Font.PLAIN, 100);
        renderer = new TextRenderer(font);
        window.setVisible(true);
        return this;
    }

    public static void onRender(GLAutoDrawable drawable) {
        renderText("hallo", 900, 560, Color.BLACK, false, 60);
    }

    private static void renderText(String text, int x, int y, Color color, boolean shadow, double scale) {
        double factor = 100/scale;
        renderer.beginRendering((int) (1000 * factor), (int) (600 * factor));
        renderer.setSmoothing(true);
        if (shadow) {
            renderer.setColor(new Color(0, 0, 0, 0.5f));
            renderer.draw(text, (int) ((x - 1) * factor), (int) ((601  - y) * factor));
        }
        renderer.setColor(color);
        renderer.draw(text, (int) (x * factor), (int) ((601  - y) * factor));
        renderer.endRendering();
    }
}
