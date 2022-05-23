package me.pelle.wordlewinner.util;


import com.jogamp.newt.event.WindowEvent;
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
        window.setTitle("WordleWinner");
        window.addGLEventListener(new WindowEventListener());
        window.addKeyListener(new InputListener());
        FPSAnimator fpsAnimator = new FPSAnimator(window, 60);
        fpsAnimator.start();

        window.setVisible(true);
        return this;
    }


    public static void onRender(GLAutoDrawable drawable) {
        drawBestWords();
        drawLines();
    }

    private static void drawBestWords() {
        int i = 1;
        RenderUtil.drawRect(0, 0, 168, 600, new Color(0.15f, 0.15f, 0.15f));
        for (String word : WordleWinner.bestWords) {
            RenderUtil.drawText(i + ":", 10, 0 + (i * 30), Color.WHITE, false, 27);
            RenderUtil.drawText(word, 55, 0 + (i * 30), Color.WHITE, false, 27);
            i++;
            if (i * 30 > 650) break;
        }
    }

    private static void drawLines() {
        int y = 4;
        for (InputLine line : WordleWinner.lines) {
            int x = 174;
            for (WordleLetter wordleLetter : line.letters) {
                Color color;
                switch (wordleLetter.state) {
                    case GREEN:
                        color = new Color(0x6aaa64);
                        break;
                    case YELLOW:
                        color = new Color(0xc9b458);
                        break;
                    case TYPED:
                        color = new Color(0x717171);
                        break;
                    default:
                        color = new Color(0x454545);
                }
                RenderUtil.drawRect(x, y, x + 92, y + 92, color);
                if (wordleLetter.state == WordleLetter.State.BLANK || wordleLetter.state == WordleLetter.State.TYPED) {
                    RenderUtil.drawRect(x + 2, y + 2, x + 90, y + 90, new Color(0.15f, 0.15f, 0.15f));
                }
                if (wordleLetter.state != WordleLetter.State.BLANK)
                    RenderUtil.drawText(wordleLetter.letter, x + 17, y + 72, Color.WHITE, false, 80);
                x += 100;
            }
            y += 100;
        }
    }
}