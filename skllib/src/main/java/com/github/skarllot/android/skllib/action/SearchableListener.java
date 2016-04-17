package com.github.skarllot.android.skllib.action;

import android.support.v7.widget.SearchView;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * Defines a listener for search events from SearchView.
 */
public interface SearchableListener extends
        SearchView.OnQueryTextListener,
        SearchView.OnClickListener,
        SearchView.OnCloseListener {
}
