package com.zoltan.szilagyi.cse.rsa;

import com.zoltan.szilagyi.cse.rsa.api.*;
import com.zoltan.szilagyi.cse.rsa.log.Logger;

import java.util.Random;
import java.util.Scanner;

public class Main {

    public static final boolean LOG_ENABLED = false;

    private Logger logger;
    private MillerRabinTest millerRabinTest;
    private Random random;
    private RSA rsa;

    private double p;
    private double q;
    private double n;
    private double fiN;

    private long d;
    private long e;

    public Main(Logger logger, MillerRabinTest millerRabinTest, Random random, RSA rsa) {
        this.logger = logger;
        this.millerRabinTest = millerRabinTest;
        this.random = random;
        this.rsa = rsa;
    }

    public static void main(String[] args) {
        Logger logger = new Logger(LOG_ENABLED);
        Random random = new Random();
        ExtendedEuclideanAlgorithm extendedEuclideanAlgorithm = new ExtendedEuclideanAlgorithm(logger);
        FastPower fastPower = new FastPower(logger);
        ChineseRemainderTheorem chineseRemainderTheorem = new ChineseRemainderTheorem(logger, fastPower, extendedEuclideanAlgorithm);
        RSA rsa = new RSA(logger, fastPower, chineseRemainderTheorem, extendedEuclideanAlgorithm, random);
        MillerRabinTest millerRabinTest = new MillerRabinTest(logger, fastPower, extendedEuclideanAlgorithm, random);

        Main main = new Main(logger, millerRabinTest, random, rsa);
        main.start();
    }

    private void start() {
        String plainText = readPlainText();

        initKeys();
        printValues();

        int[] characterArray = stringToCharacterArray(plainText);
        double[] encoded = encode(characterArray);
        int[] decoded = decode(encoded);
        String decodedText = characterArrayToString(decoded);

        printDecodedText(decodedText);
    }

    private int[] decode(double[] encoded) {
        int[] decoded = rsa.decode(p, q, encoded, d);
        logger.log("decoded", decoded);
        return decoded;
    }

    private double[] encode(int[] characterArray) {
        double[] encoded = rsa.encode(characterArray, e, n);
        logger.log("encrypted", encoded);
        return encoded;
    }

    private void printValues() {
        logger.log("p", p);
        logger.log("q", q);
        logger.log("φ(n)", fiN);
        logger.log("e", e);
        logger.log("d", d);
    }

    private void initKeys() {
        initPrimes();
        calculateN();
        calculateFiN();
        calculateE();
        calculateD();
    }

    private void initPrimes() {
        generatePrimes();
        changePrimesIfNeeded();
    }

    private String readPlainText() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Titkosítandó üzenet: ");
        return scanner.nextLine();
    }

    private void printDecodedText(String text) {
        System.out.println("Vissza fejtett üzenet: " + text);
    }

    private void calculateD() {
        d = (long) rsa.privateKey(fiN, e);
    }

    private void calculateE() {
        e = (long) rsa.publicKey(fiN);
    }

    private void changePrimesIfNeeded() {
        if (p < q) {
            double tmp = p;
            p = q;
            q = tmp;
        }
    }

    private void generatePrimes() {
        p = generateRandomPrime();
        q = generateRandomPrime();
        while (p == q) {
            q = generateRandomPrime();
        }
    }

    private void calculateFiN() {
        fiN = (p - 1) * (q - 1);
    }

    private void calculateN() {
        n = p * q;
    }

    private int[] stringToCharacterArray(String string) {
        int[] characterArray = new int[string.length()];
        for (int k = 0; k < string.length(); k++) {
            characterArray[k] = string.charAt(k);
        }
        logger.log("characters", characterArray);
        return characterArray;
    }

    private String characterArrayToString(int[] characterArray) {
        StringBuilder builder = new StringBuilder();
        for (int character : characterArray) {
            builder.append((char) character);
        }
        logger.log("decodedString", builder.toString());
        return builder.toString();
    }

    private double generateRandomPrime() {
        double num = Math.abs(random.nextLong()) % 10000;
        while (!millerRabinTest.isPrime(num)) {
            num = Math.abs(random.nextLong()) % 10000;
        }
        return num;
    }
}
