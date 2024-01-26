package cetus.transforms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;
import cetus.hir.*;

//This pass creates a separate file in which the experimental section is created 
//insert all libraries
//read input variables from the file
//start the timer
//experimental section
//stop the timer,and save the timer
//save output variables to a file
//compare the time and output variables
public class ChangeExperimentalCode extends TransformPass {

	
	 /** Line separator */
    private static final String NEWLINE = System.getProperty("line.separator");


    /** Pass name */
    private static final String pass_name = "[ChangeExperimentalSection]";

    /** Heading string for result printing */
    private static final String header = "CETUS_TIMING";
    
    /** The translation unit to be detected which contains the program entry */
    private TranslationUnit main_tunit;

    /** The main function to be detected */
    private Procedure main_proc;
    
    /**
     * The contents of code to be prepended to each translation unit that does
     * not contain the program entry (main function). This code contains the
     * declaration of the library calls.
     */
    private static final String[] headercode = {
            "#ifdef " + header,
//            "void timer_clear( int n );",
//            "void timer_start( int n );",
//            "void timer_stop( int n );",
//            "double timer_read( int n );",
            "#endif /* " + header + " */",
            ""
        };
        
    
    /**
     * The contents of code to be prepended to the translation unit that
     * contains the program entry. This code contains the definition of the
     * library calls.
     */
    private static final String[] libcode = {
        "#include <omp.h>",
        "#include <stdio.h>",
        "#include <stdlib.h>",
        "#include <unistd.h>",
        "#include <string.h>",
        "#include <time.h>",
        "#include <sys/time.h>",
        "",
        "struct timeval start, end;",
        "double cpu_time_used;",
        "",
        "void read_var_from_file(void* var, size_t sizeOfType, size_t numElements, FILE* fp);"
    };
    
