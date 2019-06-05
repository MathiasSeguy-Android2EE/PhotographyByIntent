package com.android2ee.formation.multimedia.photography;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

public class MainActivity extends Activity {

	Button btnTakePicture;
	ImageView imvThumb;
	ImageView imvPicture;
	Button btnTakeVideo;
	VideoView vdvVideo;
	String tag = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			pictureFileName = savedInstanceState.getString("pictureFileName");
			videoFileName = savedInstanceState.getString("videoFileName");
		}
		setContentView(R.layout.activity_main);
		btnTakePicture = (Button) findViewById(R.id.takeAPicture);
		btnTakePicture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				recordPicture();
			}
		});
		imvThumb = (ImageView) findViewById(R.id.thumbnail);
		imvPicture = (ImageView) findViewById(R.id.picture);
		btnTakeVideo = (Button) findViewById(R.id.takeAVideo);
		btnTakeVideo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				recordVideo();
			}
		});
		vdvVideo = (VideoView) findViewById(R.id.video);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/******************************************************************************************/
	/** MultiMediaTask **************************************************************************/
	/******************************************************************************************/

	// Pour enregistrer une photographie ou une vidéo, vous pouvez utiliser les activités natives
	// d’Android
	// en les appelant au moyen d’intentions. Il vous faut ensuite surcharger la méthode
	// onActivtyResult
	// pour récupérer votre image ou votre vidéo.
	/**
	 * The id of the request code associated to the call of the Intent Image_Capture
	 */
	private final int recordPictureMyIntentId = 11021974;
	/**
	 * The id of the request code associated to the call of the Intent Video_Capture
	 */
	private final int recordVideoMyIntentId = 13121974;
	// Shortcut some android constant
	private final int HighQuality = 1;
	private final int lowQuality = 0;

	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		bundle.putString("pictureFileName", pictureFileName);
		bundle.putString("videoFileName", videoFileName);
	}

	String pictureFileName;
	String videoFileName;

	/**
	 * The recording method exemple
	 */
	private void recordPicture() {
		/******************************************************************************/
		/** Picture recording ***************************************************************/
		/*******************************************************************************/
		// Define the Intent to launch for calling a picture record
		Intent recordPictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		// Define the URI you want your picture to be store
		File tempFile;
		tempFile = new File(Environment.getExternalStorageDirectory(), "test.jpg");
		pictureFileName = tempFile.getAbsolutePath();
		Uri outPutUri = Uri.fromFile(tempFile);
		recordPictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
		// Uri outPutUri = Uri.parse("file:///sdcard/myFilePath/myFileName");
		// Then add extra information on the Intent:
		// Where to store the file
		recordPictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
		// What is picture quality expected
		recordPictureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, HighQuality);
		// And launch the Intent
		startActivityForResult(recordPictureIntent, recordPictureMyIntentId);
		// -------------------------------------------------------------
		// !\\To take only a thumbnail, just forget to define the output
		// -------------------------------------------------------------

	}

	/**
	 * The recording method exemple
	 */
	private void recordVideo() {
		/*********************************************************************************/
		/** Video Recorder ******************************************************************/
		/*********************************************************************************/
		// Define the Intent to launch for calling a picture record
		Intent recordVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		File tempFile;
		tempFile = new File(Environment.getExternalStorageDirectory(), "video.mp4");
			videoFileName = tempFile.getAbsolutePath();
			Uri outPutVideoUri = Uri.fromFile(tempFile);
			// Define the URI you want your picture to be store
			// Then add extra information on the Intent: //Where to store the file
			recordVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, outPutVideoUri);
			// What is picture quality expected
			recordVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, HighQuality);
			// And launch the Intent
			startActivityForResult(recordVideoIntent, recordVideoMyIntentId);
		

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e(tag, "onActivityResult");
		if(data!=null) {
			Log.e(tag, "onActivityResult data.getBundleExtra(mData)"+data.getBundleExtra("mData"));
		}
		
		// if (resultCode == Activity.RESULT_OK) {
		Log.e(tag, "onActivityResult RESULT_OK");
		if (requestCode == recordPictureMyIntentId) {
			Log.e(tag, "onActivityResult requestCode == recordPictureMyIntentI");
			// Thumbnail case:
			if (data != null) {
				Log.e(tag, "onActivityResult data != null");
				if (data.hasExtra("data")) {
					Log.e(tag, "onActivityResult data.hasExtra(data)==" + data.getParcelableExtra("data"));
					// Get the thumbnail Bitmap picture
					Bitmap thumbnail = data.getParcelableExtra("data");
					imvThumb.setImageBitmap(thumbnail);
				}
			}
//			 else {
			Log.e(tag, "onActivityResult data == null");
			// A real picture case:
			// Your picture is store in the ouput uri you have defined, you can do something
			// with it
			Log.e(tag, "onActivityResult pictureFileName "+pictureFileName);
			Uri outPutVideoUri = Uri.parse(pictureFileName);
			imvPicture.setImageURI(outPutVideoUri);
			Bitmap bitmap=BitmapFactory.decodeFile(pictureFileName);
			imvThumb.setImageBitmap(bitmap);
//			 }
		} else if (requestCode == recordVideoMyIntentId) {
			Log.e(tag, "onActivityResult requestCode == recordVideoMyIntentId");
			if (data != null && data.hasExtra("data")) {
				Log.e(tag, "onActivityResult data != null");
				// Normally, your video should be in the output uri you defined
				Uri recordedVideo = data.getData();
				Log.e(tag, "onActivityResult data.hasExtra(data)==" + data.getParcelableExtra("data"));
				// Or using the URI of the video file:
				vdvVideo.setVideoURI(recordedVideo);
				// You can set the screen to remain on
				vdvVideo.setKeepScreenOn(true);
				// Launch the video:
				vdvVideo.start();
			} else {
				
				Log.e(tag, "onActivityResult data == null");
				Uri outPutVideoUri = Uri.parse(videoFileName);
				Log.e(tag, "onActivityResult videoFileName "+videoFileName);
				// Or using the URI of the video file:
//				vdvVideo.setVideoURI(outPutVideoUri);
				vdvVideo.setVideoPath(videoFileName);
				// You can set the screen to remain on
				vdvVideo.setKeepScreenOn(true);
				// Launch the video:
				vdvVideo.start();
			}
			// }
		}

	}

	private void retrievePictureInformation() {

		// Describe the columns you'd like to have returned. Selecting from the Thumbnails location
		// gives you both the Thumbnail Image ID, as well as the original image ID
		String[] projection = {
				MediaStore.Images.Thumbnails._ID, // The columns we want
				MediaStore.Images.Thumbnails.IMAGE_ID, MediaStore.Images.Thumbnails.KIND,
				MediaStore.Images.Thumbnails.DATA };
		String selection = MediaStore.Images.Thumbnails.KIND + "=" + // Select only mini's
				MediaStore.Images.Thumbnails.MINI_KIND;

		String sort = MediaStore.Images.Thumbnails._ID + " DESC";

		// At the moment, this is a bit of a hack, as I'm returning ALL images, and just taking the
		// latest one. There is a better way to narrow this down I think with a WHERE clause which
		// is currently the selection variable
		Cursor myCursor = this.managedQuery(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection, selection,
				null, sort);

		long imageId = 0l;
		long thumbnailImageId = 0l;
		String thumbnailPath = "";

		try {
			myCursor.moveToFirst();
			imageId = myCursor.getLong(myCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.IMAGE_ID));
			thumbnailImageId = myCursor.getLong(myCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID));
			thumbnailPath = myCursor.getString(myCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
		} finally {
			myCursor.close();
		}

		// Create new Cursor to obtain the file Path for the large image

		String[] largeFileProjection = { MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA };

		String largeFileSort = MediaStore.Images.ImageColumns._ID + " DESC";
		myCursor = this.managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, largeFileProjection, null, null,
				largeFileSort);
		String largeImagePath = "";

		try {
			myCursor.moveToFirst();

			// This will actually give yo uthe file path location of the image.
			largeImagePath = myCursor.getString(myCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));
		} finally {
			myCursor.close();
		}
		// These are the two URI's you'll be interested in. They give you a handle to the actual
		// images
		Uri uriLargeImage = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(imageId));
		Uri uriThumbnailImage = Uri.withAppendedPath(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
				String.valueOf(thumbnailImageId));

		// I've left out the remaining code, as all I do is assign the URI's to my own objects
		// anyways...

	}

}
