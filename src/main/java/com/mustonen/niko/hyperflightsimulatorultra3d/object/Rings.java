package com.mustonen.niko.hyperflightsimulatorultra3d.object;

import com.mustonen.niko.hyperflightsimulatorultra3d.geometry.Vector3D;
import com.mustonen.niko.hyperflightsimulatorultra3d.opengl.ColorProgram;
import com.mustonen.niko.hyperflightsimulatorultra3d.util.Debug;

import static android.opengl.GLES20.glUseProgram;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * Class for storing and handling all the collectable rings in the game.
 *
 * @author Niko Mustonen mustonen.niko@gmail.com
 * @version %I%, %G%
 * @since 1.8
 */
public class Rings {

    /**
     * Stores all the rings as an array.
     */
    private Ring[] rings;

    /**
     * Games terrain.
     */
    private Terrain terrain;

    /**
     * Generates 20 rings with random positions.
     *
     * @param ringVertices Ring vertices.
     * @param normalMap    Normal map for ring to define light direction.
     * @param terrain      Terrain object for defining position.
     */
    public Rings(float[] ringVertices, float[] normalMap, Terrain terrain) {
        this.terrain = terrain;
        rings = new Ring[20];

        Vector3D lightDirection = new Vector3D(new float[]{0, 1, 0}).getNormalizedVersion();

        for (int i = 0; i < rings.length; i++) {
            rings[i] = new Ring(ringVertices, lightDirection, normalMap);
            rings[i].setNormalMap(normalMap);
        }

        for (Ring r : rings) {
            r.setPosition(terrain.getRandomPosition(rings));
        }
    }

    /**
     * Stores ring rotation.
     */
    private int rotation = 0;

    /**
     * Draws rings.
     *
     * @param modelMatrix               Model matrix for defining drawing position.
     * @param viewProjectionMatrix      Multiplied projection view matrix.
     * @param modelViewProjectionMatrix Multiplied model view proijection matrix.
     * @param colorProgram              Color program.
     * @param wireFrameProgram          Unused in this class.
     * @param wX                        World x position.
     * @param wY                        World y position.
     * @param wZ                        World z position.
     * @param lightDirection            Light direction.
     */
    public void draw(float[] modelMatrix,
                     float[] viewProjectionMatrix,
                     float[] modelViewProjectionMatrix,
                     ColorProgram colorProgram,
                     ColorProgram wireFrameProgram,
                     float wX, float wY, float wZ,
                     Vector3D lightDirection) {

        glUseProgram(colorProgram.getProgram());

        for (Ring r : rings) {
            setIdentityM(modelMatrix, 0);
            translateM(modelMatrix, 0, -wX + r.x, -wY + r.y, -wZ - r.z);
            rotateM(modelMatrix, 0, rotation, 0, 1, 0);
            multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix,
                    0, modelMatrix, 0);

            colorProgram.setUniforms(modelViewProjectionMatrix, 0);
            r.bind(colorProgram);
            r.draw();
        }
        rotation += 3;
    }

    /**
     * Checks collision between plane and all the rings.
     *
     * @param wX World x position.
     * @param wY World y position.
     * @param wZ World z position.
     * @return True if collision happened.
     */
    public boolean isCollision(float wX, float wY, float wZ) {
        for (Ring r : rings) {
            if (r.isCollision(wX, wY, wZ)) {
                r.setPosition(terrain.getRandomPosition(rings));
                return true;
            }
        }
        return false;
    }
}
