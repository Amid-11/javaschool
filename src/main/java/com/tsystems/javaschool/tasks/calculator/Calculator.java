package com.tsystems.javaschool.tasks.calculator;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

public class Calculator {

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */

    private static final String operators = "-+/*";
    private static final String operands = "0123456789";
    private static final String brackets = "()";
    private static final String decimalDelimiter = ".";


    public String evaluate(String statement) {
        //Check correct string
        if (statement == null || statement.length() == 0) {
            return null;
        }

        boolean hasIncorrectChars = statement.chars().mapToObj(c -> (char) c)
                .filter(o -> !operators.contains(String.valueOf(o)))
                .filter(o -> !operands.contains(String.valueOf(o)))
                .filter(o -> !brackets.contains(String.valueOf(o)))
                .filter(o -> !decimalDelimiter.contains(String.valueOf(o)))
                .count() > 0;
        if (hasIncorrectChars) {
            return null;
        }

        for (int i = 0; i < statement.length() - 1; i++) {
            char first = statement.charAt(i);
            char second = statement.charAt(i + 1);
            if ((operators + decimalDelimiter).contains(String.valueOf(first)) && (operators + decimalDelimiter).contains(String.valueOf(second))) {
                return null;
            }
        }

        long numberOfOpenBrackets = statement.chars().mapToObj(c -> (char) c)
                .filter(o -> o=='(')
                .count();
        long numberOfCloseBrackets = statement.chars().mapToObj(c -> (char) c)
                .filter(o -> o==')')
                .count();
        if (numberOfOpenBrackets != numberOfCloseBrackets) {
            return null;
        }


        Double result = evaluatePostfix(convert2Postfix(stringToArrayExpression(statement)));
        if (Double.isInfinite(result)) {
            return null;
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.####",new DecimalFormatSymbols(Locale.US));
        return decimalFormat.format(result);
    }

    //парсим строку в массив-выражение
    public List<String> stringToArrayExpression(String line) {
        List<String> list = new ArrayList();
        char[] chars = line.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (isCharNumb(chars[i])) {
                String num = "";
                for (int j = i; j < chars.length; j++) {
                    if (isCharNumb(chars[j])) {
                        num = num + String.valueOf(chars[j]);
                        i = j;
                    } else {
                        break;
                    }
                }
                list.add(num);
            } else list.add(String.valueOf(chars[i]));
        }
        return list;
    }

    private boolean isCharNumb(char c) {
        return c > 47 && c < 58 || c == 46;
    }

    public List<String> convert2Postfix(List<String> expression) {
        //char[] chars = infixExpr.toCharArray();
        Stack<String> stack = new Stack<String>();
        List<String> out = new ArrayList<>();

        for (String c : expression) {
            if (isOperator(c)) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    if (operatorGreaterOrEqual(stack.peek(), c)) {
                        out.add(stack.pop());
                    } else {
                        break;
                    }
                }
                stack.push(c);
            } else if (c.equals("(")) {
                stack.push(c);
            } else if (c.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    out.add(stack.pop());
                }
                if (!stack.isEmpty()) {
                    stack.pop();
                }
            } else if (isOperand(c)) {
                out.add(c);
            }
        }
        while (!stack.empty()) {
            out.add(stack.pop());
        }
        return out;
    }

    public Double evaluatePostfix(List<String> expression) {
        Stack<Double> stack = new Stack();
        for (String c : expression) {
            if (isOperand(c)) {
                stack.push(Double.valueOf(c)); // convert char to int val
            } else if (isOperator(c)) {
                Double op1 = stack.pop();
                Double op2 = stack.pop();
                Double result;
                switch (c) {
                    case "*":
                        result = op1 * op2;
                        stack.push(result);
                        break;
                    case "/":
                        result = op2 / op1;
                        stack.push(result);
                        break;
                    case "+":
                        result = op1 + op2;
                        stack.push(result);
                        break;
                    case "-":
                        result = op2 - op1;
                        stack.push(result);
                        break;
                }
            }
        }
        return stack.pop();
    }

    private int getPrecedence(String operator) {
        int ret = 0;
        if (operator.equals("-") || operator.equals("+")) {
            ret = 1;
        } else if (operator.equals("*") || operator.equals("/")) {
            ret = 2;
        }
        return ret;
    }

    private boolean operatorGreaterOrEqual(String op1, String op2) {
        return getPrecedence(op1) >= getPrecedence(op2);
    }

    private boolean isOperator(String operator) {
        return operators.contains(operator);
    }

    private boolean isOperand(String operand) {
        try {
            Double.valueOf(operand);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static void main(String[] args) {
        System.out.println("(12.56+38)*44-512");

    }
}

