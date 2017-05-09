package com.mustonen.niko.hyperflightsimulatorultra3d.object;

import com.mustonen.niko.hyperflightsimulatorultra3d.opengl.Program;
import com.mustonen.niko.hyperflightsimulatorultra3d.opengl.TextureProgram;

/**
 * Super class for textured 3D objects.
 *
 * @author Niko Mustonen mustonen.niko@gmail.com
 * @version %I%, %G%
 * @since 1.8
 */
public abstract class Textured3DObject extends Object3D {

    /**
     * Texture component count (x, y).
     */
    public static final int TEXTURE_COMPONENT_COUNT = 2;

    /**
     * Stride size for this component.
     */
    private static final int STRIDE =
            (VERTICES_COUNT + TEXTURE_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    /**
     * Binds program to this object.
     *
     * @param program Program to be binded.
     */
    @Override
    public void bind(Program program) {
        if (program instanceof TextureProgram) {
            TextureProgram textureProgram = (TextureProgram) program;
            vertices.setVertexAttributePointer(
                    0, textureProgram.getPositionAttributeLoacation(), VERTICES_COUNT, STRIDE);
            vertices.setVertexAttributePointer(
                    VERTICES_COUNT,
                    textureProgram.getTextureCoordinatesAttributeLocation(),
                    TEXTURE_COMPONENT_COUNT,
                    STRIDE);
        }
    }
}
