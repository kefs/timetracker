package de.iweinzierl.timetracking.widgets;

import android.content.Context;
import android.os.*;
import android.widget.TextView;
import com.google.gson.Gson;
import de.iweinzierl.timetracking.domain.Job;
import de.iweinzierl.timetracking.domain.JobBuilder;
import de.iweinzierl.timetracking.utils.Logger;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Date;

public class StartStopHandler {

    public static final String FILENAME_PERSISTENT_STARTSTOP = "startstop.json";

    private class PersistentStartStop implements Serializable {
        public int projectId;
        public long startTime;
        public long endTime;

        public PersistentStartStop() {
            projectId = 0;
            startTime = 0;
            endTime = 0;
        }

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

    public static class UpdateClockHandler extends Handler {

        public static final int DISPLAY_DURATION = 1;

        public static final String DATA_DURATION = "updateclockhandler.data.duration";

        private TextView clock;

        public UpdateClockHandler(TextView clock) {
            super(Looper.getMainLooper());
            this.clock = clock;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case DISPLAY_DURATION:
                    String duration = String.valueOf(getDurationFromBundle(msg.getData()));
                    clock.setText(duration + " ms");
                    break;
            }
        }

        public static Bundle createBundle(long duration) {
            Bundle bundle = new Bundle();
            bundle.putLong(DATA_DURATION, duration);

            return bundle;
        }

        public static long getDurationFromBundle(Bundle bundle) {
            if (bundle.containsKey(DATA_DURATION)) {
                return bundle.getLong(DATA_DURATION);
            }

            return -1;
        }
    }

    private Context context;
    private PersistentStartStop persistentStartStop;
    private TextView clock;

    private AsyncTask<Void, Void, Void> updateThread;

    public StartStopHandler(Context context) {
        this.context = context;
        persistentStartStop = getPersistentStartStop();
    }

    public void start(int projectId) {
        Logger.info(getClass(), "Started new Job for Project %s", String.valueOf(projectId));

        persistentStartStop.reset();
        persistentStartStop.setProjectId(projectId);
        persistentStartStop.setStartTime(System.currentTimeMillis());

        startUpdateClock();
        saveToDisk();
    }

    public Job stop() {
        persistentStartStop.setEndTime(System.currentTimeMillis());
        Logger.info(getClass(), "Stop Job for Project %s", String.valueOf(persistentStartStop.getProjectId()));

        stopUpdateClock();
        removePersistentStartStopFromDisk();

        JobBuilder builder = new JobBuilder();
        builder.setProjectId(persistentStartStop.getProjectId());
        builder.setStart(new Date(persistentStartStop.getStartTime()));
        builder.setEnd(new Date(persistentStartStop.getEndTime()));

        persistentStartStop.reset();

        return builder.build();
    }

    public void setTextView(TextView clock) {
        this.clock = clock;
        if (persistentStartStop.getStartTime() > 0) {
            startUpdateClock();
        }
    }

    public void startUpdateClock() {
        updateThread = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                while (!isCancelled()) {
                    try {
                        long duration = System.currentTimeMillis() - persistentStartStop.getStartTime();

                        Message message = Message.obtain(new UpdateClockHandler(clock),
                                UpdateClockHandler.DISPLAY_DURATION);
                        message.setData(UpdateClockHandler.createBundle(duration));
                        message.sendToTarget();

                        wait(1000);
                    }
                    catch (Exception e) {
                        // do nothing
                    }
                }

                return null;
            }
        };

        updateThread.execute();
    }

    public void stopUpdateClock() {
        if (updateThread != null) {
            updateThread.cancel(true);
            updateThread = null;
        }
    }

    private PersistentStartStop getPersistentStartStop() {
        File persistentStartStop = getPersistentStartStopFile();
        if (persistentStartStop.exists()) {
            try {
                FileInputStream in = new FileInputStream(persistentStartStop);
                String json = IOUtils.toString(in);
                Gson gson = new Gson();
                return gson.fromJson(json, PersistentStartStop.class);

            } catch (Exception e) {
                Logger.error(getClass(), "Unable to load persisted start stop file", e);
            }
        }

        return new PersistentStartStop();
    }

    private File getPersistentStartStopFile() {
        File filesDir = context.getFilesDir();
        return new File(filesDir, FILENAME_PERSISTENT_STARTSTOP);
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

    private void removePersistentStartStopFromDisk() {
        File persistentStartStop = getPersistentStartStopFile();
        if (persistentStartStop.exists()) {
            persistentStartStop.delete();
        }
    }
}
