package com.mustonen.niko.hyperflightsimulatorultra3d.object;

import com.mustonen.niko.hyperflightsimulatorultra3d.opengl.Program;
import com.mustonen.niko.hyperflightsimulatorultra3d.util.Debug;

/**
 * Super class for all 3D objects.
 *
 * @author Niko Mustonen mustonen.niko@gmail.com
 * @version %I%, %G%
 * @since 1.8
 */
public abstract class Object3D {

    /**
     * Stores Vertex element components count.
     */
    public static final int VERTICES_COUNT = 4;

    /**
     * Stores the amount of bytes per float.
     */
    public static final int BYTES_PER_FLOAT = 4;

    /**
     * Stores Vertices element held by this object.
     */
    protected Vertices vertices;

    /**
     * Draws the object.
     */
    public abstract void draw();

    /**
     * Returns vertices from this object.
     *
     * @return Vertices element.
     */
    public final Vertices getVertices() {
        return vertices;
    }

    /**
     * Sets vertices for this object.
     *
     * @param vertices New vertices.
     */
    public final void setVertices(Vertices vertices) {
        this.vertices = vertices;
    }

    /**
     * Bind program to this object.
     *
     * @param program Program.
     */
    protected abstract void bind(Program program);
}
