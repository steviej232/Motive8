/* Copyright statement */

package sjohns70.motive8;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import sjohns70.motive8.data.UserData;

/**
 * Login.java
 *
 * This class handles the user login process, authenticating user credentials.
 */
public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    public LoginButton facebookLoginButton;
    private EditText login_email;
    private EditText login_password;
    private Button login_submit;
    private TextView login_sign_up;
    private TextView login_sign_up_text;
    private TextView login_forgot_password;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public CallbackManager callbackManager;

    private String TAG = "login: ";
    private FirebaseDatabase database;
    private UserData userData;
    private int RC_SIGN_IN = 1;
    public GoogleApiClient mGoogleApiClient;
    private SharedPreferences pref;
    private String emailAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.login);

        login_email = (EditText) findViewById(R.id.login_email);
        login_password = (EditText) findViewById(R.id.login_password);
        login_sign_up = (TextView) findViewById(R.id.sign_up);
        login_sign_up_text = (TextView) findViewById(R.id.sign_up_text);
        login_forgot_password = (TextView) findViewById(R.id.forgot_password);
        login_submit = (Button) findViewById(R.id.submit_login);

//        String UID = "";
//        if(pref != null) {
//            pref.getString("user_hashcode", UID);
//            if (UID != "") {
//                Intent myIntent = new Intent(Login.this, HomeScreen.class);
//                startActivity(myIntent);
//            }
//        }

        setupFont();

        FirebaseApp.initializeApp(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent myIntent = new Intent(Login.this, CompanyListActivity.class);
                    startActivity(myIntent);
                }
            }
        };

        initFacebook();

        login_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

            }
        });

        login_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(Login.this, SignUp.class);
                startActivity(signup);
            }
        });

        login_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create alert dialog to accept user's email address
                AlertDialog.Builder passResetBuilder = new AlertDialog.Builder(Login.this);
                passResetBuilder.setTitle("Enter Email Address:");
                final EditText input = new EditText(Login.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                passResetBuilder.setView(input);

                passResetBuilder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        emailAddress = input.getText().toString();
                        sendResetEmail(emailAddress);
                    }
                });
                passResetBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                passResetBuilder.show();
            }
        });

//        if(isLoggedIn()){
//            Intent home = new Intent(Login.this, HomeScreen.class);
//            startActivity(home);
//        }

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

    private void signIn() {
        String email = login_email.getText().toString();
        String password = login_password.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    //on the login store the login
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    editor.putString("user_hascode", user.getUid());
                    editor.commit();
                    Intent home = new Intent(Login.this, HomeScreen.class);
                    startActivity(home);
                }

                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                    Toast.makeText(Login.this, R.string.auth_failed,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendResetEmail(String emailAddress) {
        mAuth = FirebaseAuth.getInstance();

        mAuth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent");
                            Toast.makeText(Login.this, "Password Reset Email Sent", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void initFacebook(){
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        facebookLoginButton = (LoginButton) findViewById(R.id.login_button);
        facebookLoginButton.setReadPermissions("email", "public_profile","user_friends","publish_actions");
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //AccessToken accessToken =  loginResult.getAccessToken();
                handleFacebookAccessToken(loginResult.getAccessToken());
                //Toast.makeText(getApplicationContext(),"userid:"+loginResult.getAccessToken().getUserId(),Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(Login.this, HomeScreen.class);
//                myIntent.putExtra("accesstoken",accessToken);
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
        facebookLoginButton.setReadPermissions("user_friends");
    }

    private void FacebookSignOut() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        } else {
            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                    .Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {

                    LoginManager.getInstance().logOut();

                }
            }).executeAsync();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            Intent myIntent = new Intent(Login.this, HomeScreen.class);
            startActivity(myIntent);
        }
    }



    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    /**
     * This method sets up the "MOTIV8" title for the home screen as well
     * as other fonts.
     */
    private void setupFont() {
        TextView title = (TextView)findViewById(R.id.login_title);
        String titleText = "<font color=#404040>Motiv</font><font color=#FFA03E>8</font>";
        Typeface bebasFont_bold = Typeface.createFromAsset(getAssets(), "fonts/BebasNeue Bold.otf");
        Typeface bebasFont = Typeface.createFromAsset(getAssets(), "fonts/BebasNeue Regular.otf");

        title.setText(Html.fromHtml(titleText));
        title.setTypeface(bebasFont_bold);
        login_sign_up_text.setTypeface(bebasFont);
        login_sign_up.setTypeface(bebasFont);
        login_email.setTypeface(bebasFont);
        login_password.setTypeface(bebasFont);
        login_submit.setTypeface(bebasFont_bold);
        login_forgot_password.setTypeface(bebasFont_bold);
        title.setTextSize(100);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
