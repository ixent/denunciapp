package co.edu.ustadistancia.denunciapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.edu.ustadistancia.denunciapp.R;
import co.edu.ustadistancia.denunciapp.db.Denuncia;
import co.edu.ustadistancia.denunciapp.db.DenunciaState;

public class AuthorityViewReportActivity extends AppCompatActivity {

    final ArrayList<Document> denunciaDocs = new ArrayList<>();

    //Inspired by tutorial: http://www.vogella.com/tutorials/AndroidListView/article.html

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_view_report);

        //Add the toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setSubtitle("Responder Denuncia - Autoridad Ambiental");

        //TODO: This is reading from the local database here, should read from the MongoDB database
        /*AppDatabase db = AppDatabase.getAppDatabase(this);
        final List<Denuncia> denunciaList = db.denunciaDao().getAll();

        ArrayList<String> values = new ArrayList<String>();
        for (Denuncia denuncia : denunciaList) {
            String s = "Denuncia: "+denuncia.getId()+"\n"+
                    "Tipo delito: "+denuncia.getDelitoID()+"\n"+
                    "Descripcion: "+denuncia.getDescripcion();
            values.add(s);
        }
        */

        final ListView listview = (ListView) findViewById(R.id.listview);
        getDenunciasFromMongoDBDatabase();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    final int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(200).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                DenunciaState.setCurrentResponseDoc(denunciaDocs.get(position));
                                Intent intent = new Intent(AuthorityViewReportActivity.this, AuthorityReplyActivity.class);
                                startActivity(intent);                            }
                        });
            }

        });
    }

    private void getDenunciasFromMongoDBDatabase() {
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
                    ArrayList<String> values = new ArrayList<String>();
                    for (Document d : task.getResult()) {
                        String s = "Denuncia: "+d.getInteger("denuncia_id")+"\n"+
                                "Ciudadano: "+d.getString("ciudadano_id")+"\n"+
                                "Fecha: "+d.getString("fecha")+"\t"+
                                "Hora: "+d.getString("hora")+"\n"+
                                "Lat: "+d.getDouble("latitude")+"\t"+
                                "Long: "+d.getDouble("longitude")+"\n"+
                                "Tipo delito: "+d.getInteger("delito_id")+"\n"+
                                "Descripcion: "+d.getString("description");
                        values.add(s);
                        AuthorityViewReportActivity.this.denunciaDocs.add(d);
                    }

                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(AuthorityViewReportActivity.this,
                            R.layout.rowlayout, R.id.label, values);
                    final ListView listview = (ListView) findViewById(R.id.listview);
                    listview.setAdapter(adapter);

                    return;
                }
                Log.e("STITCH", "Error: " + task.getException().toString());
                task.getException().printStackTrace();
            }
        });
    }

    private class StableArrayAdapter extends ArrayAdapter<Denuncia> {

        HashMap<Denuncia, Integer> mIdMap = new HashMap<Denuncia, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<Denuncia> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            Denuncia item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
