package com.apm.sleepmon.Fragments;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.apm.sleepmon.Music;
import com.apm.sleepmon.R;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MusicFragment extends Fragment implements View.OnClickListener{

    private List<Music> music = new LinkedList<>();
    private TextView source, playing, playTime, resTime;
    private ImageView imageView;
    private ImageButton imageButton, next_music, last_music;
    private SeekBar seekBar;
    private ListView musicList;
    private MusicService musicService;
    private boolean isPlaying;
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss", Locale.getDefault());
    private ObjectAnimator animator;
    private int musicPosition = 0;

    public static String musicWidget = "com.apm.sleepmon.musicOption";
    public static String changeWidget = "com.apm.sleepmon.changeWidget";

    Handler handler = new Handler();
    Runnable update = new Runnable() {
        @Override
        public void run() {
            int current = musicService.getMediaPlayer().getCurrentPosition();
            int res = musicService.getMediaPlayer().getDuration() - current;
            seekBar.setProgress(current);
            playTime.setText(time.format(current));
            resTime.setText(time.format(res));
            if (res <= 0) {
                /*musicService.stop();
                playing.setText(R.string.stop);
//                play.setText(R.string.play);
                imageButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bofangzhuangtaitingzhi));
                isPlaying = false;
                animator.end();
                seekBar.setProgress(0);
                handler.removeCallbacks(update);*/
                setNext_music();
            }
            handler.postDelayed(update, 100);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.music_fragment, null);

        findView(view);
        prepareMusic();
        setListView();
        setAnimator();
        bindButton();
        connection();
        setSeekBarOption();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicFragment.musicWidget);
        getActivity().registerReceiver(receiver, intentFilter);
        return view;
    }


    @Override
    public void onDestroy() {
        changeWidget("点击图片打开音乐播放器", false);
        super.onDestroy();
    }

    private void findView(View view) {
        source = (TextView) view.findViewById(R.id.source);
        playing = (TextView) view.findViewById(R.id.playing);
        playTime = (TextView) view.findViewById(R.id.playTime);
        resTime = (TextView) view.findViewById(R.id.resTime);
        imageView = (ImageView) view.findViewById(R.id.image1);
        imageButton = (ImageButton) view.findViewById(R.id.play_button);
/*        stop = (Button) view.findViewById(R.id.stop);
        quit = (Button) view.findViewById(R.id.quit);*/
        next_music = (ImageButton) view.findViewById(R.id.next_music);
        last_music = (ImageButton) view.findViewById(R.id.last_music);
        seekBar = (SeekBar) view.findViewById(R.id.seekbar);
        musicList = (ListView) view.findViewById(R.id.musicList);
    }

    private void prepareMusic() {
        music.add(new Music("music1", R.raw.music1));
        music.add(new Music("music2", R.raw.music2));
        music.add(new Music("music3", R.raw.music3));
        music.add(new Music("music4", R.raw.music4));
        music.add(new Music("music5", R.raw.music5));
        music.add(new Music("music6", R.raw.music6));
        music.add(new Music("music7", R.raw.music7));
        music.add(new Music("music8", R.raw.music8));
        music.add(new Music("music9", R.raw.music9));
        music.add(new Music("music10", R.raw.music10));
        music.add(new Music("music11", R.raw.music11));
        music.add(new Music("music12", R.raw.music12));
        music.add(new Music("music13", R.raw.music13));
        music.add(new Music("music14", R.raw.music14));
        music.add(new Music("music15", R.raw.music15));
        music.add(new Music("music16", R.raw.music16));
        music.add(new Music("music17", R.raw.music17));
    }

    private void setListView() {
        List<Map<String, String>> ml = new ArrayList<>();
        for (Music i : music) {
            Map<String, String> temp = new HashMap<>();
            temp.put("musicName", i.getMusicName());
            ml.add(temp);
        }
        SimpleAdapter sa = new SimpleAdapter(getActivity(), ml, R.layout.music_list,
                new String[]{"musicName"}, new int[]{R.id.text});
        musicList.setAdapter(sa);

        musicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                musicPosition = position;
                changeMusic(position);
            }
        });
    }

    private void setAnimator() {
        animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360.0f);
        animator.setRepeatCount(0);
        animator.setInterpolator(new LinearInterpolator());
    }

    private void bindButton() {
        imageButton.setOnClickListener(this);
        /*stop.setOnClickListener(this);
        quit.setOnClickListener(this);*/
        next_music.setOnClickListener(this);
        last_music.setOnClickListener(this);
    }

    private void connection() {
        Intent intent = new Intent(getActivity(), MusicService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService = ((MusicService.MyBinder) (service)).getService();
            musicService.init();
            seekBar.setMax(musicService.getMediaPlayer().getDuration());
            handler.post(update);
            animator.setDuration(musicService.getMediaPlayer().getDuration());
            source.setText(music.get(0).getMusicSource());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }

    };

    private void setSeekBarOption() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, int progress, boolean fromUser) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicService.getMediaPlayer().seekTo(seekBar.getProgress());
                if (animator.isStarted()) {
                    animator.end();
                }
                animator.setDuration(musicService.getMediaPlayer().getDuration() -
                        musicService.getMediaPlayer().getCurrentPosition());
                if (musicService.getMediaPlayer().isPlaying()) {
                    animator.start();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_button:
                playAndPause();
                break;
/*            case R.id.stop:
                stop();
                break;
            case R.id.quit:
                changeWidget("点击图片打开音乐播放器", false);
                Intent intent = new Intent(getActivity(), MusicService.class);
                musicService.onUnbind(intent);
                getActivity().stopService(intent);
                handler.removeCallbacks(update);
                getActivity().finish();
                break;*/
            case R.id.next_music:
                setNext_music();
                break;
            case R.id.last_music:
                setLast_music();
                break;
        }
    }

    private void setNext_music() {
        if (musicPosition == musicList.getCount() - 1) {
            musicPosition = 0;
        }
        else {
            musicPosition++;
        }
        changeMusic(musicPosition);
    }

    private void setLast_music() {
        if (musicPosition != 0) {
            musicPosition--;
        }
        changeMusic(musicPosition);
    }

    private void playAndPause() {
        musicService.playAndPause();
        if (isPlaying) {
            playing.setText(R.string.pause);
            //play.setText(R.string.play);
            isPlaying = false;
            animator.pause();
            imageButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bofangzhuangtaitingzhi));
        } else {
            if (animator.isStarted()) {
                animator.resume();
            } else {
                animator.setDuration(musicService.getMediaPlayer().getDuration());
                animator.start();
            }
            playing.setText(R.string.playing);
            //play.setText(R.string.pause);
            isPlaying = true;
            handler.post(update);
            imageButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bofangzhuangtai));
        }
        changeWidget(music.get(musicPosition).getMusicName(), isPlaying);
    }

