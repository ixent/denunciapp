package co.edu.ustadistancia.denunciapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.edu.ustadistancia.denunciapp.R;
import co.edu.ustadistancia.denunciapp.db.AppDatabase;
import co.edu.ustadistancia.denunciapp.db.CategoriaDelito;
import co.edu.ustadistancia.denunciapp.db.Delito;
import co.edu.ustadistancia.denunciapp.db.DenunciaState;

public class SelectCategoryActivity extends AppCompatActivity {


    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;

    private static final String NAME = "Name";
    private static final String DETAILS = "details";
    private static final String ID = "id";

    private int groupSelected = -1;
    private int childSelected = -1;
    View childSelectedView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        //Add the toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setSubtitle("Seleccionar Categoria del Delito");

        //get the listview
        expListView = (ExpandableListView)findViewById(R.id.lvExp);

        List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
        List<List<Map<String, String>>> childData
                = new ArrayList<List<Map<String, String>>>();
        loadDataFromDB(groupData, childData);
        setUpAdapter(groupData, childData);

        // perform set on child click listener event
        addMenuListeners();

        //if revisiting this activity
        if (DenunciaState.getDelitoID()!=-1) {
            int groupPos = getGroupPos(DenunciaState.getDelitoID());
            Log.i("SelectCategoryActivity","groupPos"+groupPos);
            expListView.expandGroup(groupPos);
            int childPos = getChildPos(DenunciaState.getDelitoID());
            Log.i("SelectCategoryActivity","childPos"+childPos);
            expListView.smoothScrollToPositionFromTop(groupPos, childPos);
            //TODO: still unabe to recolor child
            //expListView.getChildAt(childPos).setBackgroundColor(Color.rgb(216,191,216));
            //listAdapter.getChildId(groupPos, childPos);
            //View v = (View)listAdapter.getChild(groupPos, childPos);
            //v.setBackgroundColor(Color.rgb(216,191,216));
        }
    }

    private void setUpAdapter(List<Map<String, String>> groupData, List<List<Map<String, String>>> childData) {
        // Set up our adapter.  Borrowed from http://abhiandroid.com/ui/simpleexpandablelistadapter-example-android-studio.html
        listAdapter = new SimpleExpandableListAdapter(
                this,
                groupData,
                android.R.layout.simple_expandable_list_item_1,
                //R.layout.group_items,
                new String[] { NAME, DETAILS },
                new int[] { android.R.id.text1, android.R.id.text2 },
                childData,
                //android.R.layout.simple_expandable_list_item_2,
                R.layout.child_items,
                new String[] { NAME, DETAILS, ID },
                new int[] { android.R.id.text1, android.R.id.text2 }
        );
/*        listAdapter = new SimpleExpandableListAdapter(
                this,
                groupData,
                android.R.layout.simple_expandable_list_item_1,
                //R.layout.group_items,
                new String[] { NAME, DETAILS },
                new int[] { android.R.id.text1, android.R.id.text2 },
                childData,
                android.R.layout.simple_expandable_list_item_2,
                //R.layout.child_items,
                new String[] { NAME, DETAILS, ID },
                new int[] { android.R.id.text1, android.R.id.text2 }
        )*/
        expListView.setAdapter(listAdapter);
    }

    private void addMenuListeners() {
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.i("SelectCategoryActivity", "GroupPos="+groupPosition+ " childPosition="+childPosition+ " id="+id);

                if (groupSelected==-1 && childSelected == -1) {
                    v.setBackgroundColor(Color.rgb(216,191,216));
                    groupSelected = groupPosition;
                    childSelected = childPosition;
                    childSelectedView = v;
                    int delitoID = getDelitoID(groupPosition, childPosition);
                    DenunciaState.setDelitoID(delitoID);
                } else if (groupSelected == groupPosition && childSelected == childPosition) {
                    v.setBackgroundColor(Color.rgb(255,255,255));
                    groupSelected = -1;
                    childSelected = -1;
                    childSelectedView = null;
                    DenunciaState.setDelitoID(-1);
                } else {
                    childSelectedView.setBackgroundColor(Color.rgb(255,255,255));
                    v.setBackgroundColor(Color.rgb(216,191,216));
                    groupSelected = groupPosition;
                    childSelected = childPosition;
                    childSelectedView = v;
                    int delitoID = getDelitoID(groupPosition, childPosition);
                    DenunciaState.setDelitoID(delitoID);
                }
                return false;
            }
        });
    }

    //ugly hack!
    private int getDelitoID(int groupPos, int childPos) {
        AppDatabase db = AppDatabase.getAppDatabase(this);
        int offset = db.delitoDao().getDelitoOffset(groupPos+1);
        int delitoID = offset+childPos + 1;
        Log.i("SelectCategoryActivity","DelitoID = "+delitoID);
        return delitoID;
    }

    private int getGroupPos(int delitoID) {
        Log.i("SelectCategoryActivity","Getting groupID for DelitoID = "+delitoID);
        AppDatabase db = AppDatabase.getAppDatabase(this);
        int groupPos = db.delitoDao().getCategoria(delitoID)-1;
        return groupPos;
    }

    private int getChildPos(int delitoID) {
        AppDatabase db = AppDatabase.getAppDatabase(this);
        int categoria = db.delitoDao().getCategoria(delitoID);
        int offset = db.delitoDao().getDelitoOffset(categoria);
        return delitoID-offset-1;
    }


    private void loadDataFromDB(List<Map<String, String>> groupData, List<List<Map<String, String>>> childData) {
        AppDatabase db = AppDatabase.getAppDatabase(this);
        List<CategoriaDelito> categorias = db.categoriaDelitoDao().getAll();
        List<Delito> delitos = db.delitoDao().getAll();

        for (CategoriaDelito categoria : categorias) {
            Map<String, String> groupMap = new HashMap<String, String>();
            groupData.add(groupMap);
            groupMap.put(NAME, categoria.getNombre());

            List<Map<String, String>> groupChildren = new ArrayList<Map<String, String>>();
            childData.add(groupChildren);

            for (Delito delito : delitos) {
                if (delito.getCategoriaID()==categoria.getId()) {
                    Map<String, String> gcmap = new HashMap<String, String>();
                    groupChildren.add(gcmap);
                    gcmap.put(NAME, delito.getDescripcion());
                    gcmap.put(DETAILS, delito.getLegislacion());
                    gcmap.put(ID, new Integer(delito.getId()).toString());
                }
            }
        }
    }

    public void findLocation(View view) {
        Intent intent = new Intent(this, FindLocationActivity.class);
        startActivity(intent);
    }

    public void mainMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
