package com.example.youss.hango.entities;
import com.example.youss.hango.infrastructure.Utilities;
import com.firebase.client.Firebase;

// ATTENTION!!!!!!
// LES CHAMPS DOIVENT CORRESPONDRE AUX CLES FIREBASE!!!!!!!

public class Event {
    private String id;
    private String eventName;
    private String creatorEmail;
    private String eventDescription;
    private String eventDate;
    private String creator;
    private String eventTime;
    private long dateCreated;
    private long dateLastChanged;


    // pour firebaserecycleradapter
    public Event() {}

    public Event(String eventName,String eventDescription,String eventDate, String CreatorEmail, String Creator,String eventTime) {
        this.eventName = eventName;
        this.eventDescription=eventDescription;
        this.eventDate=eventDate;
        this.creatorEmail = CreatorEmail;
        this.creator = Creator;
        this.dateCreated = System.currentTimeMillis();
        this.dateLastChanged=dateCreated;
        this.eventTime=eventTime;
        Firebase ref=new Firebase(Utilities.FireBaseHangoReferences).child(CreatorEmail).push();
        this.id=ref.getKey();
        ref.child("eventName").setValue(eventName);
        ref.child("eventDescription").setValue(eventDescription);
        ref.child("eventDate").setValue(eventDate);
        ref.child("eventTime").setValue(eventTime);
        ref.child("creator").setValue(creator);
        ref.child("creatorEmail").setValue(Utilities.decodeEmail(creatorEmail));
        ref.child("dateCreated").setValue(dateCreated);
        ref.child("dateLastChanged").setValue(dateLastChanged);
        ref.child("id").setValue(id);

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
    public long getdateCreated() { return dateCreated; }
    public Object getdateLastChanged() {
        return dateLastChanged;
    }
    public String getEventDescription() {
        return eventDescription;
    }
    public String getEventDate() {
        return eventDate;
    }
    public String getEventTime() {
        return eventTime;
    }
}
