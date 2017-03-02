package ufba.meuhorario;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import ufba.meuhorario.adapter.ListDisciplinesAdapter;
import ufba.meuhorario.dao.DisciplinesDAO;
import ufba.meuhorario.model.Course;
import ufba.meuhorario.model.Discipline;

/**
 * Created by Diego Novaes on 28/02/2017.
 */
public class DisciplinesActivity extends AppCompatActivity {
    private String DisciplinesJson = "https://gist.githubusercontent.com/dnovaes/419fc613aee50ae971cbfed466c8e512/raw/8352748e5d26e5f38182cadf115b66a6275ef686/meuhorario_disciplines.json";
    public String jsonDisciplinesArrayName = "disciplines";
    private String DisciplinesCoursesJson = "https://gist.githubusercontent.com/dnovaes/fdea84cf2485a9350addd0d42f87c5c0/raw/282dfae3645d690af969d8b7bc49f9b83479b337/meuhorario_course_disciplines.json";
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadList();
    }

    private void loadList() {
        DisciplinesDAO dao = new DisciplinesDAO(this);

        //Select all the areas on the SQLite and form a List<Area>
        List<Discipline> disciplines = dao.getListDisciplines(course_id);

        if(disciplines.isEmpty()){
            // Creating a anonymous JsonParser instance
            // download json and insert into SQLite
            //new JSONParser(this, DisciplinesJson, "disciplines").execute();
            //new JSONParser(this, DisciplinesCoursesJson, "course_disciplines").execute();
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
        dao.close();

        ListDisciplinesAdapter adapter = new ListDisciplinesAdapter(disciplines, this);
        disciplinesList.setAdapter(adapter);
    }
}
