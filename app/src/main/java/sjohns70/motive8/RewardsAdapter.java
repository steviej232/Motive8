/* Copyright statement */

package sjohns70.motive8;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sjohns70.motive8.data.RewardsData;

/**
 * RewardsAdapter.java
 *
 * This class
 */
public class RewardsAdapter extends BaseAdapter{

    private final ArrayList<RewardsData> rewards;
    private final Typeface font;
    private final Typeface pointFont;

    public RewardsAdapter(ArrayList<RewardsData> itemName, Typeface font, Typeface pointFont) {
        this.rewards = itemName;
        this.font = font;
        this.pointFont = pointFont;
    }

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

        TextView pointValue = (TextView) convertView.findViewById(R.id.point_value);
        TextView companyName = (TextView) convertView.findViewById(R.id.name2);
        TextView description = (TextView) convertView.findViewById(R.id.description2);

        pointValue.setText(Integer.toString(reward.getValue()) + " points");
        companyName.setText(reward.getName());
        description.setText(reward.getDescription());

        description.setTypeface(font);
        companyName.setTypeface(font);
        pointValue.setTypeface(pointFont);

            return convertView;

        };

}
