package com.raiseyourhand.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.raiseyourhand.R;
/**
 * 
 * @author Hanrui Zhang
 * P81 -- 86, but I think two tabs need different layout? one is textview one is listview?
 */
public class QuizAnswerFragment extends ListFragment {
	private Dialog dialog_submit;
	private Button button_submit;

	public QuizAnswerFragment() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_student_quiz_answer, container, false);
		button_submit = (Button) rootView.findViewById(R.id.student_quiz_button_1);

		button_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_submit = new Dialog(getActivity().getBaseContext());
				dialog_submit.setContentView(R.layout.dialog_student_submit_quiz);

				Button yes = (Button) dialog_submit.findViewById(R.id.student_submit_quiz_btn_yes);
				Button no = (Button) dialog_submit.findViewById(R.id.student_submit_quiz_btn_no);

				yes.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

					}

				});

				no.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog_submit.dismiss();
					}

				});
				dialog_submit.show();
			}

		});

		return rootView;
	}
}