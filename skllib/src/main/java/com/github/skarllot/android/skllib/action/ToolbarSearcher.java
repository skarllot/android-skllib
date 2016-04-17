package com.github.skarllot.android.skllib.action;

import android.app.SearchManager;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * Provides searching on toolbar for current view.
 */
public class ToolbarSearcher {
    private final AppCompatActivity activity;
    private SearchableListener clientSearchableListener;
    private final SearchableListener searchableListener;
    private final
    @IdRes
    int toolbarMenuSearch;

    public ToolbarSearcher(
            AppCompatActivity activity,
            @IdRes int toolbarMenuSearch
    ) {
        this.activity = activity;
        this.toolbarMenuSearch = toolbarMenuSearch;

        searchableListener = new SearchHandler();
    }

    /**
     * Sets up the search item of the Activity's standard options menu.
     *
     * @param menu The options menu in which you place your items.
     */
    public void onCreateOptionsMenu(Menu menu) {
        SearchView searchView =
                (SearchView) menu.findItem(toolbarMenuSearch).getActionView();
        SearchManager searchManager =
                (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(activity.getComponentName()));
        searchView.setOnQueryTextListener(searchableListener);
        searchView.setOnCloseListener(searchableListener);
        searchView.setOnSearchClickListener(searchableListener);
    }

    /**
     * Register a callback to be invoked when an event in this ToolbarSearcher
     * has been selected.
     *
     * @param listener The callback.
     */
    public void setSearchableListener(
            @Nullable SearchableListener listener
    ) {
        clientSearchableListener = listener;
    }

    /**
     * Defines a type to handle search events.
     */
    private class SearchHandler implements SearchableListener {

        public SearchHandler() {
        }

        @Override
        public void onClick(View v) {
            //drawerToggle.setDrawerIndicatorEnabled(false);
            //actionBar.setDisplayHomeAsUpEnabled(true);

            if (clientSearchableListener != null) {
                clientSearchableListener.onClick(v);
            }
        }

        @Override
        public boolean onClose() {
            //actionBar.setDisplayHomeAsUpEnabled(false);
            //drawerToggle.setDrawerIndicatorEnabled(true);

            return clientSearchableListener != null &&
                    clientSearchableListener.onClose();
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            return clientSearchableListener != null &&
                    clientSearchableListener.onQueryTextSubmit(query);
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return clientSearchableListener != null &&
                    clientSearchableListener.onQueryTextChange(newText);
        }
    }
}
