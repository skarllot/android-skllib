package com.github.skarllot.android.skllib.service;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * Define the expected connection status broadcast by NsProberService.
 */
public interface ConnectionStatus {
    /**
     * The connection status could not yet be determined.
     */
    int UNDEFINED = 0;

    /**
     * The connection probe was successful.
     */
    int CONNECTED = 1;

    /**
     * The connection probe failed.
     */
    int NO_CONNECTION = 2;

    /**
     * Is connected but a login or a proxy is required.
     */
    int INVALID_CONNECTION = 3;
}
