package ufba.meuhorario;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ufba.meuhorario.adapter.ListDisciplinesAdapter;
import ufba.meuhorario.dao.DisciplineDAO;
import ufba.meuhorario.model.Discipline;


/**
 * Created by Diego Novaes on 16/03/2017.
 */

public class InfoDisciplineActivity extends AppCompatActivity {

    private Long discipline_id;
    private ListView prereqList;
    private TextView prereqTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_discipline);

        Intent intent = getIntent();
        Discipline discipline = (Discipline) intent.getSerializableExtra("discipline");

        discipline_id = discipline.getId();

        //fill infodisc title with discipline name
        TextView infodiscTextView = (TextView) findViewById(R.id.infodisc_title);
        infodiscTextView.setText(discipline.getName());

        //get references to all needed views
        prereqList = (ListView)findViewById(R.id.infodisc_prereq_list);
        prereqTextView = (TextView) findViewById(R.id.infodisc_prereq_title);

        final ListView classesList = (ListView)findViewById(R.id.infodisc_classes_list);
        TextView classesTextView = (TextView) findViewById(R.id.infodisc_classes_title);

        //Set all the listviews invisible
        prereqList.setVisibility(View.GONE);
        classesList.setVisibility(View.GONE);

        //disciplinesList = (ListView) findViewById(R.id.info_discipline_view);
        //TextView titleView = (TextView) findViewById(R.id.info_discipline_title);
        //titleView.setText(discipline.getName());

        prereqTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch(prereqList.getVisibility()) {
                    case (View.GONE):
                        prereqList.setVisibility(View.VISIBLE);
                        break;
                    case (View.VISIBLE):
                        prereqList.setVisibility(View.GONE);
                        break;
                }
            }
        });

        classesTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch(classesList.getVisibility()) {
                    case (View.GONE):
                        classesList.setVisibility(View.VISIBLE);
                        break;
                    case (View.VISIBLE):
                        classesList.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void loadList() {

        DisciplineDAO dao = new DisciplineDAO(this);

        //Select all the areas on the SQLite and form a List<Area>
        //TODO: if json is corretly do a function that pick preReq disciplines
        //List<Discipline> prereqs = dao.getListPreReqDisciplines(discipline_id);
        List<Discipline> prereqs =  new ArrayList<Discipline>();
        dao.close();

        ListDisciplinesAdapter adapter = new ListDisciplinesAdapter(prereqs, this);
        prereqList.setAdapter(adapter);

        ClassDAO dao = new ClassDAO(this);
    }

}