package com.ttdat.productservice.infrastructure.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class BarcodeUtils {

    private BarcodeUtils() {
    }

    /**
     * Generates a valid 13-digit EAN-13 barcode.
     *
     * @return A valid EAN-13 barcode as a String.
     */
    public static String generateEAN13Barcode() {
        String base = RandomStringUtils.secure().nextNumeric(12);
        int checkDigit = calculateEAN13CheckDigit(base);
        return base + checkDigit;
    }

    /**
     * Calculates the EAN-13 check digit using the standard checksum formula.
     *
     * @param barcode The first 12 digits of the barcode.
     * @return The computed check digit (0-9).
     */
    private static int calculateEAN13CheckDigit(String barcode) {
        int sum = 0;
        for (int i = 0; i < barcode.length(); i++) {
            int digit = Character.getNumericValue(barcode.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        int remainder = sum % 10;
        return (remainder == 0) ? 0 : 10 - remainder;
    }
}
