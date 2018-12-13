

//terkel and jos hw1

import java.util.Scanner;
import java.util.Stack;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
//added test
//
/*
* Hints in doing the HW:
*   a) Make sure you first understand what you are doing.
*   b) Watch Lecture 2 focusing on the code described
 */


class homework3 {
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

	static public class Array_info{
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Array_info [type=" + type + ", typeSize=" + typeSize + ", dim=" + dim + ", totalSize=" + totalSize
					+ ", subpart=" + subpart + ", l=" + l + ", ixa=" + Arrays.toString(ixa) + "]";
		}


		public String type;
    	public int typeSize;                      // typeSize
    	public ArrayList<Integer> dim;					//stores every dim size
    	public int totalSize;					//total array size
    	public int subpart;
    	public ArrayList<Integer>  l;					//lower 
    	public int[] ixa;
    	
    	
    	public Array_info(){
    		dim= new ArrayList<Integer>();
    		typeSize=0;
    		totalSize=0;
    		subpart=0;
    		ixa=null;
    		l= new ArrayList<Integer>();
    		type="";
    	}
	}
//    static final class funcs{
//    	public String name;
//    	public  int Addr;
//    	public  int size;
//    	public String type;
//    }
    static final class Variable{
        // Think! what does a Variable contain?x
    	public String name;
    	public  int Addr;
    	public  int size;							//Var size / func total var size
    	public String type;				
    	public String pName;						//pointer type / func return type
    	public Array_info a_info;					
    	public int r_size;							//record size
    	public int f_depth;
    	public String frame;
    	
    	
    	public Variable(String name, int addr, String type, String pName) {
			this.name = name;
			Addr = addr;
			this.type = type;
			this.pName = pName;
			a_info=null;
			size=0;
			r_size=0;
			f_depth=0;
			
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
        public static int SLABEL;
        public static int FRAME;
        public static Stack<Integer> lStack;				//label stack
        public static ArrayList<Variable> ST;
        public static Variable curr_fun ;					//current funcntion in coded
        public static int TMP;
//        public static ArrayList<funcs> FT;
        public SymbolTable(){
        	LABEL=0;
        	ADR=0;
        	FRAME=0;
        	ST =  new ArrayList<Variable>();
        	lStack= new Stack<Integer>();
        	curr_fun = null;
        	TMP=0;
        }
        public void printST(){
        	System.out.println("name:\t\tstaticL\t\tadrs\t\ttype\t\tsize\t\tptype\t\trsize\t\tframe");
        	for(Variable v: ST){
        		System.out.println(v.name + "\t\t"+v.f_depth +"\t\t"+ v.Addr + "\t\t"+ v.type.substring(0, 3)
        								+"\t\t"+v.size+ "\t\t" + v.pName + "\t\t"+v.r_size +"\t\t"+v.frame);
        		if(v.a_info!=null){
        			System.out.println(v.a_info.toString());
        		}
        	}
        	
        }
    	private static void coded(AST ast) {
    		// TODO Auto-generated method stub
    		if (debug) System.out.println("coded ");
    		if(ast==null)
    			return;
            if (debug) System.out.println(ast.value);
            String frameName="MAIN";
            if(curr_fun!=null)
            	 frameName = curr_fun.name;                        
            switch(ast.value){
            	case("program"):
            		ADR=0;
            		Variable pv = new Variable(ast.left.left.left.value.toUpperCase(),ADR,ast.value,"");
            		pv.f_depth=0;
            		pv.frame=frameName;
            		pv.pName=pv.name;
            		ST.add(pv);
            		ADR+=5;
            		FRAME=0;
            		pv.size=5;
            		curr_fun=pv;
            		coded(ast.left);
            		coded(ast.right);
        		break;
            	case("procedure"):
            	case("function"): 
            		int fAdr=ADR;
            		ADR=0;
            		Variable fp = new Variable(ast.left.left.left.value.toUpperCase(),ADR,ast.value,"");
            		FRAME++;
            		fp.f_depth=FRAME;
            		fp.frame=frameName;
            		fp.pName=fp.name;
            		ST.add(fp);
            		ADR+=5;
            		fp.size=5;
            		curr_fun=fp;
            		coded(ast.left);
            		coded(ast.right);
            		FRAME--;
            		ADR=fAdr;
        		break;
            	case("inOutParameters"):
            		coded(ast.left);
            	break;
        		case("identifierAndParameters"):
    				coded(ast.right);
				break;
            	case("content"):
            		coded(ast.left);
            		break;
            	case("scope"):
    				coded(ast.left);
            		coded(ast.right);
            		break;
            	case("functionsList"):
            		coded(ast.left);
            		coded(ast.right);
            		break;
    			case("declarationsList"):
	    			coded(ast.left);
	    			coded(ast.right);
	    			break;
    			case("parametersList"):
    				coded(ast.left);
    				coded(ast.right);
    				break;
    			case("byReference"):
    				break;
    			case("byValue"):    				
    			case("var"):
    				Variable tmpVar=null;
    				String pName="";
	    			int curAdr=ADR;
	    			int size=0;
	    			Array_info inf =null;
	    			int rs=0;
	    			switch(ast.right.value){
		    			case "int": 
//		    				ADR+=1;
		    				size=1;
		    			break;
		    			case "real": 
//		    				ADR+=1; 
		    				size=1;
		    			break;
		    			case "bool": 
//		    				ADR+=1;
		    				size=1;
		    			break;
		    			case "pointer": 
//		    				ADR+=1; 
		    				size=1;
		    				if(ast.right.left.value.equals("identifier"))
		    					pName=ast.right.left.left.value;
		    				else
		    					pName=ast.right.left.value;
		    			break;
		    			case "array": 
		    				inf =  new Array_info(); 
		    				if(ast.right.right.value.equals("identifier"))
		    					inf.type= ast.right.right.left.value;
		    				else
		    					inf.type=ast.right.right.value;
		    				inf.typeSize=getTypeSize(inf.type);
		    			   	genArrInfo(ast.right.left,inf);
		    			   	calcArrInfo(inf);
		    			   	size=inf.totalSize;
		    			break;
		    			case "record":
	    					coded(ast.right.left);
	    					rs = ADR-curAdr;
	    					size=rs;
		    				
	    				break;
		    			case "identifier":
		    				tmpVar = getVar(ast.right.left.value.toUpperCase(), curr_fun.name);
		    				pName=ast.right.left.value.toUpperCase();
		    				size=2;
		    				break;
		    			default:
		    				System.out.println("unknown coded type: " +ast.right.value);
		    				break;
	    			}
	    			ADR+=size-rs;
	    			Variable v = new Variable(ast.left.left.value,curAdr,ast.right.value,pName);
	    			v.size=size;
	    			v.r_size=rs;
	    			v.a_info=inf;
	    			curr_fun.size+=size-rs;
	    			v.f_depth=FRAME;
	    			v.frame=frameName;
	    			if(tmpVar!=null){
	    				v.name=v.name.toUpperCase();
	    				v.type=tmpVar.type;
	    				v.size=2;
	    			}
    				ST.add(v);
	    			break;
	    		default:
	    			System.out.println("unknown coded: "+ast.value);
	    			break;
            }
    	}

    	public static void calcArrInfo(Array_info inf) {
			// TODO Auto-generated method stub
    		int sub=0;
    		for(int i=0;i<inf.l.size();i++){
    			int tmp=inf.l.get(i);
    			for(int j =inf.l.size()-1;j>i;j--){
    				tmp*=inf.dim.get(j);
    			}
    			sub+=tmp;
    		}
    		inf.subpart=sub*inf.typeSize;
    		inf.ixa=new int[inf.dim.size()];
    		for(int i=0;i<inf.dim.size();i++){
    			inf.ixa[i]=inf.typeSize;
    			for(int j =inf.dim.size()-1;j>i;j--){
    				inf.ixa[i]*=inf.dim.get(j);
    			}
    		}
    		
    		inf.totalSize=inf.typeSize;
    		for(int i=0;i<inf.dim.size();i++){
    			inf.totalSize*=inf.dim.get(i);
    		}
    		
    		
    		
			
		}
		public static int getTypeSize(String type) {
			// TODO Auto-generated method stub
			for(Variable var : ST){
				if(var.name.equals(type))
					return var.size;
			}
    		return 1;
		}
		private static void genArrInfo(AST ast,Array_info inf){
			// TODO Auto-generated method stub
    		if(ast==null)
    			return;
    		genArrInfo(ast.left, inf);
    		int l = Integer.parseInt(ast.right.left.left.value);
    		int u = Integer.parseInt(ast.right.right.left.value);
    		inf.dim.add(u-l+1);
    		inf.l.add(l);
    		
    			
    			
			return ;
		}
		public static SymbolTable generateSymbolTable(AST tree){
            // TODO: create SymbolTable from AST
        	SymbolTable st=new SymbolTable();
        	coded(tree);
        	return st;
        }
		public static SymbolTable generateRecordTable(AST tree){
            // TODO: create SymbolTable from AST
        	SymbolTable st=new SymbolTable();
        	coded(tree);
        	return st;
        }
	
//		public static int getAddr(String name) {
//				// TODO Auto-generated method stub
//				for(Variable var : ST){
//					if(var.name.equals(name))
//						return var.Addr;
//				}
//				return -1;
//			} 
//		public static String getPName(String name) {
//			// TODO Auto-generated method stub
//			for(Variable var : ST){
//				if(var.name.equals(name))
//					return var.pName;
//			}
//			return null;
//		} 
		public static Variable getVar(String name,String frame) {
			// TODO Auto-generated method stub
			
			for(Variable var : ST){
				if(var.name.equals(name) && var.frame.equals(frame))
					return var;
			}
			String parent=getParent(frame);
			if(parent!=null)
				return getVar(name,parent);
			return null;
		}
		private static String getParent(String name) {
			for(Variable var : ST){
				if(var.name.equals(name))
					return var.frame;
			}
			return null;
		}
		public static Variable getNextVar(String name,String frame) {
			// TODO Auto-generated method stub
			for(int i = 0 ;i<ST.size()-1 ;i++){
				if( ST.get(i).name.equals(name) && ST.get(i).frame.equals(frame))
					return ST.get(i+1);
			}
			String parent=getParent(frame);
			if(parent!=null)
				return getNextVar(name,parent);
			return null;
		}
		public static int getStatic(String name,String frame) {
			// TODO Auto-generated method stub
			Variable var =getVar(name,frame); 
//			for(Variable var : ST){
//				if(var.name.equals(name))
				if(var!=null)
					return FRAME-var.f_depth;
//			}
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
    private static int codec(AST ast, SymbolTable symbolTable, int la){
		if(ast==null)
			return 0;
    	int c=0;
    	switch(ast.value) {
    	case("caseList"):
    		c=codec(ast.left,symbolTable,la);
    		c++;
    		System.out.println("case_"+la+"_"+c+":");
    		code(ast.right.right,symbolTable);
    		System.out.println("ujp L"+la);
    		
//    		System.out.println("ujp case_"+la+"_"+lb);
    	break;
		default:
			System.out.println("unknown codec: "+ast.value);
		break;
    	}
    	return c;
    }
    private static void code(AST ast, SymbolTable symbolTable) {
		// TODO Auto-generated method stub
    	if (debug) System.out.println("code ");
    	if(ast==null)
			return;
        if (debug) System.out.println(ast.value);
        
        switch(ast.value){
        case("program"):
        	SymbolTable.FRAME=0;
        	SymbolTable.curr_fun = SymbolTable.ST.get(0);
            System.out.println(ast.left.left.left.value.toUpperCase()+":");
            System.out.println("ssp " + SymbolTable.curr_fun.size);
//        	System.out.println("ssp " + SymbolTable.getVar(ast.left.left.left.value.toUpperCase()).size);
//        	System.out.println("sep ?");
        	System.out.println("ujp " + ast.left.left.left.value.toUpperCase()+"_begin");
        	code(ast.right.left,symbolTable);
        	System.out.println(ast.left.left.left.value.toUpperCase()+"_begin:");
        	code(ast.right.right,symbolTable);
        	System.out.println("stp");
        	break;
        case("procedure"):
        	SymbolTable.FRAME++;
        	String pparent=SymbolTable.curr_fun.name;
        	SymbolTable.curr_fun= SymbolTable.getVar(ast.left.left.left.value.toUpperCase(),pparent);
            System.out.println(ast.left.left.left.value.toUpperCase()+":");
        	System.out.println("ssp " + SymbolTable.getVar(ast.left.left.left.value.toUpperCase(),pparent).size);
//        	System.out.println("sep ?");
        	System.out.println("ujp " + ast.left.left.left.value.toUpperCase()+"_begin");
        	code(ast.right.left,symbolTable);
        	System.out.println(ast.left.left.left.value.toUpperCase()+"_begin:");
        	code(ast.right.right,symbolTable);
        	SymbolTable.FRAME--;
        	System.out.println("retp");
        	
        	break;
        case("function"):
        	SymbolTable.FRAME++;
	        String fparent=SymbolTable.curr_fun.name;
	    	SymbolTable.curr_fun= SymbolTable.getVar(ast.left.left.left.value.toUpperCase(),fparent);
            System.out.println(ast.left.left.left.value.toUpperCase()+":");
        	System.out.println("ssp " + SymbolTable.getVar(ast.left.left.left.value.toUpperCase(),fparent).size);
//        	System.out.println("sep ?");
        	System.out.println("ujp " + ast.left.left.left.value.toUpperCase()+"_begin");
        	code(ast.right.left,symbolTable);
        	System.out.println(ast.left.left.left.value.toUpperCase()+"_begin:");
        	code(ast.right.right,symbolTable);
        	SymbolTable.FRAME--;
        	SymbolTable.curr_fun=SymbolTable.getVar(fparent,SymbolTable.getParent(fparent));
        	System.out.println("retf");
        	
        	break;
        case("content"):
        	code(ast.left ,symbolTable);
			code(ast.right ,symbolTable);
    		break;
        case("scope"):
        	code(ast.right ,symbolTable);
        	break;
        case("functionsList"):
        	code(ast.left ,symbolTable);
        	code(ast.right ,symbolTable);
        	break;
        	
        case("statementsList"):
    		code(ast.left,symbolTable);
    		code(ast.right,symbolTable);
    		break;
        case("call"):
        	SymbolTable.TMP=0;
        	Variable fvar =SymbolTable.getVar(ast.left.left.value.toUpperCase(),SymbolTable.curr_fun.name);
        	int mst = SymbolTable.FRAME-fvar.f_depth+1;
        	if(fvar.pName.equals(fvar.name)){
  	        	System.out.println("mst "+ mst);
	        	codeArg(ast.right,fvar,symbolTable);
	        	System.out.println("cup "+SymbolTable.TMP+" "+fvar.name);
        	}
        	else{
	        	System.out.println("mstf "+SymbolTable.getStatic(fvar.name, fvar.frame)+" "+fvar.Addr);
	        	codeArg(ast.right,fvar,symbolTable);
	        	System.out.println("smp " + SymbolTable.TMP);
	        	System.out.println("cupi "+ SymbolTable.getStatic(fvar.name,SymbolTable.curr_fun.name) +" "+ SymbolTable.getVar(fvar.name,SymbolTable.curr_fun.name).Addr);
	        	}
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
        case("while"):{
        	int la=SymbolTable.LABEL++;
        	int lb=SymbolTable.LABEL++;
        	SymbolTable.lStack.push(lb);
        	System.out.println("L"+la+":");
        	coder(ast.left,symbolTable);
        	System.out.println("fjp "+"L"+lb);
        	code(ast.right,symbolTable);
        	System.out.println("ujp "+"L"+la);
        	System.out.println("L"+lb+":");
        break;}
        case("switch"):{
        	int la=SymbolTable.LABEL++;
        	coder(ast.left,symbolTable);
	        System.out.println("neg");
	        System.out.println("ixj L"+la);
        	int c =codec(ast.right,symbolTable,la);
        	for(int i=c ;i>0;i--){
        		System.out.println("ujp case_"+la+"_"+i);
        	}
        	System.out.println("L"+la+":");
//        	System.out.println("L"+la+":");
        break;}
        case("break"):{
        		int lb =SymbolTable.lStack.pop();
        		System.out.println("ujp while_out_"+lb);
        		
        break;
        }
        
    	default:
    		System.out.println("unknown code: "+ast.value);
    	break;
        }
	}

	public static Variable codeArg(AST ast, Variable fvar,SymbolTable symbolTable) {
		// TODO Auto-generated method stub
		if(ast==null)
			return SymbolTable.getNextVar(fvar.pName,SymbolTable.curr_fun.name);
		Variable v = codeArg(ast.left,fvar ,symbolTable);
		SymbolTable.TMP+=v.size;
		switch(v.type){
		case("array"):
			codel(ast.right,symbolTable);
			System.out.println("movs " + v.size);
			break;
		case("int"):
			coder(ast.right,symbolTable);
			break;
		case ("procedure"):
			System.out.println("ldc "+ast.right.left.value.toUpperCase());
			int stat =SymbolTable.curr_fun.f_depth - SymbolTable.getVar(ast.right.left.value.toUpperCase(), SymbolTable.curr_fun.name).f_depth;
			System.out.println("lda "+stat+" 1");
			break;
		default:
			System.out.println("unknown codeArg: "+v.type);
    	break;
		}
		return null;
	}
	public static String codel(AST ast, SymbolTable symbolTable) {
		// TODO Auto-generated method stub
		if (debug) System.out.println("codel ");
		if(ast==null)
			return null;
        if (debug) System.out.println(ast.value);
        switch(ast.value){
	        case("identifier"):
	        	if(SymbolTable.getStatic(ast.left.value,SymbolTable.curr_fun.name)==-1)
	        		System.out.println("lda "+SymbolTable.getStatic(ast.left.value.toUpperCase(),SymbolTable.curr_fun.name)+" " + SymbolTable.getVar(ast.left.value.toUpperCase(),SymbolTable.curr_fun.name).Addr);
	        	else
	        		System.out.println("lda "+SymbolTable.getStatic(ast.left.value,SymbolTable.curr_fun.name)+" " + SymbolTable.getVar(ast.left.value,SymbolTable.curr_fun.name).Addr);
	        	return ast.left.value;
	        	
	        case("pointer"):
	        	String name = codel(ast.left,symbolTable);
	        	String pname= SymbolTable.getVar(name,SymbolTable.curr_fun.name).pName;
	        	System.out.println("ind");
	        	return pname;
	        
	        case ("array"):
	        		String n =codel(ast.left,symbolTable);
	        		Variable v=SymbolTable.getVar(n,SymbolTable.curr_fun.name);
	        		printArrInfo(v.a_info,ast.right,symbolTable);
					System.out.println("dec "+v.a_info.subpart);
					return v.a_info.type;
//		    	for(Variable var :  SymbolTable.ST){
//		    		String s1=var.name;
//		    		String s2=ast.left.left.value;
////					if(var.name==ast.left.left.value){
//		    		if(s1.equals(s2)){
//		    			System.out.println("ldc " + var.Addr);
//						printArrInfo(var.a_info,ast.right,symbolTable);
//						System.out.println("dec "+var.a_info.subpart);
//						break;
//					}
//				}
	        case ("record"):
	        		int adr1 = SymbolTable.getVar(codel(ast.left,symbolTable),SymbolTable.curr_fun.name).Addr;
	        		int adr2 = SymbolTable.getVar(ast.right.left.value,SymbolTable.curr_fun.name).Addr;
	        		adr2-=adr1;
	        		System.out.println("inc "+ adr2);
	        		return ast.right.left.value;
	        default:
	        	System.out.println("unknown codel: "+ast.value);
			break;
        }
        return null;
	}

	public static int printArrInfo(Array_info info, AST ast ,SymbolTable symbolTable) {
		// TODO Auto-generated method stub
		if(ast==null)
			return 0;
		int i = printArrInfo(info, ast.left,symbolTable);
		coder(ast.right,symbolTable);
		System.out.println("ixa "+info.ixa[i]);
		return i+1;
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
        case ("array"):
        	codel(ast,symbolTable);
        	System.out.println("ind");
    	break;
        case ("record"):
        	codel(ast,symbolTable);
    		System.out.println("ind");
    		break;
        case("call"):
        	code(ast,symbolTable);
        	break;
        default:
        	System.out.println("unknown coder: "+ast.value);
    	break;
		}
	}

//	public static void main(String[] args) {
	public static void main(String[] args) throws FileNotFoundException {

//    	Scanner scanner = new Scanner(new File("input\\tree15.txt"));
    	Scanner scanner = new Scanner(System.in);
        AST ast = AST.createAST(scanner);
        SymbolTable symbolTable = SymbolTable.generateSymbolTable(ast);
//        symbolTable.printST();
        generatePCode(ast, symbolTable);
    

    }

}