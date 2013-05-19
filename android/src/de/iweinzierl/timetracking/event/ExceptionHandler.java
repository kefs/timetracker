package de.iweinzierl.timetracking.event;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public class ExceptionHandler extends Handler {

    public static final int WHAT_EXCEPTION = 1;

    private static final String DATA_EXCEPTION_MESSAGE = "data.exception.message";

    private Context context;

    public ExceptionHandler(Context context) {
        super(Looper.getMainLooper());
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        switch (msg.what) {

            case WHAT_EXCEPTION:
                CharSequence charSequence = msg.getData().getCharSequence(DATA_EXCEPTION_MESSAGE);
                Toast.makeText(context, charSequence, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public static Bundle createBundle(String template, String... args) {
        Bundle bundle = new Bundle();
        bundle.putString(DATA_EXCEPTION_MESSAGE, String.format(template, args));

        return bundle;
    }
}
