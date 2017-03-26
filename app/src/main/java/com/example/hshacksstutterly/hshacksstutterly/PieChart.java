package com.example.hshacksstutterly.hshacksstutterly;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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

    private float[] yData = {25.3f, 10.62f, 66.6f, 33.2f, 46.001f, 16.09f, 23.896f};
    private String[] xData = {"Mitch", "Jessica", "Mohamed", "Kelsey", "Sam", "Ashley", "Robert"};
    com.github.mikephil.charting.charts.PieChart pieChart;

    ArrayList<String> troubleWordsX = new ArrayList<>();
    ArrayList<String> frequencyWordsY = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pie_chart_layout);




        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
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
                    troubleWordsX.add(key);
                    frequencyWordsY.add(String.valueOf(Collections.frequency(list, key)));
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        Log.v("Hi", "In pie chart onCreate");

        pieChart = (com.github.mikephil.charting.charts.PieChart) findViewById(R.id.pieDAY);
        String str = "Percentage of stuttered words from total ";

        pieChart.setDrawEntryLabels(true);
        pieChart.setCenterTextSize(10);
        pieChart.setRotationEnabled(true);
        pieChart.setCenterText("Stuttered Words");

        addDataSet();


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

    }

    private void addDataSet() {
        Log.v("Hi", "add data set started");
        ArrayList<PieEntry> yEntry = new ArrayList<>();
        ArrayList<String> xEntry = new ArrayList<>();

        for(int i = 0; i < yData.length; i++) {
            yEntry.add(new PieEntry(yData[i]));
        }

        for(int i = 0; i < xData.length; i++) {
            xEntry.add(xData[i]);
        }
        //create data set
        PieDataSet pieDataSet = new PieDataSet(yEntry, "Employee Sales");
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
