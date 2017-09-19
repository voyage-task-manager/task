package adapters;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.felipe.app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import models.Day;

/**
 * Created by felipe on 14/09/17.
 */

public class ViewPageAdapter extends PagerAdapter {

    private Activity activity;
    private ArrayList<ArrayList<Day>> items;
    private LayoutInflater inflater;

    public ViewPageAdapter (Activity activity, ArrayList<ArrayList<Day>> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.page_layout, container, false);

        ListView list = (ListView) view.findViewById(R.id.scroll);
        CalendarAdapter adapter = new CalendarAdapter(activity, items.get(position));
        list.setAdapter(adapter);
        TextView text = (TextView) view.findViewById(R.id.month);
        if (items.get(position).size() > 0) {
            Day d = items.get(position).get(0);
            text.setText(d.getMonth(true).toUpperCase() + ", " + d.getYear());
        }

        ((ViewPager) container).addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);
    }
}
