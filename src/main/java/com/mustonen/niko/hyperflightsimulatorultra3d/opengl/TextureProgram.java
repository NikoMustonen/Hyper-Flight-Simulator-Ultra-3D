package com.mustonen.niko.hyperflightsimulatorultra3d.opengl;

import static android.opengl.GLES20.*;

/**
 * Class for opengl texture programs.
 *
 * @author Niko Mustonen mustonen.niko@gmail.com
 * @version %I%, %G%
 * @since 1.8
 */
public class TextureProgram extends Program {

    /**
     * Stores uniform name from the glsl file.
     */
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";

    /**
     * Stores attribute name from the glsl file.
     */
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    /**
     * Texture memory location.
     */
    private final int uTextureUnitLocation;

    /**
     * Coordinates memory location.
     */
    private final int aTextureCoordinatesLocation;

    /**
     * Generates texture program.
     *
     * @param vertexCode   Vertex code in string format.
     * @param fragmentCode Fragment code in string format.
     */
    public TextureProgram(String vertexCode, String fragmentCode) {
        super(vertexCode, fragmentCode);

        uTextureUnitLocation = glGetUniformLocation(getProgram(), U_TEXTURE_UNIT);
        aTextureCoordinatesLocation = glGetAttribLocation(getProgram(), A_TEXTURE_COORDINATES);
    }

    /**
     * Sets uniforms to shader program.
     *
     * @param matrix    Matrix to be setted.
     * @param textureId Textures id.
     */
    public void setUniforms(float[] matrix, int textureId) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);
        glUniform1i(uTextureUnitLocation, 0);
    }

    /**
     * Returns texture coordinates attribute location.
     *
     * @return Memory location.
     */
    public int getTextureCoordinatesAttributeLocation() {
        return aTextureCoordinatesLocation;
    }
}
