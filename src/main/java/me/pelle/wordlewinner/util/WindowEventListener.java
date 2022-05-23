package me.pelle.wordlewinner.util;

import com.jogamp.opengl.*;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;

public class WindowEventListener implements GLEventListener {

    @Override
    public void init(GLAutoDrawable drawable) {
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        System.exit(0);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        Window.window.setSize(1000, 600);
        GL2 gl = drawable.getGL().getGL2();
        RenderUtil.gl = gl;
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, 1000, 600, 0, 0, 1);
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glClearColor(0.15f, 0.15f, 0.15f, 1);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        Window.onRender(drawable);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }
}
