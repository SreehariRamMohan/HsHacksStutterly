package com.example.hshacksstutterly.hshacksstutterly;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class SpeechActivity extends AppCompatActivity {
    final int REQUEST_CODE = 1234;
    ArrayList<String> results;
    Button start;
    public static int longestindex;
    Button load;
    public static TextView tv;

    WebView webview;
    String where;
    int moat;
    String newer = "";
    int value;

    ArrayList<String> testArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        start = (Button)findViewById(R.id.start);
        tv = (TextView)findViewById(R.id.textView);
        load = (Button)findViewById(R.id.load);
        webview = (WebView)findViewById(R.id.web);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new MyJavaScriptInterface(this), "HtmlViewer");




        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0)
        {
            start.setEnabled(false);
            start.setText("Recognizer not present");
        }


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tv1.setText(newer);
                //tv1.setText("You must say: " + newer);
                System.out.println("HEY YOU SAID HERE " );
                startVoiceRecognitionActivity();
            }
        });
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webview.loadUrl("http://sentence-generator.appspot.com/");

                webview.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        webview.loadUrl("javascript:window.HtmlViewer.showHTML" +
                                "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");

                    }
                });

                start.setEnabled(true);
            }
        });





    }
    class MyJavaScriptInterface{

        private Context ctx;


        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }
        @JavascriptInterface
        public void showHTML(String html) {
            newer = "";
            System.out.print(html);
            //int i = 360;
            //while(html.charAt(i)!='<'){
            //    newer.concat(String.valueOf(html.charAt(i)));
            //}
            for(int i = html.indexOf("<p class=")+16; i<html.indexOf("<p class=")+1000; i++){
                if(html.charAt(i)=='<' || html.charAt(i)=='/'){
                    break;
                }
                else{
                    newer += html.charAt(i);
                }
            }
            /*
            TextView tv1 = (TextView)findViewById(R.id.textView3);
            tv1.setText("Say: " + newer);
            */

            if(newer.contains("*")) {
                load.performClick();
            } else {
                System.out.println("AYYO" + newer);
            }

        }
    }
    public void startVoiceRecognitionActivity(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say: " + newer);
        startActivityForResult(intent, REQUEST_CODE);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            // Populate the wordsList with the String values the recognition engine thought it heard
            results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            //System.out.println("HEY YOU SAID " + results.get(0));
            detect(results);  //THE REAL ONE
            // for testing purposes
            //detect(testArrayList);

            //tv.setText("You said: " + theysaid + ". You should have said: " + newer + ". You made " + moat + " errors at the word: " + where );

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void detect(ArrayList<String> usersword){
        int longest = 0;
        int longestindex = 0;
        for(int i = 0; i<usersword.size(); i++){
            if(usersword.get(i).length()>longest){
                longest = usersword.get(i).length();
                longestindex = i;
            }
        }

        String[] userswords = usersword.get(longestindex).replaceAll("[^a-zA-Z ]", "").toLowerCase().split(" ");
        //String[] userswords = "watson hopped on to the up up up up u".replaceAll("[^a-zA-Z ]", "").toLowerCase().split(" ");
        System.out.println("The tokenized string array is below");
        for(int i = 0; i < userswords.length; i++) {
            System.out.print(userswords[i] + " ");
        }
        System.out.println("--------------------");


        ArrayList<String> stuttered = new ArrayList<>();
        int counter = 0;
        for(int i = 0; i<userswords.length-1; i++){
            if(userswords[i].equals(userswords[i+1])){
                counter++;
                System.out.println(userswords[i] + "," + i + "," + counter);
                if(i + 2 >= userswords.length || !(userswords[i+1].equals(userswords[i]))) {
                    Log.v("Hi", userswords[i] + "");
                    counter = 0;
                }
            }else if(!(userswords[i].equals(userswords[i+1])) && counter>=1){

                if(userswords[i].charAt(0) == userswords[i+1].charAt(0) ){
                    stuttered.add(userswords[i+1]);

                    Log.v("Hi", userswords[i+1] + "");
                } else if(( (userswords[i].charAt(1) == userswords[i+1].charAt(0)))) {
                    Log.v("Hi", "Special Case 2nd letter equal to first letter of next word");
                    System.out.println(userswords[i+1] + " ***");
                    stuttered.add(userswords[i+1]);
                } else {
                    stuttered.add(userswords[i]);

                    Log.v("Hi", userswords[i] + "");
                }
                counter = 0;
            }
        }

    }
// WHENEVER THE LOAD BUTTON IS PRESSED IT AUTOMATICALLY SHOWS THE TEXT. CHANGE THE VALUE WITHIN FIREBASE, AND HAVE A VALUE EVENT LISTENER IN ONCREATE


}

