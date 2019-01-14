package be.arcode.pietjesbak;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class RulesActivity extends AppCompatActivity {

    Button close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        close = (Button) findViewById(R.id.closeRules);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //go back to game screen
                finish();
            }
        });

    }
}
