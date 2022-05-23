package me.pelle.wordlewinner.util;

public class InputLine {
    public int index;

    public InputLine(int index) {
        this.index = index;
    }
    WordleLetter letters[] = new WordleLetter[]{
            new WordleLetter(),
            new WordleLetter(),
            new WordleLetter(),
            new WordleLetter(),
            new WordleLetter()
    };
}
