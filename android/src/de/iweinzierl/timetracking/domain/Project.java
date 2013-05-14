package de.iweinzierl.timetracking.domain;

import com.google.common.base.Preconditions;
import de.iweinzierl.timetracking.utils.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A Project represents a set of {@link Job}s for a specific {@link Customer}. A Project is upon a {@link Job} and
 * groups activities.
 */
public class Project implements Serializable {

    private Integer id;
    private Integer customerId;

    private String identifier;
    private String title;
    private String comment;

    private List<Job> jobs;

    /**
     * Create a new Project instance.
     *
     * @param title The title of the new Project. May not be null. May not have zero length.
     *
     * @throws NullPointerException if <i>title</i> is null.
     * @throws IllegalArgumentException if <i>title</i> has zero length.
     */
    public Project(String title) {
        super();

        Preconditions.checkNotNull(title);
        Preconditions.checkArgument(title.length() > 0);

        this.title = title;
        this.jobs = new ArrayList<Job>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public void addJob(Job job) {
        if (job != null) {
            if (jobs.add(job)) {
                job.setProjectId(getId());
            }
        }
    }

    /**
     * Calculates the duration of {@link Job}s in this Project.
     *
     * @return the total duration of {@Job}s in this Project in milliseconds.
     */
    public long calculateDuration() {
        long total = 0l;

        for (Job j: getJobs()) {
            try {
                total += j.calculateDuration();
            }
            catch (IllegalStateException e) {
                Logger.warn(this.getClass(), String.format("Unable to include duration of %s to total duration of " +
                        "Project.", j.getTitle()));
            }
        }

        Logger.debug(this.getClass(), String.format("Total duration of %s is %s ms", getTitle(), total));
        return total;
    }
}
