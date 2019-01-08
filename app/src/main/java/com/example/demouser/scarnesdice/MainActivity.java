package com.example.demouser.scarnesdice;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int [] dice = {R.drawable.dice1, R.drawable.dice2,
    R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice6};

    private int userScore;
    private int cpuScore;
    private int turnScore;
    private boolean isUserTurn;

    private TextView userScoreView;
    private TextView cpuScoreView;
    private TextView turnScoreView;
    private TextView currentUserView;

    private ImageButton diceButton;
    private Button holdButton;
    private Button resetButton;

    private Random random;

    private Handler handler = new Handler();
    private Runnable rollDice = new Runnable() {
        @Override
        public void run() {
            roll();
            if(!isUserTurn && turnScore < 20)
                handler.postDelayed(this, 1000);
            else if(turnScore >= 20) {
                switchTurns();
                resetButton.setEnabled(true);
                holdButton.setEnabled(true);
                diceButton.setEnabled(true);
            }
            else {
                resetButton.setEnabled(true);
                holdButton.setEnabled(true);
                diceButton.setEnabled(true);
            }
        }
    };
    private Runnable spinDice = new Runnable() {
        @Override
        public void run() {
            spin();
        }
    };
    private Runnable makeMessage = new Runnable() {
        @Override
        public void run() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        random = new Random();

        userScoreView = findViewById(R.id.UserScore);
        cpuScoreView = findViewById(R.id.ComputerScore);
        turnScoreView = findViewById(R.id.TurnScore);
        currentUserView = findViewById(R.id.currentPlayer);

        isUserTurn = true; // default is user turn

        diceButton = findViewById(R.id.dice);
        diceButton.setOnClickListener(rollClickListener);
        // dice.setImageResource(R.drawable.dice2);

        resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(resetClickListener);

        holdButton = findViewById(R.id.holdButton);
        holdButton.setOnClickListener(holdClickListener);

        updateView();
    }

    private int spin() {
        // randomly select a dice value ( get a value i between 0 and 5 inclusive )
        int i = random.nextInt(6);
        // update the display to show value ( use dice[i] )
        diceButton.setImageResource(dice[i]);
        return i;
    }

    private void roll() {
//        int numSpins = 20;
//        for(int i = 0; i < numSpins; i++)
//            handler.postDelayed(spinDice, 100);
        int i = spin();
        if(i != 0 ) {
            turnScore += i+1;
        } else {
            turnScore = 0;
            switchTurns();
        }
        updateView();
    }

    // hold
    private void switchTurns() {
        if(isUserTurn) {
            userScore += turnScore;
            isUserTurn = false;
        }
        else {
            cpuScore += turnScore;
            isUserTurn = true;
        }
        turnScore = 0;
        if(!isUserTurn) {
            takeComputerTurn();
        }
        updateView();
    }

    private void takeComputerTurn() {
        diceButton.setEnabled(false);
        resetButton.setEnabled(false);
        holdButton.setEnabled(false);
        handler.postDelayed(rollDice, 10);
    }

    private void updateView() {
        userScoreView.setText(Integer.toString(userScore));
        cpuScoreView.setText(Integer.toString(cpuScore));
        turnScoreView.setText(Integer.toString(turnScore));
        if(isUserTurn)
            currentUserView.setText("User");
        else
            currentUserView.setText("CPU");
    }

    private View.OnClickListener rollClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            roll();
        }
    };

    private View.OnClickListener holdClickListener = new View.OnClickListener() {

        public void onClick(View v) {
            switchTurns();
        }
    };

    private View.OnClickListener resetClickListener = new View.OnClickListener() {

        public void onClick(View v) {
            userScore = 0;
            cpuScore = 0;
            turnScore = 0;
            isUserTurn = true;
            updateView();
        }
    };
}
