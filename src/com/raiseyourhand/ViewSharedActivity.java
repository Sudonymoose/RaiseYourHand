package com.raiseyourhand;

import com.raiseyourhand.R;
import com.raiseyourhand.R.layout;
import com.raiseyourhand.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

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
