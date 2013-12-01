package com.raiseyourhand.fragment;

import java.util.ArrayList;

import com.raiseyourhand.R;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

/**
 * A instructor shared fragment representing a section of the app, where
 * the notes shared by the instructor are listed.
 * 
 * Page 49-50, 65-66
 */
public class InstructorSharedFragment extends Fragment{
	private ListView shared_item;
	private Uri imageUri;
	private ArrayAdapter<String> itemAdapter;
	private ArrayList<String> all_items = new ArrayList<String>();
	private String selectedFromList;
	private ImageButton share_button;
	private SharedItemSelectedListener passToActivity;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_instructor_shared, container, false);

		shared_item = (ListView) (rootView).findViewById(R.id.instructor_share_listview);
		itemAdapter = new ArrayAdapter<String>(getActivity(), R.layout.instructor_shared_item, all_items);
		shared_item.setAdapter(itemAdapter);

		//Quick Click for Preview
		shared_item.setOnItemClickListener(new ShareItemListener());
		//Long Click for Deletion
		shared_item.setOnItemLongClickListener(new DeletionListener());

		share_button = (ImageButton) rootView.findViewById(R.id.instructor_share_button);
		if(getActivity().equals(com.raiseyourhand.instructor.Lecture.class)){
			share_button.setOnClickListener(new AddItemListener());
		}else{
			share_button.setEnabled(false);
			share_button.setVisibility(View.GONE);
		}
		//use this interface to send the names of the shared items back to the activity 
		//TODO-also upload them to server
		passToActivity.passData(all_items);
		return rootView;
	}

	//check the implementation of the interface
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			passToActivity = (SharedItemSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement SharedItemSelectedListener");
		}

	}

	private class ShareItemListener implements ListView.OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
		
		}
	}

	private class DeletionListener implements ListView.OnItemLongClickListener{
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			selectedFromList = (String) (shared_item.getItemAtPosition(arg2));
			/**
			 * So how does the Delete Dialog pops up. 
			 * Right now looks like if you click the item it will just ask for deletion
			 * What if u actually want to open it?
			 */
			final Dialog deletion = new Dialog(getActivity());
			deletion.setContentView(R.layout.dialog_instructor_delete_note);

			Button yes = (Button) deletion.findViewById(R.id.instructor_delete_note_btn_yes);
			Button no = (Button) deletion.findViewById(R.id.instructor_delete_note_btn_no);

			yes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					all_items.remove(selectedFromList);
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
			return true;
		}
	}


	private class AddItemListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			
		}

	}
	/**
	 * An interface to pass data from the fragments to the activity
	 * might be unnecessary as I walked through, but a good attempt to use interface though
	 * @author Hanrui Zhang
	 *
	 */
	public interface SharedItemSelectedListener {
		public void passData(ArrayList<String> all_items);
	}  
}
