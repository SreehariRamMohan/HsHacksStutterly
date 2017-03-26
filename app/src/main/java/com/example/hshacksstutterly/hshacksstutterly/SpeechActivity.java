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
import java.util.StringTokenizer;

public class SpeechActivity extends AppCompatActivity {
    final int REQUEST_CODE = 1234;
    ArrayList<String> results;
    Button start;
    public static int longestindex;
    Button load;
    public static TextView tv;
    public static TextView tv1;
    WebView webview;
    String where;
    int moat;
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
                tv1.setText(newer);
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



            System.out.println("AYYO" + newer);

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
            detect(results);


            //tv.setText("You said: " + theysaid + ". You should have said: " + newer + ". You made " + moat + " errors at the word: " + where );

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void detect(ArrayList<String> list){
       /*
       int longest = 0;//the index of it
       for(int i = 0; i<list.size(); i++){
          if(list.get(i).length()>list.get(longest).length() ){
              longest = i;
          }
       }
       */
        int longest = 0;//the index of it
        int sameWordCount = 0;
        int versionNumber = 0;
        for(int i = 0; i<list.size(); i++){
//            if(list.get(i).length()>list.get(longest).length()){
//                longest = i;
//            }
            ArrayList al = new ArrayList();
            StringTokenizer st = new StringTokenizer(list.get(i), " ");
            while(st.hasMoreTokens()) {
                al.add(st.nextToken());
            }
            int repetitions = 0;
            for(int j = 0; i < al.size(); i++) {
                String current = al.get(j).toString();
                for(int k = 0; k < al.size(); k++) {
                    if(al.get(k) == current) {
                        repetitions++;
                    }
                }
                if(repetitions > sameWordCount) {
                    sameWordCount = repetitions;
                    versionNumber = j;
                }

            }


        }
        /*
        if(list.get(versionNumber).length()<newer.length()-1){
            tv.setText("Please try again, repeating the entire sentence.");
            return;
        }
           */
        String whattheysaid = list.get(versionNumber).toString();
        String[] words = newer.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");//what is supposed to be said
        tv.setText(whattheysaid);
        String[] wordsa = whattheysaid.split(" ");//what they said
        for(int i =0; i<wordsa.length; i++){
            wordsa[i] = wordsa[i].toLowerCase();
            wordsa[i] = wordsa[i].replaceAll("[^a-zA-Z ]", "");
        }
        //System.out.println(words.length + "," + wordsa.length);

       /*
       int currentstutter = 0;
       String wordstutteredmost = "";
       int thisstutter = 0;
       int totalstutter = 0;
       int totalstutter2 =0;
       for(int i = 0; i<words.length; i++){
               String match = words[i];
           totalstutter2 = 0;
           for(int j = i+totalstutter; j<wordsa.length; j++){
               if(wordsa[j].contains(match)){
                   //System.out.println(match + "," + wordsa[j]);
                   if(thisstutter>currentstutter){
                       currentstutter = thisstutter;
                       wordstutteredmost = wordsa[j];
                   }
                   thisstutter = 0;
                   totalstutter+=totalstutter2;
                   break;
               }
               if(j == wordsa.length-1){
                   //System.out.println("broke");
                   totalstutter+=1;
                   break;
               }
               //System.out.println("iterate " + j);
               totalstutter2++;
               thisstutter++;

           }
       }
       */
        ArrayList<String> wordsstuttered = new ArrayList<>();
        ArrayList<Integer> timesstuttered = new ArrayList<>();
        boolean stutter = false;
        String wordstutteredmost = "";
        for(int i = 0; i<words.length; i++){
            for(int j = 0; j<wordsa.length; j++){
                if(words[i].contains(wordsa[j]) || wordsa[j].contains(wordsa[i]) || partiallyRight(words[i], wordsa[j])){//for now partiallyright does nothing
                    if(stutter=true){
                        System.out.println(words[i] + "----" + wordsa[j]);
                        wordsstuttered.add(words[i]);
                        timesstuttered.add(detect2(words, wordsa, i, j));
                        stutter = false;
                    }
                    stutter = false;
                }
                stutter = true;
            }
        }

        String a1 = "";
        String b1 = "";
        for(int i = 0; i<wordsstuttered.size(); i++){
            if(i!=wordsstuttered.size()-1) {
                a1.concat(wordsstuttered.get(i) + ", ");
                b1.concat(timesstuttered.get(i) + " times, ");
            }
            else{
                a1.concat(wordsstuttered.get(i) + ".");
                b1.concat(timesstuttered.get(i) + " times.");
            }
        }
        if(wordsstuttered.size()==0) {
            System.out.println(a1);
            tv.setText("Good job! All correct! You said" + whattheysaid );
        }else{
            tv.setText("You said: " + whattheysaid + ". You stuttered " + a1 + " You stuttered them (respectively)" + b1);

        }

    }
    public int detect2(String[] a, String[] b, int i, int j){
        char firstchar = a[i].charAt(0);
        int numof = 0;
        for(int x = 0; x<j-i; x++){
            if(b[i+x].charAt(0) == b[j].charAt(0)){
                numof++;
            }
        }
        return numof;
    }

    public boolean partiallyRight(String original, String user){
        char[] a = original.toCharArray();
        char[] b = user.toCharArray();
        //hardcoded value of 60%
        int num = 0;
        int den = b.length;
        for(int i = 0; i<a.length; i++){
            for(int j = 0; j<b.length; j++){
                if(b[j] == a[i]) num++;
            }
        }
        double frac = num/den;
        //if(frac>0.6) return true;
        return false;
    }



}
