package com.example.m5.oximetergui;

/**
 * Created by Hunt on 1/29/2015.
 */
public class Patient {

    public Patient()
    {

    }

    public Patient(int id, String firstName, String lastName)
    {
        this.ID = id;
        this.FirstName = firstName;
        this.LastName = lastName;
    }

    public int ID;
    public String FirstName = "";
    public String LastName = "";
    public String DateOfBirth = "";
    public int Age = 0;
    public boolean IsOpen;
}
