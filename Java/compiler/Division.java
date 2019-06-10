package compiler;

import java.util.HashMap;

public class Division extends Expression {
    private final Expression e1, e2;

    public Division(Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public int eval(HashMap<String, Integer> map) throws Exception {
        if (e2.eval(map) == 0)
            throw new Exception("Division by 0");
        else {
            return e1.eval(map) / e2.eval(map);
        }
    }
}
