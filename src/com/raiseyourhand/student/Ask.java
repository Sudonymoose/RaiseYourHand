package com.raiseyourhand.student;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.raiseyourhand.R;
/**
 * P74, 75
 * @author Hanrui Zhang
 *
 */
public class Ask extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_ask);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_ask, menu);
		return true;
	}

}
