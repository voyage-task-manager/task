package adapters;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import voyage.task.zerodois.app.R;

import java.util.Calendar;
import java.util.List;

import models.Task;

/**
 * Created by felipe on 13/10/17.
 */

public class RotineAdapter extends PagerAdapter {

    private List<Task> tasks;
    private Activity activity;
    private TextView rotine_name;
    private TextView rotine_hour;
    private ImageView rotine_icon;

    public RotineAdapter(List<Task> tasks, Activity activity) {
        this.tasks = tasks;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return tasks.size();
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

        Task task = tasks.get(position);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.rotine_item, container, false);
        rotine_name = (TextView) view.findViewById(R.id.rotine_name);
        rotine_hour = (TextView) view.findViewById(R.id.rotine_hour);
        rotine_icon = (ImageView) view.findViewById(R.id.rotine_icon);
        rotine_name.setText(task.getTitle());

        Calendar init = Calendar.getInstance();
        init.setTimeInMillis(task.getDate());
        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(task.getEnd());

        Calendar instance = Calendar.getInstance();

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);
    }
}
