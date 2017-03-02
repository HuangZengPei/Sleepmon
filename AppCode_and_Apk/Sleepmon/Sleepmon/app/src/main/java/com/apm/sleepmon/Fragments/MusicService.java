package com.apm.sleepmon.Fragments;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.apm.sleepmon.R;

import java.io.IOException;

public class MusicService extends Service {

    private MediaPlayer mediaPlayer;
    private int source;

    private final IBinder binder = new MyBinder();
    public class MyBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    public void init() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, R.raw.music1);
        mediaPlayer.setLooping(false);
    }

    public void changeMusic() {
        mediaPlayer.release();
        try {
            /*mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(source);
            mediaPlayer.setLooping(false);
            mediaPlayer.prepare();*/
            mediaPlayer = MediaPlayer.create(this, source);
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        } catch (Exception e) {
            Log.i("e", e.toString());
        }
    }

    public void playAndPause() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
        else if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            //mediaPlayer.release();
            try {
                mediaPlayer.prepare();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }
}
