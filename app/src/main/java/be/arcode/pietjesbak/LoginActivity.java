package be.arcode.pietjesbak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    Button start;
    EditText editPlayer1, editPlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editPlayer1 = (EditText) findViewById(R.id.player1Field);
        editPlayer2 = (EditText) findViewById(R.id.player2Field);
        start = (Button) findViewById(R.id.startGameBtn);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check if player names are filled in
                if (editPlayer1.getText().toString().trim().length() == 0 || editPlayer2.getText().toString().trim().length() == 0){
                    Toast.makeText(LoginActivity.this, "Please fill in player names!", Toast.LENGTH_SHORT).show();
                }else {
                    //Intent to GameScreen / MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("namePlayer1", editPlayer1.getText().toString().trim());
                    intent.putExtra("namePlayer2", editPlayer2.getText().toString().trim());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            }
        });



    }

}
