package compiler;

public class Helper {
    public static int checkOverflow(long val){

        if(val > Integer.MAX_VALUE || val < Integer.MIN_VALUE)
            throw new ArithmeticException("Overflow");
        else return (int) val;
    }
}
