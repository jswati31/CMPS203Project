package compiler;

import java.util.HashMap;

public class Bool extends Expression {
    boolean b;
    Bool(boolean b){
        this.b=b;
    }

    int eval(HashMap<String,Integer> map) throws Exception{
        return b==true?1:0;
    }
}
