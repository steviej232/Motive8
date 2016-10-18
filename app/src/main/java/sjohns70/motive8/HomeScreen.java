package sjohns70.motive8;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

import static sjohns70.motive8.R.styleable.CoordinatorLayout;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class HomeScreen extends AppCompatActivity {
    public LoginButton loginButton;
    public CallbackManager callbackManager;
    public Button settings;
    public Button c_list;
    public Button circle_fill;
    private android.support.design.widget.CoordinatorLayout coordinatorLayout;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG ="sjohns70";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.home_screen);
        initFacebook();

        checkFirstRun();

        // Initialize Firebase Auth
        FirebaseApp.initializeApp(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

//        if(isLoggedIn()){
//            Intent myIntent = new Intent(HomeScreen.this, MapsActivity.class);
//            startActivity(myIntent);
//        }
        BottomBarActivity bottomBarActivity = new BottomBarActivity();
        bottomBarActivity.createBottomBar(this,savedInstanceState,HomeScreen.this,0);
    }


    public void initFacebook(){
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile","user_friends");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                Intent myIntent = new Intent(HomeScreen.this, MapsActivity.class);
                startActivity(myIntent);
            }
            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"Canceled logging in",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(),"Error logging in",Toast.LENGTH_SHORT).show();
            }
        });
        loginButton.setReadPermissions("user_friends");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                        }
                    }
                });
    }

    /**
     * This method uses shared preferences to save the current app version code. It will check if
     * this startup of the app is the first launch after installation, first launch after updating,
     * or just a normal run.
     */
    private void checkFirstRun() {
        final String PREFS_NAME = "Motive8PrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        // Get current version code (pulls from build.gradle file)
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code from shared preferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Compare current and saved version codes to determine run type
        if (currentVersionCode == savedVersionCode) {
            // Normal run
            //Toast.makeText(getApplicationContext(), "Normal run", Toast.LENGTH_SHORT).show();
            runTutorial();
            return;
        }
        else if (savedVersionCode == DOESNT_EXIST) {
            // First run
            //Toast.makeText(getApplicationContext(), "First run", Toast.LENGTH_SHORT).show();
            runTutorial();
        }
        else if (currentVersionCode > savedVersionCode) {
            // This is an upgrade
            //Toast.makeText(getApplicationContext(), "Upgraded", Toast.LENGTH_SHORT).show();
        }

        // Update shared preferences with current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }

    /* This method runs an app tutorial using showcaseview to instruct the user
     * on how to use the app */
    private void runTutorial() {
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this);
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(250); // quarter second between each showcase view
        sequence.setConfig(config);

        sequence.addSequenceItem (
                new MaterialShowcaseView.Builder(this)
                        .setTarget(findViewById(R.id.home_item))
                        .setTitleText("Home Button")
                        .setTitleTextColor(ContextCompat.getColor(this, R.color.white))
                        .setDismissText("GOT IT")
                        .setDismissTextColor(ContextCompat.getColor(this, R.color.white))
                        .setContentText("This is the home button")
                        .setContentTextColor(ContextCompat.getColor(this, R.color.white))
                        .setMaskColour(Color.parseColor("#F2FFA500"))
                        .withRectangleShape()
                        .build()
        );

        sequence.addSequenceItem (
                new MaterialShowcaseView.Builder(this)
                        .setTarget(findViewById(R.id.coupons_item))
                        .setTitleText("Coupons Button")
                        .setDismissText("GOT IT")
                        .setContentText("This is the coupons button")
                        .withRectangleShape()
                        .build()
        );

        sequence.addSequenceItem (
                new MaterialShowcaseView.Builder(this)
                        .setTarget(findViewById(R.id.leaderboard_item))
                        .setTitleText("Leaderboard Button")
                        .setDismissText("GOT IT")
                        .setContentText("This is the leaderboard button")
                        .withRectangleShape()
                        .build()
        );

        sequence.addSequenceItem (
                new MaterialShowcaseView.Builder(this)
                        .setTarget(findViewById(R.id.more_item))
                        .setTitleText("More Button")
                        .setDismissText("GOT IT")
                        .setContentText("This is the more button")
                        .withRectangleShape()
                        .build()
        );

        sequence.addSequenceItem (
                new MaterialShowcaseView.Builder(this)
                        .setTarget(findViewById(R.id.login_button))
                        .setTitleText("Login Button")
                        .setDismissText("GOT IT")
                        .setContentText("Log in using Facebook!")
                        .withRectangleShape()
                        .build()
        );

        sequence.start();
    }
}