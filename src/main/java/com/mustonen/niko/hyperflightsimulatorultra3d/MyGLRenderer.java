package com.mustonen.niko.hyperflightsimulatorultra3d;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.mustonen.niko.hyperflightsimulatorultra3d.object.Plane;
import com.mustonen.niko.hyperflightsimulatorultra3d.object.Wall;
import com.mustonen.niko.hyperflightsimulatorultra3d.opengl.ColorProgram;
import com.mustonen.niko.hyperflightsimulatorultra3d.opengl.TextureProgram;
import com.mustonen.niko.hyperflightsimulatorultra3d.util.Debug;
import com.mustonen.niko.hyperflightsimulatorultra3d.util.FileStringReader;
import com.mustonen.niko.hyperflightsimulatorultra3d.object.JetPlane;
import com.mustonen.niko.hyperflightsimulatorultra3d.util.ObjFileReader;
import com.mustonen.niko.hyperflightsimulatorultra3d.util.TextureUtil;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;
import static android.opengl.Matrix.*;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private int wallTexture;
    private final Context C;

    private TextureProgram textureProgram;
    private ColorProgram colorProgram;

    private JetPlane jet;
    private Wall wall;
    private Plane plane;

    //Matrices
    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];

    //Shader codes
    public static String VERTEX_SHADER_CODE, FRAGMENT_SHADER_CODE,
            TEXTURE_VERTEX_SHADER_CODE, TEXTURE_FRAGMENT_SHADER_CODE;

    public MyGLRenderer(Context c) {
        this.C = c;
        VERTEX_SHADER_CODE = FileStringReader.readFile(C, R.raw.vertex_shader);
        FRAGMENT_SHADER_CODE = FileStringReader.readFile(C, R.raw.fragment_shader);
        TEXTURE_VERTEX_SHADER_CODE = FileStringReader.readFile(C, R.raw.texture_vertex_shader);
        TEXTURE_FRAGMENT_SHADER_CODE = FileStringReader.readFile(C, R.raw.texture_fragment_shader);

        float[] vertices = ObjFileReader.readFile(C, R.raw.cube);
        plane = new Plane(vertices);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glEnable(GL_DEPTH_TEST);

        jet = new JetPlane();
        wall = new Wall();

        wallTexture = TextureUtil.loadTexture(C, R.drawable.wall);

        textureProgram = new TextureProgram(TEXTURE_VERTEX_SHADER_CODE, TEXTURE_FRAGMENT_SHADER_CODE);
        colorProgram = new ColorProgram(VERTEX_SHADER_CODE, FRAGMENT_SHADER_CODE);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        setPerspectiveMatrix(50, (float) width / (float) height, .1f, 50f, projectionMatrix);
        setLookAtM(viewMatrix, 0, 0, 0, -2f, 0, 0, -1f, 0, 1, 0);
    }

    static float[] avgRotationX = new float[10];
    static float[] avgRotationY = new float[10];
    static float avgX = 0;
    static float avgY = 0;

    public void rotate() {
        direction = GameActivity.dirX / 2f;

        avgX = 0;
        avgY = 0;

        for(int i = avgRotationX.length - 1; i > 0; i--) {
            avgRotationX[i] = avgRotationX[i-1];
            avgRotationY[i] = avgRotationY[i-1];
            avgX += avgRotationX[i];
            avgY += avgRotationY[i];
        }
        avgRotationX[0] = -GameActivity.dirX * 5;
        avgRotationY[0] = -(GameActivity.dirY + 1f) * 5;
        avgX += avgRotationX[0];
        avgY += avgRotationY[0];
        avgX = avgX / avgRotationX.length;
        avgY = avgY / avgRotationY.length;
    }

    private float direction = 0;
    private float rotationX = 0f;
    private float rotationY = 0f;
    private float rotationZ = 0f;
    private float planeDirectionX = 0;
    private float planeDirectionY = 0;
    private float planeDirectionZ = 0;

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        //Draw walls
        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, 0 - worldX, 0 - worldY, 0 - worldZ);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix,
                0, modelMatrix, 0);
        glUseProgram(textureProgram.getProgram());
        textureProgram.setUniforms(modelViewProjectionMatrix, wallTexture);
        wall.bind(textureProgram);
        wall.draw();

        //Draw plane
        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, 0, -1.5f, 0);
        rotateM(modelMatrix, 0, rotationX + 180, 0, 1, 0);
        rotateM(modelMatrix, 0, rotationY, 1, 0, 0);

        planeDirectionX = modelMatrix[2] / 10f;
        planeDirectionY = modelMatrix[9] / 5f;
        planeDirectionZ = modelMatrix[10] / 10f;
        worldX -= planeDirectionX;
        worldY += planeDirectionY;
        worldZ += planeDirectionZ;

        rotateM(modelMatrix, 0, rotationZ, 0, 0, 1);
        rotateM(modelMatrix, 0, -80, 1, 0, 0);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix,
                0, modelMatrix, 0);
        glUseProgram(colorProgram.getProgram());
        colorProgram.setUniforms(modelViewProjectionMatrix, 0);
        plane.bind(colorProgram);
        plane.draw();

        setLookAtM(viewMatrix, 0, planeDirectionX * 30, 0, -planeDirectionZ * 30, 0, 0, 0, 0, 1, 0);

        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix,
                0, modelMatrix, 0);

        move();
    }

    public static void setPerspectiveMatrix(float fovDegrees, float aspect, float near, float far, float[] matrix) {
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

    public static boolean isRotateLeft = false;
    public static boolean isRotateRight = false;
    public static boolean isUp = false;
    public static boolean isDown = false;
    public float worldX = 0;
    public float worldY = 0;
    public float worldZ = 0;

    public void move() {
        rotationX += direction;
        rotate();
        rotationZ = avgX;
        rotationY = avgY;
        /*if(avgX < 35 && avgX > -35) {
            float distance = Math.abs(avgX - rotationZ);
            float speed = distance / 3f;
            //Debug.debug(null, "Speed: " + speed);
            if(rotationZ < avgX) {
                rotationZ += speed;
            } else if(rotationZ > avgX) {
                rotationZ -= speed;
            }
        }*/
    }

    private static void printMatrix(float[] matrix) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for(float f : matrix) {
            sb.append(index++).append(": ").append(f).append("\n");
        }
        Debug.debug(null, sb.toString());
    }
}