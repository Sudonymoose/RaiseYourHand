package com.raiseyourhand.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raiseyourhand.R;

public class QuestionFragment extends Fragment{

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_instructor_question, container, false);
		return rootView;
	}
}
