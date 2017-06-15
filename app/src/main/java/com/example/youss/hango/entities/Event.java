package com.example.youss.hango.entities;

import com.firebase.client.ServerValue;

import java.util.HashMap;
import java.util.Objects;

public class Event {
    private String ID;
    private String Eventname;
    private String CreatorEmail;
    private String Creator;
    private HashMap<String,Object> DateCreated;
    private HashMap<String,Object> DateLastChanged;

    public Event() {
    }

    public Event(String ID, String eventname, String creatorEmail, String creator, HashMap<String, Object> dateCreated) {
        this.ID = ID;
        Eventname = eventname;
        CreatorEmail = creatorEmail;
        Creator = creator;
        DateCreated = dateCreated;
        HashMap<String,Object> DateLastChangedObject= new HashMap<>();
        DateLastChangedObject.put("date", ServerValue.TIMESTAMP);
        this.DateLastChanged=DateLastChangedObject;

    }

    public String getID() {
        return ID;
    }

    public String getEventname() {
        return Eventname;
    }

    public String getCreatorEmail() {
        return CreatorEmail;
    }

    public String getCreator() {
        return Creator;
    }

    public HashMap<String, Object> getDateCreated() {
        if(DateLastChanged!=null)
        {
            return DateCreated;
        }
        HashMap<String,Object> DateCreatedObject = new HashMap<>();
        DateCreatedObject.put("date",ServerValue.TIMESTAMP);
        return DateCreatedObject;
    }

    public HashMap<String, Object> getDateLastChanged() {
        return DateLastChanged;
    }

}
