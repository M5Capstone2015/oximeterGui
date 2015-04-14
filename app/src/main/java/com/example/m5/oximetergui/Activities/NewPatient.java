package com.example.m5.oximetergui.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.example.m5.oximetergui.Constants.General_Constants;
import com.example.m5.oximetergui.Constants.Intent_Constants;
import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.Helpers.ImageHelper;
import com.example.m5.oximetergui.Helpers.PatientInfoHelper;
import com.example.m5.oximetergui.Models.PatientModel;
import com.example.m5.oximetergui.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class NewPatient extends Activity implements View.OnClickListener {

    private Patient mPatient = new Patient();
    private PatientInfoHelper _helper = new PatientInfoHelper(this, this);
    private ImageView thumbnail;
    private ImageHelper _imageHelper = new ImageHelper(this);
    private Bitmap _currentImage = null;
    private PatientModel _pModel = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _helper.ConstructMainLayout();

        thumbnail = (ImageView) findViewById(R.id.thumbnail);
        thumbnail.setOnClickListener(imageViewListener);
        _pModel = new PatientModel(this);
    }

    private ImageView.OnClickListener imageViewListener = new ImageView.OnClickListener() {

        @Override
        public void onClick(View dialog)
        {
            _imageHelper.dispatchChoosePictureIntent();
        }
    };

    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.save:
                mPatient = _helper.ConstructPatient();

                if (!mPatient.Validate()) {
                    _helper.createErrorMessage();
                    break;
                }
                else
                {
                    //
                    // DAN
                    // The below is commented out because it will throw an error because mPatient's ID is NULL.
                    // After you save the patient here and get the patient unique ID we can save the image.
                    //


                    StringBuilder sb = new StringBuilder();
                    _pModel.AddPatient(mPatient, sb);


                    if (_currentImage != null) // Save JPEG if user took a picture
                    {
                        //Find last patient ID which will be the newly made patient
                        mPatient.ID = _pModel.FindMaxPatientID();
                        mPatient.imageFilePath = this.SaveImage(this._currentImage, mPatient);
                        _pModel.UpdatePatient(mPatient,null);
                    }

                    //
                    //  DAN
                    //  Lets do the SQL save here. Then we can return RESULT_OK
                    //  just to tell the Slider to refresh the patient list with the new patient.
                    //
                    //  Also use the LoadPicture() method I wrote to Load the picture in patient INFO.
                    //

                    Intent i = new Intent();
                    i.putExtra(Intent_Constants.NewPatientInfo, mPatient);
                    setResult(RESULT_OK, i);
                    finish();
                    break;
                }

            case R.id.cameraButton:
                dispatchTakePictureIntent();
        }
    }


    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == General_Constants.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            this._currentImage = (Bitmap) extras.get("data"); // Save current image.

            ImageView imageView = (ImageView) findViewById(R.id.thumbnail);

            //Rotate Image so it is stored in the correct angle
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(_currentImage,_currentImage.getWidth(),_currentImage.getHeight(),true);
            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);
            _currentImage = rotatedBitmap;

            imageView.setImageBitmap(_currentImage);
        }
        else if (resultCode == RESULT_CANCELED) {
            // User cancelled the image capture so do nothing.
        }
    }

    private String SaveImage(Bitmap bitmap, Patient patient)
    {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        String fileName = patient.ID + "_image.jpg";
        File mypath = new File(directory, fileName);

        FileOutputStream out;
        try
        {
            out = new FileOutputStream(mypath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return mypath.toString();
    }

    private void LoadImage(Context context, Patient patient)
    {
        ContextWrapper cw;
        File directory;

        try {
            cw = new ContextWrapper(context);
            directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }

        String filename = patient.ID.toString() + "_image.jpg";
        File filePath = new File(directory, filename);

        int size = (int) filePath.length();
        byte[] bytes = new byte[size];

        try
        {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(filePath));
            buf.read(bytes, 0, bytes.length);
            buf.close();
            Bitmap iamge = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            this.thumbnail.setImageBitmap(iamge);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    /*

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

    // Save a file: path for use with ACTION_VIEW intents
    _currentPhotoPath = "file:" + image.getAbsolutePath();

    return image;


    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(_currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
     */

    /*
    private void setPic() {
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
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(_currentPhotoPath, bmOptions);
        thumbnail.setImageBitmap(bitmap);
    }
    */

}
