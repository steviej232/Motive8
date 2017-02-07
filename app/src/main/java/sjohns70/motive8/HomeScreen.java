/* Copyright statement */

package sjohns70.motive8;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.app.Dialog;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.roughike.bottombar.BottomBar;

/**
 * HomeScreen.java
 *
 * This class creates the app's homescreen and runs a tutorial if this is the first time the app
 * is being run.
 */
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
        setupTitle();

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

        // Create bottom bar
        BottomBarActivity bottomBarActivity = new BottomBarActivity();
        bottomBar = bottomBarActivity.createBottomBar(this,savedInstanceState,HomeScreen.this,0);

        // Check if this is the first time the app has been run
        checkFirstRun();
    }

    /**
     * This method sets up the "MOTIV8" title for the home screen.
     */
    private void setupTitle() {
        TextView title = (TextView)findViewById(R.id.Title);
        String titleText = "<font color=#404040>Motiv</font><font color=#FFA03E>8</font>";
        Typeface bebasFont = Typeface.createFromAsset(getAssets(), "fonts/BebasNeue Bold.otf");

        title.setText(Html.fromHtml(titleText));
        title.setTypeface(bebasFont);
        title.setTextSize(100);
    }

    /**
     * This method uses shared preferences to save the current app version-code. It will check if
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
            runTutorial();
            return;
        }
        else if (savedVersionCode == DOESNT_EXIST) {
            // First run
            runTutorial();
        }
        else if (currentVersionCode > savedVersionCode) {
            // First run since update
            runTutorial();
        }

        // Update shared preferences with current version code
        // Invoking edit() will create prefereces file if one doesn't already exist
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }

    /**
     *  This method runs a tutorial instructing the user on how to use the app.
     */
    private void runTutorial() {
        // Create views from custom xml layout for each Dialog
        LayoutInflater inflater = getLayoutInflater();
        final View welcomeView = inflater.inflate(R.layout.tutorial_dialog, null);
        final View mapsView = inflater.inflate(R.layout.tutorial_dialog, null);
        final View couponsView = inflater.inflate(R.layout.tutorial_dialog, null);
        final View settingsView = inflater.inflate(R.layout.tutorial_dialog, null);

        // Create builders for each dialog
        final AlertDialog.Builder welcomeBuilder = new AlertDialog.Builder(HomeScreen.this);
        final AlertDialog.Builder mapsBuilder = new AlertDialog.Builder(HomeScreen.this);
        final AlertDialog.Builder couponsBuilder = new AlertDialog.Builder(HomeScreen.this);
        final AlertDialog.Builder settingsBuilder = new AlertDialog.Builder(HomeScreen.this);

        // Create settings dialog
        setupCustomDialog(settingsBuilder, settingsView, R.drawable.ic_library_add_black_24dp,
                R.string.tutorial_settingTitle, R.string.tutorial_settingMsg, null);
        final AlertDialog settingsDialog = settingsBuilder.create();

        // Create coupons dialog
        setupCustomDialog(couponsBuilder, couponsView, R.drawable.ic_card_giftcard_black_24dp,
                R.string.tutorial_couponTitle, R.string.tutorial_couponMsg, settingsDialog);
        final AlertDialog couponsDialog = couponsBuilder.create();

        // Create maps dialog
        setupCustomDialog(mapsBuilder, mapsView, R.drawable.ic_nearby, R.string.tutorial_mapTitle,
                R.string.tutorial_mapMsg, couponsDialog);
        final AlertDialog mapsDialog = mapsBuilder.create();

        // Create welcome dialog
        setupCustomDialog(welcomeBuilder, welcomeView, R.drawable.ic_home_black_24dp,
                R.string.tutorial_welcomeTitle, R.string.tutorial_welcomeMsg, mapsDialog);
        final AlertDialog welcomeDialog = welcomeBuilder.create();

        // Start the tutorial
        welcomeDialog.show();
    }

    /**
     * This method applies a custom xml layout view to a dialog box
     * @param builder The builder for the AlertDialog object
     * @param customView A view derived from an inflater representing the xml layout to be applied
     * @param imgResource Drawable resource reference for dialog title icon
     * @param titleResource String resource reference for dialog title
     * @param msgResource String resource reference for dialog message contents
     * @param next Reference to Dialog box to be opened next, or null if there is none
     */
    private void setupCustomDialog(AlertDialog.Builder builder, View customView, int imgResource,
                                   int titleResource, int msgResource, final AlertDialog next) {
        ImageView iv;
        TextView title;
        TextView msg;
        Typeface bebasFont_bold = Typeface.createFromAsset(getAssets(), "fonts/BebasNeue Bold.otf");
        Typeface bebasFont = Typeface.createFromAsset(getAssets(), "fonts/BebasNeue Regular.otf");

        builder.setView(customView);

        // Set next Dialog to be opened upon pressing "next" if there is one
        if (next != null) {
            builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    next.show();
                }
            });
        }
        else {
            builder.setPositiveButton("Next", null);
        }

        // Obtain refereces to xml objects
        iv = (ImageView) customView.findViewById(R.id.tutorial_dialogImg);
        title = (TextView) customView.findViewById(R.id.tutorial_dialogTitle);
        msg = (TextView) customView.findViewById(R.id.tutorial_dialogText);

        // Set xml object contents
        iv.setImageResource(imgResource);
        title.setText(titleResource);
        title.setTypeface(bebasFont_bold);
        msg.setText(msgResource);
        msg.setTypeface(bebasFont);

    }
}