package com.zoltan.szilagyi.cse.rsa.api;


import com.zoltan.szilagyi.cse.rsa.log.Logger;

import java.util.Map;

public class ChineseRemainderTheorem {

    private Logger logger;
    private FastPower fastPower;
    private ExtendedEuclideanAlgorithm extendedEuclideanAlgorithm;

    public ChineseRemainderTheorem(Logger logger, FastPower fastPower, ExtendedEuclideanAlgorithm extendedEuclideanAlgorithm) {
        this.logger = logger;
        this.fastPower = fastPower;
        this.extendedEuclideanAlgorithm = extendedEuclideanAlgorithm;
    }

    int calculate(final double p, final double q, final double code, final double d) {
        double M = p * q;
        double m1 = p;
        double m2 = q;
        double M1 = calculateMx(M, m1);
        double M2 = calculateMx(M, m2);
        double c1 = calculateCx(p, code, d);
        double c2 = calculateCx(q, code, d);
        if (m1 < m2) {
            double tmp = m1;
            m1 = m2;
            m2 = tmp;
        }
        Map<String, Double> kea = extendedEuclideanAlgorithm.calculate(m1, m2);

        double y1 = calculateYx(kea.get(ExtendedEuclideanAlgorithm.KEY_K) + 1, kea.get(ExtendedEuclideanAlgorithm.KEY_YK));
        double y2 = calculateYx(kea.get(ExtendedEuclideanAlgorithm.KEY_K), kea.get(ExtendedEuclideanAlgorithm.KEY_XK));

        int m = (int) ((c1 * y1 * M1 + c2 * y2 * M2) % M);
        while (m < 0) {
            m += M;
        }
        return m;
    }

    /**
     * Calculates Y<sub>x</sub> where <i>x</i> is Y<sub>1</sub> or Y<sub>2</sub>
     */
    private double calculateYx(double value1, double value2) {
        return Math.pow((-1), value1) * value2;
    }

    /**
     * Calculates M<sub>x</sub> where <i>x</i> is M<sub>1</sub> or M<sub>2</sub>
     */
    private double calculateMx(double m, double m2) {
        return m / m2;
    }

    /**
     * Calculates c<sub>x</sub> where <i>x</i> is c<sub>1</sub> or c<sub>2</sub>
     */
    private double calculateCx(double p, double code, double d) {
        return fastPower.calculate(code, (long) (d % (p - 1)), p);
    }
}
