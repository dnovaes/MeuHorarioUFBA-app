package ufba.meuhorario.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ufba.meuhorario.R;
import ufba.meuhorario.model.Schedule;

/**
 * Created by Diego Novaes on 01/03/2017.
 */

public class ListSchedulesAdapter extends BaseAdapter {

    private Activity activity;
    private List<Schedule> schedules;

    public ListSchedulesAdapter(List<Schedule> schedules, Activity activity) {
        this.schedules = schedules;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return schedules.size();
    }

    @Override
    public Object getItem(int position) {
        return schedules.get(position);
    }

    @Override
    public long getItemId(int position) {
        Schedule schedule = schedules.get(position);
        return schedule.getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Schedule schedule = schedules.get(position);

        LayoutInflater inflater = activity.getLayoutInflater();
        View line = inflater.inflate(R.layout.list_schedule_line, null);

        TextView viewDay = (TextView) line.findViewById(R.id.line_schedule_day);
        viewDay.setText(schedule.getDayString((schedule.getDay())));

        TextView viewEndHour = (TextView) line.findViewById(R.id.line_schedule_hour);
        viewEndHour.setText(schedule.getStartHour()+":"+schedule.getStartMin()+" às "+schedule.getEndHour()+":"+schedule.getEndMin());

        TextView viewProfName = (TextView) line.findViewById(R.id.line_schedule_profname);
        viewProfName.setText(schedule.getProfName());

        return line;
    }
}
