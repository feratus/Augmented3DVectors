package com.eduar.augmented.vectors3d.mainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.eduar.augmented.vectors3d.R;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class ActivityDescriptionFragment extends Fragment 
										implements OnClickListener
{
	
	// ********* ------------------------------------------------------------------------
	// A listener to notify when the button is pressed to launch the activity (Interface)
	OnButtonPressedListener mButtonPressedListener = null;		
	/**
	 * The Interface that represents a listener that will be notified of the selection
	 */
	public interface OnButtonPressedListener {
		/**
		 * Called when a given item of the list is selected.
		 * @param index is the index of the selected activity
		 */
		public void onButtonPressed(); // 
	}	
	// ********** ------------------------------------------------------------------------
	
	// WebView to display the description content
	private WebView mAboutWebView;
	// A TextView to show the title of the description
	private TextView mTitleTextView;
	// Button to launch the activity related to the description
	private Button mStartButton;
	
	// Strings to get the package and the class to launch
	private String mWebText = "";
	private String mTitleText = ""; 
	
	private View mView;
	
	// -----------------------------------------------------------------------------------	
	//*** Parameterless constructor, needed by framework
	public ActivityDescriptionFragment() {
		super();
	}
	
	// -----------------------------------------------------------------------------------	
	/**
	 * To set up the UI, to define the layout. It consists in a single WebView
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Option 1: Use the WebView object as the UI (no layout needed)
		/*mAboutText = new WebView(getActivity()); // getActivity() returns the Activity the Fragment is currently associated		
		loadWebView();
		return mAboutText;
		*/
		
		// Option 2: Use a layout as the UI for the fragment, and get the references
		mView = inflater.inflate(R.layout.description_frag_screen, container, false);
		mTitleTextView = (TextView) mView.findViewById(R.id.description_title);
		mTitleTextView.setTypeface(null, Typeface.BOLD);
		mAboutWebView = (WebView) mView.findViewById(R.id.html_text);
		mStartButton = (Button) mView.findViewById(R.id.button_start_activity);
		mStartButton.setOnClickListener(this);
		loadWebView();
		return mView;
	}
	
	// -----------------------------------------------------------------------------------
	/**
	 * LoadWebView : Loads description data into the WebView
	 * 
	 * This method is called internally to update the webview's contents 
	 * to the appropriate description's text  
	 */
	void loadWebView() {
		//String webText = getActivity().getIntent().getExtras().getString("ABOUT_TEXT");
		//String webText = "HTML/html_about.html";
		String aboutText = "";
				
		if (mAboutWebView != null) {
			try {
				InputStream is = getActivity().getAssets().open(mWebText);
				BufferedReader reader = new BufferedReader( new InputStreamReader(is) );
				
				String line;
				while ( (line = reader.readLine()) != null ) {
					aboutText += line;
				}
				
			} catch (IOException e) {
				Log.e("DescriptionFragment", "HTML loading failed");
			}
						
			// The loading of the contents of WebView has to be inside the 'if(webViewObject != null)'
			// other case, it could lead to a 'NullPointerExcception'
			mAboutWebView.loadData(aboutText, "text/html", "UTF-8");
			
			if (mTitleTextView != null) {
				mTitleTextView.setText(mTitleText);
			} else {
				Log.e("DescriptionFragment", "mTextFile is NULL");
			}
		}
		
	}
	
	// -----------------------------------------------------------------------------------
	/**
	 * Displays a particular description
	 */
	public void displayDescription() {
		
		loadWebView();
	}

	// -----------------------------------------------------------------------------------
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mAboutWebView = null;
		mTitleTextView = null;
	}
		
	// -----------------------------------------------------------------------------------
	/**
	 * Set the arguments using the extras from the intent
	 */
	public void setArguments(String titleText, String webText) {
		this.mTitleText = titleText;
		this.mWebText = webText;
	}
	
	// -----------------------------------------------------------------------------------
	// method from the Interface 'OnClickListener'
	@Override
	public void onClick(View v) {
		// 
		//Log.d("DescriptionFragment", "Button Pressed!!!");
		if (mButtonPressedListener != null) {
			mButtonPressedListener.onButtonPressed();
		}		
	}
	
	// -----------------------------------------------------------------------------------
	/**
	 * Sets the listener that should be notified of button events.
	 * @param listener the listener to notify
	 */
	public void setOnButtonPressedListener(OnButtonPressedListener listener) {
		mButtonPressedListener = listener;		
	}
	// -----------------------------------------------------------------------------------
}
