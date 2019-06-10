package compiler;

import java.io.IOException;
import java.util.HashMap;

public class AssignmentStatement extends Statement {

    private final Variable variable;

    private final Expression expression;


    public AssignmentStatement(Variable variable, Expression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public void emitCode(MethodBytecodeBuffer buffer) throws IOException {
        if (expression.eval(buffer, 0) != variable.emitLeftSideCode(buffer)) {
            throw new RuntimeException();
        }
    }

}
