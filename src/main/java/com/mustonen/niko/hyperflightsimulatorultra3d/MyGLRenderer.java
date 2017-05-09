package com.mustonen.niko.hyperflightsimulatorultra3d;

import android.opengl.GLSurfaceView;

import com.mustonen.niko.hyperflightsimulatorultra3d.object.Rings;
import com.mustonen.niko.hyperflightsimulatorultra3d.object.Plane;
import com.mustonen.niko.hyperflightsimulatorultra3d.object.Terrain;
import com.mustonen.niko.hyperflightsimulatorultra3d.geometry.Vector3D;
import com.mustonen.niko.hyperflightsimulatorultra3d.opengl.ColorProgram;
import com.mustonen.niko.hyperflightsimulatorultra3d.opengl.TextureProgram;
import com.mustonen.niko.hyperflightsimulatorultra3d.object.WireFrameObject;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;
import static android.opengl.Matrix.*;

/**
 * Generates graphics engine for the game..
 *
 * @author Niko Mustonen mustonen.niko@gmail.com
 * @version %I%, %G%
 * @since 1.8
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    /**
     * Stores GameActivity.
     */
    private GameActivity parent;

    /**
     * OpenGl wireframe programs memory location in device.
     */
    private ColorProgram wireFrameProgram;

    /**
     * OpenGl texture programs memory location in device.
     */
    private TextureProgram textureProgram;

    /**
     * OpenGl color programs memory location in device.
     */
    private ColorProgram colorProgram;

    /**
     * Plane game object.
     */
    private Plane plane;

    /**
     * Stores all the collectable rings.
     */
    private Rings rings;

    /**
     * Stores games terrain model.
     */
    private Terrain terrain;

    /**
     * Wireframe object for the terrain model.
     */
    private WireFrameObject wireFrameObjectTerrain;

    /**
     * Wireframe object for the plane model.
     */
    private WireFrameObject wireFrameObjectPlane;

    /**
     * Stores 3D projection matrix.
     */
    private final float[] projectionMatrix = new float[16];

    /**
     * Stores 3D model matrix.
     */
    private final float[] modelMatrix = new float[16];

    /**
     * Stores 3D view matrix.
     */
    private final float[] viewMatrix = new float[16];

    /**
     * Stores combination multiplied matrix of view and projection.
     */
    private final float[] viewProjectionMatrix = new float[16];

    /**
     * Stores combination multiplied matrix of view and projection and model.
     */
    private final float[] modelViewProjectionMatrix = new float[16];

    /**
     * Stores GLSL shader code.
     */
    public static String VERTEX_SHADER_CODE, FRAGMENT_SHADER_CODE,
            TEXTURE_VERTEX_SHADER_CODE, TEXTURE_FRAGMENT_SHADER_CODE,
            WIRE_FRAME_FRAGMENT_SHADER_CODE, WIRE_FRAME_VERTEX_SHADER_CODE;

    /**
     * Creates renderer object with loads of 3d models inside it.
     *
     * @param parent                 Activity context.
     * @param vsc                    Color programs vertex shader code.
     * @param fsc                    Color program fragment shader code.
     * @param tvsc                   Texture programs vertex shader code.
     * @param tfsc                   Texture program fragment shader code.
     * @param wfvsc                  Wire frame programs vertex shader code.
     * @param wffsc                  Wire frame program fragment shader code.
     * @param plane                  Plane object.
     * @param terrain                Terrain object.
     * @param rings                  Ring objects.
     * @param wireFrameObjectPlane   Wire frame object for plane.
     * @param wireFrameObjectTerrain Wire frame object for terrain.
     */
    public MyGLRenderer(GameActivity parent, String vsc, String fsc, String tvsc, String tfsc,
                        String wfvsc, String wffsc, Plane plane, Terrain terrain, Rings rings,
                        WireFrameObject wireFrameObjectPlane,
                        WireFrameObject wireFrameObjectTerrain) {

        this.parent = parent;

        VERTEX_SHADER_CODE = vsc;
        FRAGMENT_SHADER_CODE = fsc;

        TEXTURE_VERTEX_SHADER_CODE = tvsc;
        TEXTURE_FRAGMENT_SHADER_CODE = tfsc;

        WIRE_FRAME_VERTEX_SHADER_CODE = wfvsc;
        WIRE_FRAME_FRAGMENT_SHADER_CODE = wffsc;

        this.plane = plane;
        this.terrain = terrain;
        this.rings = rings;
        this.wireFrameObjectTerrain = wireFrameObjectTerrain;
        this.wireFrameObjectPlane = wireFrameObjectPlane;
    }

    /**
     * Initializes OpenGl for screen displaying.
     *
     * @param gl     Unused.
     * @param config Unused.
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        glLineWidth(3f);

        wireFrameProgram = new ColorProgram(
                WIRE_FRAME_VERTEX_SHADER_CODE, WIRE_FRAME_FRAGMENT_SHADER_CODE);
        textureProgram = new TextureProgram(
                TEXTURE_VERTEX_SHADER_CODE, TEXTURE_FRAGMENT_SHADER_CODE);
        colorProgram = new ColorProgram(VERTEX_SHADER_CODE, FRAGMENT_SHADER_CODE);
    }

    /**
     * Handles surface changing but is inflated only once because game does not allow portrait mode.
     *
     * @param gl     Unused.
     * @param width  Screen width.
     * @param height Screen height.
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        setPerspectiveMatrix(65, (float) width / (float) height, .1f, 180f, projectionMatrix);
        setLookAtM(viewMatrix, 0, 0, 0, -2f, 0, 0, -1f, 0, 1, 0);
    }

    /**
     * Stores device x orientation from last 25 orientation updates.
     */
    static float[] avgRotationX = new float[25];

    /**
     * Stores device y orientation from last 25 orientation updates.
     */
    static float[] avgRotationY = new float[25];

    /**
     * Stores average x orientation from the 25 last orientations to prevent shaking.
     */
    static float avgX = 0;

    /**
     * Stores average y orientation from the 25 last orientations to prevent shaking.
     */
    static float avgY = 0;

    /**
     * Stores x direction for something.
     */
    private float directionX = 0;

    /**
     * Planes x rotation.
     */
    private float rotationX = 0f;

    /**
     * Planes y rotation.
     */
    private float rotationY = 0f;

    /**
     * Planes z rotation.
     */
    private float rotationZ = 0f;

    /**
     * Planes x direction.
     */
    private float planeDirectionX = 0;

    /**
     * Planes y rotation.
     */
    private float planeDirectionY = 0;

    /**
     * Planes z rotation.
     */
    private float planeDirectionZ = 0;

    /**
     * Stores light direction.
     */
    private Vector3D lightDirection = new Vector3D(new float[]{1, 0, 0}).getNormalizedVersion();

    /**
     * Handles game drawing and model placing on the screen.
     *
     * @param gl Unused.
     */
    @Override
    public void onDrawFrame(GL10 gl) {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        //Draw plane
        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, 0, -1.5f, 0);
        rotateM(modelMatrix, 0, rotationX + 180, 0, 1, 0);
        rotateM(modelMatrix, 0, rotationY, 1, 0, 0);

        planeDirectionX = modelMatrix[2] / 10f;
        planeDirectionY = modelMatrix[9] / 5f;
        planeDirectionZ = modelMatrix[10] / 10f;

        rotateM(modelMatrix, 0, rotationZ, 0, 0, 1);
        rotateM(modelMatrix, 0, -80, 1, 0, 0);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix,
                0, modelMatrix, 0);
        glUseProgram(colorProgram.getProgram());
        colorProgram.setUniforms(modelViewProjectionMatrix, 0);
        plane.bind(colorProgram);
        plane.setLightDirection(lightDirection);
        plane.draw();

        glUseProgram(wireFrameProgram.getProgram());
        wireFrameProgram.setColorUniform(plane.getWireColor());
        wireFrameProgram.setUniforms(modelViewProjectionMatrix, 0);
        wireFrameObjectPlane.bind(wireFrameProgram);
        wireFrameObjectPlane.draw();

        //Draw ring
        rings.draw(
                modelMatrix,
                viewProjectionMatrix,
                modelViewProjectionMatrix,
                colorProgram,
                wireFrameProgram,
                worldX, worldY, worldZ, lightDirection);

        //Draw terrain
        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, -worldX, -worldY - 10, -worldZ);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix,
                0, modelMatrix, 0);
        glUseProgram(colorProgram.getProgram());
        colorProgram.setUniforms(modelViewProjectionMatrix, 0);
        terrain.bind(colorProgram);
        terrain.draw();

        glUseProgram(wireFrameProgram.getProgram());
        wireFrameProgram.setColorUniform(terrain.getWireColor());
        wireFrameProgram.setUniforms(modelViewProjectionMatrix, 0);
        wireFrameObjectTerrain.bind(wireFrameProgram);
        wireFrameObjectTerrain.draw();

        lightDirection.setTuple(-planeDirectionX * 10, planeDirectionX * 10, 1);
        lightDirection.normalize();

        rotate();
        if (parent.isGameRunning) {
            move();
            if (terrain.checkWorldCollision(worldX, worldY, worldZ)) {
                setTurbo(false);
                parent.setSoundRate(false);
                worldX = 0;
                worldY = 60;
                worldZ = 0;
            }
            if (rings.isCollision(worldX, worldY, worldZ)) {
                parent.addPoint();
            }
        }

        float cameraAddition = 30 + ((speedMultiplication - SPEED_SLOW) * 2);
        setLookAtM(viewMatrix, 0, planeDirectionX * cameraAddition, -planeDirectionY * 10,
                -planeDirectionZ * cameraAddition, 0, 0, 0, 0, 1, 0);
    }

    /**
     * Calculates perspective matrix.
     *
     * @param fovDegrees Field of view degrees.
     * @param aspect     Device aspect ratio.
     * @param near       Closest visible point.
     * @param far        Most far visible point.
     * @param matrix     Stored matrix.
     */
    public static void setPerspectiveMatrix(
            float fovDegrees, float aspect, float near, float far, float[] matrix) {
        final float angleRads = (float) (fovDegrees * Math.PI / 180.0);
        final float a = (float) (1.0 / Math.tan(angleRads / 2.0));

        matrix[0] = a / aspect;
        matrix[1] = 0f;
        matrix[2] = 0f;
        matrix[3] = 0f;
        matrix[4] = 0f;
        matrix[5] = a;
        matrix[6] = 0f;
        matrix[7] = 0f;
        matrix[8] = 0f;
        matrix[9] = 0f;
        matrix[10] = -((far + near) / (far - near));
        matrix[11] = -1f;
        matrix[12] = 0f;
        matrix[13] = 0f;
        matrix[14] = -((2f * far * near) / (far - near));
        matrix[15] = 0f;
    }

    /**
     * Stores world x position.
     */
    public float worldX = 0;

    /**
     * Stores world y position.
     */
    public float worldY = 0;

    /**
     * Stores world z position.
     */
    public float worldZ = 0;

    /**
     * Rotates everything around the origin point.
     */
    public void rotate() {
        rotationX += directionX / 2f;
        updateRotation();
        rotationZ = avgX;
        rotationY = avgY;
    }

    /**
     * Updates world movement.
     */
    private void move() {

        if (isTurbo) {
            if (speedMultiplication <= SPEED_FAST) speedMultiplication += 0.2f;
            else {
                speedMultiplication = SPEED_FAST;
            }
        } else {
            if (speedMultiplication >= SPEED_SLOW) speedMultiplication -= 0.1f;
            else speedMultiplication = SPEED_SLOW;
        }

        if (planeDirectionX < 0 && worldX < terrain.getMaxX() ||
                planeDirectionX > 0 && worldX > terrain.getMinX()) {
            worldX -= planeDirectionX * speedMultiplication;
        }

        if (planeDirectionZ > 0 && worldZ < terrain.getMaxZ() ||
                planeDirectionZ < 0 && worldZ > terrain.getMinZ()) {
            worldZ -= -planeDirectionZ * speedMultiplication;
        }

        worldY += planeDirectionY * speedMultiplication;
    }

    /**
     * Updates planes rotation and calculates average device orientation.
     */
    public void updateRotation() {
        directionX = GameActivity.dirX / 2f;

        avgX = 0;
        avgY = 0;

        for (int i = avgRotationX.length - 1; i > 0; i--) {
            avgRotationX[i] = avgRotationX[i - 1];
            avgRotationY[i] = avgRotationY[i - 1];
            avgX += avgRotationX[i];
            avgY += avgRotationY[i];
        }
        avgRotationX[0] = -GameActivity.dirX * 5;
        avgRotationY[0] = -GameActivity.dirY * 5;
        avgX += avgRotationX[0];
        avgY += avgRotationY[0];
        avgX = avgX / avgRotationX.length;
        avgY = avgY / avgRotationY.length;
    }

    /**
     * Defines whether turbo speed is on.
     */
    private boolean isTurbo;

    /**
     * Constant value for non turbo speed.
     */
    private final float SPEED_SLOW = 4;

    /**
     * Constant value for turbo speed.
     */
    private final float SPEED_FAST = 8;

    /**
     * Stores current speed.
     */
    private float speedMultiplication = SPEED_SLOW;

    /**
     * Starts and stops ultra turbo super duber speed.
     *
     * @param isTurbo Defines whether now is time for some turbo speed or what.
     */
    public void setTurbo(boolean isTurbo) {
        this.isTurbo = isTurbo;
    }
}
