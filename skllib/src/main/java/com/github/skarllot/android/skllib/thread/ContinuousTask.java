package com.github.skarllot.android.skllib.thread;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * Provides a feature to schedule a task in predefined interval.
 */
public class ContinuousTask {
    private final Handler handler;
    private final Runnable runnable;

    /**
     * Creates a new instance of ContinuousTask.
     *
     * @param runnable The task to schedule.
     * @param interval The interval between task executions.
     */
    public ContinuousTask(
            @NonNull final Runnable runnable,
            final long interval
    ) {
        handler = new Handler(Looper.getMainLooper());
        this.runnable = new Runnable() {
            @Override
            public void run() {
                runnable.run();
                handler.postDelayed(this, interval);
            }
        };
    }

    /**
     * Starts the task and infinitely schedules it for execution until stopped.
     */
    public synchronized void start() {
        runnable.run();
    }

    /**
     * Stops the task scheduling.
     */
    public synchronized void stop() {
        handler.removeCallbacks(runnable);
    }
}
