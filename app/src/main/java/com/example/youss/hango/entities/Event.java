package com.example.youss.hango.entities;

import com.firebase.client.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class Event {
    private String id;
    private String eventName;
    private String creatorEmail;
    private String creator;
    /*private Map<String,Object> dateCreated;
    private Map<String,Object> dateLastChanged;*/
    private Object dateCreated;
    private Object dateLastChanged;

    public Event() {
    }

    public Event(String id, String eventName, String CreatorEmail, String Creator,Object dateCreated) {
        this.id = id;
        this.eventName = eventName;
        this.creatorEmail = CreatorEmail;
        this.creator = Creator;
        this.dateCreated = dateCreated;
        Object DateLastChangedObject=ServerValue.TIMESTAMP;
        this.dateLastChanged=DateLastChangedObject;

    }

    public String getid() {
        return id;
    }

    public String geteventName() {
        return eventName;
    }

    public String getcreatorEmail() {
        return creatorEmail;
    }

    public String getcreator() {
        return creator;
    }

    public Object getdateCreated() {
        if(dateLastChanged!=null)
        {
            return dateCreated;
        }
       Object DateCreatedObject=ServerValue.TIMESTAMP;
        return DateCreatedObject;
    }

    public Object getdateLastChanged() {
        return dateLastChanged;
    }

}
