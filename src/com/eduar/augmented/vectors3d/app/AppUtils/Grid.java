package com.eduar.augmented.vectors3d.app.AppUtils;

import android.graphics.Color;
import android.opengl.Matrix;
import android.util.Log;

//import com.qualcomm.vuforia.samples.VuforiaSamples.app.VectorUtils.Arrow;
//import com.qualcomm.vuforia.samples.VuforiaSamples.app.VectorUtils.Line;

public class Grid {
	
	private String MSG = "Grid";
	
	private float delta;
	private boolean showAxis;
	private boolean showXZ;
	private boolean showYZ;
	private int sideSize;
	
	// -----------------------------------------------------------------------------------
	public Grid(int size) {
		
		if (size % 2 != 0)
			size++;
		
	    this.sideSize = size;
	    this.delta = 0.1F;
	    
	    this.showAxis = true;
	    this.showXZ = true;
	    this.showYZ = true;
	}
	
	// -----------------------------------------------------------------------------------
	public void drawGrid(float[] modelViewProjection, int shaderProgram) {
		
		// -----------------------------------------
		if (showAxis) {
			drawAxis(modelViewProjection, shaderProgram);
		}
		if (showXZ) {
			drawXZPlane(modelViewProjection, shaderProgram);
		}
		if (showYZ) {
			drawYZPlane(modelViewProjection, shaderProgram);
		}
		
		// -----------------------------------------		
		int color = 0;
		Line line;
		float halfSide = sideSize / 2.0f * delta;
		int width = 0;
		
		for (int dim = 0; dim < 2; dim++) { // dimension X or Y			
			
			for (float xy = -halfSide; xy <= halfSide; xy += delta) {
				
				if (dim == 0) {
					line = new Line(xy, halfSide, 0.0f, xy, -halfSide, 0.0f);
					color = Color.rgb(96, 232, 8);
				} else { 
					line = new Line(halfSide, xy, 0.0f, -halfSide, xy, 0.0f);
					color = Color.rgb(232, 52, 14);
				}
				
				// line width...
				xy = Math.round(xy * 10.0f) / 10.0f;
				width = 2;				
				if (xy == -halfSide || xy == halfSide)
					width = 5;				
				if ( xy == 0.0f)					
					width = 6;
				
				// draw the line
				line.getDataLocation(shaderProgram);
				line.setUniforms(modelViewProjection);
				line.draw(color, width);
			}
		}
		
	}
	
	// -----------------------------------------------------------------------------------
	public void drawXZPlane(float[] modelViewProjection, int shaderProgram) {
		
		int color = 0;
		Line line;
		float halfSide = sideSize / 2.0f * delta;
		int width = 0;
		
		for (int dim = 0; dim < 2; dim++) { // dimension: X or Z			
			
			for (float xz = -halfSide; xz <= halfSide; xz += delta) {
				
				// line width...
				xz = Math.round(xz * 10.0f) / 10.0f;
				width = 2;				
				if (xz == -halfSide || xz == halfSide)
					width = 5;				
				if ( xz == 0.0f)					
					width = 6;
				
				if (dim == 0) {
					if (xz >= 0.0f) {
						line = new Line(halfSide, 0.0f, xz, -halfSide, 0.0f, xz);
						color = Color.rgb(232, 52, 14);
						// draw the line
						line.getDataLocation(shaderProgram);
						line.setUniforms(modelViewProjection);
						line.draw(color, width);
					}
				} else { 
					line = new Line(xz, 0.0f, 0.0f, xz, 0.0f, halfSide);
					color = Color.rgb(96, 52, 232);
					// draw the line
					line.getDataLocation(shaderProgram);
					line.setUniforms(modelViewProjection);
					line.draw(color, width);
				}
				
			}
		}
	}
	
