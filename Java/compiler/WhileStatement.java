package compiler;


import java.io.IOException;
import java.util.HashMap;

import static compiler.Bytecode.*;

public class WhileStatement extends ConditionStatement {
    Bexp b; // Condition
    Command c; //Command to execute

    public void eval(HashMap<String, Integer> map) throws Exception{
        if(b.eval(map)==1){
            c.eval(map);
            this.eval(map);
        }else{

        }
    }

    public WhileStatement(ComparisonOperator operator, Expression left, Expression right, Statement pop) {
        super(operator, left, right, pop);
    }

    @Override
    public void emitCode(MethodBytecodeBuffer buffer) throws IOException {

        int pc = buffer.commit(3);
        this.statement.emitCode(buffer);

        int offset = buffer.currentBytecodeIndex() - pc;
        if (offset <= Short.MAX_VALUE) {
            buffer.patchUnsignedByte(pc, GOTO);
            buffer.patchShort(pc + 1, (short) offset);
            pc = pc + 3;
        } else {
            buffer.insert(pc, 2);
            buffer.patchUnsignedByte(pc, GOTO_W);
            buffer.patchInt(pc + 1, offset);
            pc = pc + 5;
        }

        left.eval(buffer, 0);
        right.eval(buffer, 1);
        offset = pc - buffer.currentBytecodeIndex();

        switch (this.operator) {
            case EQUAL:
                buffer.writeByte(IF_ICMPEQ);
                break;
            case NOT_EQUAL:
                buffer.writeByte(IF_ICMPNE);
                break;
            case GREATER:
                buffer.writeByte(IF_ICMPGT);
                break;
            case GREATER_OR_EQUAL:
                buffer.writeByte(IF_ICMPGE);
                break;
            case LESS:
                buffer.writeByte(IF_ICMPLT);
                break;
            case LESS_OR_EQUAL:
                buffer.writeByte(IF_ICMPLE);
                break;
            default:
                throw new RuntimeException();
        }

        if (offset >= Short.MIN_VALUE) {
            buffer.writeShort((short) offset);
        } else {
            buffer.writeShort((short) 8);
            buffer.writeByte(GOTO_W);
            buffer.writeInt(offset - 3);
        }
    }
}
