package adapters;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.felipe.app.R;

/**
 * Created by felipe on 14/09/17.
 */

public class DifficultyAdapter extends PagerAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private int range = 10;

    public DifficultyAdapter (Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return range;
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
        View view = inflater.inflate(R.layout.difficulty_layout, container, false);

        TextView text = (TextView) view.findViewById(R.id.difficulty);
        text.setText("" + (position + 1));

        ((ViewPager) container).addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);
    }
}

