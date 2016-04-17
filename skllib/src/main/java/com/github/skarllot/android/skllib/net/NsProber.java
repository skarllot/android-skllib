package com.github.skarllot.android.skllib.net;

import android.os.AsyncTask;

import java.net.InetAddress;
import java.net.UnknownHostException;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * Represents a task for DNS probing.
 */
public class NsProber extends AsyncTask<String, Void, NsProbeResult> {

    @Override
    protected NsProbeResult doInBackground(String... params) {
        String nsAddr = params[0];
        String nsResult = params[1];

        try {
            InetAddress currentResult = InetAddress.getByName(nsAddr);
            if (!currentResult.getHostAddress().equals(nsResult)) {
                return NsProbeResult.RESULT_NOT_MATCH;
            }
        } catch (UnknownHostException e) {
            return NsProbeResult.NO_CONNECTION;
        }

        return NsProbeResult.SUCCESSFUL;
    }
}
