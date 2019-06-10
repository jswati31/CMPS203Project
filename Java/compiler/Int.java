package compiler;

import java.util.HashMap;

public class Int extends Expression {
    private final int n;

    public Int(int n){
        this.n = n;
    }
    @Override
    public int eval(HashMap<String,Integer> map) throws Exception {
        return n;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
