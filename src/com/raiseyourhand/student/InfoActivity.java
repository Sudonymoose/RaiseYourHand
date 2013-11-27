package com.raiseyourhand.student;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.raiseyourhand.R;

/**
 * p70-10
 * 
 * Does student need this one? Or maybe should changed to be lec info?
 * @author Hanrui Zhang
 */
public class InfoActivity extends Activity {

	private Button attend_lecture_button;
	private TextView instructor_info;
	private TextView date_info;
	private TextView time_info;
	private TextView building_info;
	private TextView room_info;
	private String lecture;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_info);
		
		// Show the Up button in the action bar.
		setupActionBar();

		// Set up the attend_lecture_button
		attend_lecture_button = (Button) findViewById(R.id.student_info_button);
		attend_lecture_button.setOnClickListener(new AttendLectureOnClickListener());

		Bundle extras = getIntent().getExtras();
		lecture = extras.getString("Lecture Information");
		
		// Change the Activity Title
		setTitle(lecture);
		
		
		// TODO: get a bunch of information from database based on the title.

		// Set up TextViews
		instructor_info = (TextView) findViewById(R.id.student_info_textview_instructor_b);
		time_info = (TextView) findViewById(R.id.student_info_textview_time_b);
		date_info = (TextView) findViewById(R.id.student_info_textview_date_b);
		building_info = (TextView) findViewById(R.id.student_info_textview_building_b);
		room_info = (TextView) findViewById(R.id.student_info_textview_room_b);

		// Communicate with the background to get information...?

		//set text for the text fields
		instructor_info.setText("");
		date_info.setText("");
		time_info.setText("");
		building_info.setText("");
		room_info.setText("");

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
		case R.id.action_back:
			final Dialog leave = new Dialog(getBaseContext());
			leave.setContentView(R.layout.dialog_student_end_lecture);
			
			Button yes = (Button) leave.findViewById(R.id.student_end_lecture_btn_yes);
			Button no = (Button) leave.findViewById(R.id.student_end_lecture_btn_no);
			
			yes.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					finishActivity(0);
				}
			});
			
			no.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					leave.dismiss();
				}
			});
			//super.onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class AttendLectureOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent go_lecture = new Intent(com.raiseyourhand.student.InfoActivity.this, 
					com.raiseyourhand.student.Lecture.class);
			go_lecture.putExtra("Lecture",lecture);
			startActivity(go_lecture);
		}
	}

}
