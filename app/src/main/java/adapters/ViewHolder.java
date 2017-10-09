package adapters;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.felipe.app.R;

/**
 * Created by felipe on 13/09/17.
 */

public class ViewHolder {

    final ListView layout;
    final TextView text;

    public ViewHolder(View view, Activity act) {
        layout = (ListView) view.findViewById(R.id.events);
        text = (TextView) view.findViewById(R.id.date);
    }
}
