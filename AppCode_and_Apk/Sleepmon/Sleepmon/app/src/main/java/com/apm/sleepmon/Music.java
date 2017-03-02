package com.apm.sleepmon;

public class Music {

    public Music(String musicName, int musicSource) {
        this.musicName = musicName;
        this.musicSource = musicSource;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public int getMusicSource() {
        return musicSource;
    }

    public void setMusicSource(int musicSource) {
        this.musicSource = musicSource;
    }

    private int musicSource;
    private String musicName;
}
