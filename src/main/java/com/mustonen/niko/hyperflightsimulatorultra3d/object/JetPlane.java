package com.mustonen.niko.hyperflightsimulatorultra3d.object;

import java.nio.*;

import static android.opengl.GLES20.*;

public class JetPlane extends Colored3DObject {

    public JetPlane() {
        setVertices(new Vertices(getPlaneVertices()));
    }

    public void draw(){
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 18);
        glDrawArrays(GL_TRIANGLE_FAN, 18, 10);
        glDrawArrays(GL_TRIANGLE_STRIP, 28, 18);
        glDrawArrays(GL_TRIANGLE_FAN, 46, 10);
        glDrawArrays(GL_TRIANGLE_STRIP, 56, 4);
        glDrawArrays(GL_TRIANGLE_STRIP, 60, 4);
    }

    private float[] getPlaneVertices() {

        return new float[] {
                0f, .12f, 0.65f, 1f, 0f, 0.4f, 0.2f, 1.0f,
                0f, .12f, -0.85f, 1f, 0f, 0.4f, 0.2f, 1.0f,
                -.08f, .08f, 0.65f, 1f, 0f, 0.2f, 0.1f, 1.0f,
                -.08f, .08f, -0.85f, 1f, 0f, 0.2f, 0.1f, 1.0f,
                -.12f, .00f, 0.65f, 1f, 0f, 0.1f, 0.1f, 1.0f,
                -.12f, .00f, -0.85f, 1f, 0f, 0.1f, 0.1f, 1.0f,
                -.08f, -.08f, 0.65f, 1f, 0f, 0.2f, 0.1f, 1.0f,
                -.08f, -.08f, -0.85f, 1f, 0f, 0.2f, 0.1f, 1.0f,
                -.00f, -.12f, 0.65f, 1f, 0f, 0.4f, 0.2f, 1.0f,
                -.00f, -.12f, -0.85f, 1f, 0f, 0.4f, 0.2f, 1.0f,
                .08f, -.08f, 0.65f, 1f, 0f, 0.6f, 0.3f, 1.0f,
                .08f, -.08f, -0.85f, 1f, 0f, 0.6f, 0.3f, 1.0f,
                .12f, .00f, 0.65f, 1f, 0f, 0.8f, 0.4f, 1.0f,
                .12f, .00f, -0.85f, 1f, 0f, 0.8f, 0.4f, 1.0f,
                .08f, .08f, 0.65f, 1f, 0f, 0.6f, 0.3f, 1.0f,
                .08f, .08f, -0.85f, 1f, 0f, 0.6f, 0.3f, 1.0f,
                .00f, .12f, 0.65f, 1f, 0f, 0.4f, 0.3f, 1.0f,
                .00f, .12f, -0.85f, 1f, 0f, 0.4f, 0.3f, 1.0f,

                0f, 0f, 1f, 1f, 0.5f, 0.2f, 0.2f, 1.0f,
                0f, .12f, 0.65f, 1f, 0f, 0.4f, 0.2f, 1.0f,
                -.08f, .08f, 0.65f, 1f, 0f, 0.2f, 0.1f, 1.0f,
                -.12f, .0f, 0.65f, 1f, 0f, 0.1f, 0.1f, 1.0f,
                -.08f, -.08f, 0.65f, 1f, 0f, 0.2f, 0.1f, 1.0f,
                .0f, -.12f, 0.65f, 1f, 0f, 0.4f, 0.3f, 1.0f,
                .08f, -.08f, 0.65f, 1f, 0f, 0.6f, 0.3f, 1.0f,
                .12f, .0f, 0.65f, 1f, 0f, 0.8f, 0.4f, 1.0f,
                .08f, .08f, 0.65f, 1f, 0f, 0.6f, 0.3f, 1.0f,
                0f, .12f, 0.65f, 1f, 0f, 0.4f, 0.3f, 1.0f,

                0f, .12f, -0.85f, 1f, 0f, 0.4f, 0.2f, 1.0f,
                0f, .07f, -1f, 1f, 0f, 0.4f, 0.2f, 1.0f,
                -.08f, .08f, -0.85f, 1f, 0f, 0.2f, 0.1f, 1.0f,
                -.05f, .05f, -1f, 1f, 0f, 0.2f, 0.1f, 1.0f,
                -.12f, .00f, -0.85f, 1f, 0f, 0.1f, 0.1f, 1.0f,
                -.07f, .00f, -1f, 1f, 0f, 0.1f, 0.1f, 1.0f,
                -.08f, -.08f, -0.85f, 1f, 0f, 0.2f, 0.1f, 1.0f,
                -.05f, -.05f, -1f, 1f, 0f, 0.2f, 0.1f, 1.0f,
                -.00f, -.12f, -0.85f, 1f, 0f, 0.4f, 0.2f, 1.0f,
                -.00f, -.07f, -1f, 1f, 0f, 0.4f, 0.2f, 1.0f,
                .08f, -.08f, -0.85f, 1f, 0f, 0.6f, 0.3f, 1.0f,
                .05f, -.05f, -1f, 1f, 0f, 0.6f, 0.3f, 1.0f,
                .12f, .00f, -0.85f, 1f, 0f, 0.8f, 0.4f, 1.0f,
                .07f, .00f, -1f, 1f, 0f, 0.7f, 0.3f, 1.0f,
                .08f, .08f, -0.85f, 1f, 0f, 0.6f, 0.3f, 1.0f,
                .05f, .05f, -1f, 1f, 0f, 0.6f, 0.3f, 1.0f,
                .00f, .12f, -0.85f, 1f, 0f, 0.4f, 0.3f, 1.0f,
                .00f, .07f, -1f, 1f, 0f, 0.4f, 0.3f, 1.0f,

                0f, 0f, -0.85f, 1f, 1f, 1f, 0.0f, 1.0f,
                0f, .07f, -1f, 1f, 1f, 0.0f, 0.0f, 1.0f,
                -.05f, .05f, -1f, 1f, 1f, 0.0f, 0.0f, 1.0f,
                -.07f, .00f, -1f, 1f, 1f, 0.0f, 0.0f, 1.0f,
                -.05f, -.05f, -1f, 1f, 1f, 0.0f, 0.0f, 1.0f,
                -.00f, -.07f, -1f, 1f, 1f, 0.0f, 0.0f, 1.0f,
                .05f, -.05f, -1f, 1f, 1f, 0.0f, 0.0f, 1.0f,
                .07f, .00f, -1f, 1f, 1f, 0.0f, 0.0f, 1.0f,
                .05f, .05f, -1f, 1f, 1f, 0.0f, 0.0f, 1.0f,
                .00f, .07f, -1f, 1f, 1f, 0.00f, 0.0f, 1.0f,

                .00f, .00f, -0.2f, 1f, 0f, 0.5f, 0.3f, 1.0f,
                -1f, .00f, -0.1f, 1f, 1f, 0.00f, 0.0f, 1.0f,
                .00f, .00f, +0.3f, 1f, 1f, 0.5f, 0.3f, 1.0f,
                -.7f, .00f, +0.4f, 1f, 1f, 0.00f, 0.0f, 1.0f,

                .00f, .00f, -0.2f, 1f, 0f, 0.5f, 0.3f, 1.0f,
                1f, .00f, -0.1f, 1f, 1f, 0.00f, 0.0f, 1.0f,
                .00f, .00f, +0.3f, 1f, 1f, 0.5f, 0.3f, 1.0f,
                .7f, .00f, +0.4f, 1f, 1f, 0.00f, 0.0f, 1.0f
        };
    }
}
