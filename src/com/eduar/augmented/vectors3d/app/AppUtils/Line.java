package com.eduar.augmented.vectors3d.app.AppUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.graphics.Color;
import android.opengl.GLES20;

public class Line {

	private static final int BYTES_PER_FLOAT = 4;
	private static final int POSITION_COMPONENT_COUNT = 3;
	private static final int STRIDE = 0;
	
	private FloatBuffer vertexData;
	
	private int aPositionLocation;	
	private int uColorLocation;
	private int uMatrixLocation;
	
	private float x1, y1, z1;
	private float x2, y2, z2;
	
	// -----------------------------------------------------------------------------------
	public Line(float xh, float yh, float zh, float xt, float yt, float zt)
	{		
		setLineData(xh, yh, zh, xt, yt, zt);
	}
	
	// -----------------------------------------------------------------------------------
	public void setLineData(float xh, float yh, float zh, float xt, float yt, float zt)
	{
	    this.x1 = xh;
	    this.y1 = yh;
	    this.z1 = zh;
	    this.x2 = xt;
	    this.y2 = yt;
	    this.z2 = zt;
	    
	    transferLineData();
	}
	
	// -----------------------------------------------------------------------------------
	private void transferLineData()
  	{		
	    float[] dataLine = createLineData();
	    vertexData = ByteBuffer
	    		.allocateDirect(dataLine.length * BYTES_PER_FLOAT)
	    		.order(ByteOrder.nativeOrder())
	    		.asFloatBuffer()
	    		.put(dataLine);
  	}
	
	// -----------------------------------------------------------------------------------
	private void bindData()
	{
	    vertexData.position(0); // 5126
	    GLES20.glVertexAttribPointer(aPositionLocation,	    								
	    		POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, STRIDE, vertexData);	    
	    GLES20.glEnableVertexAttribArray(aPositionLocation);
	    vertexData.position(0);
	}
	
	// -----------------------------------------------------------------------------------
	private float[] createLineData()
	{
	    float[] arrayOfFloat = new float[6];
	    int i = 0 + 1;
	    arrayOfFloat[0] = this.x1;
	    int j = i + 1;
	    arrayOfFloat[i] = this.y1;
	    int k = j + 1;
	    arrayOfFloat[j] = this.z1;
	    int m = k + 1;
	    arrayOfFloat[k] = this.x2;
	    int n = m + 1;
	    arrayOfFloat[m] = this.y2;
	    //(n + 1);
	    arrayOfFloat[n] = this.z2;
	    return arrayOfFloat;
	}
		
	// -----------------------------------------------------------------------------------
	public void draw(int color, int lineWidth)
	{
	    bindData();
	    GLES20.glLineWidth(lineWidth);
	    GLES20.glUniform4f(uColorLocation, 
	    		Color.red(color) / 255.0F, 
	    		Color.green(color) / 255.0F, 
	    		Color.blue(color) / 255.0F, 1.0F);
	    GLES20.glDrawArrays(1, 0, 2);
	    GLES20.glDisableVertexAttribArray(aPositionLocation);
	}
	
	// -----------------------------------------------------------------------------------
	public void getDataLocation(int shaderProgram)
	{
	    this.uMatrixLocation = GLES20.glGetUniformLocation(shaderProgram, "u_Matrix");
	    this.uColorLocation = GLES20.glGetUniformLocation(shaderProgram, "u_Color");
	    this.aPositionLocation = GLES20.glGetAttribLocation(shaderProgram, "a_Position");
	}

	// -----------------------------------------------------------------------------------
	public void setUniforms(float[] modelViewProjection)
	{
		GLES20.glUniformMatrix4fv(this.uMatrixLocation, 1, false, modelViewProjection, 0);
	}

	// -----------------------------------------------------------------------------------
	
}
