package com.zoltan.szilagyi.cse.rsa.api;

import com.zoltan.szilagyi.cse.rsa.log.Logger;

import java.util.Map;
import java.util.Random;

public class RSA {

    private Logger logger;
    private FastPower fastPower;
    private ChineseRemainderTheorem chineseRemainderTheorem;
    private ExtendedEuclideanAlgorithm extendedEuclideanAlgorithm;
    private Random random;

    public RSA(Logger logger, FastPower fastPower, ChineseRemainderTheorem chineseRemainderTheorem, ExtendedEuclideanAlgorithm extendedEuclideanAlgorithm, Random random) {
        this.logger = logger;
        this.fastPower = fastPower;
        this.chineseRemainderTheorem = chineseRemainderTheorem;
        this.extendedEuclideanAlgorithm = extendedEuclideanAlgorithm;
        this.random = random;
    }

    /**
     * Encode plain message
     */
    public double[] encode(int[] textBlock, double e, double n) {
        double[] encrypted = new double[textBlock.length];
        for (int i = 0; i < textBlock.length; i++) {
            encrypted[i] = fastPower.calculate(textBlock[i], (long) e, n);
        }
        return encrypted;
    }

    /**
     * Decode encoded message
     */
    public int[] decode(double p, double q, double[] encoded, double d) {
        int[] decoded = new int[encoded.length];
        for (int i = 0; i < encoded.length; i++) {
            decoded[i] = chineseRemainderTheorem.calculate(p, q, encoded[i], d);
        }
        return decoded;
    }

    /**
     * Generates a public key
     */
    public double publicKey(double fin) {
        double e = (Math.abs(random.nextLong()) % (fin / 2) + 2L);
        while ((extendedEuclideanAlgorithm.gcd(fin, e) != 1) && (e > 1) && (e < fin)) {
            e = (long) (Math.abs(random.nextLong()) % (fin / 2) + 2L);
        }
        return e;
    }

    /**
     * Generates a private key
     */
    public double privateKey(double fin, double e) {
        final Map<String, Double> kea = extendedEuclideanAlgorithm.calculate(fin, e);
        double d = (Math.pow((-1), kea.get(ExtendedEuclideanAlgorithm.KEY_K) - 1) * kea.get(ExtendedEuclideanAlgorithm.KEY_YK));
        while (d < 0) {
            d += fin;
        }
        return d;
    }
}
