package com.raiseyourhand.student;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.raiseyourhand.R;
/**
 * Activity for a student asking a question, from the student's perspective
 * P76
 * 
 * Difference between Ask and AskActivity
 * 
 * @author Hanrui Zhang
 *
 */
public class AskActivity extends Activity {

	private Button end_question_button;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_ask);
		
		end_question_button = (Button) findViewById(R.id.student_ask_end_question_button);
		end_question_button.setOnClickListener(new EndQuestionOnClickListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_ask, menu);
		return true;
	}

	/**
	 * Listener class for when the end question button is clicked
	 */
	private class EndQuestionOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO End question and... do what activity?
			
		}
		
		
		
	}
}
