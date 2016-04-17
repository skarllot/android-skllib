package com.github.skarllot.android.skllib.col;

import android.support.annotation.Nullable;

import com.android.internal.util.Predicate;
import com.github.skarllot.android.skllib.Func2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * Provides querying methods for Iterable objects.
 */
public final class Query {

    /**
     * Returns the element at specified position.
     *
     * @param input    An object that provides a sequence of values.
     * @param position The position of the element to return.
     * @param <T>      The type of elements.
     * @return The requested element.
     * @throws IndexOutOfBoundsException When position is less than zero or greater-or-equal to the
     *                                   number of available elements.
     */
    public static <T> T at(
            final Iterable<T> input,
            final int position
    ) {
        if (input == null) {
            throw new NullPointerException("input == null");
        }
        if (position < 0) {
            throw new IndexOutOfBoundsException();
        }

        if (input instanceof List) {
            return ((List<T>) input).get(position);
        }

        final Iterator<T> iterator = input.iterator();
        int i = 0;
        while (i != position) {
            if (!iterator.hasNext()) {
                throw new IndexOutOfBoundsException();
            }
            iterator.next();
            i++;
        }

        if (!iterator.hasNext()) {
            throw new IndexOutOfBoundsException();
        }
        return iterator.next();
    }

    /**
     * Returns the number of elements found.
     *
     * @param input An object that provides a sequence of values.
     * @param <T>   The type of elements.
     * @return The number of elements.
     */
    public static <T> int count(final Iterable<T> input) {

        int counter = 0;
        for (T ignored : input) {
            counter++;
        }

        return counter;
    }

    /**
     * Returns the difference of two sets.
     *
     * @param input      An object that provides a sequence of values.
     * @param comparee   A sequence of values that must not occur on input.
     * @param comparator A comparator to determine the ordering of two objects.
     * @param <T>        The type of elements.
     * @return An iterable object that returns the differential of elements from input and comparee.
     */
    public static <T> QueryIterable<T> except(
            final Iterable<T> input,
            final Iterable<T> comparee,
            final Comparator<T> comparator
    ) {
        if (input == null) {
            throw new NullPointerException("input == null");
        }
        if (comparee == null) {
            throw new NullPointerException("comparee == null");
        }
        if (comparator == null) {
            throw new NullPointerException("comparator == null");
        }

        return new QueryIterable<>(
                new ExceptIterable<>(input, comparee, comparator));
    }

    /**
     * Determines whether specified element exists into input elements.
     *
     * @param input      An object that provides a sequence of values.
     * @param t          The element to look for.
     * @param comparator A comparator to determine the ordering of two objects.
     * @param <T>        The type of elements.
     * @return True whether element was found; otherwise, false.
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean exists(
            final Iterable<T> input,
            final T t,
            @Nullable Comparator<T> comparator
    ) {
        if (input == null) {
            throw new NullPointerException("input == null");
        }
        if (t == null) {
            throw new NullPointerException("t == null");
        }
        if (comparator == null && !(t instanceof Comparator)) {
            throw new UnsupportedOperationException("A comparator is needed when T do not implements Comparator");
        }

        if (comparator == null) {
            comparator = (Comparator<T>) t;
        }

        for (T item : input) {
            if (comparator.compare(item, t) == 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the first element of a sequence of values.
     *
     * @param input An object that provides a sequence of values.
     * @param <T>   The type of elements.
     * @return The first element if found; otherwise, null.
     */
    @Nullable
    public static <T> T firstOrNull(
            final Iterable<T> input
    ) {
        if (input == null) {
            throw new NullPointerException("input == null");
        }

        final Iterator<T> iterator = input.iterator();
        if (iterator.hasNext())
            return iterator.next();
        else
            return null;
    }

    /**
     * Returns an iterable object that can be read once.
     *
     * @param input An iterator that provides a sequence of values.
     * @param <T>   The type of elements.
     * @return An iterable object that returns a sequence of values.
     */
    public static <T> QueryIterable<T> once(
            final Iterator<T> input
    ) {
        if (input == null) {
            throw new NullPointerException("input == null");
        }

        return new QueryIterable<>(new OnceIterable<>(input));
    }

    /**
     * Projects the elements into a new type.
     *
     * @param input    An object that provides a sequence of values.
     * @param selector An method to transform elements.
     * @param <T>      The type of elements.
     * @param <R>      The type of projected elements.
     * @return An iterable object that returns projected elements from input.
     */
    public static <T, R> QueryIterable<R> select(
            final Iterable<T> input,
            final Func2<R, T> selector
    ) {
        if (input == null) {
            throw new NullPointerException("input == null");
        }
        if (selector == null) {
            throw new NullPointerException("selector == null");
        }

        return new QueryIterable<>(
                new SelectIterable<>(input, selector));
    }

    /**
     * Returns a list from iterable object.
     *
     * @param input An object that provides a sequence of values.
     * @param <T>   The type of elements.
     * @return A list from found elements.
     */
    public static <T> List<T> toList(final Iterable<T> input) {
        if (input == null) {
            throw new NullPointerException("input == null");
        }

        List<T> result = new ArrayList<>();
        for (T item : input) {
            result.add(item);
        }

        return result;
    }

    /**
     * Filters the elements from iterable object.
     *
     * @param input     An object that provides a sequence of values.
     * @param predicate An method to determine whether an element meets a condition.
     * @param <T>       The type of elements.
     * @return An iterable object that returns filtered elements from input.
     */
    public static <T> QueryIterable<T> where(
            final Iterable<T> input,
            final Func2<Boolean, T> predicate
    ) {
        if (input == null) {
            throw new NullPointerException("input == null");
        }
        if (predicate == null) {
            throw new NullPointerException("predicate == null");
        }

        return new QueryIterable<>(
                new WhereIterable<>(input, predicate));
    }

