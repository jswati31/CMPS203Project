package compiler;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CustomParseTreeListener extends BaseListener {

    private final ClassFile classFile;

    private LinkedList<Expression> expressionStack = new LinkedList<>();

    private LinkedList<Statement> statementStack = new LinkedList<>();

    private Map<String, LocalVariable> localVariableMap = new HashMap<>();

    private LinkedList<Block> blockStack = new LinkedList<>();

    public CustomParseTreeListener(String className) {
        this.classFile = new ClassFile(className);
    }

    @Override
    public void exitGlobalVariable(CustomParser.GlobalVariableContext ctx) {
        CustomParser.VariableContext variableContext = ctx.variable();
        Type type = Type.valueOf(variableContext.variableType().getText().toUpperCase());
        String variableName = variableContext.name.getText();
        this.classFile.addField(type, variableName);
    }

    @Override
    public void enterFunction(CustomParser.FunctionContext ctx) {
        for (int i = 0; i < ctx.variable().size(); i++) {
            this.localVariableMap.put(ctx.variable(i).name.getText(), new LocalVariable(Type.valueOf(ctx.variable(i).variableType().getText().toUpperCase()), i));
        }
    }

    @Override
    public void exitFunction(CustomParser.FunctionContext ctx) {
        Type returnType = Type.valueOf(ctx.returnType().getText().toUpperCase());
        String functionName = ctx.name.getText();

        List<LocalVariable> arguments = new ArrayList<>();
        for (int i = 0; i < ctx.variable().size(); i++) {
            arguments.add(new LocalVariable(Type.valueOf(ctx.variable(i).variableType().getText().toUpperCase()), i));
        }

        this.classFile.addMethod(returnType, functionName, arguments, this.blockStack.pop());
    }

    @Override
    public void exitBlock(CustomParser.BlockContext ctx) {
        Block b = new Block(new ArrayList<>(this.statementStack));
        b.setLocalVariableCount(this.localVariableMap.size());
        this.blockStack.push(b);
        this.statementStack.clear();
        this.localVariableMap.clear();
    }

    @Override
    public void exitLocalVariableStatement(CustomParser.LocalVariableStatementContext ctx) {
        LocalVariable l = new LocalVariable(Type.valueOf(ctx.localVariable.variableType().getText().toUpperCase()), this.localVariableMap.size());
        this.localVariableMap.put(ctx.localVariable.name.getText(), l);
    }

    @Override
    public void exitAssignmentStatement(CustomParser.AssignmentStatementContext ctx) {
        super.exitAssignmentStatement(ctx);
    }

    @Override
    public void exitAssignment(CustomParser.AssignmentContext ctx) {
        Expression expression = expressionStack.pop();
        Variable variable;
        if (this.classFile.hasField(ctx.name.getText())) {
            variable = this.classFile.createFieldReference(ctx.name.getText());
        } else {
            variable = this.localVariableMap.get(ctx.name.getText());
        }
        this.statementStack.push(new AssignmentStatement(variable, expression));
    }

    @Override
    public void exitFunctionCall(CustomParser.FunctionCallContext ctx) {
        String functionName = ctx.name.getText();
        List<Expression> arguments = ctx.expression().stream()
                .map(c -> this.expressionStack.poll())
                .collect(Collectors.toList());
        this.expressionStack.push(new CallExpression(functionName, arguments, this.classFile));
    }

    @Override
    public void exitExpression(CustomParser.ExpressionContext ctx) {
        arithmeticOperation(ctx);
    }

    @Override
    public void exitTerm(CustomParser.TermContext ctx) {
        arithmeticOperation(ctx);
    }

    private void arithmeticOperation(ParserRuleContext ctx) {
        if (ctx.children.size() < 3) {
            return;
        }

        List<ParseTree> children = ctx.children;
        LinkedList<ParseTree> operators = IntStream.range(0, children.size())
                .filter(n -> n % 2 == 1)
                .mapToObj(children::get)
                .collect(Collectors.toCollection(LinkedList::new));

        Expression right = this.expressionStack.pop();
        while (!this.expressionStack.isEmpty()) {
            Expression left = this.expressionStack.pop();
            ArithmeticOperator arithmeticOperator = ArithmeticOperator.from(operators.pop().getText());
            right = new BinaryOperator(arithmeticOperator, left, right);
        }
        this.expressionStack.push(right);
    }

    @Override
    public void exitFactor(CustomParser.FactorContext ctx) {
        if (ctx.expression() != null) {
        } else if (ctx.functionCall() != null) {
        } else if (ctx.variableName != null) {
            Variable variable;
            if (this.classFile.hasField(ctx.variableName.getText())) {
                variable = this.classFile.createFieldReference(ctx.variableName.getText());
            } else {
                variable = this.localVariableMap.get(ctx.variableName.getText());
            }
            this.expressionStack.push(variable);
        } else if (ctx.constant != null) {
            int value = Integer.valueOf(ctx.constant.getText());
            if (value < Short.MIN_VALUE || Short.MAX_VALUE < value) {
                int index = this.classFile.addInteger(value);
                this.expressionStack.push(new ConstantInt(index, value));
            } else {
                this.expressionStack.push(new ConstantInt(value));
            }
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public void exitReturnStatement(CustomParser.ReturnStatementContext ctx) {
        if (this.expressionStack.isEmpty()) {
            this.statementStack.push(new ReturnStatement());
        } else {
            this.statementStack.push(new ReturnStatement(this.expressionStack.pop()));
        }
    }

    @Override
    public void exitIfStatement(CustomParser.IfStatementContext ctx) {
        ComparisonOperator operator = ComparisonOperator.from(ctx.condition().comparisonOperator().getText());
        Expression right = this.expressionStack.pop();
        Expression left = this.expressionStack.pop();
        this.statementStack.push(new IfStatement(operator, left, right, this.statementStack.pop()));
    }

    @Override
    public void exitWhileStatement(CustomParser.WhileStatementContext ctx) {
        ComparisonOperator operator = ComparisonOperator.from(ctx.condition().comparisonOperator().getText());
        Expression right = this.expressionStack.pop();
        Expression left = this.expressionStack.pop();
        this.statementStack.push(new WhileStatement(operator, left, right, this.statementStack.pop()));
    }

    @Override
    public void exitBlockDef(CustomParser.BlockDefContext ctx) {
        this.statementStack.push(new BlockStatement(this.blockStack.pop()));
    }

    @Override
    public void exitFunctionCallStatement(CustomParser.FunctionCallStatementContext ctx) {
        this.statementStack.push(new CallStatement(this.expressionStack.pop()));
    }

    public ClassFile getClassFile() {
        return classFile;
    }
}
