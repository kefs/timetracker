package de.iweinzierl.timetracking.domain;

import com.google.common.base.Preconditions;

import java.util.Date;

public class JobBuilder {

    private Integer id;
    private Integer projectId;

    private Date start;
    private Date end;

    private String title;
    private String comment;

    public Job build() {
        Preconditions.checkNotNull(projectId);
        Preconditions.checkNotNull(start);
        Preconditions.checkNotNull(end);
        Preconditions.checkArgument(start.getTime() < end.getTime());

        Job job = new Job();
        job.setId(id);
        job.setProjectId(projectId);
        job.setStart(start);
        job.setEnd(end);
        job.setTitle(title);
        job.setComment(comment);

        return job;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
