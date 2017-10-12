package com.example.felipe.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import models.Task;
import models.Work;

public class EventActivity extends AppCompatActivity {

    private Task task;
    private Work work;
    private TextView event_name_show;
    private TextView event_description_show;
    private TextView event_date_show;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setSystemBarLight(this, ContextCompat.getColor(this, R.color.colorPrimaryDarker));
        setContentView(R.layout.activity_event);

        task = (Task) getIntent().getParcelableExtra("TASK");
        work = Work.findByTask(this, task.getID());
        Log.d("INFO::", "SIZE HOUR " + (work != null ? "" + work.getPayload(): "Sem work"));
        event_name_show = (TextView) findViewById(R.id.event_name_show);
        event_date_show = (TextView) findViewById(R.id.event_date_show);
        event_description_show = (TextView) findViewById(R.id.event_description_show);
        event_name_show.setText(task.getTitle());
        event_date_show.setText( String.format(Locale.getDefault(), "%tA, %td de %tB de %tY", task.getDate(), task.getDate(), task.getDate(), task.getDate() ) );
        if (task.getDescription() != null && !task.getDescription().equals(""))
            event_description_show.setText(task.getDescription());
        else
            findViewById(R.id.description_view).setVisibility(View.GONE);
    }

    public void close (View view) {
        finish();
    }

    public void delete (View view) {
        final Activity vm = this;
        new AlertDialog.Builder(this)
            .setIcon(R.drawable.ic_delete_24px_black)
            .setTitle("Excluir evento")
            .setMessage("Tem certeza que deseja excluir este evento??")
            .setPositiveButton("Sim, tenho certeza", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    task.drop(vm);
                    TabActivity.prototype.reload();
                    finish();
                }
            })
            .setNegativeButton("Não", null)
            .show();
    }
}
