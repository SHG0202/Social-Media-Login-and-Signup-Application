package com.shg.socialloginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class MainActivity2 extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private ImageView profilePic;
    private TextView userName;
    private TextView email;
    private TextView id;
    private Button logout;

    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        profilePic = findViewById(R.id.profile_pic);
        userName = findViewById(R.id.user_name);
        email = findViewById(R.id.email);
        id = findViewById(R.id.id);
        logout = findViewById(R.id.logout);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if(status.isSuccess()){
                            gotoMainActivity();
                        }else{
                            Toast.makeText(MainActivity2.this, "Logout unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        /*
        //FACEBOOK
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        updateUI(user);
         */
    }

    private void gotoMainActivity(){
        startActivity(new Intent(MainActivity2.this, MainActivity.class));
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void handleSigninResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            
            userName.setText(account.getDisplayName());
            email.setText(account.getEmail());
            id.setText(account.getId());
            Picasso.get().load(account.getPhotoUrl()).placeholder(R.mipmap.ic_launcher).into(profilePic);
        }else{
            gotoMainActivity();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //GOOGLE
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);

        if(opr.isDone()){
            GoogleSignInResult result = opr.get();
            handleSigninResult(result);
        }else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSigninResult(googleSignInResult);
                }
            });
        }

        /*
        //FACEBOOK
        mFirebaseAuth.addAuthStateListener(authStateListener);
        */
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(authStateListener != null){
            mFirebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    /*
    private void updateUI(FirebaseUser user){
        if(user != null){
            if(user.getPhotoUrl() != null){
                userName.setText(user.getDisplayName());
                email.setText(user.getEmail());
                id.setText(user.getUid());
                Picasso.get().load(user.getPhotoUrl()).placeholder(R.mipmap.ic_launcher).into(profilePic);
            }else{
                gotoMainActivity();
            }
        }
    }
    */
}