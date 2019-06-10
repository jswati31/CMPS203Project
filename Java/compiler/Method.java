package compiler;

import java.io.DataOutputStream;
import java.io.IOException;


public class Method {

    private final Type returnType;
    private final short referenceIndex;
    private final int nameIndex;
    private final int descriptorIndex;
    private final int attributeNameIndex;
    private Block block;

    public Method(Type returnType, short referenceIndex, int nameIndex, int descriptorIndex, int attributeNameIndex, Block block) {
        this.returnType = returnType;
        this.referenceIndex = referenceIndex;
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.attributeNameIndex = attributeNameIndex;
        this.block = block;
    }

    public void emitCode(DataOutputStream outputStream) throws IOException {
        outputStream.writeShort(9);
        outputStream.writeShort(nameIndex);
        outputStream.writeShort(descriptorIndex);
        outputStream.writeShort(1);

        MethodBytecodeBuffer buffer = new MethodBytecodeBuffer(attributeNameIndex, outputStream);
        this.block.emitCode(buffer);
        buffer.flush();
    }

    public Type getReturnType() {
        return returnType;
    }

    public short getReferenceIndex() {
        return referenceIndex;
    }
}
