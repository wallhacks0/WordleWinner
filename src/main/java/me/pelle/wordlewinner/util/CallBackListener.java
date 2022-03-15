package me.pelle.wordlewinner.util;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class CallBackListener {
    private static CallBackListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos;
    private boolean mouseButtonPressed[] = new boolean[3];
    private int width;
    private int height;

    private CallBackListener() {
        this.width = 1920;
        this.height = 1080;
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
    }

    public static CallBackListener get() {
        if (CallBackListener.instance == null) {
            CallBackListener.instance = new CallBackListener();
        }

        return CallBackListener.instance;
    }

    public static void mousePosCallback(long window, double xpos, double ypos) {
        get().xPos = xpos;
        get().yPos = ypos;
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = false;
            }
        }
    }

    public static void sizeCallback(long id, int width, int height) {
        get().width = width;
        get().height = height;
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame() {
        get().scrollX = 0;
        get().scrollY = 0;
    }

    public static int getWidth() {
        return get().width;
    }

    public static int getHeight() {
        return get().height;
    }

    public static int getX() {
        return (int) get().xPos;
    }

    public static int getY() {
        return (int) get().yPos;
    }

    public static float getScrollX() {
        return (float)get().scrollX;
    }

    public static float getScrollY() {
        return (float)get().scrollY;
    }

    public static boolean mouseButtonDown(int button) {
        if (button < get().mouseButtonPressed.length) {
            return get().mouseButtonPressed[button];
        } else {
            return false;
        }
    }
}
