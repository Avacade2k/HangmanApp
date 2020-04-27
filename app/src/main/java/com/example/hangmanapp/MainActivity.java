package com.example.hangmanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //declared vars
    private static final String TAG = "MyActivity";
    TextView title;
    static TextView wordDisplay;
    static TextView lettersTried;
    static TextView triesLeft;
    static EditText editInput;
    Button resetButton;
    static String wordDisplayString;
    static char wordDisplayedArray[];
    public static String guessedLetters;
    public static String triesLeftString;
    static int fails = 0;
    final static int maxFails = 6;
    public static boolean gameWon = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize var
        wordDisplay = (TextView)findViewById(R.id.wordDisplay);
        title = (TextView)findViewById(R.id.titleText);
        editInput = (EditText)findViewById(R.id.guessText);
        resetButton = (Button)findViewById(R.id.resetButton);
        lettersTried = (TextView)findViewById(R.id.lettersTried);
        triesLeft = (TextView)findViewById(R.id.triesLeft);
        title.setText("Hangman");

        startGame();
        Log.d(TAG, "Current word: "+WordList.currentWord);

        //input listener
        editInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //if input is letter
                if(s.length() != 0){
                    checkGuess(s.charAt(0));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });



    }

    public static void startGame(){
        gameWon = false;
        WordList.randomWord();
        wordDisplayedArray = WordList.currentWord.toCharArray();

        //underscores
        for(int i = 0; i < wordDisplayedArray.length; i++){
            wordDisplayedArray[i] = '_';
        }

        wordDisplayString = String.valueOf(wordDisplayedArray);

        //display word
        displayWord();

        //input
        //clear input
        editInput.setText("");

        //letters tried
        guessedLetters = " ";

        //display letters on screen
        lettersTried.setText("Letters tried: "+guessedLetters);

        //tries left
        fails = 0;
        triesLeftString = "Tries left: "+(maxFails-fails);
        triesLeft.setText(triesLeftString);


    }

    static void revealLetterInWord(char letter){
        int letterIndex = WordList.currentWord.indexOf(letter);

        //loop if letter is found
        while(letterIndex >= 0){
            wordDisplayedArray[letterIndex] = WordList.currentWord.charAt(letterIndex);
            letterIndex = WordList.currentWord.indexOf(letter, letterIndex+1);
        }

        //update string
        wordDisplayString = String.valueOf(wordDisplayedArray);
    }

    static void displayWord(){
        String formattedDisplay = "";
        for(char c : wordDisplayedArray){
            formattedDisplay += c + " ";
        }
        wordDisplay.setText(formattedDisplay);
    }

    public void onWin(){
        gameWon = true;
        editInput.setText("");
        Toast.makeText(getApplicationContext(),"You won!",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity.this, ResultActivity.class));
    }

    public void checkGuess(char letter){
        //on letter found
        if(WordList.currentWord.indexOf(letter) >= 0){
            //if letter not guessed yet
            if(wordDisplayString.indexOf(letter) < 0){
                //update underscore
                revealLetterInWord(letter);
                displayWord();
                Toast.makeText(getApplicationContext(),"Correct guess",Toast.LENGTH_SHORT).show();

                //check for win
                if(!wordDisplayString.contains("_")){
                    onWin();
                }
            }
        }
        //on letter not found
        else{
            failedGuess();
        }

        if(guessedLetters.indexOf(letter) < 0){
            guessedLetters += letter + " ";
            lettersTried.setText("Letters tried: "+guessedLetters);

        }

    }

    public void failedGuess(){
        fails++;
        if(fails >= maxFails){
            triesLeft.setText("You lost!");
            triesLeftString = "Tries left: "+(maxFails-fails);
            startActivity(new Intent(MainActivity.this, ResultActivity.class));
        }
        else{
            triesLeftString = "Tries left: "+(maxFails-fails);
            triesLeft.setText(triesLeftString);
        }
        Toast.makeText(getApplicationContext(),"Wrong guess",Toast.LENGTH_SHORT).show();
    }

    public void resetGame(View v){
        //setup new game
        startGame();
        Toast.makeText(getApplicationContext(),"Game restarted!",Toast.LENGTH_SHORT).show();
    }

    public void saveGameState(){
        SharedPreferences.Editor editor = getSharedPreferences("MyPreference", MODE_PRIVATE).edit();

        editor.putString("word", WordList.currentWord);
        editor.putString("triedLetters", guessedLetters);
        editor.putInt("fails", fails);
        editor.apply();
        editor.commit();
        Log.d("My Activity", "saved");
    }

    public void getGameState(){
        SharedPreferences prefs = getSharedPreferences("MyPreference", MODE_PRIVATE);

        WordList.currentWord = prefs.getString("word", "");
        guessedLetters = prefs.getString("triedLetters", "");
        fails = prefs.getInt("fails", 0);
        Log.d("My Activity", "loaded");

    }

}
