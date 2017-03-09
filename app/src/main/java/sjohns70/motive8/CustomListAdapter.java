/* Copyright statement */

package sjohns70.motive8;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

import sjohns70.motive8.data.BusinessData;
import sjohns70.motive8.data.RewardsData;
import sjohns70.motive8.data.UserData;

/**
 * CustomListAdapter.java
 *
 * This class
 */

public class CustomListAdapter extends BaseAdapter {

    private final ArrayList<BusinessData> businesses;
    private Typeface namefont;
    private ArrayList<RewardsData> rewards;
    private FirebaseDatabase database;
    private DatabaseReference myBusinessRef;
    private Context context;
    private GoogleApiClient mGoogleApiClient;

    public CustomListAdapter(ArrayList<BusinessData> itemName, Typeface namefont, Context context) {
        this.businesses = itemName;
        this.namefont = namefont;
        this.context = context;
    }

    @Override
    public int getCount(){ return businesses.size(); }

    @Override
    public BusinessData getItem(int position) { return businesses.get(position); }

    @Override
    public long getItemId(int position) { return 0; }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final BusinessData bus = getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.mylist, null);
        }

        TextView name = (TextView) convertView.findViewById(R.id.list_business_name);
        TextView descript = (TextView) convertView.findViewById(R.id.list_business_description);
        TextView phone = (TextView) convertView.findViewById(R.id.list_business_phone);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            phone.setText(PhoneNumberUtils.formatNumber(bus.getPhone(), Locale.getDefault().getCountry()));
        }
        else {
            phone.setText(PhoneNumberUtils.formatNumber(bus.getPhone())); //Deprecated method
        }

        name.setText(bus.getCompany_name());
        descript.setText(bus.getDescription());
        rewards = new ArrayList<RewardsData>();
        getRewards(bus.getId(), convertView);

//        if(rewards.size() > 0){
//            TextView reward = (TextView) convertView.findViewById(R.id.list_reward_name);
//            TextView rewardValue = (TextView) convertView.findViewById(R.id.list_reward_value);
//
//            reward.setText(rewards.get(0).getName());
//            String val = String.format("%d points", rewards.get(0).getValue());
//            rewardValue.setText(val);
//
//            reward.setTypeface(namefont);
//            rewardValue.setTypeface(namefont);
//
//        }
        phone.setTypeface(namefont);
        name.setTypeface(namefont);
        descript.setTypeface(namefont);
        return convertView;

    };

    protected void getRewards(final String businessId, final  View convertView){
        FirebaseApp.initializeApp(this.context);
        database = FirebaseDatabase.getInstance();
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
                if(rewards.size() > 0){
                    TextView rewardName = (TextView) convertView.findViewById(R.id.list_reward_name);
                    TextView rewardValue = (TextView) convertView.findViewById(R.id.list_reward_value);

                    rewardName.setText(rewards.get(0).getName());
                    String val = String.format("%d points", rewards.get(0).getValue());
                    rewardValue.setText(val);

                    rewardName.setTypeface(namefont);
                    rewardValue.setTypeface(namefont);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Failed");
            }
        });
    }

}


