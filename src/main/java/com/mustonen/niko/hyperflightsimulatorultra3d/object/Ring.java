package com.mustonen.niko.hyperflightsimulatorultra3d.object;

import com.mustonen.niko.hyperflightsimulatorultra3d.geometry.Vector3D;
import com.mustonen.niko.hyperflightsimulatorultra3d.opengl.ColorProgram;

import static android.opengl.GLES20.*;

/**
 * Class for generating ring 3D object.
 *
 * @author Niko Mustonen mustonen.niko@gmail.com
 * @version %I%, %G%
 * @since 1.8
 */
public class Ring extends Colored3DObject {

    /**
     * Ring position.
     */
    public float x, y, z;

    /**
     * Ring vertex count.
     */
    private int count;

    /**
     * Ring normal map for light direction.
     */
    private float[] normalMap;

    /**
     * Generates Ring 3d object.
     *
     * @param vertices       Ring vertices.
     * @param lightDirection Light direction.
     * @param normalMap      Normal map for defining lighting.
     */
    public Ring(float[] vertices, Vector3D lightDirection, float[] normalMap) {
        count = vertices.length / 8;
        setLightDirection(vertices, lightDirection, normalMap);
        setVertices(new Vertices(vertices));
    }

    /**
     * Sets new position to the ring.
     *
     * @param coords New coordinatres.
     */
    public void setPosition(float[] coords) {
        this.x = coords[0];
        this.y = coords[1];
        this.z = coords[2];
    }

    /**
     * Returns rings x position.
     *
     * @return X position.
     */
    public float getX() {
        return x;
    }

    /**
     * Returns rings z position.
     *
     * @return Z position.
     */
    public float getZ() {
        return z;
    }

    /**
     * Unused in this class.
     *
     * @param modelMatrix               Unused.
     * @param viewProjectionMatrix      Unused.
     * @param modelViewProjectionMatrix Unused.
     * @param colorProgram              Unused.
     * @param wireFrameProgram          Unused.
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
            ColorProgram wireFrameProgram, float wX, float wY, float wZ, Vector3D light) {
    }

    /**
     * Set light direction for the ring.
     *
     * @param vertices       Ring vertices.
     * @param lightDirection Light direction.
     * @param normalMap      Normal map for helping light calculation.
     */
    public void setLightDirection(float[] vertices, Vector3D lightDirection, float[] normalMap) {
        int normalIndex = 0;
        for (int i = 0; i < vertices.length; i += 8) {

            float dot = lightDirection.getDotProduct(
                    normalMap[normalIndex],
                    normalMap[normalIndex + 1],
                    normalMap[normalIndex + 2]
            ) * 10f / 2f;

            vertices[i + 4] = 0.5f + dot;
            vertices[i + 5] = .0f;
            vertices[i + 6] = 0.125f + -dot / 4f;

            normalIndex += 3;
        }
    }

    /**
     * Checks whether there was collision between the plane and the ring.
     *
     * @param wX World position x.
     * @param wY World position y.
     * @param wZ World position z.
     * @return
     */
    public boolean isCollision(float wX, float wY, float wZ) {

        return Math.abs(-wX + x) <= 2f && Math.abs(-wY + 1.0f + y) <= 2.5f && Math.abs(+wZ + z) <= 2f;
    }

    /**
     * Draws the ring.
     */
    @Override
    public void draw() {
        glDrawArrays(GL_TRIANGLES, 0, count);
    }

    /**
     * Sets normal map for the ring to help light direction calculation.
     *
     * @param normalMap New normal map.
     */
    public void setNormalMap(float[] normalMap) {
        this.normalMap = normalMap;
    }
}
