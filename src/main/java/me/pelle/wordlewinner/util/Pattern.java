package me.pelle.wordlewinner.util;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

import static me.pelle.wordlewinner.util.WordleWinner.countChar;

public class Pattern  {
    ArrayList<String> grey = new ArrayList<>();
    ConcurrentHashMap<String, Integer> yellow = new ConcurrentHashMap<>();
    PatternLetter letters[] = new PatternLetter[]{
            new PatternLetter(0),
            new PatternLetter(1),
            new PatternLetter(2),
            new PatternLetter(3),
            new PatternLetter(4)
    };

    public Pattern clone() {
        Pattern pattern = new Pattern();
        pattern.grey.addAll(grey);
        for (String s : yellow.keySet()) {
            pattern.yellow.put(s, yellow.get(s));
        }
        for (PatternLetter l : pattern.letters) {
            l.green = letters[l.index].green;
            l.yellow.addAll(letters[l.index].yellow);
        }
        return pattern;
    }

    public boolean match(String word) {
        for (String grey : grey) {
            if (word.contains(grey)) {
                return false;
            }
        }
        for (String yellow : yellow.keySet()) {
            int amount = this.yellow.get(yellow);
            if (amount > 0) {
                if (countChar(word, yellow.charAt(0)) < amount) {
                    return false;
                }
            } else {
                if (countChar(word, yellow.charAt(0)) != -1 * amount) {
                    return false;
                }
            }
        }

        for (PatternLetter letter : letters) {
            if (letter.green != null) {
                if (!letter.green.equals(String.valueOf(word.charAt(letter.index)))) return false;
            }
            for (String yellow : letter.yellow) {
                if (yellow.equals(String.valueOf(word.charAt(letter.index)))) {
                    return false;
                }
            }
        }
        return true;
    }


    public void add(InputLine inputLine) {
        ArrayList<String> ignore = new ArrayList<>();
        for (WordleLetter l : inputLine.letters) {
            if (l.state == WordleLetter.State.YELLOW && !ignore.contains(l.letter)) {
                if (yellow.containsKey(l.letter)) {
                    if (yellow.get(l.letter) < 0) continue;
                }
                ignore.add(l.letter);
                letters[l.index].yellow.add(l.letter.toLowerCase(Locale.ROOT));
                int found = 1;
                boolean sure = false;
                for (WordleLetter lx : inputLine.letters) {
                    if (lx.index == l.index) continue;
                    if (lx.letter.equalsIgnoreCase(l.letter)) {
                        if (lx.state == WordleLetter.State.YELLOW || lx.state == WordleLetter.State.GREEN) {
                            if (lx.state == WordleLetter.State.YELLOW) {
                                letters[lx.index].yellow.add(lx.letter.toLowerCase(Locale.ROOT));
                            }
                            found++;
                        } else if (lx.state == WordleLetter.State.GREY) {
                            letters[lx.index].yellow.add(lx.letter.toLowerCase(Locale.ROOT));
                            sure = true;
                        }
                    }
                }
                if (sure) found *= -1;
                if (found > 0 && yellow.containsKey(l.letter)) {
                    if (yellow.get(l.letter) > found) continue;
                }
                yellow.put(l.letter.toLowerCase(Locale.ROOT), found);
            }
        }
        for (WordleLetter l : inputLine.letters) {
            if (l.state == WordleLetter.State.GREEN && !ignore.contains(l.letter)) {
                letters[l.index].green = l.letter.toLowerCase(Locale.ROOT);
            }
        }

        for (WordleLetter l : inputLine.letters) {
            if (l.state == WordleLetter.State.GREY && !ignore.contains(l.letter)) {
                boolean flag = false;
                for (WordleLetter lx : inputLine.letters) {
                    if (lx.letter.equals(l.letter) && (lx.state == WordleLetter.State.GREEN || lx.state == WordleLetter.State.YELLOW)) {
                        flag = true;
                        break;
                    }
                }
                if (!flag)
                    grey.add(l.letter.toLowerCase(Locale.ROOT));
            }
        }
    }
}
