package sjohns70.motive8;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

public class HomeScreen extends AppCompatActivity {
    public LoginButton loginButton;
    public CallbackManager callbackManager;
    public Button settings;
    public Button c_list;
    public Button circle_fill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.home_screen);
        initFacebook();

        checkFirstRun();

        settings = (Button) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(HomeScreen.this, MapsActivity.class);
                startActivity(myIntent);
            }
        });

        c_list = (Button) findViewById(R.id.company_list);
        c_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(HomeScreen.this, CompanyListActivity.class);
                startActivity(myIntent);
            }
        });

        circle_fill = (Button) findViewById(R.id.circle_fill);
        circle_fill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(HomeScreen.this, CircleActivity.class);
                startActivity(myIntent);
            }
        });

        if (isLoggedIn()) {
            Intent myIntent = new Intent(HomeScreen.this, MapsActivity.class);
            startActivity(myIntent);
        }
    }

    public void initFacebook() {
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent myIntent = new Intent(HomeScreen.this, MapsActivity.class);
                startActivity(myIntent);
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Canceled logging in", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(), "Error logging in", Toast.LENGTH_SHORT).show();
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

    /**
     * This method uses shared preferences to save the current app version code. It will check if
     * this startup of the app is the first launch after installation, first launch after updating,
     * or just a normal run.
     */
    private void checkFirstRun() {
        final String PREFS_NAME = "Motive8PrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code from shared preferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Compare current and saved version codes to determine run type
        if (currentVersionCode == savedVersionCode) {
            // Normal run
            Toast.makeText(getApplicationContext(), "Normal run", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (savedVersionCode == DOESNT_EXIST) {
            // First run
            Toast.makeText(getApplicationContext(), "First run", Toast.LENGTH_SHORT).show();
        }
        else if (currentVersionCode > savedVersionCode) {
            // This is an upgrade
            Toast.makeText(getApplicationContext(), "Upgraded", Toast.LENGTH_SHORT).show();
        }
        else {
            // The user somehow reverted versions of the app
            return;
        }

        // Update shared preferences with current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }
}