package com.yipeipei.crypto;

import org.junit.Test;

import java.math.BigInteger;

/**
 * Created by Peipei YI on 5/15/2014.
 */
public class PrivateIntersectionTest {
    // for 1k, 10k, 100k, 1m
    int[] centers = {(int) 1e3, (int) 1e4, (int) 1e5, (int) 1e6};

    @Test
    public void testEncryptCorrectness() throws Exception {
        // 3 2 1 0      5 4        8     1312
        // plain:  1010101 00000101 00000001 00000101
        // dectypt:1010101 00000101 00000001 00000101

        int C = 513;    // two round
        int[] label = {0, 1, 2, 3, 4, 5, 8, 12, 13, 511, 512}; // two round
        PrivateIntersection.encrypt(label, C);
    }

    @Test
    public void testEncryptBenchmark() throws Exception {
        for (int c : centers) {
            int[] label = new int[c];
            for (int i = 0; i < c; i++) {
                label[i] = i;
            }

            long t0 = System.currentTimeMillis();
            BigInteger[] cipher = PrivateIntersection.encrypt(label, c);
            t0 = System.currentTimeMillis() - t0;

            System.out.println("10^" + (int) Math.log10(c) + " centers ");
            System.out.println(cipher.length + " rounds total");
            System.out.println("time elapsed to encrypt (ms): " + t0);
            System.out.println("time estimated for all labels:");
            System.out.println("\t10% * " + c * 10 + " : " + t0 * 2 * c * 10 / 1000 / 60 + "min");
            System.out.println("\t50% * " + c * 2 + " : " + t0 * 2 * c * 2 / 1000 / 60 + "min");
            System.out.println();
        }

    }

    @Test
    public void testIntersection() throws Exception {
        for (int c : centers) {
            int[] lin = {0, c - 1};
            int[] lout = {1, c - 1};

            BigInteger[] lins = PrivateIntersection.encrypt(lin, c);
            BigInteger[] louts = PrivateIntersection.encrypt(lout, c);
            BigInteger[] ret = null;

            long t0 = System.currentTimeMillis();
            for (int i = 0; i < 1e7 / c; i++) {
                ret = PrivateIntersection.intersection(lins, louts);
            }
            t0 = System.currentTimeMillis() - t0;
            System.out.println("c:" + c);
            System.out.println("len(ret): " + ret.length);
            System.out.println("time elapsed (ms):" + t0 / (1e7 / c));
        }
    }

    @Test
    public void testDecrypt() throws Exception {
        for (int c : centers) {
            int[] lin = {0, c - 1};
            int[] lout = {1, c - 1};

            BigInteger[] lins = PrivateIntersection.encrypt(lin, c);
            BigInteger[] louts = PrivateIntersection.encrypt(lout, c);
            BigInteger[] ret = PrivateIntersection.intersection(lins, louts);

            long t0 = System.currentTimeMillis();
            for (int i = 0; i < 1e6 / c; i++) {
                PrivateIntersection.decrypt(ret);
            }
            t0 = System.currentTimeMillis() - t0;
            System.out.println("c:" + c);
            System.out.println("len(ret): " + ret.length);
            System.out.println("time elapsed (ms):" + t0 / (1e6 / c));
        }
    }
}
