package com.example.m5.oximetergui.Data_Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hunt on 1/29/2015.
 */
public class Patient implements Parcelable {

    public int ID;
    public String FirstName = "";
    public String LastName = "";
    public String DateOfBirth = "";
    public boolean IsOpen;

    public Patient() { }

    // Parcelling part
    public Patient(Parcel in)
    {
        String[] data = new String[3];

        in.readStringArray(data);
        this.FirstName = data[0];
        this.LastName = data[1];
        this.DateOfBirth = data[2];
    }

    public Patient(String firstName, String lastName, String dateOfBirth, Boolean isOpen) // TODO get these constructors under control.
    {
        this.FirstName = firstName;
        this.LastName = lastName;
        this.DateOfBirth = dateOfBirth;
    }

    public Patient(String ID, String firstName, String lastName, String dateOfBirth, Boolean isOpen)
    {
        this.FirstName = firstName;
        this.LastName = lastName;
        this.DateOfBirth = dateOfBirth;
    }

    public Patient(int Id, String first, String last)
    {
        this.ID = Id;
        this.FirstName = first;
        this.LastName = last;
    }

    public String GetFullName()
    {
        return this.FirstName + " " + this.LastName;
    }

    public boolean Validate() // TODO Finish this method. Do we want some these fields to be nullable?
    {
        return !FirstName.matches("") || LastName.matches("");
    }

    public void Update(Patient p)
    {
        this.LastName = p.LastName;
        this.FirstName = p.FirstName;
        this.DateOfBirth = p.DateOfBirth;
        this.IsOpen = p.IsOpen;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeStringArray(new String[] {this.FirstName,
                this.LastName,
                this.DateOfBirth
        });
    }

    @Override
    public int describeContents(){
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Patient createFromParcel(Parcel in) {
            return new Patient(in);
        }

        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };
}
