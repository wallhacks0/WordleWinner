package me.pelle.wordlewinner.util;


import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.*;

public class Window {
    static GLWindow window;

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

        window.setVisible(true);
        return this;
    }



    public static void onRender(GLAutoDrawable drawable) {
        drawBestWords();
    }

    private static void drawBestWords() {
        int i = 1;
        RenderUtil.drawRect(0, 0, 170, 600, Color.black);
        for (String word : WordleWinner.bestWords) {
            RenderUtil.drawText(i + ":", 10, 0 + (i*30), Color.WHITE, false, 27);
            RenderUtil.drawText(word, 55, 0 + (i*30), Color.WHITE, false, 27);
            i++;
            if (i*30 > 650) break;
        }
    }


}
