package com.mustonen.niko.hyperflightsimulatorultra3d.object;

import com.mustonen.niko.hyperflightsimulatorultra3d.opengl.Program;

public abstract class Object3D {

    public static final int VERTICES_COUNT = 4;
    public static final int BYTES_PER_FLOAT = 4;

    protected Vertices vertices;

    public abstract void draw();

    public final Vertices getVertices() {
        return vertices;
    }

    public final void setVertices(Vertices vertices) {
        this.vertices = vertices;
    }

    protected abstract void bind(Program program);
}
