package compiler;

import java.util.HashMap;

public class WhileCondition extends Command{

    Bexp b; // Condition
    Command c; //Command to execute
    WhileCondition(Bexp b,Command c){
        this.b =b;
        this.c = c;

    }


    public void eval(HashMap<String, Integer> map) throws Exception{
        if(b.eval(map)==1){
            c.eval(map);
            this.eval(map);
        }else{

        }
    }
}
