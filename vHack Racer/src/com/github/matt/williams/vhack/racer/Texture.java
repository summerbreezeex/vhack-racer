package com.github.matt.williams.vhack.racer;

import java.nio.ByteBuffer;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class Texture {
    private int mId;

    public Texture(Bitmap bitmap) {
        mId = generateTextureId();
        int oldId = pushTexture();
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        popTexture(oldId);
    }

    protected void finalize() {
        if (mId != 0) {
            GLES20.glDeleteTextures(1, new int[] {mId}, 0);
        }
    }
    
    public void use(int channel) {
        GLES20.glActiveTexture(channel);
        Utils.checkErrors("glActiveTexture");
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mId);
        Utils.checkErrors("glBindTexture");
    }
    
    public int getId() {
        return mId;
    }
    
    private static int generateTextureId() {
        int[] ids = new int[1];
        GLES20.glGenTextures(1, ids, 0);
        Utils.checkErrors("glGenTextures");
        return ids[0];
    }
    
    private int pushTexture() {
        int[] oldIds = new int[1];
        GLES20.glGetIntegerv(GLES20.GL_TEXTURE_BINDING_2D, oldIds, 0);
        Utils.checkErrors("glGetIntegerv");
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mId);
        Utils.checkErrors("glBindTexture");
        return oldIds[0];
    }
    
    private void popTexture(int oldId) {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, oldId);        
        Utils.checkErrors("glBindTexture");
    }
}