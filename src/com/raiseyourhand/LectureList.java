package com.raiseyourhand;

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
import android.widget.SearchView;

import com.raiseyourhand.R;

public class LectureList extends Activity implements SearchView, ListView {
  private SearchView searchView;
	private ListView lectureListView;
	private ArrayAdapter<String> lectureAdapter;
  private String[] lectures = new String[0];
  private isStudent = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lecture_list);

    // TODO: Check the user information
    // Is the user logged in?
      // Redirect to LOGIN
    // If the user is logged in,
    // set isStudent = false if user is instructor.
    // store lecture names in the "lectures" array.


		// Show the Up button in the action bar.
		setupActionBar();

    // Set up Search Bar
    searchView = (SearchView)findViewById(R.id.lecture_list_search);
    searchView.setOnQueryTextListener(new LectureListOnQueryListener());

    // Set up list of lectures
		lectureListView = (ListView)findViewById(R.id.lecture_list_listview);
		lectureListView.setOnItemClickListener(new ViewLectureOnClickListener());      
		lectureAdapter = new ArrayAdapter<String>(this, R.layout.lecture_item, lectures);
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
      // TODO: Replace this with Logout Dialog. That logs out a user when confirmed.
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

  /**
   * Class to listen to when an object in the ListView of LectureList
   * is clicked on
   */
  public class ViewLectureOnClickListener implements OnItemClickListener {
    public static final String LECTURE_ID = "lecture_id"; // Intent extra key
    @Override
    /**
     * Method called when an item in the ListView of LectureList is clicked on
     * 
     * @param parent The AdapterView where the click happened.
     * @param view The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter.
     * @param id The row id of the item that was clicked. 
     */
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      Intent viewLecture;

      // create an Intent to launch the ViewLecture Activity
      if (isStudent) {
        viewLecture = new Intent(LectureList.this, student.ViewLecture.class);
      } else {
        viewLecture = new Intent(LectureList.this, instructor.ViewLecture.class);
      }

      // pass the selected lecture's row ID as an extra with the Intent.
      viewLecture.putExtra(LECTURE_ID, id);
      startActivity(viewLecture);
    }
  }

  public class LectureListOnQueryListener implements OnQueryTextListener {
    public boolean onQueryTextChange(String newText) {
      if (TextUtils.isEmpty(newText)) {
        lectureAdapter.getFilter().filter("");
      } else {
        lectureAdapter.getFilter().filter(newText.toString());
      }
      return true;
    }
  }
}
