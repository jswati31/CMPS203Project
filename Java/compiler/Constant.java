package compiler;

import java.io.DataOutputStream;
import java.io.IOException;

public interface Constant {

    void emitCode(DataOutputStream outputStream) throws IOException;

    short getIndex();

}
