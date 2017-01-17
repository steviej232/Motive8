package sjohns70.motive8;

/**
 * Created by Kendall on 10/2/2016.
 *
 * This activity is used display a companies profile and available coupons
 * users should be able to purchase coupons with their points from this activity
 *
 */

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.plus.model.people.Person;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class CompanyProfileActivity extends AppCompatActivity {
    private ArrayList<RewardsData> rewards;
    private RewardsAdapter adapter;
    private int points;

    private FirebaseDatabase database;
    private DatabaseReference myBusinessRef;
    private FirebaseAuth mAuth;
    private DatabaseReference myUserRef;
    private BusinessData business;
    private UserData userData;

    private TextView name;
    private ListView list;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_company_profile);
        name = (TextView) findViewById(R.id.companyName);
        list = (ListView) findViewById(R.id.rewardsList);
        image = (ImageView) findViewById(R.id.logoCompanyProfile);
        FirebaseApp.initializeApp(getApplicationContext());
        database = FirebaseDatabase.getInstance();
        business = (BusinessData) getIntent().getSerializableExtra("Business");
        rewards = new ArrayList<RewardsData>();

        setFonts();

        getPoints();

        if (business.getCompany_name() != null) {
            name.setText(business.getCompany_name());
            getRewards(business.getId());

            Resources res = getResources();
            String mDrawableName = business.getLogo();
            int resID = res.getIdentifier(mDrawableName , "drawable", getPackageName());
            image.setImageResource(resID);
        }

        adapter = new RewardsAdapter(rewards, Typeface.createFromAsset(
                getAssets(), "fonts/Montserrat-Light.otf"));

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int couponCost = ((RewardsData) parent.getItemAtPosition(position)).getValue();
                showSimplePopUp(parent.getItemAtPosition(position).toString(), couponCost);
            }
        });

        //create an instance of the bottom bar
        BottomBarActivity bottomBarActivity = new BottomBarActivity();
        bottomBarActivity.createBottomBar(this, savedInstanceState, CompanyProfileActivity.this, 2);

    }

    /*
    * Here we are gathering all of the rewards from the database
    */
    protected void getRewards(final String businessId){
        myBusinessRef = database.getReference("REWARDS");
        myBusinessRef.keepSynced(true);

        myBusinessRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot data : dataSnapshot.getChildren()) {
                    RewardsData reward = data.getValue(RewardsData.class);
                    if(businessId.equals(reward.getBusiness_id()))
                       rewards.add(reward);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Failed");
            }
        });
    }

    protected void getPoints(){

        mAuth = FirebaseAuth.getInstance();
        myUserRef = database.getReference("USERS").child(mAuth.getCurrentUser().getUid());
        userData = new UserData();

        myUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userData = dataSnapshot.getValue(UserData.class);
                points = userData.getPoints_earned();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });
    }
    /*
    * this function creates a popup to show up when a user select one
    * of the companies coupons
    * */
    private void showSimplePopUp(String name, final int CouponsPoint) {
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle(name);
        helpBuilder.setMessage(name + "description");
        helpBuilder.setPositiveButton("Accept",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    if(points >= CouponsPoint){
                        myUserRef.child("points_earned")
                                .setValue(points - CouponsPoint);

                        showDialog();

                        //expanded pop-up with coupon <code></code>
                    }
                    else{
                        //not enough funds
                    }
                    }
                });
        helpBuilder.setNegativeButton("back",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                    }
                });
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }

    /*
    * this function allows us to use custom fonts
    */
    private void setFonts(){
        Typeface custom_font = Typeface
                .createFromAsset(getAssets(),  "fonts/Montserrat-Bold.otf");
        name.setTypeface(custom_font);
    }


    private void showDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.rewards_popup);

        // set the custom dialog components - text, image and button
        ImageButton close = (ImageButton) dialog.findViewById(R.id.btnClose);

        // Close Button
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //TODO Close button action
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }

}

