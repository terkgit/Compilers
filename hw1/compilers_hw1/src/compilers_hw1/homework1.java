package compilers_hw1;
import java.util.Scanner;

/*
* Hints in doing the HW:
*   a) Make sure you first understand what you are doing.
*   b) Watch Lecture 2 focusing on the code described
 */
class homework1 {

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
        // Think! what does a Variable contain?
    }

    static final class SymbolTable{
        // Think! what does a SymbolTable contain?

        public static SymbolTable generateSymbolTable(AST tree){
            // TODO: create SymbolTable from AST
            return null;
        }
    }

    private static void generatePCode(AST ast, SymbolTable symbolTable) {
        // TODO: go over AST and print code
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AST ast = AST.createAST(scanner);
        SymbolTable symbolTable = SymbolTable.generateSymbolTable(ast);
        generatePCode(ast, symbolTable);
    }

}