package com.example.youss.hango.activities;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.youss.hango.R;
import com.example.youss.hango.dialog.AddEventDialogFragment;
import com.example.youss.hango.dialog.DeleteEventDialogFragment;
import com.example.youss.hango.entities.Event;
import com.example.youss.hango.infrastructure.Utilities;
import com.example.youss.hango.views.EventListViews.EventsListViewHolder;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.activity_main_EVAB)
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter myAdapter;

    private String bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recyclerView=(RecyclerView)findViewById(R.id.activity_main_myRecyclerView);

        if(Username.contains(" "))
        {
            bar="Welcome "+Username.substring(0,Username.indexOf(" "));
        }
        else {
            bar="Welcome "+Username;
        }
        getSupportActionBar().setTitle(bar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Firebase EventRef = new Firebase(Utilities.FireBaseHangoReferences + UserEmail);
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplication());
        String Order=sharedPreferences.getString(Utilities.MyPreferences,Utilities.OrderByKey);
        Query SortQuery;
        //Log.i(MainActivity.class.getSimpleName(),Sort);
        if(Order.equals(Utilities.OrderByKey))
        {
            SortQuery=EventRef.orderByKey();
        }
        else {
            // In that case it will be our list name or Owner email
            SortQuery=EventRef.orderByChild(Order);
        }
        myAdapter = new FirebaseRecyclerAdapter<Event, EventsListViewHolder>(Event.class, R.layout.events_list, EventsListViewHolder.class, SortQuery) {
            @Override
            protected void populateViewHolder(EventsListViewHolder eventsListViewHolder, final Event event, int i) {
                eventsListViewHolder.populate(event);
                eventsListViewHolder.LayoutView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       /* Toast.makeText(getApplicationContext(),event.geteventName()
                                +" was clicked",Toast.LENGTH_LONG).show();*/
                        ArrayList<String> HangoInfo= new ArrayList<>();
                        HangoInfo.add(event.getid());
                        HangoInfo.add(event.geteventName());
                        HangoInfo.add(event.getcreatorEmail());
                        startActivity(HangoDetails.newInstance(getApplicationContext(),HangoInfo));
                    }
                });
                eventsListViewHolder.LayoutView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        //check that the owner of the list is the owner of the email right now
                       // Log.i("Creator Email",Utilities.encodeEmail(event.getcreatorEmail()));
                      //  Log.i("User Email",UserEmail);
                        if(UserEmail.equals(Utilities.encodeEmail(event.getcreatorEmail())))
                        {
                            DialogFragment dialogFragment= DeleteEventDialogFragment.newInstance(event.getid(),true);
                            dialogFragment.show(getFragmentManager(),DeleteEventDialogFragment.class.getSimpleName());
                             return true;
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Only The Creator Can Delete The Hango",Toast.LENGTH_LONG)
                                    .show();
                            return true;
                        }
                    }
                });
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        myAdapter.cleanup();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout:
                ProgressDialog myProgressDialog = new ProgressDialog(this);
                myProgressDialog.setTitle("Loading...");
                myProgressDialog.setMessage("Logging Out");
                myProgressDialog.setCancelable(false);
                SharedPreferences.Editor Editor=sharedPreferences.edit();
                Editor.putString(Utilities.Email,null).apply();
                Editor.putString(Utilities.Username,null).apply();
                myAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                return true;
           /* case R.id.sort:
                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                return true;*/
            case R.id.friends:
                startActivity(new Intent(this, AddFriendActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.activity_main_EVAB)
    public void setFloatingActionButton(){
        DialogFragment dialogFragment= AddEventDialogFragment.newInstance();
        dialogFragment.show(getFragmentManager(),AddEventDialogFragment.class.getSimpleName());
    }
}
