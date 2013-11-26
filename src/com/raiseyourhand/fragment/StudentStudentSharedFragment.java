package com.raiseyourhand.fragment;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
public class StudentStudentSharedFragment extends Fragment {
	private ListView shared_item;
	private ImageButton add_note;
	private Uri imageUri;
	private Uri previewUri;
	protected static final int REQUEST_SAVE = 0;
	protected static final int REQUEST_LOAD = 1;
	protected static final int SHARE_PICTURE_REQUEST = 2;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_student_student_shared, container, false);
		// only when students submit the quiz answer, maybe use bundle
		
		shared_item = (ListView) rootView.findViewById(R.id.student_student_share_listview);
		shared_item.setOnItemClickListener(new SharedItemSelectedListener());

		add_note = (ImageButton) rootView.findViewById(R.id.student_share_button);
		add_note.setOnClickListener(new AddNoteListener());

		//only if the previous activity is quiz
		Dialog quiz_result = new Dialog(getActivity().getBaseContext());
		quiz_result.setContentView(R.layout.dialog_student_quiz_result);
		//ImageView result = (ImageView) quiz_result.findViewById(R.id.student_quiz_result_imageView);
		quiz_result.show();

		return rootView;
	}

	private class SharedItemSelectedListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			String item_name = (String) shared_item.getItemAtPosition(arg2);
			final Dialog preview = new Dialog(getActivity().getBaseContext());
			preview.setContentView(R.layout.dialog_student_share_preview);
			ImageView item_preview = (ImageView) preview.findViewById(R.id.student_share_item_preview);
			//loadImage from SDCard on preview using another thread
			String fileName = item_name + ".jpg";
			File f = new File(Environment.getExternalStorageDirectory(),  fileName);
			previewUri = Uri.fromFile(f);
			String imageID = convertImageUriToFile(previewUri, getActivity());
			new LoadPreviewFromSDCard().execute("" + imageID);

			item_preview.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					preview.dismiss();
				}
			});

		}

	}


	private class AddNoteListener implements OnClickListener{
		@Override
		public void onClick(View arg0) {
			AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity().getBaseContext());
			builderSingle.setIcon(R.drawable.ic_launcher);
			builderSingle.setTitle("Select Method");
			final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
					getActivity(),android.R.layout.select_dialog_singlechoice);
			arrayAdapter.add("Picture by Camera");
			arrayAdapter.add("Upload from File");
			builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			builderSingle.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String strName = arrayAdapter.getItem(which);
					if(strName.equals("Picture by Camera")){
						takePicture();
					}else if(strName.equals("Upload from File")){
						//still need to play around with this FileDialog
						Intent choose = new Intent(getActivity().getBaseContext(), FileDialog.class);
						choose.putExtra(FileDialog.START_PATH, Environment.getExternalStorageDirectory());
						//can user select directories or not
						choose.putExtra(FileDialog.CAN_SELECT_DIR, true);
						//alternatively, set file filter
						//intent.putExtra(FileDialog.FORMAT_FILTER, new String[] { "png" });
						startActivityForResult(choose, REQUEST_SAVE);

					}
				}
			});

			builderSingle.show();

		}
	}

	public void takePicture(){
		Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE);
		String fileName = "Student_Share_" + System.currentTimeMillis() + ".jpg";
		File f = new File(Environment.getExternalStorageDirectory(),  fileName);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		imageUri = Uri.fromFile(f);

		/**** EXTERNAL_CONTENT_URI : style URI for the "primary" external storage volume. ****/
		// Standard Intent action that can be sent to have the camera
		// application capture an image and return it.  
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult( intent, SHARE_PICTURE_REQUEST);
	}


	public synchronized void onActivityResult(final int requestCode,
			int resultCode, final Intent data) {

		if ( requestCode == SHARE_PICTURE_REQUEST) {
			if ( resultCode == Activity.RESULT_OK) {
				//If do not use background thread
				final Dialog share_picture = new Dialog(getActivity().getBaseContext());
				share_picture.setContentView(R.layout.dialog_student_share_item);
				ImageView shared_pic = (ImageView) share_picture.findViewById(R.id.student_share_item_imageView);

				Button yes = (Button) share_picture.findViewById(R.id.student_share_item_btn_yes);
				Button no = (Button) share_picture.findViewById(R.id.student_share_item_btn_no);

				getActivity().getContentResolver().notifyChange(imageUri, null);
				ContentResolver cr = getActivity().getContentResolver();
				try {
					Bitmap picture = android.provider.MediaStore.Images.Media.getBitmap(cr, imageUri);
					shared_pic.setImageBitmap(picture);
				} catch (Exception e) {
					Toast.makeText(getActivity().getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
				}

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

				/*
				 //if we use another thread
				String imageId = convertImageUriToFile(imageUri, getActivity());
				new LoadImagesFromSDCard().execute("" + imageId);
				 */
				/*********** Load Captured Image And Data End ****************/
			} else {
				Toast.makeText(getActivity().getBaseContext(), 
						" Picture was not taken ", Toast.LENGTH_SHORT).show();
			}
		}

		//more file operations to be updated
		if (requestCode == REQUEST_SAVE || requestCode == REQUEST_LOAD) {
			if (resultCode == Activity.RESULT_OK) {
				String filePath = data.getStringExtra(FileDialog.RESULT_PATH);
			}
		}

	}


	public static String convertImageUriToFile ( Uri imageUri, FragmentActivity activity )  {

		Cursor cursor = null;
		int imageID = 0;
		try {
			/*********** Which columns values want to get *******/
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

			/*******  If size is 0, there are no images on the SD Card. *****/

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

		final Dialog share_picture = new Dialog(getActivity().getBaseContext());
		ImageView shared_pic;
		Button yes;
		Button no;
		Bitmap mBitmap;

		protected void onPreExecute() {
			// Progress Dialog
			share_picture.setContentView(R.layout.dialog_student_share_item);
			share_picture.show();
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
		}

		@Override
		protected Void doInBackground(String... params) {
			Uri uri = null; 
			try {
				uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + params[0]);
				/**************  Decode an input stream into a bitmap. *********/
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


	public class LoadPreviewFromSDCard extends AsyncTask<String, Void, Void> {
		final Dialog preview_picture = new Dialog(getActivity().getBaseContext());
		ImageView preview_pic;
		Bitmap mBitmap;

		protected void onPreExecute() {
			// Progress Dialog
			preview_picture.setContentView(R.layout.dialog_student_share_preview);
			preview_picture.show();
			preview_pic = (ImageView) preview_picture.findViewById(R.id.student_share_item_preview);
			preview_pic.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					preview_picture.dismiss();
				}
			});
		}

		@Override
		protected Void doInBackground(String... params) {
			Uri uri = null; 
			try {
				uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + params[0]);
				/**************  Decode an input stream into a bitmap. *********/
				mBitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));		
				preview_pic.setImageBitmap(mBitmap);
			} catch (Exception e) {
				cancel(true);
				Toast.makeText(getActivity().getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
			}
			return null;
		}

		protected void onPostExecute(Void unused) {
			if(mBitmap != null){
				// Set Image to ImageView  
				preview_pic.setImageBitmap(mBitmap);
			}  
		}
	}
}