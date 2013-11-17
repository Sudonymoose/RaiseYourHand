package com.raiseyourhand.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raiseyourhand.R;
/**
 * A student shared fragment representing a section of the app, where
 * notes shared by students are listed.
 */
public class QuizAnswerFragment extends ListFragment {
	public QuizAnswerFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_student_quiz_answer,
				container, false);
		return rootView;
	}
}