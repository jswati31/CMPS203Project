package compiler;

import java.util.HashMap;

public class Sub extends Expression{
    private final Expression e1, e2;

    public Sub(Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public int eval(HashMap<String,Integer> map) throws Exception {
        Helper.checkOverflow((long)e1.eval(map) - (long)e2.eval(map));
        return e1.eval(map) - e2.eval(map);
    }
}
