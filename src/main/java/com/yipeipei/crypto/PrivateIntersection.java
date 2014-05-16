package com.yipeipei.crypto;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.BitSet;

/**
 * Created by Peipei YI on 5/14/2014.
 */
public class PrivateIntersection {
    public static final int bitLength = 1024;   //  tuning
    public static final int certainty = 64;     //  tuning
    public static final Paillier paillier = new Paillier(bitLength, certainty);

    public static final int validLength = bitLength - 2;  // highest 2 bits left blank, caused by mod
    public static final int cStep = (int) (validLength / 2.0);
    public static final BitSet bs = new BitSet(validLength);

    public static final int signum = 1; // 1 for positive


    public static final BigInteger TRUE;

    static {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bitLength; i++) {
            if (i % 2 == 0) {
                sb.append(1);   // first bit
            } else {
                sb.append(0);
            }
        }

        TRUE = new BigInteger(sb.toString(), 2);
    }

    //    public static final int magLength = (int) Math.ceil(bitLength/8);
//    public static final byte[] mag = new byte[magLength];     // magnitude in big-endian
    public static final byte[] shift = {0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, (byte) 0x80};

//    public static final byte[] shift = new byte[8];

    //    static {
//        for(int i = 0; i < 8; i++){
//            shift[i] = (byte) (1<<i);
//        }
//    }


    /**
     * This encrypt function takes one label (aka. int[]) a time,
     * and return cipher BigInteger array.
     *
     * @param label a lin or lout label
     * @param C     number of distinguished centers
     * @return
     */
    public static BigInteger[] encrypt(final int[] label, final int C) {
//        System.out.println("========== start encrypt ==========");
//        System.out.println("C: " + C);

        Arrays.sort(label);
        int N = (int) Math.ceil(C * 2.0 / validLength);
        BigInteger[] plain = new BigInteger[N];
        BigInteger[] cipher = new BigInteger[N];

//        System.out.println("nMsg: " + N);
//        System.out.println("cStep: " + cStep);

        long t0 = System.currentTimeMillis();
        int p = 0;
        int count = 0;
        while (count < N) {
            // clear and fill bitset
            bs.clear();
            while (p < label.length && label[p] < (count + 1) * cStep) {
                bs.set(label[p++] % cStep * 2);
            }
            // encrypt
//            plain[count] = new BigInteger(signum, bs.toByteArray());    // here we don't care about endianness
            cipher[count] = paillier.Encryption(new BigInteger(signum, bs.toByteArray()));

//            System.out.println("plain:  " + plain[count].toString(2));
//            System.out.println("cipher: " + cipher[count].toString(2));
//            System.out.println("dectypt:" + paillier.Decryption(cipher[count]).toString(2));

            count++;
        }

//        System.out.println("count: " + count);
//        System.out.println("time elapsed (ms): " + (System.currentTimeMillis() - t0));
//        System.out.println("========== end encrypt ==========");

        return cipher;
    }

    public static BigInteger[] intersection(final BigInteger[] lin, final BigInteger[] lout) {
        BigInteger[] ret = new BigInteger[lin.length];
        for (int i = 0; i < lin.length; i++) {
            ret[i] = lin[i].multiply(lout[i]).mod(paillier.nsquare);
        }
        return ret;
    }

    public static boolean decrypt(final BigInteger[] ret) {
        for (BigInteger b : ret) {
            if (!TRUE.and(paillier.Decryption(b)).equals(BigInteger.ZERO)) {
                return true; // short circuit
            }
        }
        return false;
    }

    public static void main(String[] args) {
        // new byte[] test
        byte[] bArr = new byte[2];
        bArr[0] = 'A'; // 61H -> 0110 0001B
        System.out.println(Integer.toBinaryString(bArr[0]));
        bArr[1] = (byte) ((bArr[0] << 1) >> 1);
        System.out.println(Integer.toBinaryString(1));

        // BitSet test
//        BitSet bs = new BitSet(64);
//        bs.set(2);
//        bs.set(7);
//        bs.set(17);
//        bs.set(23);
//        for(Byte b : bs.toByteArray()){
//            System.out.println(Integer.toBinaryString(b));
//        }

//        System.out.println("byte[] shift to binary");
//        for(int i = 0; i < shift.length; i++){
//            System.out.println("shift[" + i + "] = " + "%8s".format(Integer.toBinaryString(shift[i] & 0xFF)));
//        }

        System.out.println(TRUE.toString(2));
    }
}
