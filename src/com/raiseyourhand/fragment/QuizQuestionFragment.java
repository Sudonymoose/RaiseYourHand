package com.raiseyourhand.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raiseyourhand.R;

/**
 * A instructor shared fragment representing a section of the app, where
 * the notes shared by the instructor are listed.
 */
public class QuizQuestionFragment extends ListFragment {

	
	public QuizQuestionFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_student_quiz_question,
				container, false);

		
		
		return rootView;
	}
}