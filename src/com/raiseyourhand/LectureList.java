package com.raiseyourhand;

import java.util.ArrayList;

import com.ws.Request;
import com.ws.RequestType;
import com.ws.local.SendRequest;
import com.ws.local.ServerResponseListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

/**
 * P10, 11
 * @author Hanrui Zhang
 *
 */
public class LectureList extends Activity {
	private SearchView searchView;
	private ListView lectureListView;
	private ArrayAdapter<String> lectureAdapter;
	private String[] lectures = null; // = {"Class 1, Lecture 2"};//new String[0];
	private boolean isStudent = false;
	private String username = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lecture_list);

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

		// Get the username from the Login Intent that called this?
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		username = extras.getString("Username");
		
		// Get list of lectures from server
		Object[] args = new Object[1];
		args[0] = username;
		GetLectureServerResponseListener listener = new GetLectureServerResponseListener();
		SendRequest GetLecturesRequest = new SendRequest(RequestType.GET_LECTURES, listener, args);
		GetLecturesRequest.execute((Void)null);
		
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
				// If student, set up intent for student's ViewLecture
				viewLecture = new Intent(LectureList.this, com.raiseyourhand.student.ViewLecture.class);
			} else {
				// If instructor, set up intent for instructor's ViewLecture
				viewLecture = new Intent(LectureList.this, com.raiseyourhand.instructor.ViewLecture.class);
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

		@Override
		public boolean onQueryTextSubmit(String arg0) {
			return false;
		}
	}
	
	
	/**
	 * Private sub-class to respond to server's response when requesting for a list of lectures
	 */
	private class GetLectureServerResponseListener implements ServerResponseListener {

		@Override
		public boolean onResponse(Request r) {
			
			Object[] args = r.getArgs();
			
			try{
				if(((String)args[0]).equals(Request.FAILURE))
				{
					return false;
				}
				else if(((String)args[0]).equals(Request.SUCCESS))
				{
					ArrayList<Integer> server_list = (ArrayList<Integer>)args[1];
					
					// Populate lectures list with what is returned from the server
					lectures = new String[server_list.size()];
					for(int i = 0 ; i < server_list.size() ; i++)
					{
						lectures[i] = String.valueOf((Integer)args[i]);
					}
					return true;
				}
			} catch(ClassCastException e) {
				return false;
			}
			return false;
		}
	}
	
}
