package compiler;

import java.io.DataOutputStream;
import java.io.IOException;


public class ConstantNameAndType extends AbstractConstant {

    protected int nameIndex;

    protected int descriptorIndex;

    public ConstantNameAndType(short index, int nameIndex, int descriptorIndex) {
        super(index, ConstantType.NAMEANDTYPE);
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
    }

    @Override
    protected void emitSpecializedCode(DataOutputStream outputStream) throws IOException {
        outputStream.writeShort(this.nameIndex);
        outputStream.writeShort(this.descriptorIndex);
    }
}
