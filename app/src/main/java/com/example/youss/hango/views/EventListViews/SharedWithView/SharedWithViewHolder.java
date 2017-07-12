package com.example.youss.hango.views.EventListViews.SharedWithView;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.youss.hango.R;
import com.example.youss.hango.entities.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SharedWithViewHolder  extends RecyclerView.ViewHolder{

    //Getting the TextView where we will have the user email typed in
    @BindView(R.id.user_list_username)
    public TextView userEmail;


    @BindView(R.id.user_list_ImageView)
    public ImageView userImageView;

    public SharedWithViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView); // Binding the elements ( TextViews )

    }


    public void populate(User user) {
        /**
         * Parameters here are :
         * User because our recyclerViewAdapter will show the users
         * currentUserEmail to verify who is the logged in user so he doesn't add himself
         * context to show the toast
         */
        itemView.setTag(user);
        userEmail.setText(user.getEmail());

    }
}
