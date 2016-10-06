package sjohns70.motive8;

/**
 * Created by Kendall on 10/2/2016.
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CompanyProfileActivity extends AppCompatActivity {

    private String companyName;
    private ListView list;

    private String[] rewardsName;

    private Integer[] imgid={
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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);

        companyName = getIntent().getStringExtra("companyName");
        int position = getIntent().getIntExtra("position", 0);

        if (companyName != null) { //change all values to database values
            TextView name = (TextView) findViewById(R.id.companyName);
            name.setText(companyName);
            TextView description = (TextView) findViewById(R.id.businessDescription);
            description.setText("description for " + companyName);
            ImageView imageView = (ImageView) findViewById(R.id.imageView2);
            imageView.setImageResource(imgid[position]);
            rewardsName = getRewards(position);

        }

        CustomListAdapter adapter = new CustomListAdapter(this, rewardsName, imgid); //need to replace images with database value
        list = (ListView) findViewById(R.id.rewardsList);
        list.setAdapter(adapter);
    }

    protected String[] getRewards(int position){
        //this will eventually get values out of the database
        String[] rewards = {"discount1", "discount2", "discount3"};
        return rewards;
    }

}

