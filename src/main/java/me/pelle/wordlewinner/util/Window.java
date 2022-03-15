package me.pelle.wordlewinner.util;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
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

        id = glfwCreateWindow(width, height, title, NULL, NULL);
        if (id == NULL) {
            System.err.println("Fucked anyway");
            return;
        }

        glfwSetCursorPosCallback(id, CallBackListener::mousePosCallback);
        glfwSetMouseButtonCallback(id, CallBackListener::mouseButtonCallback);
        glfwSetScrollCallback(id, CallBackListener::mouseScrollCallback);
        glfwSetWindowSizeCallback(id, CallBackListener::sizeCallback);
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
        setup();
        int mouseX = CallBackListener.getX();
        int mouseY = CallBackListener.getY();
        glPolygonMode(GL_FRONT, GL_FILL);
        glBegin(GL_QUADS);
        glColor3d(1D, 1D,1D);
        glVertex2f(mouseX, mouseY);
        glVertex2f(mouseX + 10, mouseY);
        glVertex2f(mouseX + 10, mouseY + 10);
        glVertex2f(mouseX, mouseY + 10);
        glEnd();
        end();
    }

    private void end() {
        glfwSwapBuffers(id);
        CallBackListener.endFrame();
    }

    void setup() {
        glClear(GL_COLOR_BUFFER_BIT);
        glfwPollEvents();
        height = CallBackListener.getHeight();
        width = CallBackListener.getWidth();
        glViewport(0,0, width, height);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0.0, width, height,0,-1.0,1.0);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }
}
