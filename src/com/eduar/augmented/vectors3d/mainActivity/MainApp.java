package com.eduar.augmented.vectors3d.mainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.eduar.augmented.vectors3d.R; 


public class MainApp extends Activity implements View.OnClickListener {
	
	Button mButton;
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_app_layout);
		
		mButton = (Button) findViewById(R.id.button_start);
		mButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.button_start:
				startNextActivity();
				break;
		}
	}
	
	public void startNextActivity() {
		
		String packageName = getPackageName(); 
		//String className = packageName + ".app.FrameMarkers.FrameMarkers";
		String className = packageName + ".mainActivity.ActivityList";
		 
		Intent i = new Intent();
		i.setClassName(packageName, className);
		startActivity(i);
	}
}

