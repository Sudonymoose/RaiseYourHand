package com.raiseyourhand.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.raiseyourhand.R;
/**
 * 
 * @author Hanrui Zhang
 * P82 -- 86, but I think two tabs need different layout? one is textview one is listview?
 */
public class QuizAnswerFragment extends ListFragment {
	private Dialog dialog_submit;
	private Button button_submit;
	private ListView quiz_listview;
	private String selected;

	public QuizAnswerFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_student_quiz_answer, container, false);
		button_submit = (Button) rootView.findViewById(R.id.student_quiz_button_1);
		quiz_listview = (ListView) rootView.findViewById(R.id.student_quiz_listview);

		button_submit.setOnClickListener(new SubmitAnswerListener());
		quiz_listview.setOnItemClickListener(new QuizListItemListener());

		return rootView;
	}

	class QuizListItemListener implements ListView.OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			selected = (String) quiz_listview.getItemAtPosition(arg2);
		}
	}

	class SubmitAnswerListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			dialog_submit = new Dialog(getActivity().getBaseContext());
			dialog_submit.setContentView(R.layout.dialog_student_submit_quiz);

			Button yes = (Button) dialog_submit.findViewById(R.id.student_submit_quiz_btn_yes);
			Button no = (Button) dialog_submit.findViewById(R.id.student_submit_quiz_btn_no);

			yes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(selected != null){
						//submit the answer to server
						
					}
					//go back to the previous activity
					getActivity().onBackPressed();
					//maybe use Bundle to pass a flag
					dialog_submit.dismiss();
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

	}
}