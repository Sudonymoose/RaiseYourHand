package com.raiseyourhand;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

/**
 * Where does it go?
 * @author Hanrui Zhang
 *
 */
public class ViewSharedActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_shared);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_shared, menu);
		return true;
	}

}