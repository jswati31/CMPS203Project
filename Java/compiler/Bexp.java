package compiler;

import java.util.HashMap;

public class Bexp extends Expression{

    Bexp(Expression e1, Expression e2, String operator){
        this.e1 = e1;
        this.e2 = e2;
        this.operator = operator;
    }

    Bexp(Expression e1, String operator){
        this.e1 = e1;
        this.operator = operator;
    }

    int eval(HashMap<String, Integer> map) throws Exception{
        if (operator.equals("Equals"))
            return e1.eval(map)==e2.eval(map)?1:0;
        else if (operator.equals("LessThan"))
            return e1.eval(map)<e2.eval(map)?1:0;
        else if (operator.equals("GreaterThan"))
            return e1.eval(map)>e2.eval(map)?1:0;
        else if (operator.equals("And"))
            return e1.eval(map)==1 && e2.eval(map)==1?1:0;
        else if (operator.equals("Or"))
            return e1.eval(map)==1 || e2.eval(map)==1?1:0;
        else if (operator.equals("Not"))
            return e1.eval(map)==0?1:0;
        else{
            throw new Exception("Operator not found");
        }

    }

}
