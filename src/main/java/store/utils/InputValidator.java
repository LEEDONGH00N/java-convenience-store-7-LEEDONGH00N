package store.utils;

import store.exception.ConvenienceExceptionStatus;

import java.util.Arrays;

public class InputValidator {

    public static String validateInput(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException(ConvenienceExceptionStatus.INVALID_ORDER_FORMAT.getMessage());
        }

        if(!Arrays.stream(input.split(",")).allMatch(InputValidator::isValidItem)){
            throw new IllegalArgumentException(ConvenienceExceptionStatus.INVALID_ORDER_FORMAT.getMessage());
        }

        return input;
    }

    private static boolean isValidItem(String item) {
        if (!(item.startsWith("[") && item.endsWith("]"))) {
            throw new IllegalArgumentException(ConvenienceExceptionStatus.INVALID_ORDER_FORMAT.getMessage());
        }
        String content = item.substring(1, item.length() - 1);
        String[] parts = content.split("-");
        if (parts.length != 2) {
            throw new IllegalArgumentException(ConvenienceExceptionStatus.INVALID_ORDER_FORMAT.getMessage());
        }
        return ! parts[0].trim().isEmpty() && isNumeric(parts[1].trim());
    }

    private static boolean isNumeric(String str) {
        return str.chars().allMatch(Character::isDigit);
    }

    public static boolean isYesOrNo(String input) {
        if(input.equalsIgnoreCase("Y")){
            return true;
        }
        if(input.equalsIgnoreCase("N")){
            return false;
        }
        throw new IllegalArgumentException(ConvenienceExceptionStatus.INVALID_INPUT.getMessage());
    }
}
