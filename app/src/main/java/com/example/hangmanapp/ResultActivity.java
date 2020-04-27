package com.example.hangmanapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class ResultActivity extends AppCompatActivity {

    //declare var
    TextView result;
    TextView score;
    Button restartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //initialize var
        result = (TextView)findViewById(R.id.resultText);
        score = (TextView)findViewById(R.id.finalScoreText);
        restartButton = (Button)findViewById(R.id.restartButton);

        if(MainActivity.gameWon){
            result.setText("Result: you won!");
            score.setText("Guessed word: "+WordList.currentWord+"\n"+MainActivity.triesLeftString+"\nTried letters: "+MainActivity.guessedLetters);
        }
        else{
            result.setText("Result: you lost!");
            score.setText("Word of the game: "+WordList.currentWord+"\n"+MainActivity.triesLeftString+"\nTried letters: "+MainActivity.guessedLetters);
        }


    }

    public void restartGame(View v){
        //restart game
        MainActivity.startGame();
        MainActivity.guessedLetters = "";
        startActivity(new Intent(ResultActivity.this, MainActivity.class));
        Toast.makeText(getApplicationContext(),"Game restarted!",Toast.LENGTH_SHORT).show();
    }

}
