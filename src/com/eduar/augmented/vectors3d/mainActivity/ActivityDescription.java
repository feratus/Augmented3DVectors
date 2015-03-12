package com.eduar.augmented.vectors3d.mainActivity;

import com.eduar.augmented.vectors3d.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

//import android.view.View;
//import android.view.View.OnClickListener;
//import android.webkit.WebView;
//import android.widget.Button;
//import android.widget.TextView;


public class ActivityDescription extends FragmentActivity 
							  implements ActivityDescriptionFragment.OnButtonPressedListener 
							  {

	// -----------------------------------------------------------------------------------
	// the category and description index
	int mCategoryIndex, mDescriptionIndex;
	
	private String mClassToLaunch;
	private String mClassToLaunchPackage;
	
	ActivityDescriptionFragment mDescriptionFragment;
	
	// -----------------------------------------------------------------------------------
	/**
	 * Sets up the activity
	 * 
	 * Setting up the activity means reading the category/description index from the Intent
	 * that fired up this Activity and loading it onto the UI. It is also detected if there
	 * has been a screen configuration change (in particular, a rotation) that makes this
	 * activity unnecessary, in which case the activity is put out of the way. 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// if the app is in two-pane layout mode, this activity is no longer necessary
		if (getResources().getBoolean(R.bool.has_two_panes)) {
			finish();
			return;
		}
		
		setContentView(R.layout.description_act_screen);
		
		String webText = getIntent().getExtras().getString("ABOUT_TEXT");
		mClassToLaunchPackage = getPackageName();
		mClassToLaunch = 
				getPackageName() + "." + getIntent().getExtras().getString("ACTIVITY_TO_LAUNCH");
		
		// Place a DescriptionFragment as the content pane
		//descripFragment = new DescriptionFragment();
		//getSupportFragmentManager().beginTransaction().add(android.R.id.content, descripFragment).commit();
		mDescriptionFragment = (ActivityDescriptionFragment) 
				getSupportFragmentManager().findFragmentById(R.id.description_fragment);
		
		// set the arguments gathered from the intent		
		mDescriptionFragment.setArguments(webText);
		//
				
		// Display the correct description on the fragment
		// ,,,
		mDescriptionFragment.displayDescription();
		mDescriptionFragment.setOnButtonPressedListener( this);
		
	}

	// -----------------------------------------------------------------------------------
	@Override
	public void onButtonPressed() {
		
		//Log.d("DescriptionActivity", "Button Pressed!!!");
		Log.d("DescriptionActivity", mClassToLaunchPackage);
		Log.d("DescriptionActivity", mClassToLaunch);
		
		Intent i = new Intent();		
		i.setClassName(mClassToLaunchPackage, mClassToLaunch);
		startActivity(i);
		
	}
	// -----------------------------------------------------------------------------------
	

}
