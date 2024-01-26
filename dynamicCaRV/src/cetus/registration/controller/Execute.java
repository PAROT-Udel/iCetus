/**
 * 
 */
package cetus.registration.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;

import javax.swing.JOptionPane;

/**
 * @author 13022
 *
 */
public class Execute {

	public static int numCores = Runtime.getRuntime().availableProcessors(), numThreads = -1; //number of cores on this server is 4, number of threads for each core is 1
	public static File inputExeFile = null, openMpExeFile = null;
	public static int newInput = 0, newOpenMP = 0;
	public static double seqRunTime = -1.0, parRunTime = -1.0, speedup = -1.0, efficiency = -1.0;
	public static String ext = ".out";
	public static File inputFile = null //= new File(CetusGUITools.user_dir+CetusGUITools.file_sep+"foo.c")
			, defaultOpenMpFile = null, openMpFile = null;
	public static int op = (System.getProperty("os.name").toLowerCase()).indexOf("win");

	
	
	
	/*
	 * Compiles c code mode=0 to compile serial code mode=1 to compile openmp code
	 */
	public File compileCFile(File cFile, int mode) {
		int exitCode = 0;
		String errorMessage=null;
		
			if (cFile != null) {
				String compCommand = "";
				String compCode="";
				String compiler = "gcc";
				String openmpFlag = "-fopenmp";
		    	if (mode == 0) {
					compCommand = compiler + " -o ";
				} else if (mode == 1) {
					compCommand = compiler + " " + openmpFlag + " -o ";
				} else {
					return null;
				}

				System.out.println("\nCompiling C file: " + cFile + "\n");

				int cExtIndex = cFile.toString().toLowerCase().lastIndexOf(".");
				String cMain = cFile.toString().substring(0, cExtIndex);
				String cExe = cMain + ext;
				File cExeFile = new File(cExe);
				if (cExeFile.exists()) {
					cExeFile.delete();
					System.out.println("Deleted previous C executable: " + cExeFile + "\n");
				}
				//when compiling with gcc, using 2>&1 in the command ensures that both stdout and stderr are captured in the same stream
				compCode = compCommand + cExe + " " + cFile.toString()+" -lm ";
				System.out.println(compCode);

				// Runtime.getRuntime().exec(compCode).waitFor();
				try {
				Process p = Runtime.getRuntime().exec(compCode);
				               
				// Print compilation errors (if any)
				StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
				errorGobbler.start();


				exitCode = p.waitFor();
				if (exitCode != 0) {
	                throw new Exception("Compilation failed with exitcode" + exitCode);
	            }
	        } catch (Exception ex) {
	        	System.out.println("Compile Error Message: " + ex.toString());
	        	errorMessage= ex.toString();
	            //ex.printStackTrace();
	            //System.out.println(ex.getMessage());
	        }

				if (cExeFile.exists()) {
					System.out.println("\nCompiling C code was successful!\n");
					return cExeFile;
				} else {
					System.out.println("\nCompiling C code failed with exit code" + exitCode + " and Error Message "+ errorMessage + "!\n");
				}

			} else {
				String msg = "\nNo C file! Compiling C code failed!";
				System.out.println(msg);
			}


		return null;
	}


//	private void printErrors(InputStream errorStream) throws IOException {
//        try (BufferedReader br = new BufferedReader(new InputStreamReader(errorStream))) {
//            String line;
//            System.out.println("\nCompilation Errors:");
//
//            while ((line = br.readLine()) != null) {
//                System.out.println(line);
//            }
//        }
//	}
	/*
	 * Runs c code numThreads=0 to run serial code numThreads>0 to run openmp code- 
	 */
	/**
	 * @param File, gets the file, serial or parallel
	 * @param numThreads, set to 0 for serial code, and set to more than 0 for parallel execution
	 * @return  double, shows the duration of the execution of the program
	 */
	public double runExe(File cExeFile, int numThreads) {
		String directoryPath = cExeFile.getParent();
		try {
			if (cExeFile != null) {

				String[] runExe;

				if (numThreads == 0) {
					System.out.println("\nRunning C executable in sequential mode: " + cExeFile + "\n");
					runExe = new String[] { cExeFile.toString() };
					System.out.println("Serial Exec command" + Arrays.toString(runExe));
				} else if (numThreads > 0) {
					System.out.println("\nRunning C executable in parallel mode: " + cExeFile);
					System.out.println("\n" + System.getProperty("os.name") + ", using " + numThreads + " threads\n");
					//in linux
					if (op<0) {
						runExe = new String[]{"/bin/sh","-c",
								"OMP_NUM_THREADS="+numThreads+" "+cExeFile.toString()}; 
						System.out.println("Linux Exec Command" + Arrays.toString(runExe));
					} else {
						runExe = new String[]{cExeFile.toString()};	
						System.out.println("windows Exec Command" + Arrays.toString(runExe));
					}			
				} else {
					System.out.println("\nWrong number of threads!");
					return -1.0;
				}

				//System.out.println(CetusGUITools.convertArrayStringsToStringLines(runExe));

				long startTime = System.currentTimeMillis(); // .nanoTime();
//				Process p = null;
//				ThreadSpeedupProcess pro = new ThreadSpeedupProcess(
//						CetusGUITools.convertArrayStringsToStringLines(runExe), p, 1, this);
//				pro.start();
				//Process p = Runtime.getRuntime().exec(runExe);
				
				File directory = new File(directoryPath);
				System.out.println("Working Directory of the process: " + directory.getAbsolutePath());

				ProcessBuilder processBuilder = new ProcessBuilder(runExe);
				//setting the working directory of the process
				processBuilder.directory(directory);
				
				processBuilder.redirectOutput(new File(directory+"runExeoutput.txt"));
				processBuilder.redirectError(new File(directory+"runExeerror.txt"));
				Process p = processBuilder.start();
				//========================================Java Runtime.getRuntime(): getting output from executing a command line program==prints the output the command line========print it for the user as a query
				/*
				 * BufferedReader stdInput = new BufferedReader(new
				 * InputStreamReader(p.getInputStream()));
				 * 
				 * BufferedReader stdError = new BufferedReader(new
				 * InputStreamReader(p.getErrorStream()));
				 * 
				 * // Read the output from the command
				 * System.out.println("Standard output of the command:\n"); String s = null;
				 * while ((s = stdInput.readLine()) != null) { System.out.println(s); }
				 * 
				 * // Read any errors from the attempted command
				 * System.out.println("Standard error of the command (if any):\n"); while ((s =
				 * stdError.readLine()) != null) { System.out.println(s); }
				 * 
				 */
				
				//======================================
//				Thread t = new ThreadSpeedup(CetusGUITools.convertArrayStringsToStringLines(runExe), p, 1, this);
//				t.start();
					//waitfor causes the current thread to wait, if necessary, until the process represented by this Process object has terminated. 
					//This method returns immediately if the subprocess has already terminated. If the subprocess has not yet terminated, 
					//the calling thread will be blocked until the subprocess exits.
				p.waitFor();
				long endTime = System.currentTimeMillis();
				
		         // wait for 10 seconds and then destroy the process
					/*
					 * Thread.sleep(10000); 
					 * p.destroy();
					 */
				
//				t.interrupt();
				//String[] processMsgs = CetusGUITools.getProcessMessages(p);
				//String processMsgsString = CetusGUITools.convertArrayStringsToStringLines(processMsgs);
				//System.out.println("\n" + processMsgsString);
				double duration = (endTime - startTime) / 1000.0;
				System.out.println("\nRunning time = " + String.format("%1$,.3f", duration) + " (seconds)");
				return duration;

			} else {

				String msg = "\nExecutable file does not exist! Please compile C code first!";
				System.out.println(msg);
				return -2.0;
			}

		} catch (Exception ep) {
			System.out.println("\nRunning C executable failed\n");
			System.out.println("\n" + ep);
		}

		return -3.0;
	}
	
