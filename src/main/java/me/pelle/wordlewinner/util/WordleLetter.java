package me.pelle.wordlewinner.util;

public class WordleLetter {
    String letter;
    State state = State.BLANK;

    public enum State {
        BLANK,
        GREY,
        GREEN,
        YELLOW,
        TYPED
    }
}
