package compiler;

import java.util.HashMap;

public class Var extends Expression{
    String n;

    public Var(String n){
        this.n = n;
    }
    @Override
    public int eval(HashMap<String,Integer> map) throws Exception{
        return map.get(n);
    }
}
