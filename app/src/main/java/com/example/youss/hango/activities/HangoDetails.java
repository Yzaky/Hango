package com.example.youss.hango.activities;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import com.example.youss.hango.R;
import com.example.youss.hango.dialog.ChangeHangoNameFragment;
import com.example.youss.hango.dialog.DeleteEventDialogFragment;
import com.example.youss.hango.entities.Event;
import com.example.youss.hango.entities.User;
import com.example.youss.hango.infrastructure.Hango;
import com.example.youss.hango.infrastructure.Utilities;
import com.example.youss.hango.services.EventService;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class HangoDetails extends BaseActivity {

    public static final String HANGO_DETAILS = "Hango_Details";

    private String HangoID;
    private String HangoName;
    private String HangoOwner;
    private Firebase HangoRef;
    private ValueEventListener HangoListener;
    private Event CurrentHango; // Since the listener is going to get the info from the current Hango


    public static Intent newInstance(Context context, ArrayList<String> HangoInfo) {
        Intent intent = new Intent(context, HangoDetails.class);
        intent.putExtra(HANGO_DETAILS, HangoInfo);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hango_details);
        HangoID = getIntent().getStringArrayListExtra(HANGO_DETAILS).get(0);
        HangoName = getIntent().getStringArrayListExtra(HANGO_DETAILS).get(1);
        HangoOwner = getIntent().getStringArrayListExtra(HANGO_DETAILS).get(2);
        HangoRef = new Firebase(Utilities.FireBaseHangoReferences + UserEmail + "/" + HangoID);
        myBus.post(new EventService.GetCurrentHangoRequest(HangoRef));
        getSupportActionBar().setTitle(HangoName);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_list_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.change_hango_name:
                ArrayList<String> HangoInfo = new ArrayList<>();
                HangoInfo.add(HangoID);
                HangoInfo.add(HangoName);
                DialogFragment dialog = ChangeHangoNameFragment.newInstance(HangoInfo);
                dialog.show(getFragmentManager(), ChangeHangoNameFragment.class.getSimpleName());
                return true;

            case R.id.delete_hango: // Si on veut supprimer l'evenemnet
                DialogFragment dialog1 = DeleteEventDialogFragment.newInstance(HangoID,false);
                dialog1.show(getFragmentManager(),DeleteEventDialogFragment.class.getSimpleName());
                return true;

            case R.id.share_hango:// Si on veut le partager
                startActivity(new Intent(this,ShareHangoActivity.class));
                return true;

        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        HangoRef.removeEventListener(HangoListener);
    }

    @Subscribe
    public void getCurrentHango(EventService.GetCurrentHangoResponse response) {
        HangoListener = response.valueEventListener;
        CurrentHango = response.event;
        getSupportActionBar().setTitle(CurrentHango.geteventName());
        if (response.event != null) {

        }
    }
}

