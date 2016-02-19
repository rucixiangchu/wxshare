package com.lwzh.tool.md5Crypt.digest;

import java.util.Random;

public class B64 {


    /**
     * Table with characters for Base64 transformation.
     */
    static final String B64T = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * Base64 like conversion of bytes to ASCII chars.
     *
     * @param b2
     *            A byte from the result.
     * @param b1
     *            A byte from the result.
     * @param b0
     *            A byte from the result.
     * @param outLen
     *            The number of expected output chars.
     * @param buffer
     *            Where the output chars is appended to.
     */
    static void b64from24bit(final byte b2, final byte b1, final byte b0, final int outLen,
                             final StringBuilder buffer) {
        // The bit masking is necessary because the JVM byte type is signed!
        int w = ((b2 << 16) & 0x00ffffff) | ((b1 << 8) & 0x00ffff) | (b0 & 0xff);
        // It's effectively a "for" loop but kept to resemble the original C code.
        int n = outLen;
        while (n-- > 0) {
            buffer.append(B64T.charAt(w & 0x3f));
            w >>= 6;
        }
    }

    /**
     * Generates a string of random chars from the B64T set.
     *
     * @param num
     *            Number of chars to generate.
     */
    static String getRandomSalt(final int num) {
        final StringBuilder saltString = new StringBuilder();
        for (int i = 1; i <= num; i++) {
            saltString.append(B64T.charAt(new Random().nextInt(B64T.length())));
        }
        return saltString.toString();
    }

}
