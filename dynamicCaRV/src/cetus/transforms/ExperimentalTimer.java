package cetus.transforms;

import java.util.*;
//import cetus.analysis.*;

import java.lang.*;
import java.text.Format;

import cetus.hir.*;
import omp2gpu.analysis.LiveAnalysis0;
import cetus.analysis.*;
import cetus.analysis.ArrayPrivatization.*;

import omp2gpu.analysis.AnalysisTools;
import omp2gpu.analysis.LiveAnalysis0;
import omp2gpu.analysis.OCFGraph;
import omp2gpu.analysis.ReachAnalysis0;

import cetus.analysis.CFGraph;
import cetus.analysis.CallGraph;
import cetus.analysis.DFANode;
import cetus.hir.PragmaAnnotation;
//import cetus.application.ChainTools;

public class ExperimentalTimer extends TransformPass {

	/** Line separator */
	private static final String NEWLINE = System.getProperty("line.separator");


	/** Pass name */
	private static final String pass_name = "[ExperimentalSectionTimer]";

	private static int debug_level = 0;//PrintTools.getVerbosity();

	/** Heading string for result printing */
	private static final String header = "Initial";
	private static final String headerexp = "Experimental";
	/** The translation unit to be detected which contains the program entry */
	private TranslationUnit main_tunit;

	/** The main function to be detected */
	private Procedure main_proc;
	//to detect the procedure containing the experimental section
	private Procedure exp_proc;
	// detecting the translation unit containing the experimental section
	private TranslationUnit exp_tunit;
	//If the main procedure contains the exerimental section it will be set to true. otherwise it will be set to false.
	private boolean main_has_exp= false;

	/** The list of forced program exit points to be collected */
	private List<Statement> exit_stmts;
	//this structure holds the pointers declared in the procedure and their exact size
	private HashMap<String,String> arraySize= new HashMap<>(); // r rXIndex*m2k*m3k
	String exp_declaration_statements; //getting the declaration stetements of the experimental section whne it is not part of main procedure
	//set of procedures of the program
	 //Set<Procedure> procSet = new HashSet<>();
	//Set<Procedure> procList = ChainTools.getProcedureSet(program);
	String procedureParametersStr; //declaring teh parameters passed through function call in teh beginning of teh main procedure of te EXP section
	String expSubroutine; //the procedure that has teh exp section inside it
	private static String expname= PragmaAnnotation.expname;

	/**
	 * The contents of code to be prepended to each translation unit that does
	 * not contain the program entry (main function). This code contains the
	 * declaration of the library calls.
	 */
	private static final String[] headercode = {
			"void write_var_to_file(char *varName,void* var, size_t sizeOfType, size_t numElements, FILE* fp);",
			"void read_var_from_file(char *varName,void* var, size_t sizeOfType, size_t numElements, FILE* fp);",
	};


	/**
	 * The contents of code to be prepended to the translation unit that
	 * contains the program entry. This code contains the definition of the
	 * library calls.
	 */
	private static final String[] libcode = {
			"#include <stdio.h>",
			"#include <stdlib.h>",
			"#include <omp.h>",
			"#include <time.h>",
			"#include <sys/time.h>",
			"#include <string.h>",
			"#include <unistd.h>",
			"#include <math.h>",
			//"#define NRM  \"\\x1B[0m\"",
			//"#define RED  \"\\x1B[31m\"",
			//"#define GRN  \"\\x1B[32m\"",
			//"#define MAG  \"\\x1B[35m\"",
			//"#define CYN  \"\\x1B[36m\"",
			//"#define RESET \"\\x1B[0m\"",
			//      "//The first step is to uncomment this line, then compile and run the file.",
			"//When Initial directive is defined the original code runs in order to save the variables and timers' values.",
			"#define Initial",
			"//Once the Experimental directive is defined, the Experimental section runs and the results before and after modification to this code section gets compared. ",
			"//#define Experimental",
			"#ifdef Experimental",
			"#undef Initial",
			"#endif //Experimental",
			//This concept is implemented for ONLY comparing float and double variables. I use it mostly for comparing the execution time. In my case millisecond is important.
			"#define closeEnough(a, b) (fabs((a - b)) < 0.001)? 1 : 0", 
			"//Due to the fact that normalized loop iteration numbers begin at 0, the default option is set to 0.",
			"//By default, the input and output of the experimental section are saved to the file on the first execution. ",
			"//When the experimental section is iterated over, this option allows the user to save the input and output of the iteration of the choice to a file.",
			"#define iterationNum 0",
			"",
			"//Set the verbosity based on how detailed you want the results:",
			"//The initial value of verbosity is set based on how it is set in the Cetus run",
			"//#define verbosity 0 shows the results of the comparison of the variables,",
			"//#define verbosity 1 shows the execution time of the experimental section, the result of the comparison, and the value of the variables,",
			"//#define verbosity 2 shows the reading/writing state of variables.",
			"#define verbosity "+ debug_level+"",
			" ",
			"void read_var_from_file(char *varName,void* var, size_t sizeOfType, size_t numElements, FILE* fp){",
			"if(fread(var, sizeOfType, numElements, fp) != numElements) {",
			"	if(feof(fp))",
			"		printf(\"Premature end of file.\\n\");",
			"	else",
			"		printf(\"File read error of var %s. \\n\",varName);",
			"}else if(verbosity > 1) {",
			"	printf(\"The value of variable %s is read from the file.\\n\",varName); ",
			"     }",
			"}",
			"",
			"void write_var_to_file(char *varName, void* var, size_t sizeOfType, size_t numElements, FILE* fp){",
		   //"//size_t fwrite(const void * ptr, size_t size, size_t count, FILE * stream);",
           // "//Where ptr is the pointer to the data to be written, size is the size of each element in bytes, count is the number of elements to be written, and stream is a pointer to the file.",
			"if(fwrite(var, sizeOfType, numElements, fp) != numElements) {",
			"	if(feof(fp))",
			"		printf(\"Premature end of file.\\n\");",
			"	else",
			"		printf(\"File write error of var %s.\\n\",varName);", 
			"}else if(verbosity > 1) {",
			"	printf(\"The value of variable %s is written to the file.\\n\",varName); ",
			"     }",
			"}",
			"",
			"#ifdef Experimental",
			//"#define DATATYPE int",
			"void compare_two_int_variables_in_binary_files(FILE *initialOutputStateFile,FILE *modifiedOutputStateFile, char *varName, size_t sizeOfType, size_t numElements,char *varType, int filePointer){",
			"size_t n1, n2; //to check how many elements are read",
			"int offset = 0; //reports where the difference is",
			//"int tmp1[numElements],tmp2[numElements];",
			"int *tmp1 = malloc(sizeof(int) * numElements);",
			"int *tmp2 = malloc(sizeof(int) * numElements);",
			//  "  printf(\"\\nInside compare_two_variables_in_binary_files function .\");",
			"initialOutputStateFile=fopen(initialOutputStateFile,\"rb\");",
			"modifiedOutputStateFile=fopen(modifiedOutputStateFile,\"rb\");",
			"if(initialOutputStateFile == NULL){",
			"	printf(\"Error: Failed to open initialOutputStateFile.\\n\");",
			"	return;",
			"}",
			"if(modifiedOutputStateFile == NULL){",
			"printf(\"Error: Failed to open modifiedOutputStateFile.\\n\");",
			"return;",
			"}",
			"//sets the file pointer at the right position",
			"if(fseek(initialOutputStateFile, filePointer, SEEK_SET) != 0){",
			"	printf(\"Error: Failed to set the file pointer for initialOutputStateFile.\\n\");",
			"	return;",
			"}",
			"if(fseek(modifiedOutputStateFile, filePointer, SEEK_SET) != 0){",
			"	printf(\"Error: Failed to set the file pointer for modifiedOutputStateFile.\\n\");",
			"	return;",
			"}",
			"n1 = fread(tmp1, sizeOfType, numElements, initialOutputStateFile);",
			"if (n1 <numElements && ferror(initialOutputStateFile)) {",
			"   printf(\"Error: Failed to read from initialOutputStateFile. \\n \" );",
			"}else if(verbosity > 1) {printf(\"initialOutputStateFile is read successfully.\\n\"); }",
			"n2 = fread(tmp2, sizeOfType, numElements, modifiedOutputStateFile);",
			"if (n2 <numElements && ferror(modifiedOutputStateFile)) {",
			"   printf(\"Error: Failed to read from modifiedOutputStateFile.\\n\");",
			"}else if(verbosity > 1) {printf(\"modifiedOutputStateFile is read successfully.\\n\"); }",
			"// Check for possible buffer overflows when copying data from tmp1 and tmp2.",
//			"if(numElements > sizeof(tmp1) || numElements > sizeof(tmp2)){",
//			"	printf(\"%sError: buffer overflow.%s\\n\", RED, RESET);",
//			"	return;",
//			"}",
			"size_t n_min = n1 < n2 ? n1 : n2;",
			"int ret= memcmp(tmp1, tmp2, sizeOfType * numElements);",
			"if(ret != 0) {",
			"   for (size_t i = 0; i < n_min; i++) {",
			"     if (tmp1[i] != tmp2[i]) {",
			"      offset = i;",
			"	  if (n_min==1){",
			"         printf(\"MISMATCH: The value of variable %s before and after modification differs.\\n\",  varName );}",
			" 	  else{",
			"          printf(\"MISMATCH: The value of variable %s before and after modification differs starting from element %d.\\n\",varName,offset);}",// tmp1[i], tmp2[i]);}",
			//    	"          printf(\"%sMISMATCH: The value of variable %s differs at element %d of the array.%s\\n\",MAG,varName, offset,RESET);}",// tmp1[i], tmp2[i]);}",
			"		   break;",
			"	   }}",
			//    	"	   if(ret>0) ",
			//    	"          printf(\"\\nThe modified value of the variable %s is less than the initial value of the variable.\" ,varName);",
			//    	"	   else ",
			//    	"          printf(\"\\nThe initial value of the variable %s is less than the modified value.\",varName);",
			"}else if(ret==0){",
			"   printf(\"MATCH: The value of variable %s before and after modification is equal.\\n\",varName);",
			"}",
			"free(tmp1);",
			"free(tmp2);",
			"return;",
			"}",
			"",
			"void compare_two_double_variables_in_binary_files(FILE *initialOutputStateFile,FILE *modifiedOutputStateFile, char *varName, size_t sizeOfType, size_t numElements,char *varType, int filePointer){",
			"size_t n1, n2; //to check how many elements are read",
			"int offset = 0; //reports where the difference is",
			//"double tmp1[numElements],tmp2[numElements]; //size 8",
			"double *tmp1 = malloc(sizeof(double) * numElements); //size 8",
			"double *tmp2 = malloc(sizeof(double) * numElements); //size 8",
			"initialOutputStateFile=fopen(initialOutputStateFile,\"rb\");",
			"modifiedOutputStateFile=fopen(modifiedOutputStateFile,\"rb\");",
			"if(initialOutputStateFile == NULL){",
			"	printf(\"Error: Failed to open initialOutputStateFile.\\n\");",
			"	return;",
			"}",
			"if(modifiedOutputStateFile == NULL){",
			"	printf(\"Error: Failed to open modifiedOutputStateFile.\\n\");",
			"	return;",
			"}",
			"if(fseek(initialOutputStateFile, filePointer, SEEK_SET) != 0){",
			"	printf(\"Error: Failed to set the file pointer for initialOutputStateFile.\\n\");",
			"	return;",
			"}",
			"if(fseek(modifiedOutputStateFile, filePointer, SEEK_SET) != 0){",
			"	printf(\"Error: Failed to set the file pointer for modifiedOutputStateFile.\\n\");",
			"	return;",
			"}",
			"n1 = fread(tmp1, sizeOfType, numElements, initialOutputStateFile);",
			"if (n1 <numElements && ferror(initialOutputStateFile)) {",
			"	printf(\"Error: Failed to read from initialOutputStateFile. \\n \");",
			"}else if(verbosity > 1) {printf(\"initialOutputStateFile is read successfully.\\n\"); }",
			"n2 = fread(tmp2, sizeOfType, numElements, modifiedOutputStateFile);",
			"if (n2 <numElements && ferror(modifiedOutputStateFile)) {",
			"	printf(\"Error: Failed to read from modifiedOutputStateFile.\\n\");",
			"}else if(verbosity > 1) {printf(\"modifiedOutputStateFile is read successfully.\\n\"); }",
//			"// Check for possible buffer overflows when copying data from tmp1 and tmp2.",
//			"if(numElements > sizeof(tmp1) || numElements > sizeof(tmp2)){",
//			"	printf(\"%sError: buffer overflow.%s\\n\", RED, RESET);",
//			"	return;",
//			"}",
			"size_t n_min = n1 < n2 ? n1 : n2;",
			"int ret= memcmp(tmp1, tmp2, sizeOfType * numElements);",
			"if(ret != 0) {",
			"	for (size_t i = 0; i < n_min; i++) {",
			"		if (tmp1[i] != tmp2[i] && !closeEnough(tmp1[i],tmp2[i]) && !(isnan(tmp1[i]) && isnan(tmp2[i])) ) {",
			"			offset = i;",
			"			if (n_min==1){",
			"				printf(\"MISMATCH: The value of variable %s before and after modification differs.\\n\",  varName );}",
		//	"			// returns 1 if the given argument is -nan, and 0 if it is not.",
			"			else if((isnan(tmp1[i]) && isnan(tmp2[i]))){",
			"			continue;",
			"			}",
			"			else if(!closeEnough(tmp1[i],tmp2[i])){",
			"				printf(\"MISMATCH: The value of variable %s before and after modification differs starting from element %d.\\n\",varName,offset);",
			"				break;}",
			"		}else if ((tmp1[i] == tmp2[i] && i==n_min-1) || (closeEnough(tmp1[i],tmp2[i]) && i==n_min-1)){",
			"			printf(\"MATCH: The value of variable %s before and after modification is equal.\\n\",varName);",
			"			break;",
			"		}",
			"	}",
			"}else if(ret==0){",
			"	printf(\"MATCH: The value of variable %s before and after modification is equal.\\n\",varName);",
			"}",
			"free(tmp1);",
			"free(tmp2);",
			"return;",
			"}",
			"",
			"void compare_two_float_variables_in_binary_files(FILE *initialOutputStateFile,FILE *modifiedOutputStateFile, char *varName, size_t sizeOfType, size_t numElements,char *varType, int filePointer){",
			"size_t n1, n2; //to check how many elements are read",
			"int offset = 0; //reports where the difference is",
			//"float tmp1[numElements],tmp2[numElements]; //size 4",
			"float *tmp1 = malloc(sizeof(float) * numElements); //size 4",
			"float *tmp2 = malloc(sizeof(float) * numElements); //size 4",
			"initialOutputStateFile=fopen(initialOutputStateFile,\"rb\");",
			"modifiedOutputStateFile=fopen(modifiedOutputStateFile,\"rb\");",
			"if(initialOutputStateFile == NULL){",
			"	printf(\"Error: Failed to open initialOutputStateFile.\\n\");",
			"	return;",
			"}",
			"if(modifiedOutputStateFile == NULL){",
			"	printf(\"Error: Failed to open modifiedOutputStateFile.\\n\");",
			"	return;",
			"}",
			"if(fseek(initialOutputStateFile, filePointer, SEEK_SET) != 0){",
			"	printf(\"Error: Failed to set the file pointer for initialOutputStateFile.\\n\");",
			"	return;",
			"}",
			"if(fseek(modifiedOutputStateFile, filePointer, SEEK_SET) != 0){",
			"	printf(\"Error: Failed to set the file pointer for modifiedOutputStateFile.\\n\");",
			"	return;",
			"}",
			"n1 = fread(tmp1, sizeOfType, numElements, initialOutputStateFile);",
			"if (n1 <numElements && ferror(initialOutputStateFile)) {",
			"	printf(\"Error: Failed to read from initialOutputStateFile. \\n \" );",
			"}else if(verbosity > 1) {printf(\"initialOutputStateFile is read successfully.\\n\"); }",
			"n2 = fread(tmp2, sizeOfType, numElements, modifiedOutputStateFile);",
			"if (n2 <numElements && ferror(modifiedOutputStateFile)) {",
			"	printf(\"Error: Failed to read from modifiedOutputStateFile.\\n\");",
			"}else if(verbosity > 1) {printf(\"modifiedOutputStateFile is read successfully.\\n\"); }",
//			"// Check for possible buffer overflows when copying data from tmp1 and tmp2.",
//			"if(numElements > sizeof(tmp1) || numElements > sizeof(tmp2)){",
//			"	printf(\"%sError: buffer overflow.%s\\n\", RED, RESET);",
//			"	return;",
//			"}",
			"size_t n_min = n1 < n2 ? n1 : n2;",
			"int ret= memcmp(tmp1, tmp2, sizeOfType * numElements);",
			"if(ret != 0) {",
			"	for (size_t i = 0; i < n_min; i++) {",
			"		if (tmp1[i] != tmp2[i] && !closeEnough(tmp1[i],tmp2[i]) && !(isnan(tmp1[i]) && isnan(tmp2[i])) ) {",
			"			offset = i;",
			"			if (n_min==1){",
			"				printf(\"MISMATCH: The value of variable %s before and after modification differs.\\n\",  varName );}",
		//	"			// returns 1 if the given argument is -nan, and 0 if it is not.",
			"			else if((isnan(tmp1[i]) && isnan(tmp2[i]))){",
			"			continue;",
			"			}",
			"			else if(!closeEnough(tmp1[i],tmp2[i])){",
			"				printf(\"MISMATCH: The value of variable %s before and after modification differs starting from element %d.\\n\",varName,offset);",
			"				break;}",
			"		}else if ((tmp1[i] == tmp2[i] && i==n_min-1) || (closeEnough(tmp1[i],tmp2[i]) && i==n_min-1)){",
			"			printf(\"MATCH: The value of variable %s before and after modification is equal.\\n\",varName);",
			"           break;",
			"		}",
			"	}",
			"}else if(ret==0){",
			"	printf(\"MATCH: The value of variable %s before and after modification is equal.\\n\",varName);",
			"}",
			"free(tmp1);",
			"free(tmp2);",
			"return;",
			"}",
			
			"#endif // Experimental",  
			" ",   
			"struct timeval startexp, endexp;",
			"double exp_time_used;",
			"double saved_exp_time_used;",
			"struct timeval prgstart, prgend;",
			"double prg_time_used;",
			"struct timeval captureStartInputWrite, captureEndInputWrite;",
			"double capture_input_write_time;",
			"struct timeval captureStartOutputWrite, captureEndOutputWrite;",
			"double capture_output_write_time;",
			"struct timeval replayStartInputRead, replayEndInputRead;",
			"double replay_input_read_time;",
			"struct timeval replayStartOutputWriteCompare, replayEndOutputWriteCompare;",
			"double replay_output_write_time;",
			"int iteration_count =0; //Counts the number of iterations the experimental section goes through. ",
			"int _ret_val_0;",
			" ",
//			"FILE* initialInputStateFile;",
//			"FILE* initialOutputStateFile;",
//			"FILE* modifiedOutputStateFile;",
			"FILE* initialInputStateFile"+expname+";",
			"FILE* initialOutputStateFile"+expname+";",
			"FILE* modifiedOutputStateFile"+expname+";"
	};

