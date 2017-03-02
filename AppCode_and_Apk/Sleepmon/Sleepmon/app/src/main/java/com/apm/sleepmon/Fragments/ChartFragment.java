package com.apm.sleepmon.Fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apm.sleepmon.R;
import com.apm.sleepmon.SimpleLineChart;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class ChartFragment extends Fragment {
    private SimpleLineChart mSimpleLineChart;
    private TextView suggestion, gradeText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart_fragment, null);
        init(view);
        return view;
    }

    public void init(View view) {
        suggestion = (TextView) view.findViewById(R.id.suggest);
        gradeText = (TextView) view.findViewById(R.id.grade);
        myDB2 dbHelp = new myDB2(getActivity());
        SQLiteDatabase sqLiteDatabase = dbHelp.getWritableDatabase();
        try {
            Cursor cursor = sqLiteDatabase.rawQuery("select * from grade_table", null);

            int counts = Math.min(cursor.getCount(), 7);
            String[] time = new String[counts];
            double[] grade = new double[counts];
            if (cursor.moveToLast() == true) {
                for (int i = 0; i < counts; i++) {
                    time[i] = cursor.getString(cursor.getColumnIndex("time"));
                    grade[i] = cursor.getDouble(cursor.getColumnIndex("grade"));
                    cursor.moveToPrevious();
                }
            }
            if (time.length != 0) {
                mSimpleLineChart = (SimpleLineChart) view.findViewById(R.id.simpleLineChart);
                String[] yItem = {"100", "80", "60", "40", "20", "0"};
                mSimpleLineChart.setXItem(time);
                mSimpleLineChart.setYItem(yItem);
                HashMap<Integer, Double> pointMap = new HashMap();
                for (int i = 0; i < time.length; i++) {
                    pointMap.put(i, grade[i] / 100);
                }
                mSimpleLineChart.setData(pointMap);
                SharedPreferences sharedpref = getActivity().getSharedPreferences("info", MODE_PRIVATE);
                String suggest = sharedpref.getString("suggestion", "");
                float gra = sharedpref.getFloat("grade", 0);
                suggestion.setText("眠眠萌的建议：\n"+ suggest);
                long x = Math.round(gra);
                gradeText.setText(x + "");
            } else {
                suggestion.setText("眠眠萌的建议：\n体验一下我们的app吧~");
            }
        } catch (Exception e) {
            Log.i("e", e.toString());
        }
    }
}