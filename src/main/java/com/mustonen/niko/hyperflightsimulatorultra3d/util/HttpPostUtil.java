package com.mustonen.niko.hyperflightsimulatorultra3d.util;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class for sending http post to game server to save high score data.
 *
 * @author Niko Mustonen mustonen.niko@gmail.com
 * @version %I%, %G%
 * @since 1.8
 */
public class HttpPostUtil {

    /**
     * Saves high score data to server.
     *
     * @param c Activity context.
     * @param name User name.
     * @param points Game points.
     * @return True if success.
     */
    public static boolean saveDataToCloud(Context c, String name, int points) {

        try {
            URL url = new URL("https://powerful-everglades-31936.herokuapp.com/highscores");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setChunkedStreamingMode(0);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setConnectTimeout(10000);
            con.connect();

            JSONObject highScore = new JSONObject();
            highScore.put("name", name);
            highScore.put("points", points);
            highScore.put("id", 0);

            OutputStream dos = con.getOutputStream();
            dos.write(highScore.toString().getBytes());
            dos.flush();
            dos.close();
            con.disconnect();
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(c, "Cannot access server!", Toast.LENGTH_SHORT).show();
        return false;
    }
}
