package compiler;

import java.io.IOException;

public class BlockStatement extends Statement {

    private Block block;

    public BlockStatement(Block block) {
        this.block = block;
    }

    @Override
    public void emitCode(MethodBytecodeBuffer buffer) throws IOException {
        this.block.emitCode(buffer);
    }
}
