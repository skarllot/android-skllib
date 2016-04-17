package com.github.skarllot.android.skllib;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * Provides a general purpose function with one parameter and return.
 *
 * @param <R> The type of result.
 * @param <T> The type of parameter.
 */
public interface Func2<R, T> {
    R call(T t);
}
