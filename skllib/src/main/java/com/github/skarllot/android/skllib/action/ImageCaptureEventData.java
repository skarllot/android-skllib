package com.github.skarllot.android.skllib.action;

import com.github.skarllot.android.skllib.EventData;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * Represents a event data for ImageCapture events.
 */
public class ImageCaptureEventData extends EventData {
    private final String filename;
    private final ImageCaptureResult result;

    public ImageCaptureEventData(ImageCaptureResult result) {
        this.filename = null;
        this.result = result;
    }

    public ImageCaptureEventData(String filename) {
        this.filename = filename;
        this.result = ImageCaptureResult.NoError;
    }

    public String getFilename() {
        return filename;
    }

    public ImageCaptureResult getResult() {
        return result;
    }
}
