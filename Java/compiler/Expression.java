package compiler;


import java.io.IOException;
import java.util.HashMap;

public abstract class Expression {

    Expression e1,e2;
    String operator;

    Expression(){

    }

    public Expression(Expression e1,Expression e2,String operator ){
        this.e1 = e1;
        this.e2 = e2;
        this.operator = operator;
    }

    public Expression(Expression e1,String operator ){
        this.e1 = e1;
        this.operator = operator;
    }

    public abstract Type eval(MethodBytecodeBuffer buffer, int stackPointer) throws IOException;

    int eval(HashMap<String,Integer> map) throws Exception {
        return 0;
    }

}
