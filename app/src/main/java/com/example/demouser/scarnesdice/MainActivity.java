package com.example.demouser.scarnesdice;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    private TextView message; // the text that says what's going on ("Rolled 1!")
    private TextView rollHint; // the text that says "tap to roll" beneath the dice
    private TextView winText;

    private ImageButton diceButton;
    private Button holdButton;
    private Button resetButton;

    private Random random;
    private Animation aniDice;

    private Handler handler = new Handler();
    private Runnable rollDice = new Runnable() {
        @Override
        public void run() {
            if(!isUserTurn && turnScore < 20) {
                roll();
                handler.postDelayed(this, 500);
            }
            else if(turnScore >= 20) {
                message.setText("Computer Holds");
                handler.post(makeMessage);
                resetButton.setEnabled(true);
                holdButton.setEnabled(true);
                diceButton.setEnabled(true);
                switchTurns();
            }
            else {
                resetButton.setEnabled(true);
                holdButton.setEnabled(true);
                diceButton.setEnabled(true);
            }
        }
    };
    private Runnable makeMessage = new Runnable() {
        private boolean visible = true;
        @Override
        public void run() {
            if(visible) {
                message.setVisibility(View.VISIBLE);
                handler.postDelayed(this, 1000);
            }
            else
                message.setVisibility(View.INVISIBLE);
            visible = !visible;
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
        message = findViewById(R.id.message);
        rollHint = findViewById(R.id.clickHintText);
        winText = findViewById(R.id.WinText);
        winText.setVisibility(View.INVISIBLE);

        isUserTurn = true; // default is user turn

        diceButton = findViewById(R.id.dice);
        diceButton.setOnClickListener(rollClickListener);

        resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(resetClickListener);

        holdButton = findViewById(R.id.holdButton);
        holdButton.setOnClickListener(holdClickListener);

        aniDice = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom);

        updateView();
    }

    private boolean gameOver() {
        if(userScore >= 100) {
            winText.setText("YOU WIN");
        }
        else if(cpuScore >= 100) {
            winText.setText("CPU WINS");
        }
        else
            return false;
        winText.setVisibility(View.VISIBLE);
        diceButton.setEnabled(false);
        holdButton.setEnabled(false);
        return true;
    }

    private void roll() {
        // randomly select a dice value ( get a value i between 0 and 5 inclusive )
        int i = random.nextInt(6);
        // update the display to show value ( use dice[i] )
        diceButton.setImageResource(dice[i]);
        if(i != 0 ) {
            turnScore += i+1;
        } else {
            if(isUserTurn)
                message.setText("You Rolled 1!");
            else
                message.setText("CPU Rolled 1!");
            handler.post(makeMessage);
            turnScore = 0;
            switchTurns();
        }
        diceButton.startAnimation(aniDice);
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
        updateView();
        if(gameOver())
            return;
        if(!isUserTurn) {
            takeComputerTurn();
        }
    }

    private void takeComputerTurn() {
        diceButton.setEnabled(false);
        resetButton.setEnabled(false);
        holdButton.setEnabled(false);
        handler.postDelayed(rollDice, 1000);
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
            rollHint.setVisibility(View.INVISIBLE);
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
            rollHint.setVisibility(View.VISIBLE);

            holdButton.setEnabled(true);
            resetButton.setEnabled(true);
            diceButton.setEnabled(true);
            winText.setVisibility(View.INVISIBLE);
        }
    };
}
