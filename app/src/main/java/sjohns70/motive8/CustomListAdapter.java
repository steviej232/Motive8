/* Copyright statement */

package sjohns70.motive8;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * CustomListAdapter.java
 *
 * This class
 */
public class CustomListAdapter extends BaseAdapter {

    private final ArrayList<BusinessData> businesses;
    private Typeface namefont;
    private Typeface descriptFont;

    public CustomListAdapter(ArrayList<BusinessData> itemName, Typeface namefont, Typeface decriptFont) {
        this.businesses = itemName;
        this.namefont = namefont;
        this.descriptFont = descriptFont;
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

        TextView companyName = (TextView) convertView.findViewById(R.id.name);
        TextView description = (TextView) convertView.findViewById(R.id.description);
        ImageView image = (ImageView) convertView.findViewById(R.id.icon);

        companyName.setText(bus.getCompany_name());
        description.setText(bus.getDescription());


        Resources res = parent.getResources();
        String mDrawableName = bus.getLogo();
        int resID = res.getIdentifier(mDrawableName , "drawable", parent.getContext().getPackageName());
        image.setImageResource(resID);

        companyName.setTypeface(namefont);
        description.setTypeface(descriptFont);

        return convertView;

    };

}
