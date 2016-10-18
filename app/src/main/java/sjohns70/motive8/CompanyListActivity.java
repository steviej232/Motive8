package sjohns70.motive8;

/**
 * Created by Steven on 9/14/2016.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CompanyListActivity extends Activity{
    private CustomListAdapter adapter;
    private ArrayList<BusinessData> businesses;

    ListView list;
    String[] itemname ={
            "Adidas",
            "Amazon",
            "Apple",
            "Baskin Robbins",
            "Dell",
            "Money",
            "Dominos",
            "Intel",
            "Jamba Juice",
            "Nasa",
            "Pepsi",
            "Starbucks",
            "Target"
    };

    Integer[] imgid={
            R.drawable.adidas,
            R.drawable.amazon,
            R.drawable.apple,
            R.drawable.baskin_robbins,
            R.drawable.dell,
            R.drawable.dollar,
            R.drawable.dominos,
            R.drawable.intel,
            R.drawable.jamba_juice,
            R.drawable.nasa,
            R.drawable.pepsi,
            R.drawable.starbucks,
            R.drawable.target,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ListView list;
        DatabaseReference myRef;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_list);

        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("BUSINESSES");
        myRef.keepSynced(true);

        businesses = new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot data : dataSnapshot.getChildren()) {
                    BusinessData bus = data.getValue(BusinessData.class);
                    businesses.add(bus);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Failed");
            }
        });

        adapter = new CustomListAdapter(businesses);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(CompanyListActivity.this, CompanyProfileActivity.class);
                Bundle bun = new Bundle();
                bun.putSerializable("Business", businesses.get(position));
                myIntent.putExtras(bun);
                parent.getContext().startActivity(myIntent);
            }
        });

        BottomBarActivity bottomBarActivity = new BottomBarActivity();
        bottomBarActivity.createBottomBar(this,savedInstanceState,CompanyListActivity.this,2);
    }
}

