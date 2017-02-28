package ufba.meuhorario.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ufba.meuhorario.R;
import ufba.meuhorario.model.Area;

/**
 * Created by Diego Novaes on 26/02/2017.
 */
public class ListAreasAdapter extends BaseAdapter{

    private Activity activity;
    private List<Area> areas;

    public ListAreasAdapter(List<Area> areas, Activity activity) {
        this.areas = areas;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return areas.size();
    }

    @Override
    public Object getItem(int position) {
        return areas.get(position);
    }

    @Override
    public long getItemId(int position) {
        Area area = areas.get(position);
        return area.getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Area area = areas.get(position);

        LayoutInflater inflater = activity.getLayoutInflater();
        View line = inflater.inflate(R.layout.list_areas_line, null);

        TextView viewName = (TextView) line.findViewById(R.id.line_name);
        viewName.setText(area.getName());

        TextView viewDesc = (TextView) line.findViewById(R.id.line_desc);
        viewDesc.setText(area.getDescription());

        return line;
    }
}
