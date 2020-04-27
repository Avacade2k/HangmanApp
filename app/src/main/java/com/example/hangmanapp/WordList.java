package com.example.hangmanapp;

import java.util.Random;

public class WordList {

    public static String words[] = {"waluigi", "mario", "luigi","bowser", "yoshi", "wario"};
    static int currentWordPosition;
    public static String currentWord;

    public static void randomWord(){
        currentWordPosition = new Random().nextInt(words.length);
        currentWord = words[currentWordPosition];
    }

}
