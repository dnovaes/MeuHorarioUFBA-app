package ufba.meuhorario;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import ufba.meuhorario.adapter.ListCoursesAdapter;
import ufba.meuhorario.dao.CourseDAO;
import ufba.meuhorario.model.Area;
import ufba.meuhorario.model.Course;

/**
 * Created by Diego Novaes on 26/02/2017.
 */
public class CoursesActivity extends AppCompatActivity{


    private String meuHorarioJson = "https://gist.githubusercontent.com/dnovaes/97a966d586640d1935ee604afe7e7a15/raw/6df5abd9811c18d74236853a7d9dc302d0817381/meuhorario_courses";
    private ListView coursesList;
    private Long area_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        Intent intent = getIntent();
        Area area = (Area) intent.getSerializableExtra("area");

        area_id = area.getId();

        TextView areaTitleView = (TextView) findViewById(R.id.course_area_title);
        areaTitleView.setText(area.getName());

        coursesList = (ListView)findViewById(R.id.list_courses);

        coursesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View view, int position, long id) {
                    Course course = (Course) list.getItemAtPosition(position);
                    //Toast.makeText(AreasActivity.this, "você clicou em "+area.getName(), Toast.LENGTH_SHORT).show();

                    Intent intentDisciplines = new Intent(CoursesActivity.this.getApplicationContext(), DisciplinesActivity.class);
                    intentDisciplines.putExtra("course", course);
                    startActivity(intentDisciplines);
                }
            }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadList();
    }

    private void loadList() {
        CourseDAO dao = new CourseDAO(this);

        //Select all the areas on the SQLite and form a List<Area>
        List<Course> courses = dao.getListCourses(area_id);

        if(courses.isEmpty()){
            // Creating a anonymous JsonParser instance
            // download json and insert into SQLite
            Toast.makeText(this, "Atualizando dados de 'courses' ...", Toast.LENGTH_SHORT).show();
            new JSONParser(this, meuHorarioJson, "courses").execute();
            courses = dao.getListCourses(area_id);
        }
        dao.close();

        ListCoursesAdapter adapter = new ListCoursesAdapter(courses, this);
        coursesList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.json_delete_courses, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.jsonDelete:
                CourseDAO courseDAO = new CourseDAO(this);
                courseDAO.deleteContent();
                loadList();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
