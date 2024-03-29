package compiler;


import java.io.IOException;
import java.util.List;

import static compiler.Bytecode.*;

public class CallExpression extends Expression {

    private final String name;

    private final List<Expression> arguments;

    private ClassFile classFile;

    public CallExpression(String functionName, List<Expression> arguments, ClassFile classFile) {
        this.name = functionName;
        this.arguments = arguments;
        this.classFile = classFile;
    }

    @Override
    public Type eval(MethodBytecodeBuffer buffer, int stackPointer) throws IOException {
        StringBuilder id = new StringBuilder(this.name);
        id.append("(");

        for (int i = 0; i < arguments.size(); i++) {
            Expression argument = arguments.get(i);
            switch (argument.eval(buffer, stackPointer + i)) {
                case INT:
                    id.append(Type.INT.getDescriptor());
                    break;
                default:
                    throw new RuntimeException();
            }
        }

        id.append(")");
        String identifier = id.toString();

        Method method = this.classFile.getMethod(identifier);
        Type returnType = method.getReturnType();

        switch (returnType) {
            case INT:
                buffer.setMaxStack(stackPointer + 1);
            default:
        }

        buffer.writeByte(INVOKESTATIC);
        buffer.writeShort(method.getReferenceIndex());

        return returnType;
    }
}
