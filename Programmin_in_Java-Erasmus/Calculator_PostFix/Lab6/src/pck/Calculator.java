package pck;

import java.util.Dictionary;
import java.util.EmptyStackException;
import java.util.Stack;

public class Calculator {

    private static final String ADD = "+";
    private static final String SUB = "-";
    private static final String MUL = "*";
    private static final String DIV = "/";

    public static int calculatePostFix(String[] elements) throws Exception {

        Stack<Integer> operands = new Stack<Integer>();
        int op1, op2;
        if (elements.length <= 1) {
            throw new EmptyStackException();
        }

        for (int i = 0; i < elements.length; i++) {
            if (elements[i].length() == 0) {
                continue;
            }
            if (elements[i].equals(ADD) || elements[i].equals(SUB) || elements[i].equals(MUL)
                    || elements[i].equals(DIV)) {
                op2 = operands.pop();
                op1 = operands.pop();
                switch (elements[i]) {
                    case ADD -> {
                        int res = op1 + op2;
                        operands.push(res);
                    }
                    case SUB -> {
                        int res = op1 - op2;
                        operands.push(res);
                    }
                    case MUL -> {
                        int res = op1 * op2;
                        operands.push(res);
                    }
                    case DIV -> {
                        if (op2 == 0) {
                            throw new Exception();
                        }
                        int res = op1 / op2;
                        operands.push(res);
                    }
                    default -> {
                        throw new EmptyStackException();
                    }
                }
            } else {
                operands.push(Integer.parseInt(elements[i]));
            }
        }

        if (operands.size() > 1) {
            throw new EmptyStackException();
        }
        return operands.pop();
    }
}
