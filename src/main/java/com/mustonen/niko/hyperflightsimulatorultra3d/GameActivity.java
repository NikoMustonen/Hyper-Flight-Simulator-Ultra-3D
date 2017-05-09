package com.mustonen.niko.hyperflightsimulatorultra3d;

import android.os.*;
import android.view.*;
import android.media.*;
import android.opengl.*;
import android.widget.*;
import android.hardware.*;
import android.view.View.*;
import android.content.Context;
import android.hardware.Sensor;
import android.support.v7.app.*;
import android.view.animation.*;
import android.widget.RelativeLayout.*;

import com.mustonen.niko.hyperflightsimulatorultra3d.util.*;
import com.mustonen.niko.hyperflightsimulatorultra3d.object.*;

import java.util.*;

import javax.microedition.khronos.egl.*;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

/**
 * Starting point class for handling games lifecycle methods.
 *
 * @author Niko Mustonen mustonen.niko@gmail.com
 * @version %I%, %G%
 * @since 1.8
 */
public class GameActivity extends AppCompatActivity implements SensorEventListener {

    public boolean isGameRunning, isGameOver;
    private CloudSaveUtil cloudSaveUtil;

    public static float dirX = 0;
    public static float dirY = 0;

    GLSurfaceView mGLView;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    Timer timer;

    /**
     * Seconds animation.
     */
    Animation a;

    //Layouts
    RelativeLayout relativeLayout;
    LinearLayout saving;
    TextView pointsView, timeView, addSecondsView, tapToStart, gameOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(FontUtil.getLoadingScreen(this));
        isGameRunning = false;

