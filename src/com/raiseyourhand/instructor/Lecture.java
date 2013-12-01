package com.raiseyourhand.instructor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.raiseyourhand.R;
import com.raiseyourhand.fragment.InstructorSharedFragment;
import com.raiseyourhand.fragment.InstructorSharedFragment.PassItemListener;
import com.raiseyourhand.fragment.QuestionFragment;
import com.raiseyourhand.fragment.StudentSharedFragment;

/**
 * General framework for Q&A, Instructor Shared, Student Shared
 * @author Hanrui Zhang
 *
 */
public class Lecture extends FragmentActivity implements
ActionBar.TabListener, PassItemListener{

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
	protected static final int TAKE_ATTENDANCE = 0;
	protected static final int START_QUIZ = 1;
	private ArrayList<String> items_for_students;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instructor_lecture);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// Show the Up button in the action bar.
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Get the lecture string from the InfoActivity that started this LectureActivity
		//Bundle extras = getIntent().getExtras();
		//lectureName = extras.getString("Lecture Information");
		
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
				break;
			case 1:
				fragment = (Fragment) new InstructorSharedFragment();
				break;
			case 2:
				fragment = (Fragment) new StudentSharedFragment();
				break;
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
			Intent attendance = new Intent(Lecture.this, Attendance.class);
			startActivityForResult(attendance, TAKE_ATTENDANCE);
		}
	}
	
	public class QuizOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent quiz = new Intent(Lecture.this, SetupQuiz.class);
			startActivityForResult(quiz, START_QUIZ);	
		}
	}
	
	@Override
	public void onActivityResult(final int requestCode,
			int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == TAKE_ATTENDANCE){
			if(resultCode == RESULT_OK){
				Toast.makeText(getBaseContext(), "Attendance Taken", Toast.LENGTH_LONG).show();
			}
		}
		if(requestCode == START_QUIZ){
			if(resultCode == RESULT_OK){
				// only when students submit the quiz answer, maybe use bundle
				final Dialog quiz_result = new Dialog(Lecture.this);
				quiz_result.setContentView(R.layout.dialog_instructor_quiz_result);

				ImageView result = (ImageView) quiz_result.findViewById(R.id.instrucotr_quiz_result_imageView);
				Button dismiss = (Button) quiz_result.findViewById(R.id.instrucotr_quiz_result_button);
				
				//temp fake result pic
				InputStream is = this.getResources().openRawResource(R.drawable.ic_launcher);
				//use decodeFath in real case
				Bitmap bmImg = BitmapFactory.decodeStream(is);
				result.setImageBitmap(bmImg);

				dismiss.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						quiz_result.dismiss();
					}
				});
				
				quiz_result.show();
			}
		}
	}

	@Override
	public void passData(ArrayList<String> all_items) {
		this.items_for_students = all_items;
	}
}
