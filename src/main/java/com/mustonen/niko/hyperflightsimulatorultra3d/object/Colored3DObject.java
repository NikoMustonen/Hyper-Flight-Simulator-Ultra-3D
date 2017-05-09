package com.mustonen.niko.hyperflightsimulatorultra3d.object;

import com.mustonen.niko.hyperflightsimulatorultra3d.geometry.Vector3D;
import com.mustonen.niko.hyperflightsimulatorultra3d.opengl.ColorProgram;
import com.mustonen.niko.hyperflightsimulatorultra3d.opengl.Program;
import com.mustonen.niko.hyperflightsimulatorultra3d.opengl.TextureProgram;

/**
 * Super class for the colored 3D objects.
 *
 * @author Niko Mustonen mustonen.niko@gmail.com
 * @version %I%, %G%
 * @since 1.8
 */
public abstract class Colored3DObject extends Object3D {

    /**
     * Stores Vertex element components count.
     */
    public static final int COLOR_COMPONENT_COUNT = 4;

    /**
     * Stride size for colored 3D vertices.
     */
    public static final int STRIDE =
            (VERTICES_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    /**
     * Binds color program to object.
     *
     * @param program Program.
     */
    @Override
    public void bind(Program program) {
        if (program instanceof ColorProgram) {
            ColorProgram c = (ColorProgram) program;

            vertices.setVertexAttributePointer(0, c.getPositionAttributeLoacation(),
                    VERTICES_COUNT, STRIDE);
            vertices.setVertexAttributePointer(VERTICES_COUNT, c.getColorAttributeLocation(),
                    COLOR_COMPONENT_COUNT, STRIDE);
        }
    }

    /**
     * Draws the 3D object with given matrices.
     *
     * @param modelMatrix               Model matrix.
     * @param viewProjectionMatrix      View projection matrix.
     * @param modelViewProjectionMatrix Model view projection matrix.
     * @param colorProgram              Color program.
     * @param wireFrameProgram          Wire frame program if user wants wirteframe over opbject.
     * @param wX                        World x position.
     * @param wY                        World y position.
     * @param wZ                        World z position.
     * @param light                     Light direction.
     */
    public abstract void draw(
            float[] modelMatrix,
            float[] viewProjectionMatrix,
            float[] modelViewProjectionMatrix,
            ColorProgram colorProgram,
            ColorProgram wireFrameProgram,
            float wX, float wY, float wZ, Vector3D light
    );
}