/*    private void stop() {
        handler.removeCallbacks(update);
        seekBar.setProgress(0);
        resTime.setText(time.format(musicService.getMediaPlayer().getDuration()));
        playTime.setText(time.format(0));
        playing.setText(R.string.stop);
        //play.setText(R.string.play);
        imageButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bofangzhuangtaitingzhi));
        isPlaying = false;
        animator.end();
        musicService.stop();
        changeWidget(music.get(musicPosition).getMusicName(), isPlaying);
    }*/

    private void changeMusic(int position) {
        if (musicService.getMediaPlayer().isPlaying()) {
            handler.removeCallbacks(update);
            isPlaying = false;
        }
        if (position == 0) {
            musicService.init();
            musicService.getMediaPlayer().start();
        }
        else {
            if (musicService.getMediaPlayer() != null) {
                musicService.setSource(music.get(position).getMusicSource());
                musicService.changeMusic();
            }
        }
        seekBar.setMax(musicService.getMediaPlayer().getDuration());
        if (animator.isStarted()) {
            animator.end();
        }
        animator.setDuration(musicService.getMediaPlayer().getDuration());
        animator.start();
        handler.post(update);
        isPlaying = true;
        //play.setText(R.string.pause);
        imageButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bofangzhuangtai));
        playing.setText(R.string.playing);
        source.setText(music.get(position).getMusicSource());
        changeWidget(music.get(position).getMusicName(), isPlaying);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MusicFragment.musicWidget)) {
                int action = intent.getIntExtra("musicOption", -1);
                switch (action) {
                    case 0:
                        playAndPause();
                        break;
/*                    case 1:
                        stop();
                        break;*/
                    case 2:
                        setNext_music();
                        break;
                    case 3:
                        setLast_music();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    private void changeWidget(String source, boolean isPlaying) {
        Intent intent = new Intent(MusicFragment.changeWidget);
        intent.putExtra("source", source);
        intent.putExtra("isPlaying", isPlaying);
        getActivity().sendBroadcast(intent);
    }
}

