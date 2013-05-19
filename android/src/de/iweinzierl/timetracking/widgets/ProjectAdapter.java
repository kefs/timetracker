package de.iweinzierl.timetracking.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.iweinzierl.timetracking.R;
import de.iweinzierl.timetracking.domain.Project;

import java.util.ArrayList;
import java.util.List;

public class ProjectAdapter extends BaseAdapter {

    public static final int RES_LAYOUT = R.layout.adapter_project;

    private List<Project> projects;
    private Context context;

    public ProjectAdapter(Context context) {
        this.context = context;
        projects = new ArrayList<Project>();
    }

    @Override
    public int getCount() {
        return projects.size();
    }

    @Override
    public Object getItem(int position) {
        return position < getCount() ? projects.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(RES_LAYOUT, parent);
        setText(view, projects.get(position));

        return view;
    }

    private TextView getTextView(View container, int resId) {
        View child = container.findViewById(resId);
        return child instanceof TextView ? (TextView) child : null;
    }

    private void setText(View container, Project project) {
        TextView tv = getTextView(container, R.id.text);
        if (tv != null) {
            tv.setText(project.getTitle());
        }
    }

    public void setProjects(List<Project> newProjects) {
        if (newProjects != null) {
            projects = newProjects;
            notifyDataSetChanged();
        }
    }
}
