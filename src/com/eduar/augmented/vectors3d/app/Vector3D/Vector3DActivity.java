package com.eduar.augmented.vectors3d.app.Vector3D;

import com.eduar.augmented.vuforia.SampleApplication.SampleApplicationControl;
import com.eduar.augmented.vuforia.SampleApplication.SampleApplicationException;
import com.eduar.augmented.vuforia.SampleApplication.SampleApplicationSession;
import com.qualcomm.vuforia.State;

import android.app.Activity;

public class Vector3DActivity extends Activity 
							implements SampleApplicationControl {

	private static final String LOGTAG = "Vector3DActivity";
	
	// -------- Variables -----------
	SampleApplicationSession vuforiaAppSession;

	// -----------------------------------------------------------------------------------
	@Override
	public boolean doInitTrackers() {
		// TODO Auto-generated method stub
		return false;
	}

	// -----------------------------------------------------------------------------------
	@Override
	public boolean doLoadTrackersData() {
		// TODO Auto-generated method stub
		return false;
	}

	// -----------------------------------------------------------------------------------
	@Override
	public boolean doStartTrackers() {
		// TODO Auto-generated method stub
		return false;
	}

	// -----------------------------------------------------------------------------------
	@Override
	public boolean doStopTrackers() {
		// TODO Auto-generated method stub
		return false;
	}

	// -----------------------------------------------------------------------------------
	@Override
	public boolean doUnloadTrackersData() {
		// TODO Auto-generated method stub
		return false;
	}

	// -----------------------------------------------------------------------------------
	@Override
	public boolean doDeinitTrackers() {
		// TODO Auto-generated method stub
		return false;
	}

	// -----------------------------------------------------------------------------------
	@Override
	public void onInitARDone(SampleApplicationException e) {
		// TODO Auto-generated method stub
		
	}

	// -----------------------------------------------------------------------------------
	@Override
	public void onQCARUpdate(State state) {
		// TODO Auto-generated method stub
		
	}
	
	// -----------------------------------------------------------------------------------
}
