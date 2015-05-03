package com.eduar.augmented.vectors3d.mainActivity;

import java.util.ArrayList;
import java.util.List;


import com.eduar.augmented.vectors3d.R;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

public class ActivityListFragment extends ListFragment							 
							   implements OnItemClickListener //Interface from android.widget.AdapterView.OnItemClickListener 
							   {

	// ********* ------------------------------------------------------------------------
	// A listener to notify when an activity is selected (Interface)
	OnItemSelectedListener mItemSelectedListener = null;		
	/**
	 * The Interface that represents a listener that will be notified of the selection
	 */
	public interface OnItemSelectedListener {
		/**
		 * Called when a given item of the list is selected.
		 * @param index is the index of the selected activity
		 */
		public void onItemSelected(int index);
	}	
	// ********** ------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------
	
	// List of activities to be displayed per subject
	List<String> mIndexList = new ArrayList<String>();	
	
	// A list adapter for the list to be displayed
	ArrayAdapter<String> mListAdapter;
		
	// -----------------------------------------------------------------------------------
	// Default constructor. Parameterless, needed by framework 
	public ActivityListFragment() {
		super();
	}
	
	// -----------------------------------------------------------------------------------
	/**
	 * Sets the listener that should be notified of the item selection events.
	 * @param listener the listener to notify
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mListAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, mIndexList);
	}
	
	// -----------------------------------------------------------------------------------
	@Override
	public void onStart() {
		super.onStart();
		setListAdapter(mListAdapter);
		getListView().setOnItemClickListener(this);
		//loadIndex(0);		// Default: 0
	}
	
	// -----------------------------------------------------------------------------------
	/**
	 * Sets the listener that should be notified of item selection events.
	 * @param listener the listener to notify
	 */
	public void setOnItemSelectedListener(OnItemSelectedListener listener) {
		mItemSelectedListener = listener;
	}
	
	// -----------------------------------------------------------------------------------
	/**
	 * Load and display the index for a given subject category
	 * @param intdexList the index of the activities to display
	 */
	public void loadIndex(String [] indexList) {
		mIndexList.clear();
		
		// Load several activities into the index list	
		for (int i = 0; i < indexList.length; i++) {
			mIndexList.add(indexList[i]);
		}
				
		mListAdapter.notifyDataSetChanged(); // to update the index list
	}
	
	// -----------------------------------------------------------------------------------
	/**
	 * To handle a click on a index item
	 * This causes the configured listener to be notified that an item was selected
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (null != mItemSelectedListener) {
			mItemSelectedListener.onItemSelected(position);
		}
	}
	// -----------------------------------------------------------------------------------
}
