package com.mustonen.niko.hyperflightsimulatorultra3d.opengl;

import static android.opengl.GLES20.*;

public class TextureProgram extends Program {

    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    private final int uTextureUnitLocation;
    private final int aTextureCoordinatesLocation;

    public TextureProgram(String vertexCode, String fragmentCode) {
        super(vertexCode, fragmentCode);

        uTextureUnitLocation = glGetUniformLocation(getProgram(), U_TEXTURE_UNIT);
        aTextureCoordinatesLocation = glGetAttribLocation(getProgram(), A_TEXTURE_COORDINATES);
    }

    public void setUniforms(float[] matrix, int textureId) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);
        glUniform1i(uTextureUnitLocation, 0);
    }

    public int getTextureCoordinatesAttributeLocation() {
        return aTextureCoordinatesLocation;
    }
}
