package com.YoussefApp.Hango.live;


import android.widget.Toast;

import com.example.youss.hango.entities.Event;
import com.example.youss.hango.infrastructure.Hango;
import com.example.youss.hango.infrastructure.Utilities;
import com.example.youss.hango.services.EventService;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.Map;

public class EventServiceLive extends  LiveServiceBaseClass {
    public EventServiceLive(Hango application) {
        super(application);
    }

    @Subscribe
    public void addEvent(EventService.AddEventRequest request)
    {
        EventService.AddEventResponse response = new EventService.AddEventResponse();
        if(request.EventName.isEmpty()) {
            response.setError("HangoName","Please Choose a Hango Name");
        }
        if(request.EventDescription.isEmpty())
        {
            response.setError("HangoDescription","Please Enter a Description");
        }
        if(request.EventDate.isEmpty())
        {
            response.setError("HangoDate","Please Choose a Valid Date");
        }
        if(request.EventTime.isEmpty())
        {
            response.setError("HangoTime","Please Choose a Valid Time");
        }
        if(response.didSucceed()) {

            Event event= new Event(request.EventName,request.EventDescription,request.EventDate,request.CreatorEmail, request.CreatorName,request.EventTime);
            Toast.makeText(application.getApplicationContext(),"Your Hango Was Posted Successfully",Toast.LENGTH_LONG).show();
        }
        myBus.post(response);
    }

    @Subscribe
    public void deleteEvent(EventService.DeleteEventRequest request)
    {
        Firebase ref=new Firebase(Utilities.FireBaseHangoReferences+request.CreatorEmail+"/"+request.EventId); // Full path to the evnet
        Firebase SharedWithRef=new Firebase(Utilities.FireBaseSharedWithReference+request.EventId);
        SharedWithRef.removeValue();
        ref.removeValue();
    }

    @Subscribe
    public void changeHangoName(EventService.ChangeHangoNameRequest request)
    {
        EventService.ChangeHangoNameResponse response =new EventService.ChangeHangoNameResponse();
        if(request.NewHangoName.isEmpty())
        {
            response.setError("Hango Name","Please Provide a Valid Non-Empty Name");

        }

        if(response.didSucceed()){

            Firebase ref= new Firebase(Utilities.FireBaseHangoReferences + request.HangoCreatorEmail+"/"+request.HangoID);
            long TimeLastChanged=System.currentTimeMillis();
            //Update the database with updateChildren in case we lost connection
            Map newData= new HashMap();
            newData.put("eventName",request.NewHangoName);
            newData.put("dateLastChanged",TimeLastChanged);
            ref.updateChildren(newData);

        }
        myBus.post(response);
    }
    @Subscribe
    public void changeHangoDate(EventService.ChangeHangoDateRequest request)
    {
        EventService.ChangeHangoDateResponse response =new EventService.ChangeHangoDateResponse();
        if(request.NewHangoDate.isEmpty())
        {
            response.setError("Hango Date","Please Provide a Valid Date");

        }

        if(response.didSucceed()){

            Firebase ref= new Firebase(Utilities.FireBaseHangoReferences + request.HangoCreatorEmail+"/"+request.HangoID);
            long TimeLastChanged=System.currentTimeMillis();
            //Update the database with updateChildren in case we lost connection
            Map newData= new HashMap();
            newData.put("eventDate",request.NewHangoDate);
            newData.put("dateLastChanged",TimeLastChanged);
            ref.updateChildren(newData);

        }
        myBus.post(response);
    }

    @Subscribe
    public void changeHangoDesc(EventService.ChangeHangoDescRequest request)
    {
        EventService.ChangeHangoDescResponse response =new EventService.ChangeHangoDescResponse();
        if(request.NewHangoDesc.isEmpty())
        {
            response.setError("Hango Desc","Please Provide a Valid Description");

        }

        if(response.didSucceed()){

            Firebase ref= new Firebase(Utilities.FireBaseHangoReferences + request.HangoCreatorEmail+"/"+request.HangoID);
            long TimeLastChanged=System.currentTimeMillis();
            //Update the database with updateChildren in case we lost connection
            Map newData= new HashMap();
            newData.put("eventDescription",request.NewHangoDesc);
            newData.put("dateLastChanged",TimeLastChanged);
            ref.updateChildren(newData);

        }
        myBus.post(response);
    }
    @Subscribe
    public void changeHangoTime(EventService.ChangeHangoTimeRequest request)
    {
        EventService.ChangeHangoTimeResponse response =new EventService.ChangeHangoTimeResponse();
        if(request.NewHangoTime.isEmpty())
        {
            response.setError("Hango Time","Please Provide a Valid Time");

        }

        if(response.didSucceed()){

            Firebase ref= new Firebase(Utilities.FireBaseHangoReferences + request.HangoCreatorEmail+"/"+request.HangoID);
            long TimeLastChanged=System.currentTimeMillis();
            //Update the database with updateChildren in case we lost connection
            Map newData= new HashMap();
            newData.put("eventTime",request.NewHangoTime);
            newData.put("dateLastChanged",TimeLastChanged);
            ref.updateChildren(newData);

        }
        myBus.post(response);
    }
    @Subscribe
    public void getCurrentHango(EventService.GetCurrentHangoRequest request)
    {
        final EventService.GetCurrentHangoResponse response= new EventService.GetCurrentHangoResponse();
        response.valueEventListener=request.ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                response.event= dataSnapshot.getValue(Event.class);
                if(response.event!=null)
                {
                    myBus.post(response);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

                Toast.makeText(application.getApplicationContext(),
                        firebaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Subscribe
    public void updateReferences(EventService.UpdateList request)
    {
        long timeChanged=System.currentTimeMillis();
        Map newData=new HashMap();
        newData.put("dateLastChanged",timeChanged);
        request.ref.updateChildren(newData);
    }
}
