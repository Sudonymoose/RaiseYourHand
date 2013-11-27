package com.raiseyourhand.instructor;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.raiseyourhand.R;


/**
 * Activity for beginning a quiz on the instructor's side
 * 
 * @author Hanrui Zhang
 * Page 35- 38
 */
public class QuizActivity extends Activity {
	private ImageView question;
	private Button end_quiz;
	private Dialog end;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instructor_quiz_ongoing);
		// Show the Up button in the action bar.
		setupActionBar();
		
		question = (ImageView) findViewById(R.id.instructor_quiz_ongoing_imageView);
		//set image path from the server
		end_quiz = (Button) findViewById(R.id.instructor_quiz_ongoing_button);
		end_quiz.setOnClickListener(new EndQuizOnClickListener());
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
		getMenuInflater().inflate(R.menu.instructor_quiz, menu);
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
	
	/**
	 * OnClickListener class for when End Quiz button is clicked
	 * 
	 * When it is clicked, it brings up a dialog box that asks if the user is sure
	 * he/she wants to end the quiz, with Yes/No option.
	 */
	private class EndQuizOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			end = new Dialog(com.raiseyourhand.instructor.QuizActivity.this);
			end.setContentView(R.layout.dialog_instructor_end_quiz);
			
			Button yes = (Button) end.findViewById(R.id.instructor_end_quiz_btn_yes);
			Button no = (Button) end.findViewById(R.id.instructor_end_quiz_btn_no);
			
			// Yes, end quiz
			yes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					// TODO: Send a message to all student devices that quiz is done.
					
					// TODO: Start up a dialogue box for the quiz results.
					
					
					// Go back to LectureActivity
					(QuizActivity.this).finish();
				}

			});

			// No, do not end quiz
			no.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					end.dismiss();
				}

			});
			end.show();
		}
	}
}
