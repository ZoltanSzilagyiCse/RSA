package com.zoltan.szilagyi.cse.rsa.api;


import com.zoltan.szilagyi.cse.rsa.log.Logger;

public class FastPower {

    private Logger logger;

    public FastPower(Logger logger) {
        this.logger = logger;
    }

    double calculate(final double base, final long power, final double modulo) {
        char[] binaryArray = new StringBuilder(Long.toBinaryString(power)).reverse().toString().toCharArray();
        double[] exponents = new double[binaryArray.length];
        exponents[0] = base;

        calculateExponents(modulo, binaryArray, exponents);
        reduceExponents(binaryArray, exponents);

        return calculateResult(modulo, exponents);
    }

    /**
     * Calculates result
     * Need to calculate modulo after every multiply to avoid overflow
     */
    private double calculateResult(double modulo, double[] exponents) {
        double result = 1;
        for (double exponent : exponents) {
            result = result * exponent % modulo;
        }
        return result;
    }

    /**
     * Reduces exponents
     */
    private void reduceExponents(char[] binaryArray, double[] exponents) {
        for (int i = 0; i < binaryArray.length; i++) {
            if (binaryArray[i] == '0') {
                exponents[i] = 1L;
            }
        }
    }

    /**
     * Calculates exponents
     */
    private void calculateExponents(double modulo, char[] binaryArray, double[] exponents) {
        for (int i = 1; i < binaryArray.length; i++) {
            exponents[i] = Math.pow(exponents[i - 1], 2) % modulo;
        }
    }
}
