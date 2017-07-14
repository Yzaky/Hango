package com.example.youss.hango.entities;

// This will help me to get the users inside the friendlist of the current user so we can share the Hango event with them
//Also it will allow us to keep the check mark on the ImageView in case we have already shared the event with someone.
//Because if we add someone and then we go back and then go back again into our Hango activity, the check mark will disappear
//and we will have again the + sign eventho we have already added this friend!
// So I thought about having a listener for the users


import android.support.annotation.Nullable;

import java.util.HashMap;

public class SharedWithUsers {
    @Nullable
    private HashMap<String, User> sharedWith;

    public SharedWithUsers() {
    }

    @Nullable
    public HashMap<String, User> getSharedWith() {
        return sharedWith;
    }
}
