package components;

import android.app.Activity;
import android.app.Dialog;
import android.widget.TextView;

import voyage.task.zerodois.app.R;

/**
 * Created by felipe on 09/10/17.
 */

public class Waiting {

    private Dialog dialog;
    private String text;
    private TextView dialog_text;

    public Waiting (Activity act, String text) {
        dialog = new Dialog(act);
        dialog.setContentView(R.layout.wait);
        dialog.setTitle("Waiting...");
        dialog_text = (TextView) dialog.findViewById(R.id.dialog_text);
        this.text = text;
        dialog_text.setText(text);
        dialog.show();
    }

    private void setText (String text) {
        this.text = text;
        dialog_text.setText(text);
    }

    public void open () {
        dialog.show();
    }

    public void close () {
        dialog.dismiss();
    }
}
