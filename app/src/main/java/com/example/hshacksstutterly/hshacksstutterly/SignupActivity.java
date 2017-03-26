package com.example.hshacksstutterly.hshacksstutterly;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    Button signup;
    EditText emailfield;
    EditText pass;
    EditText email;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        signup = (Button)findViewById(R.id.signup);
        emailfield = (EditText)findViewById(R.id.email);
        pass = (EditText)findViewById(R.id.password);


         email = (EditText) findViewById(R.id.therapistEmail);





        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean isChecked = ((CheckBox) findViewById(R.id.checkBox)).isChecked();
                final String pathEmail = email.getText().toString();
                final String email = emailfield.getText().toString();
                String password = pass.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.




                                if(task.isSuccessful()){
                                    Toast.makeText(SignupActivity.this, "Success! Welcome to your Stutterly Portal!",
                                            Toast.LENGTH_SHORT).show();

                                    if(isChecked) {
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("TherapistEmail");
                                        //HashMap<String, Object> coach = new HashMap<String, Object>();
                                        ref.setValue(pathEmail);
                                        Toast.makeText(SignupActivity.this, "Linked your therapist: " + pathEmail + " to your account", Toast.LENGTH_LONG).show();

                                    }

                                    startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                                }
                                else if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Registration Error. Try a new email address!",
                                            Toast.LENGTH_SHORT).show();
                                }


                                // ...
                            }
                        });

            }
        });
    }
}
