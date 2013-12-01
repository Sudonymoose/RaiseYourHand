package com.raiseyourhand.instructor;

import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.raiseyourhand.Login;
import com.raiseyourhand.R;
import com.raiseyourhand.RaiseYourHandApp;
import com.raiseyourhand.fragment.InstructorSharedFragment;
import com.raiseyourhand.fragment.QuestionFragment;
import com.raiseyourhand.fragment.StudentSharedFragment;

/**
 * General framework for Q&A, Instructor Shared, Student Shared
 * @author Hanrui Zhang
 *
 */
public class Lecture extends FragmentActivity implements
ActionBar.TabListener {

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
	ViewPager mViewPager;
	private Button attendanceButton;
	private Button quizButton;
	
	private String lectureName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instructor_lecture);

		// Check for logged in users.
		if (RaiseYourHandApp.getUsername() == null) {
			RaiseYourHandApp.logout();
			Intent login = new Intent(this, Login.class);
			startActivity(login);
		}
		
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// Show the Up button in the action bar.
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Get the lecture string from the InfoActivity that started this LectureActivity
		Bundle extras = getIntent().getExtras();
		lectureName = extras.getString("Lecture Information");
		
		// Setup buttons
		attendanceButton = (Button) findViewById(R.id.instructor_lecture_attendance_button);
		attendanceButton.setOnClickListener(new AttendanceOnClickListener());
		quizButton = (Button) findViewById(R.id.instructor_lecture_quiz_button);
		quizButton.setOnClickListener(new QuizOnClickListener());

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.instructor_lecture_pager);
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.instructor_lecture, menu);
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
				fragment = (Fragment) new QuestionFragment();
			case 1:
				fragment = (Fragment) new InstructorSharedFragment();
			case 2:
				fragment = (Fragment) new StudentSharedFragment();
			}			

			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_question_and_answer).toUpperCase(l);
			case 1:
				return getString(R.string.title_instructor_shared).toUpperCase(l);
			case 2:
				return getString(R.string.title_student_shared).toUpperCase(l);
			}
			return null;
		}
	}
	
	public class AttendanceOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			
			// Create an Intent to launch the Attendance Activity
			Intent lecture = new Intent(Lecture.this, Attendance.class);
			startActivity(lecture);
			
			// TODO: Need a way to return here after attendance is done?
		}
	}
	public class QuizOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// create an Intent to launch the Quiz Activity
			Intent quiz = new Intent(Lecture.this, SetupQuiz.class);
			startActivity(quiz);
			
			// QuizActivity should return here automatically after it ends
			
		}
	}
	
	/**
	 * Old code from Lecture Activity

	public static class QuestionFragment extends ListFragment {
		public QuestionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.instructor_question_item,
					container, false);
			return rootView;
		}
	}
	public static class InstructorSharedFragment extends ListFragment {
		public InstructorSharedFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.instructor_shared_item,
					container, false);
			return rootView;
		}
	}
	public static class StudentSharedFragment extends ListFragment {
		public StudentSharedFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.student_shared_item,
					container, false);
			return rootView;
		}
	}
	*/
}