	/**
	 * The contents of code to be prepended to the translation unit that
	 * contains the last procedure. This code ends the initial phase run.
	 */
	private static final String[] endInitial = {"#endif //Initial"};
	
	protected ExperimentalTimer(Program program) {
		super(program);
		debug_level = PrintTools.getVerbosity();
		exit_stmts = new LinkedList<Statement>();
	}

	@Override
	public String getPassName() {
		// TODO Auto-generated method stub
		return "[ExperimentalSectionTimer]";
	}


	/**
	 * Performs instrumentation after analyzing the program to collect
	 * information required to generate the c instrumentation.
	 */
	@Override
	public void start(){
		//add timers around the section
		for (Traversable t : program.getChildren()) {
			transformTUnit((TranslationUnit)t);
		}
		if (main_tunit == null) {
			System.err.println("[WARNING] Program entry is missing");
		} else {
			// inserts libraries required to compile the file
			String libcodes = genCode(libcode);
			//if no openmp code is included then remove the library
//			if (SymbolTools.findSymbol(main_tunit, "omp") == null) {
//				libcodes = libcodes.replace("#include <omp.h>", "");
//			}
			if (SymbolTools.findSymbol(main_tunit, "printf") != null) {
				libcodes = libcodes.replace("#include <stdio.h>", "");
			}
			if (SymbolTools.findSymbol(main_tunit, "malloc") != null) {
				libcodes = libcodes.replace("#include <stdlib.h>", "");
			}
			Declaration libcode_decl =
					new AnnotationDeclaration(new CodeAnnotation(libcodes));
			//find the first procedure to set #ifdef Initial and gettimeofday(&prgstart, NULL) timer in it after declaration part
			Declaration first_proc = getFirstProcedure(main_tunit);
			//find the last procedure to insert #endif //Initial at the end of it  
			Declaration last_proc = getLastProcedure(main_tunit);
			if (first_proc == null) { // should not happen in general
				main_tunit.addDeclaration(libcode_decl);
			} else {//adding all libraries, read,write,comparison functions before the first procedure 
				main_tunit.addDeclarationBefore(first_proc, libcode_decl);
				//main_tunit.addDeclarationBefore(main_proc, libcode_decl);
			}
			
			//generate the endInitial //endInitial should be added to end of main function. all the other procedure are part of both initial and experimental
			String endInitials = genCode(endInitial);
			//convert it to declaration as it should be added to the code
			Declaration endInitials_decl =
					new AnnotationDeclaration(new CodeAnnotation(endInitials));
			if (last_proc == null) { // should not happen in general
				main_tunit.addDeclaration(endInitials_decl);
			} else {//endIf Inital will be added to end of main function
			//main_tunit.addDeclarationAfter(last_proc, endInitials_decl);
				main_tunit.addDeclarationAfter(main_proc, endInitials_decl);
			}
			/////////////////////////////////////////////
			// creates/inserts initialization code right after declaration part
			List<String> codes = new LinkedList<String>();
			codes.add(" ");
//			codes.add("omp_set_num_threads(4);");
			codes.add("gettimeofday(&prgstart, NULL); ");
//			codes.add("FILE* initialInputStateFile; ");
//			codes.add("FILE* initialOutputStateFile; ");
//			codes.add("FILE* modifiedOutputStateFile; ");
			codes.add(" ");
			// codes.add("omp_set_num_threads(4);");
			// codes.add(" ");
			codes.add("#ifdef "+ header);

			List<String> codesexp = new LinkedList<String>();
			codesexp.add(" ");
//			codesexp.add("omp_set_num_threads(4);");
			codesexp.add("gettimeofday(&prgstart, NULL); ");
			codesexp.add(" ");
//if experimental section is part of main function then we keep declaration part of the main function
			Statement first_stmt = null;
			if (main_has_exp ==true) {
				first_stmt=getFirstStatementAfterDeclaration(main_proc.getBody());
			}else if (main_has_exp == false) {
				first_stmt=getFirstStatementBeforeDeclaration(main_proc.getBody());
			}
			
			AnnotationStatement note_exp =
					new AnnotationStatement(new CodeAnnotation(genCode(codesexp)));		
			AnnotationStatement note_stmt =
					new AnnotationStatement(new CodeAnnotation(genCode(codes)));
			if (first_stmt == null) {
				main_proc.getBody().addStatement(note_stmt);
			//} else {
			} else if (main_has_exp ==true) {
				main_proc.getBody().addStatementBefore(first_stmt, note_stmt);
			} else if(main_has_exp == false) {  //make sure to start the timer in the beginning of main
				main_proc.getBody().addStatementBefore(first_stmt, note_exp);
				main_proc.annotateBefore(new CodeAnnotation("#ifdef Initial"));
			}
			codes.clear();
			// creates/inserts exiting code
			//            codes.add("#ifdef " + header);
			//            codes.add("cetus_toc(&" + prof_name + ", " + num_events + ");");
			//            codes.add("cetus_print_timers(&" + prof_name + ", stderr);");

			codes.add("gettimeofday(&prgend, NULL);");
			codes.add("prg_time_used = (double) ((prgend.tv_sec * 1000000 + prgend.tv_usec) - (prgstart.tv_sec * 1000000 + prgstart.tv_usec)) / 1000000;");
			codes.add("printf(\"\\nExecution time of the program in the capturing phase: %.6lf (seconds) \\n\", prg_time_used);");
			codes.add("printf(\"Overhead of the capturing phase: %.6lf (seconds) \\n\", capture_input_write_time+capture_output_write_time);");
			//codes.add("printf(\"Execution time of the experimental section before modification: %.6lf (seconds) \\n\", exp_time_used);");
			codes.add("printf(\"Execution time of the experimental section (before modification): %.6lf (seconds) \\n\", saved_exp_time_used);");
			//codes.add("FILE* initialExperimentalSectionRunTime= fopen(\"initialExperimentalSectionRunTime\",\"w\");");
			//codes.add("write_var_to_file(\"exp_time_used\", &exp_time_used, sizeof(double), 1, initialExperimentalSectionRunTime);");
			//codes.add("if(verbosity > 0 && iteration_count>1) {printf(\"iteration_count= %d\\n\",iteration_count);}");
			codes.add("if(iteration_count>1){printf(\"The experimental section is iterated over %d times.\\n\",iteration_count);}");
			//"	if(verbosity > 0) {printf(\"The original experimental code section took %.6lf seconds to run.\\n\", exp_time_used);}"+ NEWLINE +
			//codes.add("printf(\"\\n\\n%sUncomment the line: #define Experimental, make your changes to the experimental section then compile and run the code.%s\\n\",MAG ,RESET );");
			//codes.add("#endif //Initial");// it should not end in here. should end at the end of the program
			// program exit
			for (Statement exit : exit_stmts) {
				exit.annotateBefore(new CodeAnnotation(genCode(codes)));
			}
			// return statement
			DFIterator<ReturnStatement> iter = new DFIterator<ReturnStatement>(
					main_proc.getBody(), ReturnStatement.class);
			while (iter.hasNext()) {
				iter.next().annotateBefore(new CodeAnnotation(genCode(codes)));
			}
			// last non-return statement
			List<Traversable> children = main_proc.getBody().getChildren();
			Statement last_stmt = (Statement)children.get(children.size() - 1);
			if (!(last_stmt instanceof ReturnStatement)) {
				last_stmt.annotateAfter(new CodeAnnotation(genCode(codes)));
			}
		}	

	}


