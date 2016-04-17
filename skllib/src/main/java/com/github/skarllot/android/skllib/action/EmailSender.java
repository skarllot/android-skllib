package com.github.skarllot.android.skllib.action;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.github.skarllot.android.skllib.ActivityResultEventData;
import com.github.skarllot.android.skllib.EventData;
import com.github.skarllot.android.skllib.EventHandler;
import com.github.skarllot.android.skllib.EventListener;
import com.github.skarllot.android.skllib.EventObserver;

import java.util.ArrayList;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * Provides methods for sending emails.
 */
public class EmailSender {
    private final static String EMAIL_MIME_TYPE = "message/rfc822";
    // Activity result code
    private final static int RESULT_EMAIL_SENT = 3598;

    private final Activity activity;
    private final EventObserver<ActivityResultEventData> activityResultObserver;
    private final EventHandler<EventData> emailSentEvent;
    private final Fragment fragment;

    /**
     * Creates a new instance of EmailSender.
     *
     * @param activity               The Activity that this request was originated from.
     * @param activityResultObserver The observer that triggers onActivityResult event.
     */
    public EmailSender(
            Activity activity,
            EventObserver<ActivityResultEventData> activityResultObserver
    ) {
        this.activity = activity;
        this.activityResultObserver = activityResultObserver;
        this.fragment = null;

        this.emailSentEvent = new EventHandler<>();
    }

    /**
     * Creates a new instance of EmailSender.
     *
     * @param fragment               The Fragment that this request was originated from.
     * @param activityResultObserver The observer that triggers onActivityResult event.
     */
    public EmailSender(
            Fragment fragment,
            EventObserver<ActivityResultEventData> activityResultObserver
    ) {
        this.activity = fragment.getActivity();
        this.activityResultObserver = activityResultObserver;
        this.fragment = fragment;

        this.emailSentEvent = new EventHandler<>();
    }

    /**
     * Creates a new Activity to send an e-mail and triggers EmailSent event after sending is
     * complete.
     *
     * @param chooserTitle The title of dialog shown when the user need to choose which application
     *                     to handle current e-mail.
     * @param recipients   List of recipients of current e-mail.
     * @param subject      The subject of current e-mail.
     * @param body         The content of current e-mail.
     * @param attachments  The attachments of current e-mail.
     * @param <T>          Type of attachments.
     * @return True whether the requested Activity could be created; otherwise false.
     */
    public <T extends Parcelable> boolean sendEmail(
            String chooserTitle,
            @Nullable String[] recipients,
            String subject,
            String body,
            @Nullable ArrayList<T> attachments
    ) {
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType(EMAIL_MIME_TYPE);

        // Sets e-mail contents
        if (recipients != null && recipients.length > 0) {
            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        if (attachments != null && attachments.size() > 0) {
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, attachments);
        }

        // Test whether ACTION_SEND_MULTIPLE Activity can be created.
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activityResultObserver.setListener(onCreateEmail());
            final Intent chooser = Intent.createChooser(intent, chooserTitle);
            if (fragment == null) {
                activity.startActivityForResult(chooser, RESULT_EMAIL_SENT);
            } else {
                fragment.startActivityForResult(chooser, RESULT_EMAIL_SENT);
            }
        } else {
            return false;
        }

        return true;
    }

    /**
     * Creates a new listener to handle ACTION_SEND_MULTIPLE Activity result.
     *
     * @return New listener.
     */
    private EventListener<ActivityResultEventData> onCreateEmail() {
        return new EventListener<ActivityResultEventData>() {
            @Override
            public void onTrigger(Object sender, ActivityResultEventData data) {
                // Result from another Activity
                if (data.getRequestCode() != RESULT_EMAIL_SENT) {
                    return;
                }
                activityResultObserver.setListener(null);

                // Always triggers EmailSent event because ACTION_SEND_MULTIPLE Activity do not
                // return a proper result.
                emailSentEvent.trigger(this, new EventData());
            }
        };
    }

    /**
     * Registers a listener for EmailSent event.
     *
     * @param listener The listener.
     */
    public void setEmailSentListener(
            @Nullable EventListener<EventData> listener
    ) {
        emailSentEvent.setListener(listener);
    }
}