        new LoadFiles().execute();
        cloudSaveUtil = new CloudSaveUtil();

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        a = AnimationUtils.loadAnimation(this, R.anim.animation);
        a.reset();
        a.setAnimationListener(new Animation.AnimationListener() {

            /**
             * Shows given seconds indicator.
             *
             * @param animation
             */
            @Override
            public void onAnimationStart(Animation animation) {
                if (addSecondsView != null) addSecondsView.setVisibility(View.VISIBLE);
            }

            /**
             * Hides given seconds indicator.
             *
             * @param animation
             */
            @Override
            public void onAnimationEnd(Animation animation) {
                if (addSecondsView != null) addSecondsView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        saving = FontUtil.getSavingScreen(this);
        saving.getBackground().setAlpha(200);
        addContentView(saving, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        saving.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGLView != null) mGLView.onPause();
        soundPool.release();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        isGameRunning = false;
        if (mGLView != null) mGLView.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        dirX = -event.values[1];
        dirY = -event.values[2] + 3;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
     * Reference to renderer so activity can pause game movement.
     */
    MyGLRenderer renderer;

    /**
     * Opengl view for the game.
     * <p>
     * Starts games graphics rendering engine.
     *
     * @author Niko Mustonen mustonen.niko@gmail.com
     * @version %I%, %G%
     * @since 1.8
     */
    class MyGLView extends GLSurfaceView {

        /**
         * Creates OpenGl view that handles plane games graphics engine.
         *
         * @param c                      Activity context.
         * @param vsc                    Color programs vertex shader code.
         * @param fsc                    Color program fragment shader code.
         * @param tvsc                   Texture programs vertex shader code.
         * @param tfsc                   Texture program fragment shader code.
         * @param wfvsc                  Wire frame programs vertex shader code.
         * @param wffsc                  Wire frame program fragment shader code.
         * @param plane                  Plane object.
         * @param terrain                Terrain object.
         * @param rings                  Ring objects.
         * @param wireFrameObjectPlane   Wire frame object for plane.
         * @param wireFrameObjectTerrain Wire frame object for terrain.
         */
        public MyGLView(Context c, String vsc, String fsc, String tvsc, String tfsc,
                        String wfvsc, String wffsc, Plane plane, Terrain terrain, Rings rings,
                        WireFrameObject wireFrameObjectPlane,
                        WireFrameObject wireFrameObjectTerrain) {
            super(c);

            setEGLContextClientVersion(2);
            setEGLConfigChooser(new Configuration());
            renderer = new MyGLRenderer(
                    GameActivity.this, vsc, fsc, tvsc, tfsc, wfvsc, wffsc,
                    plane, terrain, rings, wireFrameObjectPlane, wireFrameObjectTerrain);
            setRenderer(renderer);
        }
    }

    /**
     * Holds players current sessions points.
     */
    private int points;

    /**
     * Adds points and time for player.
     */
    public void addPoint() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int moreTime = 3;
                if (points > 40) {
                    moreTime = 1;
                } else if (points > 20) {
                    moreTime = 2;
                }
                addSecondsView.setText("+" + moreTime);
                addSecondsView.startAnimation(a);
                points++;
                addSeconds(moreTime);
                pointsView.setText(points + " points");
                if (areSoundsLoaded) {
                    soundPool.play(soundId1, 0.5f, 0.5f, 1, 0, 1f);
                }
            }
        });
    }

    /**
     * Sound pool for sound files.
     */
    private SoundPool soundPool;

    /**
     * Flag to define whether sound files are loaded.
     */
    boolean areSoundsLoaded = false;

    /**
     * Ring collecting sound.
     */
    private int soundId1;
    private int soundEngine;

    /**
     * Loads sound effects files for the game.
     */
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
        soundEngine = soundPool.load(this, R.raw.ship_normal, 1);
    }

    /**
     * Time left in minutes.
     */
    private int timeMinutes = 0;

    /**
     * Time left in seconds.
     */
    private int timeSeconds = 30;

    /**
     * Handles timer.
     * <p>
     * If time runs out the timer will end the game.
     *
     * @author Niko Mustonen mustonen.niko@gmail.com
     * @version %I%, %G%
     * @since 1.8
     */
    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    removeSeconds(1);
                    if (timeMinutes == 0 && timeSeconds == 0) {
                        endGame();
                    }
                }
            });
        }
    }

    /**
     * Handles game ending when time runs out.
     */
    public void endGame() {
        if (isGameRunning) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (points > 0) cloudSaveUtil.show(getFragmentManager(), "BUU");
                    gameOver.setVisibility(View.VISIBLE);
                    isGameOver = true;
                    soundPool.stop(streamId);
                    if (timer != null) {
                        renderer.setTurbo(false);
                        timer.cancel();
                        timer = null;
                        isGameRunning = false;
                        tapToStart.setVisibility(View.VISIBLE);
                    }

                }
            });
        }
    }

    /**
     * Stores username so save task can get hold of it.
     */
    String name;

    /**
     * Starts point saving.
     * <p>
     * Gets called from the save prompt.
     *
     * @param name Username soon to be saved.
     */
    public void save(String name) {
        if (name == null || name.trim().equals("")) {
            this.name = "Unknown";
        } else {
            this.name = name;
        }
        new Save().execute();
    }

    /**
     * Handles game saving in asynchronous task.
     * <p>
     *
     * @author Niko Mustonen mustonen.niko@gmail.com
     * @version %I%, %G%
     * @since 1.8
     */
    class Save extends AsyncTask {

        /**
         * Sends saved data to server.
         *
         * @param params Unused.
         * @return Unused.
         */
        @Override
        protected Object doInBackground(Object[] params) {
            HttpPostUtil.saveDataToCloud(GameActivity.this, name, points);
            return null;
        }

        /**
         * Shows saving screen.
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            saving.setVisibility(View.VISIBLE);
        }

        /**
         * Hides saving screen.
         *
         * @param o Unused.
         */
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            saving.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Adds seconds from the time view.
     *
     * @param seconds Amount of seconds to be added.
     */
    public void addSeconds(int seconds) {
        timeSeconds += seconds;
        if (timeSeconds >= 60) {
            timeMinutes++;
            timeSeconds -= 59;
        }
        updateTimeView();
    }

    /**
     * Removes seconds from the time view.
     *
     * @param seconds Amount of seconds to be removed.
     */
    public void removeSeconds(int seconds) {
        timeSeconds -= seconds;
        if (timeSeconds <= 0) {
            if (timeMinutes > 0) {
                timeMinutes--;
                timeSeconds += 59;
            } else {
                timeMinutes = 0;
                timeSeconds = 0;
            }
        }
        updateTimeView();
    }

    /**
     * Updates timer view.
     */
    public void updateTimeView() {
        String mZ = timeMinutes < 10 ? "0" : "";
        String sZ = timeSeconds < 10 ? "0" : "";
        this.timeView.setText(mZ + timeMinutes + ":" + sZ + timeSeconds);
    }

    /**
     * Handles resource loading in asynchronous task.
     * <p>
     * When loading is ready task will inflate the opengl view.
     *
     * @author Niko Mustonen mustonen.niko@gmail.com
     * @version %I%, %G%
     * @since 1.8
     */
    private class LoadFiles extends AsyncTask<Void, Void, Void> {

        /**
         * GLSL shader code.
         */
        String VERTEX_SHADER_CODE, FRAGMENT_SHADER_CODE,
                TEXTURE_VERTEX_SHADER_CODE, TEXTURE_FRAGMENT_SHADER_CODE,
                WIRE_FRAME_VERTEX_SHADER_CODE, WIRE_FRAME_FRAGMENT_SHADER_CODE;

        /**
         * Plane 3D object.
         */
        Plane plane;

        /**
         * Terrain 3D object.
         */
        Terrain terrain;

        /**
         * Stores and handles all the collectable rings in the game.
         */
        Rings rings;

        /**
         * Wire frame effect.
         */
        WireFrameObject wireFrameObjectPlane, wireFrameObjectTerrain;

        /**
         * Loads OpenGl shaders and 3D models
         *
         * @param params Unused.
         * @return Unused.
         */
        protected Void doInBackground(Void... params) {

            //Load color glsl program code
            VERTEX_SHADER_CODE =
                    FileStringReader.readFile(GameActivity.this, R.raw.vertex_shader);
            FRAGMENT_SHADER_CODE =
                    FileStringReader.readFile(GameActivity.this, R.raw.fragment_shader);

            //Load texture glsl program code
            TEXTURE_VERTEX_SHADER_CODE =
                    FileStringReader.readFile(GameActivity.this, R.raw.texture_vertex_shader);
            TEXTURE_FRAGMENT_SHADER_CODE =
                    FileStringReader.readFile(GameActivity.this, R.raw.texture_fragment_shader);

            //Load wireframe glsl program code
            WIRE_FRAME_VERTEX_SHADER_CODE =
                    FileStringReader.readFile(GameActivity.this, R.raw.wire_frame_vertex_shader);
            WIRE_FRAME_FRAGMENT_SHADER_CODE =
                    FileStringReader.readFile(GameActivity.this, R.raw.wire_frame_fragment_shader);

            //Load and set plane
            ObjFileReader planeReader = new ObjFileReader();
            float[] planeVertices = planeReader.readFile(GameActivity.this, R.raw.cube, .1f);
            plane = new Plane(planeVertices);
            plane.setNormalMap(planeReader.getNormalMap());
            wireFrameObjectPlane = new WireFrameObject(
                    WireFrameObject.convertColorVerticesToWireFrameVertices(planeVertices));

            //Load and set terrain
            ObjFileReader terrainReader = new ObjFileReader();
            float[] terrainVertices =
                    terrainReader.readFile(GameActivity.this, R.raw.terrain, 8, 51, 101);
            terrain = new Terrain(terrainVertices, terrainReader.getNormalMap());
            terrain.setCollisionMap(terrainReader.getCollisionMap());
            wireFrameObjectTerrain = new WireFrameObject(
                    WireFrameObject.convertColorVerticesToWireFrameVertices(terrainVertices));

            //Load and set rings
            ObjFileReader ringReader = new ObjFileReader();
            float[] ringVertices = ringReader.readFile(GameActivity.this, R.raw.ring, .5f);
            rings = new Rings(ringVertices, ringReader.getNormalMap(), terrain);

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... progress) {
        }

        /**
         * Initializes game views.
         *
         * @param param
         */
        @Override
        protected void onPostExecute(Void param) {
            mGLView = new MyGLView(GameActivity.this,
                    VERTEX_SHADER_CODE, FRAGMENT_SHADER_CODE,
                    TEXTURE_VERTEX_SHADER_CODE, TEXTURE_FRAGMENT_SHADER_CODE,
                    WIRE_FRAME_VERTEX_SHADER_CODE, WIRE_FRAME_FRAGMENT_SHADER_CODE,
                    plane, terrain, rings, wireFrameObjectPlane, wireFrameObjectTerrain);
            setSoundPool();
            setContentView(mGLView);

            //Layout to be drawn over opengl view. Contains hud and instructions.
            relativeLayout = new RelativeLayout(GameActivity.this);
            relativeLayout.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            relativeLayout.setGravity(Gravity.CENTER);

            //Colors for texts
            String startColor = "#ed21dc";
            String endColor = "#25aff9";
            String startColorRed = "#ff0000";
            String endColorRed = "#000000";

            //Initialize game over font
            gameOver = FontUtil.generateSciFiText(
                    GameActivity.this, " GAME OVER! ", -50, startColorRed, endColorRed);
            gameOver.setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
            ));
            gameOver.setVisibility(View.INVISIBLE);

            //Initializes instruction text font
            tapToStart = FontUtil.generateSciFiText(
                    GameActivity.this, " tap screen to start \n press back to exit ",
                    -50, startColor, endColor);
            tapToStart.setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
            ));

            //Initializes layout for instructions
            LinearLayout linearLayout = new LinearLayout(GameActivity.this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
            ));
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.addView(gameOver);
            linearLayout.addView(tapToStart);

            //Initaializes games point and time views
            pointsView = FontUtil.generateSciFiText(GameActivity.this, "0 points", View.TEXT_ALIGNMENT_TEXT_END, startColor, endColor);
            timeView = FontUtil.generateSciFiText(GameActivity.this, "00:30", View.TEXT_ALIGNMENT_TEXT_START, startColor, endColor);
            addSecondsView = FontUtil.generateSciFiText(GameActivity.this, "+2", -50, startColor, endColor);
            addSecondsView.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
            ));
            addSecondsView.setVisibility(View.INVISIBLE);

            //Adds layouts to game view
            relativeLayout.addView(linearLayout);
            relativeLayout.addView(addSecondsView);
            relativeLayout.addView(pointsView);
            relativeLayout.addView(timeView);
            addContentView(relativeLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

            mGLView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:

                            if (!isGameRunning) {
                                soundPool.stop(streamId);
                                streamId = soundPool.play(soundEngine, 1f, 1f, 1, -1, .5f);
                                if (isGameOver) {
                                    timeMinutes = 0;
                                    timeSeconds = 30;
                                    points = 0;
                                    updateTimeView();
                                    pointsView.setText(points + " points");
                                }
                                isGameOver = false;
                                tapToStart.setVisibility(View.INVISIBLE);
                                gameOver.setVisibility(View.INVISIBLE);
                                timer = new Timer();
                                timer.schedule(new MyTimerTask(), 1000, 1000);
                                isGameRunning = true;
                            } else {
                                setSoundRate(true);
                                renderer.setTurbo(true);
                            }
                            return true;
                        case MotionEvent.ACTION_UP:
                            if (isGameRunning) {
                                renderer.setTurbo(false);
                                setSoundRate(false);
                            }
                            return true;
                    }
                    return false;
                }
            });
        }
    }

    int streamId;

    /**
     * Sets higher pitch if turbo is on.
     *
     * @param is Is turbo on.
     */
    public void setSoundRate(boolean is) {
        soundPool.stop(streamId);
        if (is) {
            streamId = soundPool.play(soundEngine, 1f, 1f, 1, -1, .6f);
        } else {
            streamId = soundPool.play(soundEngine, 1f, 1f, 1, -1, .5f);
        }
    }

    /**
     * Handles back button pressing.
     * <p>
     * Will return to main menu if game is on pause and pauses the game if the game is running.
     */
    @Override
    public void onBackPressed() {
        if (isGameRunning) {
            tapToStart.setVisibility(View.VISIBLE);
            isGameRunning = false;
            renderer.setTurbo(false);
            timer.cancel();
            timer = null;
        } else if (mGLView != null) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

/**
 * Antialiasing configurations.
 *
 * @author Niko Mustonen mustonen.niko@gmail.com
 * @version %I%, %G%
 * @since 1.8
 */
class Configuration implements GLSurfaceView.EGLConfigChooser {
    @Override
    public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
        int values[] = {
                EGL10.EGL_LEVEL, 0,
                EGL10.EGL_RENDERABLE_TYPE, 4,
                EGL10.EGL_COLOR_BUFFER_TYPE, EGL10.EGL_RGB_BUFFER,
                EGL10.EGL_RED_SIZE, 8,
                EGL10.EGL_GREEN_SIZE, 8,
                EGL10.EGL_BLUE_SIZE, 8,
                EGL10.EGL_DEPTH_SIZE, 16,
                EGL10.EGL_SAMPLE_BUFFERS, 1,
                EGL10.EGL_SAMPLES, 4,
                EGL10.EGL_NONE
        };
        EGLConfig[] configs = new EGLConfig[1];
        int[] configCounts = new int[1];
        egl.eglChooseConfig(display, values, configs, 1, configCounts);
        return configs[0];
    }
}
