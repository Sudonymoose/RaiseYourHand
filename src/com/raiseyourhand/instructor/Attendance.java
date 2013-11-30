package com.raiseyourhand.instructor;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.raiseyourhand.R;
import com.ws.Request;
import com.ws.RequestType;
import com.ws.local.SendRequest;
import com.ws.local.ServerResponseListener;

/**
 * @author Hanrui Zhang
 * Looks like P 20 - 22, but need different layout
 */
public class Attendance extends Activity {

	private boolean choose_bluetooth;
	private boolean choose_builtin;

	private SearchView searchView;
	private Button startButton;
	private ListView rosterListView;
	private ArrayAdapter<String> rosterAdapter;
	private String[] students = new String[0];

	private TextView time_left;
	private boolean time_set;
	private CountDownTimer timer;
	
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

		// Setup Start button
		startButton = (Button)findViewById(R.id.instructor_attendance_button);
		startButton.setOnClickListener(new StartAttendanceOnClickListener());
		
		time_left = (TextView) findViewById(R.id.instructor_attendance_time);
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

		case R.id.action_back:
			//finishActivity(0);
			onBackPressed();
			return true;
		case R.id.action_mic:
			final Dialog mic_setting = new Dialog(Attendance.this);
			mic_setting.setContentView(R.layout.dialog_instructor_set_mic);
			Button mic_set = (Button) mic_setting.findViewById(R.id.instructor_set_mic_btn);
			TextView bluetooth = (TextView) mic_setting.findViewById(R.id.instructor_set_mic_bluetooth_text);
			TextView builtin = (TextView) mic_setting.findViewById(R.id.instructor_set_mic_builtin_text);

			// Is this needed??
			bluetooth.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					choose_bluetooth = !choose_bluetooth;
					choose_builtin = !choose_builtin;
				}
			});

			builtin.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					choose_bluetooth = !choose_bluetooth;
					choose_builtin = !choose_builtin;
				}
			});

			mic_set.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					//set microphone
					if(choose_bluetooth){
						BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
						if (mBluetoothAdapter == null) {
							//it's actually quite complicated to connect to the Bluetooth server
						}
					}else if(choose_builtin){
						//looks like we need another dialog to record?
					}
					mic_setting.dismiss();
				}

			});
			mic_setting.show();
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


	/**
	 * Listener Class for when the timer attendance button is clicked
	 */
	private class StartAttendanceOnClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			if(!time_set){
				final Dialog set_time = new Dialog(Attendance.this);
				set_time.setContentView(R.layout.dialog_set_time);

				Button set = (Button) set_time.findViewById(R.id.set_time_button);
				Button cancel = (Button) set_time.findViewById(R.id.cancel_time_button);
				final NumberPicker minutes = (NumberPicker) set_time.findViewById(R.id.NumberPicker_minute);
				final NumberPicker seconds = (NumberPicker) set_time.findViewById(R.id.NumberPicker_second);

				minutes.setMaxValue(60);
				minutes.setMinValue(0);

				seconds.setMaxValue(60);
				seconds.setMinValue(0);

				set.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View arg0) {

						int set_min = minutes.getValue();
						int set_sec = seconds.getValue();
						time_left.setText(String.format("%02d", set_min) + ":" + String.format("%02d", set_sec));
						set_time.dismiss();
						if(set_min != 0 && set_sec != 0){
							time_set = true;
							startButton.setText(R.string.instructor_lecture_attendance_timer_end);

							// TODO: is this the right place to tell the server to start attendance?
							Object[] start_args = new Object[1]; // TODO: probably lecture id?
							SendStartAttendanceServerResponseListener start_listener = new SendStartAttendanceServerResponseListener();
							SendRequest sendStartAttendanceRequest = new SendRequest(RequestType.SEND_START_ATTENDANCE, start_listener, start_args);
							sendStartAttendanceRequest.execute((Void)null);
							
							//the countdownTimer uses background thread
							timer = new CountDownTimer(set_min * 1000 * 60 + set_sec * 1000, 1000){
								public void onTick(long millisUntilFinished){
									time_left.setText("seconds remaining: " + millisUntilFinished / 1000);
								}
								public void onFinish(){
									time_left.setText("Time's Up");
									
									// TODO: also is this the right place to tell the server to end attendance?
									Object[] end_args = new Object[1]; // TODO: probably lecture id?
									SendEndAttendanceServerResponseListener end_listener = new SendEndAttendanceServerResponseListener();
									SendRequest sendEndAttendanceRequest = new SendRequest(RequestType.SEND_END_ATTENDANCE, end_listener, end_args);
									sendEndAttendanceRequest.execute((Void)null);
								}
							};
							timer.start();
						}
					}

				});

				cancel.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						set_time.dismiss();
						time_set = false;
					}

				});
				set_time.show();
			}else{
				if(timer != null)
					timer.onFinish();
				time_set = false;
				startButton.setText(R.string.instructor_lecture_attendance_timer_start);
				//TODO: Go back to Lecture
				finish();
			}
		}
	}

	
	/**
	 * Private sub-class to respond to server's response when telling it to start attendance
	 */
	private class SendStartAttendanceServerResponseListener implements ServerResponseListener {

		@Override
		public boolean onResponse(Request r) {
			// TODO USe confirmation that server received?
			return false;
		}
	}
	
	/**
	 * Private sub-class to respond to server's response when telling it to start attendance
	 */
	private class SendEndAttendanceServerResponseListener implements ServerResponseListener {

		@Override
		public boolean onResponse(Request r) {
			// TODO USe confirmation that server received?
			return false;
		}
	}
}
