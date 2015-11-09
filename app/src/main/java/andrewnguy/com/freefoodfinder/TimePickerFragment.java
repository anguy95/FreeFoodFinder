package andrewnguy.com.freefoodfinder;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by anguy95 on 11/8/15.
 *
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        String am_pm = "";

        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        datetime.set(Calendar.MINUTE, minute);

        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";

        if (hourOfDay > 12){
            hourOfDay = hourOfDay-12;
        }

        // check for 12AM
        if (hourOfDay == 0 && am_pm.equals("AM"))
            hourOfDay = 12;

        StringBuilder time = new StringBuilder();

        Bundle args = getArguments();


        if ("start".equals(args.getString("startTime"))) {
            TextView timeTextView = (TextView) getActivity().findViewById(R.id.event_date_time_startTime);

            if (minute < 10) {
                time.append(hourOfDay).append(":").append("0").append(minute).append(" ").append(am_pm);
                timeTextView.setText(time);
            } else {
                time.append(hourOfDay).append(":").append(minute).append(" ").append(am_pm);
                timeTextView.setText(time);
            }
        } else {
            TextView timeTextView = (TextView) getActivity().findViewById(R.id.event_date_time_endTime);
            if (minute < 10) {
                time.append(hourOfDay).append(":").append("0").append(minute).append(" ").append(am_pm);
                timeTextView.setText(time);
            } else {
                time.append(hourOfDay).append(":").append(minute).append(" ").append(am_pm);
                timeTextView.setText(time);
            }
        }
    }
}
