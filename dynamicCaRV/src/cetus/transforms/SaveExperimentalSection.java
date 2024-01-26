package cetus.transforms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;

import cetus.analysis.LoopTools;
import cetus.hir.*;

public class SaveExperimentalSection extends TransformPass {

	private String inputVariables;
	
	public SaveExperimentalSection(Program program) {
		super(program);
	}
	
	public SaveExperimentalSection( Program program, String inputVariables) {
		super(program);
		this.inputVariables = inputVariables;
	}

	@Override
	public String getPassName() {
		// TODO Auto-generated method stub
		return "[SaveExperimentalSection]";
	}
	
    // Serialization
    // Save object into a file.
//    public static void writeObjectToFile(Program obj, File file) throws IOException {
//        try (FileOutputStream fos = new FileOutputStream(file);
//             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
//            oos.writeObject(obj);
//            oos.flush();
//        }
//    }
    
    // Deserialization
    // Get object from a file.
//    public static Program readObjectFromFile(File file) throws IOException, ClassNotFoundException {
//        Program result = null;
//        try (FileInputStream fis = new FileInputStream(file);
//             ObjectInputStream ois = new ObjectInputStream(fis)) {
//            result = (Program) ois.readObject();
//        }
//        return result;
//    }
	@Override
	public void start() {

		//program is saved so to create the experimental file from it
//		File file = new File("resources/testcases/output/program.bin");
//		//write the program obj to the file
//        try {
//			writeObjectToFile(program, file);
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//        System.out.println(clonedprogram);
//		Program clonedprogram = null;
//		Cloner cloner=new Cloner();
//		Object clonedprogram = cloner.deepClone(program);
//		try {
			//ccprogram = (Program) this.program.clone();
//			cloneprogram= deepClone(program);
//		} catch (CloneNotSupportedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		//print to a separate file
//		PrintStream out = null;
//		try {
//			//FileOutputStream
//			out = new PrintStream(new File("resources/testcases/output/InitialExperimentalCode.c"));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		// Store current System.out
        // before assigning a new value
//        PrintStream console = System.out;
		//identify Input Variables and output variables
		//save input variables to InitialInputStateFile
		//add timer around the experimental section
		//save the execution time of the experimental code section to InitialExperimentalogramSectionRunTime
		//save output variables to InitialOutputStateFile
		TransformPass.run(new ExperimentalTimer(program));
		
		       
        // Assign out to output stream
        // using setOut() method
//        System.setOut(out);
//        System.out.println("//compile, and execute this file before making changes to the experimental section");
//        System.out.println(program);
		 // Use stored value for output stream
//        System.setOut(console);
       // Display message only
//        System.out.println("This will be written on the console!");
		
//        Program clonedprogram=null;
//        //read the program object from the file
//		try {
//			clonedprogram = readObjectFromFile(file);
//		} catch (ClassNotFoundException e1) {
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
		
		//print to a separate file
		//PrintStream out = null;
//		try {
//			//FileOutputStream
//			out = new PrintStream(new File("resources/testcases/output/ChangeExperimentalCode.c"));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		// Store current System.out
        // before assigning a new value
//        console = System.out;
        
        // Assign out to output stream
        // using setOut() method
//        System.setOut(out);
//        System.out.println("//Following the execution of the original file, modify the Experimental Section in this file, compile it, and execute it.");
//        System.out.println(program);
		//insert all libraries
		//read input variables from the file
		//start the timer
		//experimental section
		//stop the timer,and save the timer
		//save output variables to a file
		//compare the time and output variables
		//TransformPass.run(new ChangeExperimentalCode(program));
		
		 // Use stored value for output stream
//      System.setOut(console);
//     // Display message only
//      System.out.println("This will be written on the console!");

		


	}

//	public static Program deepClone(Program object){
//		  try {
//		        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//		        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
//		        objectOutputStream.writeObject(object);
//		        ByteArrayInputStream bais = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
//		        ObjectInputStream objectInputStream = new ObjectInputStream(bais);
//		          return (Program) objectInputStream.readObject();
//		    }
//		    catch (Exception e) {
//		      e.printStackTrace();
//		      return null;
//		    }
//		  }

}
