package com.YoussefApp.Hango.live;

//This is the live service we will use for the users in the friendlist for our bus !

import android.widget.Toast;

import com.example.youss.hango.entities.SharedWithUsers;
import com.example.youss.hango.entities.Users;
import com.example.youss.hango.infrastructure.Hango;
import com.example.youss.hango.services.GetUsersService;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.otto.Subscribe;

public class UsersServiceLive extends LiveServiceBaseClass {


    public UsersServiceLive(Hango application) {
        super(application);
    }

    //To send a request in order to receive
    @Subscribe
    public void getFriends(GetUsersService.GetUsersFriendsRequest request)
    {
        // Here we will put the bus response with the list of the users
        final GetUsersService.GetUsersFriendsResponse response= new GetUsersService.GetUsersFriendsResponse();

        // The response.listener is an ValueEventListener that we will use for the data change to detect when we add a friend 4
        //https://firebase.google.com/docs/reference/android/com/google/firebase/database/ValueEventListener
        response.listener = request.ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                response.usersFriends=dataSnapshot.getValue(Users.class); // Like what I did in EventServiceLive
                myBus.post(response);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(application.getApplicationContext(),firebaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Subscribe
    public void getSharedWith(GetUsersService.GetSharedWithRequest request){
        final GetUsersService.GetSharedWithResponse response = new GetUsersService.GetSharedWithResponse();
        response.listener = request.ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                response.SharedWithUsers = dataSnapshot.getValue(SharedWithUsers.class);
                myBus.post(response);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(application.getApplicationContext(),firebaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
