package de.iweinzierl.timetracking.event;

import de.iweinzierl.timetracking.domain.Project;

import java.util.List;

public interface ProjectsChangedListener {

    void onProjectAdded(Project projected);
}
