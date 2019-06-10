package compiler;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class BaseListener implements Listener {

	@Override
    public void enterProgram(CustomParser.ProgramContext ctx) { }

	@Override
    public void exitProgram(CustomParser.ProgramContext ctx) { }

	@Override
    public void enterGlobalVariable(CustomParser.GlobalVariableContext ctx) { }

    @Override
    public void exitGlobalVariable(CustomParser.GlobalVariableContext ctx) { }

    @Override
    public void enterVariable(CustomParser.VariableContext ctx) { }

    @Override
    public void exitVariable(CustomParser.VariableContext ctx) { }

    @Override
    public void enterFunction(CustomParser.FunctionContext ctx) { }

    @Override
    public void exitFunction(CustomParser.FunctionContext ctx) { }

    @Override
    public void enterBlock(CustomParser.BlockContext ctx) { }

    @Override
    public void exitBlock(CustomParser.BlockContext ctx) { }

    @Override
    public void enterLocalVariableStatement(CustomParser.LocalVariableStatementContext ctx) { }

    @Override
    public void exitLocalVariableStatement(CustomParser.LocalVariableStatementContext ctx) { }

    @Override
    public void enterAssignmentStatement(CustomParser.AssignmentStatementContext ctx) { }

    @Override
    public void exitAssignmentStatement(CustomParser.AssignmentStatementContext ctx) { }

    @Override
    public void enterFunctionCallStatement(CustomParser.FunctionCallStatementContext ctx) { }

    @Override
    public void exitFunctionCallStatement(CustomParser.FunctionCallStatementContext ctx) { }

    @Override
    public void enterIfStatementDef(CustomParser.IfStatementDefContext ctx) { }

    @Override
    public void exitIfStatementDef(CustomParser.IfStatementDefContext ctx) { }

    @Override
    public void enterWhileStatementDef(CustomParser.WhileStatementDefContext ctx) { }

    @Override
    public void exitWhileStatementDef(CustomParser.WhileStatementDefContext ctx) { }

    @Override
    public void enterBlockDef(CustomParser.BlockDefContext ctx) { }

    @Override
    public void exitBlockDef(CustomParser.BlockDefContext ctx) { }

    @Override
    public void enterReturnStatementDef(CustomParser.ReturnStatementDefContext ctx) { }

    @Override
    public void exitReturnStatementDef(CustomParser.ReturnStatementDefContext ctx) { }

    @Override
    public void enterAssignment(CustomParser.AssignmentContext ctx) { }

    @Override
    public void exitAssignment(CustomParser.AssignmentContext ctx) { }

    @Override
    public void enterIfStatement(CustomParser.IfStatementContext ctx) { }

    @Override
    public void exitIfStatement(CustomParser.IfStatementContext ctx) { }

    @Override
    public void enterWhileStatement(CustomParser.WhileStatementContext ctx) { }

    @Override
    public void exitWhileStatement(CustomParser.WhileStatementContext ctx) { }

    @Override
    public void enterReturnStatement(CustomParser.ReturnStatementContext ctx) { }

    @Override
    public void exitReturnStatement(CustomParser.ReturnStatementContext ctx) { }

    @Override
    public void enterExpression(CustomParser.ExpressionContext ctx) { }

    @Override
    public void exitExpression(CustomParser.ExpressionContext ctx) { }

    @Override
    public void enterTerm(CustomParser.TermContext ctx) { }

    @Override
    public void exitTerm(CustomParser.TermContext ctx) { }

    @Override
    public void enterFactor(CustomParser.FactorContext ctx) { }

    @Override
    public void exitFactor(CustomParser.FactorContext ctx) { }

    @Override
    public void enterFunctionCall(CustomParser.FunctionCallContext ctx) { }

    @Override
    public void exitFunctionCall(CustomParser.FunctionCallContext ctx) { }

    @Override
    public void enterCondition(CustomParser.ConditionContext ctx) { }

    @Override
    public void exitCondition(CustomParser.ConditionContext ctx) { }

    @Override
    public void enterComparisonOperator(CustomParser.ComparisonOperatorContext ctx) { }

    @Override
    public void exitComparisonOperator(CustomParser.ComparisonOperatorContext ctx) { }

    @Override
    public void enterReturnType(CustomParser.ReturnTypeContext ctx) { }

    @Override
    public void exitReturnType(CustomParser.ReturnTypeContext ctx) { }

    @Override
    public void enterVariableType(CustomParser.VariableTypeContext ctx) { }

    @Override
    public void exitVariableType(CustomParser.VariableTypeContext ctx) { }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) { }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) { }

    @Override
    public void visitTerminal(TerminalNode node) { }

    @Override
    public void visitErrorNode(ErrorNode node) { }
}