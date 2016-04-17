package com.github.skarllot.android.skllib.col;

import android.support.annotation.Nullable;

import com.android.internal.util.Predicate;
import com.github.skarllot.android.skllib.Func2;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * Provides a sequence of values that queries can be chained.
 *
 * @param <T> The type of elements
 */
public class QueryIterable<T> implements Iterable<T> {
    private final Iterable<T> iterable;

    public QueryIterable(final Iterable<T> iterable) {
        this.iterable = iterable;
    }

    /**
     * Returns the element at specified position.
     *
     * @param position The position of the element to return.
     * @return The requested element.
     * @throws IndexOutOfBoundsException When position is less than zero or greater-or-equal to the
     *                                   number of available elements.
     */
    public T at(final int position) {
        return Query.at(iterable, position);
    }

    /**
     * Returns the number of elements found.
     *
     * @return The number of elements.
     */
    public int count() {
        return Query.count(iterable);
    }

    /**
     * Returns the difference of two sets.
     *
     * @param comparee   A sequence of values that must not occur on input.
     * @param comparator A comparator to determine the ordering of two objects.
     * @return An iterable object that returns the differential of elements from input and comparee.
     */
    public QueryIterable<T> except(
            final Iterable<T> comparee,
            final Comparator<T> comparator
    ) {
        return Query.except(iterable, comparee, comparator);
    }

    /**
     * Determines whether specified element exists into input elements.
     *
     * @param t          The element to look for.
     * @param comparator A comparator to determine the ordering of two objects.
     * @return True whether element was found; otherwise, false.
     */
    public boolean exists(
            final T t,
            @Nullable final Comparator<T> comparator
    ) {
        return Query.exists(iterable, t, comparator);
    }

    @Override
    public Iterator<T> iterator() {
        return iterable.iterator();
    }

    /**
     * Returns the first element of a sequence of values.
     *
     * @return The first element if found; otherwise, null.
     */
    @Nullable
    public T firstOrNull() {
        return Query.firstOrNull(iterable);
    }

    /**
     * Projects the elements into a new type.
     *
     * @param selector An method to transform elements.
     * @param <R>      The type of projected elements.
     * @return An iterable object that returns projected elements from input.
     */
    public <R> QueryIterable<R> select(
            final Func2<R, T> selector
    ) {
        return Query.select(iterable, selector);
    }

    /**
     * Returns a list from iterable object.
     *
     * @return A list from found elements.
     */
    public List<T> toList() {
        return Query.toList(iterable);
    }

    /**
     * Filters the elements from iterable object.
     *
     * @param predicate An method to determine whether an element meets a condition.
     * @return An iterable object that returns filtered elements from input.
     */
    public QueryIterable<T> where(
            final Func2<Boolean, T> predicate
    ) {
        return Query.where(iterable, predicate);
    }

    /**
     * Filters the elements from iterable object.
     *
     * @param predicate An method to determine whether an element meets a condition.
     * @return An iterable object that returns filtered elements from input.
     */
    public QueryIterable<T> where(
            final Predicate<T> predicate
    ) {
        return Query.where(iterable, predicate);
    }
}
