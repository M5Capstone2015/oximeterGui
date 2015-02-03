package com.example.m5.oximetergui.Data_Objects;

/**
 * Created by Hunt on 1/29/2015.
 */
public class Patient {

    public Patient()
    {

    }

    public Patient(String firstName, String lastName, String dateOfBirth, Boolean isOpen)
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

    public int ID;
    public String FirstName = "";
    public String LastName = "";
    public String DateOfBirth = "";
    public int Age = 0; // TODO get rid of this.
    public boolean IsOpen;

    public boolean Validate() // TODO Finish this method. Do we want some these fields to be nullable?
    {
        if (FirstName == "" || LastName == "")
            return false;
        else
            return true;
    }
}
