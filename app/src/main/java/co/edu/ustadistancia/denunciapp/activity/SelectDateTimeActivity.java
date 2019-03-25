package co.edu.ustadistancia.denunciapp.activity;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import co.edu.ustadistancia.denunciapp.db.DenunciaState;
import co.edu.ustadistancia.denunciapp.util.DatePickerFragment;
import co.edu.ustadistancia.denunciapp.R;
import co.edu.ustadistancia.denunciapp.util.TimePickerFragment;

public class SelectDateTimeActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date_time);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setSubtitle("Seleccionar fecha y hora");

        Time time;
        Date date;
        final Calendar c = Calendar.getInstance();

        if (DenunciaState.getHora()==null) {
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            time = new Time(hour, minute, 0);
        } else {
            time = DenunciaState.getHora();
        }

        if (DenunciaState.getFecha()==null) {
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            date = new Date((year-1900), month, day);
        } else {
            date = DenunciaState.getFecha();
        }

        TextView timeView = (TextView)findViewById(R.id.editText2);
        TextView dateView = (TextView)findViewById(R.id.editText3);
        updateDate(date, dateView);
        updateTime(time, timeView);
        DenunciaState.setFecha(date);
        DenunciaState.setHora(time);
    }

    public static void updateDate(Date date, TextView dateView) {
        DateFormat df = DateFormat.getDateInstance(); //new SimpleDateFormat("dd/MM/YYYY");
        dateView.setText("Fecha: "+df.format(date));
    }

    public static void updateTime(Time time, TextView timeView) {
        //DateFormat df = new SimpleDateFormat("hh:mm", Locale.UK);
        DateFormat df = DateFormat.getTimeInstance();
        timeView.setText("Hora: "+df.format(time));
    }


    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void addEvidence(View view) {
        Intent intent = new Intent(this, AddEvidenceActivity.class);
        startActivity(intent);
    }

    public void selectLocation(View view) {
        Intent intent = new Intent(this, FindLocationActivity.class);
        startActivity(intent);
    }

}
