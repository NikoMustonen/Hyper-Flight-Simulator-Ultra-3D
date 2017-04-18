package com.mustonen.niko.hyperflightsimulatorultra3d.object;

import java.util.ArrayList;

import static android.opengl.GLES20.*;

public class Wall extends Textured3DObject {

    private int wallDrawSize;

    public Wall() {
        setVertices(new Vertices(getWallVertices()));
    }

    @Override
    public void draw() {
        glDrawArrays(GL_TRIANGLE_STRIP, 0, wallDrawSize);
    }

    private float[] getWallVertices() {

        ArrayList<float[]> list = new ArrayList<>();

        for(int i = -10; i <= 50 -4 ; i+=4) {
            list.add(new float[] {
                    (float)i, 1.0f, -10.0f, 1.0f, 1f, 0f,
                    (float)i, -1.0f, -10.0f, 1.0f, 1f, 1f,
                    (float)i + 2, 1.0f, -10.0f, 1.0f, 0f, 0f,
                    (float)i + 2f, -1.0f, -10.0f, 1.0f, 0f, 1f
            });
        }

        for(int i = -10; i <= 10 -4 ; i+=4) {
            list.add(new float[] {
                    -10.0f, 1.0f, (float)i, 1.0f, 1f, 0f,
                    -10.0f, -1.0f, (float)i, 1.0f, 1f, 1f,
                    -10.0f, 1.0f, (float)i + 2, 1.0f, 0f, 0f,
                    -10.0f, -1.0f, (float)i + 2f, 1.0f, 0f, 1f
            });
        }

        for(int i = -10; i <= 50 -4 ; i+=4) {
            list.add(new float[] {
                    (float)i, 1.0f, 10.0f, 1.0f, 1f, 0f,
                    (float)i, -1.0f, 10.0f, 1.0f, 1f, 1f,
                    (float)i + 2, 1.0f, 10.0f, 1.0f, 0f, 0f,
                    (float)i + 2f, -1.0f, 10.0f, 1.0f, 0f, 1f
            });
        }

        wallDrawSize = list.size() * 4;
        float[] array = new float[wallDrawSize * 6];
        int index = 0;

        for(float[] fArray : list) {
            for(float f : fArray) {
                array[index] = f;
                index++;
            }
        }

        return array;
    }
}
