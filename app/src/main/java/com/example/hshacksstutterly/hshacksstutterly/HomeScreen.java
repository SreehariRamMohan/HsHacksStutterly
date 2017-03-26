package com.example.hshacksstutterly.hshacksstutterly;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeScreen extends AppCompatActivity {

    private TextView mTextMessage;
    Button logout;
    Button go;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        Button practiceButton = (Button) findViewById(R.id.practiceID);
        practiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SpeechActivity.class));
            }
        });

        Button goalsButton = (Button) findViewById(R.id.goalsID);
        goalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Goals.class));
            }
        });

        Button summaryButton = (Button) findViewById(R.id.sendScoreID);
        summaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Summary.class));
            }
        });

        Button analyzeButton = (Button) findViewById(R.id.analyzeID);
        analyzeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PieChart.class));
            }
        });

    }

}
