package de.iweinzierl.timetracking.widgets;

import android.content.Context;
import com.google.gson.Gson;
import de.iweinzierl.timetracking.domain.Job;
import de.iweinzierl.timetracking.domain.JobBuilder;
import de.iweinzierl.timetracking.utils.Logger;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

public class StartStopHandler {

    public static final String FILENAME_PERSISTENT_STARTSTOP = "startstop.json";

    private class PersistentStartStop implements Serializable {
        public int projectId;
        public long startTime;
        public long endTime;

        public void reset() {
            projectId = 0;
            startTime = 0;
            endTime = 0;
        }

        private int getProjectId() {
            return projectId;
        }

        private void setProjectId(int projectId) {
            this.projectId = projectId;
        }

        private long getStartTime() {
            return startTime;
        }

        private void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        private long getEndTime() {
            return endTime;
        }

        private void setEndTime(long endTime) {
            this.endTime = endTime;
        }
    }

    private Context context;
    private PersistentStartStop persistentStartStop;

    public StartStopHandler(Context context) {
        this.context = context;
        persistentStartStop = new PersistentStartStop();
    }

    public void start(int projectId) {
        persistentStartStop.reset();
        persistentStartStop.setProjectId(projectId);
        persistentStartStop.setStartTime(System.currentTimeMillis());

        saveToDisk();
    }

    public Job stop() {
        persistentStartStop.setEndTime(System.currentTimeMillis());

        JobBuilder builder = new JobBuilder();
        builder.setProjectId(persistentStartStop.getProjectId());
        builder.setStart(new Date(persistentStartStop.getStartTime()));
        builder.setEnd(new Date(persistentStartStop.getEndTime()));

        return builder.build();
    }

    private void saveToDisk() {
        try {
            FileOutputStream out = context.openFileOutput(FILENAME_PERSISTENT_STARTSTOP, Context.MODE_PRIVATE);

            Gson gson = new Gson();
            String json = gson.toJson(persistentStartStop);

            out.write(json.getBytes());
        } catch (FileNotFoundException e) {
            Logger.error(getClass(),
                    String.format("Unable to persist StartStop to file: %s", FILENAME_PERSISTENT_STARTSTOP), e);
        } catch (IOException e) {
            Logger.error(getClass(), "Unable to persist StartStop to disk", e);
        }
    }
}
