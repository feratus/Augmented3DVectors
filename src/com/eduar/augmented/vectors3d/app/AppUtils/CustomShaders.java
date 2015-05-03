package com.eduar.augmented.vectors3d.app.AppUtils;

public class CustomShaders {
	
	// **************************************************************************
	// BASIC
	public static final String BASIC_FRAGMENT_SHADER = "\n \n"
			+ "precision mediump float; \n"
			+ "uniform vec4 u_Color; \n"
			+ "varying vec4 v_Color; \n"
			+ "void main() \n " + "\n{ \n"
			+ "   gl_FragColor = u_Color; \n"
			+ "} \n";
	
	public static final String BASIC_VERTEX_SHADER = "\n \n"
			+ "uniform mat4 u_Matrix; \n"
			+ "\n"
			+ "attribute vec4 a_Position; \n"
			+ "attribute vec4 a_Color; \n"
			+ "\n"
			+ "varying vec4 v_Color; \n"
			+ "\n"
			+ "void main() \n{ \n"
			+ "// v_Color = a_Color; \n"   
			+"    gl_Position = u_Matrix * a_Position; \n"
			+ "   gl_PointSize = 10.0; \n"
			+ "} \n";
	
	// **************************************************************************
	// GRID
	public static final String GRID_FRAGMENT_SHADER = "\n \n"
			+ "precision mediump float; \n"
			+ "uniform vec4 u_Color; \n"
			+ "varying vec4 v_Color; \n"
			+ "void main() \n \n{ \n"
			+ "gl_FragColor = v_Color; \n"
			+ "} \n";
	
	public static final String GRID_VERTEX_SHADER = "\n \n"
			+ "uniform mat4 u_Matrix; \n"
			+ " \n"
			+ "attribute vec4 a_Position; \n"
			+ "attribute vec4 a_Color; \n"
			+ " \n"
			+ "varying vec4 v_Color; \n"
			+ " \n"
			+ "void main() \n{ \n"
			+ "   v_Color = a_Color; \n   "
			+ "   gl_Position = u_Matrix * a_Position; \n"
			+ "   gl_PointSize = 10.0; \n"
			+ "} \n";

	// **************************************************************************
}
