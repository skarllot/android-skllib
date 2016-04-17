package com.github.skarllot.android.skllib.col;

import com.android.internal.util.Predicate;
import com.github.skarllot.android.skllib.Func2;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * An element filter for List objects.
 */
public class PredicableList<T extends Searchable> {
    protected String filter = null;
    protected Iterable<T> target;

    public PredicableList(Iterable<T> target) {
        if (target == null) {
            throw new NullPointerException("target == null");
        }

        this.target = target;
    }

    /**
     * Creates a new PredicateList instance from non-Searchable type.
     *
     * @param target    The sequence of values for filtering.
     * @param predicate The predicate to test element matching.
     * @param <T>       The type of elements.
     * @return A new PredicateList instance.
     */
    public static <T> PredicableList<Searchable> createFromNonSearchable(
            final Iterable<T> target,
            final FilterPredicate<T> predicate
    ) {
        final Iterable<Searchable> result = Query.select(target, new Func2<Searchable, T>() {
            @Override
            public Searchable call(final T t) {
                return new Searchable() {
                    @Override
                    public boolean match(final String filter) {
                        return predicate.match(filter, t);
                    }
                };
            }
        });

        return new PredicableList<>(result);
    }

    /**
     * Gets the selected elements from target.
     *
     * @return A sequence with selected elements.
     */
    public Iterable<T> getSelection() {
        return Query.where(target, new Predicate<T>() {
            @Override
            public boolean apply(T t) {
                return t.match(filter);
            }
        });
    }

    /**
     * Gets the target which this instance is applying selection.
     *
     * @return A sequence of values.
     */
    public Iterable<T> getTarget() {
        return target;
    }

    /**
     * Selects elements from target where the filter string can be found into its fields.
     *
     * @param filter A string to lookup.
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }

    /**
     * Sets the target elements and refresh selection.
     *
     * @param target A sequence of values.
     */
    public void setTarget(Iterable<T> target) {
        this.target = target;
    }
}
