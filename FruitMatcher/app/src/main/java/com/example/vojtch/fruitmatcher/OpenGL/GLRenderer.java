package com.example.vojtch.fruitmatcher.OpenGL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.view.MotionEvent;

import com.example.vojtch.fruitmatcher.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES10.GL_CLAMP_TO_EDGE;

/**
 * Created by Vojtěch on 25.10.2017.
 */

public class GLRenderer implements GLSurfaceView.Renderer {

    // Our matrices
    private final float[] mtrxProjection = new float[16];
    private final float[] mtrxView = new float[16];
    private final float[] mtrxProjectionAndView = new float[16];

    // Geometric variables
    public static float vertices[];
    public static short indices[];
    public static float uvs[];
    public FloatBuffer vertexBuffer;
    public ShortBuffer drawListBuffer;
    public FloatBuffer uvBuffer;

    // Our screenresolution
    float   mScreenWidth = 1080;
    float   mScreenHeight = 1920;

    //Misc
    Context mContext;
    long mLastTime;
    int mProgram;

    float scaleRation = 1.0f;
    float scaleRationX = 1.0f;
    float scaleRationY = 1.0f;
    float developigScreenWidth = 1920.0f;
    float developingScreenHeight = 1080.0f;

    public TextManager tm;

    public Sprite sprite;

    public GLRenderer(Context c)
    {

        mContext = c;
        mLastTime = System.currentTimeMillis() + 100;
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void onPause()
    {
        /* Do stuff to pause the renderer */
    }

    public void onResume()
    {
        /* Do stuff to resume the renderer */
        mLastTime = System.currentTimeMillis();
    }

    public void SetupText()
    {
        // Create our text manager
        tm = new TextManager();

        // Tell our text manager to use index 1 of textures loaded
        tm.setTextureID(1);

        // Pass the uniform scale
        tm.setUniformscale(scaleRation);

        // Create our new textobject
        float[] color = new float[4];
        color[0] = 1f;
        color[1] = 1f;
        color[2] = 1f;
        color[3] = 1.0f;

        TextObject txt = new TextObject("Hello android",
                new PointF(developigScreenWidth/2, developingScreenHeight/2),
                color);

        // Add it to our manager
        tm.addText(txt);

        // Prepare the text for rendering
        tm.PrepareDraw();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        SetupScaling();

        sprite = new Sprite(scaleRation);

        SetupTriangle();

        SetupImage();
        // Create our texts
        SetupText();

        // Create the shaders, solid color
        int vertexShader = Shader.loadShader(GLES20.GL_VERTEX_SHADER,
                Shader.vs_SolidColor);
        int fragmentShader = Shader.loadShader(GLES20.GL_FRAGMENT_SHADER,
                Shader.fs_SolidColor);

        Shader.setSolidColorShader(GLES20.glCreateProgram());
        GLES20.glAttachShader(Shader.getSolidColorShader(), vertexShader);
        GLES20.glAttachShader(Shader.getSolidColorShader(), fragmentShader);
        GLES20.glLinkProgram(Shader.getSolidColorShader());

        // Create the shaders, images
        vertexShader = Shader.loadShader(GLES20.GL_VERTEX_SHADER,
                Shader.vs_Image);
        fragmentShader = Shader.loadShader(GLES20.GL_FRAGMENT_SHADER,
                Shader.fs_Image);

        Shader.setSpriteImageShader(GLES20.glCreateProgram());
        GLES20.glAttachShader(Shader.getSpriteImageShader(), vertexShader);
        GLES20.glAttachShader(Shader.getSpriteImageShader(), fragmentShader);
        GLES20.glLinkProgram(Shader.getSpriteImageShader());

        //Text shader
        int textVertexShader = Shader.loadShader(GLES20.GL_VERTEX_SHADER, Shader.vs_Text);
        int textFragmentShader = Shader.loadShader(GLES20.GL_FRAGMENT_SHADER, Shader.fs_Text);
        Shader.setTextShader(GLES20.glCreateProgram());
        GLES20.glAttachShader(Shader.getTextShader(), textVertexShader);
        GLES20.glAttachShader(Shader.getTextShader(), textFragmentShader);
        GLES20.glLinkProgram(Shader.getTextShader());


        // Set our shader programm
        GLES20.glUseProgram(Shader.getTextShader());
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        // We need to know the current width and height.
        mScreenWidth = width;
        mScreenHeight = height;

        // Redo the Viewport, making it fullscreen.
        GLES20.glViewport(0, 0, (int)mScreenWidth, (int)mScreenHeight);

        // Clear our matrices
        for(int i=0;i<16;i++)
        {
            mtrxProjection[i] = 0.0f;
            mtrxView[i] = 0.0f;
            mtrxProjectionAndView[i] = 0.0f;
        }

        // Setup our screen width and height for normal sprite translation.
        Matrix.orthoM(mtrxProjection, 0, 0f, mScreenWidth, 0.0f, mScreenHeight, 0, 50);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);

        SetupScaling();
    }