	/**
	 * @param File, gets the file, serial or parallel
	 * @param numThreads, set to 0 for serial code, and set to more than 0 for parallel execution
	 * @return  String, shows the execution result of the program
	 */
	//C:\Users\13022\Desktop\VM_share\apache-tomcat-9.0.41\apache-tomcat-9.0.41\wtpwebapps\dynamicCaRV\modifiedcode.out
	public String executionResult(File cExeFile, int numThreads) {
		StringBuilder execResult= new StringBuilder("");
		try {
			if (cExeFile != null) {

				String[] runExe;
				// Extract the directory path from cExeFile
				String directoryPath = cExeFile.getParent();
				if (numThreads == 0) {
					System.out.println("\nRunning C executable in sequential mode: " + cExeFile + "\n");
					runExe = new String[] { cExeFile.toString() };
				} else if (numThreads > 0) {
					System.out.println("\nRunning C executable in parallel mode: " + cExeFile);
					System.out.println("\n" + System.getProperty("os.name") + ", using " + numThreads + " threads\n");
					//in linux
					if (op<0) {
						runExe = new String[]{"/bin/sh","-c",
								"OMP_NUM_THREADS="+numThreads+" "+cExeFile.toString()}; 
					} else { //in windows- how to set the number of threads?
						runExe = new String[]{cExeFile.toString()};	
					}			
				} else {
					
					String message = "\nWrong number of threads!";
					System.out.println(message);
					return message;
				}

				//System.out.println(CetusGUITools.convertArrayStringsToStringLines(runExe));

				//long startTime = System.currentTimeMillis(); // .nanoTime();
//				Process p = null;
//				ThreadSpeedupProcess pro = new ThreadSpeedupProcess(
//						CetusGUITools.convertArrayStringsToStringLines(runExe), p, 1, this);
//				pro.start();
				//Process p = Runtime.getRuntime().exec(runExe);
				File directory = new File(directoryPath);
				System.out.println("Working Directory of the process: " + directory.getAbsolutePath());

				ProcessBuilder processBuilder = new ProcessBuilder(runExe);
				//setting the working directory of the process
				processBuilder.directory(directory);
				//processBuilder.redirectOutput(new File(directory+" executionResultoutput.txt"));
				//processBuilder.redirectError(new File(directory+" executionResulterror.txt"));
				Process p = processBuilder.start();
				
				//========================================Java Runtime.getRuntime(): getting output from executing a command line program==prints the output the command line========print it for the user as a query
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
							/*
							 * for (int k = 0; k < ((CharSequence) stdInput).length(); ++k)
							 * System.out.println("Input stream = " + stdInput.read());
							 */

					BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
					
//					BufferedReader stdOutput = new BufferedReader(new 
//						     InputStreamReader(p.getOutputStream()));
			         // get the output stream
			        // OutputStream out = p.getOutputStream();

					// Read the output from the command
					//System.out.println("[ExecutionResult] Standard output of the command:\n");
					execResult.append("\n[ExecutionResult] Standard output of the program:\n\n");
					String s = null;
					while ((s = stdInput.readLine()) != null) {
					    System.out.println(s);
					    execResult.append(s).append("\n");
					}

					// Read any errors from the attempted command
					execResult.append("\n[ExecutionResult] Standard error of the program (if any):\n\n");
					//System.out.println("[ExecutionResult] Standard error of the command (if any):\n");
					while ((s = stdError.readLine()) != null) {
					    System.out.println(s);
					    execResult.append(s).append("\n");
					}
				

				
				//======================================
//				Thread t = new ThreadSpeedup(CetusGUITools.convertArrayStringsToStringLines(runExe), p, 1, this);
//				t.start();
					//waitfor causes the current thread to wait, if necessary, until the process represented by this Process object has terminated. 
					//This method returns immediately if the subprocess has already terminated. If the subprocess has not yet terminated, 
					//the calling thread will be blocked until the subprocess exits.
				p.waitFor();
				//long endTime = System.currentTimeMillis();
				
		         // wait for 10 seconds and then destroy the process
					/*
					 * Thread.sleep(10000); 
					 * p.destroy();
					 */
				
//				t.interrupt();
				//String[] processMsgs = CetusGUITools.getProcessMessages(p);
				//String processMsgsString = CetusGUITools.convertArrayStringsToStringLines(processMsgs);
				//System.out.println("\n" + processMsgsString);
			//	double duration = (endTime - startTime) / 1000.0;
			//	System.out.println("\nRunning time = " + String.format("%1$,.3f", duration) + " (seconds)");
				return execResult.toString();

			} else {

				String msg = "\nExecutable file does not exist! Please compile C code first!";
				System.out.println(msg);
				return msg;
			}

		} catch (Exception ep) {
			System.out.println("\nRunning C executable failed\n");
			System.out.println("\n" + ep);
		}
		String mes= "Running C executable failed\n";
		return mes;
	}
	
