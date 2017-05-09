package com.mustonen.niko.hyperflightsimulatorultra3d.geometry;

/**
 * Geometry class for calculating vectors..
 *
 * @author Niko Mustonen mustonen.niko@gmail.com
 * @version %I%, %G%
 * @since 1.8
 */
public class Vector3D {
    /**
     * Stores vector direction coordinates.
     */
    private final float[] TUPLE = {0, 0, 0, 1.0f};

    /**
     * Generatres default vector with direction 0 ,0, 0;
     */
    public Vector3D() {}

    /**
     * Generates vector with given direction.
     *
     * @param newCoords New direction.
     */
    public Vector3D(float[] newCoords) {
        setTuple(newCoords);
    }

    public final void setTuple(float[] newCoords) {
        this.TUPLE[0] = newCoords[0];
        this.TUPLE[1] = newCoords[1];
        this.TUPLE[2] = newCoords[2];
    }

    /**
     * Sets new direction to vector.
     *
     * @param x X direction.
     * @param y y direction.
     * @param z Z direction.
     */
    public final void setTuple(float x, float y ,float z) {
        this.TUPLE[0] = x;
        this.TUPLE[1] = y;
        this.TUPLE[2] = z;
    }

    /**
     * Returns vectors direction.
     *
     * @return Vector direction.
     */
    public float[] getTuple() {
        return this.TUPLE;
    }

    /**
     * Returns vectors magnitude.
     *
     * @return Magnitude.
     */
    public float getMagnitude() {
        return (float) Math.sqrt(getDotProduct());
    }

    /**
     * Normalizes vector.
     */
    public void normalize() {
        float mag = getMagnitude();
        TUPLE[0] /= mag;
        TUPLE[1] /= mag;
        TUPLE[2] /= mag;
    }

    /**
     * Returns new normalized intance of this vectror.
     *
     * @return Normalized version of current vector.
     */
    public Vector3D getNormalizedVersion() {
        float mag = getMagnitude();
        float[] coords = new float[3];
        coords[0] = TUPLE[0] / mag;
        coords[1] = TUPLE[1] / mag;
        coords[2] = TUPLE[2] / mag;
        return new Vector3D(coords);
    }

    /**
     * Calculates dot product by itself.
     *
     * @return Dot product.
     */
    public float getDotProduct() {
        return TUPLE[0] * TUPLE[0] + TUPLE[1] * TUPLE[1] + TUPLE[2] * TUPLE[2];
    }

    /**
     * Calculates dotproduct between other vector.
     *
     * @param v Other vector.
     * @return Dot product.
     */
    public float getDotProduct(Vector3D v) {
        return TUPLE[0] * v.TUPLE[0] + TUPLE[1] * v.TUPLE[1] + TUPLE[2] * v.TUPLE[2];
    }

    /**
     * Calculates dot product between this vector and given coordinates.
     *
     * @param tuple Given coorniates.
     * @return Dotproduct.
     */
    public float getDotProduct(float[] tuple) {
        return TUPLE[0] * tuple[0] + TUPLE[1] * tuple[1] + TUPLE[2] * tuple[2];
    }

    /**
     * Calculates dot product between this vector and given coordinates.
     *
     * @param x X position.
     * @param y Y position.
     * @param z Z position.
     * @return
     */
    public float getDotProduct(float x, float y, float z) {
        return TUPLE[0] * x + TUPLE[1] * y + TUPLE[2] * z;
    }
}
