package de.iweinzierl.timetracking.domain;

import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.util.Date;

/**
 * A representation of a break. A break consists at least of a start and an end time.
 */
public class Break implements Serializable {

    private Integer id;
    private Integer jobId;

    private Date start;
    private Date end;

    private String comment;

    public Break() {}

    /**
     * Create a new instance of Break which time range is specified by <i>start</i> and <i>end</i>.
     *
     * @param start The start of this break.
     * @param end The end of this break.
     *
     * @throws NullPointerException if <i>start</i> or <i>end</i> is null.
     */
    public Break(Date start, Date end) {
        Preconditions.checkNotNull(start);
        Preconditions.checkNotNull(end);

        this.start = start;
        this.end = end;
    }

    public Break(Date start, Date end, String comment) {
        this(start, end);
        this.comment = comment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Calculates the milliseconds between {@link #end} and {@link #start} time.
     *
     * @return the milliseconds between {@link #end} and {@link #start} time.
     */
    public long calculateDuration() {
        return end.getTime() - start.getTime();
    }
}
