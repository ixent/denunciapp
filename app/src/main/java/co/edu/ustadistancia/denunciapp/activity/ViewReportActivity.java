package co.edu.ustadistancia.denunciapp.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.edu.ustadistancia.denunciapp.R;
import co.edu.ustadistancia.denunciapp.db.AppDatabase;
import co.edu.ustadistancia.denunciapp.db.Denuncia;

public class ViewReportActivity extends AppCompatActivity {

    //Inspired by tutorial: http://www.vogella.com/tutorials/AndroidListView/article.html

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        //Add the toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setSubtitle("Ver Informe");

        AppDatabase db = AppDatabase.getAppDatabase(this);
        final List<Denuncia> denunciaList = db.denunciaDao().getAll();

        ArrayList<String> values = new ArrayList<String>();
        for (Denuncia denuncia : denunciaList) {
            String s = "Denuncia: "+denuncia.getId()+"\n"+
                    "Tipo delito: "+denuncia.getDelitoID()+"\n"+
                    "Descripcion: "+denuncia.getDescripcion();
            values.add(s);
        }

        final ListView listview = (ListView) findViewById(R.id.listview);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.rowlayout, R.id.label, values);
        listview.setAdapter(adapter);

/*        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                denunciaList.remove(item);
                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });
            }

        });*/
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
