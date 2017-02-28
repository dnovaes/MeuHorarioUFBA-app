package ufba.meuhorario;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import ufba.meuhorario.adapter.ListAreasAdapter;
import ufba.meuhorario.dao.AreaDAO;
import ufba.meuhorario.model.Area;

public class AreasActivity extends AppCompatActivity {

    private String meuHorarioJson = "https://gist.githubusercontent.com/dnovaes/b0f2f75a18831b024ab85a5c25a1b2fe/raw/1abb25421e4d6b033bd2c68f66ba205891491b4a/meuhorario_areas.js";
    private ListView areasList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_areas);

        areasList = (ListView) findViewById(R.id.list_areas);
        /*
        // getting JSON string from URL
        JSONObject json = jParser.getJSONFromUrl(url);

        databaseHelper=new CategoryHelper(AndroidJSONParsingActivity.this);

        try {
            // Getting Array of Contacts
            contacts = json.getJSONArray(TAG_CATEGORY);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        areasList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> list, View view, int position, long id) {
                    Area area = (Area) list.getItemAtPosition(position);
                    //Toast.makeText(AreasActivity.this, "você clicou em "+area.getName(), Toast.LENGTH_SHORT).show();

                    Intent intentCourses = new Intent(AreasActivity.this.getApplicationContext(), CoursesActivity.class);
                    intentCourses.putExtra("area", area);
                    startActivity(intentCourses);
                }
            }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.json_download_option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.jsonDwnld:
                // Creating a anonymous JsonParser instance
                // download json and insert into SQLite
                //TODO: (MAYBE) trade third parameter later so JsonParser can update the whole database and not just 'areas'
                loadList();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadList();
    }

    public void loadList() {
        AreaDAO dao = new AreaDAO(this);

        //Select all the areas on the SQLite and form a List<Area>
        List<Area> areas = dao.getListAreas();

        if(areas.isEmpty()){
            // Creating a anonymous JsonParser instance
            // download json and insert into SQLite
            Toast.makeText(this, "Aguarde. Atualizando dados de 'areas'.", Toast.LENGTH_SHORT).show();
            new JSONParser(this, meuHorarioJson, "areas").execute();
            areas = dao.getListAreas();
        }
        dao.close();

        ListAreasAdapter adapter = new ListAreasAdapter(areas, this);
        areasList.setAdapter(adapter);
    }


}
