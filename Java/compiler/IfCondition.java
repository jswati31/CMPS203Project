package compiler;

import java.util.HashMap;

public class IfCondition extends Command{

    Command c1,c2;
    Bexp b;

    IfCondition(Command c1,Command c2,Bexp b){
        this.c1 = c1;
        this.c2 = c2;
        this.b = b;

    }
    public void eval(HashMap<String, Integer> map) throws Exception{
        if(b.eval(map) ==1)
            c1.eval(map);
        else c2.eval(map);
    }
}
