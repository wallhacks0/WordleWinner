package me.pelle.wordlewinner.util;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.windows.MOUSEINPUT;

import java.awt.*;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private String title;
    private long id;

    private static Window instance;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        title = "WordleWinner";

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            System.err.println("Fucked");
            return;
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        id = glfwCreateWindow(width, height, title, NULL, NULL);
        if (id == NULL) {
            System.err.println("Fucked anyway");
            return;
        }

        glfwMakeContextCurrent(id);

        glfwSwapInterval(1);
        glfwShowWindow(id);

        GL.createCapabilities();
    }

    public static Window get() {
        return instance;
    }

    public static void clean() {
        instance.clear();
    }

    private void clear() {
        glfwFreeCallbacks(id);
        glfwDestroyWindow(id);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public static Boolean interrupted() {
        return glfwWindowShouldClose(instance.id);
    }

    public static void init() {
        instance = new Window();
    }

    public static void update() {
        instance.loop();
    }

    private void loop() {
        glfwPollEvents();
        glClearColor(1, 1,1, 1);
        glClear(GL_COLOR_BUFFER_BIT);
        glfwSwapBuffers(id);
    }
}
