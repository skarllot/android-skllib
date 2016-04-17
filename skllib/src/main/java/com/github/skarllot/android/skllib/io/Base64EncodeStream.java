package com.github.skarllot.android.skllib.io;

import android.support.annotation.NonNull;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Provides a Base64 encoder for a source of bytes.
 */
public class Base64EncodeStream extends FilterInputStream {

    private static final int INPUT_CHUNK_SIZE = 3;
    private static final int OUTPUT_CHUNK_SIZE = 4;

    // Current position in the buffer
    private int position;
    private final byte[] remainderBuffer = new byte[OUTPUT_CHUNK_SIZE];
    private final byte[] singleByteBuffer = new byte[1];
    private byte[] innerBuffer;

    /**
     * Constructs a new {@code FilterInputStream} with the specified input
     * stream as source.
     * <p/>
     * <p><strong>Warning:</strong> passing a null source creates an invalid
     * {@code FilterInputStream}, that fails on every method that is not
     * overridden. Subclasses should check for null in their constructors.
     *
     * @param in the input stream to filter reads on.
     */
    public Base64EncodeStream(InputStream in) {
        super(in);
    }

    @Override
    public void close() throws IOException {
        innerBuffer = null;

        super.close();
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public int read() throws IOException {
        return read(singleByteBuffer, 0, 1);
    }

    @Override
    public int read(@NonNull byte[] buffer, int byteOffset, int byteCount) throws IOException {
        if (byteCount == 0)
            return 0;

        int writtenBytes = 0;
        // If has remaining bytes on buffer
        if (position > 0) {
            int count = OUTPUT_CHUNK_SIZE - position;
            int srcPos = position;
            if (count > byteCount) {
                count = byteCount;
                position = OUTPUT_CHUNK_SIZE - count;
            } else {
                position = 0;
            }

            System.arraycopy(remainderBuffer, srcPos, buffer, byteOffset, count);
            byteOffset += count;
            byteCount -= count;
            writtenBytes += count;
        }

        if (byteCount == 0)
            return writtenBytes;

        int innerCount = (int) Math.ceil(byteCount * 0.75);
        if (innerCount % INPUT_CHUNK_SIZE > 0)
            innerCount += (INPUT_CHUNK_SIZE - (innerCount % INPUT_CHUNK_SIZE));

        if (innerBuffer == null || innerBuffer.length < innerCount)
            innerBuffer = new byte[innerCount];

        final int innerRead = in.read(innerBuffer, 0, innerCount);
        int writePos = byteOffset;
        final int upperBounds = byteOffset + byteCount;
        for (int i = 0; i < innerRead; i += 3) {
            int count = INPUT_CHUNK_SIZE;
            if (i + count > innerRead) {
                count = innerRead - i;
            }

            if (writePos + OUTPUT_CHUNK_SIZE <= upperBounds) {
                Base64Coder.encode3to4(innerBuffer, i, count, buffer, writePos);
                writtenBytes += OUTPUT_CHUNK_SIZE;
                writePos += OUTPUT_CHUNK_SIZE;
            } else {    // End of parameter-provided buffer
                Base64Coder.encode3to4(innerBuffer, i, count, remainderBuffer, 0);
                position = upperBounds - writePos;
                System.arraycopy(remainderBuffer, 0, buffer, writePos, position);
                writtenBytes += position;
                writePos += position;
            }
        }

        return writtenBytes;
    }

    @Override
    public long skip(long byteCount) throws IOException {
        int skipCount = 0;
        if (byteCount > position) {
            byteCount -= position;
            skipCount += position;
            position = 0;
        } else {
            position += byteCount;
            byteCount = 0;
            return byteCount;
        }

        return super.skip((int) (byteCount * 0.75)) + skipCount;
    }
}
