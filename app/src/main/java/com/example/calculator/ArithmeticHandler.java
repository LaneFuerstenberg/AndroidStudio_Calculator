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
            parenthesisRecursion(contents);
        }

        Operation[] operations = new Operation[]{
                new ExponentOperation(),
                new MultiplyDivideOperation(),
                new AddSubtractOperation()
        };

        for (Operation operation : operations) {
            processOperationForExpression(operation, contents);
        }


        //done to leave contents array empty
        String result = contents.get(0);
        contents.remove(0);

        return result;
    }

    private void parenthesisRecursion(ArrayList<String> contents) {
        int indexStart = contents.indexOf("(") + 1;
        int indexEnd = contents.indexOf(")");

        //inserts * at #( instance
        if (indexStart != 0 && !StringUtil.isOperator(contents.get(indexStart - 1))) {
            contents.add(indexStart - 1, "*");
            indexStart++;
            indexEnd++;
        }

        ArrayList newArray = new ArrayList<>(contents.subList(indexStart, indexEnd));

        String result = calculateAndEmptyContents(newArray);
        for (int i = 0; i <= indexEnd - indexStart; i++) {
            contents.remove(indexStart);
        }

        contents.set(indexStart - 1, result);
    }

    /*
    TODO: The coupling of ArithmeticHandler and the Operation subclasses is very tight
    TODO: Try to make this class work with more general operations and refactor it a ton
    */
    private void processOperationForExpression(Operation operation, ArrayList<String> contents) {

        while (contentsHasOperator(operation, contents)) {
            String[] searchTerms = operation.getOperators();
            int index = -1;

            //looking for first instance of + or -
            for (int i = 0; i < contents.size(); i++) {

                String result = contents.get(i);
                if (matchesAny(searchTerms, result)) {
                    index = i;
                }
            }

            processOperationAtIndex(index, operation, contents);
        }
    }


    //receives index and processes the items left and right by the index (operation)
    private void processOperationAtIndex(int index, Operation operation, ArrayList<String> contents) {
        BigDecimal[] variables = new BigDecimal[]{
                new BigDecimal(contents.get(index - 1)),
                new BigDecimal(contents.get(index + 1))};

        BigDecimal result = new BigDecimal(0);

        if (operation instanceof MultiInputOperation) {
            String operator = contents.get(index);
            result = getResult(operation, variables, operator);

        } else if (operation instanceof SingleInputOperation) {
            result = getResult(operation, variables);
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

    private BigDecimal getResult(Operation operation, BigDecimal[] variables) {
        SingleInputOperation singleInputOperation = (SingleInputOperation) operation;
        return singleInputOperation.handleOperation(variables);
    }

    private BigDecimal getResult(Operation operation, BigDecimal[] variables, String operator) {
        MultiInputOperation multiInputOperation = (MultiInputOperation) operation;
        return multiInputOperation.handleOperation(operator, variables);
    }

    private boolean matchesAny(String[] searchTerms, String word) {
        for (String s : searchTerms) {
            if (word.equals(s)) {
                return true;
            }
        }

        return false;
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
