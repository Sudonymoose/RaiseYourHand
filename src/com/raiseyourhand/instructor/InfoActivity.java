package com.raiseyourhand.instructor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.raiseyourhand.R;
/**
 * P12, 13?
 * Or student Roster, kinda confused?
 * @author Hanrui Zhang
 *
 */
public class InfoActivity extends Activity {
	private ListView rosterListView;
	private ArrayAdapter<String> rosterAdapter;
	private Button startLectureButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instructor_info);
		
		// Get the lecture string from the LectureListActivity that started this activity
		Bundle extras = getIntent().getExtras();
		String lecture = extras.getString("Lecture Information");
		
		// Change the activity title
		setTitle(lecture);
		
		// Show the Up button in the action bar.
		setupActionBar();
		rosterListView = (ListView)findViewById(R.id.instructor_info_listview);     
		rosterAdapter = new ArrayAdapter<String>(this, R.layout.student_item);
		rosterListView.setAdapter(rosterAdapter);
		
		// Set up start lecture button
		startLectureButton = (Button) findViewById(R.id.instructor_info_button);
		startLectureButton.setOnClickListener(new StartLectureOnClickListener());
		
		// TODO: get a bunch of information from database based on the title.
		
		
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

	/**
	 * Listener Class for when the start lecture button is clicked
	 */
	private class StartLectureOnClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			
			// TODO Make an intent for starting a lecture
			Intent startLectureIntent = new Intent(InfoActivity.this, LectureActivity.class);
			
			// TODO Pass lecture info that was passed to here from LectureListActivity and pass it to startLectureIntent
			
			
			startActivity(startLectureIntent);
		}
	}
	
}
