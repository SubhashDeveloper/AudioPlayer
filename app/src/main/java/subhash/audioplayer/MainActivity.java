package subhash.audioplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;

import static subhash.audioplayer.R.id.seekBar;

public class MainActivity extends AppCompatActivity {
    Button btp, bts, btpp;
    Button bsongl;
    Button mute;
    Button selects;
    MediaPlayer mp;
    SeekBar sk;
    Handler hand = new Handler();
boolean restart =true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btp = (Button) findViewById(R.id.button);
        btpp = (Button) findViewById(R.id.button2);
        bts = (Button) findViewById(R.id.button3);
        bsongl=(Button)findViewById(R.id.button4);
        selects=(Button)findViewById(R.id.button5);
        mute=(Button)findViewById(R.id.button6);
         sk=(SeekBar)findViewById(seekBar);
        mp=MediaPlayer.create(MainActivity.this,R.raw.menu);
              sk.setMax(mp.getDuration());
          SeekUpdation();

        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mp.setVolume(0,0);
                if(restart) {
                    AudioManager mAudioManager = (AudioManager) MainActivity.this.getSystemService(Context.AUDIO_SERVICE);

                    int current_volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    //If you want to player is mute ,then set_volume variable is zero.Otherwise you may supply some value.
                    int set_volume = 0;
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, set_volume, 0);
                    restart=false;
                    mute.setBackgroundResource(R.drawable.off);

                }
                else
                {
                    AudioManager mAudioManager = (AudioManager) MainActivity.this.getSystemService(Context.AUDIO_SERVICE);

                    int current_volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    //If you want to player is mute ,then set_volume variable is zero.Otherwise you may supply some value.

                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,15,15);
                    mute.setBackgroundResource(R.drawable.on);
                    restart=true;

                }
            }
        });


        btp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                      SeekUpdation();

                if (mp == null)
                    mp = MediaPlayer.create(MainActivity.this, R.raw.menu);
                if(mp.isPlaying()) {
                    mp.seekTo(0);
                }
                    mp.start();
                              sk.setMax(mp.getDuration());
                            sk.setProgress(mp.getCurrentPosition());



            }
        });
        bts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp != null) {
                    mp.stop();
                          sk.setProgress(mp.getDuration());
                    mp = null;
                }
                Toast.makeText(MainActivity.this, "Media Stopped", Toast.LENGTH_SHORT).show();

            }
        });
        btpp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (mp != null) {
                    mp.pause();
                        sk.setProgress(mp.getCurrentPosition());
                }

            }
        });
        bsongl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,Main2Activity.class);
                startActivity(i);
            }
        });
        selects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

if(mp!=null) {
    mp.seekTo(0);

    mp.stop();

    mp = null;

}

                    Intent i=new Intent(Intent.ACTION_GET_CONTENT);
               i.setType("audio/*");
                startActivityForResult(i,100);
            }
        });

        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(mp!=null)
                {
                    mp.pause();
                }

                //Toast.makeText(MainActivity.this, "onStartTracking", Toast.LENGTH_SHORT).show();



            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(mp!=null)
                {
                    mp.seekTo(seekBar.getProgress());
                    mp.start();
                }
               // Toast.makeText(MainActivity.this, "onStopTracking", Toast.LENGTH_SHORT).show();


            }
        });
    }
    Runnable run=new Runnable() {
        @Override
        public void run() {
            SeekUpdation();
        }
    };
    public void SeekUpdation()
    {
        if(mp!=null)
        {
            int mpos=mp.getCurrentPosition();
            int mdur=mp.getDuration();
            sk.setProgress(mp.getCurrentPosition());
            hand.postDelayed(run,1000);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100&&resultCode== Activity.RESULT_OK) {

            Uri uri = data.getData();
            mp=new MediaPlayer();
            try {


                mp.setDataSource(getApplicationContext(), uri);
              //  SeekUpdation();
                mp.prepare();
                mp.start();
                SeekUpdation();

                sk.setMax(mp.getDuration());
                sk.setProgress(mp.getCurrentPosition());

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        }

    }


