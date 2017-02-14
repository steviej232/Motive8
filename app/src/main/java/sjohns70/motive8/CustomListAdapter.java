/* Copyright statement */

package sjohns70.motive8;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import sjohns70.motive8.data.BusinessData;

/**
 * CustomListAdapter.java
 *
 * This class
 */

public class CustomListAdapter extends BaseAdapter {

    private final ArrayList<BusinessData> businesses;
    private Typeface namefont;

    public CustomListAdapter(ArrayList<BusinessData> itemName, Typeface namefont) {
        this.businesses = itemName;
        this.namefont = namefont;
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

        char letter = bus.getCompany_name().charAt(0);
        ImageView image = (ImageView) convertView.findViewById(R.id.icon);
        CharacterDrawable drawable = new CharacterDrawable(letter, 0xAAFFA03E);
        image.setBackground(drawable);

        Resources res = parent.getResources();
//        String mDrawableName = bus.getLogo();
//        int resID = res.getIdentifier(mDrawableName , "drawable", parent.getContext().getPackageName());
//        if(resID !=  0) {
            //image.setImageResource(resID);
//        }

        return convertView;

    };

}


