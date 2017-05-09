package com.mustonen.niko.hyperflightsimulatorultra3d.object;

import com.mustonen.niko.hyperflightsimulatorultra3d.geometry.Vector3D;
import com.mustonen.niko.hyperflightsimulatorultra3d.opengl.ColorProgram;

import static android.opengl.GLES20.*;

/**
 * Class for terrain objects.
 *
 * @author Niko Mustonen mustonen.niko@gmail.com
 * @version %I%, %G%
 * @since 1.8
 */
public class Terrain extends Colored3DObject {
    /**
     * X dimension definition.
     */
    float minX, maxX, difX;

    /**
     * Z dimension definition.
     */
    float minZ, maxZ, difZ;

    /**
     * Vertex count.
     */
    private int count;

    /**
     * Collision height map for collision checking.
     */
    private float[][] collisionMap;

    /**
     * Generates terrain object.
     *
     * @param vertices Terrain vertices.
     * @param normals  Terrain normals for calculating light direction.
     */
    public Terrain(float[] vertices, float[] normals) {
        count = vertices.length / 8;
        setLightDirection(vertices, normals, new Vector3D(new float[]{1f, 0, -1f}).getNormalizedVersion());
        setVertices(new Vertices(vertices));
    }

    /**
     * Draws terrain.
     */
    public void draw() {
        glDrawArrays(GL_TRIANGLES, 0, count);
    }

    /**
     * Sets light direction for the terrain.
     *
     * @param vertices Terrain vertices.
     * @param normals  Terrain vertex normals.
     * @param light    Light direction.
     */
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

            vertices[i + 4] = .3f + dot;
            vertices[i + 5] = .1f;
            vertices[i + 6] = .3f + dot;

            normalIndex += 3;
        }

        difX = maxX - minX;
        difZ = maxZ - minZ;
    }

    /**
     * Stores index.
     */
    int i = 0;

    /**
     * Checks wheter there was a collision between the world and the plane.
     *
     * @param wX World position x.
     * @param wY World position y.
     * @param wZ World position z.
     * @return True is there was a collision.
     */
    public boolean checkWorldCollision(float wX, float wY, float wZ) {

        int col = (int) ((wX - minX) / difX * (float) collisionMap[0].length);
        int row = (int) ((-wZ - minZ) / difZ * (float) collisionMap.length);

        if (col < 0) col = 0;
        if (col > collisionMap[0].length - 1) col = collisionMap[0].length - 1;
        if (row < 0) row = 0;
        if (row > collisionMap.length - 1) row = collisionMap.length - 1;

        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if ((collisionMap[row][col] - wY - 11.5) > -1f) return true;
            }
        }
        return false;
    }

    /**
     * Sets collision map for this object.
     *
     * @param collisionMap Pre generated collision map.
     */
    public void setCollisionMap(float[][] collisionMap) {
        this.collisionMap = collisionMap;
    }

    /**
     * Generates random position from the map.
     *
     * @param rings Are used for defining positions that are already in use.
     * @return New random x, y and z position.
     */
    public float[] getRandomPosition(Ring[] rings) {

        boolean isValid;
        float poX;
        float poZ;

        do {
            isValid = true;
            poX = (float) (Math.random() * difX + minX);
            poZ = (float) (Math.random() * difZ + minZ);

            for (Ring r : rings) {
                if (poX > r.getX() - 1f && poX < r.getX() + 1f && poZ > r.getZ() - 1f && poZ < r.getZ() + 1f) {
                    isValid = false;
                }
            }
        } while (!isValid);

        int col = (int) ((poX - minX) / difX * (float) collisionMap[0].length);
        int row = (int) ((poZ - minZ) / difZ * (float) collisionMap.length);

        float minHeight = collisionMap[row][col] + 10f - 10f;

        return new float[]{poX, minHeight, poZ};
    }

    /**
     * Unused in this class.
     *
     * @param modelMatrix               Unused.
     * @param viewProjectionMatrix      Unused.
     * @param modelViewProjectionMatrix Unused.
     * @param colorProgram              Unused.
     * @param wireFramProgram           Unused.
     * @param wX                        Unused.
     * @param wY                        Unused.
     * @param wZ                        Unused.
     * @param light                     Unused.
     */
    @Override
    public void draw(
            float[] modelMatrix,
            float[] viewProjectionMatrix,
            float[] modelViewProjectionMatrix,
            ColorProgram colorProgram,
            ColorProgram wireFramProgram,
            float wX, float wY, float wZ, Vector3D light) {
    }

    /**
     * Return minimum x position from the map.
     *
     * @return Minimum x position.
     */
    public float getMinX() {
        return minX;
    }

    /**
     * Return maximum x position from the map.
     *
     * @return Maximum x position.
     */
    public float getMaxX() {
        return maxX;
    }

    /**
     * Return minimum z position from the map.
     *
     * @return Minimum z position.
     */
    public float getMinZ() {
        return minZ;
    }

    /**
     * Return maximum z position from the map.
     *
     * @return Maximum z position.
     */
    public float getMaxZ() {
        return maxZ;
    }

    /**
     * Stores wire color.
     */
    float[] wireColor = {0, 0, .8f, 1};

    /**
     * Returns wire color.
     *
     * @return Wire color.
     */
    public float[] getWireColor() {
        return wireColor;
    }
}
