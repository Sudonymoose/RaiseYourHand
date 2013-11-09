package com.raiseyourhand.student;

import com.raiseyourhand.R;
import com.raiseyourhand.R.layout;
import com.raiseyourhand.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AskActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_ask);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ask, menu);
		return true;
	}

}
