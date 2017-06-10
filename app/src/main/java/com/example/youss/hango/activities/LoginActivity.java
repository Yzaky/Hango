package com.example.youss.hango.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.example.youss.hango.R;
import com.example.youss.hango.services.AccountServices;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;


public class LoginActivity extends BaseActivity {

    @BindView(R.id.activityloginlinearlayout) LinearLayout linearLayout;
    @BindView(R.id.activitymainregisterbutton) Button registerButton;
    @BindView(R.id.activitymainloginbutton) Button loginButton;
    @BindView(R.id.activitymainuseremail) EditText userEmail;
    @BindView(R.id.activitymainuserpassword) EditText userPassword;
    @BindView(R.id.facebook_login_button) LoginButton FacebookButton;
    CallbackManager myCallbackMAnager;
    ProgressDialog myProgressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        linearLayout.setBackgroundResource(R.drawable.background_screen_two);
        FirebaseAuth.getInstance().signOut();

        myProgressDialog = new ProgressDialog(this);
        myProgressDialog.setTitle("Loading...");
        myProgressDialog.setMessage("Attempting to Log in ");
        myProgressDialog.setCancelable(false);
    }
    @OnClick(R.id.activitymainregisterbutton)
    public void setRegisterButton(){
        startActivity(new Intent(this,RegisterActivity.class));
        finish();
    }
    @OnClick(R.id.activitymainloginbutton)
    public void setLoginButton(){
        myBus.post(new AccountServices.RequestaUserLogIn(userEmail.getText().toString(),
                userPassword.getText().toString(),myProgressDialog));
    }

    @OnClick (R.id.facebook_login_button)
    public void setFacebookButton() {
        myCallbackMAnager = CallbackManager.Factory.create();
        FacebookButton.setReadPermissions("public_profile", "email");
        FacebookButton.registerCallback(myCallbackMAnager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult loginResult) {
                String accessToken = loginResult.getAccessToken().getToken();
                Log.i("accessToken", accessToken);

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                //Log.e("GraphResponse", "-------------" + response.toString());
                        try {
                            String name=object.getString("name");
                            String email= object.getString("email");
                            myBus.post(new AccountServices.RequestAFacebookLogin(loginResult.getAccessToken(),myProgressDialog, name,email));
                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "An Error Occurred", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        myCallbackMAnager.onActivityResult(requestCode,resultCode,data); // when facebook comes back to app !
    }

    @Subscribe
    public void logUserIn(AccountServices.RequestaUserLoginResponse response)
    {
        if(!response.didSucceed())
        {
            userEmail.setError(response.getError("email"));
            userPassword.setError(response.getError("password"));
        }
    }
}
