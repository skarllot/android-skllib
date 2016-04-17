package com.github.skarllot.android.skllib;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * Provides a general purpose function with two parameters and return.
 *
 * @param <R>  The type of result.
 * @param <T1> The type of first parameter.
 * @param <T2> The type of second parameter.
 */
public interface Func3<R, T1, T2> {
    R call(T1 t1, T2 t2);
}
