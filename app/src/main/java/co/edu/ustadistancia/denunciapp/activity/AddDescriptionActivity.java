package co.edu.ustadistancia.denunciapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import co.edu.ustadistancia.denunciapp.R;
import co.edu.ustadistancia.denunciapp.db.AppDatabase;
import co.edu.ustadistancia.denunciapp.db.Denuncia;
import co.edu.ustadistancia.denunciapp.db.DenunciaState;

public class AddDescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_description);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setSubtitle("Agregar Descripción del Delito");

        if (DenunciaState.getDescription()!=null) {
            EditText descriptionEditor = (EditText)findViewById(R.id.editText);
            descriptionEditor.setText(DenunciaState.getDescription());
        }
    }

    public void confirmSend(View view) {
        EditText descriptionEditor = (EditText)findViewById(R.id.editText);
        DenunciaState.setDescription(descriptionEditor.getText().toString());

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        final View v = view;
        builder.setTitle("Enviar Denuncia")
                .setMessage("¿Está seguro de que desea enviar la denuncia?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        complaintSent(v);
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

    public void complaintSent(View view) {
        updateDatabase();

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        final View v = view;
        builder.setTitle("Denuncia Enviada")
                .setMessage("La denuncia ha sido enviada exitosamente.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(AddDescriptionActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void updateDatabase() {
        String photoURIStr = "";
        String videoURIStr = "";
        if (DenunciaState.getPhotoURI()!=null) {
            photoURIStr = DenunciaState.getPhotoURI().toString();
        }
        if (DenunciaState.getVideoURI()!=null) {
            videoURIStr = DenunciaState.getVideoURI().toString();
        }

        Denuncia denuncia = new Denuncia(DenunciaState.getDenunciaID(),
                1,
                1,
                DenunciaState.getDelitoID(),
                DenunciaState.getUbicacion().latitude,
                DenunciaState.getUbicacion().longitude,
                photoURIStr,
                videoURIStr,
                DenunciaState.getDescription());
        AppDatabase db = AppDatabase.getAppDatabase(this);
        db.denunciaDao().insertAll(denuncia);
    }
}
