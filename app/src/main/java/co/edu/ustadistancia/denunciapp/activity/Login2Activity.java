package co.edu.ustadistancia.denunciapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import co.edu.ustadistancia.denunciapp.R;
import co.edu.ustadistancia.denunciapp.db.AppDatabase;
import co.edu.ustadistancia.denunciapp.db.DatabaseInitializer;
import co.edu.ustadistancia.denunciapp.db.DenunciaState;

public class Login2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setSubtitle("Iniciar Sesión");

    }

    /* Entrar en modo ciudadano o modo autoridad ambiental */
    public void login3(View view) {
        if (DenunciaState.getDenunciaID()!=-1) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, AuthorityViewReportActivity.class);
            startActivity(intent);
        }
    }

    /* Regresar a la pantalla inicial */
    public void login1(View view) {
        Intent intent = new Intent(this, Login1Activity.class);
        startActivity(intent);
    }

}
