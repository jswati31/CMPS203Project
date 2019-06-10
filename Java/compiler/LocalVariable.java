package compiler;


import static compiler.Bytecode.*;

public class LocalVariable extends Variable {

    public LocalVariable(Type type, int index) {
        super(type, index);
    }

    @Override
    public void emitCode(MethodBytecodeBuffer buffer, boolean isLoad) {
        int codeOffset;
        switch (this.type) {
            case INT:
                codeOffset = isLoad ? 0 : ISTORE - ILOAD;
                break;
            default:
                throw new RuntimeException();
        }

        if (index <= 3) {
            int shortOpcodeBase = ILOAD_0 + codeOffset;
            buffer.writeByte((byte) (shortOpcodeBase + index));
            return;
        }

        int longOpcode = ILOAD + codeOffset;
        if (index <= 255) {
            buffer.writeByte((byte) longOpcode);
            buffer.writeByte((byte) index);
        } else {
            buffer.writeByte(WIDE);
            buffer.writeByte((byte) longOpcode);
            buffer.writeShort((short) index);
        }
    }


}
