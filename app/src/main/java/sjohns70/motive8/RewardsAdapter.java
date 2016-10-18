package sjohns70.motive8;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by KendallGassner on 10/13/16.
 */

public class RewardsAdapter extends BaseAdapter{

    private final ArrayList<RewardsData> rewards;

    public RewardsAdapter(ArrayList<RewardsData> itemName) { this.rewards = itemName; }

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
            convertView = inflater.inflate(R.layout.mylist, null);
        }

        TextView companyName = (TextView) convertView.findViewById(R.id.name);
        TextView description = (TextView) convertView.findViewById(R.id.description);

        companyName.setText(reward.getName());
        description.setText(reward.getDescription());


            return convertView;

        };
}
