package compiler;

import java.io.DataOutputStream;
import java.io.IOException;

public class ConstantInteger extends AbstractConstant {

    protected int bytes;

    public ConstantInteger(short index, int bytes) {
        super(index, ConstantType.INTEGER);
        this.bytes = bytes;
    }

    @Override
    protected void emitSpecializedCode(DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(this.bytes);
    }
}
