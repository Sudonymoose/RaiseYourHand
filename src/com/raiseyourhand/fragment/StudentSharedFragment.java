package com.raiseyourhand.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.raiseyourhand.R;

/**
 * A student shared fragment representing a section of the app, where
 * notes shared by students are listed.
 * P62 or sth
 */
public class StudentSharedFragment extends Fragment {

	private Dialog quiz_result; 
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_student_shared, container, false);

		// only when students submit the quiz answer
		quiz_result = new Dialog(getActivity().getBaseContext());
		quiz_result.setContentView(R.layout.dialog_student_quiz_result);

		ImageView result = (ImageView) quiz_result.findViewById(R.id.student_quiz_result_imageView);
		
		//update path here
		Bitmap bmImg = BitmapFactory.decodeFile("path of your img1");
		result.setImageBitmap(bmImg);
		
		quiz_result.show();
		return rootView;
	}
}
