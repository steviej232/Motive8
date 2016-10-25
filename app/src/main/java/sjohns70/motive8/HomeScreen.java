package sjohns70.motive8;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.roughike.bottombar.BottomBar;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class HomeScreen extends AppCompatActivity  {
        public Button settings;
        public Button c_list;
        public Button circle_fill;
        private android.support.design.widget.CoordinatorLayout coordinatorLayout;
        private String TAG ="sjohns70";
        private BottomBar bottomBar;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        // Initialize Firebase Auth
        FirebaseApp.initializeApp(getApplicationContext());


        Button signOut = (Button) findViewById(R.id.sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent logIn = new Intent(HomeScreen.this, Login.class);
                startActivity(logIn);
            }
        });


        BottomBarActivity bottomBarActivity = new BottomBarActivity();
        bottomBar = bottomBarActivity.createBottomBar(this,savedInstanceState,HomeScreen.this,0);
        checkFirstRun();
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
            //runTutorial();
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
                        .setTarget(bottomBar.getBar())
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
                        .setTarget(bottomBar.getBar())
                        .setTitleText("Coupons Button")
                        .setDismissText("GOT IT")
                        .setContentText("This is the coupons button")
                        .withRectangleShape()
                        .build()
        );

        sequence.addSequenceItem (
                new MaterialShowcaseView.Builder(this)
                        .setTarget(bottomBar.getBar())
                        .setTitleText("Leaderboard Button")
                        .setDismissText("GOT IT")
                        .setContentText("This is the leaderboard button")
                        .withRectangleShape()
                        .build()
        );

        sequence.addSequenceItem (
                new MaterialShowcaseView.Builder(this)
                        .setTarget(bottomBar.getBar())
                        .setTitleText("More Button")
                        .setDismissText("GOT IT")
                        .setContentText("This is the more button")
                        .withRectangleShape()
                        .build()
        );


        sequence.start();
    }
}