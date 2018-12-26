package be.arcode.pietjesbak;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button roll, pass;
    TextView player1, player2, tv_score1, tv_score2, tvRollsLeft;
    ImageView dice1img, dice2img, dice3img, lines1img, lines2img;
    int rollsLeft = 3, score = 0, valueDice1, valueDice2, valueDice3, scorePlayer1, scorePlayer2;
    boolean player1active = true;
    boolean check69[] = {false, false, false};
    String scoreText;


    private int min = 1, max = 6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        roll = (Button) findViewById(R.id.roll);
        player1 = (TextView) findViewById(R.id.player1);
        player2 = (TextView) findViewById(R.id.player2);
        tv_score1 = (TextView) findViewById(R.id.score1);
        tv_score2 = (TextView) findViewById(R.id.score2);
        dice1img = (ImageView) findViewById(R.id.dice1);
        dice2img = (ImageView) findViewById(R.id.dice2);
        dice3img = (ImageView) findViewById(R.id.dice3);
        tvRollsLeft = (TextView) findViewById(R.id.throwsLeft);
        pass = (Button) findViewById(R.id.passTurn);
        lines1img = (ImageView) findViewById(R.id.lines1);
        lines2img = (ImageView) findViewById(R.id.lines2);

        roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollsLeft -= 1;
                tvRollsLeft.setText("Rolls left: " + String.valueOf(rollsLeft));

                diceRoll();
                calculateScore();

                if(rollsLeft == 0){

                    if(player1active == true){
                        player1.setTextColor(getResources().getColor(R.color.TextColor));
                        player2.setTextColor(getResources().getColor(R.color.ActivePlayer));
                        player1active = false;
                        rollsLeft = 3;
                        tvRollsLeft.setText("Rolls left: " + String.valueOf(rollsLeft));
                    } else {
                        player2.setTextColor(getResources().getColor(R.color.TextColor));
                        player1.setTextColor(getResources().getColor(R.color.ActivePlayer));
                        player1active = true;
                        rollsLeft = 3;
                        tvRollsLeft.setText("Rolls left: " + String.valueOf(rollsLeft));
                        compareScores();
                    }

                }

            }
        });


        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                if(rollsLeft == 3) {
                    Context context = getApplicationContext();
                    CharSequence text = "You should roll at least 1 time!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();


                } else{

                    if(player1active == true){
                        player1.setTextColor(getResources().getColor(R.color.TextColor));
                        player2.setTextColor(getResources().getColor(R.color.ActivePlayer));
                        player1active = false;
                        rollsLeft = 3 - rollsLeft;
                        tvRollsLeft.setText("Rolls left: " + String.valueOf(rollsLeft));

                    } else {
                        player2.setTextColor(getResources().getColor(R.color.TextColor));
                        player1.setTextColor(getResources().getColor(R.color.ActivePlayer));
                        player1active = true;
                        rollsLeft = 3;
                        tvRollsLeft.setText("Rolls left: " + String.valueOf(rollsLeft));
                        compareScores();

                    }

                }



            }

        });

    }


    private void diceRoll(){

        for (int i = 0; i <= 3; i++){
            Random r = new Random();
            int random = r.nextInt((max - min) + 1) + min;

            if (i == 1){

                valueDice1 = random;
                switch(random) {
                    case 1 :
                        dice1img.setBackgroundResource(R.drawable.ic_dice_one);
                        break;
                    case 2 :
                        dice1img.setBackgroundResource(R.drawable.ic_dice_two);
                        break;
                    case 3 :
                        dice1img.setBackgroundResource(R.drawable.ic_dice_three);
                        break;
                    case 4 :
                        dice1img.setBackgroundResource(R.drawable.ic_dice_four);
                        break;
                    case 5 :
                        dice1img.setBackgroundResource(R.drawable.ic_dice_five);
                        break;
                    case 6 :
                        dice1img.setBackgroundResource(R.drawable.ic_dice_six);
                        break;
                }
            }
            else if (i == 2){

                valueDice2 = random;
                switch(random) {
                    case 1 :
                        dice2img.setBackgroundResource(R.drawable.ic_dice_one);
                        break;
                    case 2 :
                        dice2img.setBackgroundResource(R.drawable.ic_dice_two);
                        break;
                    case 3 :
                        dice2img.setBackgroundResource(R.drawable.ic_dice_three);
                        break;
                    case 4 :
                        dice2img.setBackgroundResource(R.drawable.ic_dice_four);
                        break;
                    case 5 :
                        dice2img.setBackgroundResource(R.drawable.ic_dice_five);
                        break;
                    case 6 :
                        dice2img.setBackgroundResource(R.drawable.ic_dice_six);
                        break;
                }
            }
            else if (i == 3){

                valueDice3 = random;
                switch(random) {
                    case 1 :
                        dice3img.setBackgroundResource(R.drawable.ic_dice_one);
                        break;
                    case 2 :
                        dice3img.setBackgroundResource(R.drawable.ic_dice_two);
                        break;
                    case 3 :
                        dice3img.setBackgroundResource(R.drawable.ic_dice_three);
                        break;
                    case 4 :
                        dice3img.setBackgroundResource(R.drawable.ic_dice_four);
                        break;
                    case 5 :
                        dice3img.setBackgroundResource(R.drawable.ic_dice_five);
                        break;
                    case 6 :
                        dice3img.setBackgroundResource(R.drawable.ic_dice_six);
                        break;
                }
            }
        }

    }

    private void calculateScore() {

        //check if ZAND (3x zelfde waarde)
        if (valueDice1 == valueDice2 && valueDice1 == valueDice3) {

            //Determine which kind of ZAND has been rolled
            switch (valueDice1) {
                case 1:
                    //TOPSCORE 3 Apen (1-1-1)
                    score = 799; //symbolic value to determine winner
                    scoreText = "3 Apen (1-1-1)";
                    break;
                case 2:
                    //ZAND (2-2-2)
                    score = 722; //symbolic value to determine winner
                    scoreText = "Zand (2-2-2)";
                    break;
                case 3:
                    //ZAND(3-3-3)
                    score = 733; //symbolic value to determine winner
                    scoreText = "Zand (3-3-3";
                    break;
                case 4:
                    //ZAND (4-4-4)
                    score = 744; //symbolic value to determine winner
                    scoreText = "Zand (4-4-4)";
                    break;
                case 5:
                    //ZAND (5-5-5)
                    score = 755; //symbolic value to determine winner
                    scoreText = "Zand (5-5-5)";
                    break;
                case 6:
                    //ZAND (6-6-6)
                    score = 766; //symbolic value to determine winner
                    scoreText = "Zand (6-6-6)";
                    break;
            }
            writeScore();

        } else if(score == 0) {
            //Determine if SOIXANTE-NEUF has been rolled.
            if (valueDice1 == 4 || valueDice2 == 4 || valueDice3 == 4) {
                check69[0] = true;
            }
            if (valueDice1 == 5 || valueDice2 == 5 || valueDice3 == 5){
                check69[1] = true;
            }
            if (valueDice1 == 6 || valueDice2 == 6 || valueDice3 == 6){
                check69[2] = true;
            }
            if (check69[0] == true && check69[1] == true && check69[2] == true){
                //SOIXANTE-NEUF (combination of 6,5 and 4)
                score = 769; //symbolic value to determine winner
                scoreText = "SOIXANTE-NEUF (6-5-4)";
                writeScore();
            }
            else {
                //no special combination - calculate standard score
                switch (valueDice1){
                    case 1:
                        //100 points
                        score += 100;
                        break;
                    case 2:
                        //2 points
                        score += 2;
                        break;
                    case 3:
                        //3 points
                        score += 3;
                        break;
                    case 4:
                        //4 points
                        score += 4;
                        break;
                    case 5:
                        //5 points
                        score += 5;
                        break;
                    case 6:
                        //60 points
                        score += 60;
                        break;
                }

                switch (valueDice2){
                    case 1:
                        //100 points
                        score += 100;
                        break;
                    case 2:
                        //2 points
                        score += 2;
                        break;
                    case 3:
                        //3 points
                        score += 3;
                        break;
                    case 4:
                        //4 points
                        score += 4;
                        break;
                    case 5:
                        //5 points
                        score += 5;
                        break;
                    case 6:
                        //60 points
                        score += 60;
                        break;
                }

                switch (valueDice3){
                    case 1:
                        //100 points
                        score += 100;
                        break;
                    case 2:
                        //2 points
                        score += 2;
                        break;
                    case 3:
                        //3 points
                        score += 3;
                        break;
                    case 4:
                        //4 points
                        score += 4;
                        break;
                    case 5:
                        //5 points
                        score += 5;
                        break;
                    case 6:
                        //60 points
                        score += 60;
                        break;
                }

                scoreText = String.valueOf(score);
                writeScore();
            }

            //set all check69 back to false for next player/turn
            Arrays.fill(check69,false);



        }

    }

    private void writeScore(){

        //check which player is active (where to write score)
        if(player1active == true){

            //set score TextView for player1
            tv_score1.setText("Score: " + scoreText);

            //set score player1 for comparison of round winner and clear score for next time
            scorePlayer1 = score;
            score = 0;
        }else {

            //set score TextView for player2
            tv_score2.setText("Score: " + scoreText);

            //set score player2 for comparison of round winner and clear score for next time
            scorePlayer2 = score;
            score = 0;
        }

    }

    private void compareScores(){
        //compare the scores of current round for the players.
        if (scorePlayer1 == scorePlayer2){
            //it's a tie! another roll for both players.
            showWinner("It's a tie!", "Each player has to roll once more.");

        } else if(scorePlayer1 > scorePlayer2){
            //Player1 wins this round
            showWinner(player1.getText() + " wins this round", "Swipe away one of your lines.");

        } else{
            //Player2 wins this round
            showWinner(player2.getText() + " wins this round!", "Swipe away one of your lines.");

        }


    }

    public void showWinner (String winner, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(winner);
        builder.setMessage(message);
        builder.show();
    }

    /*linesPlayer1.setOnTouchListener(new OnTouchListener() {

        //if (canSwipePlayer1 == true)
        public boolean onTouch(View v, MotionEvent event) {
            // ... Respond to touch events

            //canSwipePlayer1 = false;
            return true;
        }
    });*/

}
