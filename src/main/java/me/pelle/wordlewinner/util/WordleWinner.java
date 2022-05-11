package me.pelle.wordlewinner.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class WordleWinner {
    Window window;
    ArrayList<String> words = new ArrayList<>();
    public static ArrayList<String> bestWords = new ArrayList<>();
    public static ArrayList<String> wordsLeft = new ArrayList<>();
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
        wordsLeft.addAll(words);
        window = new Window().init();
    }

    class AlgThread extends Thread {
        ArrayList<String> outCome = new ArrayList<>();
    }
}
