package me.pelle.wordlewinner.util;

public class InputLine {
    public int index;
    public boolean done = false;
    public InputLine(int index) {
        this.index = index;
    }
    WordleLetter letters[] = new WordleLetter[]{
            new WordleLetter(0),
            new WordleLetter(1),
            new WordleLetter(2),
            new WordleLetter(3),
            new WordleLetter(4)
    };
}
