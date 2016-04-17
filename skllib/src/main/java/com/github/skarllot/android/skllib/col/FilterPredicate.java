package com.github.skarllot.android.skllib.col;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * A FilterPredicate can determine whether an item meets a specific filter string.
 */
public interface FilterPredicate<T> {
    /**
     * Determines whether an item meets a specific filter string.
     *
     * @param filter A string to lookup.
     * @param item   A target for searching.
     * @return True whether filter string could be found anywhere into item instance. Otherwise, false.
     */
    boolean match(String filter, T item);
}
