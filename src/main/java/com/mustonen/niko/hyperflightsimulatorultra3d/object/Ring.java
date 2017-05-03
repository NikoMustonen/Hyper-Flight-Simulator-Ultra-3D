package com.mustonen.niko.hyperflightsimulatorultra3d.object;

import com.mustonen.niko.hyperflightsimulatorultra3d.geometry.Vector3D;
import com.mustonen.niko.hyperflightsimulatorultra3d.opengl.ColorProgram;

import static android.opengl.GLES20.*;
import static android.opengl.Matrix.*;

public class Ring extends Colored3DObject {

    private int rotation = 0;
    private float x, y, z;
    private int count;
    private float[] normalMap;

    public Ring(float[] vertices) {
        count = vertices.length / 8;
        setVertices(new Vertices(vertices));
    }

    public void setPosition(float[] coords) {
        this.x = coords[0];
        this.y = coords[1];
        this.z = coords[2];
    }

    Vector3D lightDirection = new Vector3D(new float[]{0, 1, 0}).getNormalizedVersion();
    boolean lightDirectionIsNotSet = true;

    @Override
    public void draw(
            float[] modelMatrix,
            float[] viewProjectionMatrix,
            float[] modelViewProjectionMatrix,
            ColorProgram colorProgram, float wX, float wY, float wZ, Vector3D light) {

        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, -wX + x, -wY + y , -wZ - z);
        rotateM(modelMatrix, 0, getRotation(), 0, 1, 0);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix,
                0, modelMatrix, 0);
        colorProgram.setUniforms(modelViewProjectionMatrix, 0);
        bind(colorProgram);
        rotation += 3;
        if(lightDirectionIsNotSet) {
            setLightDirection();
            lightDirectionIsNotSet = false;
        }
        glDrawArrays(GL_TRIANGLES, 0, count);
    }

    private void setLightDirection() {
        int normalIndex = 0;
        for(int i = 0; i < getVertices().verticesArray.length; i += 8) {

            float dot = lightDirection.getDotProduct(
                    normalMap[normalIndex],
                    normalMap[normalIndex + 1],
                    normalMap[normalIndex + 2]
            ) * 10f / 2f;

            getVertices().verticesArray[i + 4] = 0.5f + dot;
            getVertices().verticesArray[i + 5] = .5f;
            getVertices().verticesArray[i + 6] = 0.125f + -dot/4f;

            normalIndex += 3;
        }
        getVertices().VERTICES_BUFFER.put(getVertices().verticesArray);
        getVertices().VERTICES_BUFFER.position(0);
        lightDirectionIsNotSet = false;
    }

    public boolean isCollision(float wX, float wY, float wZ) {

        return Math.abs(-wX + x) <= 2f && Math.abs(-wY + y) <= 2f && Math.abs(+wZ + z) <= 2f;
    }

    public int getRotation() {
        return this.rotation;
    }

    @Override
    public void draw() {
        glDrawArrays(GL_TRIANGLES, 0, count);
    }

    public void setNormalMap(float[] normalMap) {
        this.normalMap = normalMap;
    }
}
