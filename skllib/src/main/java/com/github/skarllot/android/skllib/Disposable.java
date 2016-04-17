package com.github.skarllot.android.skllib;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * Represents objects that allocate resources references.
 */
public interface Disposable {
    /**
     * Releases resources references.
     */
    void dispose();
}
