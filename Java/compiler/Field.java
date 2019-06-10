package compiler;

import java.io.DataOutputStream;
import java.io.IOException;

public class Field {
    private Type type;

    private int nameIndex;

    private int descriptorIndex;

    private int referenceIndex;

    public Field(Type type, int nameIndex, int descriptorIndex, int referenceIndex) {
        this.type = type;
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.referenceIndex = referenceIndex;
    }

    public void emitCode(DataOutputStream outputStream) throws IOException {
        final int accessFlags = 0x0A;
        outputStream.writeShort(accessFlags);
        outputStream.writeShort(nameIndex);
        outputStream.writeShort(descriptorIndex);
        final int attributesCount = 0;
        outputStream.writeShort(attributesCount);
    }

    public GlobalVariable createReference() {
        return new GlobalVariable(type, referenceIndex);
    }
}
