package com.raiseyourhand.instructor;

import android.content.Intent;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
/**
 * Class to listen to when an object in the ListView of LectureListActivity
 * on the instructor's side is clicked on
 * 
 * TODO: Should this be deleted? It's already in LectureListActivity.java as an inner class...
 */
public class ViewLectureListener implements ListView.OnItemClickListener {

	@Override
	/**
	 * Method called when an item in the ListView of instructor's Lecture List is clicked on
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
        		com.raiseyourhand.instructor.LectureActivity.class);
        
        // pass the selected contact's row ID as an extra with the Intent..????
        viewContact.putExtra(ROW_ID, arg3);
        
        // TODO: Get lecture information and pass it into the activity
        
        startActivity(lectureIntent);
	}
}
