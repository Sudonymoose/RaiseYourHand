package com.raiseyourhand.instructor;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TimePicker;

import com.raiseyourhand.R;

/**
 * @author Hanrui Zhang
 * Looks like P 20 - 22, but need different layout
 */
public class Attendance extends Activity {
	
	private boolean choose_bluetooth;
	private boolean choose_builtin;
	private Button timer_button;
	
	private SearchView searchView;
	private Chronometer chronometer;
	private TimePicker timepicker;
	private Button startButton;
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
		
		chronometer = (Chronometer) findViewById(R.id.chronometer1);
		
		timepicker = (TimePicker) findViewById(R.id.timePicker1);
		// Setup Start button
		startButton = (Button)findViewById(R.id.instructor_attendance_button);
		//startButton.setOnClickListener(new StartAttendanceOnClickListener());
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
	private class AttendanceTimerOnClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Starting (and maybe ending?) the timer for taking attendance
			
		}
	}
}
