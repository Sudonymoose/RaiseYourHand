package com.raiseyourhand.student;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.raiseyourhand.R;

public class ViewLecture extends Activity {
  private Button joinButton;
  private instructorView;
  private dateView;
  private timeView;
  private buildingView;
  private roomView;

  // private LectureInfo info; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_view_lecture);
		// Show the Up button in the action bar.
		setupActionBar();

    // TODO: Check the user information
    // Is the user logged in?
      // Redirect to LOGIN
    // If the user is logged in,
      // Get lecture info 

		// Show the Up button in the action bar.
		setupActionBar();

    // Get lecture info view
    instructorView = (TextView)findViewById(R.id.student_view_lecture_instructor);
    dateView = (TextView)findViewById(R.id.student_view_lecture_date);
    timeView = (TextView)findViewById(R.id.student_view_lecture_time);
    buildingView = (TextView)findViewById(R.id.student_view_lecture_building);
    roomView = (TextView)findViewById(R.id.student_view_lecture_room);

    // Set up Join button
    joinButton = (Button)findViewById(R.id.student_view_lecture_button);
    joinButton.setOnClickListener(new JoinLectureOnClickListener());
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
		getMenuInflater().inflate(R.menu.view_lecture, menu);
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

  public class JoinLectureOnClickListener implements OnClickListener {
      @Override
      public void onClick(View v) {
        // TODO: Check if lecture is currently started, and return if so.

        // create an Intent to launch the Lecture Activity
        Intent lecture = new Intent(ViewLecture.this, Lecture.class);
        startActivity(lecture);
      }
  }

}
