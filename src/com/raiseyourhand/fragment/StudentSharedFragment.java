package com.raiseyourhand.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.raiseyourhand.R;

public class StudentSharedFragment extends Fragment {
	private Button attendance;
	private Button quiz;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_instructor_student_shared, container, false);
		attendance = (Button) rootView.findViewById(R.id.instructor_lecture_attendance_button_3);
		quiz = (Button) rootView.findViewById(R.id.instructor_lecture_quiz_button_3);
		
		return rootView;
	}
}
