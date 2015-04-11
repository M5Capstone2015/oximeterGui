package com.example.m5.oximetergui.Activities;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.m5.oximetergui.Constants.General_Constants;
import com.example.m5.oximetergui.Constants.Intent_Constants;
import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.Helpers.CameraHelper;
import com.example.m5.oximetergui.Helpers.ErrorMessage;
import com.example.m5.oximetergui.Helpers.PatientInfoHelper;
import com.example.m5.oximetergui.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class NewPatient extends Activity implements View.OnClickListener {

    Patient mPatient = new Patient();
    PatientInfoHelper _helper = new PatientInfoHelper(this, this);
    CameraHelper _camHelper = new CameraHelper();
    private Uri fileUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _helper.ConstructMainLayout();
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

            case R.id.cameraButton:// create Intent to take a picture and return control to the calling application
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                fileUri = _camHelper.getOutputMediaFileUri(General_Constants.MEDIA_TYPE_IMAGE); // create a file to save the image
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

                // start the image capture Intent
                startActivityForResult(intent, Intent_Constants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                break;
        }
    }


}
