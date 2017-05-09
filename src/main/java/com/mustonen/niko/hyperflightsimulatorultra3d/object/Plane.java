package com.mustonen.niko.hyperflightsimulatorultra3d.object;

import com.mustonen.niko.hyperflightsimulatorultra3d.geometry.Vector3D;
import com.mustonen.niko.hyperflightsimulatorultra3d.opengl.ColorProgram;

import static android.opengl.GLES20.*;

/**
 * Class for generating 3D plane object.
 *
 * @author Niko Mustonen mustonen.niko@gmail.com
 * @version %I%, %G%
 * @since 1.8
 */
public class Plane extends Colored3DObject {

    /**
     * Plane vertex count
     */
    private int count;

    /**
     * Plane normal map for helping light direction calculation.
     */
    private float[] normalMap;

    /**
     * Generates 3D plane object from given vertices.
     *
     * @param vertices Plane vertices.
     */
    public Plane(float[] vertices) {

        count = vertices.length / 8;

        setVertices(new Vertices(vertices));
    }

    /**
     * Draws the plane.
     */
    public void draw(){
        glDrawArrays(GL_TRIANGLES, 0, count);
    }

    /**
     * Sets light direction for the plane.
     *
     * @param light New light direction.
     */
    public void setLightDirection(Vector3D light) {
        int normalIndex = 0;
        for(int i = 0; i < getVertices().verticesArray.length; i += 8) {

            float dot = light.getDotProduct(
                    normalMap[normalIndex],
                    normalMap[normalIndex + 1],
                    normalMap[normalIndex + 2]
            ) * 10f / 2f;

            getVertices().verticesArray[i + 4] = 0.6f + dot;
            getVertices().verticesArray[i + 5] = 0.2f + (dot / 2f);
            getVertices().verticesArray[i + 6] = dot / 8f;

            normalIndex += 3;
        }
        getVertices().VERTICES_BUFFER.put(getVertices().verticesArray);
        getVertices().VERTICES_BUFFER.position(0);
    }

    /**
     * Sets normal map for the plane to help calculate light direction.
     *
     * @param normalMap New normal map.
     */
    public void setNormalMap(float[] normalMap) {
        this.normalMap = normalMap;
    }

    /**
     * Unused draw method.
     *
     * @param modelMatrix Unused.
     * @param viewProjectionMatrix Unused.
     * @param modelViewProjectionMatrix Unused.
     * @param colorProgram Unused.
     * @param wireFrameProgram Unused.
     * @param wX Unused.
     * @param wY Unused.
     * @param wZ Unused.
     * @param light Unused.
     */
    @Override
    public void draw(
            float[] modelMatrix,
            float[] viewProjectionMatrix,
            float[] modelViewProjectionMatrix,
            ColorProgram colorProgram,
            ColorProgram wireFrameProgram,
            float wX, float wY, float wZ, Vector3D light) {
    }

    /**
     * Stores wire color.
     */
    float[] wireColor = {1.0f, 0.45f, 0.01f, 1.0f};

    /**
     * Return wire color.
     *
     * @return Wire color.
     */
    public float[] getWireColor() {
        return wireColor;
    }
}
