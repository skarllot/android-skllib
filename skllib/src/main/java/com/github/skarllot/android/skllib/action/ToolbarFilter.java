package com.github.skarllot.android.skllib.action;

import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.skarllot.android.skllib.Disposable;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * Provides filtering on toolbar for current view.
 */
public class ToolbarFilter implements Disposable {
    private final ActionBar actionBar;
    private final Spinner actionBarSpinner;
    private final ArrayAdapter adapter;
    private FilterableListener itemSelectedListener;

    /**
     * Creates a new instance of ToolbarFilter.
     *
     * @param actionBar        The action bar from current view.
     * @param actionBarSpinner The action bar spinner used as filter selector.
     * @param adapter          The adapter to provide data to Spinner.
     */
    public ToolbarFilter(
            ActionBar actionBar,
            Spinner actionBarSpinner,
            ArrayAdapter adapter
    ) {
        this.actionBar = actionBar;
        this.actionBarSpinner = actionBarSpinner;
        this.adapter = adapter;

        actionBar.setDisplayShowTitleEnabled(false);
        // Sets up the action bar spinner for filtering
        actionBarSpinner.setAdapter(adapter);
        actionBarSpinner.setVisibility(View.VISIBLE);

        final FilterHandler filterHandler = new FilterHandler();
        actionBarSpinner.setOnItemSelectedListener(filterHandler);
    }

    @Override
    public void dispose() {
        // Rollback changes made on toolbar objects
        actionBarSpinner.setVisibility(View.GONE);
        actionBarSpinner.setAdapter(null);
        actionBar.setDisplayShowTitleEnabled(true);
        adapter.clear();
    }

    /**
     * Gets the data adapter used to filter.
     *
     * @return The data adapter.
     */
    public ArrayAdapter getAdapter() {
        return adapter;
    }

    /**
     * Gets the current selected item from Spinner.
     *
     * @return The selected item, or null.
     */
    public Object getSelectedItem() {
        return actionBarSpinner.getSelectedItem();
    }

    /**
     * Register a callback to be invoked when an item in this ToolbarFilter has
     * been selected.
     *
     * @param listener The callback.
     */
    public void setOnItemSelectedListener(
            @Nullable FilterableListener listener
    ) {
        itemSelectedListener = listener;
    }

    /**
     * Defines a type to handle filter events.
     */
    private class FilterHandler implements FilterableListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (itemSelectedListener != null) {
                itemSelectedListener.onItemSelected(parent, view, position, id);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            if (itemSelectedListener != null) {
                itemSelectedListener.onNothingSelected(parent);
            }
        }
    }
}
