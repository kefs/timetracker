package de.iweinzierl.timetracking.activities;

import android.test.ActivityInstrumentationTestCase2;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class de.iweinzierl.timetracking.activities.TimeTrackerTest \
 * de.iweinzierl.timetracking.tests/android.test.InstrumentationTestRunner
 */
public class TimeTrackerTest extends ActivityInstrumentationTestCase2<TimeTracker> {

    public TimeTrackerTest() {
        super("de.iweinzierl.timetracking", TimeTracker.class);
    }

}