	/**
	 * Inserts timing function calls and saving variables to files at the location specified by experimental section
	 * annotation.
	 * @param tunit the translation unit to be transformed.
	 * @param FILE 
	 */
	public void transformTUnit(TranslationUnit tunit) {
		DFIterator<Traversable> iter = new DFIterator<Traversable>(tunit);
		iter.pruneOn(VariableDeclaration.class);
		int counterPrg=0;
		while (iter.hasNext()) {
			Traversable t = iter.next();
			//it shows how many iterations of the program is traversed
			counterPrg++;
			//System.out.println("\nTraversable\t "+t);
			if (t instanceof Procedure
					&& ((Procedure)t).getSymbolName().equals("main")) {
				main_proc = (Procedure) t;
				if (((Procedure) t).toString().contains("experimental section")  )
						//contains("experimental section")) 
					{
					main_has_exp= true;
					exp_proc= (Procedure) t;
					}
				main_tunit = tunit;
			}else if(t instanceof Procedure && ((Procedure) t).toString().contains("experimental section") ) {
				exp_proc= (Procedure) t;
				expSubroutine=exp_proc.toString(); //added to the end of the experimental section if the experimental section is not inside main
				procedureParametersStr= getProcedureParameters(exp_proc);  //added this to where declaration is done in #ifdef Experimental 	int main()
				
				//System.out.println(((Procedure) t).getDeclaredIDs()); //procedure name
				//get declaration statements of the procedure containing the experimental section
				 DFIterator<Traversable> expiter;
				 expiter = new DFIterator<Traversable>(exp_proc);
				 StringBuilder stb=new StringBuilder();
				while (expiter.hasNext()) {
					Traversable tr = expiter.next();
					//it shows how many iterations of the program is traversed
					//counterPrg++;
					if (tr instanceof DeclarationStatement){
						if (!((DeclarationStatement) tr).toString().contains("(*")) { //pointer declarations are deleted.
						stb.append(tr);
						stb.append( NEWLINE);
						}
					}
				}
				exp_declaration_statements= stb.toString(); //initializing teh global variable
				
				//List<String> codes = new LinkedList<String>();
				//codes.add("#ifdef Initial ");
				//AnnotationStatement annote_stmt =
						//new AnnotationStatement(new CodeAnnotation(genCode(codes)));
				exp_proc.annotateBefore(new CodeAnnotation("#ifdef Initial ")); //to match the annotation used inside the experimental section
				exp_proc.annotateAfter(new CodeAnnotation("#endif //Initial "));
				//Statement first_stmt=getFirstStatementBeforeDeclaration(exp_proc.getBody());
				//exp_proc.addStatementBefore(first_stmt, annote_stmt);
				//codes.clear();
				//codes.add("#endif //Initial");
//				Statement last_stmt=getLastStatement(exp_proc.getBody());
//				exp_proc.addStatementBefore(last_stmt, annote_stmt);
				exp_tunit=tunit;
				saveprocedureparameters((Procedure) t);//saves pointers decalred in the progarm to get the size of them at run-time
						
		  //  }else if(t instanceof Procedure) {
		   // 	procSet.add((Procedure) t);
		    //	System.out.println("procedure name "+((Procedure) t).getName());
		    	
		    //	Set<Procedure> procList = ChainTools.getProcedureSet(program);
			}else if (t instanceof Statement) {
		    	Statement stmt = (Statement)t;
				//System.out.println("\nStatement\t "+stmt);
				//                Returns the first occurrence of the annotation with the specified type
				//                and the string key.
				PragmaAnnotation.Experimental event =
						stmt.getAnnotation(PragmaAnnotation.Experimental.class,"operation");
				if (event != null) {
			//		expname=event.getName();
					String fcall = "";
					String fcallpart= "";
					String flastcallpart="";
					if (main_has_exp ==false) {
						fcallpart="int main()"+NEWLINE +
						"{"+NEWLINE +
						//parameters passed through the function call
						procedureParametersStr+NEWLINE +
						//variable declaration of the procedure
						exp_declaration_statements+
			//			"omp_set_num_threads(4);"+NEWLINE +
						"gettimeofday(&prgstart, NULL);"+NEWLINE ;
						flastcallpart=expSubroutine+NEWLINE ;
					}else if(main_has_exp ==true) {
						fcallpart="gettimeofday(&prgstart, NULL);"+NEWLINE ;
						flastcallpart="}"+NEWLINE;
					}
					
					//String name = event.getName();
					String command = event.getOperation();
					String expname= event.getName();
					String  userInput= event.getInput();
					// String writeOutputVarsToFile="";
					if (command.equals("start") && userInput!=null ) {
								
						//processInput variables and write their values to a files, the Input variable can be array or basic data structure.
						//saveInputValuesinFile(event.getInput());
						fcall = //"if (iteration_count==iterationNum) {"+NEWLINE +
								saveInputValuesinFile(event.getInput())+ 
								//"}else{if(verbosity > 0){printf(\"The input set of iteration %d is not written to the file.\\n\",iteration_count);}}"+NEWLINE +
								"#endif //Initial"+NEWLINE +
								""+NEWLINE +
								"#ifdef " + headerexp+NEWLINE +
								fcallpart+
								// "FILE* initialInputStateFile;"+NEWLINE +
								//"int main()"+NEWLINE +
								//"{"+NEWLINE +
								//"omp_set_num_threads(4);"+NEWLINE +
								//"gettimeofday(&prgstart, NULL);"+NEWLINE +
								"double initial_exp_time_used;"+NEWLINE +
								"if(access(\"initialInputStateFile"+expname+"\", F_OK) == 0){"+NEWLINE +
								"	if(verbosity > 1) {printf(\"The file to load the initial state of variables exists.\\n\");}"+NEWLINE +
								"	initialInputStateFile = fopen(\"initialInputStateFile"+expname+"\", \"rb\");"+NEWLINE +
								"}else{"+NEWLINE +
								"	printf(\"The file to load variables' initial states doesn't exist. Comment out #define Experimental and run the original Cetus output file.\\n\");"+NEWLINE +
								"	exit(0);"+NEWLINE +
								"}"+NEWLINE +
								//read input variables from file,create a function that generates the whole string
								"gettimeofday(&replayStartInputRead, NULL);"+NEWLINE +
								readInputValuesfromFile(event.getInput())+
								//                                "read_var_from_file(\"arr\", arr, sizeof(int), 1000000, varFile);"+
								//                                "read_var_from_file(\"sum\",&sum, sizeof(int), 1, varFile);"+
								//                                "read_var_from_file(\"averageVal\",&averageVal, sizeof(double), 1, varFile);"+
								"gettimeofday(&replayEndInputRead, NULL);"+NEWLINE +
								"replay_input_read_time = (double) ((replayEndInputRead.tv_sec * 1000000 + replayEndInputRead.tv_usec) - (replayStartInputRead.tv_sec * 1000000 + replayStartInputRead.tv_usec)) / 1000000;"+NEWLINE +
								"if(verbosity > 0){"+NEWLINE +
								"printf(\"The input set of the experimental section is read.\\n\");"+NEWLINE +
								"printf(\"The replay_input_read_time took %.6lf seconds.\\n\", replay_input_read_time);"+NEWLINE +
								"}"+NEWLINE +
								"#endif //Experimental"+NEWLINE+
								""+NEWLINE+
								"gettimeofday(&startexp, NULL);" + NEWLINE+
								"//******************START of Experimental Section******************";
						//writeOutputVarsToFile= saveOutputValuesinFile(event.getOutput());
						// event_names.add(name);
					} else if (command.equals("start") && userInput==null ) {
						
						//if (command.equals("start") && userInput.equals(null )) {
						//analyze the statements for usesets and defsets till the stop event happens
						//iterate over the next object
						Set useSet = new TreeSet<Expression>();
						Set defSet= new TreeSet<Expression>();
						Set removeFromdefSet= new TreeSet<Expression>(); //loop iterators should be removed from defset
						//variables that are not live at the end of the experimental section should be removed from the outset.so where the event experimental section happens, check for the live variables. and then compare outset and live variables. if variable is not listed in live vars, remove it from outset.
						Set localVarSet= new TreeSet<Expression>();  
						Set removeFromuseSet= new TreeSet<Expression>();//variables that are declared inside the experimental section are added to it
						Set lives=new TreeSet<Expression>(); //variables that are live after the experimental section. these are the variables that should be displayed for validation
						//variables that are passed by return are added to lives set
						Set privateOMP=new TreeSet<Expression>(); //union it with removeFromdefSet Set
						Set lastprivateOMP=new TreeSet<Expression>(); //union it with lives Set
						//print privates that are removed from outset
						//print lastprivates added to the live set.
						//live set should be intersected with defset
						//what happens to global variables???
						//	Set updateddefSet= new TreeSet<Expression>();
						//this counter counts how many iterations inside the experimental section we have had
						//another counter is need to show where we rae in the program. we call it counterPrg
						int counterExperimentalSection= 0;
						//we are at the start of the experimental section
						do {
							t = iter.next();
							counterExperimentalSection++;
							//System.out.println("\n[Ex] Traversable\t "+t);
							//if it is not the end event then
							//                    	if (t instance of Statement) {
							try {
								stmt = (Statement)t;
								if(!(stmt instanceof AnnotationStatement) && stmt.toString().contains("experimental section stop")) {
									System.out.println("Error: The experimental section is not correctly positioned. It should enclose a structured block.");
									//return;
									System.exit(1);
								}
								//System.out.println("\n[Ex] Statement\t "+stmt);
								//event =null
								event = stmt.getAnnotation(PragmaAnnotation.Experimental.class,"operation");
								//                            Returns the first occurrence of the annotation with the specified type
								//                            and the string key.
							} 
							catch (ClassCastException e) {
								// Handle the error
							}    
							
							//variables declared inside the expeimental section should be removed from Cetus Inset or UseSet
							if(stmt instanceof DeclarationStatement || t instanceof DeclarationStatement) {
								String[] arr;
								String varDeclared;
								//System.out.println("\n[Ex] Statement\t "+stmt);
								//System.out.println("\n[Ex] t\t "+t);
								//System.out.println("\n[Ex] Statement\t "+stmt.toString());
								if(stmt.toString().contains("=")) {
								arr = stmt.toString().substring(0, stmt.toString().indexOf("=")).split(" ");
								varDeclared = arr[arr.length-1];
								//System.out.println(varDeclared); 
								removeFromuseSet.add(varDeclared);
//								if(stmt.toString().substring(0, stmt.toString().indexOf("=")).contains("*")) {
//									removeFromuseSet.add(varDeclared+"[");
//								}
								
								}else {
									arr = stmt.toString().substring(0, stmt.toString().indexOf(";")).split(" ");
									varDeclared = arr[arr.length-1];
									//System.out.println(varDeclared); 
									removeFromuseSet.add(varDeclared);
									if(stmt.toString().substring(0, stmt.toString().indexOf(";")).contains("*")) {
										removeFromuseSet.add(varDeclared+"[");
									}
								}
								//get name of teh variable that is declared
								//stmt.
							}
							
							//loop iterators should be collected and then removed from Cetus defSet
							if(stmt instanceof ForLoop) {
								//get the iterator and save it to a set
//							System.out.println(((ForLoop) stmt).getInitialStatement().toString());
//							System.out.println(((ForLoop) stmt).getCondition().toString());
//							System.out.println(((ForLoop) stmt).getStep().toString());
							if(stmt.getAnnotations()!=null) {
							String directive=stmt.getAnnotations().toString();
							if (directive.contains("private(")) {
								int privateStartIndex = directive.indexOf("private(");
								if (privateStartIndex != -1) {
								    // Set the starting index of the substring to the first character after "private("
								    privateStartIndex += 8;

								    // Find the ending index of the "private" clause
								    int privateEndIndex = directive.indexOf(")", privateStartIndex);

								    // Extract the substring between "private(" and ")" and split it by commas
								    String privateClause = directive.substring(privateStartIndex, privateEndIndex);
								   // String[] privateArray = privateClause.split(", ");
								
								//String privateClause = directive.substring(directive.indexOf("private(") + 8, directive.indexOf(")"));
								// Split the substring by commas and add the resulting variables to the set
					            String[] privateArray = privateClause.split(", ");
					            for (String var : privateArray) {
					            	privateOMP.add(var);
					            }}
					            //System.out.println("Private variables: " + privateOMP);
							}
							if (directive.contains("lastprivate(")) {
								int lastprivateStartIndex = directive.indexOf("lastprivate(");
								if (lastprivateStartIndex != -1) {
								    // Set the starting index of the substring to the first character after "private("
								    lastprivateStartIndex += 12;

								    // Find the ending index of the "private" clause
								    int lastprivateEndIndex = directive.indexOf(")", lastprivateStartIndex);

								    // Extract the substring between "private(" and ")" and split it by commas
								    String lastprivateClause = directive.substring(lastprivateStartIndex, lastprivateEndIndex);
								   // String[] privateArray = privateClause.split(", ");
								
								//String privateClause = directive.substring(directive.indexOf("private(") + 8, directive.indexOf(")"));
								// Split the substring by commas and add the resulting variables to the set
					            String[] lastprivateArray = lastprivateClause.split(", ");
					            for (String var : lastprivateArray) {
					            	lastprivateOMP.add(var);
					            }}
					            //System.out.println("LastPrivate variables: " + lastprivateOMP);
							}}
							
//							
//							Set privateOMP=new TreeSet<Expression>(); //union it with removeFromdefSet Set
//							Set lastprivateOMP=new TreeSet<Expression>(); //union it with lives Set
							//System.out.println("removeFromdefSet: \n" + removeFromdefSet);
							//System.out.println("privateOMP: \n" + privateOMP);
							removeFromdefSet.addAll(privateOMP);
							//System.out.println(" union removeFromdefSet: \n" + removeFromdefSet);
							//System.out.println("lives: \n" + lives);
							//System.out.println("lastprivateOMP: \n" + lastprivateOMP);
							lives.addAll(lastprivateOMP);
							//System.out.println("union lives: \n" + lives);
								
							String step=((ForLoop) stmt).getStep().toString();
							String[] parts = step.split("[\\s+-]+");
//					        for(String part : parts){
//					            System.out.println(part);
//					        }
							String id = parts[0];
							//System.out.println(id);
							//Expression expr = ExpressionParser.parse(id);
							removeFromdefSet.add(id);
							}
							//iter.pruneOn(AccessExpression.class);
							//get use set   (Traversable)o)

							useSet.addAll(DataFlowTools.getUseSet(t));
							// Printing elements of teh set
							//System.out.println("[Ex] useSet:"+ useSet);
							//get def set                            
							defSet.addAll(DataFlowTools.getDefSet(t));
							//System.out.println("[Ex] defSet:"+defSet);
							
												
//local variables							
							//perform liveness analysis on all nodes of the procedure
							//get the list of live variables at this point.
							//Set<Symbol> out_set = new LinkedHashSet<Symbol>();
							//System.out.println("\n\n"+stmt);
							//System.out.println("\n\n stmt"+SymbolTools.getLocalSymbols(stmt)); 
							//System.out.println("\n\n local vars of exp-proc"+SymbolTools.getLocalSymbols(exp_proc)); 
							//System.out.println("\n\n"+t);
							//System.out.println("\n\n t"+SymbolTools.getLocalSymbols(t)); 
							//System.out.println("\n\n t"+SymbolTools.getLocalSymbols(t)); 
							if (SymbolTools.getLocalSymbols(t) != null){
								Iterator<Symbol> iteratet = (Iterator<Symbol>) SymbolTools.getLocalSymbols(t).iterator();
								while (iteratet.hasNext()) {
								    Object element1 = iteratet.next();
								    //System.out.println(element1.toString());
								   // System.out.println(element1);
								    if(element1.toString().contains("=")) {
										String[] arr = element1.toString().substring(0, element1.toString().indexOf("=")).split(" ");
										String varDeclared = arr[arr.length-1];
										//System.out.println(varDeclared); 
										localVarSet.add(varDeclared);
										}else {
											//String[] arr1 = element1.toString().substring(0, element1.toString().indexOf(";")).split(" ");
											//String varDeclared1 = arr1[arr1.length-1];
											//System.out.println(element1.toString()); 
											localVarSet.add(element1.toString());
										}
								   // System.out.println( element1.getSymbolName());
								   // localVars.add(element1.getSymbolName());
							}
							}
							//localVars.addAll(SymbolTools.getLocalSymbols(t));
							//System.out.println("\n\n[Ex] localVarSet:"+ localVarSet);
							
//							ArrayPrivatization ap= new ArrayPrivatization(program);
//				            Set<Symbol> gen = new LinkedHashSet<Symbol>();
//				            Set<Symbol> kill = new LinkedHashSet<Symbol>();
//				            Set<Symbol> mask_set= new LinkedHashSet<Symbol>();
//				            Set<Symbol> private_vars;
//				            System.out.println("exp_proc.getSymbolName()="+ exp_proc.getSymbolName()+"\n");
//				            CFGraph cfg = new CFGraph(exp_proc);
//				            cfg.topologicalSort(cfg.getNodeWith("stmt", "ENTRY"));
//							
////							LiveAnalysis0 live_analysis = new LiveAnalysis0(exp_proc, cfg, private_vars, false);
////							live_analysis.run();
//				            Map<Procedure, CFGraph> cfgMap = new HashMap<Procedure, CFGraph>();
//				            DFAGraph ret = cfg.buildProcedure((Procedure) exp_proc);
				           //CFGraph cfg = cfgMap.get(exp_proc.getSymbolName());
				           
//				            Iterator cfgIter = cfg.iterator();
//				            //Iterator cfgIter = cetus.application.DataFlowAnalysis.cfgMap.get(main_proc).iterator();
//				            while (cfgIter.hasNext()) {
//				            DFANode cfgNode = (DFANode) cfgIter.next();
//				           // Object currentIR = CFGraph.getIR(cfgNode);
//				            ap.computeLiveVariables(cfgNode, gen, kill, mask_set);
//							Object tr = CFGraph.getIR( cfgNode);
//							if (!(tr instanceof Traversable)) { // symbol-entry with initializer?
//					            //break;
//								cfgIter.next();
//					        }
//							if(tr instanceof Traversable) {
//					        gen.addAll(DataFlowTools.getUseSymbol((Traversable)tr));
//					        kill.addAll(DataFlowTools.getDefSymbol((Traversable)tr));
//							}
					        //addAccessName(gen);
					       // addAccessName(kill);
//					        Set<Symbol> pri_set = new LinkedHashSet<Symbol>();
//							CFGraph live_cfg = ap.getLiveVariables(exp_proc, pri_set);
							//}
							//till we have not reached the stop operation of the experimental section
						}while(event==null );// && !event.getOperation().equals("stop") );
						
						//let's process the rest of what is left from the procedure that has the experimental section in it.
						//the goal is to get all the live variables or variables that are accessed(read) later in this procedure right after the experimental section ends.
						//intersect of these variables with outset variables is all that should be validated and compared.
						do {
							
							//if (iter.hasNext()) {
								t = iter.next();
							//	System.out.println("\n[Ex] Traversable\t " + t);
							//}else {
							//	break; //out of the while loop
							//}
						//Traversable t1= iter.next();
	                   // System.out.println("\n[Ex] Traversable\t "+t);
						//if it is not the end event then
						//  
	                    
						
						//if it is an instance of function call,then collect all the parameters passed to it
						 if(t instanceof FunctionCall) {
			                	List<Expression> funcarguments = ((FunctionCall)t).getArguments();
			                	//iterating through the arguments
			                    for (Expression exp : funcarguments) {
			                        if (exp instanceof IntegerLiteral) {
			                    		continue;
			                        }else if(exp instanceof Identifier) {
			                    		lives.add(exp.toString());
			                    	}
			                    }
							 //System.out.println("lives variables: " + lives);
						 }
	                    
	                    if (!(t instanceof Statement)) {
	                        continue;
	                    }
	                    stmt = (Statement)t;
	                    
	                  //add the variable in front of return to live outset
						if (t instanceof ReturnStatement || stmt instanceof ReturnStatement ) {
													
							String ret=stmt.toString();
							if (ret.contains("return ")) {
								int retStartIndex = ret.indexOf("return ");
								if (retStartIndex != -1) {
								    // Set the starting index of the substring to the first character after "private("
								    retStartIndex += 6;

								    // Find the ending index of the "private" clause
								    int retEndIndex = ret.indexOf(";", retStartIndex);

								    // Extract the substring between "private(" and ")" and split it by commas
								    String returned = ret.substring(retStartIndex, retEndIndex).trim();
								   // String[] privateArray = privateClause.split(", ");
							if (!returned.equals("") ) {
					            	lives.add(returned);
							}
					            }}
					           // System.out.println("lives variables: " + lives);
							
						}
						

	                    // Skip declarations and annotations
	                    if (!(stmt instanceof ExpressionStatement)) {
	                        continue;
	                    }
						Expression e = ((ExpressionStatement)stmt).getExpression();//if (t instance of Statement) {
				
						if (e instanceof FunctionCall) {
							List<Expression> funcarguments = ((FunctionCall)e).getArguments();
		                	//iterating through the arguments
		                    for (Expression exp : funcarguments) {
		                        if (exp instanceof IntegerLiteral) {
		                    		continue;
		                    	}else if(exp instanceof Identifier) {
		                    		lives.add(exp.toString());
		                    	}else if(exp instanceof StringLiteral ||exp instanceof IntegerLiteral ) {
		                    		continue;
		                    	}else if(exp instanceof BinaryExpression) { //k-1
		                    		 //Expression lhs=((BinaryExpression) exp).getLHS();
		                    		// Expression rhs=((BinaryExpression) exp).getRHS();
		                    		 List<Traversable> children= exp.getChildren();
		                    		 for (Traversable child : children) {
		                    			 if(child instanceof Identifier) {
		                    				 lives.add(child.toString());
		                    			 }
		                    		 }
		                    	} 
		                    }
						 //System.out.println("lives variables: " + lives);
						}
						if (e instanceof Identifier) {
			                continue;
			            // Comma identifiers
			            } else if (e instanceof CommaExpression) {
			            	continue;
			            	//assignment Expression, accessed variables should be collected
			            }else if (e instanceof AssignmentExpression) {
			                Expression to = ((AssignmentExpression)e).getRHS();
			               //in case there is  a function call on the RHs of the assignment expression, 
			                //its parameters should be collected if they are identifiers
			                if(to instanceof FunctionCall) {
			                	List<Expression> arguments = ((FunctionCall)to).getArguments();
			                	//iterating through the arguments
			                    for (Expression exp : arguments) {
			                        if (exp instanceof IntegerLiteral) {
			                    		continue;
			                    	}//function call parameters passed
			                    	if (exp instanceof BinaryExpression) {
			                    		 Expression lhs=((BinaryExpression) exp).getLHS();
			                    		 Expression rhs=((BinaryExpression) exp).getRHS();
			                    	// rhs and lhs can be BinaryExpressions themselves
			                    	if(((BinaryExpression) exp).getLHS() instanceof Identifier) {
			                    			lives.add(((BinaryExpression) exp).getLHS().toString());			               
			                    	}else if(lhs instanceof BinaryExpression) { //Binary Expression has LHS and RHS part to be analyzed.
			                    		Expression lhsL = ((BinaryExpression) lhs).getLHS();
			                    		 if (lhsL instanceof Identifier) {
			                    		      lives.add(lhsL.toString());
			                    		    }
			                    		 if (((BinaryExpression) lhs).getRHS() instanceof Identifier) {
			                    		      lives.add(((BinaryExpression) lhs).getRHS().toString());
			                    		    }			                    
			                    	}
			                    		
			                    		
			                    	if (((BinaryExpression) exp).getRHS() instanceof Identifier) {
			                    		lives.add(((BinaryExpression) exp).getRHS().toString());
			                    	}else if(rhs instanceof BinaryExpression) {
			                    		Expression rhsR = ((BinaryExpression) rhs).getRHS();
			                    		 if (rhsR instanceof Identifier) {
			                    		      lives.add(rhsR.toString());
			                    		    }
			                    		 if (((BinaryExpression) rhs).getLHS() instanceof Identifier) {
			                    		      lives.add(((BinaryExpression) rhs).getLHS().toString());
			                    		    }
			                    	}
			                    	
			                    	//System.out.println(exp.toString());
			                    }
			                    	}
			                   continue;
			                }//end of function call instance
			                //if there is no function call on the RHS of the assignment expression then 
			                //simply go over all the variables that are accessed on the RHS of the assignment expression
			                    for (Symbol variable : SymbolTools.getAccessedSymbols(to)) {
			                       // if (stmts.containsSymbol(var)) {
			                            lives.add(variable.getSymbolName().toString());
			                       // }
			                    }
						}  //end of assignment expression instance

						
							//when at the end of procedure then live analysis is done
						}while(!(t instanceof Procedure ) && iter.hasNext());
						
						//all global variables inside the defset should be added to the lives Set
		               // Symbol base = ((Identifier)arg).getSymbol();
		               //if (SymbolTools.isGlobal(base) 
						//System.out.println("\n\n[Ex] lives Set:"+ lives);
						//System.out.println("\n\n[Ex] def Set:"+ defSet);		
		                Iterator<Expression> itr = defSet.iterator();
		                Symbol base = null;
		                while (itr.hasNext()) {
		                    Expression expression = itr.next();
		                  base= SymbolTools.getSymbolOf(expression);
//		                    if(expression instanceof ArrayAccess) { base = ((ArrayAccess)expression) ;}
//		                    else if(expression instanceof Identifier) { base = ((Identifier)expression).getSymbol();}
		                    
		                    if (SymbolTools.isGlobal(base)) {
		                    	//System.out.println("add it to lives "+ base);
		                    	//System.out.println("add it to lives "+ base.getSymbolName());
		                    	lives.add(base.getSymbolName());
		                    	} 
		                    
		                    // Do further analysis on the expression
		                }
						
						//System.out.println("\n\n[Ex] lives Set:"+ lives);
						//System.out.println("\n\n[Ex] def Set:"+ defSet);
						System.out.println("\nLive-out set before applying Enhanced Liveness Analysis "+ defSet);
						
						

						//useSet and defSet is created. now create the In and Out statement for the pragma.
						//print all the elements in useSet and extract the Type and size from declaration.
						//example: In=int:n,float:a:10000,float:b:10000,float:t:100
						SymbolTable symtab = (SymbolTable) t.getParent();
						List specs = null;
						//this is the inset string that should be created 
						String cetusInputSet=null;
						String cetusOutputSet=null;
						//these are input and output sets passed to different functions.
						//so the changes for replacing pointers should be made inside this function.
						//[d1, d2, d3, i1, i2, i3, j1, j2, j3, m1j, m2j, m3j, r, r[i3+1][i2+1][i1+1], r[i3+1][i2+1][i1+2], r[i3+1][i2+1][i1], r[i3+1][i2+2][i1+1], r[i3+1][i2+2][i1], r[i3+1][i2][i1+1], r[i3+1][i2][i1], r[i3+2][i2+1][i1+1], r[i3+2][i2+1][i1], r[i3+2][i2+2][i1+1], r[i3+2][i2+2][i1], r[i3+2][i2][i1+1], r[i3+2][i2][i1], r[i3][i2+1][i1+1], r[i3][i2+1][i1], r[i3][i2+2][i1+1], r[i3][i2+2][i1], r[i3][i2][i1+1], r[i3][i2][i1], s, s[j3][j2][j1], x1, x1[i1+2], x1[i1], x2, y1, y1[i1+2], y1[i1], y2]
						//remove variable declared inside the experimental section from useSet
						Iterator<String> iter1 = useSet.iterator();
						Iterator<String> iter2 = removeFromuseSet.iterator();
						while (iter1.hasNext()) {
						    Object element1 = iter1.next();
						   // System.out.println(element1.toString());
						    String str = element1.toString();
						    String subStr=null;
						    if(str.contains("[")) {
						        String[] arr = str.split("\\[");
						        subStr = arr[0];
						       // System.out.println(subStr); 
						    }else {subStr= str;}
						    while (iter2.hasNext()) {
						        Object element2 = iter2.next();
						        
						        if (element1.toString().equals(element2.toString()) || subStr.equals(element2.toString())) {
						            useSet.remove(element1);
						            //resetting iterator
						            iter1=useSet.iterator();
						            break;
						        }
						    }
						    //resetting iterator
						    iter2=removeFromuseSet.iterator();
						}
						
						
						cetusInputSet = "In="+generateCetusIOSet(useSet, symtab);
//						Iterator<Expression> defSetIterator = defSet.iterator();
//						while(defSetIterator.hasNext()) {
//							Expression expression = defSetIterator.next();
//							//if(!removeFromdefSet.contains(expression)) {
//								System.out.println(expression);
//								updateddefSet.add(expression);
//							//}
//						}
						//defSet.remove(removeFromdefSet);
						//remove loop iterators from defsets
						Iterator<String> iterator1 = defSet.iterator();
						Iterator<String> iterator2 = removeFromdefSet.iterator();
						while (iterator1.hasNext()) {
						    Object element1 = iterator1.next();
						    
						    while (iterator2.hasNext()) {
						        Object element2 = iterator2.next();
						        
						        if (element1.toString().equals(element2.toString())) {
						            defSet.remove(element1);
						            iterator1=defSet.iterator();
						            break;
						        }
						    }
						    iterator2=removeFromdefSet.iterator();
						}
						
						//remove local variables of teh experimnetal section from defSet
						Iterator<String> iterator11 = defSet.iterator();
						Iterator<String> iterator21 = localVarSet.iterator();
						while (iterator11.hasNext()) {
						    Object element11 = iterator11.next();
						    
						    while (iterator21.hasNext()) {
						        Object element21 = iterator21.next();
						        
						        if (element11.toString().equals(element21.toString()) || element11.toString().contains(element21.toString()+"[")) {
						            defSet.remove(element11);
						            iterator11=defSet.iterator();
						            break;
						        }
						    }
						    iterator21=localVarSet.iterator();
						}
						
						
						
					    //just keep the live variables in the defSet
						Set intersection = new TreeSet<Expression>();
						//System.out.println("\ndefSet"+ defSet);
						//System.out.println("\nlive Set"+ lives);
						Iterator<String> iterator13 = defSet.iterator();
						Iterator<String> iterator23 = lives.iterator();
						while (iterator13.hasNext()) {
						    Object element13 = iterator13.next();
						    
						    while (iterator23.hasNext()) {
						        Object element23 = iterator23.next();
						        
						        if ((element13.toString().equals(element23.toString())) || element13.toString().contains(element23.toString()+"[")) {
						        	intersection.add(element13);
						            //iterator13=defSet.iterator();
						            break;
						        }
						    }
						    iterator23=lives.iterator();
						}
						//defSet.retainAll(lives);
//						System.out.println("\ndefSet"+ defSet);
//						System.out.println("\nlive Set"+ lives);
//						Set intersection = new TreeSet<Expression>();
//						for (Object exp : lives) {
//							System.out.println("\n"+exp.toString());
//						    if (defSet.toString().contains(exp.toString().trim()+",")|| defSet.toString().contains(exp.toString()+"[")) {
//						        intersection.add(exp.toString());
//						        System.out.println("\n added to the set"+exp.toString());
//						    }
//						}
						//System.out.println("\nintersection Set"+ intersection);
						//System.out.println("\nLive-out set before applying Enhanced Liveness Analysis "+ defSet);
						//System.out.println("\nOpenMP privateOMP Set"+ privateOMP);
						//System.out.println("\nOpenMP lastprivateOMP Set"+ lastprivateOMP);
						System.out.println("Live-out set after applying Enhanced Liveness Analysis "+ intersection+"\n" );
						
						defSet=intersection;
						//System.out.println("[Ex] updated defSet:"+defSet);
						//defSet.retainAll(removeFromdefSet);
//						List<Expression> expressions = new ArrayList<>(); 
//						for (String s : removeFromdefSet) { 
//						    Expression e = Expression.parse(s.toString()); 
//						    expressions.add(e); 
//						} 
//						defSet.removeAll(expressions);
						
//						String[] strings = (String[]) removeFromdefSet.toArray(new String[removeFromdefSet.size()]);
//						List<Expression> expressions = new ArrayList<>(); 
//						for (String s : strings) { 
//						    Expression e = (Expression)(s);
//						   // toString().compareTo(e.toString());
//						    expressions.add(e); 
//						} 
//						defSet.removeAll(expressions);
						cetusOutputSet = "Out="+generateCetusIOSet(defSet, symtab);
						//cetusOutputSet = "Out="+generateCetusIOSet(defSet, symtab);
						//start the program again to get to the start of the event then set fcall
                        //reset Iterable and go to the point that the event start and stops and enter what should be entered
						//RESTARTED
						iter.reset();
						for (int i = 0; i < (counterPrg+counterExperimentalSection) && iter.hasNext(); i++) {
							t =iter.next(); //Traversable t = iter.next();
							if (t instanceof Statement) {
								stmt = (Statement)t;
								//System.out.println("\nStatement\t "+stmt);
								//                Returns the first occurrence of the annotation with the specified type
								//                and the string key.
								event =stmt.getAnnotation(PragmaAnnotation.Experimental.class,"operation");
								if (event != null) {
									command = event.getOperation();
									// String writeOutputVarsToFile="";
									if (command.equals("start")){
										event.setInput(cetusInputSet);
										//start of the experimental event
										fcall = //"if (iteration_count==iterationNum) {"+NEWLINE +
												saveInputValuesinFile(cetusInputSet)+ 
												//"}else{if(verbosity > 0){printf(\"The input set of iteration %d is not written to the file.\\n\",iteration_count);}}"+NEWLINE +
												"#endif //Initial"+NEWLINE +
												""+NEWLINE +
												"#ifdef " + headerexp+NEWLINE +
												// "FILE* initialInputStateFile;"+NEWLINE +
												fcallpart+
												//"int main()"+NEWLINE +
												//"{"+NEWLINE +
												//"omp_set_num_threads(4);"+NEWLINE +
												//"gettimeofday(&prgstart, NULL);"+NEWLINE +
												"double initial_exp_time_used;"+NEWLINE +
												"if(access(\"initialInputStateFile"+expname+"\", F_OK) == 0){"+NEWLINE +
												"	if(verbosity > 1) {printf(\"The file to load the initial state of variables exists.\\n\");}"+NEWLINE +
												"	initialInputStateFile"+expname+" = fopen(\"initialInputStateFile"+expname+"\", \"rb\");"+NEWLINE +
												"}else{"+NEWLINE +
												"	printf(\"The file to load variables' initial states doesn't exist. Comment out #define Experimental and run the original Cetus output file.\\n\");"+NEWLINE +
												"	exit(0);"+NEWLINE +
												"}"+NEWLINE +
												//read input variables from file,create a function that generates the whole string
												"gettimeofday(&replayStartInputRead, NULL);"+NEWLINE +
												readInputValuesfromFile(cetusInputSet)+
												//                                "read_var_from_file(\"arr\", arr, sizeof(int), 1000000, varFile);"+
												//                                "read_var_from_file(\"sum\",&sum, sizeof(int), 1, varFile);"+
												//                                "read_var_from_file(\"averageVal\",&averageVal, sizeof(double), 1, varFile);"+
												"gettimeofday(&replayEndInputRead, NULL);"+NEWLINE +
												"replay_input_read_time = (double) ((replayEndInputRead.tv_sec * 1000000 + replayEndInputRead.tv_usec) - (replayStartInputRead.tv_sec * 1000000 + replayStartInputRead.tv_usec)) / 1000000;"+NEWLINE +
												"if(verbosity > 0){"+NEWLINE +
												"printf(\"The input set of the experimental section is read.\\n\");"+NEWLINE +
												"printf(\"The replay_input_read_time took %.6lf seconds.\\n\", replay_input_read_time);"+NEWLINE +
												"}"+NEWLINE +
												"#endif //Experimental"+NEWLINE+
												""+NEWLINE+
												"gettimeofday(&startexp, NULL);" + NEWLINE+
												"//******************START of Experimental Section******************";
										
										stmt.annotate(new CodeAnnotation(
												//"#ifdef " + header + NEWLINE
												fcall + NEWLINE));
									}else if (command.equals("stop")){//replace event.getOutput() with cetusOutputSet
										//define outset
										// set event.setOutput(cetusOutputSet);
										////print all the elements in defset and extract the Type and size from declaration.
//										for (Object element : defSet) { 
//											System.out.println("[Ex] defSet Elements:"+element); 
//										}
										//output is generated automatically)
										event.setOutput(cetusOutputSet);
										fcall = "//****************** END of Experimental Section ******************"+ NEWLINE +"gettimeofday(&endexp, NULL); "+ NEWLINE +
												"exp_time_used = (double) ((endexp.tv_sec * 1000000 + endexp.tv_usec) - (startexp.tv_sec * 1000000 + startexp.tv_usec)) / 1000000;"+ NEWLINE +
												"#ifdef Initial "+NEWLINE +
												"if (iteration_count==iterationNum) {"+NEWLINE +
												//"printf(\"\\nThe original Experimental code section has finished running.\\n\");"+ NEWLINE +
												//"printf(\"\\n\\n[ExperimentalSectionTime] Execution time(seconds) = %f \\n\", exp_time_used);" + NEWLINE+
												"saved_exp_time_used=exp_time_used;"+NEWLINE+
												"FILE* initialExperimentalSectionRunTime"+expname+"= fopen(\"initialExperimentalSectionRunTime"+expname+"\",\"w\");"+ NEWLINE+
												//"fprintf(initialExperimentalSectionRunTime,\"exp_time_used %f\",exp_time_used); "+ NEWLINE+
												"write_var_to_file(\"exp_time_used\", &exp_time_used, sizeof(double), 1, initialExperimentalSectionRunTime"+expname+");"+NEWLINE+
												"if(verbosity >= 0) {printf(\"The original experimental code section took %.6lf seconds to run.\\n\", exp_time_used);}"+ NEWLINE +
												//"fclose(initialExperimentalSectionRunTime);"+ NEWLINE+
												"gettimeofday(&captureStartOutputWrite, NULL);"+NEWLINE+
												saveOutputValuesinFile(cetusOutputSet,"initialOutputStateFile"+expname)+ 
												"gettimeofday(&captureEndOutputWrite, NULL);"+NEWLINE+
												"capture_output_write_time = (double) ((captureEndOutputWrite.tv_sec * 1000000 + captureEndOutputWrite.tv_usec) - (captureStartOutputWrite.tv_sec * 1000000 + captureStartOutputWrite.tv_usec)) / 1000000;"+NEWLINE+
												"if(verbosity > 0){"+NEWLINE+
												"printf(\"The output set of iteration %d is written to the file.\\n\",iteration_count);"+NEWLINE+
												"printf(\"The capture_output_write_time took %.6lf seconds.\\n\\n\", capture_output_write_time);"+NEWLINE+
												"}"+NEWLINE+
												"}else{if(verbosity > 1){printf(\"The output set of iteration %d is not written to the file.\\n\",iteration_count);}}"+ NEWLINE+
												"iteration_count++;"+ NEWLINE+
												"#endif //Initial"+ NEWLINE+
												"#ifdef Experimental"+ NEWLINE+
												"printf(\"\\nComparing the execution time of the experimental section before and after modification:\\n\");"+NEWLINE+
												//"printf(\"\\nThe modified experimental code section has finished running.\\n\\n\");"+ NEWLINE+
												"printf(\"The modified experimental section took %.6lf seconds to run.\\n\", exp_time_used);"+ NEWLINE+
												"FILE* initialExperimentalSectionRunTime"+expname+" = fopen(\"initialExperimentalSectionRunTime"+expname+"\", \"r\");"+ NEWLINE+
												"read_var_from_file(\"initial_exp_time_used\",&initial_exp_time_used, sizeof(double), 1, initialExperimentalSectionRunTime"+expname+");"+ NEWLINE+
												"printf(\"The original experimental section (before modifications) took %.6lf seconds to run.\\n\", initial_exp_time_used);"+ NEWLINE+
												"if(closeEnough(initial_exp_time_used, exp_time_used)){"+ NEWLINE+
												"printf(\"The execution time of the experimental section before and after modification remains the same.\\n\");"+ NEWLINE+
												"}else{"+ NEWLINE+
												"printf(\"There is a change of %.6lf seconds between the execution time of the experimental section before and after the modification\\n\", fabs((initial_exp_time_used) - (exp_time_used)));}"+ NEWLINE+
												"FILE* modifiedExperimentalSectionTimeFile"+expname+" = fopen(\"modifiedExperimentalSectionRunTime"+expname+"\", \"w\");"+ NEWLINE+
												"fwrite(&exp_time_used, sizeof(double), 1, modifiedExperimentalSectionTimeFile);"+ NEWLINE+
												"gettimeofday(&replayStartOutputWriteCompare, NULL);"+ NEWLINE+
												saveOutputValuesinFile(cetusOutputSet, "modifiedOutputStateFile"+expname )+ 
												""+ NEWLINE+
												//"if(verbosity > 0) {printf(\"\\nThe results/output of the modified experimental section have been saved in the output file: modifiedOutputStateFile.\\n\");}"+ NEWLINE+
												"if(verbosity > 0){"+NEWLINE+
												"printf(\"\\n\\nValues of output variables before modification:\\n\");"+NEWLINE+
												read_output_vars_from_file(cetusOutputSet,"initialOutputStateFile"+expname)+NEWLINE+
												"printf(\"\\n\\nValues of output variables after modification:\\n\");"+NEWLINE+
												"fseek(modifiedOutputStateFile"+expname+", 0, SEEK_SET);"+NEWLINE+
												read_output_vars_from_file(cetusOutputSet,"modifiedOutputStateFile"+expname)+NEWLINE+
												"} //end of if"+NEWLINE+
												/* Seek to the beginning of the file */
												// "fseek(initialOutputStateFile, 0, SEEK_SET);"+NEWLINE+
												"printf(\"\\n\\nComparing the output variables of the experimental section before and after modification:\\n\");"+NEWLINE+
												//this fseek needs to stay. If I add the fseek for initialOutputStateFile I get core dump.
												"fseek(modifiedOutputStateFile"+expname+", 0, SEEK_SET);"+NEWLINE+
												compare_output_vars(cetusOutputSet)+NEWLINE+
												"gettimeofday(&replayEndOutputWriteCompare, NULL);"+NEWLINE+
												"replay_output_write_time = (double) ((replayEndOutputWriteCompare.tv_sec * 1000000 + replayEndOutputWriteCompare.tv_usec) - (replayStartOutputWriteCompare.tv_sec * 1000000 + replayStartOutputWriteCompare.tv_usec)) / 1000000;"+NEWLINE+
												"if(verbosity > 0){"+NEWLINE+
												"printf(\"\\nThe output set of the experimental section is recorded and compared.\\n\");"+NEWLINE+
												"printf(\"The replay_output_write_time took %.6lf seconds.\\n\\n\", replay_output_write_time);"+NEWLINE+
												"}"+NEWLINE+
												"gettimeofday(&prgend, NULL);"+NEWLINE+
												"prg_time_used = (double) ((prgend.tv_sec * 1000000 + prgend.tv_usec) - (prgstart.tv_sec * 1000000 + prgstart.tv_usec)) / 1000000;"+NEWLINE+
												"printf(\"\\nExecution time of the experimental section in the Replaying Phase: %.6lf (seconds) \\n\", prg_time_used);"+NEWLINE+
												"printf(\"Overhead of the Replaying Phase is: %.6lf (seconds) \\n\", prg_time_used-exp_time_used);"+NEWLINE+
												"printf(\"Execution time of the (modified) experimental section: %.6lf (seconds) \\n\", exp_time_used);"+NEWLINE+
												"_ret_val_0=0;"+NEWLINE+
												"return _ret_val_0;"+NEWLINE+  //added
												"}"+NEWLINE+ //added
												"//****************** Added for the sake of compilation only******************"+NEWLINE+
												flastcallpart+
												"#endif //Experimental "+ NEWLINE+
												"#ifdef Initial "+NEWLINE;
										
									}
								}
						     }
						}
						
						//at the stop add the other fcall
						//processInput variables and write their values to a files, the Input variable can be array or basic data structure.
						//saveInputValuesinFile(event.getInput());
					//user provides the outset	
					}else if (command.equals("stop")) {
					
						// int event_num = event_names.indexOf(name);
						fcall = "//****************** END of Experimental Section ******************"+ NEWLINE +"gettimeofday(&endexp, NULL); "+ NEWLINE +
								"exp_time_used = (double) ((endexp.tv_sec * 1000000 + endexp.tv_usec) - (startexp.tv_sec * 1000000 + startexp.tv_usec)) / 1000000;"+ NEWLINE +
								"#ifdef Initial "+NEWLINE +
								//"printf(\"\\nThe original Experimental code section has finished running.\\n\");"+ NEWLINE +
								//"printf(\"\\n\\n[ExperimentalSectionTime] Execution time(seconds) = %f \\n\", exp_time_used);" + NEWLINE+
								"if (iteration_count==iterationNum) {"+ NEWLINE+
								"saved_exp_time_used=exp_time_used;"+NEWLINE+
								"FILE* initialExperimentalSectionRunTime"+expname+"= fopen(\"initialExperimentalSectionRunTime"+expname+"\",\"w\");"+ NEWLINE+
								//"fprintf(initialExperimentalSectionRunTime,\"exp_time_used %f\",exp_time_used); "+ NEWLINE+
								"write_var_to_file(\"exp_time_used\", &exp_time_used, sizeof(double), 1, initialExperimentalSectionRunTime"+expname+");"+NEWLINE+
								"if(verbosity >= 0) {printf(\"The original experimental code section took %.6lf seconds to run.\\n\", exp_time_used);}"+ NEWLINE +
								"gettimeofday(&captureStartOutputWrite, NULL);"+ NEWLINE +
								//"fclose(initialExperimentalSectionRunTime);"+ NEWLINE+
								saveOutputValuesinFile(event.getOutput(),"initialOutputStateFile"+expname)+ 
								"gettimeofday(&captureEndOutputWrite, NULL);"+ NEWLINE +
								"capture_output_write_time = (double) ((captureEndOutputWrite.tv_sec * 1000000 + captureEndOutputWrite.tv_usec) - (captureStartOutputWrite.tv_sec * 1000000 + captureStartOutputWrite.tv_usec)) / 1000000;"+ NEWLINE +
								"if(verbosity > 0){"+ NEWLINE +
								"printf(\"The output set of iteration %d is written to the file.\\n\",iteration_count);"+ NEWLINE +
								"printf(\"The capture_output_write_time took %.6lf seconds.\\n\\n\", capture_output_write_time);"+ NEWLINE +
								"}"+ NEWLINE +
								"}else{if(verbosity > 1){printf(\"The output set of iteration %d is not written to the file.\\n\",iteration_count);}}"+ NEWLINE+
								"iteration_count++;"+ NEWLINE+
								"#endif //Initial"+ NEWLINE+
								"#ifdef Experimental"+ NEWLINE+
								"printf(\"\\nComparing the execution time of the experimental section before and after modification:\\n\");"+NEWLINE+
								//"printf(\"\\nThe modified experimental code section has finished running.\\n\\n\");"+ NEWLINE+
								"printf(\"The modified experimental section took %.6lf seconds to run.\\n\", exp_time_used);"+ NEWLINE+
								"FILE* initialExperimentalSectionRunTime"+expname+" = fopen(\"initialExperimentalSectionRunTime"+expname+"\", \"r\");"+ NEWLINE+
								"read_var_from_file(\"initial_exp_time_used\",&initial_exp_time_used, sizeof(double), 1, initialExperimentalSectionRunTime"+expname+");"+ NEWLINE+
								"printf(\"The original experimental section (before modifications) took %.6lf seconds to run.\\n\", initial_exp_time_used);"+ NEWLINE+
								"if(closeEnough(initial_exp_time_used, exp_time_used)){"+ NEWLINE+
								"printf(\"The execution time of the experimental section before and after modification remains the same.\\n\");"+ NEWLINE+
								"}else{"+ NEWLINE+
								"printf(\"There is a change of %.6lf seconds between the execution time of the experimental section before and after the modification\\n\", fabs((initial_exp_time_used) - (exp_time_used)));}"+ NEWLINE+
								"FILE* modifiedExperimentalSectionTimeFile"+expname+" = fopen(\"modifiedExperimentalSectionRunTime"+expname+"\", \"w\");"+ NEWLINE+
								"fwrite(&exp_time_used, sizeof(double), 1, modifiedExperimentalSectionTimeFile);"+ NEWLINE+
								"gettimeofday(&replayStartOutputWriteCompare, NULL);"+ NEWLINE+
								saveOutputValuesinFile(event.getOutput(), "modifiedOutputStateFile"+expname )+ 
								""+ NEWLINE+
								//"if(verbosity > 0) {printf(\"\\nThe results/output of the modified experimental section have been saved in the output file: modifiedOutputStateFile.\\n\");}"+ NEWLINE+
								"if(verbosity > 0){"+NEWLINE+
								"printf(\"\\n\\nValues of output variables before modification:\\n\");"+NEWLINE+
								read_output_vars_from_file(event.getOutput(),"initialOutputStateFile"+expname)+NEWLINE+
								"printf(\"\\n\\nValues of output variables after modification:\\n\");"+NEWLINE+
								"fseek(modifiedOutputStateFile"+expname+", 0, SEEK_SET);"+NEWLINE+
								read_output_vars_from_file(event.getOutput(),"modifiedOutputStateFile"+expname)+NEWLINE+
								"} //end of if"+NEWLINE+
								/* Seek to the beginning of the file */
								// "fseek(initialOutputStateFile, 0, SEEK_SET);"+NEWLINE+
								"printf(\"\\n\\nComparing the output variables of the experimental section before and after modification:\\n\");"+NEWLINE+
								//this fseek needs to stay. If I add the fseek for initialOutputStateFile I get core dump.
								"fseek(modifiedOutputStateFile"+expname+", 0, SEEK_SET);"+NEWLINE+
								compare_output_vars(event.getOutput())+NEWLINE+
								"gettimeofday(&replayEndOutputWriteCompare, NULL);"+NEWLINE+
								"replay_output_write_time = (double) ((replayEndOutputWriteCompare.tv_sec * 1000000 + replayEndOutputWriteCompare.tv_usec) - (replayStartOutputWriteCompare.tv_sec * 1000000 + replayStartOutputWriteCompare.tv_usec)) / 1000000;"+NEWLINE+
								"if(verbosity > 0){"+NEWLINE+
								"printf(\"\\nThe output set of the experimental section is recorded and compared.\\n\");"+NEWLINE+
								"printf(\"The replay_output_write_time took %.6lf seconds.\\n\\n\", replay_output_write_time);"+NEWLINE+
								"}"+NEWLINE+
								"gettimeofday(&prgend, NULL);"+NEWLINE+
								"prg_time_used = (double) ((prgend.tv_sec * 1000000 + prgend.tv_usec) - (prgstart.tv_sec * 1000000 + prgstart.tv_usec)) / 1000000;"+NEWLINE+
								"printf(\"\\nExecution time of the experimental section in the Replaying Phase: %.6lf (seconds) \\n\", prg_time_used);"+NEWLINE+
								"printf(\"Overhead of the Replaying Phase is: %.6lf (seconds) \\n\", prg_time_used-exp_time_used);"+NEWLINE+
								"printf(\"Execution time of the (modified) experimental section: %.6lf (seconds) \\n\", exp_time_used);"+NEWLINE+
								"_ret_val_0=0;"+NEWLINE+
								"return _ret_val_0;"+NEWLINE+  //added
								"}"+NEWLINE+ //added
								"#endif //Experimental "+ NEWLINE+
								"#ifdef Initial "+NEWLINE;
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
			//            else if (t instanceof FunctionCall
			//                    && ((FunctionCall)t).getName().toString().equals("exit")) {
			//                exit_stmts.add(((Expression)t).getStatement());
			//            }
		}
		if (tunit != main_tunit) {
			tunit.addDeclarationFirst(new AnnotationDeclaration(
					new CodeAnnotation(genCode(headercode))));
		}
	}

	public String generateCetusIOSet(Set IOSet, SymbolTable symtab) {
		String cetusIOSet=null;
		String str = null;
		String str2= null;
		String result3= null;
		//String el =null;
		String output = null; //its used for filteringteh specifier
		String Specifiers=null;
		String arrayDimensions =null;
		Set<String> set = new LinkedHashSet<>();
		//This map structure is used to save the value of the ids that are initialized in case they can be used for array indices
		HashMap<String,Integer> idValues= new HashMap<>();
		//StringBuilder sb = new StringBuilder();
		for (Object o : IOSet) { 
			//System.out.println("\n\n[Ex] useSet Element:"+o); 
			//System.out.println("[Ex] Set of Declarations"+ symtab.getDeclarations()); 
			//symtab.getDeclarations().toString();
			//System.out.println("[Ex] declarations"+ symtab.getDeclarations().toString()); 
			//System.out.println("[Ex] declarations"+ symtab.getDeclarations().toArray()); 
			if (o instanceof ArrayAccess) {
                ArrayAccess acc = (ArrayAccess)o;
				//System.out.println("Here we are");
				Symbol array_symbol =
                        SymbolTools.getSymbolOf(acc.getArrayName());
				if (array_symbol instanceof NestedDeclarator) {
					//what to do with nested declarator? (* r)[m2k][m1k] = (double (* )[m2k][m1k])or
					//array_symbol instanceof NestedDeclarator is  a pointer?
					//System.out.println("Symbol is an array: array Symbol  "+ array_symbol.getSymbolName());
				}
				
				if (array_symbol instanceof VariableDeclarator && array_symbol.toString().contains("malloc")) {
					
					arrayDimensions= array_symbol.toString().substring(array_symbol.toString().indexOf("malloc")+7 , array_symbol.toString().length() - 1);
					output = Specifiers.substring( 1, Specifiers.indexOf(","));
					if(output.equals("static")) {output = Specifiers.substring(Specifiers.indexOf(",")+1);}
					set.add(output+":"+array_symbol.getSymbolName()+":"+ arrayDimensions +",");	
					
					
				 
				//if the array symbol exists in arraySize hashmap then getthe value from that structure, otherwise calculate it
				} else if (arraySize.containsKey(array_symbol.getSymbolName())) {
				    arrayDimensions = arraySize.get(array_symbol.getSymbolName());
				    //System.out.println("The value of the variable is " + arrayDimensions );
				    //let's divide all the array dimensions
				    String[] arr = arrayDimensions.split("\\*");
				    //System.out.println(Arrays.toString(arr));
				    for (String element : arr) {
				        //System.out.println(element);
				        set.add("int:"+element.trim()+",");
				    }
					Specifiers =array_symbol.getTypeSpecifiers().toString();
					output = Specifiers.substring(Specifiers.indexOf(",") + 2, Specifiers.length() - 1);
					//System.out.println(output);
				    set.add(output+":"+array_symbol.getSymbolName()+":"+ arrayDimensions +",");


				}else {
				//System.out.println("Here we are");
				//System.out.println("Symbol is an array: array Symbol  "+ array_symbol.toString());
				int frmindex=0;
				int toindex=0;
				//in case of (* r)[m2k][m1k] = (double (* )[m2k][m1k])or stops when it reaches =
				int pointerindex=array_symbol.toString().indexOf('*',frmindex );
				int symbolindex=array_symbol.toString().indexOf(array_symbol.getSymbolName(),frmindex );
				int equalindex= array_symbol.toString().indexOf('=',frmindex );
				int numIndices=0;

				StringBuilder sbArrayindices = new StringBuilder();
				frmindex = array_symbol.toString().indexOf('[');
				//System.out.println("frmindex"+frmindex);
				while(frmindex>0 && toindex>=0 && frmindex<array_symbol.toString().length() && (equalindex>frmindex+1 || equalindex<0) ) {
					
					//System.out.println("frmindex"+frmindex);
					//System.out.println("toindex"+toindex);
					//System.out.println("StringLength"+array_symbol.toString().length());
					numIndices++;
					toindex = array_symbol.toString().indexOf(']',frmindex );
					equalindex= array_symbol.toString().indexOf('=',frmindex );
					//System.out.println("toindex"+toindex);
					//System.out.println("value "+array_symbol.toString().substring(frmindex+1,toindex ));
					Specifiers =array_symbol.getTypeSpecifiers().toString();
					output = Specifiers.substring(Specifiers.indexOf(",") + 2, Specifiers.length() - 1);
					//System.out.println(output);
					if (numIndices==1 ) {
						//is it an identifier?has it a value assigned?
						//System.out.println("["+array_symbol.toString().substring(frmindex+1,toindex )+"]");
						sbArrayindices.append("["+array_symbol.toString().substring(frmindex+1,toindex )+"]");
						//System.out.println(sbArrayindices.toString());
					}else if (numIndices>1) {
						//System.out.println("*"+"["+array_symbol.toString().substring(frmindex+1,toindex )+"]");
						sbArrayindices.append("*"+"["+array_symbol.toString().substring(frmindex+1,toindex )+"]");	
						//System.out.println(sbArrayindices.toString());
					}
					frmindex =toindex+1;
				}
				
				if (pointerindex>0 && pointerindex<symbolindex) {
					
					//set.add(array_symbol.getTypeSpecifiers()+":*"+array_symbol.getSymbolName()+":"+ sbArrayindices.toString()+",");
					
					set.add(output+":*"+array_symbol.getSymbolName()+":"+ sbArrayindices.toString()+",");
				}else {
					//set.add(array_symbol.getTypeSpecifiers()+":"+array_symbol.getSymbolName()+":"+ sbArrayindices.toString()+",");
					//set.add(array_symbol.getTypeSpecifiers()+":"+array_symbol.getSymbolName()+":"+ sbArrayindices.toString()+",");
					set.add(output+":"+array_symbol.getSymbolName()+":"+ sbArrayindices.toString()+",");
				}}
				//System.out.println("Symbols are "+ symtab.getSymbols());
			}else if (o instanceof Identifier) {
				Identifier id=(Identifier)o;
				Symbol symbol = ((Identifier)o).getSymbol();
				//System.out.println("[Ex]"+ symbol.getTypeSpecifiers()+":  "+symbol  +",");
				//System.out.println("Symbol "+ symbol.getSymbolName()  +",");
				
				//System.out.println(symbol.getTypeSpecifiers()+":"+symbol.getSymbolName()+",");
				Specifiers =symbol.getTypeSpecifiers().toString();
				output = Specifiers.substring(Specifiers.indexOf(",") + 2, Specifiers.length() - 1);
				//System.out.println(output);
				
				if(symbol instanceof VariableDeclarator && Specifiers.contains("*") && symbol.toString().contains("malloc")) {
					
					arrayDimensions= symbol.toString().substring(symbol.toString().indexOf("malloc")+7 , symbol.toString().length() - 1);
					output = Specifiers.substring( 1, Specifiers.indexOf(","));
					set.add(output+":"+symbol.getSymbolName()+":"+ arrayDimensions +",");
					//break;
				}
				if (symbol instanceof NestedDeclarator) {
					//what to do with nested declarator? (* r)[m2k][m1k] = (double (* )[m2k][m1k])or
					//if the array symbol exists in arraySize hashmap then getthe value from that structure, otherwise calculate it
					if (arraySize.containsKey(symbol.getSymbolName())) {
					    arrayDimensions = arraySize.get(symbol.getSymbolName());
					    //System.out.println("The value of the variable is " + arrayDimensions );
					    //let's divide all the array dimensions
					    String[] arr = arrayDimensions.split("\\*");
					    //System.out.println(Arrays.toString(arr));
					    for (String element : arr) {
					        //System.out.println(element);
					        set.add("int:"+element.trim()+",");
					    }
					    set.add(output+":"+symbol.getSymbolName()+":"+ arrayDimensions +",");

					}else {
					int frmindex=0;
					int toindex=0;
					//in case of (* r)[m2k][m1k] = (double (* )[m2k][m1k])or stops when it reaches =
					int pointerindex=symbol.toString().indexOf('*',frmindex );
					int symbolindex=symbol.toString().indexOf(symbol.getSymbolName(),frmindex );
					int equalindex= symbol.toString().indexOf('=',frmindex );
					int numIndices=0;
					StringBuilder sbArrayindices = new StringBuilder();
					frmindex = symbol.toString().indexOf('[');
					//System.out.println("frmindex"+frmindex);
					while(frmindex>0 && toindex>=0 && frmindex<symbol.toString().length() && equalindex>frmindex+1) {
						//System.out.println("frmindex"+frmindex);
						//System.out.println("toindex"+toindex);
						//System.out.println("StringLength"+symbol.toString().length());
						numIndices++;
						toindex = symbol.toString().indexOf(']',frmindex );
						equalindex= symbol.toString().indexOf('=',frmindex );
						//System.out.println("toindex"+toindex);
						//System.out.println("value "+symbol.toString().substring(frmindex+1,toindex ));
						if (numIndices==1 ) {
							//is it an identifier?has it a value assigned?
							//System.out.println("["+symbol.toString().substring(frmindex+1,toindex )+"]");
							sbArrayindices.append("["+symbol.toString().substring(frmindex+1,toindex )+"]");
							//System.out.println(sbArrayindices.toString());
						}else if (numIndices>1) {
							//System.out.println("*"+"["+symbol.toString().substring(frmindex+1,toindex )+"]");
							sbArrayindices.append("*"+"["+symbol.toString().substring(frmindex+1,toindex )+"]");	
							//System.out.println(sbArrayindices.toString());
						}
						frmindex =toindex+1;
					}
					
					if (pointerindex>0 && pointerindex<symbolindex) {
						set.add(symbol.getTypeSpecifiers()+":*"+symbol.getSymbolName()+":"+ sbArrayindices.toString()+",");
					}else {
						set.add(symbol.getTypeSpecifiers()+":"+symbol.getSymbolName()+":"+ sbArrayindices.toString()+",");
					}}
				}
				//for variables that are not initialized
				if(symbol.getSymbolName().equals(symbol.toString())) {
					//set.add(symbol.getTypeSpecifiers()+":"+symbol.getSymbolName()+",");
					
					set.add(output+":"+symbol.getSymbolName()+",");
					//variables that are initialized
				}else if (symbol.toString().contains("=") && !(symbol instanceof NestedDeclarator)) {
					//System.out.println("Symbol contains '=' assignment operation");
					//set.add(symbol.getTypeSpecifiers()+":"+symbol.getSymbolName()+",");
					if(symbol.toString().contains("malloc")) {
						arrayDimensions= symbol.toString().substring(symbol.toString().indexOf("malloc")+7 , symbol.toString().length() - 1);
						output = Specifiers.substring( 1, Specifiers.indexOf(","));
						set.add(output+":"+symbol.getSymbolName()+":"+ arrayDimensions +",");
					}else {
					
					set.add(output+":"+symbol.getSymbolName()+",");
					//get the value
					String strSymbolwithValue = symbol.toString();
					//System.out.println("string "+strSymbolwithValue);
					int fromindex = strSymbolwithValue.indexOf('=');
					//int toindex = strSymbolwithValue.indexOf(' ');
					String value= strSymbolwithValue.substring(fromindex+1 ).trim();
					//System.out.println("value"+value);
					//save the symbol and its value in the map idValues
					 try {
					idValues.put("["+symbol.getSymbolName().toString()+"]", Integer.parseInt(value));
					    } catch (NumberFormatException e) {
					        //handle exception
					    	//what to do in case of (* r)[m2k][m1k] = (double (* )[m2k][m1k])or,
					    	//forget about the value, just create the variable correctly. if it is array, create the correct Format.class if it is pointer create teh correct format.
					    }
					}
//				}else if (o instanceof ArrayAccess) {
//	                ArrayAccess acc = (ArrayAccess)o;
//					System.out.println("Here we are");
					
					}else if ((symbol.toString().contains("[")  && !(symbol instanceof NestedDeclarator)) || o instanceof ArrayAccess  ) {
					//System.out.println("Symbol is an array ");
					int frmindex=0;
					int toindex=0;
					int numIndices=0;
					StringBuilder sbArrayindices = new StringBuilder();
					frmindex = symbol.toString().indexOf('[');
					//System.out.println("frmindex"+frmindex);
					while(frmindex>0 && toindex>=0 && frmindex<symbol.toString().length() ) {
						numIndices++;
						toindex = symbol.toString().indexOf(']',frmindex );
						//System.out.println("toindex"+toindex);
						//System.out.println("value "+symbol.toString().substring(frmindex+1,toindex ));
						if (numIndices==1) {
							sbArrayindices.append("["+symbol.toString().substring(frmindex+1,toindex )+"]");
						}else if (numIndices>1) {
							sbArrayindices.append("*"+"["+symbol.toString().substring(frmindex+1,toindex )+"]");	
						}
						frmindex =toindex+1;
					}
					//set.add(symbol.getTypeSpecifiers()+":"+symbol.getSymbolName()+":"+ sbArrayindices.toString()+",");
					set.add(output+":"+symbol.getSymbolName()+":"+ sbArrayindices.toString()+",");
					//System.out.println("Symbols are "+ symtab.getSymbols());
				}
				//                    	    else if (o instanceof ArrayAccess) {
				//                    	    	ArrayAccess acc = (ArrayAccess)o;
				//                    	    	Symbol array_symbol =
				//                                        SymbolTools.getSymbolOf(acc.getArrayName());
				//                    	    	//Symbol symbol = ((Identifier)o).getSymbol();
				//                    	    	System.out.println("[Ex]"+ array_symbol.getTypeSpecifiers()+":"+array_symbol +":"+ acc.getNumIndices()+"\n");
				//                    	    	set.add(array_symbol.getTypeSpecifiers()+":"+array_symbol +":"+",");
				//                    	    }
			}
			//joins all the elements of the set into a String
			str = String.join("", set);
			//str2=null;
			
			//print map idValues
			//System.out.println("[Ex] value"+idValues);

			//iterate over the map and replace the keys with the values in the str
			//Get the key set
			Set<String> keys = idValues.keySet();
			if (!keys.isEmpty()) {
			//Iterate over the key set
			for (String key : keys) {
				//Get the value for the current key
				Integer valueforkey = idValues.get(key);

				//Print the key-value pair
				//System.out.println("\nkey "+ key + " =  value" + valueforkey);
				
				//System.out.println(str);
				//replaces keys in the String with the value of the map. Regex is used for replaceAll
				str2 = str.replaceAll( "\\"+key, valueforkey.toString());
				//all keys are now replaced with values
				//System.out.println(str2);
				//multiplication should be done
				//creating an array of Strings from the string
				String[] arr = str2.split(",");
				for(int i=0;i<arr.length;i++)
				{
				    if(arr[i].contains("*"))
				    {
				    	int indexmult = arr[i].indexOf("*");
						//System.out.println(indexmult);
						//if it is not found -1 would be returned
						int indexcolon = arr[i].lastIndexOf(':', arr[i].indexOf('*'));
						//System.out.println(indexcolon);
				        int res = Integer.parseInt(arr[i].substring(indexcolon+1,indexmult )) * Integer.parseInt(arr[i].substring(indexmult+1 ));
				        arr[i] = arr[i].replace(arr[i].substring(indexcolon+1 ), Integer.toString(res));
				    }
				
				}
				
				result3 = String.join(",", arr);
				//System.out.println(result3);
				//replace all [ and ]
				//cetusIOSet= "In="+result3.replaceAll("[\\[\\]]", "");
				cetusIOSet= result3.replaceAll("[\\[\\]]", "");
				//the input to the experimental section
				//System.out.println("[EX] Cetus Input set created: "+ cetusIOSet);
				//In=int:n,float:a:10000,float:b:10000,float:t:100
				
			}
			//what if no Key set exists?
			}else if (keys.isEmpty()) {
				cetusIOSet= str.replaceAll("[\\[\\]]", "");
				//cetusIOSet= "In="+str.replaceAll("[\\[\\]]", "");
				//the input to the experimental section
				//System.out.println("[EX] Cetus set created-no key exists: "+ cetusIOSet);
			}
		}
		return cetusIOSet;
	}
	

	private String compare_output_vars(String output) {
		// Create the string that should be added after the timer for saving the value of output variables to the file
		//save the variables in the order that should be retrieved
		//System.out.println("Output"+ output.substring(4)); //float:t,float:b:1000000
		//System.out.println("Output length"+ output.length());//27
		int fromIndex=4;
		int toIndex=0;
		String varType;
		String varName;
		String varSize;
		int idx1=0,idx2=0,idx3=0;
		StringBuilder sbfilePointer = new StringBuilder("0"); 
		StringBuilder stb=new StringBuilder();

		if(output.length()>4) {
			while(fromIndex<output.length()) {
				//System.out.println("fromIndex"+ fromIndex);//6
				toIndex=output.indexOf(":", fromIndex); //6
				if (toIndex<0) {
					stb.append("");
					break;
				}
				//         	//System.out.println(input.indexOf(":", fromIndex));//6
				//         	//System.out.println(input.substring(fromIndex, toIndex));//int 
				varType= output.substring(fromIndex, toIndex); //int
				//System.out.println("varType fromIndex "+fromIndex+" toIndex "+ toIndex + "="+ varType);
				fromIndex=toIndex+1; //6

				idx1=output.indexOf(":", fromIndex);//14
				//System.out.println("idx1    "+ idx1);
				idx2=output.indexOf(",", fromIndex);//8
				//System.out.println("idx2   "+idx2);
				if (idx2<idx1 && idx2>0 && idx1>0) {
					//we have reached a comma
					//System.out.println("create the fwrite sentence ");
					toIndex=idx2;
					varName=output.substring(fromIndex, toIndex);
					//System.out.println("varName fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varName );
					//stb.append("\tif(fwrite(&"+varName +", sizeof("+varType+ "), 1,initialOutputStateFile )<1){printf(\"error in writing \'"+ varName +"\' to file\");}" );
//					stb.append("#undef DATATYPE");
//					stb.append( NEWLINE);
//					stb.append("#define DATATYPE "+ varType);
//					stb.append( NEWLINE);
					stb.append("compare_two_"+varType+"_variables_in_binary_files(\"initialOutputStateFile"+expname+"\",\"modifiedOutputStateFile"+expname+"\",\""+varName+"\", sizeof("+varType+"), 1, \""+varType+"\","+sbfilePointer.toString()+");");
					sbfilePointer.append("+sizeof("+varType+")");
					//sizeof(int.class)
					//stb.append("read_var_from_file(\""+varName+"\","+"&"+varName +", sizeof("+varType+ "), 1,"+filename+" );" );
					stb.append( NEWLINE);
					//typeformat for printfunction///////////////////////////////////////////////////variable format for print function should be recognized pas vartype to the function and get print format
					//printf("variable Name= %S ,Variable Value= %lf",varName, varName);

					//         		stb.append("	if(verbosity > 0) {printf(\"Variable "+varName+"'s Value= "+ formatSpecifier(varType)+" \\n\",");
					//         		stb.append(varName);
					//         		stb.append(");}");

					//printf("The initial code (before modifications) took %.3lf seconds to run.\n", initial_exp_time_used);
					//stb.append("printf(\"variable Name= "+varName+" ,Variable Value= "+varName+");" );
					//stb.append( NEWLINE);
					//System.out.println(stb.toString());
					fromIndex=toIndex+1;
				}else if((idx2>idx1 && idx2>0 && idx1>0) ||(idx2<0 && idx1>0)){
					//you have reached :
					//System.out.println("process array data type, single dimension array");
					toIndex=idx1;
					varName=output.substring(fromIndex, toIndex);
					//System.out.println("varName fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varName );

					fromIndex=toIndex+1;
					toIndex=output.indexOf(",", fromIndex);//8
					if(toIndex>0) {
						varSize=output.substring(fromIndex, toIndex);
						fromIndex=toIndex+1;
					}else {
						varSize=output.substring(fromIndex);
						fromIndex=output.length();
					}
					//System.out.println("varSize fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varSize );
					//System.out.println(varSize);
					//stb.append("\tif(fwrite("+ varName +", sizeof("+varType+ "), "+varSize+",initialOutputStateFile )<"+varSize+"){printf(\"error in writing \'"+varName+"\' to file\");}" );
//					stb.append("#undef DATATYPE");
//					stb.append( NEWLINE);
//					stb.append("#define DATATYPE "+ varType);
//					stb.append( NEWLINE);
					stb.append("compare_two_"+varType+"_variables_in_binary_files(\"initialOutputStateFile"+expname+"\",\"modifiedOutputStateFile"+expname+"\",\""+varName+"\", sizeof("+varType+"), "+varSize+", \""+varType+"\","+sbfilePointer.toString()+");");
					sbfilePointer.append("+("+varSize+"*sizeof("+varType+"))");
					//filePointer= filePointer+ ( Float.SIZE * Integer.parseInt(varSize));
					//stb.append("read_var_from_file(\""+varName+"\","+"&"+varName +", sizeof("+varType+ "), 1,"+filename+" );" );
					stb.append( NEWLINE);
					//stb.append("read_var_from_file(\""+varName+"\","+ varName +", sizeof("+varType+ "), "+varSize+","+filename+" );" );
					//stb.append( NEWLINE);
					//stb.append( "//uncomment to see the value of array elements");
					//stb.append( "if(verbosity > 0) {");

					//         		stb.append( NEWLINE);
					//         		stb.append( "if(verbosity > 0) {for(int loop = 0; loop <"+ varSize+"; loop++)");
					//         		stb.append( NEWLINE);
					//         		stb.append( "  printf(\""+varName+"[%d]="+formatSpecifier(varType)+"\\t\", "+"loop,"+varName+"[loop]);}");

					//stb.append( NEWLINE);
					//stb.append( "}");
					//let teh user see the array elements
					//stb.append( NEWLINE);
					//System.out.println(stb.toString());
					//System.out.println("last fromIndex"+fromIndex);
				}else if((idx2>0 && idx1<0) ){
					toIndex=idx2;
					varName=output.substring(fromIndex, toIndex);
					//System.out.println("varName fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varName);
//					stb.append("#undef DATATYPE");
//					stb.append( NEWLINE);
//					stb.append("#define DATATYPE "+ varType);
//					stb.append( NEWLINE);
					stb.append("compare_two_"+varType+"_variables_in_binary_files(\"initialOutputStateFile"+expname+"\",\"modifiedOutputStateFile"+expname+"\",\""+varName+"\", sizeof("+varType+"), 1, \""+varType+"\","+sbfilePointer.toString()+");");
					sbfilePointer.append("+sizeof("+varType+")");
					stb.append( NEWLINE);
					//System.out.println(stb.toString());
					fromIndex=toIndex+1;
				}else if((idx2<0 && idx1<0) ){
					toIndex=output.length();
					varName=output.substring(fromIndex, toIndex);
					//System.out.println("varName fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varName);
//					stb.append("#undef DATATYPE");
//					stb.append( NEWLINE);
//					stb.append("#define DATATYPE "+ varType);
//					stb.append( NEWLINE);
					stb.append("compare_two_"+varType+"_variables_in_binary_files(\"initialOutputStateFile"+expname+"\",\"modifiedOutputStateFile"+expname+"\",\""+varName+"\", sizeof("+varType+"), 1, \""+varType+"\","+sbfilePointer.toString()+");");
					sbfilePointer.append("+sizeof("+varType+")");
					//filePointer= filePointer+ Float.SIZE ;
					//stb.append("read_var_from_file(\""+varName+"\","+"&"+varName +", sizeof("+varType+ "), 1,"+filename+" );" );

					//         		stb.append( NEWLINE);
					//         		stb.append("printf(\"Variable Value= "+ formatSpecifier(varType)+" \\n\",");
					//         		stb.append(varName);
					//         		stb.append(");");
					stb.append( NEWLINE);
					//System.out.println(stb.toString());
					fromIndex++;
				}
			}
		}

		// indexOf( input, int fromIndex)

		//stb.append(b);
		//System.out.println(stb.toString());
		return stb.toString();
	}




	//public class getDataClass(String dataType) {
	//  if (dataType.equals("float")) {
	//    return Float;
	//  } else if (dataType.equals("int")) {
	//    return Integer;
	//  } else if (dataType.equals("double")) {
	//    return Double;
	////  } else if (dataType instanceof String) {
	////    return String.class;
	////  } else if (dataType instanceof Boolean) {
	////    return Boolean.class;
	//  } else {
	//    return null;
	//  }
	//}


	private String read_output_vars_from_file(String output, String filename) {
		// Create the string that should be added after the timer for saving the value of output variables to the file
		//save the variables in the order that should be retrieved
		//System.out.println("Output"+ output.substring(4)); //float:t,float:b:1000000
		//System.out.println("Output length"+ output.length());//27
		int fromIndex=4;
		int toIndex=0;
		String varType;
		String varName;
		String varSize;
		int idx1=0,idx2=0,idx3=0;
		StringBuilder stb=new StringBuilder();

		if(output.length()>4) {
			while(fromIndex<output.length()) {
				//System.out.println("fromIndex"+ fromIndex);//6
				toIndex=output.indexOf(":", fromIndex); //6
				//        	//System.out.println(input.indexOf(":", fromIndex));//6
				//        	//System.out.println(input.substring(fromIndex, toIndex));//int 
				if(toIndex<0) {
					stb.append("");
					break;
				}
				varType= output.substring(fromIndex, toIndex); //int
				//System.out.println("varType fromIndex "+fromIndex+" toIndex "+ toIndex + "="+ varType);
				fromIndex=toIndex+1; //6

				idx1=output.indexOf(":", fromIndex);//14
				//System.out.println("idx1    "+ idx1);
				idx2=output.indexOf(",", fromIndex);//8
				//System.out.println("idx2   "+idx2);
				if (idx2<idx1 && idx2>0 && idx1>0) {
					//we have reached a comma
					////System.out.println("create the fwrite sentence ");
					toIndex=idx2;
					varName=output.substring(fromIndex, toIndex);
					//System.out.println("varName fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varName );
					//stb.append("\tif(fwrite(&"+varName +", sizeof("+varType+ "), 1,initialOutputStateFile )<1){printf(\"error in writing \'"+ varName +"\' to file\");}" );
					//stb.append("#undef DATATYPE");
					//stb.append( NEWLINE);
					//stb.append("#define DATATYPE "+ varType);
					//stb.append( NEWLINE);
					//stb.append("compare_two_variables_in_binary_files(\"initialOutputStateFile\",\"modifiedOutputStateFile\",\""+varName+"\", sizeof("+varType+"), 1, \""+varType+"\");");
					stb.append("read_var_from_file(\""+varName+"\","+"&"+varName +", sizeof("+varType+ "), 1,"+filename+" );" );
					stb.append( NEWLINE);
					//typeformat for printfunction///////////////////////////////////////////////////variable format for print function should be recognized pas vartype to the function and get print format
					//printf("variable Name= %S ,Variable Value= %lf",varName, varName);
					stb.append("printf(\""+varName+"="+ formatSpecifier(varType)+" \\n\",");
					//stb.append("printf(\"Variable "+varName+"'s Value= "+ formatSpecifier(varType)+" \\n\",");
					stb.append(varName);
					stb.append(");");

					//printf("The initial code (before modifications) took %.3lf seconds to run.\n", initial_exp_time_used);
					//stb.append("printf(\"variable Name= "+varName+" ,Variable Value= "+varName+");" );
					stb.append( NEWLINE);
					//System.out.println(stb.toString());
					fromIndex=toIndex+1;
				}else if(idx1<0 && idx2>0 ) {
					fromIndex=toIndex+1;
					//System.out.println("fromIndex"+ fromIndex);//6
					toIndex=idx2;
					//System.out.println("toIndex"+ toIndex);
					varName=output.substring(fromIndex, toIndex);
					//System.out.println("varName fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varName );
					//    		stb.append("\tif(fwrite(&"+varName +", sizeof("+varType+ "), 1,initialInputStateFile )<1){printf(\"error in writing \'"+ varName +"\' to file\");}" );
					stb.append("read_var_from_file(\""+varName+"\","+"&"+varName +", sizeof("+varType+ "), 1,"+filename+" );" );
					stb.append( NEWLINE);
					stb.append("printf(\""+varName+"="+ formatSpecifier(varType)+" \\n\",");
					//stb.append("printf(\"Variable "+varName+"'s Value= "+ formatSpecifier(varType)+" \\n\",");
					stb.append(varName);
					stb.append(");");
					//System.out.println(stb.toString());
					fromIndex=toIndex+1;
				}else if((idx2>idx1 && idx2>0 && idx1>0) ||(idx2<0 && idx1>0)){
					//you have reached :
					//System.out.println("process array data type, single dimension array");
					toIndex=idx1;
					varName=output.substring(fromIndex, toIndex);
					//System.out.println("varName fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varName );

					fromIndex=toIndex+1;
					toIndex=output.indexOf(",", fromIndex);//8
					if(toIndex>0) {
						varSize=output.substring(fromIndex, toIndex);
						fromIndex=toIndex+1;
					}else {
						varSize=output.substring(fromIndex);
						fromIndex=output.length();
					}
					//System.out.println("varSize fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varSize );
					//System.out.println(varSize);
					//stb.append("\tif(fwrite("+ varName +", sizeof("+varType+ "), "+varSize+",initialOutputStateFile )<"+varSize+"){printf(\"error in writing \'"+varName+"\' to file\");}" );
					//stb.append("#undef DATATYPE");
					//stb.append( NEWLINE);
					//stb.append("#define DATATYPE "+ varType);
					//stb.append( NEWLINE);
					//stb.append("compare_two_variables_in_binary_files(\"initialOutputStateFile\",\"modifiedOutputStateFile\",\""+varName+"\", sizeof("+varType+"), "+varSize+", \""+varType+"\");");
					stb.append("read_var_from_file(\""+varName+"\","+varName +", sizeof("+varType+ "),"+varSize+","+filename+" );" );
					//stb.append( NEWLINE);
					//stb.append("read_var_from_file(\""+varName+"\","+ varName +", sizeof("+varType+ "), "+varSize+","+filename+" );" );
					//stb.append( NEWLINE);
					//stb.append( "The value of array elements");
					stb.append( NEWLINE);
					stb.append( "for(int loop = 0; loop <"+ varSize+"; loop++)");
					stb.append( NEWLINE);
					stb.append( "  		printf(\""+varName+"[%d]="+formatSpecifier(varType)+"\\t\", "+"loop,"+varName+"[loop]);");
					stb.append( NEWLINE);
					stb.append( "printf(\"\\n\\n\"); ");
					//stb.append( "	printf(\""+formatSpecifier(varType)+" \", "+varName+"[loop]);");
					//let teh user see the array elements
					stb.append( NEWLINE);
					//System.out.println(stb.toString());
					//System.out.println("last fromIndex"+fromIndex);
				}else if(idx2<0 && idx1<0){
					toIndex=output.length();
					varName=output.substring(fromIndex, toIndex);
					//System.out.println("varName fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varName);
					//stb.append("#undef DATATYPE");
					//stb.append( NEWLINE);
					//stb.append("#define DATATYPE "+ varType);
					//stb.append( NEWLINE);
					//stb.append("compare_two_variables_in_binary_files(\"initialOutputStateFile\",\"modifiedOutputStateFile\",\""+varName+"\", sizeof("+varType+"), 1, \""+varType+"\");");
					stb.append("read_var_from_file(\""+varName+"\","+"&"+varName +", sizeof("+varType+ "), 1,"+filename+" );" );
					stb.append( NEWLINE);
					stb.append("printf(\""+varName+"="+ formatSpecifier(varType)+" \\n\",");
					stb.append(varName);
					stb.append(");");
					//System.out.println(stb.toString());
					fromIndex++;
				}
			}
		}

		// indexOf( input, int fromIndex)

		//stb.append(b);
		//System.out.println(stb.toString());
		return ""+filename+"= fopen(\""+filename+"\",\"rb\");"+NEWLINE+// "fseek("+filename+", 0, SEEK_SET);" +NEWLINE+
				stb.toString();//+"fclose(initialOutputStateFile);";
	}

	private String formatSpecifier(String varType) {
		String format=null;
		switch(varType) {
		case "int" :
		case "short":
		case "unsigned short":
		case "long":
			format= "%d";
			break;
		case "float" :
			format= "%.6f";
			break;
		case "double" :
			format= "%.6lf";
			break;
		case "void *" :
			format= "%p";
			break;
		case "char *" :
			format= "%s";
			break;
		case " " :
			format= "%n";
			break;
		}
		return format;
	}

	private String readInputValuesfromFile(String input) {
		//In=int:n,float:a:1000000,float:b:1000000,float:t
		//create the string that should be added before the timer starts.
		//save all variables to the file in the order you get them
		//fwrite("n", sizeof(int), 1, initialInputStateFile);
		//System.out.println("Input "+ input.substring(3)); //int:n,float:a:1000000,float:b:1000000,float:t
		//System.out.println("Input length"+ input.length());
		int fromIndex=3;
		int toIndex=0;
		String varType;
		String varName;
		String varSize;
		int idx1=0,idx2=0,idx3=0;
		StringBuilder stb=new StringBuilder();
		stb.append("/* Seek to the beginning of the file */");
		stb.append( NEWLINE);
		stb.append("fseek(initialInputStateFile"+expname+", 0, SEEK_SET);");
		stb.append( NEWLINE);
		if(input.length()>3) {
			while(fromIndex<input.length()) {
				//System.out.println("fromIndex"+ fromIndex);//6
				toIndex=input.indexOf(":", fromIndex); //6
				//    	//System.out.println(input.indexOf(":", fromIndex));//6
				//    	//System.out.println(input.substring(fromIndex, toIndex));//int 
				varType= input.substring(fromIndex, toIndex); //int
				//System.out.println("varType fromIndex "+fromIndex+" toIndex "+ toIndex + "="+ varType);
				fromIndex=toIndex+1; //6

				idx1=input.indexOf(":", fromIndex);//14
				//System.out.println("idx1    "+ idx1);
				idx2=input.indexOf(",", fromIndex);//8
				//System.out.println("idx2   "+idx2);
				if (idx2<idx1 && idx2>0 && idx1>0) {
					//we have reached a comma
					//System.out.println("create the fwrite sentence ");
					toIndex=idx2;
					varName=input.substring(fromIndex, toIndex);
					//System.out.println("varName fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varName );
					
					//stb.append("\tif(fwrite(&"+varName +", sizeof("+varType+ "), 1,initialInputStateFile )<1){printf(\"error in writing \'"+ varName +"\' to file\");}" );
					if (!main_has_exp && varName.contains("XIndex")) {
					stb.append(varType+" "+varName+";" ); //added
					stb.append( NEWLINE);}
					stb.append("read_var_from_file(\""+varName+"\","+"&"+varName +", sizeof("+varType+ "), 1,initialInputStateFile"+expname+" );" );
					stb.append( NEWLINE);
					//System.out.println(stb.toString());
					fromIndex=toIndex+1;
				}else if(idx2>idx1 && idx2>0 && idx1>0){
					//you have reached :
					//System.out.println("process array data type ");
					toIndex=idx1;
					varName=input.substring(fromIndex, toIndex);
					//System.out.println("varName fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varName );

					fromIndex=toIndex+1;
					toIndex=input.indexOf(",", fromIndex);//8
					varSize=input.substring(fromIndex, toIndex);
					//System.out.println("varSize fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varSize );
					//System.out.println(varSize);
					//stb.append("\tif(fwrite("+ varName +", sizeof("+varType+ "), "+varSize+",initialInputStateFile )<"+varSize+"){printf(\"error in writing \'"+ varName +"\' to file\");}" );
//					if (!main_has_exp) {
//						if (varSize.contains("*")) {
//						    String[] arr = varSize.split("\\*");
//						    stb.append(varType+" "+varName);
//						    for (String element : arr) {
//						        //System.out.println(element);
//						        stb.append("["+element.trim()+"]");
//						    }
//						    stb.append(";" );
//						} else {
//						    //System.out.println(varSize);
//						    stb.append(varType+" "+varName+"["+varSize+"]"+";" ); //added
//						}	
//					//stb.append(varType+" "+varName+"["+varSize+"]"+";" ); //added
//					stb.append( NEWLINE);}
					if(varSize.contains("XIndex")) {
						String[] parts = varSize.split("\\*"); // Split the input string by '*'

						//instead of creating large arrays on the stack, we created them on the heap. 
						StringBuilder mallocString = new StringBuilder(varType + " (*").append(varName).append(")");
					    for (int i = 1; i < parts.length; i++) { // Start from 1 to skip the first dimension in pointer declaration
					        mallocString.append("[").append(parts[i]).append("]");
					    }
					    mallocString.append(" = malloc(sizeof(").append(varType);
					    
					    // Construct the size portion of the malloc call including all dimensions
					    for (String part : parts) {
					        mallocString.append("[").append(part).append("]");
					    }
					    mallocString.append("));");

					    // Append the constructed declaration to the StringBuilder
					    stb.append(mallocString.toString());
					    stb.append(NEWLINE);
					    
					    
//						// Start building the variable declaration for a pointer to an array
//					    StringBuilder mallocString = new StringBuilder(varType + " ");
//					    for (int i = 0; i < parts.length; i++) {
//					        if (i == 0) {
//					            mallocString.append("(*").append(varName);
//					        }
//					        mallocString.append("[");
//					        mallocString.append(parts[i]);
//					        mallocString.append("]");
//					        if (i == 0) {
//					            mallocString.append(")");
//					        }
//					    }
//					    mallocString.append(" = malloc(sizeof(").append(varType);
//
//					    // Construct the size portion of the malloc call
//					    for (String part : parts) {
//					        mallocString.append("[").append(part).append("]");
//					    }
//					    mallocString.append("));");
//
//					    // Append the constructed declaration to the StringBuilder
//					    stb.append(mallocString.toString());
//					    stb.append(NEWLINE);
					//}
						// Create the converted string by iterating through the parts
//				        StringBuilder convertedString = new StringBuilder();
//				        for (int i = 0; i < parts.length; i++) {
//				            convertedString.append("[").append(parts[i]).append("]");
//				        }
//				        
//				        stb.append(varType+" "+varName+convertedString.toString()+";" ); //added
//				        stb.append( NEWLINE);
					}
					stb.append("read_var_from_file(\""+varName+"\","+ varName +", sizeof("+varType+ "), "+varSize+",initialInputStateFile"+expname+" );" );
					stb.append( NEWLINE);
					//System.out.println(stb.toString());
					fromIndex=toIndex+1;
					//System.out.println("last fromIndex"+fromIndex);
				}else if(idx1<0 && idx2>0 ) {
					fromIndex=toIndex+1;
					//System.out.println("fromIndex"+ fromIndex);//6
					toIndex=idx2;
					//System.out.println("toIndex"+ toIndex);
					varName=input.substring(fromIndex, toIndex);
					//System.out.println("varName fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varName );
					//    		stb.append("\tif(fwrite(&"+varName +", sizeof("+varType+ "), 1,initialInputStateFile )<1){printf(\"error in writing \'"+ varName +"\' to file\");}" );
//					if (!main_has_exp) {
//					stb.append(varType+" "+varName+";" ); //added
//					stb.append( NEWLINE);}
					stb.append("read_var_from_file(\""+varName+"\","+"&"+varName +", sizeof("+varType+ "), 1,initialInputStateFile"+expname+" );" );
					stb.append( NEWLINE);
					//System.out.println(stb.toString());
					fromIndex=toIndex+1;
				}else if(idx2<0 && idx1<0){
					toIndex=input.length();
					varName=input.substring(fromIndex, toIndex);
					//System.out.println("varName fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varName);
					//stb.append("\tif(fwrite(&"+varName +", sizeof("+varType+ "), 1,initialInputStateFile )<1){printf(\"error in writing \'"+ varName +"\' to file\");}" );
//					if (!main_has_exp) {
//					stb.append(varType+" "+varName+";" ); //added
//					stb.append( NEWLINE);}
					stb.append("read_var_from_file(\""+varName+"\","+"&"+varName +", sizeof("+varType+ "), 1,initialInputStateFile"+expname+");" );
					stb.append( NEWLINE);
					//System.out.println(stb.toString());
					fromIndex++;
				}else if(idx1>0 && idx2<0 ) {
					//System.out.println("process array data type ");
					toIndex=idx1;
					varName=input.substring(fromIndex, toIndex);
					//System.out.println("varName fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varName );

					fromIndex=toIndex+1;
					toIndex=input.length();
					varSize=input.substring(fromIndex, toIndex);
					//System.out.println("varSize fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varSize );
					//stb.append("\tif(fwrite(&"+varName +", sizeof("+varType+ "), 1,initialInputStateFile )<1){printf(\"error in writing \'"+ varName +"\' to file\");}" );
//					if (!main_has_exp) {
//					stb.append(varType+" "+varName+"["+varSize+"]"+";" ); //added
//					stb.append( NEWLINE);}
					if(varSize.contains("XIndex")) {
						String[] parts = varSize.split("\\*"); // Split the input string by '*'

						// Create the converted string by iterating through the parts
				        StringBuilder convertedString = new StringBuilder();
				        for (int i = 0; i < parts.length; i++) {
				            convertedString.append("[").append(parts[i]).append("]");
				        }
				        
				        stb.append(varType+" "+varName+convertedString.toString()+";" ); //added
				        stb.append( NEWLINE);
					}
					stb.append("read_var_from_file(\""+varName+"\","+varName +", sizeof("+varType+ "), "+varSize+",initialInputStateFile"+expname+" );" );
					stb.append( NEWLINE);
					//System.out.println(stb.toString());
					fromIndex=toIndex+1;
				}
			}
		}

		// indexOf( input, int fromIndex)

		//stb.append(b);
		//System.out.println(stb.toString());
		return stb.toString();//+"fclose(initialInputStateFile);";

	}

	private String saveOutputValuesinFile(String output, String filename) {
		// Create the string that should be added after the timer for saving the value of output variables to the file
		//save the variables in the order that should be retrieved
		//System.out.println("filename"+ filename);
		//System.out.println("Output"+ output);
		
		//System.out.println("Output"+ output.substring(4)); //float:t,float:b:1000000
		//System.out.println("Output length"+ output.length());//27
		int fromIndex=4;
		int toIndex=0;
		String varType;
		String varName;
		String varSize;
		int idx1=0,idx2=0,idx3=0;
		StringBuilder stb=new StringBuilder();

		if(output.length()>4) {
			while(fromIndex<output.length()) {
				//System.out.println("fromIndex"+ fromIndex);//6
				toIndex=output.indexOf(":", fromIndex); //6
				//in case Out=null
				if (toIndex<0) {
					stb.append("");
					break;
				}
				//        	System.out.println(input.indexOf(":", fromIndex));//6
				//        	System.out.println(input.substring(fromIndex, toIndex));//int 
				varType= output.substring(fromIndex, toIndex); //int
				//System.out.println("varType fromIndex "+fromIndex+" toIndex "+ toIndex + "="+ varType);
				fromIndex=toIndex+1; //6

				idx1=output.indexOf(":", fromIndex);//14
				//System.out.println("idx1    "+ idx1);
				idx2=output.indexOf(",", fromIndex);//8
				//System.out.println("idx2   "+idx2);
				if (idx2<idx1 && idx2>0 && idx1>0) {
					//we have reached a comma
					//System.out.println("create the fwrite sentence ");
					toIndex=idx2;
					varName=output.substring(fromIndex, toIndex);
					//System.out.println("varName fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varName );
					//stb.append("\tif(fwrite(&"+varName +", sizeof("+varType+ "), 1,initialOutputStateFile )<1){printf(\"error in writing \'"+ varName +"\' to file\");}" );
					stb.append("write_var_to_file(\""+varName+"\","+"&"+varName +", sizeof("+varType+ "), 1,"+filename+" );" );
					stb.append( NEWLINE);
					//System.out.println(stb.toString());
					fromIndex=toIndex+1;
				}else if(idx1<0 && idx2>0 ) {
					fromIndex=toIndex+1;
					//System.out.println("fromIndex"+ fromIndex);//6
					toIndex=idx2;
					//System.out.println("toIndex"+ toIndex);
					varName=output.substring(fromIndex, toIndex);
					//System.out.println("varName fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varName );
					//    		stb.append("\tif(fwrite(&"+varName +", sizeof("+varType+ "), 1,initialInputStateFile )<1){printf(\"error in writing \'"+ varName +"\' to file\");}" );
					stb.append("write_var_to_file(\""+varName+"\","+"&"+varName +", sizeof("+varType+ "), 1,"+filename+" );" );
					stb.append( NEWLINE);
					//System.out.println(stb.toString());
					fromIndex=toIndex+1;
				}else if((idx2>idx1 && idx2>0 && idx1>0) ||(idx2<0 && idx1>0)){
					//you have reached :
					//System.out.println("process array data type, single dimension array");
					toIndex=idx1;
					varName=output.substring(fromIndex, toIndex);
					//System.out.println("varName fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varName );

					fromIndex=toIndex+1;
					toIndex=output.indexOf(",", fromIndex);//8
					if(toIndex>0) {
						varSize=output.substring(fromIndex, toIndex);
						fromIndex=toIndex+1;
					}else {
						varSize=output.substring(fromIndex);
						fromIndex=output.length();
					}
					//System.out.println("varSize fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varSize );
					//System.out.println(varSize);
					//stb.append("\tif(fwrite("+ varName +", sizeof("+varType+ "), "+varSize+",initialOutputStateFile )<"+varSize+"){printf(\"error in writing \'"+varName+"\' to file\");}" );
					stb.append("write_var_to_file(\""+varName+"\","+ varName +", sizeof("+varType+ "), "+varSize+","+filename+" );" );
					stb.append( NEWLINE);
					//System.out.println(stb.toString());
					//System.out.println("last fromIndex"+fromIndex);
				}else if(idx2<0 && idx1<0){
					toIndex=output.length();
					varName=output.substring(fromIndex, toIndex);
					//System.out.println("varName fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varName);
					stb.append("write_var_to_file(\""+varName+"\","+"&"+varName +", sizeof("+varType+ "), 1,"+filename+" );" );
					stb.append( NEWLINE);
					//System.out.println(stb.toString());
					fromIndex++;
				}
			}
		}

		// indexOf( input, int fromIndex)

		//stb.append(b);
		//System.out.println(stb.toString());
		return ""+filename+" = fopen(\""+filename+"\",\"wb\");"+NEWLINE+stb.toString();//+"fclose(initialOutputStateFile);";

	}
	//get all the parametrs that the procedure has to add them to the declarator part of teh experimental section
private String getProcedureParameters(Procedure exp_proc) {
	//System.out.println("\nTraversable:::::::\t "+((Procedure) exp_proc).getDeclarator().getParameters());
	String strParameters=((Procedure) exp_proc).getDeclarator().getParameters().toString();
	// Delete "["
	strParameters = strParameters.replace("[", "");

    // Replace ","
	strParameters = strParameters.replace(",", ";");

    // Replace "]"
	strParameters = strParameters.replace("]", ";");
	//System.out.println("\nFinal parameter set:::::::\t "+strParameters);
    return strParameters;
	
}
	//create the pointer name and its size based on procedure declaration and save it in HashMap<String,Integer> idSize
	//the goal is to save the pointer size to use it in analysis for experimental sections that are introduced inside procedures other than main
	//
	//save pointers declared in the procedure based on what is passed in the arguments-checks if pointers are passed as arguments to the procedure.
	//and then checks in the declaration part of the procedure if new pointers are pointing to these pointers
	private void saveprocedureparameters(Procedure exp_proc) {
		//to save the nmae of pointers from the procedure declarator
		HashMap<String,String> idSize= new HashMap<>();
		//this variable should be declared as the global variable since it is being used in generatingCetusIO
		//HashMap<String,String> arraySize= new HashMap<>(); //double r m1k*m2k*m3k
		 String variableName= null;
		 String variableSize=null;
		 DFIterator<Traversable> iter;
		 if(exp_proc != null ){
		iter = new DFIterator<Traversable>(exp_proc);
		}else {
		iter = new DFIterator<Traversable>(main_proc);
		}
		 
		//iter.pruneOn(VariableDeclaration.class);
		//int counterPrg=0;
		while (iter.hasNext()) {
			Traversable t = iter.next();
			//it shows how many iterations of the program is traversed
			//counterPrg++;
			if (t instanceof Procedure ){//|| t instanceof DeclarationStatement || t instanceof VariableDeclaration) {
			//System.out.println("\nTraversable:::::::\t "+((Procedure) t).getDeclarator());
			
			Declarator d = ((Procedure) t).getDeclarator();
			//System.out.println("\nTraversable:::::::\t "+((Procedure) t).getDeclarator().getParameters());// [void * or, int m1k, int m2k, int m3k, void * os, int m1j, int m2j, int m3j, int k]
			//System.out.println("\nTraversable:::::::\t "+((Procedure) t).getDeclarator().getID());//rprj3
			String strParameters=((Procedure) t).getDeclarator().getParameters().toString();
			strParameters = strParameters.substring(1, strParameters.length() - 1); 
			String[] output = strParameters.split(",");
			//separated parameters are printed
			//for (String str : output) {
			for (int i = 0; i < output.length; i++) { 
				//System.out.println(output[i]);
				//if a pointer is identified read all integer parametrs after it
				//1st element, 5th element,9th element
				 if(output[i].contains("*")) {  //void * or- all pointers passed are of type void
					 variableName= output[i].substring(output[i].indexOf("*") + 1);
					 variableSize=null;//set the size to 1 since array indices are multiplied together
					 idSize.put(variableName, variableSize);
					 //System.out.print("\n"+output[i].substring(output[i].indexOf("*") + 1));
				 }
//				 else { //it is not a pointer
//					 System.out.print("\n"+ output[i]);
//				 }
				 //2nd element
//				if( ++i < output.length && output[i].contains("int ")) {
//					 variableSize= output[i].substring(4, output[i].length());
//					 idSize.put(variableName, variableSize);
//					 //idSize.put(variableName, variableSize);
//					 System.out.print("["+ output[i].substring(4, output[i].length())+"]");
//				 }else if(i< output.length && output[i].contains("*") ) { //another pointer
//					 System.out.print("\n"+output[i].substring(output[i].indexOf("*") + 1));
//				 }else {break;}
				 
				 //3rd element
//				 if(i >= output.length) {
//					 break;
//				 }else if(output[++i].contains("int") && i < output.length) {
//					 variableSize = variableSize+"*"+output[i].substring(4, output[i].length());
//					 idSize.put(variableName, variableSize);
//					 System.out.print("["+output[i].substring(4, output[i].length())+"]");
//				 }else if(output[i].contains("*") && i < output.length) { //another pointer
//					 System.out.print("\n"+output[i].substring(output[i].indexOf("*") + 1));
//				 }
//				 
				 //4th element
//				 if(i >= output.length) {
//					 break;
//				 }else if(output[++i].contains("int") && i < output.length) {
//					 variableSize = variableSize+"*"+output[i].substring(4, output[i].length());
//					 idSize.put(variableName, variableSize);
//					 System.out.print("["+output[i].substring(4, output[i].length())+"]");
//				 }else if(output[i].contains("*") && i < output.length) { //another pointer
//					 System.out.print("\n"+output[i].substring(output[i].indexOf("*") + 1));
//				 }

			}
		
			}
			else if( t instanceof DeclarationStatement) {
				//if it contains the pointer name saved in the idSize on the right hand side then put the name of the pointer on the left hand size to the 
				//idSize structure and put the same size as for the pointer passed to the procedure in it
				//then find that pointer name in the inset and outset and replace it with new size and the array name
				
//				System.out.println("\ngetDeclaration"+((DeclarationStatement) t).getDeclaration());//(* r)[m2k][m1k] = (double (* )[m2k][m1k])or
//				System.out.println("\ngetDeclaredIDs"+((DeclarationStatement) t).getDeclaration().getDeclaredIDs());//[r]
//				
//				if(((DeclarationStatement) t).getDeclaration().toString().contains("r")){
//				System.out.println("\ncontains or");
//				}else {System.out.println("\nDoes not contains or");}
				
				Set<String> keys = idSize.keySet();
				if (!keys.isEmpty()) {
				    //Iterate over the key set
				    for (String key : keys) {
				    	//if the key exists in this declartaion statement
				    	if(((DeclarationStatement) t).getDeclaration().toString().contains(key.trim())){
				    		//now that the key exist in here,get the name of declared variable and set the same value as for key to this element
				    		//System.out.println("\ngetDeclaredIDs"+((DeclarationStatement) t).getDeclaration());
				    		
				    		int index=((DeclarationStatement) t).getDeclaration().toString().indexOf(" ");
				    		String type=((DeclarationStatement) t).getDeclaration().toString().substring(0, index); 
				    		//System.out.println("\ngetDeclaredIDs"+((DeclarationStatement) t).getDeclaration().getDeclaredIDs().toString());
							//System.out.println("\ncontains "+ key.trim());
							
							 String str = ((DeclarationStatement) t).getDeclaration().toString();
							 int i = 0; //used for the while loop
							 String s = ""; 
							 String idName=((DeclarationStatement) t).getDeclaration().getDeclaredIDs().toString(); //[r]
						     String output = idName.substring(1, idName.length()-1)+"XIndex"; 
						    
						     String[] first = str.split("="); //only first half of string is parsed
						     // Split before = 
						     String[] second = first[0].split("\\["); //splits based [
						     
						  // To extract all contents between [] 
						        while (i < second.length - 1) { 
						        	 // Extracting contents 
						        	output=output+ "*";
						            s = second[i + 1]; 
						            int l = s.indexOf("]"); 
						            s = s.substring(0, l ); 
						            // Calculating product 
						            
						         // Concatinating output string 
						            output = output+ s ; 
						            i++; 
						        }
						        //System.out.println(output); //rXIndex*m2k*m1k
				    	
//							String value= idSize.get(key);//get the value
//							idSize.remove(key);//remove the key
							//arraySize.put(((DeclarationStatement) t).getDeclaration().getDeclaredIDs().toString().replace("[", "").replace("]", ""), idSize.get(key));//replace new key for the old value
							arraySize.put(((DeclarationStatement) t).getDeclaration().getDeclaredIDs().toString().replace("[", "").replace("]", ""),output);
							//should I save the type in the hashmap arraySize?NO// what does inset and outset already holds?double:*s:m2j*m1j, double:*r:m2k*m1k
							}else {
								//System.out.println("\nDoes not contain "+key);
							}
				        //Get the value for the current key
				        //String valueforkey = idSize.get(key);

				        //Print the key-value pair
				       // System.out.println("\nKey "+ key + " =  Value " + valueforkey);
				    }
				}
			}
		}
		//at this point we have saved the pointers and their sizes passed to the program through call by reference.
		//now we iterate through the procedure declaration and find those pointers pointing to these pointers. if there was an assignment then add the name of the new pointer to the list
		//then save this name in the inset and outset string created.
		//Iterate over the map and print the key-value pairs
		Set<String> keys = idSize.keySet();
		if (!keys.isEmpty()) {
		    //Iterate over the key set
		    for (String key : keys) {
		        //Get the value for the current key
		        String valueforkey = idSize.get(key);

		        //Print the key-value pair
		        //System.out.println("\nKey "+ key + " =  Value " + valueforkey);
		    }
		}//create the pointer size in form of [m3k][m2k][m1]? or[m1k] [m2k] [m3k];// save this in a data structure,pointer nmae and pointer size. it should be  aglobal data structure. then when you require the size of the third elemnt,if it exists in that datastructure. us e it to get the size for the 3rd elment.

		return;
		}
	//save the decalration pasrt of the procedure containingthe experimental section
	//private String saveproceduredeclaeartioninFile(Procedure exp_proc) {
//		DFIterator<Traversable> iter = new DFIterator<Traversable>(exp_proc);
//		iter.pruneOn(VariableDeclaration.class);
//		//int counterPrg=0;
//		while (iter.hasNext()) {
//			Traversable t = iter.next();
//			//it shows how many iterations of the program is traversed
//			//counterPrg++;
//			if ( t instanceof DeclarationStatement || t instanceof VariableDeclaration) {
//			System.out.println("\nTraversable:::::::\t "+t);
//			}
//		}
//		return null;
//		}
	//Input double:t4,logical:timers_enabled,double:t1,static, double:x:(2*(1<<16)),double:t3,double:t2,int:i,double:x1,static, double:q:10,double:x2,int:k_offset,int:l,int:k,int:np,int:ik,double, *:reduce:,int:kk,double:an,int:reduce_span_0,double, *:reduce,double:sx,double:sy,
	private String saveInputValuesinFile(String input) {
		saveprocedureparameters(exp_proc);
		//saveproceduredeclaeartioninFile(exp_proc);
		//In=int:n,float:a:1000000,float:b:1000000,float:t
		//create the string that should be added before the timer starts.
		//save all variables to the file in the order you get them
		//fwrite("n", sizeof(int), 1, initialInputStateFile);
		//System.out.println("Input "+ input.substring(3)); //int:n,float:a:1000000,float:b:1000000,float:t
		//System.out.println("Input length"+ input.length());
		int fromIndex=3;
		int toIndex=0;
		String varType;
		String varName;
		String varSize;
		int idx1=0,idx2=0,idx3=0;
		StringBuilder stb=new StringBuilder();
		StringBuilder stbpointers=new StringBuilder();//string builder for pointer size

		if(input.length()>3) {
			while(fromIndex<input.length()) {
				//System.out.println("fromIndex"+ fromIndex);//6
				toIndex=input.indexOf(":", fromIndex); //6
				//System.out.println("toIndex"+ toIndex);
				//System.out.println("input.length()"+ input.length());
				//System.out.println(input.indexOf(":", fromIndex));//6
				//    	System.out.println(input.substring(fromIndex, toIndex));//int 
				varType= input.substring(fromIndex, toIndex); //int
				//System.out.println("varType fromIndex "+fromIndex+" toIndex "+ toIndex + "="+ varType);
				fromIndex=toIndex+1; //6

				idx1=input.indexOf(":", fromIndex);//14
				//System.out.println("idx1    "+ idx1);
				idx2=input.indexOf(",", fromIndex);//8
				//System.out.println("idx2   "+idx2);
				if (idx2<idx1 && idx2>0 && idx1>0) {
					//we have reached a comma
					//System.out.println("create the fwrite sentence ");
					toIndex=idx2;
					varName=input.substring(fromIndex, toIndex);
					//System.out.println("varName fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varName );
					//    		stb.append("\tif(fwrite(&"+varName +", sizeof("+varType+ "), 1,initialInputStateFile )<1){printf(\"error in writing \'"+ varName +"\' to file\");}" );
					stb.append("write_var_to_file(\""+varName+"\","+"&"+varName +", sizeof("+varType+ "), 1,initialInputStateFile"+expname+" );" );
					stb.append( NEWLINE);
					//System.out.println(stb.toString());
					fromIndex=toIndex+1;
				}else if(idx1<0 && idx2>0 ) {
					fromIndex=toIndex+1;
					//System.out.println("fromIndex"+ fromIndex);//6
					toIndex=idx2;
					//System.out.println("toIndex"+ toIndex);
					varName=input.substring(fromIndex, toIndex);
					//System.out.println("varName fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varName );
					//    		stb.append("\tif(fwrite(&"+varName +", sizeof("+varType+ "), 1,initialInputStateFile )<1){printf(\"error in writing \'"+ varName +"\' to file\");}" );
					stb.append("write_var_to_file(\""+varName+"\","+"&"+varName +", sizeof("+varType+ "), 1,initialInputStateFile"+expname+" );" );
					stb.append( NEWLINE);
					//System.out.println(stb.toString());
					fromIndex=toIndex+1;
				}else if((idx2>idx1 && idx2>0 && idx1>0) ){
					//you have reached :
					//System.out.println("process array data type ");
					toIndex=idx1;
					varName=input.substring(fromIndex, toIndex);
					//System.out.println("varName fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varName );

					fromIndex=toIndex+1;
					toIndex=input.indexOf(",", fromIndex);//8
					varSize=input.substring(fromIndex, toIndex);
					//System.out.println("varSize fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varSize );
					//System.out.println(varSize);
					//stb.append("\tif(fwrite("+ varName +", sizeof("+varType+ "), "+varSize+",initialInputStateFile )<"+varSize+"){printf(\"error in writing \'"+ varName +"\' to file\");}" );
					if(varSize.contains("XIndex")) {
						String firstIndex=varSize.substring(0, varSize.indexOf("*"));
						String dereference= varSize.replaceAll("[^\\*]","");
						//System.out.println("int "+firstIndex +"= (sizeof("+dereference+varName+")/sizeof("+varType+"));" );
						
						//System.out.println(varSize.replaceAll("[^\\*]","") );
//						stb.insert(0, NEWLINE);
//						stb.insert(0,"int "+firstIndex +"= (sizeof("+dereference+varName+")/sizeof("+varType+"));");		
						stbpointers.insert(0,"int "+firstIndex +"= (sizeof("+dereference+varName+")/sizeof("+varType+"));");		
						stbpointers.insert(0, NEWLINE);
						//stb.insert(0, NEWLINE);
						}
					stb.append("write_var_to_file(\""+varName+"\","+ varName +", sizeof("+varType+ "), "+varSize+",initialInputStateFile"+expname+" );" );
					stb.append( NEWLINE);
					//System.out.println(stb.toString());
					fromIndex=toIndex+1;
					//System.out.println("last fromIndex"+fromIndex);
				}else if(idx2<0 && idx1<0){
					toIndex=input.length();
					varName=input.substring(fromIndex, toIndex);
					//System.out.println("varName fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varName);
					//stb.append("\tif(fwrite(&"+varName +", sizeof("+varType+ "), 1,initialInputStateFile )<1){printf(\"error in writing \'"+ varName +"\' to file\");}" );
					stb.append("write_var_to_file(\""+varName+"\","+"&"+varName +", sizeof("+varType+ "), 1,initialInputStateFile"+expname+" );" );
					stb.append( NEWLINE);
					//System.out.println(stb.toString());
					fromIndex++;
				}else if(idx1>0 && idx2<0 ) {
					//System.out.println("process array data type ");
					toIndex=idx1;
					varName=input.substring(fromIndex, toIndex);
					//System.out.println("varName fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varName );

					fromIndex=toIndex+1;
					toIndex=input.length();
					varSize=input.substring(fromIndex, toIndex);
					//System.out.println("varSize fromIndex "+fromIndex+"toIndex "+ toIndex+ "="+ varSize );
					if(varSize.contains("XIndex")) {
						String firstIndex=varSize.substring(0, varSize.indexOf("*"));
						String dereference= varSize.replaceAll("[^\\*]","");
						//System.out.println("int "+firstIndex +"= (sizeof("+dereference+varName+")/sizeof("+varType+"));" );
						
						//System.out.println(varSize.replaceAll("[^\\*]","") );
//						stb.insert(0, NEWLINE);
//						stb.insert(0,"int "+firstIndex +"= (sizeof("+dereference+varName+")/sizeof("+varType+"));");	
						stbpointers.insert(0,"int "+firstIndex +"= (sizeof("+dereference+varName+")/sizeof("+varType+"));");	
						stbpointers.insert(0, NEWLINE);
						//stb.insert(0, NEWLINE);
						}
					//stb.append("\tif(fwrite(&"+varName +", sizeof("+varType+ "), 1,initialInputStateFile )<1){printf(\"error in writing \'"+ varName +"\' to file\");}" );
					stb.append("write_var_to_file(\""+varName+"\","+varName +", sizeof("+varType+ "), "+varSize+",initialInputStateFile"+expname+" );" );
					stb.append( NEWLINE);
					//System.out.println(stb.toString());
					fromIndex=toIndex+1;
				}
			}
		}

		// indexOf( input, int fromIndex)

		//stb.append("}else{if(verbosity > 0){printf(\"The input set of iteration %d is not written to the file.\\n\",iteration_count);}");
		//"if (iteration_count==iterationNum) {"+NEWLINE+
		//System.out.println(stb.toString());
//		return "initialInputStateFile= fopen(\"initialInputStateFile\",\"wb\");"+NEWLINE+stb.toString();//+"fclose(initialInputStateFile);";
		return stbpointers.toString()+NEWLINE+"if (iteration_count==iterationNum) {"+NEWLINE+
				"gettimeofday(&captureStartInputWrite, NULL);"+NEWLINE+
				"initialInputStateFile"+expname+"= fopen(\"initialInputStateFile"+expname+"\",\"wb\");"+NEWLINE+stb.toString()+
				"gettimeofday(&captureEndInputWrite, NULL);"+NEWLINE+
				"capture_input_write_time = (double) ((captureEndInputWrite.tv_sec * 1000000 + captureEndInputWrite.tv_usec) - (captureStartInputWrite.tv_sec * 1000000 + captureStartInputWrite.tv_usec)) / 1000000;"+NEWLINE+
				"if(verbosity > 0){"+NEWLINE+
				"printf(\"The input set of iteration %d is written to the file.\\n\",iteration_count);"+NEWLINE+
				"printf(\"The capture_input_write_time took %.6lf seconds.\\n\", capture_input_write_time);"+NEWLINE+
				"}"+NEWLINE+
				"}else{if(verbosity > 1){printf(\"The input set of iteration %d is not written to the file.\\n\",iteration_count);}}"+NEWLINE;//+"fclose(initialInputStateFile);";

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

	
	
	private Declaration getLastProcedure(TranslationUnit tu) {
		Declaration ret = null;
		List<Traversable> children = tu.getChildren();
		//for (int i = children.size() - 1; i >= 0; i--) {
			Declaration decl = (Declaration)children.get(children.size() - 1);
//			if (decl instanceof AnnotationDeclaration &&
//					decl.toString().contains("endinclude")) {
//				break;
//			}
			if (decl instanceof Procedure) {
				ret = decl;
			}
		//}
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

	/** Returns the first statement after declaration part */
	private static Statement getFirstStatementAfterDeclaration(CompoundStatement cstmt) {
		Statement ret = null;
		List<Traversable> children = cstmt.getChildren();
		for (int i = children.size() - 1; i >= 0; i--) {
			Statement stmt = (Statement)children.get(i);
			if (stmt instanceof DeclarationStatement) {
				break;
			}
			ret = stmt;
		}
		return ret;
	}
	
	/** Returns the first bracket before declaration part */
	private static Statement getFirstStatementBeforeDeclaration(CompoundStatement cstmt) {
		Statement ret = null;
		List<Traversable> children = cstmt.getChildren();
		for (int i = children.size() - 1; i >= 0; i--) {
			Statement stmt = (Statement)children.get(i);
//			if (stmt instanceof DeclarationStatement) {
//				break;
//			}
			ret = stmt;
		}
		return ret;
	}
//	private CFGraph getLiveVariables(Traversable t, Set<Symbol> mask_set) {
//	    CFGraph g = new CFGraph(t);
//	    g.topologicalSort(g.getNodeWith("stmt", "ENTRY"));
//	    TreeMap<Integer, DFANode> work_list = new TreeMap<Integer, DFANode>();
//	    List<DFANode> exit_nodes = g.getExitNodes();
//	    for (DFANode exit_node : exit_nodes) {
//	        work_list.put((Integer)exit_node.getData("top-order"), exit_node);
//	    }
//	    while (!work_list.isEmpty()) {
//	        DFANode node = work_list.remove(work_list.lastKey());
//	        // LIVEout
//	        Set<Symbol> live_out = new LinkedHashSet<Symbol>();
//	        for (DFANode succ : node.getSuccs()) {
//	            Set<Symbol> succ_in = succ.getData("live-in");
//	            if (succ_in != null) {
//	                live_out.addAll(succ_in);
//	            }
//	        }
//	        // Convergence
//	        Set<Symbol> prev_live_out = node.getData("live-out");
//	        if (prev_live_out != null && prev_live_out.equals(live_out)) {
//	            continue;
//	        }
//	        node.putData("live-out", new LinkedHashSet<Symbol>(live_out));
//	        // Local computation
//	        Set<Symbol> gen = new LinkedHashSet<Symbol>();
//	        Set<Symbol> kill = new LinkedHashSet<Symbol>();
//	        computeLiveVariables(node, gen, kill, mask_set);
//	        // LiveIn = Gen (v) ( LiveOut - Kill )
//	        live_out.removeAll(kill);
//	        live_out.addAll(gen);
//	        // Intersect with the masking set (reduces the size of the live set)
//	        live_out.retainAll(mask_set);
//	        node.putData("live-in", live_out);
//	        for (DFANode pred : node.getPreds()) {
//	            work_list.put((Integer)pred.getData("top-order"), pred);
//	        }
//	    }
//	    return g;
//	}
//
//	/**
//	 * Transfer function for live variable analysis.
//	 */
//	 private void computeLiveVariables(DFANode node,
//	                                   Set<Symbol> gen,
//	                                   Set<Symbol> kill,
//	                                   Set<Symbol> mask_set) {
//	     Object tr = CFGraph.getIR(node);
//	     if (!(tr instanceof Traversable)) { // symbol-entry with initializer?
//	         return;
//	     }
//	     gen.addAll(DataFlowTools.getUseSymbol((Traversable)tr));
//	     kill.addAll(DataFlowTools.getDefSymbol((Traversable)tr));
//	     // Conservative decision on funcion calls; add any variables in the
//	     // mask_set to the GEN set.
//	     if (IRTools.containsClass((Traversable)tr, FunctionCall.class)) {
//	         for (Symbol var : mask_set) {
//	             if (SymbolTools.isGlobal(var, current_loop)) {
//	                 gen.add(var);
//	             }
//	         }
//	     }
//	     // Name only support for access expressions.
//	     addAccessName(gen);
//	     addAccessName(kill);
//	     return;
//	 }
//
//	 
//	
}

