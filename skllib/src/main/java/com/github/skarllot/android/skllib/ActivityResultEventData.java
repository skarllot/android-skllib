package com.github.skarllot.android.skllib;

import android.content.Intent;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * Represents a event data for ActivityResult events.
 */
public class ActivityResultEventData extends EventData {
    private final int requestCode;
    private final int resultCode;
    private final Intent data;

    public ActivityResultEventData(int requestCode, int resultCode, Intent data) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public Intent getData() {
        return data;
    }
}
