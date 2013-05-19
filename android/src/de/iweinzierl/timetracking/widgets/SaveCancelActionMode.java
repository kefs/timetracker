package de.iweinzierl.timetracking.widgets;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import de.iweinzierl.timetracking.R;

public class SaveCancelActionMode implements ActionMode.Callback {

    public interface SelectionListener {
        void onSave();

        void onCancel();
    }

    private SelectionListener listener;

    public SaveCancelActionMode(SaveCancelActionMode.SelectionListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.actionmode_savecancel, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {

            case R.id.save:
                listener.onSave();
                return true;

            case R.id.cancel:
                listener.onCancel();
                return true;
        }

        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }
}