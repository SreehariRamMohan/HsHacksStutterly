package com.example.hshacksstutterly.hshacksstutterly;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Goals extends AppCompatActivity {

    private EditText mWordToQuit;
    private EditText mDateToQuitBy;


    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Goals");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

            mWordToQuit =  (EditText) findViewById(R.id.wordToQuitStutteringOn);
            mDateToQuitBy = (EditText) findViewById(R.id.dateTOQuitBy);

        Button submitGoalButton = (Button) findViewById(R.id.createGoalButton);
        submitGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = mWordToQuit.getText().toString().trim();
                String date = mDateToQuitBy.getText().toString();


                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put(word, date);
                ref.updateChildren(map);

                Toast.makeText(getApplicationContext(), "Added your goal to our database",
                        Toast.LENGTH_SHORT).show();




            }
        });

    }
}
