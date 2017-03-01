package ufba.meuhorario;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import ufba.meuhorario.adapter.ListDisciplinesAdapter;
import ufba.meuhorario.dao.DisciplinesDAO;
import ufba.meuhorario.model.Course;
import ufba.meuhorario.model.Discipline;

/**
 * Created by Diego Novaes on 28/02/2017.
 */
public class DisciplinesActivity extends AppCompatActivity {
    private String DisciplinesJson = "https://gist.githubusercontent.com/dnovaes/419fc613aee50ae971cbfed466c8e512/raw/b0aa21ca34ddcf3f540fa25c73872eb6956f5ab1/meuhorario_disciplines";
    private String DisciplinesCoursesJson = "https://gist.githubusercontent.com/dnovaes/fdea84cf2485a9350addd0d42f87c5c0/raw/d446f1ffa3cf1cb21f671afbc26370d956e726b0/meuhorario_courses_disciplines";
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
            Toast.makeText(this, "Atualizando dados de 'disciplines' ...", Toast.LENGTH_SHORT).show();
            new JSONParser(this, DisciplinesJson, "disciplines").execute();
            new JSONParser(this, DisciplinesCoursesJson, "course_disciplines").execute();
            //Add a window here that shows the return of JSONParser and force the user to click to back to the DisciplinesActivity and calls the onResume method
        }
        dao.close();

        ListDisciplinesAdapter adapter = new ListDisciplinesAdapter(disciplines, this);
        disciplinesList.setAdapter(adapter);
    }
}
