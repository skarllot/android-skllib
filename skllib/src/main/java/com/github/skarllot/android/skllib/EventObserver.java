package com.github.skarllot.android.skllib;

import android.support.annotation.Nullable;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * Represents an interface for event observers.
 */
public interface EventObserver<T extends EventData> {
    /**
     * Sets the listener for events of this handler.
     *
     * @param listener The listener.
     */
    void setListener(@Nullable EventListener<T> listener);
}
