package com.github.skarllot.android.skllib;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * Defines a object that receive events from a observer.
 */
public interface EventListener<T> {
    /**
     * Defines the method called when a new event is triggered.
     *
     * @param sender The sender who triggered current event.
     * @param data   The data related to current event.
     */
    void onTrigger(Object sender, T data);
}
