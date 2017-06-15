package com.example.youss.hango.activities;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.youss.hango.R;
import com.example.youss.hango.dialog.AddEventDialogFragment;
import com.example.youss.hango.entities.User;
import com.example.youss.hango.infrastructure.Utilities;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.activity_main_EVAB)
    FloatingActionButton floatingActionButton;

    private String bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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
                myProgressDialog.dismiss();
        }

        return true;
    }

    @OnClick(R.id.activity_main_EVAB)
    public void setFloatingActionButton(){
        DialogFragment dialogFragment= AddEventDialogFragment.newInstance();
        dialogFragment.show(getFragmentManager(),AddEventDialogFragment.class.getSimpleName());
    }
}
