package andrewnguy.com.freefoodfinder;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by anguy95 on 11/7/15.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Calendar eventDate = Calendar.getInstance();
        eventDate.set(year, month, day);

        int weekDay = eventDate.get(Calendar.DAY_OF_WEEK);

        String dayOfWeek = null;
        String monthOfYear = null;

        switch(month){
            case 0:
                monthOfYear = "Jan";
                break;
            case 1:
                monthOfYear = "Feb";
                break;
            case 2:
                monthOfYear = "Mar";
                break;
            case 3:
                monthOfYear = "Apr";
                break;
            case 4:
                monthOfYear = "May";
                break;
            case 5:
                monthOfYear = "Jun";
                break;
            case 6:
                monthOfYear = "Jul";
                break;
            case 7:
                monthOfYear = "Aug";
                break;
            case 8:
                monthOfYear = "Sep";
                break;
            case 9:
                monthOfYear = "Oct";
                break;
            case 10:
                monthOfYear = "Nov";
                break;
            case 11:
                monthOfYear = "Dec";
                break;
            default:
                break;
        }

        switch (weekDay){
            case 1:
                dayOfWeek = "Sun";
                break;
            case 2:
                dayOfWeek = "Mon";
                break;
            case 3:
                dayOfWeek = "Tue";
                break;
            case 4:
                dayOfWeek = "Wed";
                break;
            case 5:
                dayOfWeek = "Thur";
                break;
            case 6:
                dayOfWeek = "Fri";
                break;
            case 7:
                dayOfWeek = "Sat";
                break;
            default:
                break;
        }

        ((TextView) getActivity().findViewById(R.id.event_date_time_date)).setText(dayOfWeek+ ", " + monthOfYear + " " + day + " " + String.valueOf(year));
    }


}