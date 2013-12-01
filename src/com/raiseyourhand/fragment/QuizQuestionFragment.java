package com.raiseyourhand.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.raiseyourhand.R;

/**
 * This fragment just simply displayed the quiz image from the instructor
 * 
 * P81
 */
public class QuizQuestionFragment extends ListFragment {

	private ImageView question_image;
	public QuizQuestionFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_student_quiz_question,
				container, false);
		question_image = (ImageView) rootView.findViewById(R.id.student_quiz_imageView);
		
		//set quiz image here
		String img_path = "";
		//new DownloadImageTask(question_image).execute(img_path);

		return rootView;
	}
	
}