package com.example.youss.hango.entities;
import com.example.youss.hango.infrastructure.Utilities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.firebase.client.Firebase;
import com.google.firebase.*;

import com.firebase.client.ServerValue;

import java.util.HashMap;
import java.util.Map;

// ATTENTION!!!!!!
// LES CHAMPS DOIVENT CORRESPONDRE AUX CLES FIREBASE!!!!!!!

public class Event {
    private String id;
    private String eventName;
    private String creatorEmail;
    private String creator;
    private long dateCreated;
    private long dateLastChanged;
    /*
    @JsonProperty
    private Object dateCreated;
    @JsonProperty
    private Object dateLastChanged;
    */

    // pour firebaserecycleradapter
    public Event() {}

    public Event(String eventName, String CreatorEmail, String Creator) {
        this.eventName = eventName;
        this.creatorEmail = CreatorEmail;
        this.creator = Creator;
        this.dateCreated = System.currentTimeMillis();
        this.dateLastChanged=dateCreated;

        Firebase ref=new Firebase(Utilities.FireBaseHangoReferences).child(CreatorEmail).push();
        this.id=ref.getKey();
        ref.child("eventName").setValue(eventName);
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

}
