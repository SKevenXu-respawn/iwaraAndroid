package com.sk.iwara.util;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import androidx.annotation.OptIn;
import androidx.media3.common.GlObjectsProvider;
import androidx.media3.common.GlTextureInfo;
import androidx.media3.common.VideoFrameProcessingException;
import androidx.media3.common.util.Size;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.effect.BaseGlShaderProgram;
import androidx.media3.effect.GlEffect;
import androidx.media3.effect.GlShaderProgram;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.concurrent.Executor;

@UnstableApi
public class Anime4KGlEffect implements GlEffect {

    private final String glsl;

    public Anime4KGlEffect(Context ctx) throws IOException {
//        String mpv = readAsset(ctx, "glsl/Anime4K_Thin_Fast.glsl");
//        glsl = MpvToEs2Translator.translate(mpv, "uTexture", "uResolution");
        glsl = readAsset(ctx, "glsl/es2_glsl/Anime4K_Thin_Fast_ES2.glsl");
    }

    @OptIn(markerClass = UnstableApi.class)
    @Override
    public GlShaderProgram toGlShaderProgram(Context context, boolean useHdr) throws VideoFrameProcessingException {

        return new BaseGlShaderProgram(useHdr,2) {
            private int program=0;
            private int aPos,aTex,uTex,uRes;
            private Size size;
            /* 顶点着色器 - 固定 */
            private final String vtx=
                    "attribute vec4 aPosition;\n" +
                            "attribute vec2 aTextureCoord;\n" +
                            "varying vec2 vTexCoord;\n" +
                            "void main(){\n" +
                            "  gl_Position=aPosition;\n" +
                            "  vTexCoord=aTextureCoord;\n" +
                            "}";
            @Override
            public Size configure(int inputWidth, int inputHeight) throws VideoFrameProcessingException {
                size=new Size(inputWidth*2, inputHeight*2);
                return size;   // 同尺寸输出
            }

            @Override
            public void drawFrame(int inputTexId, long presentationTimeUs) throws VideoFrameProcessingException {
                if(program==0) initGL();
                GLES20.glUseProgram(program);
                GLES20.glUniform2f(uRes,size.getWidth(),size.getHeight());
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,inputTexId);
                GLES20.glUniform1i(uTex,0);
                drawQuad();
                Log.i("Anime4KGlEffect", inputTexId+" "+presentationTimeUs);
            }
            private void initGL() throws VideoFrameProcessingException{
                int vs=loadShader(GLES20.GL_VERTEX_SHADER,vtx);
                int fs=loadShader(GLES20.GL_FRAGMENT_SHADER,glsl); // <— 直接复用
                program=GLES20.glCreateProgram();
                GLES20.glAttachShader(program,vs);
                GLES20.glAttachShader(program,fs);
                GLES20.glLinkProgram(program);
                int[]link=new int[1];
                GLES20.glGetProgramiv(program,GLES20.GL_LINK_STATUS,link,0);
                if(link[0]!=GLES20.GL_TRUE){
                    String log=GLES20.glGetProgramInfoLog(program);
                    throw new VideoFrameProcessingException("link err:"+log);
                }
                aPos=GLES20.glGetAttribLocation(program,"aPosition");
                aTex=GLES20.glGetAttribLocation(program,"aTextureCoord");
                uTex=GLES20.glGetUniformLocation(program,"uTexture");
                uRes=GLES20.glGetUniformLocation(program,"uResolution");
            }
            private int loadShader(int type,String code) throws VideoFrameProcessingException{
                int s=GLES20.glCreateShader(type);
                GLES20.glShaderSource(s,code);
                GLES20.glCompileShader(s);
                int[] compiled=new int[1];
                GLES20.glGetShaderiv(s,GLES20.GL_COMPILE_STATUS,compiled,0);
                if(compiled[0]==0){
                    String log=GLES20.glGetShaderInfoLog(s);
                    GLES20.glDeleteShader(s);
                    throw new VideoFrameProcessingException("compile err:"+log);
                }
                return s;
            }
            private void drawQuad(){
                float[]v={-1,-1,0,0,  1,-1,1,0,  -1,1,0,1,  1,1,1,1};
                FloatBuffer fb= ByteBuffer.allocateDirect(v.length*4)
                        .order(ByteOrder.nativeOrder()).asFloatBuffer();
                fb.put(v).position(0);
                GLES20.glVertexAttribPointer(aPos,2,GLES20.GL_FLOAT,false,4*4,fb);
                fb.position(2);
                GLES20.glVertexAttribPointer(aTex,2,GLES20.GL_FLOAT,false,4*4,fb);
                GLES20.glEnableVertexAttribArray(aPos);
                GLES20.glEnableVertexAttribArray(aTex);
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
                GLES20.glDisableVertexAttribArray(aPos);
                GLES20.glDisableVertexAttribArray(aTex);
            }
        };
    }

    private String readAsset(Context ctx, String name) throws IOException {
        InputStream is = ctx.getAssets().open(name);
        byte[] buf = new byte[is.available()];
        is.read(buf);
        is.close();
        return new String(buf);
    }

    /* ========== 单通着色器包装 ========== */
    private static class SingleShaderProgram implements GlShaderProgram {

        private final String fragment;

        SingleShaderProgram(String fragment) {
            this.fragment = fragment;
        }


        @Override
        public void setInputListener(InputListener inputListener) {

        }

        @Override
        public void setOutputListener(OutputListener outputListener) {
            Log.i("Anime4KGlEffect", "Outputing");
        }

        @Override
        public void setErrorListener(Executor executor, ErrorListener errorListener) {

        }

        @Override
        public void queueInputFrame(GlObjectsProvider glObjectsProvider, GlTextureInfo inputTexture, long presentationTimeUs) {

        }

        @Override
        public void releaseOutputFrame(GlTextureInfo outputTexture) {

        }

        @Override
        public void signalEndOfCurrentInputStream() {

        }

        @Override
        public void flush() {

        }

        @Override
        public void release() throws VideoFrameProcessingException {

        }
    }
}