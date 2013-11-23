package com.raiseyourhand.student;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.raiseyourhand.R;

/**
 * p70-10
 * 
 * Does student need this one? Or maybe should changed to be lec info?
 * @author Hanrui Zhang
 */
public class InfoActivity extends Activity {

	private Button attend_lecture_button;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_info);
		// Show the Up button in the action bar.
		setupActionBar();
		
		// Set up the attend_lecture_button
		attend_lecture_button = (Button) findViewById(R.id.student_info_button);
		attend_lecture_button.setOnClickListener(new AttendLectureOnClickListener());
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class AttendLectureOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Go into the lecture activity via Intent
			
		}
	}
	
}
