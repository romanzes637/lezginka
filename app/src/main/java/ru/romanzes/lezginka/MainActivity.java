package ru.romanzes.lezginka;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import java.util.Random;

/**
 * Created by Roman on 06.07.2015.
 */

public class MainActivity extends Activity implements SensorEventListener {

    private int mSensitivityX = 15;
    private int mSensitivityY = 15;
    private int mSensitivityZ = 18;

    private int[] mSongs;
    private Random mRandom;

    private MediaPlayer mMediaPlayer;
    private MediaPlayer mMediaPlayerShot;
    private MediaPlayer mMediaPlayerMultiShot;
    private MediaPlayer mMediaPlayerWhip;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private Vibrator mVibe;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Log.d(this.getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final CheckBox checkBoxMusic = (CheckBox) findViewById(R.id.checkBoxMusic);

        checkBoxMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    checkBoxMusic.setChecked(false);
                } else {
                    mMediaPlayer.start();
                    checkBoxMusic.setChecked(true);
                }
            }
        });

        mSongs = new int[]{
                R.raw.adygovainahskaya,
                R.raw.dagestanskaya,
                R.raw.gorsky,
                R.raw.krasivaya,
                R.raw.krymskotatarskaya,
                R.raw.normas,
                R.raw.supermoschnaya};

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mVibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        mRandom = new Random();
        int position = mRandom.nextInt(mSongs.length);

        mMediaPlayer = MediaPlayer.create(this, mSongs[position]);
        mMediaPlayerShot = MediaPlayer.create(this, R.raw.shot);
        mMediaPlayerMultiShot = MediaPlayer.create(this, R.raw.multishot);
        mMediaPlayerWhip = MediaPlayer.create(this, R.raw.whip);

        mMediaPlayer.start();
        checkBoxMusic.setChecked(true);

        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStart() {
        Log.d(this.getClass().getName(), "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(this.getClass().getName(), "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(this.getClass().getName(), "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(this.getClass().getName(), "onStop");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d(this.getClass().getName(), "onRestart");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.d(this.getClass().getName(), "onDestroy");
        super.onDestroy();

        mSensorManager.unregisterListener(this, mSensor);

        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.release();

        if (mMediaPlayerShot.isPlaying()) {
            mMediaPlayerShot.stop();
        }
        mMediaPlayerShot.release();

        if (mMediaPlayerMultiShot.isPlaying()) {
            mMediaPlayerMultiShot.stop();
        }
        mMediaPlayerMultiShot.release();

        if (mMediaPlayerWhip.isPlaying()) {
            mMediaPlayerWhip.stop();
        }
        mMediaPlayerWhip.release();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(this.getClass().getName(),
                "X = " + String.valueOf(event.values[0])
                        + " Y = " + String.valueOf(event.values[1])
                        + " Z = " + String.valueOf(event.values[2]));

        if ((event.values[2] > mSensitivityZ || event.values[2] < -mSensitivityZ)
                && mMediaPlayer.isPlaying()) {
            mSensorManager.unregisterListener(this, mSensor);

            mMediaPlayerWhip.start();
            while (mMediaPlayerWhip.getCurrentPosition() < 1100) {
            }
            mMediaPlayerWhip.pause();
            mMediaPlayerWhip.seekTo(0);

            mMediaPlayer.stop();
            mMediaPlayer.reset();
            int position = mRandom.nextInt(mSongs.length);
            mMediaPlayer = MediaPlayer.create(this, mSongs[position]);
            mMediaPlayer.start();

            mVibe.vibrate(100);
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

        } else if (event.values[1] > mSensitivityY || event.values[1] < -mSensitivityY) {
            mSensorManager.unregisterListener(this, mSensor);

            mMediaPlayerMultiShot.start();
            while (mMediaPlayerMultiShot.getCurrentPosition() < 1000) {
            }
            mMediaPlayerMultiShot.pause();
            mMediaPlayerMultiShot.seekTo(0);

            mVibe.vibrate(100);
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

        } else if (event.values[0] > mSensitivityX || event.values[0] < -mSensitivityX) {
            mSensorManager.unregisterListener(this, mSensor);

            mMediaPlayerShot.start();
            while (mMediaPlayerShot.getCurrentPosition() < 800) {
            }
            mMediaPlayerShot.pause();
            mMediaPlayerShot.seekTo(0);

            mVibe.vibrate(100);
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
