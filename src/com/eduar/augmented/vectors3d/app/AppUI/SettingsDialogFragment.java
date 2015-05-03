package com.eduar.augmented.vectors3d.app.AppUI;

import java.util.ArrayList;

//import com.eduar.augmented.vectors3d.R;


import com.eduar.augmented.vectors3d.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;


public class SettingsDialogFragment extends DialogFragment {

	private ArrayList<Integer> mSelectedItems = new ArrayList<Integer>();;
	
	
	// -----------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------
	public interface SettingsDialogListener {
		/**
		 * Called when the 'OK' button is selected.
		 * @param index is the index of the selected activity
		 */
		public void onProcessSettings(boolean[] checkList);
	}
	SettingsDialogListener updateSettingsListener = null; // the listener
	
	
	// -----------------------------------------------------------------------------------
	public SettingsDialogFragment () {
		
	}
	
	String [] items;	
	boolean [] checkedItems; 
	// -----------------------------------------------------------------------------------
	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState) {
				
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				
		builder.setTitle(R.string.settings_button)		
				
			   	.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {					
					@Override
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
	                       // If the user checked the item, add it to the selected items
	                       mSelectedItems.add(which);
	                       checkedItems[which] = true; 
	                   } else if (mSelectedItems.contains(which)) {
	                       // Else, if the item is already in the array, remove it 
	                       mSelectedItems.remove(Integer.valueOf(which));
	                       checkedItems[which] = false;
	                   }
					}
				})
				
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						setOkAction();
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub						
						Log.d("SettingFragment", "Size: " + mSelectedItems.size());
						for (int i=0; i<mSelectedItems.size(); i++) {
							Log.d("SettingFragment", "Data: " + mSelectedItems.get(i));
						}
					}
				});
					
		return builder.create();
	}
	
	// -----------------------------------------------------------------------------------
	public void setSettingsDialogListener (SettingsDialogListener listener) {
		updateSettingsListener = listener;
	}
	
	// -----------------------------------------------------------------------------------
	public void setListContent (String[] items, boolean[] checkedItems) {
		this.items = items;
		this.checkedItems = checkedItems;
		
		for (int i=0; i<checkedItems.length; i++) {
			if (checkedItems[i]) {
				mSelectedItems.add(i);
			}
		}
	}
	
	// -----------------------------------------------------------------------------------
	public void setOkAction() {
		Log.e("SettingFragment", "OK button");
		updateSettingsListener.onProcessSettings(checkedItems);
	}
}
