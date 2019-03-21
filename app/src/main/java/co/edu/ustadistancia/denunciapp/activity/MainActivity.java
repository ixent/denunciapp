package co.edu.ustadistancia.denunciapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import co.edu.ustadistancia.denunciapp.R;
import co.edu.ustadistancia.denunciapp.db.AppDatabase;
import co.edu.ustadistancia.denunciapp.db.DatabaseInitializer;
import co.edu.ustadistancia.denunciapp.db.DenunciaState;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setSubtitle("Menú Principal");

        DatabaseInitializer.populateAsync(AppDatabase.getAppDatabase(this), this);

        DenunciaState.initializeDenunciaState(this);

/*        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();*/
    }

    /* Este metodo es llamado cuando el usario tap el boton para crear nueva denuncia */
    public void createComplaint(View view) {
        Intent intent = new Intent(this, SelectCategoryActivity.class);
        startActivity(intent);
    }

    /* Este metodo es llamado cuando el usario tap el boton para ver informe */
    public void viewReport(View view) {
        Intent intent = new Intent(this, ViewReportActivity.class);
        startActivity(intent);
    }
}
