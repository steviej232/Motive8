package sjohns70.motive8;

/**
 * Created by Kendall on 10/2/2016.
 */

import android.content.DialogInterface;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class CompanyProfileActivity extends AppCompatActivity {
    private BusinessData business;
    private Point p;
    private ArrayList<RewardsData> rewards;
    private RewardsAdapter adapter;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private TextView name;
    private TextView description;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);
        name = (TextView) findViewById(R.id.companyName);
        description = (TextView) findViewById(R.id.businessDescription);
        list = (ListView) findViewById(R.id.rewardsList);

        FirebaseApp.initializeApp(getApplicationContext());
        database = FirebaseDatabase.getInstance();
        business = (BusinessData) getIntent().getSerializableExtra("Business");
        rewards = new ArrayList<RewardsData>();


        if (business.getCompany_name() != null) {
            name.setText(business.getCompany_name());
            description.setText(business.getDescription());
            getRewards(business.getId());
        }

        adapter = new RewardsAdapter(rewards);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showSimplePopUp(parent.getItemAtPosition(position).toString());
            }
        });

    }

    protected void getRewards(final String businessId){
        myRef = database.getReference("REWARDS");
        myRef.keepSynced(true);

        myRef.addValueEventListener(new ValueEventListener() {
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


    private void showSimplePopUp(String name) {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle(name);
        helpBuilder.setMessage(name + "description");
        helpBuilder.setPositiveButton("Accept",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //we will need to update the database
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

}

