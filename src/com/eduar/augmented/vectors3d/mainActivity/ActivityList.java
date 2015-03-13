package com.eduar.augmented.vectors3d.mainActivity;

import com.eduar.augmented.vectors3d.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;


public class ActivityList extends FragmentActivity 
					   implements OnClickListener,
					   			  ActivityListFragment.OnItemSelectedListener
					   {

	// Fragments
	ActivityListFragment mListFragment;
	// DesctiptionFragment mDescriptonFragment;
	
	// to check the availability of dual-pane
	boolean mIsDualPane = false;
	
	// the index currently being displayed
	int mListIndex, mCatIndex;
	
	// List of Activities::
	private String mActivitiesList[] = { 
			" Introducción",						// 1 
			" Sistema de Coordenadas",				// 2
			" Vector en 3D", 						// 3
			" Adición y Resta con Vectores en 3D", 	// 4	
			" Producto Cruz y el Plano",			// 5
			" * Ejercicios 1",				 		// 6
			" * Ejercicios 2",						// 7
			" * Ejercicios 3"						// 8
			};
	
	// -----------------------------------------------------------------------------------
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_list_layout); // searches for the alias @values/layouts
		
		// find the fragments...
		mListFragment = (ActivityListFragment) 
				getSupportFragmentManager().findFragmentById(R.id.activity_list_fragment);
		
		// Register this activity as the listener for the IndexFragment envents.
		mListFragment.setOnItemSelectedListener(this);
	}
	
	// -----------------------------------------------------------------------------------
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		restoreSelection(savedInstanceState);
	}
	public void restoreSelection(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			setIndexCategory(savedInstanceState.getInt("catIndex", 0));
			if (mIsDualPane) {
				int actIndex = savedInstanceState.getInt("actIndex", 0);
				mListFragment.setSelection(actIndex);
				onItemSelected(actIndex);
			}
		}
	}
	// -----------------------------------------------------------------------------------
	@Override
	public void onStart() {
		super.onStart();
		setIndexCategory(0);
	}
	/**
	 * setItemsCategory sets the displayed Items for a specific subject
	 * This causes the Index Fragment to be populated with the appropriate content
	 */
	void setIndexCategory(int categoryIndex) {
		// 
		mCatIndex = categoryIndex;
		mListFragment.loadIndex(mActivitiesList);
		
		// In dual pane, display the content on the right, and update that too
		if (mIsDualPane) {
			//mDescriptionFragment.displayDescription();
		}
	}
	
	// -----------------------------------------------------------------------------------
	/**
	 * Called when an Activity is selected
	 * 
	 * This is called by the ActivitiesFragment (via its listener interface) to notify
	 * that an activity was selected in the Action Bar. The way this reacts depends on 
	 * whether the app is in single o dual-pane mode. In the first, a new activity is 
	 * launched to display the selected content; in dual-pane the content is displayed
	 * on the description fragment
	 * 
	 * @param index the index of the selected activity
	 */
	@Override
	public void onItemSelected(int index) {
		// TODO Auto-generated method stub
		//mDescriptionIndex = index;		
		if (mIsDualPane) {
			// TODO display it on the description fragment
			// mDescriptionFragment.displayDescription(mCurrent...);
		} else {
			// use separate activity
			/*Intent i = new Intent(this, DescriptionActivity.class);
			i.putExtra("categoryIndex", 0);
			i.putExtra("descriptionIndex",index);
			startActivity(i);
			*/
			launchActivity(index);
		}
	}
	private void launchActivity (int indexPosition) {
		
		Intent intent = new Intent(this, ActivityDescription.class);
        //intent.putExtra("ABOUT_TEXT_TITLE", activityTitle);        
		
		//String packageName = getPackageName(); 
		//String className = packageName + ".app.FrameMarkers.FrameMarkers";
		//Intent intent = new Intent();
		//intent.setClassName(packageName, className);
		
		
        switch (indexPosition) {
        case 0: // Introducción
            intent.putExtra("ACTIVITY_TO_LAUNCH", "app.FrameMarkers.FrameMarkers");
            intent.putExtra("ABOUT_TEXT", "FrameMarkers/FM_about.html");
            break;
        case 1: // Sistema de Coordenadas
            intent.putExtra("ACTIVITY_TO_LAUNCH", "mainActivity.MainApp");
            intent.putExtra("ABOUT_TEXT", "FrameMarkers/FM_about.html");
            break;
        case 2: // Vector en 3D
            intent.putExtra("ACTIVITY_TO_LAUNCH", "app.FrameMarkers.FrameMarkers");
            intent.putExtra("ABOUT_TEXT", "FrameMarkers/FM_about.html");
            break;
        case 3: // Adición y Resta con Vectores en 3D
            intent.putExtra("ACTIVITY_TO_LAUNCH", "app.FrameMarkers.FrameMarkers");
            intent.putExtra("ABOUT_TEXT", "FrameMarkers/FM_about.html");
            break;
        case 4: // Producto cruz y el plano
            intent.putExtra("ACTIVITY_TO_LAUNCH", "app.FrameMarkers.FrameMarkers");
            intent.putExtra("ABOUT_TEXT", "FrameMarkers/FM_about.html");
            break;
        case 5: // Ejercicio 1
        	break;
        case 6: // Ejercicio 2
        	break;
        case 7: // Ejercicio 3
        	break;
        }
        
        
        startActivity(intent);
		//finish();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
