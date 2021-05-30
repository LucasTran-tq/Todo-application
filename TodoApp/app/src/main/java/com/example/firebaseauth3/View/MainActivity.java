package com.example.firebaseauth3.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.firebaseauth3.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "GOOGLEAUTH";
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;


    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        intiallizeGoogleLogin();
        initiallizeFacebookLogin();

        checkUser();





    }

    public void intiallizeGoogleLogin(){
        Button signInBtn = findViewById(R.id.google_signIn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });
    }

    public void initiallizeFacebookLogin(){

        LoginButton fb_login = findViewById(R.id.login_btn_facebook);
        callbackManager = CallbackManager.Factory.create();

        fb_login.setPermissions("email", "public_profile");

        fb_login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.d("Facebook", "On Success");
                firebaseAuthwithFacebook(loginResult);
            }

            @Override
            public void onCancel() {

                Log.d("Facebook", "On Cancel");
            }

            @Override
            public void onError(FacebookException error) {

                Log.d("Facebook", "On Error");

            }
        });
    }


    private void checkUser(){
        // if user is already sign in then go to profile activity
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser != null){
            Log.d(TAG, "checkUser: Already Logged in");
            startActivity(new Intent(this, Profile.class));
            finish();
        }
    }

    private void signInWithGoogle(){

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }

        else{

            callbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }

    private void firebaseAuthwithFacebook(LoginResult loginResult){

        AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isComplete()){

                            FirebaseUser user = mAuth.getCurrentUser();

                            Log.d("Facebook Login", "Success");

                            startActivity(new Intent(MainActivity.this, Profile.class));
                            finish();

                        }
                        else {
                            Log.d("Facebook Login", "Error");
                            Toast.makeText(MainActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }



                    }
                });



    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        Log.d(TAG, "onSuccess: Logged in");
                        FirebaseUser user = mAuth.getCurrentUser();

                        String uid = user.getUid();
                        String email = user.getEmail();

                        Log.d(TAG,"onSuccess: Email: " + email);
                        Log.d(TAG,"onSuccess: Uid: " + uid);

                        // check if user is new or existing
                        if(authResult.getAdditionalUserInfo().isNewUser()){
                            Log.d(TAG, "onSuccess: Account created...\n" + email);
                            Toast.makeText(MainActivity.this, "onSuccess: Account created..." + email, Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Log.d(TAG, "onSuccess: Existing User...\n" + email);
                            Toast.makeText(MainActivity.this, "onSuccess: Existing User..." + email, Toast.LENGTH_SHORT).show();
                        }

                        // start Profile Activity
                        startActivity(new Intent(MainActivity.this, Profile.class));
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d(TAG, "onFailture: Loggin failed" + e.getMessage());
                    }
                });


    }

























}