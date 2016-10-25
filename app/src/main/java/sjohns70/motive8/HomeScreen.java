package sjohns70.motive8;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;


public class HomeScreen extends AppCompatActivity  {
        public Button settings;
        public Button c_list;
        public Button circle_fill;
        private android.support.design.widget.CoordinatorLayout coordinatorLayout;
        private String TAG ="sjohns70";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        checkFirstRun();
            FirebaseApp.initializeApp(getApplicationContext());


            createBottomBar(this,savedInstanceState,HomeScreen.this);

             Button signOut = (Button) findViewById(R.id.sign_out);
            signOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    Intent logIn = new Intent(HomeScreen.this, Login.class);
                    startActivity(logIn);
                }
            });

         }



    public void createBottomBar(Activity activity, Bundle savedInstanceState, final Context className){
        BottomBar bottomBar = BottomBar.attach(activity, savedInstanceState);
        bottomBar.setItemsFromMenu(R.menu.four_buttons_menu, new OnMenuTabSelectedListener() {
            @Override
            public void onMenuItemSelected(int itemId) {
                switch (itemId) {
                    case R.id.home_item:
                        Intent myIntent = new Intent(className, HomeScreen.class);
                        className.startActivity(myIntent);
                        break;
                    case R.id.coupons_item:
                        Intent myIntent4 = new Intent(className, MapsActivity.class);
                        className.startActivity(myIntent4);
                        break;
                    case R.id.leaderboard_item:
                        Intent myIntent3 = new Intent(className,CompanyListActivity.class);
                        className.startActivity(myIntent3);
                        break;
                    case R.id.more_item:
                        Intent myIntent2 = new Intent(className, CircleActivity.class);
                        className.startActivity(myIntent2);
                        break;
                }
            }
        });
// Set the color for the active tab. Ignored on mobile when there are more than three tabs.
        bottomBar.setActiveTabColor("#C2185B");

// Use the dark theme. Ignored on mobile when there are more than three tabs.
        bottomBar.useDarkTheme(true);

// Use custom text appearance in tab titles.
        bottomBar.setTextAppearance(R.style.BB_BottomBarItem_Fixed);

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
            // Current app version code is < saved version code ?
            return;
        }

        // Update shared preferences with current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }




}