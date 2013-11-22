package com.raiseyourhand.instructor;

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
 * 
 * @author Hanrui Zhang
 * Page 10 - 11
 */
public class LectureListActivity extends Activity {
	private ListView lectureListView;
	private ArrayAdapter<String> lectureAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instructor_lecture_list);
		// Show the Up button in the action bar.
		setupActionBar();
		lectureListView = (ListView)findViewById(R.id.instructor_lecture_list_listview);
		lectureListView.setOnItemClickListener( new ViewLectureListener());      

		lectureAdapter = new ArrayAdapter<String>(this, R.layout.lecture_item);
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


	class ViewLectureListener implements ListView.OnItemClickListener{

		@Override
		/**
		 * Method called when an item in the ListView of student's Lecture List is clicked on
		 * 
		 * @param parent The AdapterView where the click happened.
		 * @param view The view within the AdapterView that was clicked
		 * @param position The position of the view in the adapter.
		 * @param id The row id of the item that was clicked. 
		 */
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			// create an Intent to launch the ViewContact Activity
			Intent lectureIntent = new Intent(com.raiseyourhand.instructor.LectureListActivity.this,
					com.raiseyourhand.student.LectureActivity.class);

			// pass the selected contact's row ID as an extra with the Intent..????
			//viewContact.putExtra(ROW_ID, arg3);

			// TODO: Get lecture information and pass it into the activity

			startActivity(lectureIntent);
		}

	}
}
