package com.raiseyourhand.student;

import java.util.ArrayList;

import com.entities.Course;
import com.raiseyourhand.R;
import com.ws.Request;
import com.ws.RequestType;
import com.ws.local.SendRequest;
import com.ws.local.ServerResponseListener;

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

public class ViewLecture extends Activity {
	private Button joinButton;
	private TextView instructorView;
	private TextView dateView;
	private TextView timeView;
	private TextView buildingView;
	private TextView roomView;

	// private LectureInfo info; 
	private long lecture_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_view_lecture);
		// Show the Up button in the action bar.
		setupActionBar();

		// TODO: Check the user information
		// Is the user logged in?
		// Redirect to LOGIN
		// If the user is logged in,
		// Get lecture info 

		// Show the Up button in the action bar.
		setupActionBar();

		// Get lecture ID
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		lecture_id = extras.getLong("lecture_id");
		
		// Call to server to get roster
		Object[] args = new Object[1];
		args[0] = lecture_id;
		GetLectureServerResponseListener listener = new GetLectureServerResponseListener();
		SendRequest getLectureRequest = new SendRequest(RequestType.GET_LECTURE, listener, args);
		getLectureRequest.execute((Void)null);
		
		// Get lecture info view
		instructorView = (TextView)findViewById(R.id.student_view_lecture_instructor);
		dateView = (TextView)findViewById(R.id.student_view_lecture_date);
		timeView = (TextView)findViewById(R.id.student_view_lecture_time);
		buildingView = (TextView)findViewById(R.id.student_view_lecture_building);
		roomView = (TextView)findViewById(R.id.student_view_lecture_room);

		instructorView.setText("");
		dateView.setText("");
		timeView.setText("");
		buildingView.setText("");
		roomView.setText("");
		
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

public class JoinLectureOnClickListener implements OnClickListener {
	@Override
	public void onClick(View v) {
		// TODO: Check if lecture is currently started
		
		// Tell server that this student is going to join the lecture
		Object[] args = new Object[2];
		// args[0] = username?
		// args[1] = lecture_id?
		SendJoinLectureServerResponseListener listener = new SendJoinLectureServerResponseListener();
		SendRequest sendJoinLectureRequest = new SendRequest(RequestType.SEND_JOIN_LECTURE, listener, args);
		sendJoinLectureRequest.execute((Void) null);
		
		// Create an Intent to launch the Lecture Activity
		Intent lecture = new Intent(ViewLecture.this, Lecture.class);
		startActivity(lecture);
	}
}

/**
 * Private sub-class to respond to server's response when requesting for the roster list for this lecture
 */
private class GetLectureServerResponseListener implements ServerResponseListener {

	@Override
	public boolean onResponse(Request r) {

		Object[] args = r.getArgs();
		
		// Get the Course object returned by the server
		try{
			if(((String)args[0]).equals(Request.FAILURE))
			{
				return false;
			}
			else if(((String)args[0]).equals(Request.SUCCESS))
			{
				Course c = (Course)args[1];
				
				// TODO: Populate appropriate views with the course information
				
				
				// TODO: refresh?
				
				return true;
			}
		} catch(ClassCastException e) {
			return false;
		}
		return false;
	}
}

/**
 * Private sub-class to respond to server's response when telling server that this student is joining this lecture
 */
private class SendJoinLectureServerResponseListener implements ServerResponseListener {

	@Override
	public boolean onResponse(Request r) {
		// TODO Know if message sending was successful
		return false;
	}
}

}
