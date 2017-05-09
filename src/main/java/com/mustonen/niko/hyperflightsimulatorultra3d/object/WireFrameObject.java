package com.mustonen.niko.hyperflightsimulatorultra3d.object;

import com.mustonen.niko.hyperflightsimulatorultra3d.opengl.ColorProgram;
import com.mustonen.niko.hyperflightsimulatorultra3d.opengl.Program;

import static android.opengl.GLES20.*;

/**
 * Super class for wire frame object.
 *
 * @author Niko Mustonen mustonen.niko@gmail.com
 * @version %I%, %G%
 * @since 1.8
 */
public class WireFrameObject extends Object3D {

    /**
     * Texture component count.
     */
    public static final int TEXTURE_COMPONENT_COUNT = 2;

    /**
     * Stride size.
     */
    private static final int STRIDE =
            (VERTICES_COUNT + TEXTURE_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    /**
     * Vertices count.
     */
    int count;

    /**
     * Generates wireframe object.
     *
     * @param vertices Object vertices.
     */
    public WireFrameObject(float[] vertices) {
        setVertices(new Vertices(vertices));
        this.count = vertices.length / 6;
    }

    /**
     * Draws wire frame object.
     */
    @Override
    public void draw() {
        glDrawArrays(GL_LINES, 0, count);
    }

    /**
     * Binds program to this object.
     *
     * @param program Program to be binded.
     */
    @Override
    public void bind(Program program) {
        ColorProgram wireFrameProgram = (ColorProgram) program;
        vertices.setVertexAttributePointer(
                0, wireFrameProgram.getPositionAttributeLoacation(), VERTICES_COUNT, STRIDE);
    }

    /**
     * Convert given vertices arrays color component to wire frame componments.
     *
     * @param colorCoords
     * @return
     */
    public static final float[] convertColorVerticesToWireFrameVertices(float[] colorCoords) {
        int size = colorCoords.length / 8 * 6;
        float[] wireArray = new float[size];
        int wI = 0;

        for (int i = 0; i < colorCoords.length; i += 8) {
            wireArray[wI] = colorCoords[i];
            wireArray[wI + 1] = colorCoords[i + 1];
            wireArray[wI + 2] = colorCoords[i + 2];
            wireArray[wI + 3] = 1f;
            wireArray[wI + 4] = colorCoords[i + 1];
            wireArray[wI + 5] = colorCoords[i + 2];

            wI += 6;
        }

        return wireArray;
    }
}
