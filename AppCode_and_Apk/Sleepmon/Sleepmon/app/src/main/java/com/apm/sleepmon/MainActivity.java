package com.apm.sleepmon;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.apm.sleepmon.Fragments.AlarmFragment;
import com.apm.sleepmon.Fragments.ChartFragment;
import com.apm.sleepmon.Fragments.DiaryFragment;
import com.apm.sleepmon.Fragments.MusicFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{

    List<Fragment> list = new ArrayList<>();
    BottomNavigationBar bottomNavigationBar;
    ChartFragment chartFragment;
    AlarmFragment alarmFragment;
    MusicFragment musicFragment;
    DiaryFragment diaryFragment;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getFragmentManager();

        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);

        /**
         *添加tab标签页
         */
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.sleep_pressed, "睡眠")
                        .setActiveColorResource(R.color.sleep).setInactiveIconResource(R.drawable.sleep_normal))
                .addItem(new BottomNavigationItem(R.drawable.analysis_pressed, "睡眠报告")
                        .setActiveColorResource(R.color.analysis).setInactiveIconResource(R.drawable.analysis_normal))
                .addItem(new BottomNavigationItem(R.drawable.diary_pressed, "眠梦日记")
                        .setActiveColorResource(R.color.diary).setInactiveIconResource(R.drawable.diary_normal))
                .addItem(new BottomNavigationItem(R.drawable.music_pressed, "催眠曲")
                        .setActiveColorResource(R.color.music).setInactiveIconResource(R.drawable.music_normal))
                .initialise();

        onTabSelected(0);
        bottomNavigationBar.setTabSelectedListener(this);
    }

    public void hideFragment(FragmentTransaction transaction) {
        for (Fragment fragment : list) {
            transaction.hide(fragment);
        }
    }

    @Override
    public void onTabSelected(int position) {

        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        switch (position) {
            case 0:
                if (alarmFragment == null) {
                    alarmFragment = new AlarmFragment();
                    transaction.add(R.id.layFrame, alarmFragment);
                    list.add(alarmFragment);
                } else {
                    transaction.show(alarmFragment);
                }
                DiaryFragment.tag = true;
                break;
            case 1:
                if (chartFragment != null) {
                    chartFragment = null;
                }
                chartFragment = new ChartFragment();
                transaction.add(R.id.layFrame, chartFragment);
                list.add(chartFragment);
                DiaryFragment.tag = true;
                break;
            case 2:
                if (diaryFragment != null) {
                    diaryFragment = null;
                }
                diaryFragment = new DiaryFragment();
                transaction.add(R.id.layFrame, diaryFragment);
                list.add(diaryFragment);
                break;
            case 3:
                if (musicFragment == null) {
                    musicFragment = new MusicFragment();
                    transaction.add(R.id.layFrame, musicFragment);
                    list.add(musicFragment);
                } else {
                    transaction.show(musicFragment);
                }
                DiaryFragment.tag = true;
                break;
        }
        transaction.commit();
    }

    @Override
    public void onTabReselected(int position) {}

    @Override
    public void onTabUnselected(int position) {}

    @Override
    public void onActivityResult(int resquestCode, int resultCode, Intent data1) {
        if (resquestCode == 6 || resquestCode == 9) {
            diaryFragment = null;
            DiaryFragment.tag = false;
            onTabSelected(2);
        }
    }
}
