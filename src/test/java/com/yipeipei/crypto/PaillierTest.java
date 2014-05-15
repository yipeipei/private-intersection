package com.yipeipei.crypto;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by Peipei YI on 5/15/2014.
 */
public class PaillierTest {
    int times = 10000;

    int bitLength = 1024;
    int certainty = 64;
    Paillier paillier = new Paillier(bitLength, certainty);

    @Test
    public void testCorrectness() throws Exception {
        for(int i = 0; i < times; i++){
            BigInteger plain = new BigInteger(bitLength -2, new Random(System.currentTimeMillis()));
            BigInteger cipher;
            BigInteger decrypt;

            cipher = paillier.Encryption(plain);
            decrypt = paillier.Decryption(cipher);

//            System.out.println("bitLength:" + bitLength);
//            System.out.println("plain:  " + plain.toString(2));
//            System.out.println("cipher: " + cipher.toString(2));
//            System.out.println("decrypt:" + decrypt.toString(2));

            System.out.println(plain.equals(decrypt));
            assert plain.equals(decrypt);
            System.out.println(i);
        }
    }

    @Test
    public void testEncryption() throws Exception {
        System.out.println("========== benchmark encrypt ==========");
        System.out.println("Paillier bitLength=" + bitLength + " certainty=" + certainty);
        System.out.println("loop " + times + " times");

        BigInteger plain = new BigInteger(bitLength -2, new Random(System.currentTimeMillis()));
        BigInteger cipher;

        long t0 = System.currentTimeMillis();
        for(int i = 0; i < times; i++){
            cipher = paillier.Encryption(plain);
        }
        t0 = System.currentTimeMillis() - t0;

        System.out.println("average encrypt time (ms): " + t0/times);
    }

    @Test
    public void testDecryption() throws Exception {
        System.out.println("========== benchmark decrypt ==========");
        System.out.println("Paillier bitLength=" + bitLength + " certainty=" + certainty);
        System.out.println("loop " + times + " times");

        BigInteger plain = new BigInteger(bitLength -2, new Random(System.currentTimeMillis()));
        BigInteger cipher = paillier.Encryption(plain);

        long t0 = System.currentTimeMillis();
        for(int i=0; i < times; i++){
            plain = paillier.Decryption(cipher);
        }
        t0 = System.currentTimeMillis() - t0;

        System.out.println("average decrypt time (ms): " + t0/times);
    }
}
