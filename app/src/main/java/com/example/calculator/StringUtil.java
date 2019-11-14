package com.example.calculator;

public class StringUtil {
    private static final char[] OPERATOR = new char[] {'+', '-', '/', '*', '^'};
    private static final char[] MODIFIER = new char[] {'(', ')'};

    public static boolean isOperator(String input) {
        for (char operator : OPERATOR) {
            if (input.equals(Character.toString(operator))) {
                return true;
            }
        }

        return false;
    }

    public static boolean isParenthesis(String input) {
        for (char modifier : MODIFIER) {
            if (input.equals(Character.toString(modifier))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDigit(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
