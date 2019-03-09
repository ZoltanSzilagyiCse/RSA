package com.zoltan.szilagyi.cse.rsa.api;

import com.zoltan.szilagyi.cse.rsa.log.Logger;

import java.util.Random;

public class MillerRabinTest {

    private Logger logger;
    private FastPower fastPower;
    private ExtendedEuclideanAlgorithm extendedEuclideanAlgorithm;
    private Random random;

    public MillerRabinTest(Logger logger, FastPower fastPower, ExtendedEuclideanAlgorithm extendedEuclideanAlgorithm, Random random) {
        this.logger = logger;
        this.fastPower = fastPower;
        this.extendedEuclideanAlgorithm = extendedEuclideanAlgorithm;
        this.random = random;
    }

    /**
     * Checks number is prime
     */
    public boolean isPrime(double p) {
        if (isComplexNumber(p)) {
            return false;
        }
        double S = calculateS(p);
        double d = calculateD(p, S);
        boolean[] results = primeTests(p, S, d);

        return checkResults(results);
    }

    /**
     * Run prime tests <i>10</i> times
     */
    private boolean[] primeTests(double p, double s, double d) {
        boolean[] results = new boolean[10];
        for (int i = 0; i < 10; i++) {
            double a = generateA(p, random);
            for (int r = 0; r < s; r++) {
                double tmp = fastPower.calculate(a, (long) (d * Math.pow(2, r)), p);
                if (tmp == 1 || tmp == (p - 1)) {
                    results[i] = true;
                    break;
                }
            }
        }
        return results;
    }

    /**
     * Calculate <i>d</i>
     */
    private double calculateD(double p, double s) {
        return (p - 1) / (Math.pow(2, s));
    }

    /**
     * Calculate <i>S</i>
     * Divide <i>p</i> with <i>2</i> until the result is odd
     */
    private double calculateS(double p) {
        int s = 1;
        while ((((p - 1) / Math.pow(2, s)) % 2) == 0d) {
             s++;
        }
        return s;
    }

    /**
     * Generates a number which is relative prime to <i>p</i>
     */
    private double generateA(double p, Random random) {
        double a = generateNumber(p, random);
        while (!extendedEuclideanAlgorithm.isRelativePrime(p, a)) {
            a = (generateNumber(p, random));
        }
        return a;
    }

    /**
     * Generates a random number between 2 and <i>p-1</i>
     */
    private double generateNumber(double p, Random random) {
        return Math.abs(random.nextLong()) % (p - 1L - 2L) + 2L;
    }

    /**
     * Check the results
     */
    private boolean checkResults(boolean[] results) {
        int k = 0;
        for (int i = 0; i < 10; i++) {
            if (results[i]) {
                k++;
            }
            if (k >= 9) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks number is complex number
     */
    private boolean isComplexNumber(double p) {
        return (p % 2) == 0 || (p % 3) == 0 || (p % 4) == 0 || (p % 5) == 0 || (p % 7) == 0 || (p % 10) == 0;
    }
}
