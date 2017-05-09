package com.mustonen.niko.hyperflightsimulatorultra3d.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class for reading files and converting them to string format.
 *
 * @author Niko Mustonen mustonen.niko@gmail.com
 * @version %I%, %G%
 * @since 1.8
 */
public class FileStringReader {

    /**
     * Converts file to string.
     *
     * @param c      Activity context.
     * @param fileId File id.
     * @return Converted file as a string format.
     */
    public static String readFile(Context c, int fileId) {

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(c.getResources().openRawResource(fileId)))) {

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            Debug.exception(null, e);
            return null;
        }
    }
}
