package com.example.hshacksstutterly.hshacksstutterly;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    String goals;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);


        //update the approve text view so the user can see who they are sending it to.

        tv = (TextView) findViewById(R.id.approveTextView);
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
                ArrayList<Template> list = new ArrayList<Template>();


                Iterator i = dataSnapshot.getChildren().iterator();
                Iterator i2 = dataSnapshot.getChildren().iterator();

                int k = 0;
                while(i.hasNext()){
                    list.add(new Template());
                    list.get(k).setGoal((((DataSnapshot)i.next()).getKey()).toString());
                    k++;
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







    }

    public class Template {
        String goal;
        String date;

        public void setGoal(String goal) {
            this.goal = goal;

        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
