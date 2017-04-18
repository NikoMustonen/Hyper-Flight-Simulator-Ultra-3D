package com.mustonen.niko.hyperflightsimulatorultra3d.object;

import com.mustonen.niko.hyperflightsimulatorultra3d.opengl.ColorProgram;
import com.mustonen.niko.hyperflightsimulatorultra3d.opengl.Program;

public abstract class Colored3DObject extends Object3D {

    public static final int COLOR_COMPONENT_COUNT = 4;
    private static final int STRIDE =
            (VERTICES_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    @Override
    public void bind(Program program) {
        if(program instanceof ColorProgram) {
            ColorProgram c = (ColorProgram) program;

            vertices.setVertexAttributePointer(0, c.getPositionAttributeLoacation(),
                    VERTICES_COUNT, STRIDE);
            vertices.setVertexAttributePointer(VERTICES_COUNT, c.getColorAttributeLocation(),
                    COLOR_COMPONENT_COUNT, STRIDE);
        }
    }
}