    /**
     * Filters the elements from iterable object.
     *
     * @param input     An object that provides a sequence of values.
     * @param predicate An method to determine whether an element meets a condition.
     * @param <T>       The type of elements.
     * @return An iterable object that returns filtered elements from input.
     */
    public static <T> QueryIterable<T> where(
            final Iterable<T> input,
            final Predicate<T> predicate
    ) {
        if (predicate == null) {
            throw new NullPointerException("predicate == null");
        }

        return where(input, new Func2<Boolean, T>() {
            @Override
            public Boolean call(T t) {
                return predicate.apply(t);
            }
        });
    }

    /**
     * An Iterable type for except loops.
     *
     * @param <T> The type of elements.
     */
    private static class ExceptIterable<T> implements Iterable<T> {
        final Iterable<T> iterable;
        final Iterable<T> comparee;
        final Comparator<T> comparator;

        public ExceptIterable(
                Iterable<T> iterable,
                Iterable<T> comparee,
                Comparator<T> comparator
        ) {
            this.iterable = iterable;
            this.comparee = comparee;
            this.comparator = comparator;
        }

        @Override
        public Iterator<T> iterator() {
            return new ExceptIterator<>(iterable.iterator(), comparee, comparator);
        }
    }

    /**
     * An iterator for except loops.
     *
     * @param <T> The type of elements
     */
    private static class ExceptIterator<T> implements Iterator<T> {
        final Iterator<T> iterator;
        final Iterable<T> comparee;
        final Comparator<T> comparator;
        T nextItem = null;

        public ExceptIterator(
                Iterator<T> iterator,
                Iterable<T> comparee,
                Comparator<T> comparator
        ) {
            this.iterator = iterator;
            this.comparee = comparee;
            this.comparator = comparator;
        }

        @Override
        public boolean hasNext() {
            if (nextItem != null)
                return true;

            while (iterator.hasNext()) {
                final T item = iterator.next();

                if (!Query.exists(comparee, item, comparator)) {
                    nextItem = item;
                    return true;
                }
            }

            return false;
        }

        @Override
        public T next() throws NoSuchElementException {
            if (nextItem != null) {
                final T item = nextItem;
                nextItem = null;
                return item;
            }

            while (iterator.hasNext()) {
                final T item = iterator.next();
                if (!Query.exists(comparee, item, comparator))
                    return item;
            }

            throw new NoSuchElementException();
        }

        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * An Iterable type for one-time loops.
     *
     * @param <T> The type of elements.
     */
    private static class OnceIterable<T> implements Iterable<T> {
        Iterator<T> iterator;

        public OnceIterable(
                Iterator<T> iterator
        ) {
            this.iterator = iterator;
        }

        @Override
        public Iterator<T> iterator() {
            final Iterator<T> copy = iterator;
            iterator = null;
            return copy;
        }
    }

    /**
     * An Iterable type for projection loops.
     *
     * @param <T> The type of elements.
     * @param <R> The type of projected elements.
     */
    private static class SelectIterable<T, R> implements Iterable<R> {
        final Iterable<T> iterable;
        final Func2<R, T> selector;

        public SelectIterable(Iterable<T> iterable, Func2<R, T> selector) {
            this.iterable = iterable;
            this.selector = selector;
        }

        @Override
        public Iterator<R> iterator() {
            return new SelectIterator<>(iterable.iterator(), selector);
        }
    }

    /**
     * An iterator for projection loops.
     *
     * @param <T> The type of elements
     * @param <R> The type of projected elements.
     */
    private static class SelectIterator<T, R> implements Iterator<R> {
        final Iterator<T> iterator;
        final Func2<R, T> selector;

        public SelectIterator(Iterator<T> iterator, Func2<R, T> selector) {
            this.iterator = iterator;
            this.selector = selector;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public R next() throws NoSuchElementException {
            if (iterator.hasNext()) {
                return selector.call(iterator.next());
            }

            throw new NoSuchElementException();
        }

        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * An Iterable type for filtered loops.
     *
     * @param <T> The type of elements.
     */
    private static class WhereIterable<T> implements Iterable<T> {
        final Iterable<T> iterable;
        final Func2<Boolean, T> predicate;

        public WhereIterable(Iterable<T> iterable, Func2<Boolean, T> predicate) {
            this.iterable = iterable;
            this.predicate = predicate;
        }

        @Override
        public Iterator<T> iterator() {
            return new WhereIterator<>(iterable.iterator(), predicate);
        }
    }

    /**
     * An iterator for filtered loops.
     *
     * @param <T> The type of elements
     */
    private static class WhereIterator<T> implements Iterator<T> {
        final Iterator<T> iterator;
        final Func2<Boolean, T> predicate;
        T nextItem = null;

        public WhereIterator(Iterator<T> iterator, Func2<Boolean, T> predicate) {
            this.iterator = iterator;
            this.predicate = predicate;
        }

        @Override
        public boolean hasNext() {
            if (nextItem != null)
                return true;

            while (iterator.hasNext()) {
                final T item = iterator.next();

                if (predicate.call(item)) {
                    nextItem = item;
                    return true;
                }
            }

            return false;
        }

        @Override
        public T next() throws NoSuchElementException {
            if (nextItem != null) {
                final T item = nextItem;
                nextItem = null;
                return item;
            }

            while (iterator.hasNext()) {
                final T item = iterator.next();
                if (predicate.call(item))
                    return item;
            }

            throw new NoSuchElementException();
        }

        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }
    }
}
