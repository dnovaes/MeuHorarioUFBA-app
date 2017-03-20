package ufba.meuhorario.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ufba.meuhorario.R;
import ufba.meuhorario.model.ClassInfo;
import ufba.meuhorario.model.Schedule;

/**
 * Created by Diego Novaes on 01/03/2017.
 */

public class ListClassesInfoAdapter extends BaseAdapter {

    private Activity activity;
    private List<ClassInfo> classesInfo;

    public ListClassesInfoAdapter(List<ClassInfo> classesInfo, Activity activity) {
        this.classesInfo = classesInfo;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return classesInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return classesInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        ClassInfo classInfo = classesInfo.get(position);
        return classInfo.getDisciplineClassId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClassInfo classInfo = classesInfo.get(position);

        LayoutInflater inflater = activity.getLayoutInflater();
        View line = inflater.inflate(R.layout.list_classinfo_line, null);

        TextView viewClassNumber = (TextView) line.findViewById(R.id.line_classinfo_classnumber);
        viewClassNumber.setText(classInfo.getClassNumber());

        TextView viewVacancy = (TextView) line.findViewById(R.id.line_classinfo_vacancy);
        viewVacancy.setText(classInfo.getVacancy()+" Vagas");

        ListView listViewSchedule = (ListView) line.findViewById(R.id.line_classinfo_schedule);

        //TODO: change "new ArrayList" to a function that gets all the schedules related to this disciplineClassId (ClassInfoID)

        List<Schedule> scheduleList =  classInfo.getScheduleList();
        ListSchedulesAdapter adapter = new ListSchedulesAdapter(scheduleList, activity);
        listViewSchedule.setAdapter(adapter);

        return line;
    }
}
