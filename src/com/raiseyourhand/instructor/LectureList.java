package com.raiseyourhand.instructor;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.raiseyourhand.R;
/**
 * Activity for list of lectures for instructor to select
 * 
 * @author Hanrui Zhang
 * Page 10 - 11
 */
public class LectureList extends Activity {
	
	/**
	 * A dummy list of lecture names
	 * TODO: remove after connecting to server with lecture info
	 */
	private static final String[] DUMMY_LECTURE_LIST = new String[] {"Lecture 1", "Lecture 2", "Lecture 3"};
	
	private ListView lectureListView;
	private ArrayAdapter<String> lectureAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instructor_lecture_list);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		// List of lectures in a ListView
		lectureListView = (ListView)findViewById(R.id.instructor_lecture_list_listview);
		lectureListView.setOnItemClickListener( new ViewLectureListener());
		
		// TODO: Change DUMMY_LECTURE_LIST when able to connect to server
		// Set up the list and adapter for lectureListView
		final ArrayList<String> lectureList = new ArrayList<String>();
		for(int i = 0 ; i < DUMMY_LECTURE_LIST.length ; i++)
			lectureList.add(DUMMY_LECTURE_LIST[i]);
		lectureAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lectureList);
		// old code: lectureAdapter = new ArrayAdapter<String>(this, R.layout.lecture_item);
		lectureListView.setAdapter(lectureAdapter);
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
		getMenuInflater().inflate(R.menu.lecture_list, menu);
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
		case R.id.action_signout:
			//sign out here
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Class to listen to when an object in the ListView of LectureListActivity
	 * on the instructor's side is clicked on
	 */
	private class ViewLectureListener implements ListView.OnItemClickListener{

		@Override
		/**
		 * Called when an item in the ListView of student's Lecture List is clicked on
		 * 
		 * @param parent The AdapterView where the click happened.
		 * @param view The view within the AdapterView that was clicked
		 * @param position The position of the view in the adapter.
		 * @param id The row id of the item that was clicked. 
		 */
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			// create an Intent to launch the LectureActivity on the students end i think??
			// shouldn't this go into InfoActivity on instructor's end?
			//Intent lectureIntent = new Intent(com.raiseyourhand.instructor.LectureListActivity.this,
			//		com.raiseyourhand.student.LectureActivity.class);
			
			// Get lecture string from the selected item
			String lecture = (String) lectureListView.getItemAtPosition(position);
			
			// Create an Intent to load up the InfoActivity on the instructor's end
			Intent infoIntent = new Intent(LectureList.this, Info.class);
			
			// pass the selected contact's row ID as an extra with the Intent..????
			//viewContact.putExtra(ROW_ID, arg3);
			
			// Put lecture string into infoIntent to pass it on
			infoIntent.putExtra("Lecture Information", lecture);
			startActivity(infoIntent);
		}

	}
}
