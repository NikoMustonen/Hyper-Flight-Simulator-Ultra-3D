package com.mustonen.niko.hyperflightsimulatorultra3d.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ObjFileReader {

    private float[] verticesArray;
    private float[] normalsArray;

    private int rows, cols;
    private float[][] collisionMap;

    public float[] readFile(Context c, int fileId, float scale) {

        ArrayList<float[]> normals = new ArrayList<>();
        ArrayList<Float> normalsInOrder = new ArrayList<>();
        ArrayList<float[]> vertices = new ArrayList<>();
        ArrayList<Float> verticesBuffer = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(c.getResources().openRawResource(fileId)))) {

            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("v ")) {
                    float[] coords = lineToCoords(line, scale);
                    if (coords != null) {
                        vertices.add(coords);
                    }
                } else if (line.startsWith("f ")) {
                    addPoints(verticesBuffer, vertices, line);

                    int[] indexes = getNormalIndexes(line);
                    for (int i : indexes) {
                        normalsInOrder.add(normals.get(i)[0]);
                        normalsInOrder.add(normals.get(i)[1]);
                        normalsInOrder.add(normals.get(i)[2]);
                    }
                } else if (line.startsWith("vn ")) {
                    normals.add(lineToCoords(line, scale));
                }
            }

        } catch (IOException e) {
            Debug.exception(null, e);
            return null;
        }

        normalsArray = new float[normalsInOrder.size()];
        int iN = 0;
        for (Float f : normalsInOrder) {
            normalsArray[iN++] = (f != null ? f : 0.0f);
        }

        verticesArray = new float[verticesBuffer.size()];
        int i = 0;
        for (Float f : verticesBuffer) {
            verticesArray[i++] = (f != null ? f : 0.0f);
        }
        return verticesArray;
    }

    public float[] readFile(Context c, int fileId, float scale, int objRows, int objCols) {

        this.collisionMap = new float[objRows][objCols];

        ArrayList<float[]> normals = new ArrayList<>();
        ArrayList<Float> normalsInOrder = new ArrayList<>();
        ArrayList<float[]> vertices = new ArrayList<>();
        ArrayList<Float> verticesBuffer = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(c.getResources().openRawResource(fileId)))) {

            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("v ")) {
                    float[] coords = lineToCoords(line, scale, objRows, objCols);
                    if (coords != null) {
                        vertices.add(coords);
                    }
                } else if (line.startsWith("f ")) {
                    addPoints(verticesBuffer, vertices, line);

                    int[] indexes = getNormalIndexes(line);
                    for (int i : indexes) {
                        normalsInOrder.add(normals.get(i)[0]);
                        normalsInOrder.add(normals.get(i)[1]);
                        normalsInOrder.add(normals.get(i)[2]);
                    }
                } else if (line.startsWith("vn ")) {
                    normals.add(lineToCoords(line, scale));
                }
            }

        } catch (IOException e) {
            Debug.exception(null, e);
            return null;
        }

        normalsArray = new float[normalsInOrder.size()];
        int iN = 0;
        for (Float f : normalsInOrder) {
            normalsArray[iN++] = (f != null ? f : 0.0f);
        }

        verticesArray = new float[verticesBuffer.size()];
        int i = 0;
        for (Float f : verticesBuffer) {
            verticesArray[i++] = (f != null ? f : 0.0f);
        }
        return verticesArray;
    }

    private static final String VERTICES = "(^[v ]*)";
    private static final String VERTICES_NORMAL = "(^[vn ]*)";
    private static final String FACE = "(^[f ]*)";

    private float[] lineToCoords(String line, float scale) {
        line = line.trim();
        String trimmedLine = line.replaceAll(VERTICES, "");
        trimmedLine = trimmedLine.replaceAll(VERTICES_NORMAL, "");
        String[] coordsString = trimmedLine.split(" ");
        float[] coords = new float[coordsString.length];
        try {
            for (int i = 0; i < coords.length; i++) {
                coords[i] = Float.parseFloat(coordsString[i]) * scale;
            }
            return coords;
        } catch (Exception e) {
            Debug.exception(null, e);
        }
        return null;
    }

    private float[] lineToCoords(String line, float scale, int objRows, int objCols) {
        line = line.trim();
        String trimmedLine = line.replaceAll(VERTICES, "");
        trimmedLine = trimmedLine.replaceAll(VERTICES_NORMAL, "");
        String[] coordsString = trimmedLine.split(" ");
        float[] coords = new float[coordsString.length];
        try {
            for (int i = 0; i < coords.length; i++) {
                coords[i] = Float.parseFloat(coordsString[i]) * scale;

                if(i == 1) {
                    this.collisionMap[this.rows][this.cols] = coords[i];
                    this.cols++;
                    if(this.cols >= objCols) {
                        this.cols = 0;
                        this.rows++;
                    }
                }
            }
            return coords;
        } catch (Exception e) {
            Debug.exception(null, e);
        }
        return null;
    }

    private void addPoints(ArrayList<Float> verticesBuffer,
                                  ArrayList<float[]> vertices,
                                  String line) {

        line = line.replaceFirst(FACE, "").trim();
        boolean isSlash = line.contains("/");
        String[] vertex = line.split(" ");

        for (String s : vertex) {
            try {
                int pointIndex;
                if (isSlash) {
                    String points[] = s.split("/");
                    pointIndex = Integer.parseInt(points[0]) - 1;
                } else {
                    pointIndex = Integer.parseInt(s) - 1;
                }

                float[] v = vertices.get(pointIndex);

                verticesBuffer.add(v[0]);
                verticesBuffer.add(v[1]);
                verticesBuffer.add(v[2]);

                verticesBuffer.add(1f); // w
                verticesBuffer.add(.5f); // red
                verticesBuffer.add(.5f); // green
                verticesBuffer.add(.5f); // blue
                verticesBuffer.add(1f); // alpha
            } catch (Exception e) {
                Debug.exception(null, e);
            }
        }
    }

    public float[] getNormalMap() {
        return this.normalsArray;
    }

    public float[][] getCollisionMap() {
        return this.collisionMap;
    }

    private int[] getNormalIndexes(String line) {

        line = line.replaceFirst(FACE, "").trim();
        String[] vertex = line.split(" ");
        int[] indexes = new int[3];
        int i = 0;

        for (String s : vertex) {
            String[] points = s.split("/");
            try {
                int index = Integer.parseInt(points[2]) - 1;
                indexes[i++] = index;
            } catch (Exception e) {
                Debug.exception(null, e);
            }
        }
        return indexes;
    }
}