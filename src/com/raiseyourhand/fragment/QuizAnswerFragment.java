package com.raiseyourhand.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.raiseyourhand.R;

/**
 * A student shared fragment representing a section of the app, where
 * notes shared by students are listed.
 */
public class QuizAnswerFragment extends Fragment {

	private Dialog dialog_submit;
	private Button button_submit;
	private ListView quiz_listview;
	private String selected;

	public QuizAnswerFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_student_quiz_answer,
				container, false);

		button_submit = (Button) rootView.findViewById(R.id.student_quiz_button_1);
		quiz_listview = (ListView) rootView.findViewById(R.id.student_quiz_listview);
		ArrayAdapter<String> optionAdapter = new ArrayAdapter<String>(getActivity(), R.layout.student_shared_item);
		//hard-coded as to get options from the instructor
		Bundle b = getArguments();
		int option_size = b.getInt("Option Size");
		for(int i = 1; i < option_size; i++)
			optionAdapter.add(i + " : "+ b.getString("Option_" + i));

		quiz_listview.setAdapter(optionAdapter);

		button_submit.setOnClickListener(new SubmitAnswerListener());
		quiz_listview.setOnItemClickListener(new QuizListItemListener());

		return rootView;
	}


	private class QuizListItemListener implements ListView.OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			selected = (String) quiz_listview.getItemAtPosition(arg2);
		}
	}

	private class SubmitAnswerListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			dialog_submit = new Dialog(getActivity());
			dialog_submit.setContentView(R.layout.dialog_student_submit_quiz);

			Button yes = (Button) dialog_submit.findViewById(R.id.student_submit_quiz_btn_yes);
			Button no = (Button) dialog_submit.findViewById(R.id.student_submit_quiz_btn_no);
			TextView confirmation = (TextView) dialog_submit.findViewById(R.id.student_submit_quiz_textview);

			String text = confirmation.getText().toString() + " : " + selected;
			confirmation.setText(text);

			yes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(selected != null){
						//submit the answer to server
						dialog_submit.dismiss();
						//go back to the previous activity
						getActivity().onBackPressed();
					}else{
						Toast.makeText(getActivity().getBaseContext(), "Please select the answer",
								Toast.LENGTH_SHORT).show();
						dialog_submit.dismiss();
					}
					//TODO: indicate the instructor the student has finished quiz

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