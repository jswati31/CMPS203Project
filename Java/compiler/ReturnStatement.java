package compiler;

import java.io.IOException;

public class ReturnStatement extends Statement {

    private Expression expression;

    public ReturnStatement(Expression expression) {
        this.expression = expression;
    }

    public ReturnStatement() {
    }

    @Override
    public void emitCode(MethodBytecodeBuffer buffer) throws IOException {
        if (expression == null) {
            buffer.writeByte(Bytecode.RETURN);
        } else if (expression.eval(buffer, 0) == Type.INT) {
            buffer.writeByte(Bytecode.IRETURN);
        } else {
            throw new RuntimeException();
        }
    }
}
