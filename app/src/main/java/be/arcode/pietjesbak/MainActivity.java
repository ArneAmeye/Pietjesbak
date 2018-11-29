package be.arcode.pietjesbak;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button roll;
    TextView player1, player2, score1, score2;
    ImageView dice1img, dice2img, dice3img;

    private int min = 0, max = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        roll = (Button) findViewById(R.id.roll);
        player1 = (TextView) findViewById(R.id.player1);
        player2 = (TextView) findViewById(R.id.player2);
        score1 = (TextView) findViewById(R.id.score1);
        score2 = (TextView) findViewById(R.id.score2);
        dice1img = (ImageView) findViewById(R.id.dice1);
        dice2img = (ImageView) findViewById(R.id.dice2);
        dice3img = (ImageView) findViewById(R.id.dice3);

        roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diceRoll();
            }
        });
    }


    private void diceRoll(){

        for (int i = 0; i <= 3; i++){
            Random r = new Random();
            int random = r.nextInt((max - min) + 1) + min;

            if (i == 1){
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



}
