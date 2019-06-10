package compiler;


import java.io.IOException;
import static compiler.Bytecode.*;

public class BinaryOperator extends Expression {

    private final ArithmeticOperator operator;

    private final Expression left;

    private final Expression right;

    public BinaryOperator(ArithmeticOperator operator, Expression left, Expression right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public Type eval(MethodBytecodeBuffer buffer, int stackPointer) throws IOException {
        int opCode;
        switch (operator) {
            case DIVIDE:
                opCode = IDIV;
                break;
            case MINUS:
                opCode = ISUB;
                break;
            case MULTIPLY:
                opCode = IMUL;
                break;
            case PLUS:
                opCode = IADD;
                break;
            default:
                throw new RuntimeException();
        }

        Type type = left.eval(buffer, stackPointer);
        if (right.eval(buffer, stackPointer + 1) != type) {
            throw new RuntimeException();
        }

        switch (type) {
            case INT:
                buffer.writeByte((byte) opCode);
                return type;
            default:
                throw new RuntimeException();
        }
    }
}
