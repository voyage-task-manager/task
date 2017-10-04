package adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.felipe.app.R;

/**
 * Created by felipe on 13/09/17.
 */

public class ViewHolder {

    final LinearLayout layout;
    final TextView text;

    public ViewHolder(View view, Activity act) {
        layout = (LinearLayout) view.findViewById(R.id.events);
        text = (TextView) view.findViewById(R.id.date);
    }
}
