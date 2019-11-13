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
    private ArrayList<String> contents;

    public String calculateAndEmptyContents(ArrayList<String> contents) {
        this.contents = contents;

        Operation[] operations = new Operation[]{
                new ExponentOperation(),
                new MultiplyDivideOperation(),
                new AddSubtractOperation()
        };

        for (Operation operation : operations) {
            processOperationForExpression(operation);
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
    private void processOperationForExpression(Operation operation) {

        while (contentsHasOperator(operation)) {
            String[] searchTerms = operation.getOperators();
            int index = -1;

            //looking for first instance of + or -
            for (int i = 0; i < contents.size(); i++) {

                String result = contents.get(i);
                if (matchesAny(searchTerms, result)) {
                    index = i;
                }
            }

            processOperationAtIndex(index, operation);
        }
    }


    //receives index in array and processes the value before and after index
    private void processOperationAtIndex(int index, Operation operation) {
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

        //not a typo removes the middle and last value used to calculateAndEmptyContents
        contents.remove(index);
        contents.remove(index);

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(9);
        df.setMinimumFractionDigits(0);

        //replace first value with result
        contents.set(index - 1, df.format(result));
    }

    private boolean matchesAny(String[] searchTerms, String word) {
        for (String s : searchTerms) {
            if (word.equals(s)) {
                return true;
            }
        }

        return false;
    }

    private boolean contentsHasOperator(Operation operation) {
        for (String o : operation.getOperators()) {
            if (contents.contains(o)) {
                return true;
            }
        }

        return false;
    }
}
