package appkill.explodingstar.com.appkill;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by myxom on 16/02/14.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    private static final String ARG_TIME = "time";

    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(int time){
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, "time");

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //use the current time as the default values
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_time, null);

        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_time_picker);

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    //onTimeSet() callback method
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        //do something with the user chosen time
        //get reference of host activity (xml layout) textview widget
        TextView tv = (TextView) getActivity().findViewById(R.id.timerValue);
        tv.setText("Your chosen time is...\n\n");
        //Display the user changed time on TextView
        tv.setText(tv.getText()+ "Hour: " +String.valueOf(hourOfDay)
        +"\nMinute: " + String.valueOf(minute) + "\n");
    }
}
