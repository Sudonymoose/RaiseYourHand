package com.raiseyourhand.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.raiseyourhand.R;


/**
 * A instructor shared fragment representing a section of the app, where
 * the notes shared by the instructor are listed.
 * 
 * Page 49-50, 65-66
 */
public class InstructorSharedFragment extends Fragment{
	private ListView shared_listView;
	private Dialog deletion;
	private Dialog share_file;
	private ArrayAdapter<String> itemAdapter;
	private List<String> items = new ArrayList<String>();
	private String selectedFromList;
	private Button share_button;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_instructor_shared, container, false);

		shared_listView = (ListView) (rootView).findViewById(R.id.instructor_share_listview);
		itemAdapter = new ArrayAdapter<String>(getActivity(), R.layout.instructor_shared_item, items);
		shared_listView.setAdapter(itemAdapter);
		shared_listView.setOnItemClickListener(new OnItemClickListener (){
			@Override
			public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
				selectedFromList = (String) (shared_listView.getItemAtPosition(myItemInt));
				/**
				 * So how does the Delete Dialog pops up. 
				 * Right now looks like if you click the item it will just ask for deletion
				 * What if u actually want to open it?
				 */
				deletion = new Dialog(getActivity().getBaseContext());
				deletion.setContentView(R.layout.dialog_instructor_delete_note);

				Button yes = (Button) deletion.findViewById(R.id.instructor_delete_note_btn_yes);
				Button no = (Button) deletion.findViewById(R.id.instructor_delete_note_btn_no);

				yes.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						items.remove(selectedFromList);
						itemAdapter.notifyDataSetChanged();
						//delete that on the server as well

					}

				});

				no.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						deletion.dismiss();
					}

				});

				deletion.show();
			}            
		});

		share_button = (Button) rootView.findViewById(R.id.instructor_share_button);
		share_button.setOnClickListener(new OnClickListener (){

			@Override
			public void onClick(View v) {
				share_file = new Dialog(getActivity());
				share_file.setContentView(R.layout.dialog_instructor_share_item);
				
				Button yes = (Button) share_file.findViewById(R.id.instructor_share_item_btn_yes);
				Button no = (Button) share_file.findViewById(R.id.instructor_share_item_btn_no);
				
				yes.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//share the file, upload to server
					}

				});

				no.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						share_file.dismiss();
					}

				});
				share_file.show();
			}
			
		});

		return rootView;
	}


}
