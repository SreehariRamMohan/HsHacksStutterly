package com.example.hshacksstutterly.hshacksstutterly;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private static final String TAG = "ForgotPassword";
    String emailtosend;
    Button send;
    EditText email;
    TextView text;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        send = (Button) findViewById(R.id.enterpassword);
        email = (EditText) findViewById(R.id.passwordclass);
        text = (TextView) findViewById(R.id.textinfo);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!email.getText().toString().trim().isEmpty()) {
                    emailtosend = email.getText().toString().trim();
                    auth.sendPasswordResetEmail(emailtosend)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        text.setText("A confirmation email has been sent to " + emailtosend + ".");
                                    }
                                }
                            });
                }
            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .5));
    }
}
