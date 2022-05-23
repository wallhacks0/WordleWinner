package me.pelle.wordlewinner.util;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

public class InputListener implements KeyListener{
    @Override
    public void keyPressed(KeyEvent d) {
        WordleWinner.onKeyPress(d);
    }

    @Override
    public void keyReleased(KeyEvent d) {
    }
}