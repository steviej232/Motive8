package sjohns70.motive8;

/**
 * Created by Steven on 9/14/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {

    private final ArrayList<BusinessData> businesses;

    public CustomListAdapter(ArrayList<BusinessData> itemName) { this.businesses = itemName; }

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

        TextView companyName = (TextView) convertView.findViewById(R.id.name);
        TextView description = (TextView) convertView.findViewById(R.id.description);

        companyName.setText(bus.getCompany_name());
        description.setText(bus.getDescription());


        return convertView;

    };
}
