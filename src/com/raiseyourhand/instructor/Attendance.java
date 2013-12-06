package com.raiseyourhand.instructor;

import ws.Request;
import ws.RequestType;
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
import android.widget.Toast;

import com.raiseyourhand.R;
import com.raiseyourhand.RaiseYourHandApp;
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
	private CountDownTimer timer;
	private int option_flag;//0--Timer set, 1--Start Timer, 2--End Timer

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instructor_attendance);
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

	/*
	 * Private method to tell server to start attendance
	 * 
	 * Not called by the demo, since it makes a connection to the server.
	 */
	private void startAttendanceToServer() {
		Object[] start_args = new Object[]{RaiseYourHandApp.getCourseNum()};
		SendStartAttendanceServerResponseListener start_listener = new SendStartAttendanceServerResponseListener();
		SendRequest sendStartAttendanceRequest = new SendRequest(new Request(RequestType.SEND_START_ATTENDANCE, start_args), start_listener);
		sendStartAttendanceRequest.execute((Void)null);
	}

	/*
	 * Private method to tell server to end attendance
	 * 
	 * Not called by the demo, since it makes a connection to the server.
	 */
	private void endAttendanceToServer() {
		Object[] end_args = new Object[]{RaiseYourHandApp.getCourseNum()};
		SendEndAttendanceServerResponseListener end_listener = new SendEndAttendanceServerResponseListener();
		SendRequest sendEndAttendanceRequest = new SendRequest(new Request(RequestType.SEND_END_ATTENDANCE, end_args), end_listener);
		sendEndAttendanceRequest.execute((Void)null);
	}

	/**
	 * Private sub-class to respond to server's response when telling it to start attendance
	 * 
	 * Not called by the demo, since it makes a connection to the server.
	 */
	private class SendStartAttendanceServerResponseListener implements ServerResponseListener {

		@Override
		public boolean onResponse(Request r) {
			Object[] args = r.getArgs();

			// Just make sure server got the message correctly
			try{
				if(((String)args[0]).equals(Request.FAILURE))
				{
					return false;
				}
				else if(((String)args[0]).equals(Request.SUCCESS))
				{
					return true;
				}
			} catch(ClassCastException e) {
				return false;
			}
			return false;
		}
	}


	/**
	 * Listener Class for when the timer attendance button is clicked
	 * 
	 * Not called by the demo, since it makes a connection to the server.
	 */
	private class StartAttendanceOnClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			if(option_flag == 0){
				final Dialog set_time = new Dialog(Attendance.this);
				set_time.setContentView(R.layout.dialog_set_time);

				Button set = (Button) set_time.findViewById(R.id.set_time_button);
				Button cancel = (Button) set_time.findViewById(R.id.cancel_time_button);
				final NumberPicker minutes = (NumberPicker) set_time.findViewById(R.id.NumberPicker_minute);
				final NumberPicker seconds = (NumberPicker) set_time.findViewById(R.id.NumberPicker_second);

				minutes.setMaxValue(59);
				minutes.setMinValue(0);

				seconds.setMaxValue(59);
				seconds.setMinValue(0);

				set.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View arg0) {
						int set_min = minutes.getValue();
						int set_sec = seconds.getValue();
						if(set_min + set_sec != 0){
							time_left.setText(String.format("%02d", set_min) + ":" + String.format("%02d", set_sec));
							option_flag = 1;
							startButton.setText(R.string.instructor_lecture_attendance_timer_start);
						}else{
							time_left.setText("Please set time again");
						}
						set_time.dismiss();		
					}

				});

				cancel.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View arg0) {
						option_flag = 0;
						set_time.dismiss();				
					}

				});
				set_time.show();
			}else if(option_flag == 1){
				String time = time_left.getText().toString();
				int set_min = Integer.parseInt(time.substring(0, 2));
				int set_sec = Integer.parseInt(time.substring(3));

				option_flag = 2;
				startButton.setText(R.string.instructor_lecture_attendance_timer_end);
				Toast.makeText(getBaseContext(), time + "/" + set_min + "/" + set_sec, Toast.LENGTH_LONG).show();
				//the countdownTimer uses background thread
				timer = new CountDownTimer(set_min * 1000 * 60 + set_sec * 1000, 1000){
					public void onTick(long millisUntilFinished){
						time_left.setText("seconds remaining: " + millisUntilFinished / 1000);
					}
					public void onFinish(){
						time_left.setText("Time's Up");
						option_flag = 0;
						startButton.setText(R.string.instructor_lecture_attendance_timer_set);
						setResult(RESULT_OK);
						finish();
					}
				};
				timer.start();

				//TODO-Send the attendance notice to students, update checked-in list 

			}else if(option_flag == 2){
				if(timer != null)
					timer.onFinish();
				option_flag = 0;
				startButton.setText(R.string.instructor_lecture_attendance_timer_set);
				setResult(RESULT_OK);
				finish();
			}
		}
	}

	/**
	 * Private sub-class to respond to server's response when telling it to start attendance
	 * 
	 * Not called by the demo, since it makes a connection to the server.
	 */
	private class SendEndAttendanceServerResponseListener implements ServerResponseListener {

		@Override
		public boolean onResponse(Request r) {

			Object[] args = r.getArgs();

			// Just make sure server got the message correctly
			try{
				if(((String)args[0]).equals(Request.FAILURE))
				{
					return false;
				}
				else if(((String)args[0]).equals(Request.SUCCESS))
				{
					return true;
				}
			} catch(ClassCastException e) {
				return false;
			}
			return false;
		}
	}
}
