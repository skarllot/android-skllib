package com.github.skarllot.android.skllib;

import android.support.annotation.Nullable;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * Represents a base class for event observers.
 */
public class EventHandler<T extends EventData> implements EventObserver<T> {
    private EventListener<T> listener;

    /**
     * Sets the listener for events of this handler.
     *
     * @param listener The listener.
     */
    @Override
    public void setListener(@Nullable EventListener<T> listener) {
        this.listener = listener;
    }

    /**
     * Triggers a new event calling the listener.
     *
     * @param sender The object who triggered this event.
     * @param data   The data related to current event.
     */
    public void trigger(Object sender, T data) {
        if (listener != null) {
            listener.onTrigger(sender, data);
        }
    }
}
