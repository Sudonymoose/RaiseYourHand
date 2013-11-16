package com.raiseyourhand.fragment;

import android.support.v4.app.Fragment;

import com.raiseyourhand.R;

public class QuestionFragment extends Fragment{
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_instructor_question, container, false);

		return rootView;
	}
}
