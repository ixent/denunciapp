package co.edu.ustadistancia.denunciapp.util;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Date;

import co.edu.ustadistancia.denunciapp.R;
import co.edu.ustadistancia.denunciapp.activity.SelectDateTimeActivity;
import co.edu.ustadistancia.denunciapp.db.DenunciaState;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Time time = new Time(hourOfDay, minute, 0);
        DenunciaState.setHora(time);
        TextView timeView = (TextView)getActivity().findViewById(R.id.editText2);
        SelectDateTimeActivity.updateTime(time, timeView);
    }
}