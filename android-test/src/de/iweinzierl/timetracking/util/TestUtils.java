package de.iweinzierl.timetracking.util;

import de.iweinzierl.timetracking.domain.Job;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TestUtils {

    public static Job createJob(String title, int duration) {
        Date[] dates = createStartEndDates(duration);

        Job job = new Job();
        job.setTitle(title);
        job.setStart(dates[0]);
        job.setEnd(dates[1]);

        return job;
    }

    public static Date[] createStartEndDates(int duration) {
        Calendar cal = new GregorianCalendar();

        Date end = cal.getTime();
        cal.add(Calendar.MINUTE, duration * -1);

        return new Date[] { cal.getTime(), end};
    }
}
