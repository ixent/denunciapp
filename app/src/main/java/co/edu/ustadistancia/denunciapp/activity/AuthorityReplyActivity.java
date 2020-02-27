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

public class AuthorityReplyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_reply);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setSubtitle("Responder a Denuncia");

    }

    /* Este metodo es llamado cuando el usario tap el boton para crear nueva denuncia */
    public void confirmSend(View view) {
        EditText descriptionEditor = (EditText)findViewById(R.id.editText);
        //DenunciaState.setDescription(descriptionEditor.getText().toString()); //TODO

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        final View v = view;
        builder.setTitle("Enviar Respuesta")
                .setMessage("¿Está seguro de que desea enviar la respuesta a la denuncia?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        replySent(v);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void replySent(View view) {
        //updateDatabase(); //TODO
        //updateMongoDBDatabase(); //TODO

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        final View v = view;
        builder.setTitle("Respuesta Enviada")
                .setMessage("La respuesta ha sido enviada exitosamente.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(AuthorityReplyActivity.this, AuthorityViewReportActivity.class);
                        startActivity(intent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }


}
