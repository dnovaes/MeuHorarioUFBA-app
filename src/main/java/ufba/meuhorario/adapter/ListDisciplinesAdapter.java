package ufba.meuhorario.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ufba.meuhorario.DisciplinesActivity;
import ufba.meuhorario.R;
import ufba.meuhorario.model.Discipline;

/**
 * Created by Diego Novaes on 01/03/2017.
 */

public class ListDisciplinesAdapter extends BaseAdapter {

    private Activity activity;
    private List<Discipline> disciplines;

    public ListDisciplinesAdapter(List<Discipline> disciplines, Activity activity) {
        this.disciplines = disciplines;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return disciplines.size();
    }

    @Override
    public Object getItem(int position) {
        return disciplines.get(position);
    }

    @Override
    public long getItemId(int position) {
        Discipline discipline = disciplines.get(position);
        return discipline.getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Discipline discipline = disciplines.get(position);

        LayoutInflater inflater = activity.getLayoutInflater();
        View line = inflater.inflate(R.layout.list_disci_line, null);

        TextView viewCode = (TextView) line.findViewById(R.id.line_disci_code);
        viewCode.setText(discipline.getCode());

        TextView viewName = (TextView) line.findViewById(R.id.line_disci_name);
        viewName.setText(discipline.getName());

        return line;
    }
}
