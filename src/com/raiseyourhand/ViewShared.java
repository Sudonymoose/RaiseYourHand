package com.raiseyourhand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
/**
 * Not sure which page
 * @author Hanrui Zhang
 *
 */
public class ViewShared extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_shared);
		
		// Check for logged in users.
		if (RaiseYourHandApp.getUsername() == null) {
			RaiseYourHandApp.logout();
			Intent login = new Intent(this, Login.class);
			startActivity(login);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_shared, menu);
		return true;
	}

}
