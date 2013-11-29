package com.raiseyourhand.instructor;

import java.util.ArrayList;

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
public class Info extends Activity {
	
	private String lectureName;
	private ListView rosterListView;
	private ArrayAdapter<String> rosterAdapter;
	private Button startLectureButton;
	
	/**
	 * A dummy list of student names in the roster for this lectures
	 * TODO: remove after connecting to server with roster info
	 */
	private static final String[] DUMMY_ROSTER = new String[] {"Alex", "Hanrui", "Arthur"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instructor_info);
		
		// Get the lecture string from the LectureListActivity that started this activity
		Bundle extras = getIntent().getExtras();
		lectureName = extras.getString("Lecture Information");
		
		// Change the activity title
		setTitle(lectureName);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		// TODO: Change DUMMY_ROSTER when able to connect to server
		// Set up the ListView with the roster
		rosterListView = (ListView)findViewById(R.id.instructor_info_listview);     
		final ArrayList<String> rosterList = new ArrayList<String>();
		for(int i = 0 ; i < DUMMY_ROSTER.length ; i++)
			rosterList.add(DUMMY_ROSTER[i]);
		rosterAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, rosterList);
		// old code: rosterAdapter = new ArrayAdapter<String>(this, R.layout.student_item);
		rosterListView.setAdapter(rosterAdapter);
		
		// Set up start lecture button
		startLectureButton = (Button) findViewById(R.id.instructor_info_button);
		startLectureButton.setOnClickListener(new StartLectureOnClickListener());
		
		// TODO: get a bunch of information from database based on the lecture title?
		
		
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
			
			// TODO Make an intent for starting a lecture, is this correct?
			Intent startLectureIntent = new Intent(Info.this, LectureActivity.class);
			
			// TODO Pass lecture title into startLectureIntent to pass onto LectureActivity
			startLectureIntent.putExtra("Lecture Information", lectureName);
			startActivity(startLectureIntent);
		}
	}
	
}
