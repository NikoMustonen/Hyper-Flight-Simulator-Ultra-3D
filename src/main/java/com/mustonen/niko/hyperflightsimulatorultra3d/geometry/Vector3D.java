package com.mustonen.niko.hyperflightsimulatorultra3d.geometry;

public class Vector3D {
    private final float[] TUPLE = {0, 0, 0, 1.0f};

    public Vector3D() {}

    public Vector3D(float[] newCoords) {
        setTuple(newCoords);
    }

    public final void setTuple(float[] newCoords) {
        this.TUPLE[0] = newCoords[0];
        this.TUPLE[1] = newCoords[1];
        this.TUPLE[2] = newCoords[2];
    }

    public final void setTuple(float x, float y ,float z) {
        this.TUPLE[0] = x;
        this.TUPLE[1] = y;
        this.TUPLE[2] = z;
    }

    public float[] getTuple() {
        return this.TUPLE;
    }

    public float getMagnitude() {
        return (float) Math.sqrt(getDotProduct());
    }

    public void normalize() {
        float mag = getMagnitude();
        TUPLE[0] /= mag;
        TUPLE[1] /= mag;
        TUPLE[2] /= mag;
    }

    public Vector3D getNormalizedVersion() {
        float mag = getMagnitude();
        float[] coords = new float[3];
        coords[0] = TUPLE[0] / mag;
        coords[1] = TUPLE[1] / mag;
        coords[2] = TUPLE[2] / mag;
        return new Vector3D(coords);
    }

    public float getDotProduct() {
        return TUPLE[0] * TUPLE[0] + TUPLE[1] * TUPLE[1] + TUPLE[2] * TUPLE[2];
    }

    public float getDotProduct(Vector3D v) {
        return TUPLE[0] * v.TUPLE[0] + TUPLE[1] * v.TUPLE[1] + TUPLE[2] * v.TUPLE[2];
    }

    public float getDotProduct(float[] tuple) {
        return TUPLE[0] * tuple[0] + TUPLE[1] * tuple[1] + TUPLE[2] * tuple[2];
    }

    public float getDotProduct(float x, float y, float z) {
        return TUPLE[0] * x + TUPLE[1] * y + TUPLE[2] * z;
    }

    public float getX() {
        return this.TUPLE[0];
    }

    public float getY() {
        return this.TUPLE[1];
    }

    public float getZ() {
        return this.TUPLE[2];
    }
}
