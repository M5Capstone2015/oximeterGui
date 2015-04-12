package com.example.m5.oximetergui.Helpers;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.example.m5.oximetergui.Constants.Intent_Constants;

/**
 * Created by danabeled on 4/12/2015.
 */
public class ImageHelper {

    private Activity _context;

    public ImageHelper(Activity context)
    {
        _context = context;
    }

    public void dispatchChoosePictureIntent() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        _context.startActivityForResult(photoPickerIntent, Intent_Constants.SELECT_PHOTO);
    }

    public void setImage(Uri image, ImageView view)
    {

        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = _context.getContentResolver().query(
                image, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();


        Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
        view.setImageBitmap(yourSelectedImage);
    }
}
