package com.eduar.augmented.vectors3d.app.AppUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.graphics.Color;
import android.opengl.GLES20;

public class Plane {
	
	private static final int POSITION_COMPONENT_COUNT = 3;
	private static final int BYTES_PER_FLOAT = 4;
	private static final int STRIDE = 12;
	
	private FloatBuffer vertexData;
	
	private int aPositionLocation;	
	private int uColorLocation;
	private int uMatrixLocation; 
	
	// -----------------------------------------------------------------------------------
	public Plane()
	{
		float[] planeVertices = {
				0.0f,  0.0f,  0.0f,
			   -0.7f, -0.7f,  0.0f,
			    0.7f, -0.7f,  0.0f,
			    0.7f,  0.7f,  0.0f,
			   -0.7f,  0.7f,  0.0f,
			   -0.7f, -0.7f,  0.0f
		};
		
	    vertexData = ByteBuffer.allocateDirect(planeVertices.length * BYTES_PER_FLOAT)
	    		.order(ByteOrder.nativeOrder())
	    		.asFloatBuffer()
	    		.put(planeVertices);	    
	}
	
	// -----------------------------------------------------------------------------------
	private void bindData()
	{
		vertexData.position(0);
	    GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, 
	    		GLES20.GL_FLOAT, false, STRIDE, vertexData);
	    GLES20.glEnableVertexAttribArray(aPositionLocation);
	    vertexData.position(0);
  	}
	
	// -----------------------------------------------------------------------------------
	public void draw(int color)
	{
	    draw(color, 0.4F);
	}

	// -----------------------------------------------------------------------------------
	public void draw(int color, float paramFloat)
	{
	    bindData();
	    GLES20.glEnable(3042);
	    GLES20.glBlendFunc(770, 771);
	    
	    GLES20.glUniform4f(uColorLocation, 
	    		Color.red(color) / 255.0F, 
	    		Color.green(color) / 255.0F, 
	    		Color.blue(color) / 255.0F, 
	    		paramFloat);
	    
	    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);
	    GLES20.glDisable(GLES20.GL_BLEND); // *****?
	    GLES20.glDisableVertexAttribArray(aPositionLocation);
	}

	// -----------------------------------------------------------------------------------
	public void getDataLocation(int program)
	{
	    uMatrixLocation = GLES20.glGetUniformLocation(program, "u_Matrix");
	    uColorLocation = GLES20.glGetUniformLocation(program, "u_Color");
	    aPositionLocation = GLES20.glGetAttribLocation(program, "a_Position");
	}

	// -----------------------------------------------------------------------------------
	public void setUniforms(float[] modelViewProjection)
	{
	    GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, modelViewProjection, 0);
	}
	
	// -----------------------------------------------------------------------------------
}
