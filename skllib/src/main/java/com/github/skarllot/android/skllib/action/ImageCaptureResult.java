package com.github.skarllot.android.skllib.action;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * Defines results for image capturing action.
 */
public enum ImageCaptureResult {
    /**
     * Capture was cancelled.
     */
    Cancelled,
    /**
     * Error trying to create file.
     */
    CreateFileError,
    /**
     * No errors occurred.
     */
    NoError
}
