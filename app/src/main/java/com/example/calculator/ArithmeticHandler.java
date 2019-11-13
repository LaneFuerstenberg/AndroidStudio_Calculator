package com.example.calculator;

import com.example.calculator.operations.AddSubtractOperation;
import com.example.calculator.operations.MultiplyDivideOperation;
import com.example.calculator.operations.Operation;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

/*
Is represented by Calculator class to handle calculations specifically.
 */
public class ArithmeticHandler {
    private ArrayList<String> contents;

    public String calculateAndEmptyContents(ArrayList<String> contents) {
        this.contents = contents;

        Operation[] operations = new Operation[]{
                new MultiplyDivideOperation(),
                new AddSubtractOperation()
        };

        for (Operation o : operations) {
            processOperators(o);
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
    private void processOperators(Operation o) {

        while (contentsHasOperator(o)) {

            int index = findIndex(o);
            processIndex(index, o);
        }
    }

    private int findIndex(Operation o) {
        String[] searchTerms = o.getOperators();


        //looking for first instance of + or -
        for (int i = 0; i < contents.size(); i++) {

            String result = contents.get(i);
            if (result.equals(searchTerms[0])
                    || result.equals(searchTerms[1])) {
                return i;
            }
        }

        //should never occur since we validate that a + or - exists before running
        return -1;
    }

    //receives index in array and processes the value before and after index
    private void processIndex(int index, Operation o) {
        BigDecimal result = getResult(index, o);

        //not a typo removes the middle and last value used to calculateAndEmptyContents
        contents.remove(index);
        contents.remove(index);

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(9);
        df.setMinimumFractionDigits(0);

        //replace first value with result
        contents.set(index - 1, df.format(result));
    }

    private BigDecimal getResult(int index, Operation o) {
        if (contents.size() == index + 1) {
            return new BigDecimal(contents.get(index - 1));
        }

        BigDecimal[] variables = new BigDecimal[]{
                new BigDecimal(contents.get(index - 1)),
                new BigDecimal(contents.get(index + 1))};


        String operator = contents.get(index); //only 1 char anyway index is needed
        BigDecimal result = o.handleOperation(operator, variables);

        return result;
    }

    private boolean contentsHasOperator(Operation o) {
        for (String operation : o.getOperators()) {
            if (contents.contains(operation)) {
                return true;
            }
        }

        return false;
    }
}
