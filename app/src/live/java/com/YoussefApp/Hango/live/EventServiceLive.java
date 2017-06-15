package com.YoussefApp.Hango.live;


import android.widget.Toast;

import com.example.youss.hango.entities.Event;
import com.example.youss.hango.infrastructure.Hango;
import com.example.youss.hango.infrastructure.Utilities;
import com.example.youss.hango.services.EventService;
import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;
import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.Objects;

public class EventServiceLive extends  LiveServiceBaseClass {
    public EventServiceLive(Hango application) {
        super(application);
    }

    @Subscribe
    public void addEvent(EventService.AddEventRequest request)
    {
        EventService.AddEventResponse response = new EventService.AddEventResponse();
        if(request.EventName.isEmpty())
        {
            response.setError("HangoName","Please Choose a Hango Name");

        }
        if(response.didSucceed()) {
            Firebase ref=new Firebase(Utilities.FireBaseHangoReferences + request.CreatorEmail).push();//random string
            HashMap<String,Object> timeCreated = new HashMap<>();
            timeCreated.put("timestamp", ServerValue.TIMESTAMP);// to keep track when it was created
            Event event= new Event(ref.getKey(),request.EventName,Utilities.decodeEmail(request.CreatorEmail),
                    request.CreatorName,timeCreated);
            ref.child("id").setValue(event.getID());
            ref.child("Eventname").setValue(event.getEventname());
            ref.child("Creator").setValue(event.getCreator());
            ref.child("CreatorEmail").setValue(event.getCreatorEmail());
            ref.child("DateCreated").setValue(event.getDateCreated());
            ref.child("DateLastChanged").setValue(event.getDateLastChanged());
            Toast.makeText(application.getApplicationContext(),"Hango is Being Created",Toast.LENGTH_LONG).show();

        }
        myBus.post(response);
    }
}
