package com.mustonen.niko.hyperflightsimulatorultra3d.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Class for reading .obj files.
 *
 * @author Niko Mustonen mustonen.niko@gmail.com
 * @version %I%, %G%
 * @since 1.8
 */
public class ObjFileReader {

    /**
     * Stores vertices in an array.
     */
    private float[] verticesArray;

    /**
     * Stores object normals in an array if they are stored in the obj file.
     */
    private float[] normalsArray;

    /**
     * Amount of rows or cols in object.
     */
    private int rows, cols;

    /**
     * Stores collision for terrains.
     */
    private float[][] collisionMap;

    /**
     * Reads .obj file.
     *
     * @param c      Activity context.
     * @param fileId File id.
     * @param scale  Object scale.
     * @return Return objects vertices as an array of floats.
     */
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

    /**
     * Reads .obj file and generates collision map based on its height positions.
     *
     * @param c       Activity context.
     * @param fileId  File id.
     * @param scale   Object scale.
     * @param objRows Rows in object.
     * @param objCols Cols in object.
     * @return Return objects vertices as an array of floats.
     */
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

    /**
     * Regex for lines that start with vertex indicator.
     */
    private static final String VERTICES = "(^[v ]*)";

    /**
     * Regex for lines that start with vertex normal indicator.
     */
    private static final String VERTICES_NORMAL = "(^[vn ]*)";

    /**
     * Regex for lines that start with polygon face indicator.
     */
    private static final String FACE = "(^[f ]*)";

    /**
     * Converts file line to float coordinates.
     *
     * @param line  Line to be converted.
     * @param scale Object scale.
     * @return Returns converted line as a float array.
     */
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

    /**
     * Converts file line to float coordinates.
     *
     * @param line    Line to be converted.
     * @param scale   Object scale.
     * @param objRows Rows in object.
     * @param objCols Cols in object.
     * @return
     */
    private float[] lineToCoords(String line, float scale, int objRows, int objCols) {
        line = line.trim();
        String trimmedLine = line.replaceAll(VERTICES, "");
        trimmedLine = trimmedLine.replaceAll(VERTICES_NORMAL, "");
        String[] coordsString = trimmedLine.split(" ");
        float[] coords = new float[coordsString.length];
        try {
            for (int i = 0; i < coords.length; i++) {
                coords[i] = Float.parseFloat(coordsString[i]) * scale;

                if (i == 1) {
                    this.collisionMap[this.rows][this.cols] = coords[i];
                    this.cols++;
                    if (this.cols >= objCols) {
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

    /**
     * Adds points to vertices array.
     *
     * @param verticesBuffer Array to store new values.
     * @param vertices       Current vertices.
     * @param line           Line to define vertices.
     */
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

    /**
     * Returns objects normal map.
     *
     * @return Normal map float array.
     */
    public float[] getNormalMap() {
        return this.normalsArray;
    }

    /**
     * Returns collision map.
     *
     * @return Collision map float array.
     */
    public float[][] getCollisionMap() {
        return this.collisionMap;
    }

    /**
     * Converts string line to normal indexes.
     *
     * @param line Converted line.
     * @return Normal indexes.
     */
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