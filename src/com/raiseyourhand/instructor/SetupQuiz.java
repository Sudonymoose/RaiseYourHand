package com.raiseyourhand.instructor;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.raiseyourhand.R;
import com.raiseyourhand.Login;
import com.raiseyourhand.RaiseYourHandApp;
import com.raiseyourhand.util.FileDialog;
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
	private TextView time_set;
	private int count;
	private Uri imageUri;
	protected static final int REQUEST_SAVE = 0;
	protected static final int REQUEST_LOAD = 1;
	protected static final int SHARE_PICTURE_REQUEST = 2;
	protected static final int START_QUIZ = 3;
	private boolean timer_set;

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


		if(savedInstanceState != null){
			count = savedInstanceState.getInt("Image#");
			imageUri = Uri.parse(savedInstanceState.getString("URI"));
		}
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
			if(timer_set){
				// Pass the quiz screenshot into the intent
				//Do not pass bitmap bytearray here, too much memory and guaranteed not working
				if(imageUri != null){
					Intent beginQuizIntent = new Intent(SetupQuiz.this, OngoingQuiz.class);
					beginQuizIntent.putExtra("Quiz ImageUri", imageUri);
					beginQuizIntent.putExtra("Quiz Time", time_set.getText().toString());

					//TODO in what form do we send those options for quiz

					// Tell server that this quiz has started
					Object[] args = new Object[1]; // TODO: probably lecture id?
					SendStartQuizServerResponseListener listener = new SendStartQuizServerResponseListener();
					SendRequest sendStartQuizRequest = new SendRequest(new Request(RequestType.SEND_START_QUIZ, args), listener);
					sendStartQuizRequest.execute((Void)null);


					//Send image to server (in bytearray), and the time/options for the quiz
					startActivityForResult(beginQuizIntent, START_QUIZ);
				}else{
					Toast.makeText(SetupQuiz.this.getBaseContext(), "No quiz image available",
							Toast.LENGTH_SHORT).show();
				}
			}else{
				final Dialog set_time = new Dialog(SetupQuiz.this);
				set_time.setContentView(R.layout.dialog_set_time);

				Button set = (Button) set_time.findViewById(R.id.set_time_button);
				Button cancel = (Button) set_time.findViewById(R.id.cancel_time_button);
				final NumberPicker minutes = (NumberPicker) set_time.findViewById(R.id.NumberPicker_minute);
				final NumberPicker seconds = (NumberPicker) set_time.findViewById(R.id.NumberPicker_second);

				minutes.setMaxValue(59);
				minutes.setMinValue(0);

				seconds.setMaxValue(59);
				seconds.setMinValue(0);

				set.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View arg0) {
						int set_min = minutes.getValue();
						int set_sec = seconds.getValue();
						if(set_min + set_sec != 0){
							time_set.setText(String.format("%02d", set_min) + ":" + String.format("%02d", set_sec));
							timer_set = true;
							begin_quiz_button.setText(R.string.instructor_quiz_begin_button);
						}else{
							time_set.setText("Please set time again");
						}
						set_time.dismiss();		
					}

				});

				cancel.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View arg0) {
						set_time.dismiss();				
					}
				});

				set_time.show();
			}



		}
	}

	/**
	 * OnClickListener for when the upload screenshot button is clicked
	 */
	private class UploadScreenshotOnClickListener implements OnClickListener
	{
		private String selected;
		@Override
		public void onClick(View v) {
			AlertDialog.Builder builderSingle = new AlertDialog.Builder(SetupQuiz.this);
			builderSingle.setIcon(R.drawable.ic_launcher);
			builderSingle.setTitle("Select Method");
			final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
					SetupQuiz.this,android.R.layout.select_dialog_singlechoice);
			arrayAdapter.add("Picture by Camera");
			arrayAdapter.add("Upload from File");
			builderSingle.setSingleChoiceItems(arrayAdapter, -1, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					selected = arrayAdapter.getItem(which);
				}
			});

			builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			builderSingle.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

					if(selected == null){
						dialog.dismiss();
					}else if(selected.equals("Picture by Camera")){
						dialog.dismiss();
						count++;
						String pic_name = "Quiz_" + count + ".jpg";
						takePicture(pic_name);
					}else if(selected.equals("Upload from File")){
						//still need to play around with this FileDialog
						dialog.dismiss();
						Intent choose = new Intent(SetupQuiz.this.getBaseContext(), 
								com.raiseyourhand.util.FileDialog.class);
						choose.putExtra(FileDialog.START_PATH, Environment.getExternalStorageDirectory());
						//can user select directories or not
						choose.putExtra(FileDialog.CAN_SELECT_DIR, true);
						//alternatively, set file filter
						//intent.putExtra(FileDialog.FORMAT_FILTER, new String[] { "png" });
						startActivityForResult(choose, REQUEST_SAVE);

					}else{
						dialog.dismiss();
					}
				}
			});

			builderSingle.show();
		}

	}

	protected void takePicture(String pic_name){
		File f = new File(Environment.getExternalStorageDirectory(),  pic_name);
		imageUri = Uri.fromFile(f);
		Toast.makeText(getBaseContext(), 
				pic_name, Toast.LENGTH_SHORT).show();
		Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(intent, SHARE_PICTURE_REQUEST);
	}

	public void onActivityResult(final int requestCode,
			int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SHARE_PICTURE_REQUEST) {
			if (resultCode == Activity.RESULT_OK) {
				//If do not use background thread
				//pic_name = data.getExtras().get("data").toString();

				try{
					Bitmap b = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
					quiz_image.setImageBitmap(Bitmap.createScaledBitmap(b, 120, 120, false));
					Toast.makeText(getBaseContext(), 
							"Picture Taken " + imageUri.toString(), Toast.LENGTH_SHORT).show();
				}catch(Exception e){
					Toast.makeText(getBaseContext(), e.getMessage(), 
							Toast.LENGTH_SHORT).show();
				}

			}else {
				count--;
				Toast.makeText(getBaseContext(), 
						" Picture was not taken ", Toast.LENGTH_SHORT).show();
			}
		}//more file operations to be updated
		if (requestCode == REQUEST_SAVE || requestCode == REQUEST_LOAD) {
			if (resultCode == Activity.RESULT_OK) {
				String filePath = data.getStringExtra(FileDialog.RESULT_PATH);
				File f = new File(filePath);
				imageUri = Uri.fromFile(f);
				Toast.makeText(getBaseContext(), 
						"File: " + imageUri.toString(), Toast.LENGTH_SHORT).show();
				try{
					Bitmap b = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
					quiz_image.setImageBitmap(Bitmap.createScaledBitmap(b, 120, 120, false));
				}catch(Exception e){
					Toast.makeText(getBaseContext(), e.getMessage(), 
							Toast.LENGTH_SHORT).show();
				}				
			}
		}
		if (requestCode == START_QUIZ){
			if (resultCode == Activity.RESULT_OK) {
				//go back to fragments
				setResult(RESULT_OK);
				onBackPressed();
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(count != 0)
			outState.putInt("Image#", count);
		if(imageUri != null)
			outState.putString("URI", imageUri.toString());
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
