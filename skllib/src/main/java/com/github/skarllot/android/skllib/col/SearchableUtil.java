package com.github.skarllot.android.skllib.col;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * Provides common methods for Searchable.
 */
public final class SearchableUtil {
    /**
     * Determines whether any element from iterable matches the specified filter.
     *
     * @param iterable An object with iterable elements.
     * @param filter   A text to lookup.
     * @param <T>      The type of iterable elements.
     * @return True whether any iterable element match the specified filter. Otherwise, false.
     */
    public static <T extends Searchable> boolean matchAny(Iterable<T> iterable, String filter) {
        if (iterable == null)
            return false;

        for (T item : iterable) {
            if (item.match(filter))
                return true;
        }

        return false;
    }
}
