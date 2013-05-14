package de.iweinzierl.timetracking.domain;

import com.google.common.base.Preconditions;

import java.util.List;

public class ProjectBuilder {

    private String title;
    private String identifier;
    private String comment;

    private Integer id;
    private Integer customerId;

    private List<Job> jobs;

    public Project build() {
        Preconditions.checkNotNull(title);

        Project project = new Project(title);
        project.setId(id);
        project.setIdentifier(identifier);
        project.setComment(comment);
        project.setJobs(jobs);
        project.setCustomerId(customerId);

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

    public void setId(Integer id) {
        this.id = id;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
}
