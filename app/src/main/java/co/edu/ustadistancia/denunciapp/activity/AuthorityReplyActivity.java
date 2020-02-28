package co.edu.ustadistancia.denunciapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import co.edu.ustadistancia.denunciapp.R;
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
                        updateDenunciaReplyToMongoDBDatabase();
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

    private void updateDenunciaReplyToMongoDBDatabase() {
        /* iniciar conexión con MongoDB en la nube */
        final StitchAppClient client =
                Stitch.getDefaultAppClient();

        final RemoteMongoClient mongoClient =
                client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

        final RemoteMongoCollection<Document> coll =
                mongoClient.getDatabase("denunciapp").getCollection("denuncias");

        final RemoteMongoCollection<Document> dummyColl =
                mongoClient.getDatabase("denunciapp").getCollection("dummy");

        client.getAuth().loginWithCredential(new AnonymousCredential()).continueWithTask(
                new Continuation<StitchUser, Task<RemoteInsertOneResult>>() {

                    @Override
                    public Task<RemoteInsertOneResult> then(@NonNull Task<StitchUser> task) throws Exception {
                        if (!task.isSuccessful()) {
                            Log.e("STITCH", "Login failed!");
                            throw task.getException();
                        }

                        //Taken from https://docs.mongodb.com/stitch/mongodb/actions/collection.updateOne/
                        Document filterDoc = new Document().append("owner_id", task.getResult().getId()).
                                append("_id", DenunciaState.getCurrentResponseDoc().getObjectId("_id"));

                        EditText descriptionEditor = (EditText)findViewById(R.id.editText);
                        Document updateDoc = DenunciaState.getCurrentResponseDoc().append(
                                "respuesta", descriptionEditor.getText().toString()
                        );

                        final Task <RemoteUpdateResult> updateTask =
                                coll.updateOne(filterDoc, updateDoc);
                        updateTask.addOnCompleteListener(new OnCompleteListener <RemoteUpdateResult> () {
                            @Override
                            public void onComplete(@NonNull Task<RemoteUpdateResult> task) {
                                if (task.isSuccessful()) {
                                    long numMatched = task.getResult().getMatchedCount();
                                    long numModified = task.getResult().getModifiedCount();
                                    Log.d("STITCH", String.format("successfully matched %d and modified %d documents",
                                            numMatched, numModified));
                                } else {
                                    Log.e("STITCH", "failed to update document with: ", task.getException());
                                }
                            }
                        });
                        Log.d("STITCH", "updated");

                        final Document newDoc = new Document(
                                "owner_id",
                                task.getResult().getId()
                        );
                        newDoc.put("dummy", 1);
                        newDoc.put("random", Math.random());

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
                        .filter(new Document("dummy",0))
                        //.filter(new Document("autoridad_id", DenunciaState.getUsuario()))
                        .limit(100)
                        .into(docs);
            }
        }).addOnCompleteListener(new OnCompleteListener<List<Document>>() {
            @Override
            public void onComplete(@NonNull Task<List<Document>> task) {
                if (task.isSuccessful()) {
                    Log.e("STITCH", "Found docs: " + task.getResult().toString());

                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(AuthorityReplyActivity.this, android.R.style.Theme_Material_Dialog);
                    } else {
                        builder = new AlertDialog.Builder(AuthorityReplyActivity.this);
                    }
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

                    return;
                }
                Log.e("STITCH", "Error: " + task.getException().toString());
                task.getException().printStackTrace();
            }
        });
    }

}
