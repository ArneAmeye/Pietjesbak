package be.arcode.pietjesbak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class PregameActivity extends AppCompatActivity {

    Button start, logout;
    EditText editPlayer1, editPlayer2;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregame);


        editPlayer1 = (EditText) findViewById(R.id.player1Field);
        editPlayer2 = (EditText) findViewById(R.id.player2Field);
        start = (Button) findViewById(R.id.startGameBtn);
        logout = (Button) findViewById(R.id.logoutBtn);
        mAuth = FirebaseAuth.getInstance();

        editPlayer1.setEnabled(false);



        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check if player names are filled in
                if (editPlayer1.getText().toString().trim().length() == 0 || editPlayer2.getText().toString().trim().length() == 0){
                    Toast.makeText(PregameActivity.this, "Please fill in player name!", Toast.LENGTH_SHORT).show();
                }else {
                    //Intent to GameScreen / MainActivity
                    Intent intent = new Intent(PregameActivity.this, MainActivity.class);
                    intent.putExtra("namePlayer1", editPlayer1.getText().toString().trim());
                    intent.putExtra("namePlayer2", editPlayer2.getText().toString().trim());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //logout from Firebase connection and show message
                mAuth.signOut();
                Toast.makeText(getApplicationContext(), "logged out successfully", Toast.LENGTH_LONG ).show();

                //Bring user back to login screen
                Intent intent = new Intent(PregameActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null){
            //user logged in, fill in username for player1
            editPlayer1.setText(user.getDisplayName());

        }else{
            //user not logged in, return to login screen
            Intent intent = new Intent(PregameActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

}
