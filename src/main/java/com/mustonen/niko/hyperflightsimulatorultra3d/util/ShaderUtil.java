package com.mustonen.niko.hyperflightsimulatorultra3d.util;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

/**
 * Class for generating shaders and shader programs.
 *
 * @author Niko Mustonen mustonen.niko@gmail.com
 * @version %I%, %G%
 * @since 1.8
 */
public class ShaderUtil {

    /**
     * Generates vertex shader.
     *
     * @param code Vertex shader code.
     * @return Shader memory location.
     */
    public static int compileVertexShader(String code) {
        return compileShader(GL_VERTEX_SHADER, code);
    }

    /**
     * Generates fragment shader.
     *
     * @param code Fragment shader code.
     * @return Shader memory location.
     */
    public static int compileFragmentShader(String code) {
        return compileShader(GL_FRAGMENT_SHADER, code);
    }

    /**
     * Compiles shader.
     *
     * @param type Shader type, fragment or vertex.
     * @param code Shader code.
     * @return Returns shaders memory location.
     */
    private static int compileShader(int type, String code) {
        final int SHADER_ID = glCreateShader(type);
        if(SHADER_ID == 0) {
            Debug.warning(null, "Error while creating new shader.");
            return 0;
        } else {
            glShaderSource(SHADER_ID, code);
            glCompileShader(SHADER_ID);
            final int[] COMPILE_STATUS = new int[1];
            glGetShaderiv(SHADER_ID, GL_COMPILE_STATUS, COMPILE_STATUS, 0);
            if(COMPILE_STATUS[0] == 0) {
                final String GL_MESSAGE = glGetShaderInfoLog(SHADER_ID);
                Debug.warning(null, "Error while compiling shader. \nOPenGL LOG: " + GL_MESSAGE);
                glDeleteShader(SHADER_ID);
                return 0;
            } else {
                return SHADER_ID;
            }
        }
    }

    /**
     * Links fragment shader and vertex shader to OpenGl program.
     *
     * @param VERTEX_SHADER_ID Vertex shader.
     * @param FRAGMENT_SHADER_ID Fragment shader.
     * @return Returns programs memory loacation.
     */
    public static int linkProgram(final int VERTEX_SHADER_ID, final int FRAGMENT_SHADER_ID) {
        final int PROGRAM_ID = glCreateProgram();

        if(PROGRAM_ID == 0) {
            Debug.warning(null, "Error while creating new OpenGL program.");
            return 0;
        } else {
            glAttachShader(PROGRAM_ID, VERTEX_SHADER_ID);
            glAttachShader(PROGRAM_ID, FRAGMENT_SHADER_ID);
            glLinkProgram(PROGRAM_ID);

            final int[] LINK_STATUS = new int[1];
            glGetProgramiv(PROGRAM_ID, GL_LINK_STATUS, LINK_STATUS, 0);

            if(LINK_STATUS[0] == 0) {
                final String GL_MESSAGE = glGetProgramInfoLog(PROGRAM_ID);
                Debug.warning(null, "Error while linking program. \nOpenGL LOG: " + GL_MESSAGE);
                glDeleteProgram(PROGRAM_ID);
                return 0;
            } else {
                return PROGRAM_ID;
            }
        }
    }

    /**
     * Validates shader program.
     *
     * @param PROGRAM_ID Program memory location.
     * @return Returns boolean value based on programs state.
     */
    public static boolean isValidProgram(final int PROGRAM_ID) {
        glValidateProgram(PROGRAM_ID);

        final int[] VALIDATE_STATUS = new int[1];
        glGetProgramiv(PROGRAM_ID, GL_VALIDATE_STATUS, VALIDATE_STATUS, 0);

        if(VALIDATE_STATUS[0] == 0) {
            final String GL_MESSAGE = glGetProgramInfoLog(PROGRAM_ID);
            Debug.warning(null, "Program failed validation.\nOpenGL LOG: " + GL_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    /**
     * Starts shader program building.
     *
     * @param vertexShaderCode Vertex shader code in string format.
     * @param fragmentShaderCode Fragment shader code in string format.
     * @return Returns ready programs memory loaction.
     */
    public static int buildProgram(String vertexShaderCode, String fragmentShaderCode) {
        final int VERTEX_SHADER = compileVertexShader(vertexShaderCode);
        final int FRAGMENT_SHADER = compileFragmentShader(fragmentShaderCode);
        final int PROGRAM = linkProgram(VERTEX_SHADER, FRAGMENT_SHADER);
        Debug.debug(null, "Is Program valid" + isValidProgram(PROGRAM));
        return PROGRAM;
    }
}
