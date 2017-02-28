package ufba.meuhorario.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ufba.meuhorario.R;
import ufba.meuhorario.model.Course;

/**
 * Created by Diego Novaes on 26/02/2017.
 */
public class ListCoursesAdapter extends BaseAdapter{

    private Activity activity;
    private List<Course> courses;

    public ListCoursesAdapter(List<Course> courses, Activity activity) {
        this.courses = courses;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return courses.size();
    }

    @Override
    public Object getItem(int position) {
        return courses.get(position);
    }

    @Override
    public long getItemId(int position) {
        Course course = courses.get(position);
        return course.getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Course course = courses.get(position);

        LayoutInflater inflater = activity.getLayoutInflater();
        View line = inflater.inflate(R.layout.list_courses_line, null);

        TextView viewName = (TextView) line.findViewById(R.id.line_course_text);
        viewName.setText(course.getCode()+" - "+course.getName());

        return line;
    }
}
