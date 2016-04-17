package com.github.skarllot.android.skllib.io;

import android.annotation.TargetApi;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Base64OutputStream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// Authors:
//  Fabrício Godoy <skarllot@gmail.com>, 2015.

/**
 * Provides Input/Output operations.
 */
@TargetApi(19)
public final class IO {
    private static final int BUFFER_SIZE = 8192;

    /**
     * Copies specified source file to dest file.
     *
     * @param source The file that provides content.
     * @param dest   The file where content will be written.
     * @return True whether copy was done successfully; otherwise, false.
     */
    public static boolean copy(File source, File dest) {
        final byte[] buffer = new byte[BUFFER_SIZE];

        try (InputStream r = new FileInputStream(source);
             OutputStream w = new FileOutputStream(dest)) {
            int read;
            while ((read = r.read(buffer)) != -1) {
                w.write(buffer, 0, read);
            }
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    /**
     * Reads the specified file to the memory.
     *
     * @param file         The file to read.
     * @param encodeBase64 Defines whether should encode to Base-64.
     * @return The file as byte array.
     */
    @Nullable
    public static byte[] readFile(File file, boolean encodeBase64) {
        final ByteArrayOutputStream w = new ByteArrayOutputStream((int) file.length());
        final OutputStream middleware;
        if (encodeBase64) {
            middleware = new Base64OutputStream(w, Base64.NO_WRAP);
        } else {
            middleware = w;
        }

        final byte[] buffer = new byte[BUFFER_SIZE];
        try (InputStream r = new FileInputStream(file)) {
            int read;
            while ((read = r.read(buffer)) != -1) {
                middleware.write(buffer, 0, read);
            }
        } catch (IOException e) {
            return null;
        }

        final byte[] result = w.toByteArray();
        try {
            middleware.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Writes to specified file the content of a readable source.
     *
     * @param reader The readable source.
     * @param dest   The file where the content will be written.
     * @return True whether copy was done successfully; otherwise, false.
     */
    public static boolean writeToFile(InputStream reader, File dest) {
        final byte[] buffer = new byte[BUFFER_SIZE];
        try (OutputStream writer = new FileOutputStream(dest)) {
            int read;
            while ((read = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, read);
            }
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}
