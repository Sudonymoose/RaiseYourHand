package com.raiseyourhand.student;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.raiseyourhand.R;
import com.ws.Request;
import com.ws.local.ServerResponseListener;
/**
 * Activity for a student asking a question, from the student's perspective
 * P76
 * 
 * 
 * 
 * @author Hanrui Zhang
 *
 */
public class Ask extends Activity {

	private EditText type_question;
	private ImageButton record_question_button;
	private Button end_question_button;

	private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
	private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
	private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
	private MediaRecorder recorder = null;
	private int currentFormat = 0;
	private int output_formats[] = { MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.OutputFormat.THREE_GPP };
	private String file_exts[] = { AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP };

	private int option_flag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_ask);

		type_question = (EditText) findViewById(R.id.student_ask_type_question);
		type_question.setOnClickListener(new TypeQuestionOnClickListener());
		
		record_question_button = (ImageButton) findViewById(R.id.student_ask_record_button);
		record_question_button.setOnClickListener(new RecordQuestionOnClickListener());

		end_question_button = (Button) findViewById(R.id.student_ask_end_question_button);
		end_question_button.setOnClickListener(new EndQuestionOnClickListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_ask, menu);
		return true;
	}

	
	private class TypeQuestionOnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			option_flag = 1;
		}
	}
	
	
	private class RecordQuestionOnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			option_flag = 0;
			startRecording();

			// Disable other options during recording to avoid 
			record_question_button.setEnabled(false);
			type_question.setEnabled(false);
			end_question_button.setEnabled(true);
		}
	}

	/**
	 * Listener class for when the end question button is clicked
	 */
	private class EndQuestionOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO End question and... do what activity?

			if(option_flag == 0){
				stopRecording();
				//send the audio file to server
			}else if(option_flag == 1){
				String questions = type_question.getText().toString();
				//send the String to server
				Toast.makeText(Ask.this, 
						" Question asked: " + questions, Toast.LENGTH_SHORT).show();
			}
			record_question_button.setEnabled(true);
			type_question.setEnabled(true);
			end_question_button.setEnabled(false);
			finish();
		}	
	}

	private void startRecording() {
		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(output_formats[currentFormat]);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOutputFile(getFilename());
		recorder.setOnErrorListener(errorListener);
		recorder.setOnInfoListener(infoListener);
		try {
			recorder.prepare();
			recorder.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getFilename() {
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath, AUDIO_RECORDER_FOLDER);
		if (!file.exists()) {
			file.mkdirs();
		}
		return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat]);
	}

	private void stopRecording() {
		if (null != recorder) {
			recorder.stop();
			recorder.reset();
			recorder.release();
			recorder = null;
			Toast.makeText(Ask.this, 
					" Question asked: ", Toast.LENGTH_SHORT).show();
		}
	}

	// Signals the recoding error
	private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
		@Override
		public void onError(MediaRecorder mr, int what, int extra) {
			Toast.makeText(Ask.this, 
					"Error: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
		}
	};

	// Indicates the progress of the recording
	private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
		@Override
		public void onInfo(MediaRecorder mr, int what, int extra) {
			Toast.makeText(Ask.this, 
					"Warning: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
		}
	};

	/**
	 * Private sub-class to respond to server's response when sending the student's question to the server
	 * 
	 * TODO: Where do we put this?
	 */
	private class SendQuestionServerResponseListener implements ServerResponseListener {

		@Override
		public boolean onResponse(Request r) {
			// TODO Know if message sending was successful?
			return false;
		}
	}
	
}
