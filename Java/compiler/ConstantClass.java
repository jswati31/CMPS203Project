package compiler;

import java.io.DataOutputStream;
import java.io.IOException;

public class ConstantClass extends AbstractConstant {

    protected int nameIndex;

    public ConstantClass(short index, int nameIndex) {
        super(index, ConstantType.CLASS);
        this.nameIndex = nameIndex;
    }

    @Override
    protected void emitSpecializedCode(DataOutputStream outputStream) throws IOException {
        outputStream.writeShort(nameIndex);
    }
}
