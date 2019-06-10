package compiler;

import java.util.HashMap;

public class Assign extends Command{
    Var a;
    Expression b;

    public Assign(Var a, Expression b){
        this.a = a;
        this.b = b;
    }
    @Override
    public void eval(HashMap<String,Integer> map) throws Exception {
        map.put(a.n,b.eval(map));
    }
}
