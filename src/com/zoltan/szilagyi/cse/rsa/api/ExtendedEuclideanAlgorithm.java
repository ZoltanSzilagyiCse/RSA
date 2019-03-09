package com.zoltan.szilagyi.cse.rsa.api;

import com.zoltan.szilagyi.cse.rsa.log.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtendedEuclideanAlgorithm {

    static final String KEY_RK = "rk";
    static final String KEY_QK = "qk";
    static final String KEY_XK = "xk";
    static final String KEY_YK = "yk";
    static final String KEY_K = "k";

    private Logger logger;

    public ExtendedEuclideanAlgorithm(Logger logger) {
        this.logger = logger;
    }

    Map<String, Double> calculate(final double a, final double b) {
        Map<String, Double> map = new HashMap<>();
        List<Double> qk = new ArrayList<>();
        List<Double> rk = new ArrayList<>();
        List<Double> xk = new ArrayList<>();
        List<Double> yk = new ArrayList<>();

        // Alapértelmezett értékek feltöltése
        qk.add(a);
        qk.add(b);
        rk.add(-1d);
        xk.add(1d);
        xk.add(0d);
        yk.add(0d);
        yk.add(1d);

        calculateQkRk(qk, rk);
        int k = calculateXkYkAndK(rk, xk, yk);

        // Kibővített euklideszi algoritmus utolsó (nem 0) oszlopának elmentése
        map.put(KEY_QK, qk.get(qk.size() - 2));
        map.put(KEY_RK, rk.get(rk.size() - 1));
        map.put(KEY_XK, xk.get(xk.size() - 1));
        map.put(KEY_YK, yk.get(yk.size() - 1));
        map.put(KEY_K, (double) k);
        return map;
    }

    /**
     * Calculates x<sub>k</sub> and y<sub>k</sub> and k
     *
     * @return k
     */
    private int calculateXkYkAndK(List<Double> rk, List<Double> xk, List<Double> yk) {
        int k = 1;
        while (k < rk.size() - 1) {
            xk.add(xk.get(xk.size() - 1) * rk.get(k) + xk.get(xk.size() - 2));
            yk.add(yk.get(yk.size() - 1) * rk.get(k) + yk.get(yk.size() - 2));
            k++;
        }
        return k;
    }

    /**
     * Calculates q<sub>k</sub> and r<sub>k</sub>
     */
    private void calculateQkRk(List<Double> qk, List<Double> rk) {
        while ((qk.lastIndexOf(0d) == -1)) {
            Double qk2 = qk.get(qk.size() - 2);
            Double qk1 = qk.get(qk.size() - 1);
            rk.add(Math.floor(qk2 / qk1));
            qk.add(qk2 % qk1);
        }
    }

    /**
     * Greatest Common Divisor (GCD)<br>
     * gcd = a * (-1)<sup>k</sup> * x<sub>k</sub> + b * (-1)<sup>(k+1)</sup> * y<sub>k</sub>
     */
    double gcd(final double a, final double b) {
        Map<String, Double> kea = calculate(a, b);
        return ((a * Math.pow((-1), kea.get(KEY_K)) * kea.get(KEY_XK)) + (b * Math.pow((-1), kea.get(KEY_K) + 1) * kea.get(KEY_YK)));
    }

    boolean isRelativePrime(final double a, final double b) {
        return gcd(a, b) == 1;
    }
}