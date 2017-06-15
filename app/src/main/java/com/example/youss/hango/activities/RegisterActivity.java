package com.example.youss.hango.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.*;

import com.example.youss.hango.R;
import com.example.youss.hango.services.AccountServices;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RegisterActivity extends BaseActivity {

    @BindView(R.id.activityregisterlinearlayout) LinearLayout linearLayout2;
    @BindView(R.id.activityregisterloginButton) Button loginButton2;
    @BindView(R.id.activityregisteruserEmail) EditText userEmail;
    @BindView(R.id.activityregisteruserName) EditText userName;
    @BindView(R.id.activityregisterregisterButton) Button registerButton;

    ProgressDialog myProgressDialog;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        //linearLayout2.setBackgroundResource(R.drawable.background_screen_two);
        linearLayout2.setBackgroundColor(Color.WHITE);

        myProgressDialog = new ProgressDialog(this);
        myProgressDialog.setTitle("Loading...");
        myProgressDialog.setMessage("Attempting to Register Account");
        myProgressDialog.setCancelable(false);
    }

    @OnClick(R.id.activityregisterloginButton)
    public void setLoginButton(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @OnClick(R.id.activityregisterregisterButton)
    public void setRegisterButton(){
                        //Passing the post to the myBus
        myBus.post(new AccountServices.RegisterANewUser(
                userName.getText().toString(),userEmail.getText().toString(),myProgressDialog,sharedPreferences));

    }
    @Subscribe
    public void registerUser(AccountServices.RegisterANewUserResponse response)
    {
        if(!response.didSucceed()){
            userEmail.setError(response.getError("email"));
            userName.setError(response.getError("username"));

        }
    }
}
