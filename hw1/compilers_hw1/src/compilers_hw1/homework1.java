//terkel and jos hw1

package compilers_hw1;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Scanner;

/*
* Hints in doing the HW:
*   a) Make sure you first understand what you are doing.
*   b) Watch Lecture 2 focusing on the code described
 */


class homework1 {
	private static boolean debug =false;
    // Abstract Syntax Tree
    static final class AST {
        public final String value;
        public final AST left; // can be null
        public final AST right; // can be null

        private AST(String val,AST left, AST right) {
            value = val;
            this.left = left;
            this.right = right;
        }

        public static AST createAST(Scanner input) {
            if (!input.hasNext())
                return null;

            String value = input.nextLine();
            if (value.equals("~"))
                return null;

            return new AST(value, createAST(input), createAST(input));
        }
    }

    static final class Variable{
        // Think! what does a Variable contain?x
    }

    static final class SymbolTable{
        // Think! what does a SymbolTable contain?
        public static int ADR;
        public static Hashtable<String, Integer> ST;
//        public SymbolTable(){
//        	ADR=5;
//        	ST =  new Hashtable<String,Integer>();
//        }
        public static SymbolTable generateSymbolTable(AST tree){
            // TODO: create SymbolTable from AST
        	ADR=5;
        	ST =  new Hashtable<String,Integer>();
            return null;
        }
    }

    private static void generatePCode(AST ast, SymbolTable symbolTable) {
        // TODO: go over AST and print code
    	if (debug) System.out.println("generatePcode ");
    	if(ast==null)
			return;
        if (debug) System.out.println(ast.value);
        
    	code(ast,symbolTable);
    }

    private static void code(AST ast, SymbolTable symbolTable) {
		// TODO Auto-generated method stub
    	if (debug) System.out.println("code ");
    	if(ast==null)
			return;
        if (debug) System.out.println(ast.value);
        
    	if(ast.value.equals("program")){
    		coded(ast.left,symbolTable);
    		if(ast.right!=null){
    			code(ast.right ,symbolTable);
    		}
    	}
    	if(ast.value.equals("content")){
			coded(ast.left,symbolTable);
			code(ast.right ,symbolTable);
    	}
    	if(ast.value.equals("statementsList")){
    		code(ast.left,symbolTable);
    		code(ast.right,symbolTable);
    	}
    	if(ast.value.equals("assignment")){
    		codel(ast.left,symbolTable);
    		coder(ast.right,symbolTable);
    		System.out.println("sto");
    	}
  
	}

	private static void codel(AST ast, SymbolTable symbolTable) {
		// TODO Auto-generated method stub
		if (debug) System.out.println("codel ");
		if(ast==null)
			return;
        if (debug) System.out.println(ast.value);

		if(ast.value.equals("identifier")){
			System.out.println("ldc " + SymbolTable.ST.get(ast.left.value)+"");
    	}
	}

	private static void coder(AST ast, SymbolTable symbolTable) {
		if (debug) System.out.println("coder ");
		if(ast==null)
			return;
        if (debug) System.out.println(ast.value);
		if(ast.value.equals("constInt")){
			System.out.println("ldc " + SymbolTable.ST.get(ast.left.value)+"");
		}
		
	}

	private static void coded(AST ast, SymbolTable symbolTable) {
		// TODO Auto-generated method stub
		if (debug) System.out.println("coded ");
		if(ast==null)
			return;
        if (debug) System.out.println(ast.value);
        
		if(ast.value.equals("scope"))
			coded(ast.left, symbolTable);
		
		if(ast.value.equals("declarationsList")){
			coded(ast.left, symbolTable);
			coded(ast.right, symbolTable);
		}
		if(ast.value.equals("var")){
			SymbolTable.ST.put(ast.left.left.value, SymbolTable.ADR);
			switch(ast.right.value){
			case "int": SymbolTable.ADR+=1; break;
			}
		}
		
	}

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("main start");
    	Scanner scanner = new Scanner(new File("tree3.txt"));
//    	Scanner scanner = new Scanner(System.in);
        AST ast = AST.createAST(scanner);
        SymbolTable symbolTable = SymbolTable.generateSymbolTable(ast);
        generatePCode(ast, symbolTable);

    }

}