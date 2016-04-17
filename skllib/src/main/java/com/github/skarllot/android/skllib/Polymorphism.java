package com.github.skarllot.android.skllib;

import java.util.ArrayList;
import java.util.List;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * Provides some common polymorphism methods.
 */
public final class Polymorphism {
    /**
     * Tries to cast specified object to specified type.
     *
     * @param t   The class to cast.
     * @param o   The object to cast.
     * @param <T> The type to cast.
     * @return The object cast to specified type; otherwise null.
     */
    public static <T> T tryCast(Class<T> t, Object o) {
        return t.isInstance(o) ? t.cast(o) : null;
    }

    /**
     * Cast a list elements to its super type.
     *
     * @param list The list whose its elements will be casted to super type.
     * @param <R>  The super type.
     * @param <P>  The child type
     * @return A new list whose its elements is a super type from specified list.
     */
    public static <R, P extends R> List<R> castList(List<P> list) {
        if (list == null) {
            return new ArrayList<>();
        }

        final List<R> castList = new ArrayList<>(list.size());
        for (P item : list) {
            castList.add(item);
        }

        return castList;
    }

    /**
     * Cast a list elements to its super type.
     *
     * @param list The list whose its elements will be casted to super type.
     * @param t    The Class from returned element type.
     * @param <R>  The super type.
     * @param <P>  The child type
     * @return A new list whose its elements is a super type from specified list.
     */
    public static <R, P extends R> List<R> castList(List<P> list, Class<R> t) {
        if (t == null) {
            throw new TypeNotPresentException("R", null);
        }

        return Polymorphism.castList(list);
    }
}
