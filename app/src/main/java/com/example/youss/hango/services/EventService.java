package com.example.youss.hango.services;


import com.example.youss.hango.infrastructure.ServiceResponse;

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
}

