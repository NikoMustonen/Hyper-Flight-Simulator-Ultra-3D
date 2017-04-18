package com.mustonen.niko.hyperflightsimulatorultra3d;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLView = new MyGLView(this);
        setContentView(mGLView);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onPause() {
        super.onPause();
        mGLView.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
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
}
