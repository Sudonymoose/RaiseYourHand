package com.raiseyourhand.instructor;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.raiseyourhand.R;
import com.raiseyourhand.instructor.ViewLecture.StartLectureOnClickListener;
import com.raiseyourhand.instructor.ViewLecture.ViewLectureOnQueryListener;

public class Attendance extends Activity {
	private SearchView searchView;
	private Button incButton;
	private Button decButton;
	private Button startButton;
	private Button stopButton;
	private ListView rosterListView;
	private ArrayAdapter<String> rosterAdapter;
	private String[] students = new String[0];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instructor_attendance);

		// TODO: Check the user information
		// Is the user logged in?
		// Redirect to LOGIN
		// If the user is logged in,
		// store student names in the "students" array.

		// Show the Up button in the action bar.
		setupActionBar();

		// Set up Search Bar
		searchView = (SearchView)findViewById(R.id.instructor_attendance_search);
		searchView.setOnQueryTextListener(new AttendanceOnQueryListener());

		// Set up lecture roster
		rosterListView = (ListView)findViewById(R.id.instructor_attendance_listview);     
		rosterAdapter = new ArrayAdapter<String>(this, R.layout.roster_item, students);
		rosterListView.setAdapter(rosterAdapter);
		
		// Setup Timer buttons
	/*	incButton = (Button)findViewById(R.id.instructor_attendance_inc_button);
		incButton.setOnClickListener(new IncrementTimerOnClickListener());

		decButton = (Button)findViewById(R.id.instructor_attendance_dec_button);
		decButton.setOnClickListener(new DecrementTimerOnClickListener());
*/
		// Setup Start button
		startButton = (Button)findViewById(R.id.instructor_attendance_button);
	//	startButton.setOnClickListener(new StartAttendanceOnClickListener());
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
		getMenuInflater().inflate(R.menu.attendance, menu);
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

	public class AttendanceOnQueryListener implements OnQueryTextListener {
		public boolean onQueryTextChange(String newText) {
			if (TextUtils.isEmpty(newText)) {
				rosterAdapter.getFilter().filter("");
			} else {
				rosterAdapter.getFilter().filter(newText.toString());
			}
			return true;
		}

		@Override
		public boolean onQueryTextSubmit(String query) {
			return false;
		}
	}
}
