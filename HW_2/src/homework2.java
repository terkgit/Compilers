
//terkel and jos hw1

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
//added test
//
/*
* Hints in doing the HW:
*   a) Make sure you first understand what you are doing.
*   b) Watch Lecture 2 focusing on the code described
 */


class homework2 {
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
    	private String name;
    	private  int Addr;
    	private String type;
    	public Variable(String name, int addr, String type) {
			this.name = name;
			Addr = addr;
			this.type = type;
		}
    	
    	/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
		
	
		/**
		 * @return the type
		 */
		public String getType() {
			return type;
		}
		/**
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}
		/**
		 * @return the addr
		 */
		public int getAddr() {
			return Addr;
		}
		/**
		 * @param addr the addr to set
		 */
		public void setAddr(int addr) {
			Addr = addr;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Variable other = (Variable) obj;
			if (Addr != other.Addr)
				return false;
			if (type == null) {
				if (other.type != null)
					return false;
			} else if (!type.equals(other.type))
				return false;
			return true;
		}
    	
    }

    static final class SymbolTable{
        // Think! what does a SymbolTable contain?
        public static int ADR;
        public static int LABEL;
        public static ArrayList<Variable> ST;
        public SymbolTable(){
        	LABEL=0;
        	ADR=5;
        	ST =  new ArrayList<Variable>();
        }
    	private static void coded(AST ast) {
    		// TODO Auto-generated method stub
    		if (debug) System.out.println("coded ");
    		if(ast==null)
    			return;
            if (debug) System.out.println(ast.value);
            
            switch(ast.value){
            	case("content"):
            		coded(ast.left);
            		break;
            	case("scope"):
    				coded(ast.left);
            		break;
    			case("declarationsList"):
	    			coded(ast.left);
	    			coded(ast.right);
	    			break;
    			case("var"):
	    			ST.add(new Variable(ast.left.left.value,ADR,ast.right.value));
	    			switch(ast.right.value){
		    			case "int": 
		    				ADR+=1; 
		    			break;
		    			case "real": 
		    				ADR+=1; 
		    			break;
		    			case "bool": 
		    				ADR+=1; 
		    			break;
		    			default:
		    				System.out.println("unknown coded type: " +ast.right.value);
		    				break;
	    			}
	    			break;
	    		default:
	    			System.out.println("unknown coded: "+ast.value);
	    			break;
            }
    	}

    	public static SymbolTable generateSymbolTable(AST tree){
            // TODO: create SymbolTable from AST
        	SymbolTable st=new SymbolTable();
        	coded(tree.right);
        	return st;
        }
	
	public static int getAddr(String name) {
			// TODO Auto-generated method stub
			for(Variable var : ST){
				if(var.getName().equals(name))
					return var.getAddr();
			}
			return -1;
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
        
        switch(ast.value){
        case("program"):
        	
     		if(ast.right!=null){
    			code(ast.right ,symbolTable);
    		}     
        	break;
        case("content"):
			code(ast.right ,symbolTable);
    		break;
        case("statementsList"):
    		code(ast.left,symbolTable);
    		code(ast.right,symbolTable);
    		break;
        case("assignment"):
    		codel(ast.left,symbolTable);
    		coder(ast.right,symbolTable);
    		System.out.println("sto");
    		break;
        case("print"):
    		coder(ast.left,symbolTable);
    		System.out.println("print");
    		break;
        case("if"):
        	if(ast.right.value.equals("else")){

        		int la=SymbolTable.LABEL++;
        		int lb=SymbolTable.LABEL++;
        		
        		coder(ast.left,symbolTable);
        		System.out.println("fjp "+"L"+la);
        		
        		code(ast.right.left,symbolTable);
        		System.out.println("ujp "+"L"+lb);
        		
        		System.out.println("L"+la+":");
        		
        		code(ast.right.right,symbolTable);
        		System.out.println("L"+lb+":");
        		
        	}
        	else{
        		int la=SymbolTable.LABEL++;
        		
        		coder(ast.left,symbolTable);
        		System.out.println("fjp "+"L"+la);
        		
        		code(ast.right,symbolTable);
        		System.out.println("L"+la+":");
        		
        	}
    		break;
        case("while"):
        	int la=SymbolTable.LABEL++;
        	int lb=SymbolTable.LABEL++;
        	System.out.println("L"+la+":");
        	coder(ast.left,symbolTable);
        	System.out.println("fjp "+"L"+lb);
        	code(ast.right,symbolTable);
        	System.out.println("ujp "+"L"+la);
        	System.out.println("L"+lb+":");
        	break;
    	default:
    		System.out.println("unknown code: "+ast.value);
    		break;
        }
	}

	private static void codel(AST ast, SymbolTable symbolTable) {
		// TODO Auto-generated method stub
		if (debug) System.out.println("codel ");
		if(ast==null)
			return;
        if (debug) System.out.println(ast.value);

		if(ast.value.equals("identifier")){
			System.out.println("ldc " + SymbolTable.getAddr(ast.left.value));
    	}
	}

	private static void coder(AST ast, SymbolTable symbolTable) {
		if (debug) System.out.println("coder ");
		if(ast==null)
			return;
        if (debug) System.out.println(ast.value);
        
        switch(ast.value){
        case("false"):
        	System.out.println("ldc 0");
        break;
        case("true"):
        	System.out.println("ldc 1");
        break;
        case("constInt"):
			System.out.println("ldc " + ast.left.value);
        break;
        case("constReal"):
			System.out.println("ldc " + ast.left.value);
        break;
        case("not"):
        	coder(ast.left,symbolTable);
			System.out.println("not");
        break;
        case("negative"):
        	coder(ast.left,symbolTable);
			System.out.println("neg");
        break;
        case("identifier"):
        	codel(ast,symbolTable);
        	System.out.println("ind");
        break;
        case("plus"):
        	coder(ast.left,symbolTable);
        	coder(ast.right,symbolTable);
        	System.out.println("add");
        break;
        case("minus"):
        	coder(ast.left,symbolTable);
        	coder(ast.right,symbolTable);
        	System.out.println("sub");
        break;
        case("or"):
        	coder(ast.left,symbolTable);
        	coder(ast.right,symbolTable);
        	System.out.println("or");
        break;
        case("and"):
        	coder(ast.left,symbolTable);
        	coder(ast.right,symbolTable);
        	System.out.println("and");
        break;
        case("multiply"):
        	coder(ast.left,symbolTable);
        	coder(ast.right,symbolTable);
        	System.out.println("mul");
        break; 
        case("divide"):
        	coder(ast.left,symbolTable);
        	coder(ast.right,symbolTable);
        	System.out.println("div");
        break;        
        case("lessThan"):
        	coder(ast.left,symbolTable);
        	coder(ast.right,symbolTable);
        	System.out.println("les");
        break;      
        case("greaterThan"):
        	coder(ast.left,symbolTable);
        	coder(ast.right,symbolTable);
        	System.out.println("grt");
        break;        
        case("lessOrEquals"):
        	coder(ast.left,symbolTable);
        	coder(ast.right,symbolTable);
        	System.out.println("leq");
        break;        
        case("greaterOrEquals"):
        	coder(ast.left,symbolTable);
        	coder(ast.right,symbolTable);
        	System.out.println("geq");
        break;        
        case("notEquals"):
        	coder(ast.left,symbolTable);
        	coder(ast.right,symbolTable);
        	System.out.println("neq");
        break;      
        case("equals"):
        	coder(ast.left,symbolTable);
        	coder(ast.right,symbolTable);
        	System.out.println("equ");
        break;
        default:
        	System.out.println("unknown coder: "+ast.value);
    	break;
		}
	}

//	public static void main(String[] args) {
	public static void main(String[] args) throws FileNotFoundException {

    	Scanner scanner = new Scanner(new File("input\\tree3.txt"));
//    	Scanner scanner = new Scanner(System.in);
        AST ast = AST.createAST(scanner);
        SymbolTable symbolTable = SymbolTable.generateSymbolTable(ast);
        generatePCode(ast, symbolTable);

    }

}