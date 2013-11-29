package com.raiseyourhand.fragment;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.raiseyourhand.R;
import com.raiseyourhand.util.FileDialog;

/**
 * A student shared fragment representing a section of the app, where
 * notes shared by students are listed.
 * P62 or sth
 */
public class StudentSharedFragment extends Fragment {
	private ListView shared_item;
	private ImageButton add_note;
	private Uri imageUri;
	protected static final int REQUEST_SAVE = 0;
	protected static final int REQUEST_LOAD = 1;
	protected static final int SHARE_PICTURE_REQUEST = 2;
	private ArrayAdapter<String> itemAdapter;
	private int count;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_student_shared, container, false);

		//count = savedInstanceState.getInt("Image#");

		shared_item = (ListView) rootView.findViewById(R.id.student_student_share_listview);
		shared_item.setOnItemClickListener(new SharedItemSelectedListener());

		itemAdapter = new ArrayAdapter<String>(getActivity(), R.layout.student_shared_item);
		itemAdapter.notifyDataSetChanged();
		shared_item.setAdapter(itemAdapter);

		add_note = (ImageButton) rootView.findViewById(R.id.student_share_button);
		add_note.setOnClickListener(new AddNoteListener());

		/* // only when students submit the quiz answer, maybe use bundle
		final Dialog quiz_result = new Dialog(getActivity());
		quiz_result.setContentView(R.layout.dialog_student_quiz_result);

		ImageView result = (ImageView) quiz_result.findViewById(R.id.student_quiz_result_imageView);

		//update path here
		Bitmap bmImg = BitmapFactory.decodeFile("path of your img1");
		result.setImageBitmap(bmImg);

		quiz_result.show();
		 */
		return rootView;
	}

	private class SharedItemSelectedListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			String item_name = (String) shared_item.getItemAtPosition(arg2);
			final Dialog share_pic = new Dialog(getActivity());
			share_pic.setContentView(R.layout.dialog_student_share_item);

			ImageView shared_pic = (ImageView) share_pic.findViewById(R.id.student_share_item_imageView);

			Button yes = (Button) share_pic.findViewById(R.id.student_share_item_btn_yes);
			Button no = (Button) share_pic.findViewById(R.id.student_share_item_btn_no);

			yes.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO: send the picture to the server
					Toast.makeText(getActivity().getBaseContext(), imageUri.toString(), Toast.LENGTH_LONG).show();
					share_pic.dismiss();
				}
			});

			no.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					share_pic.dismiss();
				}
			});

			imageUri = Uri.parse(item_name);

			try {
				Bitmap picture = Media.getBitmap(getActivity().getContentResolver(), imageUri);
				shared_pic.setImageBitmap(picture);
			} catch (Exception e) {
				Toast.makeText(getActivity().getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
			}

			share_pic.show();
			/*if we use another thread
			String imageId = convertImageUriToFile(imageUri, getActivity());
			new LoadImagesFromSDCard().execute("" + imageId);
			 */
		}
	}


	private class AddNoteListener implements OnClickListener{
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
						String pic_name = "Student_Share_" + count + ".jpg";
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
				String pic_Name = "Student_Share_" + count + ".jpg";
				itemAdapter.add(pic_Name);
				itemAdapter.notifyDataSetChanged();
				shared_item.setAdapter(itemAdapter);
				Toast.makeText(getActivity().getBaseContext(), 
						"Picture Taken" + pic_Name + shared_item.getCount(), Toast.LENGTH_SHORT).show();
			}else {
				count--;
				Toast.makeText(getActivity().getBaseContext(), 
						" Picture was not taken ", Toast.LENGTH_SHORT).show();
			}

			//more file operations to be updated
			if (requestCode == REQUEST_SAVE || requestCode == REQUEST_LOAD) {
				if (resultCode == Activity.RESULT_OK) {
					String filePath = data.getStringExtra(FileDialog.RESULT_PATH);

				}
			}
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (imageUri != null) 
			outState.putString("ImageURI", imageUri.toString());
		if(count != 0)
			outState.putInt("Image#", count);
	}

	@Override
	public void onActivityCreated (Bundle savedInstanceState){
		
	}
	
	/*
	public static String convertImageUriToFile ( Uri imageUri, FragmentActivity activity )  {

		Cursor cursor = null;
		int imageID = 0;
		try {
			String [] proj={
					MediaStore.Images.Media.DATA,
					MediaStore.Images.Media._ID,
			};

			cursor = activity.managedQuery(
					imageUri,         //  Get data for specific image URI
					proj,             //  Which columns to return
					null,             //  WHERE clause; which rows to return (all rows)
					null,             //  WHERE clause selection arguments (none)
					null              //  Order-by clause (ascending by name)
					);

			//  Get Query Data
			int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
			int size = cursor.getCount();

			if (size !=0 ){
				if (cursor.moveToFirst()) 
					imageID = cursor.getInt(columnIndex);
			}    
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return "" + imageID;
	}


	//Load the image from background 
	public class LoadImagesFromSDCard extends AsyncTask<String, Void, Void> {

		final Dialog share_picture = new Dialog(getActivity());
		ImageView shared_pic;
		Button yes;
		Button no;
		Bitmap mBitmap;

		protected void onPreExecute() {
			// Progress Dialog
			share_picture.setContentView(R.layout.dialog_student_share_item);

			shared_pic = (ImageView) share_picture.findViewById(R.id.student_share_item_imageView);
			yes = (Button) share_picture.findViewById(R.id.student_share_item_btn_yes);
			no = (Button) share_picture.findViewById(R.id.student_share_item_btn_no);

			yes.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO: send the picture to the server

				}
			});

			no.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					share_picture.dismiss();
				}
			});
			share_picture.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			Uri uri = null; 
			try {
				uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + params[0]);
				mBitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));		
				shared_pic.setImageBitmap(mBitmap);
			} catch (Exception e) {
				cancel(true);
				Toast.makeText(getActivity().getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
			}
			return null;
		}

		protected void onPostExecute(Void unused) {
			// NOTE: You can call UI Element here.
			if(mBitmap != null){
				// Set Image to ImageView  
				shared_pic.setImageBitmap(mBitmap);
			}  
		}
	}
	 */


}