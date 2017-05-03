package com.mustonen.niko.hyperflightsimulatorultra3d;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.*;
import android.widget.LinearLayout;
import android.widget.LinearLayout.*;

import com.mustonen.niko.hyperflightsimulatorultra3d.util.Debug;

import static android.widget.LinearLayout.LayoutParams.*;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    public static float dirX = 0;
    public static float dirY = 0;

    GLSurfaceView mGLView;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Receiver receiver;
    private IntentFilter filter;

    LinearLayout linearLayout;
    TextView pointsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLView = new MyGLView(this);
        setContentView(mGLView);
        setSoundPool();

        linearLayout = new LinearLayout(this);
        linearLayout.setGravity(Gravity.RIGHT);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        pointsView = new TextView(this);
        pointsView.setText("0 POINTS");
        pointsView.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
        ));
        pointsView.setTextColor(Color.parseColor("#bb0000"));
        pointsView.setTextSize(30);
        Typeface face = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD);
        pointsView.setTypeface(face);
        linearLayout.setPadding(100, 100, 150, 100);
        linearLayout.addView(pointsView);
        addContentView(linearLayout, new LayoutParams( LayoutParams.MATCH_PARENT , LayoutParams.MATCH_PARENT) );

        filter = new IntentFilter("i");
        this.receiver = new Receiver();
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        mGLView.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(receiver, filter);
        mGLView.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        dirX = -event.values[1];
        dirY = -event.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    class MyGLView extends GLSurfaceView {

        public MyGLView(Context c) {
            super(c);

            setEGLContextClientVersion(2);

            Renderer renderer = new MyGLRenderer(getContext());
            setRenderer(renderer);
        }
    }

    private int points;

    class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            points++;
            pointsView.setText(points + " POINTS");
            if(areSoundsLoaded) {
                soundPool.play(soundId1, 0.5f, 0.5f, 1, 0, 1f);
            }
            Debug.debug(null, "POINTS: " +  points);
        }
    }

    private SoundPool soundPool;
    boolean areSoundsLoaded = false;
    private int soundId1;

    private void setSoundPool() {
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        AudioAttributes atr =
                new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();

        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setAudioAttributes(atr);
        builder.setMaxStreams(3);
        soundPool = builder.build();

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                GameActivity.this.areSoundsLoaded = true;
            }
        });

        soundId1 = soundPool.load(this, R.raw.point, 1);
    }
}
