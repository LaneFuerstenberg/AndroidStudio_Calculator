package com.example.calculator;

import com.example.calculator.operations.AddSubtractOperation;
import com.example.calculator.operations.ExponentOperation;
import com.example.calculator.operations.MultiInputOperation;
import com.example.calculator.operations.MultiplyDivideOperation;
import com.example.calculator.operations.Operation;
import com.example.calculator.operations.SingleInputOperation;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/*
Is represented by Calculator class to handle calculations specifically.
 */
public class ArithmeticHandler {
    private HashMap<String, String> PEMDAS = new HashMap<>();

    public String seperateFormula(ArrayList<String> formula) {
        ArrayList<String> formula2 = new ArrayList<String>() {{
            add("");
        }};
        int operators = 0;
        int openp = 0;
        for (int i = 0; i < formula.size(); i++) {
            int form2length = formula2.size();
            String character = formula.get(i);
            if (StringUtil.isOperator(character)) {
                formula2.add(character);
                formula2.add("");

            } else if (StringUtil.isDigit(character) || character.equals(".")) {
                formula2.set(form2length - 1, formula2.get(form2length - 1) + character);

            } else if (StringUtil.isParenthesis(character)) {
                openp = ParenthesisCount(character, openp);
                if (!StringUtil.isOperator(formula2.get(form2length - 1)) &&
                        character.equals("(")) {
                    formula2.add("*");
                }
                formula2.add(character);
            }

        }
        if (openp == 0) {
            DetermineFormula(formula2);
        }
        return formula.toString();
    }



    public void DetermineFormula(ArrayList<String> formula) {
        System.out.println(formula.toString());
    }

    public void CalculateFormula(ArrayList<String> formula) {

    }

    public int ParenthesisCount(String character, int openp) {
        if (character.equals("(")) {
            openp++;

        } else if (character.equals(")")) {
            openp--;
        }
        return openp;
    }
}

   /* public String calculateAndEmptyContents(ArrayList<String> contents) {
        this.formula2 = contents;

        Operation[] operations = new Operation[]{
                new ExponentOperation(),
                new MultiplyDivideOperation(),
                new AddSubtractOperation()
        };

        for (Operation operation : operations) {
            processOperators(operation);
        }


        //done to leave contents array empty
        String result = contents.get(0);
        contents.remove(0);

        return result;
    }

    *//*
    TODO: The coupling of ArithmeticHandler and the Operation subclasses is very tight
    TODO: Try to make this class work with more general operations and refactor it a ton
    *//*
    private void processOperators(Operation operation) {

        while (contentsHasOperator(operation)) {

            int index = findIndex(operation);
            processIndex(index, operation);
        }
    }

    private int findIndex(Operation operation) {
        String[] searchTerms = operation.getOperators();


        //looking for first instance of + or -
        for (int i = 0; i < formula2.size(); i++) {

            String result = formula2.get(i);
            if (matchesAny(searchTerms, result)) {
                return i;
            }
        }

        //should never occur since we validate that a + or - exists before running
        return -1;
    }

    private boolean matchesAny(String[] searchTerms, String word) {
        for (String s : searchTerms) {
            if (word.equals(s)) {
                return true;
            }
        }

        return false;
    }

    //receives index in array and processes the value before and after index
    private void processIndex(int index, Operation operation) {
        BigDecimal result = getResult(index, operation);

        //not a typo removes the middle and last value used to calculateAndEmptyContents
        formula2.remove(index);
        formula2.remove(index);

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(9);
        df.setMinimumFractionDigits(0);

        //replace first value with result
        formula2.set(index - 1, df.format(result));
    }

    private BigDecimal getResult(int index, Operation operation) {
        if (formula2.size() == index + 1) {
            return new BigDecimal(formula2.get(index - 1));
        }

        BigDecimal[] variables = new BigDecimal[]{
                new BigDecimal(formula2.get(index - 1)),
                new BigDecimal(formula2.get(index + 1))};

        BigDecimal result = new BigDecimal(0);

        if (operation instanceof MultiInputOperation) {
            String operator = formula2.get(index); //only 1 char anyway index is needed
            MultiInputOperation newO = (MultiInputOperation) operation;
            result = newO.handleOperation(operator, variables);

        } else if (operation instanceof SingleInputOperation) {
            SingleInputOperation newO = (SingleInputOperation) operation;
            result = newO.handleOperation(variables);
        }

        return result;
    }

    private boolean contentsHasOperator(Operation operation) {
        for (String o : operation.getOperators()) {
            if (formula2.contains(o)) {
                return true;
            }
        }

        return false;
    }
}
*/