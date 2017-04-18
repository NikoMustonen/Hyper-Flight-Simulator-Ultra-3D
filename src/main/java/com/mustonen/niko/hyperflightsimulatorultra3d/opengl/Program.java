package com.mustonen.niko.hyperflightsimulatorultra3d.opengl;

import android.content.Context;
import com.mustonen.niko.hyperflightsimulatorultra3d.util.ShaderUtil;

import static android.opengl.GLES20.*;

public abstract class Program {

    protected static final String U_MATRIX = "u_Matrix";
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";

    protected final int uMatrixLocation;
    protected final int aPositionLocation;

    protected final int PROGRAM;

    protected Program(String vertexCode, String fragmentCode) {

        this.PROGRAM = ShaderUtil.buildProgram(vertexCode, fragmentCode);
        uMatrixLocation = glGetUniformLocation(this.PROGRAM, U_MATRIX);
        aPositionLocation = glGetAttribLocation(this.PROGRAM, A_POSITION);
    }

    public int getProgram() {
        return this.PROGRAM;
    }

    public int getPositionAttributeLoacation() {
        return aPositionLocation;
    }

    protected abstract void setUniforms(float[] matrix, int textureId);
}
