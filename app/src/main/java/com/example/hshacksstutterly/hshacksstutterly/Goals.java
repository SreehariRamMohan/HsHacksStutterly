package com.example.hshacksstutterly.hshacksstutterly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Goals extends AppCompatActivity {

    private EditText mWordToQuit;
    private EditText mDateToQuitBy;
    ListView listview;
    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    Button btn;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Goals");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        btn = (Button)findViewById(R.id.button3);
            mWordToQuit =  (EditText) findViewById(R.id.wordToQuitStutteringOn);
            //mDateToQuitBy = (EditText) findViewById(R.id.dateTOQuitBy);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
            }
        });
        Button submitGoalButton = (Button) findViewById(R.id.createGoalButton);
        submitGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = mWordToQuit.getText().toString().trim();
                //String date = mDateToQuitBy.getText().toString();


                HashMap<String, Object> map = new HashMap<String, Object>();
                //map.put(word, date);
                ref.updateChildren(map);

                Toast.makeText(getApplicationContext(), "Added your goal to our database",
                        Toast.LENGTH_SHORT).show();




            }
        });

        listview = (ListView)findViewById(R.id.listview);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listview.setAdapter(arrayAdapter);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Goals");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                //Set<String> set2 = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                while(i.hasNext()){
                    String key = ((DataSnapshot) i.next()).getKey();
                    String val = dataSnapshot.child(key).getValue().toString();
                    String f = "Learn '"+ key + "' by " + val;
                    set.add(f);

                }
                list.clear();
                list.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
