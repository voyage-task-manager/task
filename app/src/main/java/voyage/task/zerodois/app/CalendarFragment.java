package voyage.task.zerodois.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import adapters.ViewPageAdapter;
import models.Day;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class CalendarFragment extends Fragment {

    private Activity activity;
    private ViewPager viewPager;
    private int month;
    private int year;
    private int today;
    private int page = 1;
    private List<String> headers = new ArrayList<>();
    private ArrayList<ArrayList<Day>> arr;
    private ViewPageAdapter pageAdapter;

    public void atualize() {
        month = -1;
        init();
    }

    interface Listener {
        public ArrayList<Day> load(int month, int year);
        public void callEventRecord();
        public void reload ();
        public ArrayList<ArrayList<Day>> init ();
    }

    public void init () {
        if (month == Calendar.getInstance().get(Calendar.MONTH)) {
            scrollToDay();
            return;
        }
        arr.clear();
        headers.clear();
        ArrayList<ArrayList<Day>> init = ((Listener) activity).init();

        if (init == null) {
            pageAdapter.notifyDataSetChanged();
            return;
        }

        Calendar c = Calendar.getInstance();
        month = c.get(Calendar.MONTH);
        int m = month - 1, y = c.get(Calendar.YEAR);
        for(ArrayList<Day> i : init) {
            arr.add(i);
            headers.add(makeHeader(m, y));
            Log.d("INFO::", "CA: " + makeHeader(m, y));
            m++;
            if (m > 11) {
                m = 0;
                y++;
            }
        }
        pageAdapter.notifyDataSetChanged();
    }

    private String makeHeader (int month, int year) {
        return Day.months[ month ].toUpperCase() + ", " + year;
    }

    private void scrollToDay() {
        final ListView list = (ListView) viewPager.findViewById(R.id.scroll);
        ArrayList<Day> items = arr.get(1);
        int index = 0;
        int diff = 32;
        for (int i = 0; i < items.size(); i++) {
            Day day = items.get(i);
            if ( Math.abs(day.getNumber() - today) < diff ) {
                diff = Math.abs(day.getNumber() - today);
                index = i;
            }
        }
        list.smoothScrollToPositionFromTop(index, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            arr = new ArrayList<>();
            pageAdapter = new ViewPageAdapter(activity, arr, headers);
            init();
            today = bundle.getInt("today");
            month = bundle.getInt("month");
            year = bundle.getInt("year");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        viewPager.setAdapter(pageAdapter);
        view.findViewById(R.id.add_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Listener) activity).callEventRecord();
            }
        });
        viewPager.setCurrentItem(1);
        viewPager.setOffscreenPageLimit(3);
        listener(viewPager);
        return view;
    }

    public void listener (final ViewPager pager) {
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            boolean block = false;
            @Override
            public void onPageSelected(int position) {
                if (block) {
                    block = false;
                    return;
                }
                int var = position - page;
                page = position;
                month += var;
                if (var > 0) {
                    int a = (month + 1)%12;
                    int y = (month + 1 > 11) ? year + 1 : year;
                    month = month % 12;
                    if (month == 0) year++;
                    arr.add( ((Listener) activity).load( a, y ));
                    headers.add(makeHeader(a, y));
                    arr.remove(0);
                    headers.remove(0);
                }
                else if (position == 0) {
                    if (month < 0) month = 11;
                    if (month == 11) year--;
                    int a = (month-1) < 0 ? 11 : (month-1);
                    int y = a == 11 ? year-1 : year;
                    arr.add( 0, ((Listener) activity).load( a, y ));
                    headers.add(0, makeHeader(a, y));
                }
                if (var < 0) {
                    arr.remove(arr.size() - 1);
                    headers.remove(headers.size() - 1);
                }
                pageAdapter.notifyDataSetChanged();
                page = 1;
                block = true;
                pager.setCurrentItem(page);
            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }
}
