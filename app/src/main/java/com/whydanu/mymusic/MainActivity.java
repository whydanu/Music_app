package com.whydanu.mymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    //inisiasi
    TextView playerposetion,playerdurasion;
    SeekBar seekBar;
    ImageView btrew,btplay,btpause,btff;

    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //take id dari xml
        playerposetion = findViewById(R.id.player_position);
        playerdurasion = findViewById(R.id.player_durasion);
        seekBar = findViewById(R.id.seek_bar);
        btrew = findViewById(R.id.bt_rew);
        btplay = findViewById(R.id.bt_play);
        btpause = findViewById(R.id.bt_pause);
        btff = findViewById(R.id.bt_ff);

        //inisiasi musik di media player
        //mediaPlayer  = MediaPlayer.create(this,R.raw.flying_high);
        //mediaPlayer  = MediaPlayer.create(this,R.raw.rainbow);
        mediaPlayer  = MediaPlayer.create(this,R.raw.love_nwantiti);
        //inisiasi runnable
        runnable = new Runnable() {
            @Override
            public void run() {
                //mensetel progres di sek bar
                seekBar.setProgress(mediaPlayer.getCurrentPosition());

                //handler delay 0.5 seken
                handler.postDelayed(this,200);
            }
        };

        //get durasion di media player
        int duration = mediaPlayer.getDuration();

        //konvert milisekon ke menit dan sekon
        String sDuration = convertFormat(duration);
        //set durasi di text view
        playerdurasion.setText(sDuration);

        btplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hide button play
                btplay.setVisibility(View.GONE);
                //show pause butten
                btpause.setVisibility(View.VISIBLE);
                //start media player
                mediaPlayer.start();
                //seek max media player
                seekBar.setMax(mediaPlayer.getDuration());
                //star handler
                handler.postDelayed(runnable,0);
            }
        });

        btpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hide pause butten
                btpause.setVisibility(View.GONE);
                //show play butten
                btplay.setVisibility(View.VISIBLE);
                //media pause
                mediaPlayer.pause();
                //stop handler
                handler.removeCallbacks(runnable);
            }
        });
        btff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get curret posisi media player
                int curretposition = mediaPlayer.getCurrentPosition();
                //get Durasion media player
                int duration = mediaPlayer.getDuration();
                //check condition
                if ( mediaPlayer.isPlaying() && duration != curretposition);
                //ketika media berputar dan tidak dalam posisi nilai sama
                //melangkah 5 detik lebih cepat
                curretposition = curretposition + 3000;
                //set curet posisi ke text view
                playerposetion.setText(convertFormat(curretposition));
                //set on seek bar
                mediaPlayer.seekTo(curretposition);

            }
        });

        btrew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get current position of media player
                int currentPosition = mediaPlayer.getCurrentPosition();
                //check kondisi
                if(mediaPlayer.isPlaying()&& currentPosition > 3000){
                    //
                    currentPosition = currentPosition - 3000;
                    //get current posisiton
                    playerposetion.setText(convertFormat(currentPosition));
                    //set progres
                    mediaPlayer.seekTo(currentPosition);

                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //check kondisi
                if (fromUser){

                    mediaPlayer.seekTo(progress);

                }
                //set current position on text view
                playerposetion.setText(convertFormat(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //hide pause  button
                btpause.setVisibility(View.GONE);
                //show play button
                btplay.setVisibility(View.VISIBLE);
                //set media player to initial position
                mediaPlayer.seekTo(0);
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration)-
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }
}