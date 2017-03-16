package ufba.meuhorario;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import ufba.meuhorario.adapter.ListDisciplinesAdapter;
import ufba.meuhorario.dao.DisciplineDAO;
import ufba.meuhorario.model.Course;
import ufba.meuhorario.model.Discipline;

/**
 * Created by Diego Novaes on 28/02/2017.
 */
public class DisciplinesActivity extends AppCompatActivity {
    private String DisciplinesJson = "https://gist.githubusercontent.com/dnovaes/419fc613aee50ae971cbfed466c8e512/raw/8352748e5d26e5f38182cadf115b66a6275ef686/meuhorario_disciplines.json";
    public String jsonDisciplinesArrayName = "disciplines";
    private ListView disciplinesList;
    private Long course_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        Intent intent = getIntent();
        Course course = (Course) intent.getSerializableExtra("course");

        course_id = course.getId();

        disciplinesList = (ListView)findViewById(R.id.list_courses);

        TextView titleView = (TextView) findViewById(R.id.activity_title);
        titleView.setText(course.getName());

        getJsonData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadList();
    }

    private void loadList() {

        DisciplineDAO dao = new DisciplineDAO(this);

        //Select all the areas on the SQLite and form a List<Area>
        List<Discipline> disciplines = dao.getListDisciplines(course_id);
        dao.close();

        ListDisciplinesAdapter adapter = new ListDisciplinesAdapter(disciplines, this);
        disciplinesList.setAdapter(adapter);
    }

    public void getJsonData() {
        DisciplineDAO dao = new DisciplineDAO(this);

        //Select all the areas on the SQLite and form a List<Area>
        List<Discipline> disciplines = dao.getListDisciplines(course_id);

        if(disciplines.isEmpty()){
            // Create a anonymous JsonParser instance
            // download JSON and insert into SQLite

            //this line below download and insert both: disciplines and course_disciplines tables.
            new JSONParser(this, DisciplinesJson, "disciplines", new Long(course_id)).execute();

            // Deprecated!
            //option to load Json Disciplines from file
            //loadJsonFromFile();
        }
        dao.close();
    }

    private void loadJsonFromLocalFile(){
        DisciplineDAO dao = new DisciplineDAO(this);

        InputStream is = this.getResources().openRawResource(R.raw.meuhorario_disciplines);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int ctr;
        try {
            ctr = is.read();
            while (ctr != -1) {
                byteArrayOutputStream.write(ctr);
                ctr = is.read();
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObj = new JSONObject(byteArrayOutputStream.toString());
            JSONArray jsonArray = jsonObj.getJSONArray(jsonDisciplinesArrayName);
            Log.e("countDisciplines", String.valueOf(dao.getCountDisciplines()));
            if (!(dao.getCountDisciplines() > 0)){
                JSONParser.StoreinDatabase(jsonArray, dao, jsonDisciplinesArrayName);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
