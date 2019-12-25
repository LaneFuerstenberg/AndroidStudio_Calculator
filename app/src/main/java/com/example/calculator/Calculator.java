package com.example.calculator;

import java.util.ArrayList;

/*
Calculator class is representative of the front end interface for the calculator application.
It handles how input is received, how to know what displays for the GUI and also makes sure the user
does not input something that is an invalid expression.

Calculator class uses stagingArea to prepare something to be added to a full expression

Calculator class uses HAS-A relationship with ArithmeticHandler and will always contain 1 instance
of this to handle calculations.
 */
public class Calculator {
    private ArrayList<String> expression = new ArrayList<>();
    //contains the most recent entry when it's not set in stone to then be added to expression
    private String stagingArea = "";
    //value used for clearing the staging area specifically after pressing equals
    private boolean resetStagingArea = false;
    //class that handles all calculations
    private ArithmeticHandler arithmeticHandler = new ArithmeticHandler();

    public void receiveDot() {
        if (StringUtil.isOperator(stagingArea)) {
            commitStagingAreaAndReplace(".");
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

    public void receiveOperator(String operator) {
        if (stagingArea.equals("(")) {
            return;
        }

        if (StringUtil.isOperator(stagingArea)) {
            stagingArea = operator;

        } else {
            commitStagingAreaAndReplace(operator);
            resetStagingArea = false;
        }

    }

    public void receiveNumber(String number) {
        if (StringUtil.isOperator(stagingArea) || stagingArea.equals("(") || stagingArea.equals(")")) {
            commitStagingAreaAndReplace(number);

        } else {
            if (resetStagingArea) {
                stagingArea = number;
                resetStagingArea = false;
            } else {
                stagingArea += number;
            }
        }
    }

    public void receiveOpenParenthesis() {
        if (stagingArea.isEmpty() || resetStagingArea == true) {
            stagingArea = "(";
            resetStagingArea = false;
        } else {
            commitStagingAreaAndReplace("(");
        }
    }

    public void receiveCloseParenthesis() {
        if (areMoreOpenThanClosedParenthesis() && !stagingArea.equals("(")) {
            if (stagingArea.isEmpty()) {
                stagingArea = ")";
            } else {
                commitStagingAreaAndReplace(")");
            }
        }
    }

    private boolean areMoreOpenThanClosedParenthesis() {
        int open = 0;
        int closed = 0;

        for (String contents : expression) {
            open += numberOfCharInString(contents, '(');
            closed += numberOfCharInString(contents, ')');
        }

        open += numberOfCharInString(stagingArea, '(');
        closed += numberOfCharInString(stagingArea, ')');

        return open > closed;
    }


    private int numberOfCharInString(String word, char character) {
        int amount = 0;
        for (char c : word.toCharArray()) {
            if (c == character) {
                amount++;

            }
        }

        return amount;
    }

    public String outputDisplay() {
        String output = "";

        for (String item : expression) {
            output += item;
        }

        output += stagingArea;
        return output;
    }

    public String calculate() {
        if (stagingArea.equals("")
                || StringUtil.isOperator(stagingArea)
                || !asManyOpenAsClosedParenthesis()) {
            return outputDisplay();
        }

        commitStagingAreaAndReplace();
        stagingArea = arithmeticHandler.calculateAndEmptyContents(expression);
        resetStagingArea = true;

        return stagingArea;
    }

    private boolean asManyOpenAsClosedParenthesis() {
        int open = 0;
        int closed = 0;

        /*
        rather obtuse solution but is basically trying to add stagingArea to calculating how many
        parenthesis of each kind exist without being forced to commit stagingArea to expression.
         */
        int index = 0;
        String item = stagingArea;
        do {
            if (item.equals("(")) {
                open++;
            }

            else if (item.equals(")")) {
                closed++;
            }

            if (expression.size() > index) {
                item = expression.get(index);
            }
        } while ((index++ < expression.size()));

        return open == closed;
    }


    private void commitStagingAreaAndReplace(String input) {
        if (stagingArea.equals("")) {
            return;
        }

        expression.add(stagingArea);
        stagingArea = input;
    }

    private void commitStagingAreaAndReplace() {
        commitStagingAreaAndReplace("");
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
