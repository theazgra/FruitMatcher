package com.example.vojtch.fruitmatcher.OpenGL;

import android.opengl.GLES20;

/**
 * Created by Vojtěch on 25.10.2017.
 */

public class Shader {
    private static int solidColorShader;
    private static int spriteImageShader;
    private static int textShader;


    public static final String vs_Text =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec4 a_Color;" +
                    "attribute vec2 a_texCoord;" +
                    "varying vec4 v_Color;" +
                    "varying vec2 v_texCoord;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "  v_texCoord = a_texCoord;" +
                    "  v_Color = a_Color;" +
                    "}";
    public static final String fs_Text =
            "precision mediump float;" +
                    "varying vec4 v_Color;" +
                    "varying vec2 v_texCoord;" +
                    "uniform sampler2D s_texture;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D( s_texture, v_texCoord ) * v_Color;" +
                    "  gl_FragColor.rgb *= v_Color.a;" +
                    "}";


    public static final String vs_SolidColor =
            "uniform    mat4        uMVPMatrix;" +
                    "attribute  vec4        vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    public static final String fs_SolidColor =
            "precision mediump float;" +
                    "void main() {" +
                    "  gl_FragColor = vec4(0.5,0,0,1);" +
                    "}";

    /* SHADER Image
 *
 * This shader is for rendering 2D images straight from a texture
 * No additional effects.
 *
 */
    public static final String vs_Image =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec2 a_texCoord;" +
                    "varying vec2 v_texCoord;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "  v_texCoord = a_texCoord;" +
                    "}";
    public static final String fs_Image =
                    "precision mediump float;" +
                    "varying vec2 v_texCoord;" +
                    "uniform sampler2D s_texture;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D( s_texture, v_texCoord );" +
                    "}";

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        // return the shader
        return shader;
    }

    public static int getSolidColorShader() {
        return solidColorShader;
    }

    public static int getSpriteImageShader() {
        return spriteImageShader;
    }

    public static int getTextShader() {
        return textShader;
    }

    public static void setSpriteImageShader(int spriteImageShader) {
        Shader.spriteImageShader = spriteImageShader;
    }

    public static void setSolidColorShader(int solidColorShader) {
        Shader.solidColorShader = solidColorShader;
    }

    public static void setTextShader(int textShader) {
        Shader.textShader = textShader;
    }
}
