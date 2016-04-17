package com.github.skarllot.android.skllib.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.skarllot.android.skllib.Func2;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * Represents a service that continuously executes a work.
 */
public abstract class ContinuousWorkService extends Service {

    protected final String serviceName;
    private LoopHandler serviceHandler;

    public ContinuousWorkService(String serviceName) {
        super();

        this.serviceName = serviceName;
    }

    /**
     * Gets the sleep time between each work execution.
     *
     * @return The number of milliseconds to sleep.
     */
    protected abstract int getSleepTime();

    /**
     * Gets the work to execute continuously until it returns false.
     * <p>The Semaphore must be released after the work is done.
     *
     * @return The work method.
     */
    @NonNull
    protected abstract Func2<Boolean, Semaphore> getWork();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread(serviceName,
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        serviceHandler = new LoopHandler(thread.getLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("ContinuousWorkService",
                String.format("The service '%s' is being stopped", this.getClass().getSimpleName()));

        serviceHandler.stopEvent.release();
        serviceHandler = null;

        super.onDestroy();
    }

    private final class LoopHandler extends Handler {
        private final Semaphore stopEvent = new Semaphore(0);
        private final Semaphore workDoneEvent = new Semaphore(0);

        public LoopHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                while (!stopEvent.tryAcquire(0, TimeUnit.SECONDS)) {
                    if (!getWork().call(workDoneEvent)) {
                        break;
                    }

                    workDoneEvent.acquire();
                    if (stopEvent.tryAcquire(getSleepTime(), TimeUnit.MILLISECONDS)) {
                        break;
                    }
                }
            } catch (InterruptedException ignored) {
            }

            stopSelf(msg.arg1);
        }
    }
}
