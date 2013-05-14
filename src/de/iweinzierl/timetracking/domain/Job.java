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

    private Integer id;
    private Integer projectId;

    private String title;
    private String comment;

    private Date start;
    private Date end;

    private List<Break> breaks;

    public Job() {
        breaks = new ArrayList<Break>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
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

    public Date getStart() {
        return start;
    }

    public void setStart(Date startTime) {
        this.start = startTime;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date stopTime) {
        this.end = stopTime;
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
     * Calculate the duration that the user is already working at this {@link Job}. If no {@link #end} is set,
     * the time from the beginning until now is calculated. <b>This calculation also includes the time of breaks that
     * have interrupt this Job</b>.
     *
     * @return the time in milliseconds that the user is or was working on this {@link Job}.
     */
    public long calculateDuration() {
        if (start == null) {
            return 0l;
        }
        else if (start != null && end == null) {
            return System.currentTimeMillis() - start.getTime() - calculateDurationOfBreaks();
        }
        else if (start != null && end != null) {
            return end.getTime() - start.getTime() - calculateDurationOfBreaks();
        }
        else {
            throw new IllegalStateException("Illegal state of Job. Cannot calculate duration!");
        }
    }
}
