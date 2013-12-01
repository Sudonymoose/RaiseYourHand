package com.raiseyourhand.student;

import java.util.ArrayList;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;

import com.raiseyourhand.Login;
import com.raiseyourhand.R;
import com.raiseyourhand.RaiseYourHandApp;
import com.raiseyourhand.fragment.InstructorSharedFragment;
import com.raiseyourhand.fragment.InstructorSharedFragment.PassItemListener;
import com.raiseyourhand.fragment.StudentSharedFragment;
/**
 * General Framework for Instructor Shared and Student Shared
 * @author Hanrui Zhang
 * Everything from P68 - 80 is done, not backend stuff
 */
public class Lecture extends FragmentActivity implements ActionBar.TabListener, PassItemListener, SensorEventListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;
	private Button downloadButton;
	private Button questionButton;
	private SensorManager sensorManager;
	private Sensor accelerometer;

	private float[] mGravity;
	private float mAccel;
	private float mAccelCurrent;
	private float mAccelLast;
	private boolean shaking;
	private ArrayList<String> all_items;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_lecture);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// Show the Up button in the action bar.
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		// Check for logged in users.
		if (RaiseYourHandApp.getUsername() == null) {
			RaiseYourHandApp.logout();
			Intent login = new Intent(this, Login.class);
			startActivity(login);
		}

		// Setup buttons
		downloadButton = (Button) findViewById(R.id.student_lecture_download_button);
		downloadButton.setOnClickListener(new DownloadOnClickListener());
		questionButton = (Button) findViewById(R.id.student_lecture_ask_button);
		questionButton.setOnLongClickListener(new QuestionOnClickListener());
		questionButton.setEnabled(true);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.student_lecture_pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

		//initializing the motion sensors
		sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mAccel = 0.00f;
		mAccelCurrent = SensorManager.GRAVITY_EARTH;
		mAccelLast = SensorManager.GRAVITY_EARTH;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_lecture, menu);
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
			final Dialog leave = new Dialog(this);
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

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = new InstructorSharedFragment();
				break;
			case 1:
				fragment = new StudentSharedFragment();
				break;
			}			

			return fragment;
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_instructor_shared).toUpperCase(l);
			case 1:
				return getString(R.string.title_student_shared).toUpperCase(l);
			}
			return null;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		sensorManager.registerListener(this, accelerometer,
				SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
	}


	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
			mGravity = event.values.clone();
			// Shake detection
			float x = mGravity[0];
			float y = mGravity[1];
			float z = mGravity[2];
			mAccelLast = mAccelCurrent;
			mAccelCurrent = FloatMath.sqrt(x*x + y*y + z*z);
			float delta = mAccelCurrent - mAccelLast;
			mAccel = mAccel * 0.9f + delta;
			//threshold
			if(mAccel > 0.1){ 
				shaking = true;
			}else{
				shaking = false;
			}
		}

	}


	@Override
	public void passData(ArrayList<String> all_items) {
		// TODO: implemented method to get all items in shared fragments
		this.all_items = all_items;
	}

	private class DownloadOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO: DOWNLOAD DIALOG / CONTENT PROVIDER DOWNLOAD ETC.
			/*Get data from fragments*/
			ArrayList<String> selected = all_items;
			if(selected != null){
				for(String s : selected){
					//go search in the database and download
				}
			}
		}
	}

	private class QuestionOnClickListener implements OnLongClickListener {
		@Override
		public boolean onLongClick(View arg0) {
			// TODO: HANDLE SHAKING PHONE. PERHAPS A DIALOG WHILE BEING HELD.
			Log.i("Shaking", mAccelCurrent + ", " + mAccel );
			if(shaking){
				Intent ask_question = new Intent(com.raiseyourhand.student.Lecture.this, 
						Ask.class);
				startActivity(ask_question);
			}
			return true;
		}
	}




}
