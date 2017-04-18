package com.mustonen.niko.hyperflightsimulatorultra3d.object;

import java.nio.*;

import static android.opengl.GLES20.*;

public class Vertices {

    //Static class variables
    public static final int VERTICES_COUNT = 4;
    public static final int BYTES_PER_FLOAT = 4;
    public static final int COLOR_COMPONENT_COUNT = 4;
    public static final int TEXTURE_COMPONENT_COUNT = 2;

    public static final int STRIDE_WITHOUT_TEXTURE =
            (VERTICES_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    public static final int STRIDE_WITH_TEXTURE =
            (VERTICES_COUNT + TEXTURE_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private final FloatBuffer VERTICES_BUFFER;
    
    public Vertices(float[] verticesArray) {
        VERTICES_BUFFER = ByteBuffer
                .allocateDirect(verticesArray.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(verticesArray);
    }
    public void setVertexAttributePointer(int offset, int attributeLocation,
                                       int componentAmount, int stride) {
        VERTICES_BUFFER.position(offset);
        glVertexAttribPointer(attributeLocation, componentAmount, GL_FLOAT,
                false, stride, VERTICES_BUFFER);
        glEnableVertexAttribArray(attributeLocation);
        VERTICES_BUFFER.position(0);
    }
}
