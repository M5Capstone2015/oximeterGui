package com.example.m5.oximetergui.Activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.m5.oximetergui.Constants.General_Constants;
import com.example.m5.oximetergui.Constants.Intent_Constants;
import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.Helpers.ErrorMessage;
import com.example.m5.oximetergui.Helpers.PatientInfoHelper;
import com.example.m5.oximetergui.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class NewPatient extends Activity implements View.OnClickListener {

    Patient mPatient = new Patient();
    PatientInfoHelper _helper = new PatientInfoHelper(this, this);
    private Uri fileUri;
    ImageView thumbnail;
    String _currentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _helper.ConstructMainLayout();
        thumbnail = (ImageView) findViewById(R.id.thumbnail);
    }

    public void onClick(View v) {
        switch (v.getId())
        {
            //case R.id.Edit:
                //break;
            case R.id.save:
                mPatient = _helper.ConstructPatient();

                if (!mPatient.Validate()) {
                    _helper.createErrorMessage();
                    break;
                }
                else
                {
                    Intent i = new Intent();
                    mPatient.ID = 0;
                    i.putExtra(Intent_Constants.NewPatientInfo, mPatient);
                    setResult(RESULT_OK, i);
                    finish();
                    break;
                }

            case R.id.cameraButton:

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, General_Constants.REQUEST_IMAGE_CAPTURE);
                    }
                    break;
                }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == General_Constants.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                this.galleryAddPic();
                this.setPic();

            }
            else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            }

    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        _currentPhotoPath = "file:" + image.getAbsolutePath();

        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(_currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPic()
    {
        // Get the dimensions of the View
        int targetW = thumbnail.getWidth();
        int targetH = thumbnail.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(_currentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(_currentPhotoPath, bmOptions);
        thumbnail.setImageBitmap(bitmap);
    }



}
