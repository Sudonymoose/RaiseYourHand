package com.raiseyourhand.fragment;

import java.util.ArrayList;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.raiseyourhand.R;

/**
 * Instructor Version
 * @author Hanrui Zhang
 * 
 */
public class QuestionFragment extends Fragment{

	private ListView questions;
	private ArrayAdapter<String> questionAdapter;
	private ArrayList<String> all_questions = new ArrayList<String>();
	private String selected_question;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_instructor_question, container, false);
		
		questions = (ListView) rootView.findViewById(R.id.question_listview);

		//TO-DO download the all_questions from the server
		all_questions.clear();
		all_questions.add("What's going on?");
		
		questionAdapter = new ArrayAdapter<String>(getActivity(), R.layout.instructor_shared_item, all_questions);
		questions.setAdapter(questionAdapter);
		
		questions.setOnItemClickListener(new ListView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				selected_question = (String) (questions.getItemAtPosition(arg2));
				
				final Dialog display = new Dialog(getActivity());
				display.setContentView(R.layout.dialog_instructor_receive_question);
				
				TextView student = (TextView) display.findViewById(R.id.instructor_receive_question_textview);
				//TODO-add the student name
				student.setText(R.string.instructor_dialog_receive_question);
				
				TextView content = (TextView) display.findViewById(R.id.instrucotr_receive_question_content_textView);
				//TODO-suppose to be actual question text here
				content.setText(selected_question);
				
				Button revoke = (Button) display.findViewById(R.id.instrucotr_receive_question_button);
				revoke.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						display.dismiss();
						final Dialog confirm = new Dialog(getActivity());
						confirm.setContentView(R.layout.dialog_instructor_ignore);
						
						Button yes = (Button) confirm.findViewById(R.id.instructor_ignore_btn_yes);
						Button no = (Button) confirm.findViewById(R.id.instructor_ignore_btn_no);
						
						yes.setOnClickListener(new OnClickListener(){
							@Override
							public void onClick(View v) {
								all_questions.remove(selected_question);
								questionAdapter.notifyDataSetChanged();
								
								//TODO-invalidate the link to this file on the server
								confirm.dismiss();
							}
						});
						
						no.setOnClickListener(new OnClickListener(){
							@Override
							public void onClick(View v) {
								confirm.dismiss();
							}
						});
						confirm.show();
					}
				});
				display.show();
			}
			
		});
		return rootView;
	}
}
