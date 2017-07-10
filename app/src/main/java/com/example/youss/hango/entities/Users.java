package com.example.youss.hango.entities;

import java.util.HashMap;

// This will help me to get the users inside the friendlist of the current user so we can remove them from friends if we want !
//Also it will allow us to keep the check mark on the ImageView in case we have already added someone.
//Because if we add someone and then we go back and then go back again into our addfriend activity, the check mark will disappear
//and we will have again the + sign eventho we have already added this friend!
// So I thought about having a listener for the users

public class Users {

    private HashMap<String, User> usersFriends;  // Same name as in the Firebase to avoid Bounce to Type exception

    public Users(){

    }

    public Users(HashMap<String, User> usersFriends) {
        this.usersFriends = usersFriends;
    }

    public HashMap<String, User> getUsersFriends() {
        return usersFriends;
    }
}
