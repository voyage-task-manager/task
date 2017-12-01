package components;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zerodois on 30/11/17.
 */

public class Tooltip {
    Toast t;
    public Tooltip (Context context, String message) {
        this(context, message, false);
    }
    public Tooltip (Context context, String message, boolean fast) {
        t = Toast.makeText(context, message, fast ? Toast.LENGTH_SHORT: Toast.LENGTH_LONG);
    }
    public void show () {
        t.show();
    }
}
