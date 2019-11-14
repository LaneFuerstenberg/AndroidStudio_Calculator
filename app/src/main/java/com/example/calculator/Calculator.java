package com.example.calculator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/*
Calculator class is representative of the front end interface for the calculator application.
It handles how input is received, how to know what displays for the GUI and also makes sure the user
does not input something that is an invalid expression.

Calculator class uses stagingArea to prepare something to be added to a full expression

Calculator class uses HAS-A relationship with ArithmeticHandler and will always contain 1 instance
of this to handle calculations.
 */


public class Calculator {
    private ArrayList<String> formula = new ArrayList<>();
    //contains the most recent entry when it's not set in stone to then be added to formula
    private String stagingArea = "";
    //value used for clearing the staging area specifically after pressing equals
    private boolean resetStagingArea = false;
    //class that handles all calculations
    private ArithmeticHandler arithmeticHandler = new ArithmeticHandler();

    public void receiveRightmost(String expression) {
        int formLength = formula.size();
        if (formLength > 0){
            System.out.println(formula.get(formLength - 1));
        }

        if (StringUtil.isOperator(expression) &&
           !StringUtil.isOperator(formula.get(formLength -1)) &&
           (!formula.get(formLength -1).equals("("))) {
                formula.add(expression);

        } else if (StringUtil.isDigit(expression) ||
                  (expression.equals(".") && !formula.get(formLength -1).equals(".")) ||
                   StringUtil.isParenthesis(expression)){
            formula.add(expression);
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

    public String outputDisplay() {
        String output = "";
        for (String item : formula) {
            output += item;
        }
        output += stagingArea;
        return output;
    }

    public String calculate() {
        int formLength = formula.size();
        if (formLength == 0 || StringUtil.isOperator(formula.get(formLength - 1))) {
            return outputDisplay();
        }
        String output = arithmeticHandler.seperateFormula(formula);
        resetStagingArea = true;

        return output;
    }

    public void deleteLast() {
        int formLength = formula.size();
        if (formLength != 0){
            formula.remove(formLength - 1);
        }

    }

    public void clear() {
        formula.clear();
    }

    public void clearEntry() {
    }

    private boolean isLastInputEqualTo(char c) {
        return stagingArea.charAt(stagingArea.length() - 1) != c;
    }
}
