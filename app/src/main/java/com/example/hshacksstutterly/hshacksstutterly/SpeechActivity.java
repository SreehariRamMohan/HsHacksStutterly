package com.example.hshacksstutterly.hshacksstutterly;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SpeechActivity extends AppCompatActivity {
    final int REQUEST_CODE = 1234;
    ArrayList<String> results;
    Button start;
    Button load;
    TextView tv;
    TextView tv1;
    WebView webview;
    String newer = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);
        start = (Button)findViewById(R.id.start);
        tv = (TextView)findViewById(R.id.textView);
        tv1 = (TextView)findViewById(R.id.textView3);
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
                        tv1.setText("You must say: " + newer);

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
            int i = 359;
            while(html.charAt(i)!='<'){
                newer.concat(String.valueOf(html.charAt(i)));
            }
            System.out.println("AYYO" + newer);


        }
    }
    public void startVoiceRecognitionActivity(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");
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
            int longestindex = 0;
            int longest = 0;
            for(int i = 0; i<results.size(); i++){
                if(results.get(i).length()>longest){
                    longest = results.get(i).length();
                    longestindex = i;
                }

            }
            tv.setText("You said: " + results.get(longestindex));
            detect(results.get(longestindex));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void detect(String text){

    }

}

