package com.example.m5.oximetergui.Helpers;

import org.json.*;

import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.Data_Objects.Reading;
import com.example.m5.oximetergui.Models.DataModel;
import com.example.m5.oximetergui.Models.PatientModel;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by Hunt on 4/9/2015.
 */
public class BackupBuilder {

    PatientModel patientModel;
    DataModel dataModel;

    public BackupBuilder(PatientModel pm, DataModel dm)
    {
        patientModel = pm;
        dataModel = dm;
    }

    private Hashtable<Integer, List<Reading>> loadCache()
    {
        List<Reading> readings = dataModel.GetAllData();
        Hashtable<Integer, List<Reading>> readingMap = new Hashtable<>();

        for (Reading r : readings)
        {
            // If no readings for this patient exist, initialize the List and add to hash.
            if (!readingMap.containsKey(r.PatientID))
            {
                ArrayList lst = new ArrayList<Reading>();
                lst.add(r);
                readingMap.put(r.PatientID, lst);
            }
            // If there is a list of readings for this patient, add them to the list.
            else
            {
                readingMap.get(r.PatientID).add(r);
            }
        }

        return readingMap;
    }

    private JSONObject createReadingJSON(Reading r)
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("start_date", r.StartDate);
            jsonObject.put("end_date", r.EndDate);
            jsonObject.put("data_string", r.DataString);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return jsonObject;
    }

    public String GenerateFile()
    {
        Hashtable<Integer, List<Reading>> readingCache = this.loadCache();
        List<Patient> patients = this.patientModel.LoadPatientNames();

        JSONArray allPatients = new JSONArray();

        try {
            for (Patient p : patients) {

                JSONObject patientJSON = new JSONObject();

                patientJSON.put("dob", p.ID);
                patientJSON.put("first_name", p.FirstName);
                patientJSON.put("last_name", p.LastName);
                patientJSON.put("dob", p.DateOfBirth);

                if (readingCache.containsKey(p.ID))
                {
                    JSONArray readingsArray = new JSONArray();

                    for (Reading r : readingCache.get(p.ID))
                        readingsArray.put(this.createReadingJSON(r));

                    patientJSON.put("readings", readingsArray);
                }
                allPatients.put(patientJSON);

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return allPatients.toString();
    }
}
