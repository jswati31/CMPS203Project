package compiler;

import java.util.HashMap;

public class Seq extends Command{

    Command c1;
    Command c2;

    Seq(Command c1, Command c2){
        this.c1 = c1;
        this.c2 = c2;
    }

    public void eval(HashMap<String,Integer> map) throws Exception{
        c1.eval(map);
        c2.eval(map);
    }
}
