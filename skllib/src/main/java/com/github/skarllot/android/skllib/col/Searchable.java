package com.github.skarllot.android.skllib.col;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * Represents an object that allows searching.
 */
public interface Searchable {
    /**
     * Determines whether current instance meets a specific filter string.
     *
     * @param filter A text to lookup.
     * @return True whether filter text could be found anywhere into current instance. Otherwise, false.
     */
    boolean match(String filter);
}
