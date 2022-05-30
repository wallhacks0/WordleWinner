package me.pelle.wordlewinner.util;

public class WordleLetter {
    int index;
    String letter;
    State state = State.BLANK;

    public WordleLetter(int index) {
        this.index = index;
    }

    public enum State {
        BLANK,
        GREY,
        GREEN,
        YELLOW,
        TYPED
    }
}
