package com.raiseyourhand.fragment;

import com.raiseyourhand.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StudentInstructorSharedFragment extends Fragment{
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_student_instructor_shared, container, false);
		
		
		
		return rootView;
	}
}
