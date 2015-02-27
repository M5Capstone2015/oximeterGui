package com.example.m5.oximetergui.Helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.R;

import java.util.List;

/**
 * Created by danabeled on 2/26/2015.
 */
public class PatientAdapter extends BaseAdapter
{
    private LayoutInflater mInflater;
    private List<Patient> mPatients;

    private boolean clickingEnabled;

    public void DisableClicking()
    {
        this.clickingEnabled = false;
    }

    public void EnableClicking()
    {
        this.clickingEnabled = true;
    }

    public PatientAdapter(Context context, List<Patient> patients){
        mInflater = LayoutInflater.from(context);
        mPatients = patients;
    }

    @Override
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
        //return clickingEnabled;
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
        }
        else {
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

        Patient p = mPatients.get(position);
        holder.name.setText(p.FirstName + " " + p.LastName);
        return view;
    }


    private class ViewHolder {
        public TextView name;
    }
}
