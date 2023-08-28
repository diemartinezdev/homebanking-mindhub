package com.mindhub.homebanking.utilities;

public class Utility {
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static int getCvvNumber() {
        int cardCvv = getRandomNumber(000, 999);
        return cardCvv;
    }

    public static String getCardNumber() {
        String cardNumber = getRandomNumber(0000, 9999) + "-" + getRandomNumber(0000, 9999) + "-" + getRandomNumber(0000, 9999)+ "-" + getRandomNumber(0000, 9999);
        return cardNumber;
    }
}