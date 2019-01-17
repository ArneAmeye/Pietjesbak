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
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {


    private EditText username, useremail, passw, passw2;
    private ProgressBar loadingProgress;
    private Button regBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.regName);
        useremail = findViewById(R.id.regEmail);
        passw = findViewById(R.id.regPassword);
        passw2 = findViewById(R.id.regPassword2);
        loadingProgress = findViewById(R.id.regProgressbar);
        regBtn = findViewById(R.id.regBtn);

        loadingProgress.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        regBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                regBtn.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
                final String email = useremail.getText().toString();
                final String password = passw.getText().toString();
                final String password2  = passw2.getText().toString();
                final String name = username.getText().toString();

                if (email.isEmpty() || name.isEmpty() || password.isEmpty() || password2.isEmpty() || !password.equals(password2)){

                    //fields not filled in or passwords don't match for registering process.
                    showMessage("Please verify all fields");
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);

                }else {
                    // All fields are filled in and passwords match
                    //create user account
                    CreateUserAccount(email,name,password);
                }

            }
        });

    }

    private void CreateUserAccount(String email, final String name, String password) {

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //account registered
                            showMessage("Account successfully registered");
                            //add name to account
                            updateAccount(name , mAuth.getCurrentUser());

                        }
                        else{
                            //account registration failed
                            showMessage("account registration failed" + task.getException().getMessage().toString());
                            regBtn.setVisibility(View.VISIBLE);
                            loadingProgress.setVisibility(View.INVISIBLE);
                        }

                    }
                });

    }

    //update account with their username
    private void updateAccount(String name, FirebaseUser currentUser) {
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        currentUser.updateProfile(profileUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //user name added to profile
                            showMessage("Account is setup and ready");
                            updateUI();
                        }
                    }
                });
    }

    private void updateUI() {
        //Intent to go to pregame screen
        Intent intent = new Intent(getApplicationContext(), PregameActivity.class);
        startActivity(intent);
        finish();

    }

    //show Toast messages
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG ).show();
    }

}
