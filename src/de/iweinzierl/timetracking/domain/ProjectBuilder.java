package de.iweinzierl.timetracking.domain;

import com.google.common.base.Preconditions;

import java.util.List;

public class ProjectBuilder {

    private String title;
    private String identifier;
    private String comment;

    private long id;

    private List<Job> jobs;

    public Project build() {
        Preconditions.checkNotNull(title);
        Preconditions.checkNotNull(id);

        Project project = new Project(title);
        project.setId(id);
        project.setIdentifier(identifier);
        project.setComment(comment);
        project.setJobs(jobs);

        return project;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }
}
