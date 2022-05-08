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

        Font font = new Font("SansSerif", Font.PLAIN, 36);
        renderer = new TextRenderer(font);
        window.setVisible(true);
        return this;
    }

    public static void onRender(GLDrawable drawable) {

    }

    private static void renderText(String text, int x, int y, Color color, boolean shadow) {
        renderer.beginRendering(1000, 600);
        renderer.setSmoothing(true);
        if (shadow) {
            renderer.setColor(new Color(0, 0, 0, 0.5f));
            renderer.draw(text, x - 1, 601 - y);
        }
        renderer.setColor(color);
        renderer.draw(text, x, 600 - y);
        renderer.endRendering();
    }
}
