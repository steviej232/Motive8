package sjohns70.motive8;

/**
 * Created by KendallGassner on 2/23/17.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import sjohns70.motive8.data.BusinessData;

public class CompanyPageAdapter extends PagerAdapter  {

    private Context mContext;
    private ArrayList<BusinessData> bussineses;

    public CompanyPageAdapter(Context context, ArrayList<BusinessData> bus) {
        mContext = context;
        bussineses = bus;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.my_pager_list, collection, false);
        TextView name = (TextView) layout.findViewById(R.id.pager_business_name);
        TextView description = (TextView) layout.findViewById(R.id.pager_business_name);
        if(bussineses.size() > position) {
            BusinessData business = bussineses.get(position);
            name.setText(business.getCompany_name());
            description.setText(business.getDescription());
            setFonts(name);
        }
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //BusinessData customPagerEnum = bussineses.get(position);
        return "HERE";
    }

    private void setFonts(TextView type){
        Typeface custom_font = Typeface
                .createFromAsset(mContext.getAssets(),  "fonts/BebasNeue Bold.otf");
        type.setTypeface(custom_font);
    }
}
