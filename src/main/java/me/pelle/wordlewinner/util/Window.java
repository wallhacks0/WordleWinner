package me.pelle.wordlewinner.util;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;

import java.awt.*;

public class Window {
    public static boolean click;
    static GLWindow window;
    private static int mouseX;
    private static int mouseY;

    public static void updateMouse(int x, int y) {
        mouseX = x;
        mouseY = y;
    }

    public static void onRender(GLAutoDrawable drawable) {
        drawBestWords();
        drawLines();
        drawWordsLeft();
        drawProgress();
        click = false;
    }

    private static void drawProgress() {
        int offset = (int) (window.getWidth() * WordleWinner.algThread.progress);
        boolean done = offset == window.getWidth();
        if (!done)
            RenderUtil.drawRect(0, 601, window.getWidth(), 630, new Color(0.11f, 0.11f, 0.11f));
        RenderUtil.drawRect(0, 601, offset, 630, done ? new Color(0x6aaa64) : new Color(0xc9b458));
        if (done)
            RenderUtil.drawText("Done!", 10, 622, Color.WHITE, false, 18);
        else
            RenderUtil.drawText("Computing.. " + (int) (WordleWinner.algThread.progress*100) + "%",  10, 622, Color.WHITE, false, 18);
    }

    private static void drawBestWords() {
        int i = 1;
        RenderUtil.drawRect(0, 0, 170, 30, new Color(0.11f, 0.11f, 0.11f));
        RenderUtil.drawRect(0, 30, 170, 601, new Color(0.13f, 0.13f, 0.13f));
        RenderUtil.drawText("Best Guesses:", 10, 23, Color.white, false, 23);
        for (String word : WordleWinner.bestWords) {
            RenderUtil.drawText(i + ":", 10, 28 + (i * 28), Color.WHITE, false, 27);
            RenderUtil.drawText(word, 55, 28 + (i * 28), Color.WHITE, false, 27);
            i++;
            if (i * 30 > 650) break;
        }
    }

    private static void drawWordsLeft() {
        int i = 1;
        RenderUtil.drawRect(670, 0, 920, 30, new Color(0.11f, 0.11f, 0.11f));
        RenderUtil.drawRect(670, 30, 920, 601, new Color(0.13f, 0.13f, 0.13f));
        RenderUtil.drawText("Words Left: (" + WordleWinner.wordsLeftForRender.size() + ")", 690, 23, Color.white, false, 23);
        for (String word : WordleWinner.wordsLeftForRender) {
            RenderUtil.drawText(i + ":", 680, 28 + (i * 28), Color.WHITE, false, 27);
            RenderUtil.drawText(word, 725, 28 + (i * 28), Color.WHITE, false, 27);
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
                if (wordleLetter.state != WordleLetter.State.BLANK) {
                    RenderUtil.drawText(wordleLetter.letter, x + 17, y + 72, Color.WHITE, false, 80);
                    if (click && WordleWinner.activeLine == line) {
                        if (mouseX > x && mouseX < x + 92 && mouseY > y && mouseY < y + 92) {
                            switch (wordleLetter.state) {
                                case TYPED:
                                case GREEN:
                                    wordleLetter.state = WordleLetter.State.GREY;
                                    break;
                                case GREY:
                                    wordleLetter.state = WordleLetter.State.YELLOW;
                                    break;
                                case YELLOW:
                                    wordleLetter.state = WordleLetter.State.GREEN;
                                    break;
                            }
                        }
                    }
                }
                x += 100;
            }
            y += 100;
        }
    }

    public Window init() {
        GLProfile.initSingleton();
        GLProfile profile = GLProfile.getDefault();
        GLCapabilities capabilities = new GLCapabilities(profile);
        window = GLWindow.create(capabilities);
        window.setSize(920, 630);
        window.setTitle("WordleWinner");
        window.addGLEventListener(new WindowEventListener());
        window.addMouseListener(new MouseEventListener());
        window.addKeyListener(new InputListener());
        FPSAnimator fpsAnimator = new FPSAnimator(window, 60);
        fpsAnimator.start();
        window.setVisible(true);
        return this;
    }
}