	// -----------------------------------------------------------------------------------
	public void drawYZPlane(float[] modelViewProjection, int shaderProgram) {
		
		int color = 0;
		Line line;
		float halfSide = sideSize / 2.0f * delta;
		int width = 0;
		
		for (int dim = 0; dim < 2; dim++) { // dimension: X or Z			
			
			for (float yz = -halfSide; yz <= halfSide; yz += delta) {
				
				// line width...
				yz = Math.round(yz * 10.0f) / 10.0f;
				width = 2;				
				if (yz == -halfSide || yz == halfSide)
					width = 5;				
				if ( yz == 0.0f)					
					width = 6;
				
				if (dim == 0) {
					if (yz >= 0.0f) {
						line = new Line(0.0f, halfSide, yz, 0.0f, -halfSide, yz);
						color = Color.rgb(96, 232, 8);
						// draw the line
						line.getDataLocation(shaderProgram);
						line.setUniforms(modelViewProjection);
						line.draw(color, width);
					}
				} else { 
					line = new Line(0.0f, yz, 0.0f, 0.0f, yz, halfSide);
					color = Color.rgb(96, 52, 232);
					// draw the line
					line.getDataLocation(shaderProgram);
					line.setUniforms(modelViewProjection);
					line.draw(color, width);
				}
				
			}
		}
	}
	
	// -----------------------------------------------------------------------------------
	public void drawAxis(float[] modelViewProjection, int shaderProgram) {
		float f = 1.0F * this.delta;
	    float[] modelMatrix1 = new float[16];
	    float[] modelMatrix2 = new float[16];
	    
	    Line lineX = new Line(0.0F, 0.0F, 0.0F, f, 0.0F, 0.0F);
	    lineX.getDataLocation(shaderProgram);
	    lineX.setUniforms(modelViewProjection);
	    lineX.draw(Color.rgb(255, 0, 0), 10);
	    
	    
	    Matrix.setIdentityM(modelMatrix1, 0);
	    Matrix.rotateM(modelMatrix1, 0, 90.0F, 0.0F, 1.0F, 0.0F);
	    Matrix.multiplyMM(modelMatrix2, 0, modelViewProjection, 0, modelMatrix1, 0);
	    /*
	    Arrow xAxis = new Arrow(0.006F, f, 40);	    
	    xAxis.getDataLocation(shaderProgram);
	    xAxis.setUniforms(modelMatrix2);
	    xAxis.draw(Color.rgb(255, 0, 0));
	    */
	    Line lineY = new Line(0.0F, 0.0F, 0.0F, 0.0F, f, 0.0F);
	    lineY.getDataLocation(shaderProgram);
	    lineY.setUniforms(modelViewProjection);
	    lineY.draw(Color.rgb(0, 255, 0), 10);
	    
	    
	    Matrix.setIdentityM(modelMatrix1, 0);
	    Matrix.rotateM(modelMatrix1, 0, -90.0F, 1.0F, 0.0F, 0.0F);
	    Matrix.multiplyMM(modelMatrix2, 0, modelViewProjection, 0, modelMatrix1, 0);
	    /*
	    Arrow yArrow = new Arrow(0.006F, f, 40);	    
	    yArrow.getDataLocation(shaderProgram);
	    yArrow.setUniforms(modelMatrix2);
	    yArrow.draw(Color.rgb(0, 255, 0));
	    */
	    Line lineZ = new Line(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, f);
	    lineZ.getDataLocation(shaderProgram);
	    lineZ.setUniforms(modelViewProjection);
	    lineZ.draw(Color.rgb(0, 0, 255), 10);
	    /*
	    Arrow zArrow = new Arrow(0.006F, f, 40);
	    zArrow.getDataLocation(shaderProgram);
	    zArrow.setUniforms(modelViewProjection);
	    zArrow.draw(Color.rgb(0, 0, 255));
	    */
	}
	
	//---------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------
	// GETTERS
	public int getSideSize() {
		return this.sideSize;
	}
	// -----------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------
	// SETTERS
	public void setDelta(float delta)
	{
	    this.delta = delta;
	    Log.d(MSG, "setDelta");
	}
	// -----------------------------------------------------------------------------------
	public void setShowAxis(boolean showAxis)
	{
		this.showAxis = showAxis;
		Log.d(MSG, "showAxis");
	}
	// -----------------------------------------------------------------------------------
	public void setShowXZ(boolean showAxis)
	{
	    this.showXZ = showAxis;
	    Log.d(MSG, "showXZ");
	}
	// -----------------------------------------------------------------------------------
	public void setShowYZ(boolean showAxis)
	{
	    this.showYZ = showAxis;
	    Log.d(MSG, "showYZ");
	}
	// -----------------------------------------------------------------------------------
	public void setSideSize(int side)
	{
	    if (side % 2 != 0)
	    	side++;
	    this.sideSize = side;
	}
	// -----------------------------------------------------------------------------------
}
