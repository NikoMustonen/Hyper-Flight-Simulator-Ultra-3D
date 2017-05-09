package com.mustonen.niko.hyperflightsimulatorultra3d.opengl;

import android.content.Context;

import com.mustonen.niko.hyperflightsimulatorultra3d.util.ShaderUtil;

import static android.opengl.GLES20.*;

/**
 * Super class opengl programs.
 *
 * @author Niko Mustonen mustonen.niko@gmail.com
 * @version %I%, %G%
 * @since 1.8
 */
public abstract class Program {

    /**
     * Matrix uniform variable name in shader program.
     */
    protected static final String U_MATRIX = "u_Matrix";

    /**
     * Position attribute variable name in shader program.
     */
    protected static final String A_POSITION = "a_Position";

    /**
     * Color attribute variable name in shader program.
     */
    protected static final String A_COLOR = "a_Color";

    /**
     * Matrix uniform memory location.
     */
    protected final int uMatrixLocation;

    /**
     * Position attribute memory location.
     */
    protected final int aPositionLocation;

    /**
     * Programs memory location.
     */
    protected final int PROGRAM;

    /**
     * Generates program from given shaders.
     *
     * @param vertexCode   Vertex code.
     * @param fragmentCode Fragment code.
     */
    protected Program(String vertexCode, String fragmentCode) {

        this.PROGRAM = ShaderUtil.buildProgram(vertexCode, fragmentCode);
        uMatrixLocation = glGetUniformLocation(this.PROGRAM, U_MATRIX);
        aPositionLocation = glGetAttribLocation(this.PROGRAM, A_POSITION);
    }

    /**
     * Return generated program.
     *
     * @return Programs memory loaction.
     */
    public int getProgram() {
        return this.PROGRAM;
    }

    /**
     * Returns position attribute.
     *
     * @return Position attribute.
     */
    public int getPositionAttributeLoacation() {
        return aPositionLocation;
    }

    /**
     * Sets uniforms to shader program.
     *
     * @param matrix    Matrix to be setted.
     * @param textureId Textures id.
     */
    protected abstract void setUniforms(float[] matrix, int textureId);
}
