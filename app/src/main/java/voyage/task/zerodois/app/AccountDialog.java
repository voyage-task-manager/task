package voyage.task.zerodois.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import adapters.CalendarAccountAdapter;
import models.CalendarProvider;

public class AccountDialog extends DialogFragment {

    private List<CalendarProvider> calendars;
    private ListView list_email;
    private Activity activity;

    public interface Listener {
        public void setCalendar (CalendarProvider calendar);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            calendars = bundle.getParcelableArrayList("calendars");
            CalendarAccountAdapter adapter = new CalendarAccountAdapter(activity, calendars, onClick());
            list_email.setAdapter(adapter);
        }
    }

    public CalendarAccountAdapter.Listener onClick () {
        final AccountDialog vm = this;
        return new CalendarAccountAdapter.Listener() {
            @Override
            public void onClick(CalendarProvider calendarProvider) {
                ((Listener) activity).setCalendar(calendarProvider);
                vm.dismiss();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_dialog, container);
        list_email = view.findViewById(R.id.list_email);
        getDialog().setTitle("Selecionar calendário");
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }
}
