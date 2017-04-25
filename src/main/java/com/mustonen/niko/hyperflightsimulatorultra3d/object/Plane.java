package com.mustonen.niko.hyperflightsimulatorultra3d.object;

import java.nio.*;

import static android.opengl.GLES20.*;

public class Plane extends Colored3DObject {

    private int count;

    public Plane(float[] vertices) {

        count = vertices.length / 8;

        setVertices(new Vertices(vertices));
    }

    public void draw(){
        glDrawArrays(GL_TRIANGLES, 0, count);
    }
}
