package com.mindhub.homebanking.utilities;

import java.util.Random;

public class Utility {
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static int getCvvNumber() {
        int cardCvv = getRandomNumber(000, 999);
        return cardCvv;
    }

    public static String getCardNumber() {
        Random random = new Random();
        StringBuilder creditCardNumber = new StringBuilder();

        // Genera 16 dígitos aleatorios para la tarjeta de crédito
        for (int i = 0; i < 16; i++) {
            int digit = random.nextInt(10); // Genera un dígito entre 0 y 9
            creditCardNumber.append(digit);

            // Agrega un guión después de cada grupo de 4 dígitos (excepto el último grupo)
            if ((i + 1) % 4 == 0 && i != 15) {
                creditCardNumber.append("-");
            }
        }

        return creditCardNumber.toString();
    }
}