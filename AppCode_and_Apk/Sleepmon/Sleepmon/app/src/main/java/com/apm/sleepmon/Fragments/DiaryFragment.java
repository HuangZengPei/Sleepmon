package com.apm.sleepmon.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.apm.sleepmon.R;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class DiaryFragment extends Fragment implements View.OnClickListener{

    private TextView password;
    private EditText password1, password2;
    private Button login;
    private ListView listView;
    private ImageButton add_diary;
    private Intent intent1;
    private View view;
    public static boolean tag = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.diary_fragment, null);
        login = (Button) view.findViewById(R.id.login);
        login.setOnClickListener(this);
        if (!tag) {
            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.diary);
            init(relativeLayout);
            relativeLayout.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
        }
        if (tag) {
            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.diary);
            init(relativeLayout);
            relativeLayout.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.login:
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View layout = inflater.inflate(R.layout.password, (ViewGroup) v.findViewById(R.id.dialog));
                password1 = (EditText)layout.findViewById(R.id.password1);
                password2 = (EditText)layout.findViewById(R.id.password2);
                password = (TextView) layout.findViewById(R.id.pass2);

                AlertDialog.Builder message = new AlertDialog.Builder(getActivity());
                message.setTitle("输入密码");
                message.setView(layout);
                SharedPreferences sharedpref = getActivity().getSharedPreferences("info", MODE_PRIVATE);

                //获得保存在SharedPredPreferences中的用户名和密码
                String password_1 = sharedpref.getString("password1", "");
                String password_2 = sharedpref.getString("password2", "");
                //在用户名和密码的输入框中显示用户名和密码
                password1.setText(password_1);
                password2.setText(password_2);


                //在用户名和密码的输入框中显示用户名和密码

                if(!"".equals(password_2)){
                    password.setVisibility(View.GONE);
                    password2.setVisibility(View.GONE);
                    password1.setText("");
                    tag = false;
                }

                message.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String password_1 = password1.getText().toString();
                        String password_2 = password2.getText().toString();

                        SharedPreferences sharedpref = getActivity().getSharedPreferences("info", MODE_PRIVATE);

                        SharedPreferences.Editor shared = sharedpref.edit();

                        shared.putString("password1", password_1);
                        shared.putString("password2", password_2);

                        shared.commit();

                        if ("".equals(password_1) || "".equals(password_2)) {
                            Toast.makeText(getActivity(), "Password cannot be empty.", Toast.LENGTH_LONG).show();
                        }
                        else if (!password_1.equals(password_2)) {
                            if (password.getVisibility() == View.VISIBLE) {
                                shared.remove("password1");
                                shared.remove("password2");
                                shared.commit();
                            }
                            Toast.makeText(getActivity(), "Password Mismatch", Toast.LENGTH_LONG).show();
                        }
                        else {
                            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.diary);
                            init(relativeLayout);
                            relativeLayout.setVisibility(View.VISIBLE);
                            login.setVisibility(View.GONE);
                        }
                    }

                });
                message.setNegativeButton("取消", null);
                message.create().show();
        }
    }

    public void init(View view) {
        listView = (ListView)view.findViewById(R.id.diary_list);
        add_diary = (ImageButton)view.findViewById(R.id.add_diary);
        intent1 = new Intent(getActivity(), Diary_edit.class);

        myDB dbHelp = new myDB(getActivity());
        final SQLiteDatabase sqLiteDatabase = dbHelp.getWritableDatabase();
        final Cursor cursor = sqLiteDatabase.rawQuery("select * from diary_table", null);
        int counts = cursor.getCount();
        final String[] date = new String[counts];
        final String[] month = new String[counts];
        final String[] weekday = new String[counts];
        final String[] time = new String[counts];
        final String[] diary = new String[counts];
        final String[] diary_summary = new String[counts];
        final List<Map<String, Object>> data = new ArrayList<>();

        if (cursor.moveToFirst() == true) {
            for (int i = 0; i < counts; i++) {
                month[i] = cursor.getString(cursor.getColumnIndex("month"));
                date[i] = cursor.getString(cursor.getColumnIndex("date"));
                weekday[i] = cursor.getString(cursor.getColumnIndex("week"));
                time[i] = cursor.getString(cursor.getColumnIndex("time"));
                diary[i] = cursor.getString(cursor.getColumnIndex("diary"));
                diary_summary[i] = getSummary(diary[i]);
                cursor.moveToNext();
            }
        }
        for (int i = 0; i < counts; i++) {
            Map<String, Object>temp = new LinkedHashMap<>();
            temp.put("month", month[i]);
            temp.put("date", date[i]);
            temp.put("week", weekday[i]);
            temp.put("time", time[i]);
            temp.put("diary", diary_summary[i]);
            data.add(temp);
        }
        final SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), data, R.layout.diary_list,
                new String[] {"month","date", "week", "time", "diary"}, new int[] {R.id.item_month,
                R.id.item_date, R.id.item_week, R.id.item_time, R.id.item_summary});
        if (cursor.moveToFirst() == true)
            listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent1.putExtra("diary_position", position);
                getActivity().startActivityForResult(intent1, 9);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("是否删除？");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String whereClaus = "diary = ?";
                        String[] whereArgs = {diary[position]};
                        sqLiteDatabase.delete("diary_table", whereClaus, whereArgs);
                        data.remove(position);
                        simpleAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                builder.create().show();
                return true;
            }
        });

        add_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivityForResult(intent1, 6);
            }
        });
    }

    public String getSummary(String string) {
        String r_string;
        if (string.length() >= 18) {
            r_string = string.substring(0, 11) + "…";
        } else {
            r_string = string;
        }
        return r_string;
    }
}
