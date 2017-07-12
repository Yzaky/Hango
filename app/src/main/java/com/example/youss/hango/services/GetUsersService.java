package com.example.youss.hango.services;


// We will use this Service for sending Requests and receiving responses using our bus (otto.square)
// As I did with AccountServices for Log ins and EventService for creating events!

import com.example.youss.hango.entities.ShareWithUsers;
import com.example.youss.hango.entities.Users;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;

public class GetUsersService {

    public static class GetUsersFriendsRequest {

        public Firebase ref;

        public GetUsersFriendsRequest(Firebase ref) {
            this.ref = ref;

        }

    }

    public static class GetUsersFriendsResponse {

        public ValueEventListener listener;
        public Users usersFriends;
    }


    public static class GetSharedWithRequest{
        public Firebase ref;

        public  GetSharedWithRequest(Firebase ref) {
            this.ref = ref;
        }

    }

    public static class GetSharedWithResponse{
        public ValueEventListener listener;
        public ShareWithUsers SharedWithUsers;
    }

}
