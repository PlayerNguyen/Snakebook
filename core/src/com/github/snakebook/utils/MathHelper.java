package com.github.snakebook.utils;

public class MathHelper {

    public static int randomNextInt(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

}
