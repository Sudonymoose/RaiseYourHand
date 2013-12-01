package com.raiseyourhand.instructor;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.raiseyourhand.Login;
import com.raiseyourhand.R;
import com.raiseyourhand.RaiseYourHandApp;
import com.ws.Request;
import com.ws.RequestType;
import com.ws.local.SendRequest;
import com.ws.local.ServerResponseListener;


/**
 * Activity for beginning a quiz on the instructor's side
 * 
 * @author Hanrui Zhang
 * Page 35- 38
 */
public class OngoingQuiz extends Activity {
	private ImageView question;
	private Button end_quiz;
	private TextView time_left;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instructor_quiz_ongoing);
		// Show the Up button in the action bar.
		setupActionBar();
		
		// Check for logged in users.
		if (RaiseYourHandApp.getUsername() == null) {
			RaiseYourHandApp.logout();
			Intent login = new Intent(this, Login.class);
			startActivity(login);
		}
		
		// Get bundle from Quiz that passed stuff into this QuizActivity
				Bundle extras = getIntent().getExtras();
				
				// Get byteArray, convert to bitmap, and set ImageView question's image accordingly
				Uri imageUri = (Uri) extras.get("Quiz ImageUri");
				String time_given = extras.getString("Quiz Time");
				
				question = (ImageView) findViewById(R.id.instructor_quiz_ongoing_imageView);
				try {
					Bitmap b = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
					question.setImageBitmap(Bitmap.createScaledBitmap(b, 120, 120, false));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				// Set image path from the server
				end_quiz = (Button) findViewById(R.id.instructor_quiz_ongoing_button);
				end_quiz.setOnClickListener(new EndQuizOnClickListener());
				
				time_left = (TextView) findViewById(R.id.instructor_quiz_ongoing_timerTextView);
				time_left.setText(time_given);
				
				int set_min = Integer.parseInt(time_given.substring(0, 2));
				int set_sec = Integer.parseInt(time_given.substring(3));
				
				CountDownTimer timer = new CountDownTimer(set_min * 1000 * 60 + set_sec * 1000, 1000){
					public void onTick(long millisUntilFinished){
						time_left.setText("seconds remaining: " + millisUntilFinished / 1000);
					}
					public void onFinish(){
						time_left.setText("Time's Up");
						setResult(RESULT_OK);
						finish();
					}
				};
				timer.start();
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
			final Dialog end = new Dialog(com.raiseyourhand.instructor.OngoingQuiz.this);
			end.setContentView(R.layout.dialog_instructor_end_quiz);
			
			Button yes = (Button) end.findViewById(R.id.instructor_end_quiz_btn_yes);
			Button no = (Button) end.findViewById(R.id.instructor_end_quiz_btn_no);
			
			// Yes, end quiz
			yes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					// Tell server to end the quiz
					Object[] args = new Object[1]; // TODO: PRobably lecture id?
					SendEndQuizServerResponseListener listener = new SendEndQuizServerResponseListener();
					SendRequest sendEndQuizRequest = new SendRequest(new Request(RequestType.SEND_END_QUIZ, args), listener);
					sendEndQuizRequest.execute((Void) null);
					
					end.dismiss();					
					setResult(RESULT_OK);
					// Go back to LectureActivity
					(OngoingQuiz.this).finish();
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
	
	/**
	 * Private method to tells erver to end quiz
	 */
	private void endQuizToServer() {
		Object[] args = new Object[]{RaiseYourHandApp.getCourseNum()};
		SendEndQuizServerResponseListener listener = new SendEndQuizServerResponseListener();
		SendRequest sendEndQuizRequest = new SendRequest(new Request(RequestType.SEND_END_QUIZ, args), listener);
		sendEndQuizRequest.execute((Void) null);
	}
	
	/**
	 * Private sub-class to respond to server's response when telling server to end quiz
	 */
	private class SendEndQuizServerResponseListener implements ServerResponseListener {

		@Override
		public boolean onResponse(Request r) {
			Object[] args = r.getArgs();
			
			// Just make sure server got the message correctly
			try{
				if(((String)args[0]).equals(Request.FAILURE))
				{
					return false;
				}
				else if(((String)args[0]).equals(Request.SUCCESS))
				{
					return true;
				}
			} catch(ClassCastException e) {
				return false;
			}
			return false;
		}
	}
}
