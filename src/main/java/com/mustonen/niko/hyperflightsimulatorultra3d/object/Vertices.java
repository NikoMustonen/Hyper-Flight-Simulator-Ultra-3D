package com.mustonen.niko.hyperflightsimulatorultra3d.object;

import java.nio.*;

import static android.opengl.GLES20.*;

/**
 * Class for storing and handling object vertices..
 *
 * @author Niko Mustonen mustonen.niko@gmail.com
 * @version %I%, %G%
 * @since 1.8
 */
public class Vertices {

    /**
     * Vertices elements couint.
     */
    public static final int VERTICES_COUNT = 4;

    /**
     * Bytes per float.
     */
    public static final int BYTES_PER_FLOAT = 4;

    /**
     * Defines the amount of color components in vertex.
     */
    public static final int COLOR_COMPONENT_COUNT = 4;

    /**
     * Defines texture component count.
     */
    public static final int TEXTURE_COMPONENT_COUNT = 2;

    /**
     * Stores floatbuffer for vertices that are send to graphics card.
     */
    public final FloatBuffer VERTICES_BUFFER;

    /**
     * Original vertices array.
     */
    public float[] verticesArray;

    /**
     * Generates floatbuffer which wiill be given to graphics card.
     *
     * @param verticesArray Original vertices array.
     */
    public Vertices(float[] verticesArray) {
        this.verticesArray = verticesArray;
        VERTICES_BUFFER = ByteBuffer
                .allocateDirect(verticesArray.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(verticesArray);
    }

    /**
     * Sets vertex attribute pointer.
     *
     * @param offset            Array offset.
     * @param attributeLocation Attribute memory location.
     * @param componentAmount   Amount of components.
     * @param stride            Stride size.
     */
    public void setVertexAttributePointer(int offset, int attributeLocation,
                                          int componentAmount, int stride) {
        VERTICES_BUFFER.position(offset);
        glVertexAttribPointer(attributeLocation, componentAmount, GL_FLOAT,
                false, stride, VERTICES_BUFFER);
        glEnableVertexAttribArray(attributeLocation);
        VERTICES_BUFFER.position(0);
    }
}
