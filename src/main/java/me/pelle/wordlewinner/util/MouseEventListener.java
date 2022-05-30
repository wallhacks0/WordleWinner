package me.pelle.wordlewinner.util;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

public class MouseEventListener implements MouseListener {
    @Override
    public void mouseClicked(MouseEvent e) {

    }


    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        Window.click = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Window.updateMouse(e.getX(), e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }
    @Override
    public void mouseWheelMoved(MouseEvent e) {

    }
}
