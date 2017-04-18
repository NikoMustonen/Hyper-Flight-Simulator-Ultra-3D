package com.mustonen.niko.hyperflightsimulatorultra3d.opengl;

import android.support.annotation.Nullable;

import static android.opengl.GLES20.*;

public class ColorProgram extends Program {

    private final int aColorLocation;

    public ColorProgram(String vertexCode, String fragmentCode) {
        super(vertexCode, fragmentCode);

        aColorLocation = glGetAttribLocation(getProgram(), A_COLOR);
    }

    @Override
    public void setUniforms(float[] matrix, int unused) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
    }

    public int getColorAttributeLocation() {
        return aColorLocation;
    }
}
