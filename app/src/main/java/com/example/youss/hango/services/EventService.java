package com.example.youss.hango.services;


import com.example.youss.hango.entities.Event;
import com.example.youss.hango.infrastructure.ServiceResponse;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;

public class EventService {

    private EventService() {
    }

    public static class AddEventRequest {

        public String EventName;
        public String CreatorName;
        public String CreatorEmail;

        public AddEventRequest(String eventName, String creatorName, String creatorEmail) {

            EventName = eventName;
            CreatorName = creatorName;
            CreatorEmail = creatorEmail;
        }
    }

    public static class AddEventResponse extends ServiceResponse{

    }

    public static class DeleteEventRequest {
        public String CreatorEmail;
        public String EventId;

        public DeleteEventRequest(String creatorEmail, String eventId) {
            CreatorEmail = creatorEmail;
            EventId = eventId;
        }
    }

    public static class ChangeHangoNameRequest{

        public String NewHangoName;
        public String HangoID;
        public String HangoCreatorEmail;

        public ChangeHangoNameRequest(String newHangoName, String hangoID, String hangoCreatorEmail) {
            NewHangoName = newHangoName;
            HangoID = hangoID;
            HangoCreatorEmail = hangoCreatorEmail;
        }

    }

    public static class ChangeHangoNameResponse extends ServiceResponse
    {

    }
    public static class GetCurrentHangoRequest{
        public Firebase ref;

        public GetCurrentHangoRequest(Firebase ref) {
            this.ref = ref;
        }
    }
    public static class GetCurrentHangoResponse{
        public Event event;
        public ValueEventListener valueEventListener;
    }
}

