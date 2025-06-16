package com.company.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Util {

    public int generateRandomNumber() {
        int digits =4;
        int min = (int) Math.pow(10, digits - 1);
        int max = (int) Math.pow(10, digits) - 1;
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

}
