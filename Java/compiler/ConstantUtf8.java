package compiler;

import java.io.DataOutputStream;
import java.io.IOException;

public class ConstantUtf8 extends AbstractConstant {

    protected String bytes;

    public ConstantUtf8(short index, String bytes) {
        super(index, ConstantType.UTF8);
        this.bytes = bytes;
    }

    @Override
    protected void emitSpecializedCode(DataOutputStream outputStream) throws IOException {
        outputStream.writeUTF(bytes);
    }
}
