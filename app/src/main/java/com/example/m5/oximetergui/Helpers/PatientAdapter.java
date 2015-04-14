package com.example.m5.oximetergui.Helpers;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by danabeled on 2/26/2015.
 */
public class PatientAdapter extends BaseAdapter
{
    private LayoutInflater mInflater;
    private List<Patient> mPatients;

    public PatientAdapter(Context context, List<Patient> patients){
        mInflater = LayoutInflater.from(context);
        mPatients = patients;
    }

    public int getCount() {
        return mPatients.size();
    }

    @Override
    public Object getItem(int position){
        return mPatients.get(position);
    }

    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view;
        ViewHolder holder;

        if (convertView == null) {
            view = mInflater.inflate(R.layout.row_layout, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.listText);
            view.setTag(holder);
            holder.placeholder = (ImageView) view.findViewById(R.id.placeholder);
            view.setTag(holder);
        }
        else {
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

        Patient p = mPatients.get(position);
        holder.name.setText(p.FirstName + " " + p.LastName);
        if (p.imageFilePath.matches("")) {
            LoadImage(parent.getContext(), p, holder.placeholder);
        }
        else {
            holder.placeholder.setImageResource(R.drawable.placeholder);
        }
        return view;
    }

    private void LoadImage(Context context, Patient patient, ImageView view)
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
            view.setImageBitmap(iamge);
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

    private class ViewHolder {
        public TextView name;
        public ImageView placeholder;
    }
}
