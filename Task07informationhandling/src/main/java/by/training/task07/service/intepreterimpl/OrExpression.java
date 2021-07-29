package by.training.task07.service.intepreterimpl;

import by.training.task07.service.Interpreter;

import java.util.Stack;

public class OrExpression implements Interpreter {

    @Override
    public void interpret(Stack<Integer> stack) {
        int temp = stack.pop();
        stack.push(temp | stack.pop());
    }
}
