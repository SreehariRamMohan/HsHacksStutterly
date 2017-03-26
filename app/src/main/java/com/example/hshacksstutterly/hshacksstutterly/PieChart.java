package com.example.hshacksstutterly.hshacksstutterly;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
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


public class PieChart extends AppCompatActivity {

     private static String TAG = "Hi";

    private float[] yData; //= {25.3f, 10.62f, 66.6f, 33.2f, 46.001f, 16.09f, 23.896f};
    private String[] xData;  //= {"Mitch", "Jessica", "Mohamed", "Kelsey", "Sam", "Ashley", "Robert"};
    com.github.mikephil.charting.charts.PieChart pieChart;

    ArrayList<String> troubleWordsX = new ArrayList<>();
    ArrayList<String> frequencyWordsY = new ArrayList<>();
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pie_chart_layout);
        b = (Button)findViewById(R.id.updateChart);

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
        ref1.child("Words").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> list = new ArrayList<String>();


                Iterator i = dataSnapshot.getChildren().iterator();
                Log.v("Hi", "In the on data change method that Anjan Wrote");

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

                    troubleWordsX.add(key);
                    //System.out.println(troubleWordsX.get(1));

                    //System.out.println(String.valueOf(Collections.frequency(list, key)));
                    frequencyWordsY.add(String.valueOf(Collections.frequency(list, key)));

                    //System.out.println(key + "*********" + Collections.frequency(list,key));
                }




            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*
        for(int k = 0; k < troubleWordsX.size(); k++) {
            System.out.print(troubleWordsX.get(k) + " *******");
        }
        System.out.println("");
        for(int j = 0; j < frequencyWordsY.size(); j++) {
            System.out.print(frequencyWordsY.get(j) + " ****");
        }
        */
        //Log.v("Size", "The size of array a is " + troubleWordsX.size() + "      " + "THE SIZE OF ARRAY B IS " + frequencyWordsY.size());

        Log.v("Hi", "In pie chart onCreate");


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pieChart = (com.github.mikephil.charting.charts.PieChart) findViewById(R.id.pieDAY);
                String str = "Percentage of stuttered words from total ";

                pieChart.setDrawEntryLabels(true);
                pieChart.setCenterTextSize(10);
                pieChart.setEntryLabelTextSize(20);
                pieChart.setEntryLabelColor(Color.BLACK);
                pieChart.setRotationEnabled(true);
                pieChart.setCenterText("Stuttered Words");
                pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                    @Override
                    public void onValueSelected(Entry e, Highlight h) {
                        Log.v("Hi", "Inside on chart value selected " + e.toString() );
                        Log.v("Hi", h.toString() + " this is h");


                    }

                    @Override
                    public void onNothingSelected() {

                    }
                });

                addDataSet();
            }
        });





        System.out.println("****" + System.currentTimeMillis());
        //the two array lists should be populated.
        /*
        for(int i = 0; i < troubleWordsX.size(); i++) {
            System.out.print(troubleWordsX.get(i) + " *******");
        }
        System.out.println("");
        for(int j = 0; j < frequencyWordsY.size(); j++) {
            System.out.print(frequencyWordsY.get(j) + " ****");
        }
        System.out.println("------------------------");
        */
    }

    private void addDataSet() {
        Log.v("Hi", "add data set started");
        ArrayList<PieEntry> yEntry = new ArrayList<>();
        ArrayList<String> xEntry = new ArrayList<>();
        float[] yData = new float[frequencyWordsY.size()];

         for(int l = 0; l < frequencyWordsY.size(); l++) {
             yData[l] = Float.valueOf(frequencyWordsY.get(l));
         }

         String[] xData = new String[troubleWordsX.size()];

         for(int u = 0; u < troubleWordsX.size(); u++) {
             xData[u] = (troubleWordsX.get(u));
         }

         Log.v("Size", "The size of array one is " + frequencyWordsY.size() + " and the size of array 2 is " + troubleWordsX.size());

        for(int i = 0; i < yData.length; i++) {
            yEntry.add(new PieEntry(yData[i],xData[i]));
        }

        for(int i = 0; i < xData.length; i++) {
            xEntry.add(xData[i]);
        }
        //create data set
        PieDataSet pieDataSet = new PieDataSet(yEntry, "Words Stuttered");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);

        pieDataSet.setColors(colors);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

    }
}
