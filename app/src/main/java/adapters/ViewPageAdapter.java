package adapters;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import voyage.task.zerodois.app.R;

import java.util.ArrayList;
import java.util.List;

import models.Day;

/**
 * Created by felipe on 14/09/17.
 */

public class ViewPageAdapter extends PagerAdapter {

    private Activity activity;
    private ArrayList<ArrayList<Day>> items;
    private List<String> headers;
    private LayoutInflater inflater;

    public ViewPageAdapter (Activity activity, ArrayList<ArrayList<Day>> items, List<String> headers) {
        this.activity = activity;
        this.items = items;
        this.headers = headers;
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
    public Object instantiateItem(ViewGroup container, final int position) {

        inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.page_layout, container, false);

        final ListView list = (ListView) view.findViewById(R.id.scroll);
        CalendarAdapter adapter = new CalendarAdapter(activity, items.get(position));
        list.setAdapter(adapter);
        ArrayList<Day> days = items.get(position);
        TextView text = (TextView) view.findViewById(R.id.month);
        text.setText(headers.get(position));

        FrameLayout empty = (FrameLayout) view.findViewById(R.id.empty);
        if (days.size() > 0) {
            empty.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
        } else {
            list.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);
    }
}
