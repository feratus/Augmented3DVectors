package com.eduar.augmented.vectors3d.app.GridSpace;

import java.util.ArrayList;

import com.eduar.augmented.vuforia.SampleApplication.SampleApplicationControl;
import com.eduar.augmented.vuforia.SampleApplication.SampleApplicationException;
import com.eduar.augmented.vuforia.SampleApplication.SampleApplicationSession;
import com.eduar.augmented.vuforia.SampleApplication.utils.LoadingDialogHandler;
import com.eduar.augmented.vuforia.SampleApplication.utils.SampleApplicationGLView;
import com.qualcomm.vuforia.CameraDevice;
import com.qualcomm.vuforia.Marker;
import com.qualcomm.vuforia.MarkerTracker;
import com.qualcomm.vuforia.State;
import com.qualcomm.vuforia.Tracker;
import com.qualcomm.vuforia.TrackerManager;
import com.qualcomm.vuforia.Vec2F;
import com.qualcomm.vuforia.Vuforia;
import com.eduar.augmented.vectors3d.R;
import com.eduar.augmented.vectors3d.app.AppUI.SettingsDialogFragment;
//import com.eduar.augmented.vectors3d.app.AppUI.SettingsDialogFragment.SettingsDialogListener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class GridActivity extends FragmentActivity 
					   implements 	OnClickListener,
					   				SettingsDialogFragment.SettingsDialogListener,
					   				SampleApplicationControl {

	private static final String LOGTAG = "GridActivity";
	
	// -----------------------------------------------------------------------------------
	// -------- Variables -----------
	SampleApplicationSession vuforiaAppSession;
	
	// the OpenGL view:
	private SampleApplicationGLView mGLView;
	// the Renderer:
	private GridRenderer mRenderer;
	
	private RelativeLayout mUILayout;			// import android.widget.RelativeLayout
	private Marker dataSet[];					// import com.qualcomm.vuforia.Marker
	private GestureDetector mGestureDetector;	// import android.view.GestureDetector
	
	//private SampleAppMenu mSampleAppMenu;	//
	
	// Menu Variables:
	private boolean mFlash = false;
	private boolean mContAutofocus = false;
	private boolean mIsFrontCameraActive = true;
	
	private boolean mShowAxis = true;
	private boolean mShowXZPlane = true;
	private boolean mShowYZPlane = true;
	
	private TextView mTextView;
	
	private View mFlashOptionView;
	
	private LoadingDialogHandler loadingDialogHandler = new LoadingDialogHandler(this);
	
	boolean mIsDroidDevice = false;
	
	ArrayList<Integer> mSelectedItems;
	
	SettingsDialogFragment mDialog;
	
	// -----------------------------------------------------------------------------------
	// -------- Inner Class ----------
	// Process Single Tap event to trigger autofoucs
	private class GestureListener extends GestureDetector.SimpleOnGestureListener {
		
		// Used to set autofocus one second after a manual focus is triggered
		private final Handler autofocusHandler = new Handler();
		
		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}
		
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// Generates a Handler to a trigger autofocus
			// after 1 second
			autofocusHandler.postDelayed(new Runnable()
				{
					public void run() {
						boolean result = CameraDevice.getInstance().setFocusMode(
							CameraDevice.FOCUS_MODE.FOCUS_MODE_TRIGGERAUTO);
						if(!result)
							Log.e("SingleTapUp", "Unable to trigger focus");
					}
				}
				, 1000L);
			return true;
		}
	}
	
	// -----------------------------------------------------------------------------------
	// Called when the activity first starts of the user navigates back to an activity
	@Override
	protected void onCreate(Bundle savedInstanceState){
		
		Log.d(LOGTAG, "onCerate");
		super.onCreate(savedInstanceState);
		
		//
		vuforiaAppSession = new SampleApplicationSession(this);
		
		//
		startLoadingAnimation();
		
		// pass the screen rotation to the SampleApplicationSession object:
		//vuforiaAppSession.initAR(this, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		vuforiaAppSession.initAR(this, getResources().getConfiguration().orientation);
		
		//
		mGestureDetector = new GestureDetector(this, new GestureListener());
		
		//
		mIsDroidDevice = android.os.Build.MODEL.toLowerCase().startsWith("droid");
		
		//
		mDialog = new SettingsDialogFragment();
		mDialog.setSettingsDialogListener(this);
		
		String[] items = {"One", "Two", "Three"};
		boolean[] checkedItems = {true, true, true};
		mDialog.setListContent(items, checkedItems);
	}
			
	// -----------------------------------------------------------------------------------
	// Called when the activity starts the interaction with the user
	@Override
	protected void onResume() {
		
		Log.d(LOGTAG, "onResume");
		super.onResume();
		
		// this is needed for some Droid devices to force portrait
		if (mIsDroidDevice) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		
		try {			
			vuforiaAppSession.resumeAR();
		} catch (SampleApplicationException e) {
			Log.e(LOGTAG, e.getString());
		}
		
		// Resume the GL View
		if (mGLView != null) {
			mGLView.setVisibility(View.VISIBLE);
			mGLView.onResume();
		}
	}
	
	// -----------------------------------------------------------------------------------
	@Override
	public void onConfigurationChanged(Configuration config) {
		
		Log.d(LOGTAG, "onConfigurationChanged");
		super.onConfigurationChanged(config);
		
		vuforiaAppSession.onConfigurationChanged();
	}
	
	// -----------------------------------------------------------------------------------
	@Override
	protected void onPause() {
		
		Log.d(LOGTAG, "onPause");
		super.onPause();
		
		if (mGLView != null) {
			mGLView.setVisibility(View.INVISIBLE);
			mGLView.onPause();
		}
		
		// turn off the flash
		/*if (mFlashOptionView != null && mFlash) {
			// OnCheckedChangeListener is called upon changing the checked state
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
				( (Switch) mFlashOptionView).setChecked(false);
			} else {
				((CheckBox) mFlashOptionView).setChecked(false);
			}
		}*/
		
		try {
			vuforiaAppSession.pauseAR();
		} catch (SampleApplicationException e) {
			Log.e(LOGTAG, e.getString());
		}
	}
	
	// -----------------------------------------------------------------------------------
	// The final call you receive before your activity is destroyed
	@Override
	protected void onDestroy() {
		
		Log.d(LOGTAG, "onDestroy");
		super.onDestroy();
		
		try {
			vuforiaAppSession.stopAR();
		} catch (SampleApplicationException e) {
			Log.e(LOGTAG, e.getString());
		}
		
		System.gc(); 	// indicates to the VM to run the Garbage Collector
	}
	
	// -----------------------------------------------------------------------------------
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Process the Gestures
		//if (mSampleAppMenu != null && mSampleAppMenu.processEvent(event))
			//return true;
		
		return mGestureDetector.onTouchEvent(event);
	}
	
	// -----------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------
	private void startLoadingAnimation() {
		LayoutInflater inflater = LayoutInflater.from(this);
        mUILayout = (RelativeLayout) inflater.inflate(R.layout.camera_overlay, null, false);
		
        mUILayout.setVisibility(View.VISIBLE);
        mUILayout.setBackgroundColor(Color.BLACK);
        
        // Gets a reference to the loading dialog
        loadingDialogHandler.mLoadingDialogContainer = mUILayout
            .findViewById(R.id.loading_indicator);
        
        // Shows the loading indicator at start
        loadingDialogHandler
            .sendEmptyMessage(LoadingDialogHandler.SHOW_LOADING_DIALOG);
        
        // Adds the inflated layout to the view
        addContentView(mUILayout, 
        		new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	// -----------------------------------------------------------------------------------
	// Methods from the implemented Interface
	// -----------------------------------------------------------------------------------
	@Override
	public boolean doInitTrackers() {
		// Indicate if the trackers were initialized correctly
        boolean result = true;
        
        // Initialize the marker tracker:
        TrackerManager trackerManager = TrackerManager.getInstance();
        Tracker trackerBase = trackerManager.initTracker(MarkerTracker
            .getClassType());
        MarkerTracker markerTracker = (MarkerTracker) (trackerBase);
        
        if (markerTracker == null)
        {
            Log.e(
                LOGTAG,
                "Tracker not initialized. Tracker already initialized or the camera is already started");
            result = false;
        } else
        {
            Log.i(LOGTAG, "Tracker successfully initialized");
        }
        
        return result;
	}

	// -----------------------------------------------------------------------------------
	@Override
	public boolean doLoadTrackersData() {
		
		TrackerManager tManager = TrackerManager.getInstance();
        MarkerTracker markerTracker = (MarkerTracker) tManager
            .getTracker(MarkerTracker.getClassType());
        if (markerTracker == null)
            return false;
        
        dataSet = new Marker[1];
        
        dataSet[0] = markerTracker.createFrameMarker(3, "Base", new Vec2F(50, 50));
        if (dataSet[0] == null)
        {
            Log.e(LOGTAG, "Failed to create frame marker Q.");
            return false;
        }        
        
        Log.i(LOGTAG, "Successfully initialized MarkerTracker.");
        
        return true;
	}

	// -----------------------------------------------------------------------------------
	@Override
	public boolean doStartTrackers() {
		// Indicate if the trackers were started correctly
        boolean result = true;
        
        TrackerManager tManager = TrackerManager.getInstance();
        MarkerTracker markerTracker = (MarkerTracker) tManager
            .getTracker(MarkerTracker.getClassType());
        if (markerTracker != null)
            markerTracker.start();
        
        return result;
	}

	// -----------------------------------------------------------------------------------
	@Override
	public boolean doStopTrackers() {
		// Indicate if the trackers were stopped correctly
        boolean result = true;
        
        TrackerManager tManager = TrackerManager.getInstance();
        MarkerTracker markerTracker = (MarkerTracker) tManager
            .getTracker(MarkerTracker.getClassType());
        if (markerTracker != null)
            markerTracker.stop();
        
        return result;
	}

	// -----------------------------------------------------------------------------------
	@Override
	public boolean doUnloadTrackersData() {
		// Indicate if the trackers were unloaded correctly
        boolean result = true;
        
        return result;
	}

	// -----------------------------------------------------------------------------------
	@Override
	public boolean doDeinitTrackers() {
		// Indicate if the trackers were deinitialized correctly
        boolean result = true;
        
        TrackerManager tManager = TrackerManager.getInstance();
        tManager.deinitTracker(MarkerTracker.getClassType());
        
        return result;
	}

	// -----------------------------------------------------------------------------------
	@Override
	public void onInitARDone(SampleApplicationException exception) {
		
		if (exception == null)
        {
            initApplicationAR();
            
            mRenderer.mIsActive = true;
            
            // Now add the GL surface view. It is important
            // that the OpenGL ES surface view gets added
            // BEFORE the camera is started and video
            // background is configured.
            addContentView(mGLView, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
            
            // Sets the UILayout to be drawn in front of the camera
            mUILayout.bringToFront();
            
            // Hides the Loading Dialog
            loadingDialogHandler
                .sendEmptyMessage(LoadingDialogHandler.HIDE_LOADING_DIALOG);
            
            // Sets the layout background to transparent
            mUILayout.setBackgroundColor(Color.TRANSPARENT);
            
            try
            {
                vuforiaAppSession.startAR(CameraDevice.CAMERA.CAMERA_DEFAULT);
            } catch (SampleApplicationException e)
            {
                Log.e(LOGTAG, e.getString());
            }
            
            boolean result = CameraDevice.getInstance().setFocusMode(
                CameraDevice.FOCUS_MODE.FOCUS_MODE_CONTINUOUSAUTO);
            
            if (result)
                mContAutofocus = true;
            else
                Log.e(LOGTAG, "Unable to enable continuous autofocus");
            
            /*
            mSampleAppMenu = new SampleAppMenu(this, this, "Grilla",
                mGlView, mUILayout, null);
            setSampleAppMenuSettings();
            */
            
            // TODO --- add objects to the UI Layout
            RelativeLayout.LayoutParams rlp = //new RelativeLayout.LayoutParams(-2, -2);
            		new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
            				RelativeLayout.LayoutParams.WRAP_CONTENT);
            //rlp.addRule(RelativeLayout.ALIGN_RIGHT, RelativeLayout.TRUE);
            rlp.addRule(11, -1);
            rlp.addRule(10, -1);
            
            /*mTextView = new TextView(this);
            mTextView.setText("GRID Activity");            
            mUILayout.addView(mTextView, rlp);
            */            
            //RelativeLayout rl = (RelativeLayout) findViewById(R.id.head_bar_layout);
            //mUILayout.addView(rl, rlp);
            
            Button btn = new Button(this);
            btn.setText(R.string.settings_button);
            btn.setOnClickListener(this);
            
            mUILayout.addView(btn, rlp);
            
        } else
        {
            Log.e(LOGTAG, exception.getString());
            finish();
        }
	}

	// -----------------------------------------------------------------------------------
	@Override
	public void onQCARUpdate(State state) {
		// TODO Auto-generated method stub
		
	}

	// -----------------------------------------------------------------------------------
	// To initialize AR application components
	private void initApplicationAR()
    {
        // Create OpenGL ES view:
        int depthSize = 16;
        int stencilSize = 0;
        boolean translucent = Vuforia.requiresAlpha();
        
        mGLView = new SampleApplicationGLView(this);
        mGLView.init(translucent, depthSize, stencilSize);
        
        mRenderer = new GridRenderer(this, vuforiaAppSession);        
        mGLView.setRenderer(mRenderer);
        
    }

	// -----------------------------------------------------------------------------------
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		// custom AlertDialog		
		//AlertDialog dialog = (AlertDialog) createDialog();//adBuilder.create();
		//dialog.show();
		FragmentManager fm = getSupportFragmentManager();
		mDialog.show(fm, "Other Title");
				
	}
	
	// -----------------------------------------------------------------------------------	
	/*public Dialog createDialog ( ) {
		
		mSelectedItems = new ArrayList();  // Where we track the selected items
		String[] itemsArray = new String[] {
											"Ver Ejes",
											"Ver Plano XZ",
											"Ver Plano YZ"
		}; 
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.settings_button)
		// 
			.setMultiChoiceItems(itemsArray, null, new DialogInterface.OnMultiChoiceClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {
					// TODO Auto-generated method stub
					if (isChecked) {
                       // If the user checked the item, add it to the selected items
                       mSelectedItems.add(which);
                   } else if (mSelectedItems.contains(which)) {
                       // Else, if the item is already in the array, remove it 
                       mSelectedItems.remove(Integer.valueOf(which));
                   }
				}
			})
		// Action buttons
			.setPositiveButton("Ok", new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub					
				}
			})
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub					
				}
			});
		
		return builder.create();
	}*/

	// -----------------------------------------------------------------------------------
	/**
	 * Called when the 'OK' button is selected
	 * 
	 * This is called by the GridActivitie (via its listener interface) to notify
	 * that a change (or changes) was done in the fragment options menu. 
	 * 
	 */
	@Override
	public void onProcessSettings(boolean[] checkList) {
		// TODO Auto-generated method stub
				
		for (int i=0; i<checkList.length; i++) {
			switch (i) {
				case 0:
					mShowAxis = checkList[i]; // !mShowAxis;
					mGLView.queueEvent(new Runnable() {
						@Override
						public void run() {
							mRenderer.getGrid().setShowAxis(mShowAxis);
						}
					});
					break;
				case 1:
					mShowXZPlane = checkList[i]; // !mShowXZPlane;
					mGLView.queueEvent(new Runnable() {
						@Override
						public void run() {
							mRenderer.getGrid().setShowXZ(mShowXZPlane);
						}
					});
					Log.e("", "X Z :: " + mShowXZPlane);
					break;
				case 2:
					mShowYZPlane = checkList[i];  // !mShowYZPlane;
					mGLView.queueEvent(new Runnable() {
						@Override
						public void run() {
							mRenderer.getGrid().setShowYZ(mShowYZPlane);
						}
					});
					Log.e("", "Y Z :: " + mShowYZPlane);

					break;
			}
		}
	}
	// -----------------------------------------------------------------------------------
}
