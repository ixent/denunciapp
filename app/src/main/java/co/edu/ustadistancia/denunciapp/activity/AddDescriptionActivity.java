package co.edu.ustadistancia.denunciapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

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
        myToolbar.setSubtitle("Agregar Descripción del Delito"); /**/

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
        updateMongoDBDatabase();

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

    private void updateMongoDBDatabase() {
        /* iniciar conexión con MongoDB en la nube */
        final StitchAppClient client =
                Stitch.getDefaultAppClient();

        final RemoteMongoClient mongoClient =
                client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

        final RemoteMongoCollection<Document> coll =
                mongoClient.getDatabase("denunciapp").getCollection("denuncias");

        client.getAuth().loginWithCredential(new AnonymousCredential()).continueWithTask(
                new Continuation<StitchUser, Task<RemoteInsertOneResult>>() {

                    @Override
                    public Task<RemoteInsertOneResult> then(@NonNull Task<StitchUser> task) throws Exception {
                        if (!task.isSuccessful()) {
                            Log.e("STITCH", "Login failed!");
                            throw task.getException();
                        }

                        String photoURIStr = "";
                        String videoURIStr = "";
                        if (DenunciaState.getPhotoURI()!=null) {
                            photoURIStr = DenunciaState.getPhotoURI().toString();
                        }
                        if (DenunciaState.getVideoURI()!=null) {
                            videoURIStr = DenunciaState.getVideoURI().toString();
                        }

                        final Document newDoc = new Document(
                                "owner_id",
                                task.getResult().getId()
                        );
                        newDoc.put("dummy", 0);
                        newDoc.put("denuncia_id", DenunciaState.getDenunciaID());
                        newDoc.put("ciudadano_id", DenunciaState.getUsuario());
                        newDoc.put("autoridad_id", "autoridad1");
                        newDoc.put("delito_id", DenunciaState.getDelitoID());
                        newDoc.put("fecha", DenunciaState.getFecha().toString());
                        newDoc.put("hora", DenunciaState.getHora().toString());
                        newDoc.put("latitude",DenunciaState.getUbicacion().latitude);
                        newDoc.put("longitude",DenunciaState.getUbicacion().longitude);
                        newDoc.put("photo",photoURIStr);


                        newDoc.put("video",videoURIStr);
                        newDoc.put("description",DenunciaState.getDescription());

                        return coll.insertOne(newDoc);
                    }
                }
        ).continueWithTask(new Continuation<RemoteInsertOneResult, Task<List<Document>>>() {
            @Override
            public Task<List<Document>> then(@NonNull Task<RemoteInsertOneResult> task) throws Exception {
                if (!task.isSuccessful()) {
                    Log.e("STITCH", "Update failed!");
                    throw task.getException();
                }
                List<Document> docs = new ArrayList<>();
                return coll
                        .find(new Document("owner_id", client.getAuth().getUser().getId()))
                        .limit(100)
                        .into(docs);
            }
        }).addOnCompleteListener(new OnCompleteListener<List<Document>>() {
            @Override
            public void onComplete(@NonNull Task<List<Document>> task) {
                if (task.isSuccessful()) {
                    Log.d("STITCH", "Found docs: " + task.getResult().toString());
                    return;
                }
                Log.e("STITCH", "Error: " + task.getException().toString());
                task.getException().printStackTrace();
            }
        });

    }
}
