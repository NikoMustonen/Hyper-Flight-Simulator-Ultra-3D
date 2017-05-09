package com.mustonen.niko.hyperflightsimulatorultra3d.opengl;

import android.support.annotation.Nullable;

import static android.opengl.GLES20.*;

/**
 * Class for opengl color programs.
 *
 * @author Niko Mustonen mustonen.niko@gmail.com
 * @version %I%, %G%
 * @since 1.8
 */
public class ColorProgram extends Program {

    /**
     * Color attribute memory location.
     */
    private final int aColorLocation;

    /**
     * Color uniform memory location.
     */
    private final int uColor;

    /**
     * Color uniform name from glsl file.
     */
    private final String U_COLOR = "u_Color";

    /**
     * Generates color program from given shader code.
     *
     * @param vertexCode
     * @param fragmentCode
     */
    public ColorProgram(String vertexCode, String fragmentCode) {
        super(vertexCode, fragmentCode);

        uColor = glGetUniformLocation(getProgram(), U_COLOR);
        aColorLocation = glGetAttribLocation(getProgram(), A_COLOR);
    }

    /**
     * Sets uniforms to shader program.
     *
     * @param matrix Matrix to be setted.
     * @param unused Unused.
     */
    @Override
    public void setUniforms(float[] matrix, int unused) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
    }

    /**
     * Sets color uniforms
     *
     * @param color Color uniforms.
     */
    public void setColorUniform(float[] color) {
        glUniform4fv(uColor, 1, color, 0);
    }

    /**
     * Returns color attribute locations.
     *
     * @return Colo0r attribute locations.
     */
    public int getColorAttributeLocation() {
        return aColorLocation;
    }
}
