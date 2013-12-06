package com.raiseyourhand.fragment;

import java.io.File;
import java.util.ArrayList;

import ws.Request;
import ws.RequestType;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.raiseyourhand.R;
import com.raiseyourhand.RaiseYourHandApp;
import com.raiseyourhand.util.FileDialog;
import com.ws.local.SendRequest;
import com.ws.local.ServerResponseListener;

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
	protected static final int REQUEST_SAVE = 0;
	protected static final int REQUEST_LOAD = 1;
	protected static final int SHARE_PICTURE_REQUEST = 2;
	private PassItemListener passToActivity;
	private int count;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_instructor_shared, container, false);

		shared_item = (ListView) (rootView).findViewById(R.id.instructor_share_listview);
		itemAdapter = new ArrayAdapter<String>(getActivity(), R.layout.instructor_shared_item, all_items);
		shared_item.setAdapter(itemAdapter);

		//Quick Click for Preview
		shared_item.setOnItemClickListener(new  SharedItemSelectedListener ());
		//Long Click for Deletion
		shared_item.setOnItemLongClickListener(new DeletionListener());

		share_button = (ImageButton) rootView.findViewById(R.id.instructor_share_button);
		if(!RaiseYourHandApp.getIsStudent()){
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
			passToActivity = (PassItemListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement SharedItemSelectedListener");
		}

	}

	private class  SharedItemSelectedListener  implements ListView.OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			String item_name = (String) shared_item.getItemAtPosition(arg2);
			final Dialog share_pic = new Dialog(getActivity());
			share_pic.setContentView(R.layout.dialog_instructor_share_item);

			ImageView shared_pic = (ImageView) share_pic.findViewById(R.id.instructor_share_item_imageview);

			Button yes = (Button) share_pic.findViewById(R.id.instructor_share_item_btn_yes);
			Button no = (Button) share_pic.findViewById(R.id.instructor_share_item_btn_no);

			yes.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO: send the picture to the server

					share_pic.dismiss();
				}
			});

			no.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					share_pic.dismiss();
				}
			});

			if(item_name.endsWith("jpg")||item_name.endsWith("jpeg")||item_name.endsWith("png")){
				//hard-code to distinguish file selected or from camera
				if(!item_name.contains("sdcard"))
					imageUri = Uri.parse(Environment.getExternalStorageDirectory() + "/" + item_name);
				else
					imageUri = Uri.parse(item_name);

				try {
					shared_pic.setImageURI(imageUri);
				} catch (Exception e) {
					Toast.makeText(getActivity().getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
			share_pic.show();

			/*if we use another thread
			String imageId = convertImageUriToFile(imageUri, getActivity());
			new LoadImagesFromSDCard().execute("" + imageId);
			 */
		}
	}

	private class DeletionListener implements ListView.OnItemLongClickListener{
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			selectedFromList = (String) (shared_item.getItemAtPosition(arg2));

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
		private String selected; 

		@Override
		public void onClick(View arg0) {
			AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
			builderSingle.setIcon(R.drawable.ic_launcher);
			builderSingle.setTitle("Select Method");
			final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
					getActivity(),android.R.layout.select_dialog_singlechoice);
			arrayAdapter.add("Picture by Camera");
			arrayAdapter.add("Upload from File");
			builderSingle.setSingleChoiceItems(arrayAdapter, -1, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					selected = arrayAdapter.getItem(which);
				}
			});

			builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			builderSingle.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

					if(selected == null){
						dialog.dismiss();
					}else if(selected.equals("Picture by Camera")){
						dialog.dismiss();
						count++;
						String pic_name = "Instructor_Share_" + count + ".jpg";
						takePicture(pic_name);
					}else if(selected.equals("Upload from File")){
						//still need to play around with this FileDialog
						dialog.dismiss();
						Intent choose = new Intent(getActivity().getBaseContext(), 
								com.raiseyourhand.util.FileDialog.class);
						choose.putExtra(FileDialog.START_PATH, Environment.getExternalStorageDirectory());
						//can user select directories or not
						choose.putExtra(FileDialog.CAN_SELECT_DIR, true);
						//alternatively, set file filter
						//intent.putExtra(FileDialog.FORMAT_FILTER, new String[] { "png" });
						startActivityForResult(choose, REQUEST_SAVE);

					}else{
						dialog.dismiss();
					}
				}
			});

			builderSingle.show();
		}

	}

	protected void takePicture(String pic_name){
		File f = new File(Environment.getExternalStorageDirectory(),  pic_name);
		imageUri = Uri.fromFile(f);
		Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(intent, SHARE_PICTURE_REQUEST);
	}

	public void onActivityResult(final int requestCode,
			int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SHARE_PICTURE_REQUEST) {
			if (resultCode == Activity.RESULT_OK) {
				//If do not use background thread
				//pic_name = data.getExtras().get("data").toString();

				//hard-coded
				String pic_Name = "Instructor_Share_" + count + ".jpg";
				itemAdapter.add(pic_Name);
				itemAdapter.notifyDataSetChanged();
				shared_item.setAdapter(itemAdapter);
				Toast.makeText(getActivity().getBaseContext(), 
						"Picture Taken", Toast.LENGTH_SHORT).show();
			}else {
				count--;
				Toast.makeText(getActivity().getBaseContext(), 
						" Picture was not taken ", Toast.LENGTH_SHORT).show();
			}
		}
		//more file operations to be updated
		if (requestCode == REQUEST_SAVE || requestCode == REQUEST_LOAD) {
			if (resultCode == Activity.RESULT_OK) {
				String filePath = data.getStringExtra(FileDialog.RESULT_PATH);
				Toast.makeText(getActivity().getBaseContext(), 
						"File: " + filePath, Toast.LENGTH_SHORT).show();
				itemAdapter.add(filePath);
				itemAdapter.notifyDataSetChanged();
				shared_item.setAdapter(itemAdapter);
			}
		}


	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(count != 0)
			outState.putInt("Image#", count);
		if(!itemAdapter.isEmpty()){
			int item_count = itemAdapter.getCount();
			for (int i = 0; i < item_count; i++)
				outState.putString("item_" + i, itemAdapter.getItem(i));
			outState.putInt("Item Size", item_count);
		}
	}

	/**
	 * Private method, call this to send the instructor note to the server
	 * 
	 * Not called by the demo, since it makes a connection to the server.
	 */
	private void sendNoteToServer() {
		Object[] args = new Object[3];
		args[0] = RaiseYourHandApp.getCourseNum();
		args[1] = null; // Name of note
		args[2] = null; // note itself
		SendInstructorNoteServerResponseListener listener = new  SendInstructorNoteServerResponseListener();
		SendRequest sendInstructorNoteRequest = new SendRequest(new Request(RequestType.SEND_INSTRUCTOR_NOTE, args), listener);
	}

	/**
	 * Private sub-class to respond to server's response when sending instructor's note to server
	 * 
	 * Not called by the demo, since it makes a connection to the server.
	 */
	private class SendInstructorNoteServerResponseListener implements ServerResponseListener {

		@Override
		public boolean onResponse(Request r) {
			Object[] args = r.getArgs();

			// Just make sure server got the message correctly
			try{
				if(((String)args[0]).equals(Request.FAILURE)){
					return false;
				}else if(((String)args[0]).equals(Request.SUCCESS)){
					return true;
				}
			} catch(ClassCastException e) {
				return false;
			}
			return false;
		}

	}

	/**
	 * An interface to pass data from the fragments to the activity
	 * might be unnecessary as I walked through, but a good attempt to use interface though
	 * 
	 * @author Hanrui Zhang
	 */
	public interface PassItemListener {
		public void passData(ArrayList<String> all_items);
	}  
}
