package com.raiseyourhand.student;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

/**
 * Class to listen to when an object in the ListView of LectureListActivity
 * on the students's side is clicked on
 */
public class ViewLectureListener implements ListView.OnItemClickListener {

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
        Intent lectureIntent = new Intent(this, com.raiseyourhand.student.LectureActivity.class);
        
        // pass the selected contact's row ID as an extra with the Intent..????
        viewContact.putExtra(ROW_ID, arg3);
        
        // TODO: Get lecture information and pass it into the activity
        
        startActivity(lectureIntent);
	}
	
}
