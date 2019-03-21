package co.edu.ustadistancia.denunciapp.util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Date;

import co.edu.ustadistancia.denunciapp.R;
import co.edu.ustadistancia.denunciapp.activity.SelectDateTimeActivity;
import co.edu.ustadistancia.denunciapp.db.DenunciaState;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        Date date = new Date((year-1900), month, day);
        DenunciaState.setFecha(date);
        TextView dateView = (TextView)getActivity().findViewById(R.id.editText3);
        SelectDateTimeActivity.updateDate(date, dateView);
    }
}