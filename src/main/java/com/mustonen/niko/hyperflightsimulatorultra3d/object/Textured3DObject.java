package com.mustonen.niko.hyperflightsimulatorultra3d.object;

import com.mustonen.niko.hyperflightsimulatorultra3d.opengl.Program;
import com.mustonen.niko.hyperflightsimulatorultra3d.opengl.TextureProgram;

public abstract class Textured3DObject extends Object3D {

    public static final int TEXTURE_COMPONENT_COUNT = 2;

    private static final int STRIDE =
            (VERTICES_COUNT + TEXTURE_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    @Override
    public void bind(Program program) {
        if(program instanceof TextureProgram) {
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
