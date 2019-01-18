package be.arcode.pietjesbak;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.widget.ShareDialog;


import java.util.Arrays;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    Button roll, pass;
    TextView player1, player2, tv_score1, tv_score2, tvRollsLeft, quitGame, rules;
    ImageView dice1img, dice2img, dice3img, lines1img, lines2img;
    CheckBox chk1, chk2, chk3;
    int rollsLeft = 3, score = 0, valueDice1, valueDice2, valueDice3, scorePlayer1, scorePlayer2, linesPlayer1 =5, linesPlayer2 = 5;
    boolean player1active = true, canSwipePlayer1 = false, canSwipePlayer2 = false, player2ThrowAfterPass = false;
    boolean check69[] = {false, false, false};
    String scoreText, swipeLines, winner, loser;
    private int min = 1, max = 6;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        roll = findViewById(R.id.roll);
        player1 = findViewById(R.id.player1);
        player2 = findViewById(R.id.player2);
        tv_score1 = findViewById(R.id.score1);
        tv_score2 = findViewById(R.id.score2);
        dice1img = findViewById(R.id.dice1);
        dice2img = findViewById(R.id.dice2);
        dice3img = findViewById(R.id.dice3);
        tvRollsLeft = findViewById(R.id.throwsLeft);
        pass = findViewById(R.id.passTurn);
        lines1img = findViewById(R.id.lines1);
        lines2img = findViewById(R.id.lines2);
        quitGame = findViewById(R.id.quitGame);
        rules = findViewById(R.id.rules);
        chk1 = findViewById(R.id.chk1);
        chk2 = findViewById(R.id.chk2);
        chk3 = findViewById(R.id.chk3);



        player1.setText(getIntent().getExtras().getString("namePlayer1"));
        player2.setText(getIntent().getExtras().getString("namePlayer2"));


        quitGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Intent to go to the player screen
                Intent intent = new Intent(MainActivity.this, PregameActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        rules.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Intent to go to rules screen
                Intent intent = new Intent(MainActivity.this, RulesActivity.class);
                startActivity(intent);
            }
        });

        roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //make sure all dices are selected for first roll!
                if( (rollsLeft == 3 && (chk1.isChecked() || chk2.isChecked() || chk3.isChecked() )) ||(player2ThrowAfterPass && (chk1.isChecked() || chk2.isChecked() || chk3.isChecked() )) ){
                    //show toast that player needs to roll al dices the first time
                    Toast.makeText(MainActivity.this, "First throw needs all dices, deselect them!", Toast.LENGTH_SHORT).show();

                }
                else{
                    //not first throw or all dices deselected in first throw, move on with throwing the dices
                    rollsLeft -= 1;
                    tvRollsLeft.setText("Rolls left: " + String.valueOf(rollsLeft));

                    //reset flag for player2ThrowAfterPass if player2 has rolled the dice
                    if(!player1active){
                        player2ThrowAfterPass = false;
                    }

                    //throw dices and show roll score
                    diceRoll();
                    calculateScore();

                }


                if(rollsLeft == 0){
                    //reset checkboxes for next player
                    chk1.setChecked(false);
                    chk2.setChecked(false);
                    chk3.setChecked(false);

                    //switch players
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

                //check if rolled at least 1 time before passing the turn
                if(rollsLeft == 3) {
                    Context context = getApplicationContext();
                    CharSequence text = "You should roll at least 1 time!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();


                } else{
                    //reset checkboxes for next player
                    chk1.setChecked(false);
                    chk2.setChecked(false);
                    chk3.setChecked(false);


                    //check which player was active and make corresponding game changes
                    if(player1active == true){
                        player1.setTextColor(getResources().getColor(R.color.TextColor));
                        player2.setTextColor(getResources().getColor(R.color.ActivePlayer));
                        player1active = false;
                        rollsLeft = 3 - rollsLeft;
                        tvRollsLeft.setText("Rolls left: " + String.valueOf(rollsLeft));
                        player2ThrowAfterPass = true;

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

        lines1img.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeDown() {
                if(canSwipePlayer1){
                    drawLinesPlayer1();
                    canSwipePlayer1 = false;
                }else{
                    Toast.makeText(MainActivity.this, player1.getText() + " should win a round to swipe lines away!", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onSwipeUp() {
                if(canSwipePlayer1){
                    drawLinesPlayer1();
                    canSwipePlayer1 = false;
                }else{
                    Toast.makeText(MainActivity.this, player1.getText() + " should win a round to swipe lines away!", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onSwipeLeft() {
                if(canSwipePlayer1){
                    drawLinesPlayer1();
                    canSwipePlayer1 = false;
                }else{
                    Toast.makeText(MainActivity.this, player1.getText() + " should win a round to swipe lines away!", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onSwipeRight() {
                if(canSwipePlayer1){
                    drawLinesPlayer1();
                    canSwipePlayer1 = false;
                }else{
                    Toast.makeText(MainActivity.this, player1.getText() + " should win a round to swipe lines away!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        lines2img.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeDown() {
                if(canSwipePlayer2){
                    drawLinesPlayer2();
                    canSwipePlayer2 = false;
                }else{
                    Toast.makeText(MainActivity.this, player2.getText() + " should win a round to swipe lines away!", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onSwipeUp() {
                if(canSwipePlayer2){
                    drawLinesPlayer2();
                    canSwipePlayer2 = false;
                }else{
                    Toast.makeText(MainActivity.this, player2.getText() + " should win a round to swipe lines away!", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onSwipeLeft() {
                if(canSwipePlayer2){
                    drawLinesPlayer2();
                    canSwipePlayer2 = false;
                }else{
                    Toast.makeText(MainActivity.this, player2.getText() + " should win a round to swipe lines away!", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onSwipeRight() {
                if(canSwipePlayer2){
                    drawLinesPlayer2();
                    canSwipePlayer2 = false;
                }else{
                    Toast.makeText(MainActivity.this, player2.getText() + " should win a round to swipe lines away!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private void diceRoll(){

        for (int i = 0; i <= 3; i++){
            Random r = new Random();
            int random = r.nextInt((max - min) + 1) + min;

            if (i == 1 && !chk1.isChecked()){

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
            else if (i == 2 && !chk2.isChecked()){

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
            else if (i == 3 && !chk3.isChecked()){

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

                if (score == 7){
                    linesPlayer1 +=1;
                    linesPlayer2 +=1;
                    drawLinesPlayer1();
                    drawLinesPlayer2();
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
            rollsLeft = 1;
            showWinner("It's a tie!", "Play another round!");

        } else if(scorePlayer1 > scorePlayer2){
            //Player1 wins this round

            //Determine how many lines can be swiped away
            switch (scorePlayer1){
                case 722:
                    linesPlayer1 -= 2;
                    swipeLines = "2";
                    break;
                case 733:
                    linesPlayer1 -= 2;
                    swipeLines = "2";
                    break;
                case 744:
                    linesPlayer1 -= 2;
                    swipeLines = "2";
                    break;
                case 755:
                    linesPlayer1 -= 2;
                    swipeLines = "2";
                    break;
                case 766:
                    linesPlayer1 -= 2;
                    swipeLines = "2";
                    break;
                case 769:
                    linesPlayer1 -= 3;
                    swipeLines = "3";
                    break;
                case 799:
                    if (linesPlayer1 == 5){
                        //uitschakelen speler, andere speler wint
                    }else {
                        linesPlayer1 = 0;
                        swipeLines = "ALL";
                        break;
                    }
                default:
                    linesPlayer1 -= 1;
                    swipeLines = "1";
                    break;
            }
            canSwipePlayer1 = true;
            showWinner(player1.getText() + " wins this round", "Swipe away " + swipeLines + " of your lines.");

        } else{
            //Player2 wins this round

            //Determine how many lines can be swiped away
            switch (scorePlayer2){
                case 722:
                    linesPlayer2 -= 2;
                    swipeLines = "2";
                    break;
                case 733:
                    linesPlayer2 -= 2;
                    swipeLines = "2";
                    break;
                case 744:
                    linesPlayer2 -= 2;
                    swipeLines = "2";
                    break;
                case 755:
                    linesPlayer2 -= 2;
                    swipeLines = "2";
                    break;
                case 766:
                    linesPlayer2 -= 2;
                    swipeLines = "2";
                    break;
                case 769:
                    linesPlayer2 -= 3;
                    swipeLines = "3";
                    break;
                case 799:
                    if (linesPlayer2 == 5){
                        //uitschakelen speler, andere speler wint
                    }else {
                        linesPlayer2 = 0;
                        swipeLines = "ALL";
                        break;
                    }
                default:
                    linesPlayer2 -= 1;
                    swipeLines = "1";
                    break;
            }
            canSwipePlayer2 = true;
            showWinner(player2.getText() + " wins this round!", "Swipe away " + swipeLines + " of your lines.");

        }


    }

    public void showWinner (String winner, String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(winner);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //close alertDialog
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void drawLinesPlayer1(){
        switch (linesPlayer1){
            case 1:
                lines1img.setBackgroundResource(R.drawable.lines1);
                break;
            case 2:
                lines1img.setBackgroundResource(R.drawable.lines2);
                break;
            case 3:
                lines1img.setBackgroundResource(R.drawable.lines3);
                break;
            case 4:
                lines1img.setBackgroundResource(R.drawable.lines4);
                break;
            case 5:
                lines1img.setBackgroundResource(R.drawable.lines5);
                break;
            case 6:
                lines1img.setBackgroundResource(R.drawable.lines6);
                break;
            case 7:
                lines1img.setBackgroundResource(R.drawable.lines7);
                break;
            case 8:
                lines1img.setBackgroundResource(R.drawable.lines8);
                break;
            case 9:
                lines1img.setBackgroundResource(R.drawable.lines9);
                break;
            case 10:
                lines1img.setBackgroundResource(R.drawable.lines10);
                break;
            default:
                if(linesPlayer1 > 5){
                    lines1img.setBackgroundResource(R.drawable.lines5);
                }else {
                    lines1img.setBackgroundResource(R.drawable.winner);
                    winner = (String) player1.getText();
                    loser = (String) player2.getText();
                    showFinalWinner(player1.getText() + " has WON the game!", "You can share your victory now!");
                }
                break;
        }
    }

    public void drawLinesPlayer2(){
        switch (linesPlayer2){
            case 1:
                lines2img.setBackgroundResource(R.drawable.lines1);
                break;
            case 2:
                lines2img.setBackgroundResource(R.drawable.lines2);
                break;
            case 3:
                lines2img.setBackgroundResource(R.drawable.lines3);
                break;
            case 4:
                lines2img.setBackgroundResource(R.drawable.lines4);
                break;
            case 5:
                lines2img.setBackgroundResource(R.drawable.lines5);
                break;
            case 6:
                lines2img.setBackgroundResource(R.drawable.lines6);
                break;
            case 7:
                lines2img.setBackgroundResource(R.drawable.lines7);
                break;
            case 8:
                lines2img.setBackgroundResource(R.drawable.lines8);
                break;
            case 9:
                lines2img.setBackgroundResource(R.drawable.lines9);
                break;
            case 10:
                lines2img.setBackgroundResource(R.drawable.lines10);
                break;
            default:
                if(linesPlayer2 > 5){
                    lines2img.setBackgroundResource(R.drawable.lines5);
                }else {
                    lines2img.setBackgroundResource(R.drawable.winner);
                    //show Final Winner
                    winner = (String) player2.getText();
                    loser = (String) player1.getText();
                    showFinalWinner(player2.getText() + " has WON the game!", "You can share your victory now!");
                }
                break;
        }
    }

    public void showFinalWinner (String Finalwinner, String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(Finalwinner);
        builder.setMessage(message);
        builder.setNeutralButton("Share Victory on Facebook", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                shareOnFB();
                resetGame();

            }
        });
        builder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                resetGame();
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void resetGame() {

        //reset game
        linesPlayer1 = 5;
        linesPlayer2 = 5;
        scorePlayer1 = 0;
        scorePlayer2 = 0;
        score = 0;
        rollsLeft = 3;
        canSwipePlayer1 = false;
        canSwipePlayer2 = false;
        player1active = true;
        drawLinesPlayer1();
        drawLinesPlayer2();
        tv_score1.setText("Score: " + score);
        tv_score2.setText("Score: " + score);
    }

    private void shareOnFB() {

        //share Facebook OpenGraph Story
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_launcher);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .setUserGenerated(true)
                .build();

        // Create an object
        ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
                .putString("og:type", "games.victory")
                .putString("og:title", winner + " has WON from " + loser)
                .putString("og:description", "The classic Flemish pubgame on Android")
                .putString("og:image:url","android.resource://" + getPackageName() + "/drawable/" + "logo_launcher.png")
                .build();
        // Create an action
        ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
                .setActionType("games.celebrate")
                .putObject("games:victory", object)
                .putPhoto("image", photo)
                .build();
        // Create the content
        ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
                .setPreviewPropertyName("games:victory")
                .setAction(action)
                .build();

        ShareDialog.show(MainActivity.this, content);
    }


}
