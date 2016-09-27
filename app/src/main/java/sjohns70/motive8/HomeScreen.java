package sjohns70.motive8;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.home_screen);
        initFacebook();
        settings = (Button)findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(HomeScreen.this, MapsActivity.class);
                startActivity(myIntent);
            }
        });
        c_list = (Button)findViewById(R.id.company_list);
        c_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(HomeScreen.this, CompanyListActivity.class);
                startActivity(myIntent);
            }
        });

        if(isLoggedIn()){
            Intent myIntent = new Intent(HomeScreen.this, MapsActivity.class);
            startActivity(myIntent);
        }
    }

    public void initFacebook(){
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

}
