package com.mustonen.niko.hyperflightsimulatorultra3d;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.support.v4.app.FragmentActivity;

import com.mustonen.niko.hyperflightsimulatorultra3d.util.FontUtil;
import com.mustonen.niko.hyperflightsimulatorultra3d.util.MusicService;

import org.json.JSONArray;
import org.json.JSONException;
import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.io.BufferedInputStream;
import java.net.MalformedURLException;

/**
 * Main menu screen.
 *
 * @author Niko Mustonen mustonen.niko@gmail.com
 * @version %I%, %G%
 * @since 1.8
 */
public class MainActivity extends FragmentActivity {

    /**
     * Defines whether highscores are been loaded.
     */
    public boolean isLoading = false;

    /**
     * Stores list view fragment for the 10 best highscores globally.
     */
    HighScoreFragment hsf;

    /**
     * Stores main layout screen for the main menu screen.
     */
    LinearLayout l;

    Intent musicServiceIntent;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        hsf = (HighScoreFragment) getFragmentManager().findFragmentById(R.id.highscore_list_fragment);
        getFragmentManager().beginTransaction().hide(hsf).commit();

        String startColor = "#ed21dc";
        String endColor = "#25aff9";
        String title1 = " hyper flight \n simulator ";
        String title2 = " ultra 3d ";
        l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView t1 = FontUtil.generateSciFiText(this, title1,
                View.TEXT_ALIGNMENT_CENTER, startColor, endColor);
        t1.setLayoutParams(textParams);
        TextView t2 = FontUtil.generateSciFiText(this, title2,
                View.TEXT_ALIGNMENT_CENTER, startColor, endColor);
        t2.setLayoutParams(textParams);
        t2.setPadding(0, 0, 0, 0);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        l.addView(t1);
        l.addView(t2);
        addContentView(l, params);

        loading = FontUtil.getLoadingScreen(this);
        loading.getBackground().setAlpha(200);
        addContentView(loading, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        loading.setVisibility(View.INVISIBLE);
        musicServiceIntent = new Intent(this, MusicService.class);
        startService(musicServiceIntent);
    }

    /**
     * Starts game and is inflated from the main menu screen.
     *
     * @param view Unused.
     */
    public void startGame(View view) {
        if (!isLoading) {
            Intent i = new Intent(this, GameActivity.class);
            startActivity(i);
        }
    }

    /**
     * Loading text.
     */
    LinearLayout loading;

    /**
     * Starts highscore loading from the server and is inflated from the main menu screen.
     *
     * @param view Unused.
     */
    public void loadScore(View view) {
        if (!isLoading && hsf != null && hsf.isVisible()) {
            getFragmentManager().beginTransaction().hide(hsf).commit();
            l.setVisibility(View.VISIBLE);
        } else if(!isLoading){
            new LoadScore().execute();
        }
    }

    public void exit(View view) {
        System.exit(1);
    }

    /**
     * Views loaded highscores by setting scores to highscore fragment and displays it.
     */
    public void viewHighScore(JSONArray highScoreList) {
        hsf.setHighScoreList(highScoreList);
        getFragmentManager().beginTransaction().show(hsf).commit();
    }

    /**
     * Stores loaded highscores.
     */
    JSONArray highScore;

    /**
     * Highscore loader.
     *
     * @author Niko Mustonen mustonen.niko@gmail.com
     * @version %I%, %G%
     * @since 1.8
     */
    class LoadScore extends AsyncTask {

        /**
         * Loads highscore from the server.
         *
         * @param params Unused.
         * @return Unused.
         */
        @Override
        protected Object doInBackground(Object[] params) {

            highScore = getHighScore();
            viewHighScore(highScore);

            return null;
        }

        /**
         * Shows loading text.
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isLoading = true;
            loading.setVisibility(View.VISIBLE);
        }

        /**
         * Hides useles information from the screen after loading is done.
         *
         * @param o Unused.
         */
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            loading.setVisibility(View.INVISIBLE);
            l.setVisibility(View.INVISIBLE);
            isLoading = false;
        }
    }

    /**
     * Handles back button pressing.
     */
    @Override
    public void onBackPressed() {

        if (hsf != null && hsf.isVisible()) {
            getFragmentManager().beginTransaction().hide(hsf).commit();
            l.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Shut dow the main music.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(musicServiceIntent);
    }

    /**
     * Loads list of highscore elements from the server.
     *
     * @return JSONArray of loaded highscore from the server.
     */
    public JSONArray getHighScore() {
        try {
            URL url = new URL("https://powerful-everglades-31936.herokuapp.com/highscores");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(10000);
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            StringWriter writer = new StringWriter();
            IOUtils.copy(in, writer);
            JSONArray response = new JSONArray(writer.toString());

            writer.close();
            urlConnection.disconnect();
            return response;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
