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
import java.util.Map;
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
            Object timeCreated;
            timeCreated=ServerValue.TIMESTAMP;// to keep track when it was created
            Event event= new Event(ref.getKey(),request.EventName,Utilities.decodeEmail(request.CreatorEmail),
                    request.CreatorName,timeCreated);
            ref.child("id").setValue(event.getid());
            ref.child("eventName").setValue(event.geteventName());
            ref.child("creator").setValue(event.getcreator());
            ref.child("creatorEmail").setValue(event.getcreatorEmail());
            ref.child("dateCreated").setValue(event.getdateCreated());
            ref.child("dateLastChanged").setValue(event.getdateLastChanged());
            Toast.makeText(application.getApplicationContext(),"Your Hango Was Posted Successfully",Toast.LENGTH_LONG).show();

        }
        myBus.post(response);
    }

    @Subscribe
    public void deleteEvent(EventService.DeleteEventRequest request)
    {
        Firebase ref=new Firebase(Utilities.FireBaseHangoReferences+request.CreatorEmail+"/"+request.EventId); // Full path to the evnet
        ref.removeValue();
    }
}
