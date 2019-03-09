package com.zoltan.szilagyi.cse.rsa.log;

import java.util.Arrays;

public class Logger {

    private boolean logEnabled;

    public Logger(boolean logEnabled) {
        this.logEnabled = logEnabled;
    }

    public void log(String name, String value) {
        if (logEnabled) {
            System.out.println("[" + name + "]" + " = [" + value + "]");
        }
    }

    public void log(String name, double value) {
        if (logEnabled) {
            System.out.println("[" + name + "]" + " = [" + value + "]");
        }
    }

    public void log(String name, double[] array) {
        if (logEnabled) {
            System.out.println("[" + name + "]" + " = " + Arrays.toString(array));
        }
    }

    public void log(String name, int[] array) {
        if (logEnabled) {
            System.out.println("[" + name + "]" + " = " + Arrays.toString(array));
        }
    }
}
