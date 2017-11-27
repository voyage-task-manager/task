package voyage.task.zerodois.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AppRater {
    private final static String APP_TITLE = "Voyage";// App Name
    private final static String APP_PNAME = "voyage.task.zerodois";// Package Name

    private final static int DAYS_UNTIL_PROMPT = 3;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 3;//Min number of launches

    public static void app_launched(Activity mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }

        editor.commit();
    }

    public static void showRateDialog(final Activity mContext, final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setTitle("Rate " + APP_TITLE);

        LinearLayout layout = (LinearLayout) mContext.getLayoutInflater().inflate(R.layout.rate_dialog, null);

        Button b1 = layout.findViewById(R.id.rate_now);
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
            dialog.dismiss();
            }
        });

        Button b2 = layout.findViewById(R.id.rate_later);
        b2.setTypeface(null, Typeface.NORMAL);
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        /*Button b3 = layout.findViewById(R.id.no_rate);
        b3.setTypeface(null, Typeface.NORMAL);
        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            if (editor != null) {
                editor.putBoolean("dontshowagain", true);
                editor.commit();
            }
            dialog.dismiss();
            }
        });*/

        dialog.setContentView(layout);
        dialog.show();
    }
}