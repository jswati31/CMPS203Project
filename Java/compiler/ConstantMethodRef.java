package compiler;

import java.io.DataOutputStream;
import java.io.IOException;

public class ConstantMethodRef extends AbstractConstant {

    protected int classIndex;

    protected int nameAndTypeIndex;

    public ConstantMethodRef(short index, int classIndex, int nameAndTypeIndex) {
        super(index, ConstantType.METHODREF);
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    @Override
    protected void emitSpecializedCode(DataOutputStream outputStream) throws IOException {
        outputStream.writeShort(classIndex);
        outputStream.writeShort(nameAndTypeIndex);
    }
}
