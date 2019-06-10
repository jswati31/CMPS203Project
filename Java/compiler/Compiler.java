package compiler;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CodePointBuffer;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Interpreter for the WHILE Language
 */
public class Compiler
{
    public static void main( String[] args ) throws Exception {
        /*HashMap<String,Integer> map = new HashMap<>();
        System.out.println("======================================================");
        System.out.println("Arithmetic Expression : ");
        System.out.println("======================================================");

        try {
            System.out.print("Test Case : 5 + 2 * (10 + 10) Expected Result = 45 , Compared to Value = 45 => ");
            int t1 = new Add(new Int(5), new Int(new Mul(new Int(2), new Int(new Add(new Int(10), new Int(10)).eval(map))).eval(map))).eval(map);
            int compareTo = 45;
            System.out.println(t1 == compareTo);
        }catch(Exception e) {
            e.printStackTrace();
        }

        try {
            System.out.print("Test Case : 10 - 2 Expected Result = 8 , Compared to Value = 10 => ");
            int t1 = new Sub(new Int(10),new Int(2)).eval(map);
            int compareTo = 10;
            System.out.println(t1 == compareTo);
        }catch(Exception e) {
            e.printStackTrace();
        }

        try {
            System.out.print("Test Case : 10 / 2 Expected Result = 5 , Compared to Value = 5 => ");
            int t1 = new Division(new Int(10),new Int(2)).eval(map);
            int compareTo = 5;
            System.out.println(t1 == compareTo);
        }catch(Exception e) {
            e.printStackTrace();
        }

        try {
            System.out.println("Test Case : 10 / 0 Expected Result = Error  => ");
            int t1 = new Division(new Int(10),new Int(0)).eval(map);
            int compareTo = 5;
            System.out.println(t1 == compareTo);
        }catch(Exception e) {
            e.printStackTrace();
        }

        try{
            System.out.println("Test Case : 99999999 * (99999999 + 99999999) Expected Result = Overflow Exception");
            int t6 = new Mul(new Int(99999999), new Int(new Add(new Int(99999999), new Int(99999999)).eval(map))).eval(map);
            int compareTo = 5;
            System.out.println((t6==compareTo));
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("\n");
        System.out.println("======================================================");
        System.out.println("Boolean Expressions :");
        System.out.println("======================================================");
        try{
            System.out.print("true AND false =>");
            Bexp b1 = new Bexp(new Bool(true),new Bool(false),"And");
            System.out.println(b1.eval(map)==1?"true":"false");
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            System.out.print("true AND true =>");
            Bexp b1 = new Bexp(new Bool(true),new Bool(true),"And");
            System.out.println(b1.eval(map)==1?"true":"false");
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            System.out.print("true OR false =>");
            Bexp b2 = new Bexp(new Bool(true),new Bool(false),"Or");
            System.out.println(b2.eval(map)==1?"true":"false");
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            System.out.print("false OR false =>");
            Bexp b2 = new Bexp(new Bool(false),new Bool(false),"Or");
            System.out.println(b2.eval(map)==1?"true":"false");
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            System.out.print("NOT true =>");
            Bexp b3 = new Bexp(new Bool(true),"Not");
            System.out.println(b3.eval(map)==1?"true":"false");
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            System.out.print("NOT false =>");
            Bexp b3 = new Bexp(new Bool(false),"Not");
            System.out.println(b3.eval(map)==1?"true":"false");
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            System.out.print("Test Case : 1 == 1 =>");
            Bexp b4 = new Bexp(new Int(1),new Int(1),"Equals");
            System.out.println(b4.eval(map)==1?"true":"false");
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            System.out.print("Test Case : 1 > 2  =>");
            Bexp b5 = new Bexp(new Int(1),new Int(2),"GreaterThan");
            System.out.println(b5.eval(map)==1?"true":"false");
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            System.out.print("Test Case : 1 < 2  =>");
            Bexp b5 = new Bexp(new Int(1),new Int(2),"LessThan");
            System.out.println(b5.eval(map)==1?"true":"false");
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            System.out.println("Test Case for Ge : Operator that does not exist=>");
            Bexp b6 = new Bexp(new Int(1),new Int(1),"Ge");
            System.out.println(b6.eval(map)==1?"true":"false");
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("\n");
        System.out.println("======================================================");
        System.out.println("Variables and Assignment :");
        System.out.println("======================================================");

        try{
            Var a = new Var("a");
            Int val = new Int(15);
            Assign a1 = new Assign(a,val);
            a1.eval(map);
            System.out.println("Declared a variable 'a' and assigned a value of 15");
            System.out.print("a => ");
            System.out.println(map.get("a"));
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Trying to assign a string value to variables gives a compile time error");


        System.out.println("\n");
        System.out.println("======================================================");
        System.out.println("Program to calculate Factorial of a number");
        System.out.println("======================================================");
        System.out.println("Program is testing the following Expressions: ");
        System.out.println("1. Int");
        System.out.println("2. Var");
        System.out.println("3. Assign");
        System.out.println("3. Seq");
        System.out.println("4. While Loop");
        System.out.println("5. Multiplication");
        System.out.println("6. Subtraction");

        System.out.println("------------------------------------------------------");


        try{
            Int f1 = new Int(4);
            System.out.println("Factorial of 4 = " + factorial(map, f1));

            Int f2 = new Int(5);
            System.out.println("Factorial of 5 = " + factorial(map, f2));

        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("\n");
        System.out.println("======================================================");
        System.out.println("Program to check If Condition : ");
        System.out.println("======================================================");
        System.out.println("If (num1 < num2) : num1++ else num2++ ");
        System.out.println("------------------------------------------------------");

        try{
            System.out.println("num1 = 1 and num2 = 5");
            ifcheck(map,new Int(1),new Int(5));
            System.out.println("------------------------------------------------------");
            System.out.println("num1 = 7 and num2 = 5");
            ifcheck(map,new Int(7),new Int(5));

        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("\n");
        System.out.println("======================================================");
        System.out.println("Program to check Skip Condition : ");
        System.out.println("======================================================");
        System.out.println("If (num1 < num2) : num1++ else skip ");
        System.out.println("------------------------------------------------------");


        try{
            System.out.println("num1 = 1 and num2 = 5");
            skipcheck(map,new Int(1),new Int(5));
            System.out.println("------------------------------------------------------");
            System.out.println("num1 = 7 and num2 = 5");
            skipcheck(map,new Int(7),new Int(5));

        }catch(Exception e){
            e.printStackTrace();
        }


    }

    static int factorial(HashMap<String,Integer> map,Int f1) throws Exception{
        Var number = new Var("number");
        Assign a2 = new Assign(number,f1);
        a2.eval(map);

        Var fact = new Var("fact");
        Assign a3 = new Assign(fact,new Int(1));
        a3.eval(map);

        Bexp f12 = new Bexp(number,new Int(0),"GreaterThan");
        Assign s1 = new Assign(fact,new Mul(fact,number));
        Assign s2 = new Assign(number,new Sub(number,new Int(1)));

        Seq s = new Seq(s1,s2);
        WhileCondition w = new WhileCondition(f12,s);
        w.eval(map);

        return fact.eval(map);
    }

    static void ifcheck(HashMap<String,Integer> map,Int f1,Int f2) throws Exception{
        Var num1 = new Var("num1");
        Assign a1 = new Assign(num1,f1);
        a1.eval(map);

        Var num2 = new Var("num2");
        Assign a2 = new Assign(num2,f2);
        a2.eval(map);
        Bexp b5 = new Bexp(f1,f2,"LessThan");
        Assign s1 = new Assign(num1,new Add(num1,new Int(1)));
        Assign s2 = new Assign(num2,new Add(num2,new Int(1)));
        IfCondition i = new IfCondition(s1,s2,b5);
        i.eval(map);
        System.out.println("num 1 = " + num1.eval(map));
        System.out.println("num 2 = " + num2.eval(map));
    }

    static void skipcheck(HashMap<String,Integer> map,Int f1,Int f2) throws Exception{
        Var num1 = new Var("num1");
        Assign a1 = new Assign(num1,f1);
        a1.eval(map);

        Var num2 = new Var("num2");
        Assign a2 = new Assign(num2,f2);
        a2.eval(map);
        Bexp b5 = new Bexp(f1,f2,"LessThan");
        Assign s1 = new Assign(num1,new Add(num1,new Int(1)));
        Skip s2 = new Skip();
        IfCondition i = new IfCondition(s1,s2,b5);
        i.eval(map);
        System.out.println("num 1 = " + num1.eval(map));
        System.out.println("num 2 = " + num2.eval(map));
    }*/

        List<String> files = Arrays.asList(args).stream()
                .filter(f -> f.endsWith(".c"))
                .filter(f -> Files.exists(Paths.get(f)))
                .collect(Collectors.toList());

        Compiler compiler = new Compiler();
        List<ClassFile> classFiles = null;
        try {
            classFiles = compiler.compile(files);
        } catch (IOException e) {
            e.printStackTrace();
        }
        compiler.emitCode(classFiles);

    }

    List<ClassFile> compile(List<String> files) throws IOException {
        List<ClassFile> classFiles = new ArrayList<>();
        for (String file : files) {
            byte[] byteArray = Files.readAllBytes(Paths.get(file));
            CharStream charStream = CodePointCharStream.fromBuffer(CodePointBuffer.withBytes(ByteBuffer.wrap(byteArray)));

            CustomLexer lexer = new CustomLexer(charStream);
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);

            CustomParser parser = new CustomParser(tokenStream);
            CustomParser.ProgramContext programContext = parser.program();

            ParseTreeWalker walker = new ParseTreeWalker();

            CustomParseTreeListener listener = new CustomParseTreeListener(file.replace(".c", ""));
            walker.walk(listener, programContext);
            classFiles.add(listener.getClassFile());
        }
        return classFiles;
    }

    void emitCode(List<ClassFile> classFiles) throws Exception {
        for (ClassFile classFile : classFiles) {
            classFile.evaluate();
        }
    }


}