	public String calculateSpeedup(double seqRunTime, double parRunTime, int numThreads ) {
		System.out.println("\n"+"Calculating speedup and efficiency...");
		if (seqRunTime > 0 && parRunTime > 0 && numThreads >= 0) {
			speedup = seqRunTime/parRunTime;
			efficiency = speedup/numThreads;
			String report= "[Execution]"
			+ "\n Sequential code running time in seconds = " + String.format("%1$,.3f", seqRunTime)
			+ "\n Number of threads used = " + numThreads
			+ "\n Parallel code running time in seconds = " + String.format("%1$,.3f",parRunTime)
			+ "\n Speedup (sequential RunTime/parrallel RunTime)= " + String.format("%1$,.3f", speedup)
			+ "\n Efficiency (speed up/ number of Threads) = " + String.format("%1$,.3f", efficiency)+"\n";
			System.out.println(
				"\nSequential running time in seconds = " + String.format("%1$,.3f", seqRunTime)
				+ "\nParallel running time in seconds = " + parRunTime
				+ "\nNumber of threads used = " + numThreads
				+ "\nSpeedup = " + String.format("%1$,.3f", speedup)
				+ "\nEfficiency = " + String.format("%1$,.3f", efficiency)+"\n");
			return report;
		}else if ( parRunTime > 0 && numThreads >= 0) {
			//speedup = seqRunTime/parRunTime;
			//efficiency = speedup/numThreads;
			String report= "[Execution]"
			+ "\n Number of threads used = " + numThreads
			+ "\n Parallel code running time in seconds = " + String.format("%1$,.3f",parRunTime);

			System.out.println(
				"\nParallel running time in seconds = " + parRunTime
				+ "\nNumber of threads used = " + numThreads);
			return report;
		} else {
			String msg = "\nCalculating speedup and efficiency failed!";
			System.out.println(msg);
			return msg;
		}
	}


}
