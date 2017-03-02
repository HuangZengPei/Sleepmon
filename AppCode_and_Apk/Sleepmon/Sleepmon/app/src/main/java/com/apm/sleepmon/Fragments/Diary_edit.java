package com.apm.sleepmon.Fragments;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.apm.sleepmon.JsonParser;
import com.apm.sleepmon.MainActivity;
import com.apm.sleepmon.R;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;


import java.util.Calendar;
import java.util.TimeZone;


public class Diary_edit extends AppCompatActivity implements View.OnClickListener{

    private static final String TABLE_NAME = "diary_table";
    Button save;
    TextView date, cacel;
    EditText diary_text;
    ImageButton add_diary;
    int year;
    int month;
    int day;
    int minute;
    int hour;
    int weekday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_edit);

        verifyStoragePermissions();

        String APP_ID = "585d429d";
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=" + APP_ID);

        save = (Button)findViewById(R.id.save);
        date = (TextView)findViewById(R.id.date);
        cacel = (TextView)findViewById(R.id.cancel);
        add_diary = (ImageButton)findViewById(R.id.add_diary);
        diary_text = (EditText)findViewById(R.id.diary_text);

        add_diary.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        final Intent intent = new Intent(Diary_edit.this, MainActivity.class);

        if (extras == null) {
            Calendar calendar = Calendar.getInstance();
            TimeZone timeZone = java.util.TimeZone.getTimeZone("GMT+8");
            calendar.setTimeZone(timeZone);

             year = calendar.get(Calendar.YEAR);
             month = calendar.get(Calendar.MONTH) + 1;
             day = calendar.get(Calendar.DAY_OF_MONTH);
             minute = calendar.get(Calendar.MINUTE);
             hour = calendar.get(Calendar.HOUR_OF_DAY);
             weekday = calendar.get(Calendar.DAY_OF_WEEK);

            String weekdayTrans = null;
            if (weekday == 1) {
                weekdayTrans = "周日";
            } else if (weekday == 2) {
                weekdayTrans = "周一";
            } else if (weekday == 3) {
                weekdayTrans = "周二";
            } else if (weekday == 4) {
                weekdayTrans = "周三";
            } else if (weekday == 5) {
                weekdayTrans = "周四";
            } else if (weekday == 6) {
                weekdayTrans = "周五";
            } else if (weekday == 7) {
                weekdayTrans = "周六";
            }

            date.setText(year+"年"+month+"月"+day+"日"+" "+hour+":"+minute +" "+ weekdayTrans);

            final String finalWeekdayTrans = weekdayTrans;

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(diary_text.getText().toString())) {
                        Toast.makeText(Diary_edit.this, "日记内容不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        myDB mydb = new myDB(getBaseContext());
                        SQLiteDatabase db = mydb.getWritableDatabase();
                        ContentValues cv = new ContentValues();
                        cv.put("date", day);
                        cv.put("month", month);
                        cv.put("week", finalWeekdayTrans);
                        cv.put("time", hour+":"+minute);
                        cv.put("diary", diary_text.getText().toString());
                        cv.put("sum_time", date.getText().toString());
                        db.insert(TABLE_NAME, null, cv);
                        db.close();
                        startActivity(intent);
                        Diary_edit.this.finish();
                    }
                }
            });
        } else {
            int  position;
            position = extras.getInt("diary_position");
            myDB dbHelp = new myDB(this);
            final SQLiteDatabase sqLiteDatabase = dbHelp.getWritableDatabase();
            final Cursor cursor = sqLiteDatabase.rawQuery("select * from diary_table", null);
            cursor.moveToPosition(position);
            date.setText(cursor.getString(cursor.getColumnIndex("sum_time")));
            diary_text.setText(cursor.getString(cursor.getColumnIndex("diary")));

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentValues cv = new ContentValues();
                    cv.put("diary", diary_text.getText().toString());
                    String whereClause = "_id = ?";
                    String[] whereArgs = {cursor.getString(cursor.getColumnIndex("_id"))};
                    sqLiteDatabase.update("diary_table", cv, whereClause, whereArgs);
                    setResult(6, new Intent());
                    startActivity(intent);
                    Diary_edit.this.finish();
                }
            });
        }

        cacel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        FlowerCollector.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FlowerCollector.onPause(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_diary) {
            RecognizerDialog recognizerDialog = new RecognizerDialog(Diary_edit.this, null);
            recognizerDialog.setParameter(SpeechConstant.DOMAIN, "iat");
            recognizerDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            recognizerDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
            recognizerDialog.setListener(mRecoListener);
            recognizerDialog.show();
        }
    }

    private RecognizerDialogListener mRecoListener = new RecognizerDialogListener() {

        @Override
        public void onError(SpeechError speechError) {
        }

        @Override
        public void onResult(com.iflytek.cloud.RecognizerResult recognizerResult, boolean b) {
            String text = diary_text.getText().toString();
            text += JsonParser.parseIatResult(recognizerResult.getResultString());
            diary_text.setText(text);
        }

    };

    private void verifyStoragePermissions() {
        String[] permissions = new String[]{
                "android.permission.RECORD_AUDIO",
                "android.permission.ACCESS_NETWORK_STATE",
                "android.permission.READ_PHONE_STATE",
                "android.permission.CHANGE_NETWORK_STATE",
                "android.permission.INTERNET",
                "android.permission.ACCESS_WIFI_STATE"
        };
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(Diary_edit.this, permissions, new PermissionsResultAction() {
            @Override
            public void onGranted() {}

            @Override
            public void onDenied(String permission) {}
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }
}