    @Override
    public void onDrawFrame(GL10 unused) {

        // Get the current time
        long now = System.currentTimeMillis();

        // We should make sure we are valid and sane
        if (mLastTime > now) return;

        // Get the amount of time the last frame took.
        long elapsed = now - mLastTime;

        //UpdateSprite();

        if(tm!=null)
            tm.Draw(mtrxProjectionAndView);

        // Render our example
        Render(mtrxProjectionAndView);

        // Save the current time to see how long it took <img src="http://androidblog.reindustries.com/wp-includes/images/smilies/icon_smile.gif" alt=":)" class="wp-smiley"> .
        mLastTime = now;
    }

    private void Render(float[] m) {

        GLES20.glUseProgram(Shader.getSpriteImageShader());
        // clear Screen and Depth Buffer,
        // we have set the clear color as black.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // get handle to vertex shader's vPosition member
        int mPositionHandle =
                GLES20.glGetAttribLocation(Shader.getSpriteImageShader(), "vPosition");

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, 3,
                GLES20.GL_FLOAT, false,
                0, vertexBuffer);

        // Get handle to texture coordinates location
        int mTexCoordLoc = GLES20.glGetAttribLocation(Shader.getSpriteImageShader(),
                "a_texCoord" );

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray ( mTexCoordLoc );

        // Prepare the texturecoordinates
        GLES20.glVertexAttribPointer ( mTexCoordLoc, 2, GLES20.GL_FLOAT,
                false,
                0, uvBuffer);

