package com.example.hshacksstutterly.hshacksstutterly;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static android.R.id.list;

public class Summary extends AppCompatActivity {
    String sendTo;
    String thenote;
    TextView tv;
    TextView textView;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);


        //update the approve text view so the user can see who they are sending it to.
        textView = (TextView)findViewById(R.id.textView);
        tv = (TextView) findViewById(R.id.approveTextView);
        send = (Button)findViewById(R.id.button2);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("TherapistEmail");
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Goals");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sendTo = dataSnapshot.getValue().toString();
                tv.append("\n " + sendTo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //ArrayList<String> list = new ArrayList<Template>();


                Iterator i = dataSnapshot.getChildren().iterator();


                while(i.hasNext()){
                    String key = (((DataSnapshot) i.next()).getKey());
                    String value = ((dataSnapshot).child(key).getValue().toString());
                    thenote += ("I want to stop stuttering " + key+" by "+value + ". ");
                    //list.get(k).setGoal((((DataSnapshot)i.next()).getKey()).toString());

                }
                textView.setText(thenote);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });







    }


}
