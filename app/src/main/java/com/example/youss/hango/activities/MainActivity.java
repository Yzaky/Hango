package com.example.youss.hango.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.youss.hango.R;
import com.example.youss.hango.entities.User;
import com.example.youss.hango.infrastructure.Utilities;

public class MainActivity extends BaseActivity {
    private String bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Username.contains(" "))
        {
            bar=Username.substring(0,Username.indexOf(" "))+ "'s list";
        }
        else {
            bar=Username+"'s list";
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
}
