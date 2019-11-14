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

/*
Is represented by Calculator class to handle calculations specifically.
 */
public class ArithmeticHandler {

    public String calculateAndEmptyContents(ArrayList<String> contents) {
        while (contents.contains("(")) {
            int indexStart = contents.indexOf("(") + 1;
            int indexEnd = contents.indexOf(")");
            ArrayList newArray = new ArrayList<>(contents.subList(indexStart, indexEnd));

            String result = calculateAndEmptyContents(newArray);
            for (int i = 0; i <= indexEnd - indexStart; i++) {
                contents.remove(indexStart);
            }

            contents.set(indexStart - 1, result);
        }

        Operation[] operations = new Operation[]{
                new ExponentOperation(),
                new MultiplyDivideOperation(),
                new AddSubtractOperation()
        };

        for (Operation operation : operations) {
            processOperators(operation, contents);
        }


        //done to leave contents array empty
        String result = contents.get(0);
        contents.remove(0);

        return result;
    }

    /*
    TODO: The coupling of ArithmeticHandler and the Operation subclasses is very tight
    TODO: Try to make this class work with more general operations and refactor it a ton
    */
    private void processOperators(Operation operation, ArrayList<String> contents) {

        while (contentsHasOperator(operation, contents)) {

            int index = findIndex(operation, contents);
            processIndex(index, operation, contents);
        }
    }

    private int findIndex(Operation operation, ArrayList<String> contents) {
        String[] searchTerms = operation.getOperators();


        //looking for first instance of + or -
        for (int i = 0; i < contents.size(); i++) {

            String result = contents.get(i);
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
    private void processIndex(int index, Operation operation, ArrayList<String> contents) {
        BigDecimal result = getResult(index, operation, contents);

        //not a typo removes the middle and last value used to calculateAndEmptyContents
        contents.remove(index);
        contents.remove(index);

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(9);
        df.setMinimumFractionDigits(0);

        //replace first value with result
        contents.set(index - 1, df.format(result));
    }

    private BigDecimal getResult(int index, Operation operation, ArrayList<String> contents) {
        if (contents.size() == index + 1) {
            return new BigDecimal(contents.get(index - 1));
        }

        BigDecimal[] variables = new BigDecimal[]{
                new BigDecimal(contents.get(index - 1)),
                new BigDecimal(contents.get(index + 1))};

        BigDecimal result = new BigDecimal(0);

        if (operation instanceof MultiInputOperation) {
            String operator = contents.get(index); //only 1 char anyway index is needed
            MultiInputOperation newO = (MultiInputOperation) operation;
            result = newO.handleOperation(operator, variables);

        } else if (operation instanceof SingleInputOperation) {
            SingleInputOperation newO = (SingleInputOperation) operation;
            result = newO.handleOperation(variables);
        }

        return result;
    }

    private boolean contentsHasOperator(Operation operation, ArrayList<String> contents) {
        for (String o : operation.getOperators()) {
            if (contents.contains(o)) {
                return true;
            }
        }

        return false;
    }
}
