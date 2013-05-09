package de.iweinzierl.timetracking.activities;

import android.app.Activity;
import android.os.Bundle;
import de.iweinzierl.timetracking.R;

public class TimeTracker extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}
