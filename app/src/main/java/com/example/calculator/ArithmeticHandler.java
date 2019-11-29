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
    Operation[] operations;

    public ArithmeticHandler() {
        operations = new Operation[]{
                new ExponentOperation(),
                new MultiplyDivideOperation(),
                new AddSubtractOperation()
        };
    }

    public String calculateAndEmptyContents(ArrayList<String> expression) {
        while (expression.contains("(")) {
            parenthesisRecursion(expression);
        }

        for (Operation operation : operations) {
            processOperationForExpression(operation, expression);
        }

        //done to leave expression array empty
        String result = expression.get(0);
        expression.remove(0);

        return result;
    }

    private void parenthesisRecursion(ArrayList<String> expression) {
        addingImplicitMultiplication(expression);

        int indexStart = expression.indexOf("(") + 1;
        int indexEnd = findCorrespondingEndParenthesis(indexStart, expression);
        ArrayList<String> oneLevelDownArray = new ArrayList<>(expression.subList(indexStart, indexEnd));

        while (oneLevelDownArray.contains("(")) {
            parenthesisRecursion(oneLevelDownArray);
        }

        String result = calculateAndEmptyContents(oneLevelDownArray);
        for (int i = 0; i <= indexEnd - indexStart; i++) {
            expression.remove(indexStart);
        }

        expression.set(indexStart - 1, result);
    }

    private void addingImplicitMultiplication(ArrayList<String> expression) {
        ArrayList<Integer> allOpenIndex = findAllIndexForString(expression, "(");
        allOpenIndex.remove(Integer.valueOf(0));

        int offsetOpen = 0;
        for (int i : allOpenIndex) {
            if (!StringUtil.isOperator(expression.get(i - 1)) && !expression.get(i - 1).equals("(")) {
                expression.add(i + offsetOpen++, "*");
            }
        }

        ArrayList<Integer> allClosedIndex = findAllIndexForString(expression, ")");
        allClosedIndex.remove(Integer.valueOf(expression.size() - 1));

        int offsetClosed = 1;
        for (int i : allClosedIndex) {
            if (StringUtil.isDigit(expression.get(i + 1))) {
                expression.add(i + offsetClosed, "*");
            }
        }
    }

    private void processOperationForExpression(Operation operation, ArrayList<String> expression) {

        while (contentsHasOperator(operation, expression)) {
            String[] searchTerms = operation.getOperators();
            int index = -1;

            //looking for first instance of + or -
            for (int i = 0; i < expression.size(); i++) {

                String result = expression.get(i);
                if (matchesAny(searchTerms, result)) {
                    index = i;
                }
            }

            processOperationAtIndex(index, operation, expression);
        }
    }

    //receives index and processes the items left and right by the index (operation)
    private void processOperationAtIndex(int index, Operation operation, ArrayList<String> expression) {
        BigDecimal[] variables = new BigDecimal[]{
                new BigDecimal(expression.get(index - 1)),
                new BigDecimal(expression.get(index + 1))};

        BigDecimal result = new BigDecimal(0);

        if (operation instanceof MultiInputOperation) {
            String operator = expression.get(index);
            result = getResult(operation, variables, operator);

        } else if (operation instanceof SingleInputOperation) {
            result = getResult(operation, variables);
        }

        //not a typo removes the middle and last value used to calculateAndEmptyContents
        expression.remove(index);
        expression.remove(index);

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(9);
        df.setMinimumFractionDigits(0);

        //replace first value with result
        expression.set(index - 1, df.format(result));
    }

    private BigDecimal getResult(Operation operation, BigDecimal[] variables) {
        SingleInputOperation singleInputOperation = (SingleInputOperation) operation;
        return singleInputOperation.handleOperation(variables);
    }

    private BigDecimal getResult(Operation operation, BigDecimal[] variables, String operator) {
        MultiInputOperation multiInputOperation = (MultiInputOperation) operation;
        return multiInputOperation.handleOperation(operator, variables);
    }

    private int findCorrespondingEndParenthesis(int start, ArrayList<String> expression) {
        //delay is used to skip the next ) if ( is found first
        int delay = 0;
        for (int i = start; i < expression.size(); i++) {
            if (expression.get(i).equals("(")) {
                delay++;

            } else if (expression.get(i).equals(")")) {

                if (delay != 0) {
                    delay--;
                } else {
                    return i;
                }
            }
        }

        return -1;
    }

    private ArrayList<Integer> findAllIndexForString(ArrayList<String> expression, String search) {
        ArrayList<Integer> allIndex = new ArrayList<>();
        for (int i = 0; i < expression.size(); i++) {
            if (expression.get(i).equals(search)) {
                allIndex.add(i);
            }
        }

        return allIndex;
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
