package de.iweinzierl.timetracking.event;

public interface HasProjectsChangedListeners {

    void registerProjectsChangedListener(ProjectsChangedListener listener);
}
