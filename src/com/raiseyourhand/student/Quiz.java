package com.raiseyourhand.student;

import java.util.Locale;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.raiseyourhand.R;
import com.ws.DownloadImageTask;

/**
 * Activity for a student taking a quiz, from student's perspective
 * P81-84
 * 
 * Difference between Quiz and QuizActivity
 * @author Hanrui Zhang
 *
 */
public class Quiz extends FragmentActivity implements
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	setContentView(R.layout.activity_student_quiz);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
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
		getMenuInflater().inflate(R.menu.student_quiz, menu);
		return true;
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
			ListFragment fragment = null;
			
			switch (position) {
			case 0:
				fragment = new QuizQuestionFragment();
			case 1:
				fragment = new QuizAnswerFragment();
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
				return getString(R.string.title_quiz_question).toUpperCase(l);
			case 1:
				return getString(R.string.title_quiz_answer).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A instructor shared fragment representing a section of the app, where
	 * the notes shared by the instructor are listed.
	 */
	public static class QuizQuestionFragment extends ListFragment {
		private ImageView question_image;
		
		public QuizQuestionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_student_quiz_question,
					container, false);
			question_image = (ImageView) rootView.findViewById(R.id.student_quiz_imageView);
			
			//set quiz image here
			String img_path = "";
			new DownloadImageTask(question_image).execute(img_path);
			
			return rootView;
		}
	}
	/**
	 * A student shared fragment representing a section of the app, where
	 * notes shared by students are listed.
	 */
	public static class QuizAnswerFragment extends ListFragment {
		
		private Dialog dialog_submit;
		private Button button_submit;
		private ListView quiz_listview;
		private String selected;
		
		public QuizAnswerFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_student_quiz_answer,
					container, false);
			
			button_submit = (Button) rootView.findViewById(R.id.student_quiz_button_1);
			quiz_listview = (ListView) rootView.findViewById(R.id.student_quiz_listview);

			button_submit.setOnClickListener(new SubmitAnswerListener());
			quiz_listview.setOnItemClickListener(new QuizListItemListener());

			return rootView;
		}
		
		
		private class QuizListItemListener implements ListView.OnItemClickListener{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				selected = (String) quiz_listview.getItemAtPosition(arg2);
			}
		}

		private class SubmitAnswerListener implements OnClickListener{

			@Override
			public void onClick(View v) {
				dialog_submit = new Dialog(getActivity().getBaseContext());
				dialog_submit.setContentView(R.layout.dialog_student_submit_quiz);

				Button yes = (Button) dialog_submit.findViewById(R.id.student_submit_quiz_btn_yes);
				Button no = (Button) dialog_submit.findViewById(R.id.student_submit_quiz_btn_no);

				yes.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(selected != null){
							//submit the answer to server
							
						}
						//go back to the previous activity
						getActivity().onBackPressed();
						//maybe use Bundle to pass a flag
						dialog_submit.dismiss();
					}

				});

				no.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog_submit.dismiss();
					}

				});
				dialog_submit.show();
			}

		}
	}

	
	
}
