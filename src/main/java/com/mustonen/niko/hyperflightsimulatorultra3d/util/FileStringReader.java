package com.mustonen.niko.hyperflightsimulatorultra3d.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileStringReader {

    public static String readFile(Context c, int fileId) {

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(c.getResources().openRawResource(fileId)))) {

            StringBuilder sb = new StringBuilder();
            String line;

            while((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            Debug.exception(null, e);
            return null;
        }
    }
}