        // Get handle to shape's transformation matrix
        int mtrxhandle = GLES20.glGetUniformLocation(Shader.getSpriteImageShader(),
                "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);

        // Get handle to textures locations
        int mSamplerLoc = GLES20.glGetUniformLocation (Shader.getSpriteImageShader(),
                "s_texture" );

        // Set the sampler texture unit to 0, where we have saved the texture.
        GLES20.glUniform1i ( mSamplerLoc, 0);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);

    }

    public void SetupImage()
    {
        int numberOfObject = 30;
        //for 30 objects 4 vertices 2 floats,u and v
        uvs = new float[numberOfObject * 4 * 2];

        Random rnd = new Random();

        for (int i = 0; i < numberOfObject; i++) {
            int random_u_offset = rnd.nextInt(2);
            int random_v_offset = rnd.nextInt(2);

            // Adding the UV's using the offsets
            uvs[(i*8) + 0] = random_u_offset * 0.5f;
            uvs[(i*8) + 1] = random_v_offset * 0.5f;

            uvs[(i*8) + 2] = random_u_offset * 0.5f;
            uvs[(i*8) + 3] = (random_v_offset+1) * 0.5f;

            uvs[(i*8) + 4] = (random_u_offset+1) * 0.5f;
            uvs[(i*8) + 5] = (random_v_offset+1) * 0.5f;

            uvs[(i*8) + 6] = (random_u_offset+1) * 0.5f;
            uvs[(i*8) + 7] = random_v_offset * 0.5f;
        }

        // The texture buffer
        ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        uvBuffer = bb.asFloatBuffer();
        uvBuffer.put(uvs);
        uvBuffer.position(0);

        // Generate Textures, if more needed, alter these numbers.
        int[] texturenames = new int[2];
        GLES20.glGenTextures(1, texturenames, 0);
        GLES20.glGenTextures(2, texturenames, 0);

        // Retrieve our image from resources.
        //int id = R.drawable.flag_cz;
        int id = R.drawable.textureatlas;

        // Temporary create a bitmap
        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), id);

        // Bind texture to texturename
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[0]);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);

        // Set wrapping mode
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GL_CLAMP_TO_EDGE);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

        // We are done using the bitmap so we should recycle it.
        bmp.recycle();

        // Again for the text texture
        id = R.drawable.font;

        bmp = BitmapFactory.decodeResource(mContext.getResources(), id);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + 1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[1]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);
        bmp.recycle();
    }

    public void SetupScaling()
    {
        float screenWidth =  mContext.getResources().getDisplayMetrics().widthPixels;
        float screenHeight = mContext.getResources().getDisplayMetrics().heightPixels;

        scaleRationX = screenWidth / developigScreenWidth;
        scaleRationY = screenHeight / developingScreenHeight;

        scaleRation = scaleRationX > scaleRationY ? scaleRationY : scaleRationX;
    }

    public void SetupTriangle()
    {
        // We will need a randomizer
        Random rnd = new Random();

        // Our collection of vertices
        int numberOfObject = 30;
        vertices = new float[numberOfObject*4*3];

        // Create the vertex data
        for(int i=0;i<numberOfObject;i++)
        {
            int offset_x = rnd.nextInt((int)developigScreenWidth);
            int offset_y = rnd.nextInt((int)developingScreenHeight);

            // Create the 2D parts of our 3D vertices, others are default 0.0f
            vertices[(i*12) + 0] = offset_x;
            vertices[(i*12) + 1] = offset_y + (30.0f*scaleRation);
            vertices[(i*12) + 2] = 0f;

            vertices[(i*12) + 3] = offset_x;
            vertices[(i*12) + 4] = offset_y;
            vertices[(i*12) + 5] = 0f;

            vertices[(i*12) + 6] = offset_x + (30.0f*scaleRation);
            vertices[(i*12) + 7] = offset_y;
            vertices[(i*12) + 8] = 0f;

            vertices[(i*12) + 9] = offset_x + (30.0f*scaleRation);
            vertices[(i*12) + 10] = offset_y + (30.0f*scaleRation);
            vertices[(i*12) + 11] = 0f;
        }

        // The indices for all textured quads
        indices = new short[numberOfObject*6];
        int last = 0;
        for(int i=0;i<numberOfObject;i++)
        {
            // We need to set the new indices for the new quad
            indices[(i*6) + 0] = (short) (last + 0);
            indices[(i*6) + 1] = (short) (last + 1);
            indices[(i*6) + 2] = (short) (last + 2);
            indices[(i*6) + 3] = (short) (last + 0);
            indices[(i*6) + 4] = (short) (last + 2);
            indices[(i*6) + 5] = (short) (last + 3);

            // Our indices are connected to the vertices so we need to keep them
            // in the correct order.
            // normal quad = 0,1,2,0,2,3 so the next one will be 4,5,6,4,6,7
            last = last + 4;
        }

        // The vertex buffer.
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);
    }

    public void UpdateSprite()
    {
        // Get new transformed vertices
        vertices = sprite.GetTransformedVertices();

        // The vertex buffer.
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
    }

    public void processTouchEvent(MotionEvent event)
    {
        // Get the half of screen value
        int screenhalf = (int) (mScreenWidth / 2);
        int screenheightpart = (int) (mScreenHeight / 3);

        if(event.getX()<screenhalf)
        {
            // Left screen touch
            if(event.getY() < screenheightpart)
                sprite.Scale(-0.01f);
            else if(event.getY() < (screenheightpart*2))
                sprite.Translate(-10f, -10f);
            else
                sprite.Rotate(0.01f);
        }
        else
        {
            // Right screen touch
            if(event.getY() < screenheightpart)
                sprite.Scale(0.01f);
            else if(event.getY() < (screenheightpart*2))
                sprite.Translate(10f, 10f);
            else
                sprite.Rotate(-0.01f);
        }
    }
}
