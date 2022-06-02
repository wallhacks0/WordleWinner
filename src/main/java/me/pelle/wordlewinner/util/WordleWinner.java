package me.pelle.wordlewinner.util;

import com.jogamp.newt.event.KeyEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WordleWinner {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static AlgThread algThread;
    public static CopyOnWriteArrayList<String> bestWords = new CopyOnWriteArrayList<>();
    public static ArrayList<String> wordsGuess = new ArrayList<>();
    public static CopyOnWriteArrayList<String> wordsLeft = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<String> wordsLeftForRender = new CopyOnWriteArrayList<>();
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

        try {
            FileInputStream fis = new FileInputStream("words2.txt");
            Scanner sc = new Scanner(fis);

            while (sc.hasNextLine()) {
                wordsGuess.add(sc.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bestWords.addAll(wordsGuess);
        wordsLeftForRender.addAll(words);
        window = new Window().init();
        activeLine = lines[0];
        activeLetter = activeLine.letters[0];
        updateAlgThread();
    }

    public static void updateAlgThread() {
        if (algThread != null) {
            algThread.interrupt();
        }
        algThread = new AlgThread();
        algThread.start();
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
            } else if (activeLetter.index == 0) {
                if (activeLine.index != 0) {
                    activeLine = lines[activeLine.index-1];
                    activeLetter = activeLine.letters[4];
                    activeLine.done = false;
                    updateAlgThread();
                }
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
                    updateAlgThread();
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

    public static void printLine(InputLine line) {
        StringBuilder print = new StringBuilder();
        for (WordleLetter wordleLetter : line.letters) {
            switch (wordleLetter.state) {
                case GREY:
                    print.append(ANSI_BLACK);
                    break;
                case YELLOW:
                    print.append(ANSI_YELLOW);
                    break;
                case GREEN:
                    print.append(ANSI_GREEN);
                    break;
            }
            print.append(wordleLetter.letter).append(ANSI_RESET);
        }
        System.out.println(print);
    }

    public static InputLine generateLine(String answer, String test) {
        InputLine sendBack = new InputLine(0);

        for (int i = 0; i < answer.length(); i++) {
            sendBack.letters[i].letter = String.valueOf(test.charAt(i));
        }

        for (int i = 0; i < answer.length(); i++) {
            if (sendBack.letters[i].state != WordleLetter.State.BLANK) continue;
            String answerChar = String.valueOf(answer.charAt(i));
            if (sendBack.letters[i].letter.equals(answerChar)) {
                sendBack.letters[i].state = WordleLetter.State.GREEN;
            } else {
                if (answer.contains(sendBack.letters[i].letter)) {
                    int max = countChar(answer, sendBack.letters[i].letter.charAt(0));
                    for (int i2 = 0; i2 < answer.length(); i2++) {
                        if (sendBack.letters[i2].letter.equals(sendBack.letters[i].letter) && sendBack.letters[i2].letter.equals(String.valueOf(answer.charAt(i2)))) {
                            sendBack.letters[i2].state = WordleLetter.State.GREEN;
                            max--;
                        }
                    }
                    for (int i2 = 0; i2 < answer.length(); i2++) {
                        if (sendBack.letters[i2].state == WordleLetter.State.BLANK && sendBack.letters[i2].letter.equals(sendBack.letters[i].letter)) {
                            if (max != 0) {
                                sendBack.letters[i2].state = WordleLetter.State.YELLOW;
                                max--;
                            } else {
                                sendBack.letters[i2].state = WordleLetter.State.GREY;
                            }
                        }
                    }
                } else {
                    sendBack.letters[i].state = WordleLetter.State.GREY;
                }
            }
        }
        return sendBack;
    }

    static class AlgThread extends Thread {
        double progress = 0;
        long startTime;

        public String timeLeft() {
            String s = "Time Left: ";
            long delta = System.currentTimeMillis() - startTime;
            double factor = 1 / progress;
            double timeLeft = (factor * delta) - delta;
            timeLeft *= 0.00001666666;
            double extra = timeLeft % 1;
            timeLeft -= extra;
            timeLeft = (int) timeLeft;
            int seconds = (int) (extra * 60);
            String minutes = String.valueOf(timeLeft);
            minutes = minutes.substring(0, minutes.length() - 2) + ":";
            return s + (minutes.equals("0:") ? "" : minutes) + seconds;
        }

        @Override
        public void run() {
            startTime = System.currentTimeMillis();
            Pattern pattern = new Pattern();
            for (InputLine inputLine : lines) {
                if (!inputLine.done) break;
                pattern.add(inputLine);
            }
            wordsLeft.clear();
            for (String word : words) {
                if (pattern.match(word)) wordsLeft.add(word);
            }
            wordsLeftForRender.clear();
            wordsLeftForRender.addAll(wordsLeft);
            ArrayList<GuessThread> threads = new ArrayList<>();
            ExecutorService executor = Executors.newFixedThreadPool(6);
            for (String word : wordsGuess) {
                GuessThread t = new GuessThread(word, pattern);
                executor.execute(t);
                threads.add(t);
            }
            boolean done = false;
            while (!done) {
                done = true;
                int threadsDone = 0;
                for (GuessThread thread : threads) {
                    if (thread.done) {
                        threadsDone++;
                    } else done = false;
                }
                if (wordsLeft.isEmpty())
                    progress = 1;
                else
                    progress = (double) threadsDone / threads.size();
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    return;
                }
            }
            Collections.sort(threads, new Sorter());
            bestWords.clear();
            for (GuessThread thread : threads) {
                bestWords.add(thread.guess);
            }
        }
    }

    static class GuessThread extends Thread {
        Pattern pattern;
        String guess;
        int hits;
        boolean done = false;

        public GuessThread(String guess, Pattern pattern) {
            this.guess = guess;
            this.pattern = pattern;
        }

        @Override
        public void run() {
            for (String word : wordsLeft) {
                Pattern p = pattern.clone();
                p.add(generateLine(word, guess));
                for (String test : wordsLeft) {
                    hits += p.match(test) ? 0 : 1;
                }
            }
            done = true;
        }
    }
}

class Sorter implements Comparator<WordleWinner.GuessThread> {
    @Override
    public int compare(WordleWinner.GuessThread o1, WordleWinner.GuessThread o2) {
        if (o1.hits == o2.hits) {
            boolean b1 = WordleWinner.wordsLeft.contains(o1.guess);
            boolean b2 = WordleWinner.wordsLeft.contains(o2.guess);
            if (b2 == b1)
                return 0;
            else {
                if (b1)
                    return -1;
                else return 1;
            }
        } else if (o1.hits > o2.hits)
            return -1;
        else
            return 1;
    }
}
