package com.example.hshacksstutterly.hshacksstutterly;

import android.content.Intent;
import android.net.Uri;
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
import java.util.Collections;
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
        textView.setText("");
        send = (Button)findViewById(R.id.button2);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("TherapistEmail");
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sendTo = dataSnapshot.getValue().toString();
                tv.append(sendTo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ref1.child("Goals").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //ArrayList<String> list = new ArrayList<Template>();
                thenote += "My goals: ";

                Iterator i = dataSnapshot.getChildren().iterator();


                while(i.hasNext()){
                    String key = (((DataSnapshot) i.next()).getKey());
                    String value = ((dataSnapshot).child(key).getValue().toString());
                    thenote += ("I want to stop stuttering " + key+" by "+value + ". " + '\n');
                    //list.get(k).setGoal((((DataSnapshot)i.next()).getKey()).toString());

                }
                textView.setText(thenote);
                System.out.println(thenote);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref1.child("Words").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> list = new ArrayList<String>();


                Iterator i = dataSnapshot.getChildren().iterator();


                while(i.hasNext()){
                    String key = (((DataSnapshot) i.next()).getKey());
                    for(int m = 0; m<key.length(); m++){
                        if (!Character.isLetter(key.charAt(m))){
                            key = key.substring(0, m);
                        }
                    }

                    list.add(key);


                    //list.get(k).setGoal((((DataSnapshot)i.next()).getKey()).toString());

                }
                Set<String> unique = new HashSet<String>(list);
                for (String key : unique) {
                    thenote += "I stuttered: " + key + " " + Collections.frequency(list, key) + " times.";
                }
                textView.setText(thenote);
                System.out.println(thenote);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, sendTo);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Speech Report");
                intent.putExtra(Intent.EXTRA_TEXT, thenote);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });







    }


}
