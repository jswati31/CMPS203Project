package compiler;

import java.io.IOException;

public abstract class Statement {

    public abstract void emitCode(MethodBytecodeBuffer buffer) throws IOException;

}
