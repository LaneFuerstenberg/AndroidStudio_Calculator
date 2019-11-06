package com.example.calculator;

import java.util.ArrayList;

public class Calculator {
    //contains full history of expression of received and is generally 1 to 1 with what's displayed
    private ArrayList<String> expression = new ArrayList<>();
    //contains the most recent entry when it's not set in stone to then be added to expression
    private String stagingArea = "";
    //value used for clearing the staging area specifically after pressing equals
    private boolean resetStagingArea = false;
    //class that handles all calculations
    private ArithmeticHandler arithmeticHandler = new ArithmeticHandler();

    public void receiveDot() {
        if (StringUtil.isOperator(stagingArea)) {
            commitStagingArea(".");
        } else if (!stagingArea.contains(".")) {
            stagingArea += ".";
        }
    }

    public void receiveNegative() {
        if (stagingArea.length() > 0) {
            if (stagingArea.charAt(0) == '-') {
                stagingArea = stagingArea.substring(1);
            } else if (!StringUtil.isOperator(stagingArea)) {
                stagingArea = "-" + stagingArea;
            }
        }
    }

    public void receiveExponent() {
        if (!StringUtil.isOperator(stagingArea)) {
            stagingArea += "^";
        }
    }

    public void receiveOperator(String operator) {
        if (StringUtil.isOperator(stagingArea)) {
            stagingArea = operator;
        }

        else {
            commitStagingArea(operator);
            resetStagingArea = false;
        }

    }

    public void receiveNumber(String number) {
        if (StringUtil.isOperator(stagingArea)) {
            commitStagingArea(number);
        }

        else {
            if (resetStagingArea) {
                stagingArea = number;
                resetStagingArea = false;
            }

            else {
                stagingArea += number;
            }
        }
    }

    public String outputDisplay() {
        String output = "";

        for (String item : expression) {
            output += item + " ";
        }

        output += stagingArea;
        return output;
    }

    public String calculate() {
        if (stagingArea.equals("") || StringUtil.isOperator(stagingArea)) {
            return outputDisplay();
        }

        commitStagingArea();
        stagingArea = arithmeticHandler.calculate(expression);
        resetStagingArea = true;


        return stagingArea;
    }


    private boolean commitStagingArea(String input) {
        if (stagingArea.equals("")) {
            return false;
        }

        expression.add(stagingArea);
        stagingArea = input;
        return true;
    }

    private boolean commitStagingArea() {
        return commitStagingArea("");
    }

    public void deleteLast() {
        if (!StringUtil.isOperator(stagingArea) && !stagingArea.equals("")) {
            stagingArea = stagingArea.substring(0, stagingArea.length() - 1);
        }
    }

    public void clear() {
        expression.clear();
        stagingArea = "";
    }

    public void clearEntry() {
        stagingArea = "";
    }
}
