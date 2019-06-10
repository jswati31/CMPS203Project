package compiler;

import java.io.IOException;

import static compiler.Bytecode.POP;


public class CallStatement extends Statement {

    private Expression expression;

    public CallStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void emitCode(MethodBytecodeBuffer buffer) throws IOException {
        switch (expression.eval(buffer, 0)) {
            case INT:
                buffer.writeByte(POP);
        }
    }
}
