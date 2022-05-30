package me.pelle.wordlewinner.util;

import com.jogamp.newt.event.KeyEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class WordleWinner {
    public static ArrayList<String> bestWords = new ArrayList<>();
    public static ArrayList<String> wordsLeft = new ArrayList<>();
    public static InputLine activeLine;
    public static WordleLetter activeLetter;
    public static InputLine lines[] = new InputLine[]{
            new InputLine(0),
            new InputLine(1),
            new InputLine(2),
            new InputLine(3),
            new InputLine(4),
            new InputLine(5)
    };
    static ArrayList<String> words = new ArrayList<>();
    Window window;

    public WordleWinner() {
        try {
            FileInputStream fis = new FileInputStream("words.txt");
            Scanner sc = new Scanner(fis);

            while (sc.hasNextLine()) {
                words.add(sc.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bestWords.addAll(words);
        window = new Window().init();
        activeLine = lines[0];
        activeLetter = activeLine.letters[0];
    }

    public static void onKeyPress(KeyEvent e) {
        if (e.getKeyCode() == 8) {
            if (activeLetter == null) {
                activeLetter = activeLine.letters[4];
            } else if (activeLetter == activeLine.letters[4]) {
                activeLetter = activeLine.letters[3];
            } else if (activeLetter == activeLine.letters[3]) {
                activeLetter = activeLine.letters[2];
            } else if (activeLetter == activeLine.letters[2]) {
                activeLetter = activeLine.letters[1];
            } else if (activeLetter == activeLine.letters[1]) {
                activeLetter = activeLine.letters[0];
            }
            activeLetter.letter = "";
            activeLetter.state = WordleLetter.State.BLANK;
        } else if (activeLetter == null) {
            if (e.getKeyCode() == 13) {
                boolean flag = false;
                for (WordleLetter l : activeLine.letters) {
                    if (l.state == WordleLetter.State.TYPED || l.state == WordleLetter.State.BLANK) flag = true;
                }
                if (!flag) {
                    activeLine.done = true;
                    activeLine = lines[activeLine.index + 1];
                    activeLetter = activeLine.letters[0];
                    new AlgThread().start();
                }
            }
        } else if (Character.isAlphabetic(e.getKeyChar())) {
            activeLetter.letter = String.valueOf(e.getKeyChar()).toUpperCase(Locale.ROOT);
            activeLetter.state = WordleLetter.State.TYPED;
            if (activeLetter == activeLine.letters[0]) {
                activeLetter = activeLine.letters[1];
            } else if (activeLetter == activeLine.letters[1]) {
                activeLetter = activeLine.letters[2];
            } else if (activeLetter == activeLine.letters[2]) {
                activeLetter = activeLine.letters[3];
            } else if (activeLetter == activeLine.letters[3]) {
                activeLetter = activeLine.letters[4];
            } else activeLetter = null;
        }
    }

    public static int countChar(String word, char search) {
        int count = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == search) {
                count++;
            }
        }
        return count;
    }

    static class AlgThread extends Thread {
        ArrayList<String> outCome = new ArrayList<>();

        @Override
        public void run() {
            Pattern pattern = new Pattern();
            for (InputLine inputLine : lines) {
                if (!inputLine.done) break;
                pattern.add(inputLine);
            }
            for (String word : words) {
               if (pattern.match(word)) wordsLeft.add(word);
            }
            wordsLeft.forEach(System.out::println);
        }
    }
}
