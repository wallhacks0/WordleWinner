package me.pelle.wordlewinner.util;

public class WordleWinner {
    WindowThread windowThread;
    public WordleWinner() {
        windowThread = new WindowThread();
        windowThread.start();
    }

    public class WindowThread extends Thread {
        @Override
        public void run() {
            Window.init();

            while (!Window.interrupted()) {
                Window.update();
            }
            Window.clean();
        }
    }
}
