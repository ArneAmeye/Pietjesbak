package be.arcode.pietjesbak;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    private EditText userEmail, userPassw;
    private Button loginBtn, registerBtn;
    private ProgressBar loginProgressBar;
    private FirebaseAuth mAuth;
    private Intent PregameActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmail = findViewById(R.id.loginMail);
        userPassw = findViewById(R.id.loginPass);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);
        loginProgressBar = findViewById(R.id.loginProgress);
        mAuth = FirebaseAuth.getInstance();
        PregameActivity = new Intent(this, PregameActivity.class);

        loginProgressBar.setVisibility(View.INVISIBLE);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProgressBar.setVisibility(View.VISIBLE);
                loginBtn.setVisibility(View.INVISIBLE);

                final String mail = userEmail.getText().toString();
                final String passw = userPassw.getText().toString();

                if(mail.isEmpty() || passw.isEmpty()){
                    showMessage("Please verify all fields");
                    loginBtn.setVisibility(View.VISIBLE);
                    loginProgressBar.setVisibility(View.INVISIBLE);

                }else{
                    signIn(mail,passw);
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });


    }

    private void signIn(String mail, String passw) {

        mAuth.signInWithEmailAndPassword(mail,passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    loginProgressBar.setVisibility(View.INVISIBLE);
                    loginBtn.setVisibility(View.VISIBLE);
                    showMessage("Logged in successfully");
                    updateUI();
                }else{
                    showMessage(task.getException().getMessage());
                    loginBtn.setVisibility(View.VISIBLE);
                    loginProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void updateUI() {

        startActivity(PregameActivity);
        finish();

    }


    //show message via toast
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null){
            //user is already signed in, skip to pregame screen
            showMessage("User recognized, signed in!");
            updateUI();

        }
    }
}
