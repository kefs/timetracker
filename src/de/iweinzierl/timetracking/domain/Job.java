package de.iweinzierl.timetracking.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This domain class represents an activity that has been done by the user. <i>Jobs</i> have at least a timerange and a
 * title. A Job may be interrupted by one or multiple {@link Break}s.
 */
public class Job implements Serializable {

    private long id;

    private String title;
    private String comment;

    private Date startTime;
    private Date stopTime;

    private List<Break> breaks;

    public Job() {
        breaks = new ArrayList<Break>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public List<Break> getBreaks() {
        return breaks;
    }

    public void setBreaks(List<Break> breaks) {
        this.breaks = breaks;
    }

    /**
     * Add a new {@link Break} to this Job if <i>aBreak</i> is not null.
     *
     * @param aBreak The break that should be added. May not be null.
     *
     * @return the {@link Break} if it was added or null.
     */
    public Break addBreak(Break aBreak) {
        if (aBreak != null) {
            breaks.add(aBreak);
            return aBreak;
        }

        return null;
    }

    /**
     * Calculate the duration of all breaks that have been done during this Job.
     *
     * @return the duration of breaks in milliseconds.
     */
    public long calculateDurationOfBreaks() {
        long duration = 0l;

        for (Break b: breaks) {
            duration += b.calculateDuration();
        }

        return duration;
    }

    /**
     * Calculate the duration that the user is already working at this {@link Job}. If no {@link #stopTime} is set,
     * the time from the beginning until now is calculated. <b>This calculation also includes the time of breaks that
     * have interrupt this Job</b>.
     *
     * @return the time in milliseconds that the user is or was working on this {@link Job}.
     */
    public long calculateDuration() {
        if (startTime == null) {
            return 0l;
        }
        else if (startTime != null && stopTime == null) {
            return System.currentTimeMillis() - startTime.getTime() - calculateDurationOfBreaks();
        }
        else if (startTime != null && stopTime != null) {
            return stopTime.getTime() - startTime.getTime() - calculateDurationOfBreaks();
        }
        else {
            throw new IllegalStateException("Illegal state of Job. Cannot calculate duration!");
        }
    }
}
