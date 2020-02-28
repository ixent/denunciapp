package co.edu.ustadistancia.denunciapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

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
        EditText editPWText = (EditText)findViewById(R.id.editPWText);
        //TODO: check credentials with MongoDB database
        if (!editPWText.getText().toString().equals("12345") ||
                (!DenunciaState.getUsuario().equals("usuario1") &&
                        !DenunciaState.getUsuario().equals("usuario2") &&
                        !DenunciaState.getUsuario().equals("usuario3") &&
                        !DenunciaState.getUsuario().equals("autoridad1"))) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            final View v = view;
            builder.setTitle("Usuario o Contraseña incorrecta")
                    .setMessage("Por favor intente nuevamente.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Login2Activity.this, Login1Activity.class);
                            startActivity(intent);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();

        } else if (DenunciaState.getDenunciaID()!=-1) {
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
