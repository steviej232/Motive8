/* Copyright statement */

package sjohns70.motive8;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.plus.model.people.Person;

import java.util.ArrayList;

/**
 * RewardsAdapter.java
 *
 * This class
 */
public class RewardsAdapter extends BaseAdapter{

    private final ArrayList<RewardsData> rewards;
    private final Typeface font;

    public RewardsAdapter(ArrayList<RewardsData> itemName, Typeface font) {
        this.rewards = itemName;
        this.font = font;}

    @Override
    public int getCount(){ return rewards.size(); }

    @Override
    public RewardsData getItem(int position) { return rewards.get(position); }

    @Override
    public long getItemId(int position) { return 0; }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final RewardsData reward = getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.my_rewards, null);
        }

        TextView companyName = (TextView) convertView.findViewById(R.id.name2);
        TextView description = (TextView) convertView.findViewById(R.id.description2);

        companyName.setText(reward.getName());
        description.setText(reward.getDescription());

        companyName.setTypeface(font);
            return convertView;

        };

}
