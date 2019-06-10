package compiler;

import java.io.DataOutputStream;
import java.io.IOException;

public class ConstantFieldRef extends AbstractConstant {

    protected int classIndex;

    protected int nameAndTypeIndex;

    public ConstantFieldRef(short index, int classIndex, int nameAndTypeIndex) {
        super(index, ConstantType.FIELDREF);
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    @Override
    protected void emitSpecializedCode(DataOutputStream outputStream) throws IOException {
        outputStream.writeShort(classIndex);
        outputStream.writeShort(nameAndTypeIndex);
    }
}
