package com.raiseyourhand.instructor;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
 * P33, 34
 * @author Hanrui Zhang
 *
 */
public class SetupQuiz extends Activity {
	
	private boolean choose_bluetooth;
	private boolean choose_builtin;
	private Button begin_quiz_button;
	private Button upload_screenshot_button;
	private ImageView quiz_image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instructor_quiz);
		// Show the Up button in the action bar.
		setupActionBar();
	
		// Check for logged in users.
		if (RaiseYourHandApp.getUsername() == null) {
			RaiseYourHandApp.logout();
			Intent login = new Intent(this, Login.class);
			startActivity(login);
		}
		
		// Set up the start_quiz_button
		begin_quiz_button = (Button) findViewById(R.id.instructor_quiz_button_begin);
		begin_quiz_button.setOnClickListener(new BeginQuizOnClickListener());
		
		// Set up the take_screenshot_button
		upload_screenshot_button = (Button) findViewById(R.id.instructor_quiz_button_upload);
		upload_screenshot_button.setOnClickListener(new UploadScreenshotOnClickListener());
		
		// Set up the ImageView
		quiz_image = (ImageView) findViewById(R.id.instructor_quiz_imageView);
		
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
		case R.id.action_back:
			//finishActivity(0);
			onBackPressed();
			return true;
		case R.id.action_mic:
			final Dialog mic_setting = new Dialog(SetupQuiz.this);
			mic_setting.setContentView(R.layout.dialog_instructor_set_mic);
			Button mic_set = (Button) mic_setting.findViewById(R.id.instructor_set_mic_btn);
			TextView bluetooth = (TextView) mic_setting.findViewById(R.id.instructor_set_mic_bluetooth_text);
			TextView builtin = (TextView) mic_setting.findViewById(R.id.instructor_set_mic_builtin_text);



			bluetooth.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					choose_bluetooth = !choose_bluetooth;
					choose_builtin = !choose_builtin;
				}
			});

			builtin.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					choose_bluetooth = !choose_bluetooth;
					choose_builtin = !choose_builtin;
				}
			});

			mic_set.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					//set microphone
					if(choose_bluetooth){
						BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
						if (mBluetoothAdapter == null) {
							//it's actually quite complicated to connect to the Bluetooth server
						}
					}else if(choose_builtin){
						//looks like we need another dialog to record?
					}
					mic_setting.dismiss();
				}

			});
			mic_setting.show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * OnClickListener for when the begin quiz button is clicked
	 */
	private class BeginQuizOnClickListener implements OnClickListener
	{

		@Override
		public void onClick(View arg0) {
			
			// Create an intent for calling OngoingQuiz (aka starting a quiz)
			Intent beginQuizIntent = new Intent(SetupQuiz.this, OngoingQuiz.class);
			
			// Pass the quiz screenshot into the intent
			// Used stackoverflow.com/questions/11519691/passing-image-from-one-activity-another-activity
			Bitmap bitmap = ((BitmapDrawable)quiz_image.getDrawable()).getBitmap();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] byteArray = stream.toByteArray();
			beginQuizIntent.putExtra("Quiz Image", byteArray);
			
			// Tell server that this quiz has started
			Object[] args = new Object[1]; // TODO: probably lecture id?
			SendStartQuizServerResponseListener listener = new SendStartQuizServerResponseListener();
			SendRequest sendStartQuizRequest = new SendRequest(RequestType.SEND_START_QUIZ, listener, args);
			sendStartQuizRequest.execute((Void)null);
			
			
			startActivity(beginQuizIntent);
		}
	}
	
	/**
	 * OnClickListener for when the upload screenshot button is clicked
	 */
	private class UploadScreenshotOnClickListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Go to an activity that chooses an image or use the camera?
			
			// TODO Change quiz_image to the screenshot taken, if it's been taken
			
		}
		
	}
	
	/**
	 * Private sub-class to respond to server's response when telling server to start quiz
	 */
	private class SendStartQuizServerResponseListener implements ServerResponseListener {

		@Override
		public boolean onResponse(Request r) {
			// TODO Make sure server got message correctly?
			return false;
		}
	}

}
