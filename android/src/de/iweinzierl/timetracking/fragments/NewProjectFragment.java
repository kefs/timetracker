package de.iweinzierl.timetracking.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import de.iweinzierl.timetracking.R;
import de.iweinzierl.timetracking.domain.Project;
import de.iweinzierl.timetracking.domain.ProjectBuilder;
import de.iweinzierl.timetracking.utils.Logger;
import de.iweinzierl.timetracking.widgets.SaveCancelActionMode;

public class NewProjectFragment extends Fragment implements TimeTrackerFragment<NewProjectFragment>,
        SaveCancelActionMode.SelectionListener {

    public interface Callback {
        void save(Project project);

        void cancel();
    }

    private Callback callback;

    @Override
    public NewProjectFragment create() {
        return new NewProjectFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_newproject, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        Activity activity = getActivity();
        if (activity instanceof Callback) {
            this.callback = (Callback) activity;
        } else {
            throw new IllegalArgumentException("Parent Activity must implement Callback");
        }

        activity.startActionMode(new SaveCancelActionMode(this));
    }

    @Override
    public void onSave() {
        saveProject();
    }

    @Override
    public void onCancel() {
        cancel();
    }

    private void saveProject() {
        Logger.debug(getClass(), "Save new customer");
        Project project;

        try {
            project = getProject();
            callback.save(project);

            return;
        }
        catch (Exception e) {
            Logger.error(getClass(), "Unable to build new Customer", e);
        }

        Toast.makeText(getActivity(), R.string.newcustomer_dialog_cannotsave, Toast.LENGTH_SHORT).show();
    }

    private void cancel() {
        Logger.debug(getClass(), "Cancel adding new customer");
        callback.cancel();
    }

    private Project getProject() throws NullPointerException {
        ProjectBuilder builder = new ProjectBuilder();
        builder.setTitle(getTitle());

        return builder.build();
    }

    private String getTitle() {
        View root = getView();
        View title = root != null ? root.findViewById(R.id.newproject_title) : null;

        if (title instanceof EditText) {
            return ((EditText) title).getText().toString();
        }

        return null;
    }
}
