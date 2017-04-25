package com.mustonen.niko.hyperflightsimulatorultra3d.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ObjFileReader {

    public static float[] readFile(Context c, int fileId) {

        ArrayList<float[]> vertices = new ArrayList<>();
        ArrayList<Float> verticesBuffer = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(c.getResources().openRawResource(fileId)))) {

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("v ")) {
                    float[] coords = lineToCoords(line);
                    if (coords != null) {
                        vertices.add(coords);
                    }
                } else if (line.startsWith("f ")) {
                    addPoints(verticesBuffer, vertices, line);
                }
            }

        } catch (IOException e) {
            Debug.exception(null, e);
            return null;
        }

        float[] verticesArray = new float[verticesBuffer.size()];
        int i = 0;
        for (Float f : verticesBuffer) {
            verticesArray[i++] = (f != null ? f : 0.0f);
        }
        return verticesArray;
    }

    public static final String VERTICES = "(^[v ]*)";
    public static final String FACE = "(^[f ]*)";

    private static float[] lineToCoords(String line) {
        line = line.trim();
        String trimmedLine = line.replaceAll(VERTICES, "");
        String[] coordsString = trimmedLine.split(" ");
        float[] coords = new float[coordsString.length];
        try {
            for (int i = 0; i < coords.length; i++) {
                coords[i] = Float.parseFloat(coordsString[i]) / 10f;
            }
            return coords;
        } catch (Exception e) {
            Debug.exception(null, e);
        }
        return null;
    }

    private static void addPoints(ArrayList<Float> verticesBuffer,
                                  ArrayList<float[]> vertices,
                                  String line) {

        line = line.replaceFirst(FACE, "").trim();
        String[] vertex = line.split(" ");

        for(String s : vertex) {
            String[] points = s.split("/");
            try {
                int saakeli = Integer.parseInt(points[0]) - 1;
                float[] v = vertices.get(saakeli);

                verticesBuffer.add(v[0]);
                verticesBuffer.add(v[1]);
                verticesBuffer.add(v[2]);
                verticesBuffer.add(1f); // w
                verticesBuffer.add((float)Math.random()); // red
                verticesBuffer.add((float)Math.random()); // green
                verticesBuffer.add((float)Math.random()); // blue
                verticesBuffer.add(1f); // alpha
            } catch (Exception e) {
                Debug.exception(null, e);
            }
        }
    }
}