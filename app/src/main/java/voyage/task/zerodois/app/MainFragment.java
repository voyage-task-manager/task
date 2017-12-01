package voyage.task.zerodois.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import models.CalendarProvider;
import models.Graph;
import models.Setting;
import models.Task;
import models.Work;

public class MainFragment extends Fragment {

    private LinearLayout linear;
    private ArrayList<Task> tasks;
    private Activity activity;
    private ImageView rotine_icon;
    private TextView rotine_name, rotine_hour;
    private Button to_late;
    private Task now;
    private Setting mySetting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        linear = activity.findViewById(R.id.linear);
        rotine_icon = (ImageView) activity.findViewById(R.id.rotine_icon);
        rotine_hour = (TextView) activity.findViewById(R.id.rotine_hour);
        rotine_name = (TextView) activity.findViewById(R.id.rotine_name);
        to_late = (Button) activity.findViewById(R.id.to_late);
        now = null;
        mySetting = Setting.init(activity).get(1);
        Task next = null;
        long min = 3600000 * 24;

        Bundle bundle = getArguments();
        if (bundle != null) {
            tasks = bundle.getParcelableArrayList("tasks");

            Calendar instance = Calendar.getInstance();
            long time = instance.getTimeInMillis();
            for (Task t : tasks) {
                if (time >= t.getDate() && time <= t.getEnd() && !t.isAllDay()) now = t;
                else if (time > t.getDate()) continue;

                if (t.getDate() - time < min) {
                    next = t;
                    min = t.getDate() - time;
                }
            }

            if (now == null) {
                to_late.setVisibility(View.GONE);
                rotine_name.setText("Este horário é livre para fazer o que quiser");
                if (next != null)
                    rotine_hour.setText( String.format(Locale.getDefault(), "Próxima tarefa às %tR", next.getDate()) );
                else {
                    String periodo = "o dia";
                    if (instance.get(Calendar.HOUR_OF_DAY) > 18)
                        periodo = "a noite";
                    else if (instance.get(Calendar.HOUR_OF_DAY) > 12)
                        periodo = "a tarde";
                    rotine_hour.setText( "Você tem " + periodo + " livre, aproveite!");
                }
                rotine_icon.setImageResource(R.drawable.ic_very_happy_black_24px);
                //rotine_icon.setImageDrawable( ContextCompat.getDrawable(getActivity(), R.drawable.ic_very_happy_black_24px) );
            } else {
                to_late.setVisibility(View.VISIBLE);
                rotine_name.setText("Agora é hora de trabalhar na tarefa " + now.getTitle());
                rotine_hour.setText( String.format(Locale.getDefault(), "Das %tR até %tR", now.getDate(), now.getEnd()) );
                rotine_icon.setImageResource(R.drawable.ic_work_black_24px);
                //rotine_icon.setImageDrawable( ContextCompat.getDrawable(getActivity(), R.drawable.ic_work_black_24px) );
            }

            /*PersonalAdapter adapter = new PersonalAdapter(getActivity(), tasks);
            for (int i=0; i<tasks.size(); i++)
                if (linear != null)
                    linear.addView(adapter.getView(i, null, linear));
                else
                    Log.d("INFO::", "NOOOOO!");*/
        }

        to_late.setOnClickListener(onClick());
    }

    private View.OnClickListener onClick () {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button b = ((Button) view);
                Work work = Work.findByTask(activity, now.getID());
                if (work == null || work.getReference() == -1)
                    return;
                List<Task> list = CalendarProvider.readCalendar(activity.getContentResolver()); //CalendarProvider.readCalendar(null, null, activity.getContentResolver(), work.getReference());
                Task origin = null;
                for (Task l : list)
                    if (l.getID() == work.getReference()) {
                        origin = l;
                        break;
                    }
                if (origin == null)
                    return;
                Calendar o = Calendar.getInstance();
                Calendar i = Calendar.getInstance();
                i.set(Calendar.HOUR_OF_DAY, 1);
                o.setTimeInMillis(origin.getDate());

                Graph graph = new Graph(i, o, mySetting, activity);
                int t = (int) Math.ceil( (now.getEnd() - now.getDate())/3600000 );
                List<Task> prev = graph.organize(origin, t, null);

                if (prev.size() == 0) {
                    Toast.makeText(activity, "Não há tempo na sua agenda para executar esta tarefa :(", Toast.LENGTH_LONG).show();
                    return;
                }

                for (Task n: prev) {
                    n.record(activity);
                    Work w = new Work(activity);
                    w.setPayloadType(work.getPayloadType());
                    w.setPayload(w.getPayload());
                    w.setReference(work.getReference());
                    w.setTask(n.getID());
                    w.save();
                }

                Task.delete(activity, now.getID());
                work.delete();
                TabActivity.prototype.reload();

                b.setEnabled(false);
                b.setClickable(false);
                b.setText("Tarefa adiada, procrastine a vontade :)");
                b.setTextColor(ContextCompat.getColor(activity, R.color.disabled));
            }
        };
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity)
            activity = (Activity) context;
    }
}
