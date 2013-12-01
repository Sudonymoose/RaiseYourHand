package com.raiseyourhand.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.raiseyourhand.R;

/**
 * A instructor shared fragment representing a section of the app, where
 * the notes shared by the instructor are listed.
 */
public class QuizQuestionFragment extends Fragment {

	public QuizQuestionFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_student_quiz_question,
				container, false);
		ImageView question_image = (ImageView) rootView.findViewById(R.id.student_quiz_imageView);
		String imgPath = getArguments().getString("imgPath");

		//set quiz image here

		try{
			question_image.setImageURI(Uri.parse(imgPath));
		}catch(Exception e){
			Toast.makeText(getActivity().getBaseContext(), "Cannot load picture",
					Toast.LENGTH_SHORT).show();
		}
		//new DownloadImageTask(question_image).execute(img_path);

		return rootView;
	}
}