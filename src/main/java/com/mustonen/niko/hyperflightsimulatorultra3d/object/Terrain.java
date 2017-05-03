package com.mustonen.niko.hyperflightsimulatorultra3d.object;

import com.mustonen.niko.hyperflightsimulatorultra3d.geometry.Vector3D;
import com.mustonen.niko.hyperflightsimulatorultra3d.opengl.ColorProgram;
import com.mustonen.niko.hyperflightsimulatorultra3d.util.Debug;

import static android.opengl.GLES20.*;

public class Terrain extends Colored3DObject {
    float minX, maxX, difX;
    float minZ, maxZ, difZ;

    private int count;
    private float[][] collisionMap;

    public Terrain(float[] vertices, float[] normals) {
        count = vertices.length / 8;
        setLightDirection(vertices, normals, new Vector3D(new float[]{1f, -1f, 0f}).getNormalizedVersion());
        setVertices(new Vertices(vertices));
    }

    public void draw() {
        glDrawArrays(GL_TRIANGLES, 0, count);
    }

    public void setLightDirection(float[] vertices, float[] normals, Vector3D light) {

        int normalIndex = 0;

        for (int i = 0; i < vertices.length; i += 8) {

            float dot = light.getDotProduct(
                    normals[normalIndex],
                    normals[normalIndex + 1],
                    normals[normalIndex + 2]
            ) / 10f;

            minX = Math.min(vertices[i], minX);
            maxX = Math.max(vertices[i], maxX);

            minZ = Math.min(vertices[i + 2], minZ);
            maxZ = Math.max(vertices[i + 2], maxZ);

            vertices[i + 4] = .05f + (dot / 3f);
            vertices[i + 5] = .5f + dot;
            vertices[i + 6] = .05f + (dot / 3f);

            normalIndex += 3;
        }

        difX = maxX - minX;
        difZ = maxZ - minZ;
    }

    int i = 0;

    public boolean checkWorldCollision(float wX, float wY, float wZ) {

        int col = (int) ((wX + 1f - minX) / difX * (float) collisionMap[0].length);
        int row = collisionMap.length - (int) ((wZ + 1f - minZ) / difZ * (float) collisionMap.length);

        if(col < 0) col = 0;
        if(col > collisionMap[0].length - 1)col = collisionMap[0].length - 1;
        if(row < 0) row = 0;
        if(row > collisionMap.length - 1)row = collisionMap.length - 1;

        for(int i = row - 1; i <= row + 1; i++) {
            for(int j = col; j <= col + 1; j++) {
                if ((collisionMap[row][col] - wY - 11.5) > -1f) return true;
            }
        }
        return false;
    }

    public void setCollisionMap(float[][] collisionMap) {
        this.collisionMap = collisionMap;
    }

    public float[] getRandomPosition() {

        float poX = (float)(Math.random() * difX + minX);
        float poZ = (float)(Math.random() * difZ + minZ);

        int col = (int) ((poX - minX) / difX * (float) collisionMap[0].length) + 1;
        int row = collisionMap.length - 1 - (int) ((poZ - minZ) / difZ * (float) collisionMap.length);

        float minHeight = collisionMap[row][col] + 10f - 10f;

        return new float[]{poX, minHeight, poZ};
    }

    @Override
    public void draw(
            float[] modelMatrix,
            float[] viewProjectionMatrix,
            float[] modelViewProjectionMatrix,
            ColorProgram colorProgram, float wX, float wY, float wZ, Vector3D light) {
    }

    public float getMinX() {
        return minX;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMinZ() {
        return minZ;
    }

    public float getMaxZ() {
        return maxZ;
    }
}
