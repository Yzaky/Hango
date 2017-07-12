package com.example.youss.hango.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.youss.hango.R;
import com.example.youss.hango.entities.User;
import com.example.youss.hango.entities.Users;
import com.example.youss.hango.infrastructure.Utilities;
import com.example.youss.hango.services.GetUsersService;
import com.example.youss.hango.views.EventListViews.AddFriendView.AddFriendViewholder;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.squareup.otto.Subscribe;

import java.util.HashMap;

public class AddFriendActivity extends BaseActivity {

    FirebaseRecyclerAdapter adapter;
    Firebase friendsRef; // We will use that to retrieve the friendlist using the GetUsersService Service
    ValueEventListener listener;// interface can be used to receive events about data changes at location using queries ! it will
    // be fired when a value changes so we can know that we have added someone
    Users friends;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        setTitle("Friends List");

        //A recycler view is responsible for providing views that represents items in a data set,
        // here I use it to display the friends
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.add_friend_recyclerView);

        friendsRef = new Firebase(Utilities.FireBaseUserFriendReference + UserEmail);

        myBus.post(new GetUsersService.GetUsersFriendsRequest(friendsRef));

        Firebase usersRef = new Firebase(Utilities.FireBaseUserReferences);
        // As I did with events list
        adapter = new FirebaseRecyclerAdapter<User, AddFriendViewholder>(User.class, R.layout.user_list, AddFriendViewholder.class, usersRef) {


            @Override
            protected void populateViewHolder(final AddFriendViewholder addFriendViewholder, final User user, int i) {

                // populate method in AddFriendViewholder will display each user in a set of blocks. it has an
                // inside onclick listener on the imageView in case we want to send a request !
                // So populate here refer for a user, and populateViewHolder() will call it for all the users !
                addFriendViewholder.populate(user);

                //Checking if the user exists in the current user friendlist ( which is a hash map as Mr Sebastien Mentioned )
                if (isFriend(friends.getUsersFriends(), user)) {
                    addFriendViewholder.userImageView.setImageResource(R.mipmap.ic_check);//Exists in our friendlist already
                } else {
                    addFriendViewholder.userImageView.setImageResource(R.mipmap.ic_plus); // Does not exist

                }

                addFriendViewholder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (UserEmail.equals(Utilities.encodeEmail(user.getEmail()))) {
                            Toast.makeText(getApplicationContext(), "Can Not Add Yourself", Toast.LENGTH_LONG).show();
                        } else {
                            //After verifying that the user is not himself
                            Firebase friendsRef = new Firebase(Utilities.FireBaseUserFriendReference
                                    + UserEmail + "/" + "usersFriends/"
                                    + Utilities.encodeEmail(user.getEmail()));

                            // in case we click on the user who is already in the friendlist, we remove him and change the UI
                            if (isFriend(friends.getUsersFriends(), user)) {
                                friendsRef.removeValue();
                                addFriendViewholder.userImageView.setImageResource(R.mipmap.ic_plus);

                            } else {
                                // add him into the list
                                friendsRef.setValue(user);
                                addFriendViewholder.userImageView.setImageResource(R.mipmap.ic_check);
                            }

                        }

                    }
                });
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_friend_details, menu);
        return super.onCreateOptionsMenu(menu);
    }
    // Important to clean the adapter
    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.cleanup();
        friendsRef.removeEventListener(listener);
    }

    // To retrieve the list of the friends from the frombase using the bus.
    //Subscribe is the complement to event publishing, allowing us to receive notification that an event has occurred.
    //So here we subscribe to the event about getting the friends.
    //The response is a parameter acting like a listener that listens to whenever the parameter gets published on the event bus !
    //The advantage of this is that we can declare an event anywhere in the code and then this method will capture the call.
    //So whenever we try to get the list of the friends, this method will capture the call from the bus and do the processing
    //https://www.youtube.com/watch?v=GD_TrOuzkkQ
    @Subscribe
    public void getFriends(GetUsersService.GetUsersFriendsResponse response) {
        listener = response.listener;

        if (response.usersFriends != null) {
            friends = response.usersFriends;
        } else {
            // In case the user does not have any friend yet, then we will use this check to make sure that the app does not crash
            // it is a typo for the else!
            friends = new Users();
        }
    }


    // Checking that the user exists in the friend list
    private boolean isFriend(HashMap<String, User> Friends, User user) {
        return Friends != null && Friends.size() != 0 && Friends.containsKey(Utilities.encodeEmail(user.getEmail()));
    }


}
