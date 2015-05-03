package com.eduar.augmented.vectors3d.app.GridSpace;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.eduar.augmented.vectors3d.app.AppUtils.AppConstants;
import com.eduar.augmented.vectors3d.app.AppUtils.CustomShaders;
import com.eduar.augmented.vectors3d.app.AppUtils.Grid;
import com.eduar.augmented.vectors3d.app.AppUtils.Plane;
import com.eduar.augmented.vuforia.SampleApplication.SampleApplicationSession;
import com.eduar.augmented.vuforia.SampleApplication.utils.SampleUtils;
//import com.qualcomm.vuforia.Marker;
//import com.qualcomm.vuforia.MarkerResult;
//import com.qualcomm.vuforia.MarkerTracker;
import com.qualcomm.vuforia.Renderer;
import com.qualcomm.vuforia.State;
import com.qualcomm.vuforia.Tool;
import com.qualcomm.vuforia.TrackableResult;
import com.qualcomm.vuforia.VIDEO_BACKGROUND_REFLECTION;
import com.qualcomm.vuforia.Vuforia;
//import com.qualcomm.vuforia.samples.SampleApplication.utils.CubeShaders;
//import com.qualcomm.vuforia.samples.SampleApplication.utils.SampleUtils;
//import com.qualcomm.vuforia.samples.VuforiaSamples.app.VectorUtils.CustomShaders;



import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

public class GridRenderer implements GLSurfaceView.Renderer{

	// Constants:
	private static final String LOGTAG = "GridRenderer";	
	static private float kScale = AppConstants.WORLD_SCALE;
	static private float kTranslate = 0f;
	
	// Vuforia:
	SampleApplicationSession vuforiaAppSession;
	GridActivity mActivity;
	
	public boolean mIsActive = false;
	
	// OpenGL ES 2.0 specific:	
	private int mShaderProgram;
	
	// Objects
	private Grid grid;
	private Plane plane;
	
	// -----------------------------------------------------------------------------------
	public GridRenderer(GridActivity activity, SampleApplicationSession session) {
		
		mActivity = activity;
		vuforiaAppSession = session;
	}

	// -----------------------------------------------------------------------------------
	
	
	// -----------------------------------------------------------------------------------	
	@Override
	public void onDrawFrame(GL10 arg0) {
		if (!mIsActive)
            return;        
        // Call our function to render content
        renderFrame();		
	}
	// -----------------------------------------------------------------------------------
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		Log.d(LOGTAG, "GLRenderer.onSurfaceChanged");
        
        // Call Vuforia function to handle render surface size changes:
        vuforiaAppSession.onSurfaceChanged(width, height);
		
	}
	
	// -----------------------------------------------------------------------------------
	// Called when the surface is created or recreated
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		Log.d(LOGTAG, "GLRenderer.onSurfaceCreated");
        
        // Call function to initialize rendering:
        initRendering();
        
        // Call Vuforia function to (re)initialize rendering after first use
        // or after OpenGL ES context was lost (e.g. after onPause/onResume):
        vuforiaAppSession.onSurfaceCreated();		
	}
	
	// -----------------------------------------------------------------------------------
	void initRendering() {
		
		Log.d(LOGTAG, "initRendering");
        
        // Define clear color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, Vuforia.requiresAlpha() ? 0.0f : 1.0f);
                
        // TODO
        mShaderProgram = SampleUtils.createProgramFromShaderSrc(
        		CustomShaders.BASIC_VERTEX_SHADER, CustomShaders.BASIC_FRAGMENT_SHADER);
        
        grid = new Grid(14);
        plane = new Plane();
	}	
	
	// -----------------------------------------------------------------------------------
	void renderFrame() {
		
		// Clear color and depth buffer
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
        // Get the state from Vuforia and mark the beginning of a rendering
        // section
        State state = Renderer.getInstance().begin();
        
        // Explicitly render the Video Background
        Renderer.getInstance().drawVideoBackground();
        
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        
        // We must detect if background reflection is active and adjust the
        // culling direction.
        // If the reflection is active, this means the post matrix has been
        // reflected as well,
        // therefore standard counter clockwise face culling will result in
        // "inside out" models.
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
        if (Renderer.getInstance().getVideoBackgroundConfig().getReflection() == VIDEO_BACKGROUND_REFLECTION.VIDEO_BACKGROUND_REFLECTION_ON)
            GLES20.glFrontFace(GLES20.GL_CW);  // Front camera
        else
            GLES20.glFrontFace(GLES20.GL_CCW);   // Back camera
            
        // Did we find any trackables this frame?
        for (int tIdx = 0; tIdx < state.getNumTrackableResults(); tIdx++)
        {
            // Get the trackable:
            TrackableResult trackableResult = state.getTrackableResult(tIdx);
            float[] modelViewMatrix = Tool.convertPose2GLMatrix(
                trackableResult.getPose()).getData();
                                    
            // Check the type of the trackable:
            //assert (trackableResult.getType() == MarkerTracker.getClassType());
            //MarkerResult markerResult = (MarkerResult) (trackableResult);
            //Marker marker = (Marker) markerResult.getTrackable();
            
            
            float[] modelViewProjection = new float[16];
            
            //if (mActivity.isFrontCameraActive())
            //    Matrix.rotateM(modelViewMatrix, 0, 180, 0.f, 1.0f, 0.f);
            
            Matrix.translateM(modelViewMatrix, 0, -kTranslate, -kTranslate, 0.f);
            Matrix.scaleM(modelViewMatrix, 0, kScale, kScale, kScale);
            Matrix.rotateM(modelViewMatrix, 0, -90, 0, 0, 1);
            Matrix.multiplyMM(modelViewProjection, 0, vuforiaAppSession
                .getProjectionMatrix().getData(), 0, modelViewMatrix, 0);
            
            ///********* 
            GLES20.glUseProgram(mShaderProgram);
            
            grid.setSideSize(12);
            grid.drawGrid(modelViewProjection, mShaderProgram);
            
            plane.getDataLocation(mShaderProgram);
            plane.setUniforms(modelViewProjection);
            plane.draw(Color.rgb(230, 211, 211));
            
            //*********
            SampleUtils.checkGLError("In render frame"); 
            
        }
        
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        
        Renderer.getInstance().end();
	}
	
	// -----------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------
	// GETTERS
	public Grid getGrid() {
		return this.grid;
	}
	
	// -----------------------------------------------------------------------------------
}
