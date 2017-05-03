package com.mustonen.niko.hyperflightsimulatorultra3d.object;

import com.mustonen.niko.hyperflightsimulatorultra3d.geometry.Vector3D;
import com.mustonen.niko.hyperflightsimulatorultra3d.opengl.ColorProgram;

import static android.opengl.GLES20.*;

public class Plane extends Colored3DObject {

    private int count;
    private float[] normalMap;

    public Plane(float[] vertices) {

        count = vertices.length / 8;

        setVertices(new Vertices(vertices));
    }

    public void draw(){
        glDrawArrays(GL_TRIANGLES, 0, count);
    }

    public void setLightDirection(Vector3D light) {
        int normalIndex = 0;
        for(int i = 0; i < getVertices().verticesArray.length; i += 8) {

            float dot = light.getDotProduct(
                    normalMap[normalIndex],
                    normalMap[normalIndex + 1],
                    normalMap[normalIndex + 2]
            ) * 10f / 2f;

            getVertices().verticesArray[i + 4] = 0.5f + dot;
            getVertices().verticesArray[i + 5] = .1f;
            getVertices().verticesArray[i + 6] = 0.25f + -dot/2f;

            normalIndex += 3;
        }
        getVertices().VERTICES_BUFFER.put(getVertices().verticesArray);
        getVertices().VERTICES_BUFFER.position(0);
    }

    public void setNormalMap(float[] normalMap) {
        this.normalMap = normalMap;
    }

    @Override
    public void draw(
            float[] modelMatrix,
            float[] viewProjectionMatrix,
            float[] modelViewProjectionMatrix,
            ColorProgram colorProgram, float wX, float wY, float wZ, Vector3D light) {


    }
}
