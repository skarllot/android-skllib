package com.github.skarllot.android.skllib.action;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import com.github.skarllot.android.skllib.ActivityResultEventData;
import com.github.skarllot.android.skllib.EventHandler;
import com.github.skarllot.android.skllib.EventListener;
import com.github.skarllot.android.skllib.EventObserver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * Provides methods for image capturing.
 */
public class ImageCapture {
    private final static String FILENAME_DATE_FORMAT = "yyyyMMdd_HHmmss";
    // Activity result code
    private final static int RESULT_IMAGE_CAPTURE = 8765;

    private final Activity activity;
    private final EventObserver<ActivityResultEventData> activityResultObserver;
    private final Fragment fragment;
    private final EventHandler<ImageCaptureEventData> newCaptureEvent;

    /***
     * Creates a new instance of ImageCapture.
     *
     * @param activity               The Activity that this request was originated from.
     * @param activityResultObserver The observer that triggers onActivityResult event.
     */
    public ImageCapture(
            Activity activity,
            EventObserver<ActivityResultEventData> activityResultObserver
    ) {
        this.activity = activity;
        this.activityResultObserver = activityResultObserver;
        this.fragment = null;

        newCaptureEvent = new EventHandler<>();
    }

    /***
     * Creates a new instance of ImageCapture.
     *
     * @param fragment               The Activity that this request was originated from.
     * @param activityResultObserver The observer that triggers onActivityResult event.
     */
    public ImageCapture(
            Fragment fragment,
            EventObserver<ActivityResultEventData> activityResultObserver
    ) {
        this.activity = fragment.getActivity();
        this.activityResultObserver = activityResultObserver;
        this.fragment = fragment;

        newCaptureEvent = new EventHandler<>();
    }

    /**
     * Creates a new Activity to capture a new image and triggers NewCapture event after capture
     * request is complete.
     *
     * @return True whether the requested Activity could be created; otherwise false.
     */
    public boolean captureNewImage() {
        // Starts a new Activity to handle image capture
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Test whether ACTION_IMAGE_CAPTURE Activity can be created.
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            File image = getNewFilename();

            // Tests whether a new file could be created.
            if (image != null) {
                activityResultObserver.setListener(onNewCapture(image.getAbsolutePath()));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
                if (fragment == null)
                    activity.startActivityForResult(intent, RESULT_IMAGE_CAPTURE);
                else
                    fragment.startActivityForResult(intent, RESULT_IMAGE_CAPTURE);
            } else {
                newCaptureEvent.trigger(this, new ImageCaptureEventData(
                        ImageCaptureResult.CreateFileError));
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Deletes a file from Android image gallery.
     *
     * @param filename The name of the file to delete.
     */
    public void deleteCapturedFile(String filename) {
        activity.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Images.Media.DATA + "=?", new String[]{filename});
    }

    /**
     * Gets a filename to write a new capture.
     *
     * @return A File to write a new capture; Null in case of lack of permission.
     */
    @Nullable
    private File getNewFilename() {
        String filename = String.format("EMAIL_%s.jpg",
                new SimpleDateFormat(FILENAME_DATE_FORMAT, Locale.ENGLISH).format(new Date()));

        File image = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                filename);

        try {
            if (!image.createNewFile()) {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
        if (!image.delete()) {
            // Something unexpected happened
            return null;
        }

        return image;
    }

    /**
     * Creates a new listener to handle ACTION_IMAGE_CAPTURE Activity result.
     *
     * @return New listener.
     */
    private EventListener<ActivityResultEventData> onNewCapture(final String filename) {
        return new EventListener<ActivityResultEventData>() {
            @Override
            public void onTrigger(Object sender, ActivityResultEventData data) {
                // Result from another Activity
                if (data.getRequestCode() != RESULT_IMAGE_CAPTURE) {
                    return;
                }
                activityResultObserver.setListener(null);

                // Test whether requested Activity was cancelled.
                if (data.getResultCode() != Activity.RESULT_OK) {
                    newCaptureEvent.trigger(this, new ImageCaptureEventData(
                            ImageCaptureResult.Cancelled));
                } else {
                    newCaptureEvent.trigger(this, new ImageCaptureEventData(
                            filename));
                }

            }
        };
    }

    /**
     * Registers a listener for NewCapture event.
     *
     * @param listener The listener.
     */
    public void setNewCaptureListener(
            @Nullable EventListener<ImageCaptureEventData> listener
    ) {
        newCaptureEvent.setListener(listener);
    }

}
