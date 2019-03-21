package co.edu.ustadistancia.denunciapp.db;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


public class DatabaseInitializer {
    private static final String TAG = DatabaseInitializer.class.getName();

    private static AppCompatActivity activity;

    public static void populateAsync(@NonNull final AppDatabase db, AppCompatActivity act) {
        activity = act;
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    public static void populateSync(@NonNull final AppDatabase db) {
        populateWithData(db);
    }

    private static void populateWithData(AppDatabase db) {
        populateCategories(db);
        populateDelitos(db);

    }

    private static void populateCategories(AppDatabase db) {

        db.categoriaDelitoDao().deleteAll();
        AssetManager assetManager = activity.getAssets();
        // To load text file
        InputStream is;
        try {
            is = assetManager.open("categorias.data");
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String line;
            while ((line = br.readLine()) != null) {
                Log.i("XXXX",line);
                String[] elems = line.split("\\|");
                int id = Integer.parseInt(elems[0]);
                String nombre = elems[1];
                CategoriaDelito categoriaDelito = new CategoriaDelito(id, nombre);
                db.categoriaDelitoDao().insertAll(categoriaDelito);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        List<CategoriaDelito> categoriaList = db.categoriaDelitoDao().getAll();
        Log.d(DatabaseInitializer.TAG, "Rows Count: " + categoriaList.size());
    }

    private static void populateDelitos(AppDatabase db) {

        db.delitoDao().deleteAll();
        AssetManager assetManager = activity.getAssets();
        // To load text file
        InputStream is;
        try {
            is = assetManager.open("delitos.data");
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String line;
            while ((line = br.readLine()) != null) {
                Log.i("XXXX",line);
                String[] elems = line.split("\\|");
                int id = Integer.parseInt(elems[0]);
                int categoriaID = Integer.parseInt(elems[1]);
                String legislacion = elems[2];
                String descripcion = elems[3];
                Delito delito = new Delito(id, categoriaID, legislacion, descripcion);
                db.delitoDao().insertAll(delito);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        List<Delito> delitoList = db.delitoDao().getAll();
        Log.d(DatabaseInitializer.TAG, "Rows Count: " + delitoList.size());
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;

        PopulateDbAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithData(mDb);
            return null;
        }

    }
}