	protected ChangeExperimentalCode(Program program) {
		super(program);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getPassName() {
		// TODO Auto-generated method stub
		return "[ChangeExperimentalSection]";
	}

	@Override
	public void start() {

        
        for (Traversable t : program.getChildren()) {
            transformTUnit((TranslationUnit)t);
        }
        if (main_tunit == null) {
            System.err.println("[WARNING] Program entry is missing");
        } else {
            // insert includes only if it is necessary
            String libcodes = genCode(libcode);
            //if no openmp code is included then remove the library
            if (SymbolTools.findSymbol(main_tunit, "omp") == null) {
                libcodes = libcodes.replace("#include <omp.h>", "");
            }
            if (SymbolTools.findSymbol(main_tunit, "printf") != null) {
                libcodes = libcodes.replace("#include <stdio.h>", "");
            }
            if (SymbolTools.findSymbol(main_tunit, "malloc") != null) {
                libcodes = libcodes.replace("#include <stdlib.h>", "");
            }
            Declaration libcode_decl =
                    new AnnotationDeclaration(new CodeAnnotation(libcodes));
            
            Declaration first_proc = getFirstProcedure(main_tunit);
            if (first_proc == null) { // should not happen in general
                main_tunit.addDeclaration(libcode_decl);
            } else {
                main_tunit.addDeclarationBefore(first_proc, libcode_decl);
            }
            /////////////////////////////////////////////
            // creates/inserts initialization code right after declaration part
            List<String> codes = new LinkedList<String>();
            codes.add("#ifdef " + header);
            codes.add("FILE *fp;");
            codes.add("fp = fopen(\"/test.txt\", \"w+\");");
            codes.add("fprintf(fp, \"This is testing for fprintf...\\n\");");
//            codes.add("char " + prof_name + "_names[][32] = {");
//            for (String name : event_names) {
//                codes.add("    \"" + name + "\", ");
//            }
//            codes.add("    \"PROGRAM\"");
//            codes.add("};");
//            codes.add(prof_name + ".num_events = " + (num_events + 1) + ";");
//            codes.add("cetus_init_timers(&" + prof_name + ", " + prof_name +
//                    "_names);");
//            codes.add("cetus_tic(&" + prof_name + ", " + num_events + ");");
            codes.add("#endif /* " + header + " */");
//            Statement first_stmt =
//                    getFirstStatementAfterDeclaration(main_proc.getBody());
//            AnnotationStatement note_stmt =
//                    new AnnotationStatement(new CodeAnnotation(genCode(codes)));
//            if (first_stmt == null) {
//                main_proc.getBody().addStatement(note_stmt);
//            } else {
//                main_proc.getBody().addStatementBefore(first_stmt, note_stmt);
//            }
//            codes.clear();
//            // creates/inserts exiting code
//            codes.add("#ifdef " + header);
//            codes.add("cetus_toc(&" + prof_name + ", " + num_events + ");");
//            codes.add("cetus_print_timers(&" + prof_name + ", stderr);");
//            codes.add("#endif");
            // program exit
//            for (Statement exit : exit_stmts) {
//                exit.annotateBefore(new CodeAnnotation(genCode(codes)));
//            }
            // return statement
//            DFIterator<ReturnStatement> iter = new DFIterator<ReturnStatement>(
//                    main_proc.getBody(), ReturnStatement.class);
//            while (iter.hasNext()) {
//                iter.next().annotateBefore(new CodeAnnotation(genCode(codes)));
//            }
            // last non-return statement
//            List<Traversable> children = main_proc.getBody().getChildren();
//            Statement last_stmt = (Statement)children.get(children.size() - 1);
//            if (!(last_stmt instanceof ReturnStatement)) {
//                last_stmt.annotateAfter(new CodeAnnotation(genCode(codes)));
//            }
        }	

	


	}

	private void transformTUnit(TranslationUnit tunit) {
	       DFIterator<Traversable> iter = new DFIterator<Traversable>(tunit);
	        iter.pruneOn(VariableDeclaration.class);
	        while (iter.hasNext()) {
	            Traversable t = iter.next();
	            if (t instanceof Procedure
	                    && ((Procedure)t).getSymbolName().equals("main")) {
	                main_proc = (Procedure) t;
	                main_tunit = tunit;
	            } else if (t instanceof Statement) {
	                Statement stmt = (Statement)t;
//	                Returns the first occurrence of the annotation with the specified type
//	                and the string key.
	                PragmaAnnotation.Experimental event =
	                        stmt.getAnnotation(PragmaAnnotation.Experimental.class,"operation");
	                if (event != null) {
	                    String fcall = "";
	                    //String name = event.getName();
	                    String command = event.getOperation();
	                   // String writeOutputVarsToFile="";
	                    if (command.equals("start")) {
	                    	//processInput variables and write their values to a files, the Input variable can be array or basic data structure.
	                    	//saveInputValuesinFile(event.getInput());
	                    	//saveInputValuesinFile(event.getInput())+
	                        fcall =  NEWLINE+"gettimeofday(&start, NULL);" + NEWLINE ;
	                        //writeOutputVarsToFile= saveOutputValuesinFile(event.getOutput());
	                       // event_names.add(name);
	                    } else if (command.equals("stop")) {
	                       // int event_num = event_names.indexOf(name);
	                    	//saveOutputValuesinFile(event.getOutput())+ NEWLINE;
	                        fcall = "gettimeofday(&end, NULL); "+ NEWLINE +"cpu_time_used = (double) ((end.tv_sec * 1000000 + end.tv_usec) - (start.tv_sec * 1000000 + start.tv_usec)) / 1000000;"+NEWLINE + "printf(\"\\n\\n[ExperimentalSectionTime] Execution time(seconds) = %f \\n\", cpu_time_used);" +  NEWLINE+ "FILE* initialExperimentalSectionRunTime= fopen(\"initialExperimentalSectionRunTime\",\"w\");"+NEWLINE+"fprintf(initialExperimentalSectionRunTime,\"cpu_time_used %f\",cpu_time_used); "+NEWLINE+"fclose(initialExperimentalSectionRunTime);"+ NEWLINE;
	                        //process output variables, save their values to  a file
	                    } else {
	                        throw new InternalError(pass_name +
	                                " Unknown event pragma");
	                    }
	                    stmt.annotate(new CodeAnnotation(
	                            //"#ifdef " + header + NEWLINE
	                             fcall + NEWLINE));
	                            //+ "#endif /*" + header + " */"));
	                }
	            } 
//	            else if (t instanceof FunctionCall
//	                    && ((FunctionCall)t).getName().toString().equals("exit")) {
//	                exit_stmts.add(((Expression)t).getStatement());
//	            }
	        }
	        if (tunit != main_tunit) {
	            tunit.addDeclarationFirst(new AnnotationDeclaration(
	                    new CodeAnnotation(genCode(headercode))));
	        }
		
	}	
	
	/** Returns the procedure that lexically appears first in the user code */
    private Declaration getFirstProcedure(TranslationUnit tu) {
        Declaration ret = null;
        List<Traversable> children = tu.getChildren();
        for (int i = children.size() - 1; i >= 0; i--) {
            Declaration decl = (Declaration)children.get(i);
            if (decl instanceof AnnotationDeclaration &&
                decl.toString().contains("endinclude")) {
                break;
            }
            if (decl instanceof Procedure) {
                ret = decl;
            }
        }
        return ret;
    }
    
    /** Generates code with the given list of lines */
    private String genCode(List<String> code) {
        String ret = "";
        for (String line : code) {
            ret += line + NEWLINE;
        }
        return ret;
    }
    
    /** Generates code with the given list of lines */
    private String genCode(String[] code) {
        String ret = "";
        for (String line : code) {
            ret += line + NEWLINE;
        }
        return ret;
    }

}
