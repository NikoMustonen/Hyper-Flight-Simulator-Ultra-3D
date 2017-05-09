package com.mustonen.niko.hyperflightsimulatorultra3d.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static android.opengl.GLES20.*;
import static android.opengl.GLUtils.*;

/**
 * Class for loading textures.
 *
 * @author Niko Mustonen mustonen.niko@gmail.com
 * @version %I%, %G%
 * @since 1.8
 */
public class TextureUtil {

    /**
     * Loads textures.
     *
     * @param c Activity context.
     * @param fileId File id.
     * @return Textures memory location.
     */
    public static int loadTexture(Context c, int fileId) {
        final int[] textures = new int[1];
        glGenTextures(1, textures, 0);

        if(textures[0] == 0) {
            Debug.debug(null, "Error while loading textures.");
            glDeleteTextures(1, textures, 0);
            return 0;
        }

        final BitmapFactory.Options OPTS = new BitmapFactory.Options();
        OPTS.inScaled = false;
        final Bitmap BITMAP = BitmapFactory.decodeResource(c.getResources(), fileId, OPTS);

        if(BITMAP == null) {
            Debug.debug(null, "Error while decoding textures.");
            glDeleteTextures(1, textures, 0);
            return 0;
        }
        glBindTexture(GL_TEXTURE_2D, textures[0]);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        texImage2D(GL_TEXTURE_2D, 0, BITMAP, 0);
        BITMAP.recycle();
        glGenerateMipmap(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);
        return textures[0];
    }
}
