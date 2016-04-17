package com.github.skarllot.android.skllib.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.github.skarllot.android.skllib.Func2;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * A service test connectivity through DNS probing.
 */
public class NsProberService extends ContinuousWorkService {
    public static final String ACTION_CONNECTION_STATUS_CHANGED =
            "br.com.fritecnologia.android.ims.CONNECTION_STATUS_CHANGED";
    private final static int DEFAULT_SLEEP_TIME = 8 * 1000;
    private static final String SERVICE_NAME = "NSProber";
    public static final String EXTRA_CONNECTION_STATUS = "ConnectionStatusResult";
    private static final String EXTRA_SLEEP_TIME = "SleepTime";
    private static final String EXTRA_NS_ADDRESS = "NSAddress";
    private static final String EXTRA_NS_RESULT = "NSResult";

    private LocalBroadcastManager broadcastManager;
    protected Func2<Void, Integer> connectionStatusCallback;
    private int lastStatus = ConnectionStatus.UNDEFINED;
    private int sleepTime = DEFAULT_SLEEP_TIME;
    private String nsAddress;
    private String nsResult;

    public NsProberService() {
        super(SERVICE_NAME);
    }

    /**
     * Creates a new Intent required to start this service.
     *
     * @param context   The current context.
     * @param sleepTime Defines the time wait between each probing.
     * @param nsAddr    The DNS address to probe.
     * @param nsResult  The expected result from DNS probe.
     * @param cls       The class to create a new Intent.
     * @param <T>       The type used to create a new Intent.
     * @return The Intent instance to start a new service.
     */
    public static <T extends NsProberService> Intent createIntent(
            Context context,
            int sleepTime,
            String nsAddr,
            String nsResult,
            @Nullable Class<T> cls
    ) {
        final Class<?> intentCls;
        if (cls == null)
            intentCls = NsProberService.class;
        else
            intentCls = cls;

        Intent intent = new Intent(context, intentCls);
        intent.putExtra(EXTRA_SLEEP_TIME, sleepTime);
        intent.putExtra(EXTRA_NS_ADDRESS, nsAddr);
        intent.putExtra(EXTRA_NS_RESULT, nsResult);

        return intent;
    }

    @Override
    protected int getSleepTime() {
        return sleepTime;
    }

    @NonNull
    @Override
    protected Func2<Boolean, Semaphore> getWork() {
        return new Func2<Boolean, Semaphore>() {
            @Override
            public Boolean call(Semaphore workDoneEvent) {
                probe();
                workDoneEvent.release();
                return true;
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        sleepTime = extras.getInt(EXTRA_SLEEP_TIME);
        nsAddress = extras.getString(EXTRA_NS_ADDRESS);
        nsResult = extras.getString(EXTRA_NS_RESULT);

        return super.onStartCommand(intent, flags, startId);
    }

    private void probe() {
        try {
            InetAddress currentResult = InetAddress.getByName(nsAddress);
            if (!currentResult.getHostAddress().equals(nsResult)) {
                sendBroadcast(ConnectionStatus.INVALID_CONNECTION);
                return;
            }
        } catch (UnknownHostException e) {
            sendBroadcast(ConnectionStatus.NO_CONNECTION);
            return;
        }

        sendBroadcast(ConnectionStatus.CONNECTED);
    }

    /**
     * Sends the probe result for all receivers.
     *
     * @param result The probe result.
     */
    private synchronized void sendBroadcast(final int result) {
        // Lazy loading
        if (broadcastManager == null) {
            broadcastManager = LocalBroadcastManager.getInstance(this);
        }

        if (lastStatus != result) {
            lastStatus = result;
            if (connectionStatusCallback != null) {
                connectionStatusCallback.call(result);
            }
        }

        Intent intent = new Intent(ACTION_CONNECTION_STATUS_CHANGED);
        intent.putExtra(EXTRA_CONNECTION_STATUS, result);
        broadcastManager.sendBroadcast(intent);
    }
}
