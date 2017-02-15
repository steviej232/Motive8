package sjohns70.motive8;

/**
 * Created by Steven on 2/15/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Steven on 2/15/2017.
 */

public class AppRater extends Activity {
    private final static String APP_TITLE = "Motive8";// App Name
    private final static String APP_PNAME = "sjohns70.motive8";// Package Name

    private final static int DAYS_UNTIL_PROMPT = 3;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 10;//Min number of launches

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }

        editor.commit();
    }

    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        final View rateView = inflater.inflate(R.layout.tutorial_dialog, null);
        final AlertDialog.Builder rateBuilder = new AlertDialog.Builder(mContext);
        setupCustomDialog(rateBuilder, rateView, R.drawable.logo,
                "Rate " + APP_TITLE,"If you enjoy using " + APP_TITLE + ", please take a moment to rate it. Thanks for your support!", null, 0,mContext,editor);
        final AlertDialog welcomeDialog = rateBuilder.create();
        welcomeDialog.show();
    }

    private static void setupCustomDialog(AlertDialog.Builder builder, View customView, int imgResource,
                                          String titleResource, String msgResource, final AlertDialog next, final int flag, final Context mContext, final SharedPreferences.Editor editor) {
        ImageView iv;
        TextView title;
        TextView msg;
        Typeface font = Typeface.createFromAsset(customView.getContext().getAssets(), "fonts/AlumFreePromotional.ttf");
        Typeface font2 = Typeface.createFromAsset(customView.getContext().getAssets(), "fonts/CaviarDreams.ttf");

        builder.setView(customView);

        // Set next Dialog to be opened upon pressing "next" if there is one
        builder.setPositiveButton("Rate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));

            }
        });
        builder.setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
            }
        });

        // Obtain refereces to xml objects
        iv = (ImageView) customView.findViewById(R.id.tutorial_dialogImg);
        title = (TextView) customView.findViewById(R.id.tutorial_dialogTitle);
        msg = (TextView) customView.findViewById(R.id.tutorial_dialogText);

        // Set xml object contents
        iv.setImageResource(imgResource);
        title.setText(titleResource);
        title.setTypeface(font);
        msg.setText(msgResource);
        msg.setTypeface(font2);

    }
}

