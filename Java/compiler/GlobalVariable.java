package compiler;

import java.io.IOException;

import static compiler.Bytecode.*;


public class GlobalVariable extends Variable {

    public GlobalVariable(Type type, int referenceIndex) {
        super(type, referenceIndex);
    }

    @Override
    public void emitCode(MethodBytecodeBuffer buffer, boolean isLoad) throws IOException {
        buffer.writeByte(isLoad ? GETSTATIC : PUTSTATIC);
        buffer.writeShort((short) this.index);
    }
}
