package com.mustonen.niko.hyperflightsimulatorultra3d.object;

import com.mustonen.niko.hyperflightsimulatorultra3d.geometry.Vector3D;
import com.mustonen.niko.hyperflightsimulatorultra3d.opengl.ColorProgram;
import com.mustonen.niko.hyperflightsimulatorultra3d.util.Debug;

import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_LINE_STRIP;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;

public class CollisionMapDebug extends Colored3DObject {

    private int count;

    public CollisionMapDebug(float[][] collisionMap, float miX, float maX, float miZ, float maZ) {

        setVertices(new Vertices(getMap(collisionMap, miX, maX, miZ, maZ)));
        count = collisionMap.length * collisionMap[0].length;
    }

    @Override
    public void draw() {
        glDrawArrays(GL_POINTS, 0, count);
    }

    @Override
    public void draw(float[] modelMatrix, float[] viewProjectionMatrix, float[] modelViewProjectionMatrix, ColorProgram colorProgram, float wX, float wY, float wZ, Vector3D light) {

    }

    private float[] getMap(float[][] collisionMap, float miX, float maX, float miZ, float maZ) {
        int size = collisionMap.length * collisionMap[0].length * 8;
        float[] vertices = new float[size];
        int vertexIndex = 0;

        float x, z;

        for(int i = 0; i < collisionMap.length; i++) {
            StringBuilder sb = new StringBuilder();
            z = -(miZ + (float)i / ((float)collisionMap.length - 1f) * (maZ - miZ));


            for(int j = 0; j < collisionMap[i].length; j++) {

                x = miX + (float)j /((float)collisionMap[i].length - 1f) * (maX - miX);

                vertices[vertexIndex] = x;
                vertices[vertexIndex + 1] = collisionMap[i][j];
                vertices[vertexIndex + 2] = z;
                sb.append("X: ").append(vertices[vertexIndex]);
                //sb.append("Y: ").append(vertices[vertexIndex + 1]);
                //sb.append("Z: ").append(String.format("%.2f, ", vertices[vertexIndex + 2]));
                vertices[vertexIndex + 3] = 1f;
                vertices[vertexIndex + 4] = 1f;
                vertices[vertexIndex + 5] = 0f;
                vertices[vertexIndex + 6] = 0f;
                vertices[vertexIndex + 7] = 1f;
                vertexIndex += 8;
            }
            Debug.debug(null, sb.toString());
        }

        return vertices;
    }
}
