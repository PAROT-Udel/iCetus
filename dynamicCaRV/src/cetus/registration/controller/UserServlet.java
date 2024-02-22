package cetus.registration.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import cetus.exec.Driver;
import cetus.registration.dao.CarvreplayDAO;
import cetus.registration.dao.UserDAO;
import cetus.registration.dao.CetusDAO;
import cetus.registration.model.Carvreplay;
import cetus.registration.model.User;
import cetus.registration.controller.Execute;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

/**
 * Servlet implementation class UserServlet
 */
//@WebServlet("/UserServlet")
//register
@WebServlet("/")
//@MultipartConfig(location = "C:/Users/13022/Desktop/CExamples/")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB file�s size that is greater than this threshold will be
// directly written to disk, instead of saving in memory.
		maxFileSize = 1024 * 1024 * 10, // maximum size for a single upload file.
		maxRequestSize = 1024 * 1024 * 50) // 50MB maximum size for a request. All sizes are measured in bytes.

/* @MultipartConfig */
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO;
	private Execute execute;
	private CetusDAO cetusDAO;

	public void init() {
		userDAO = new UserDAO();
		execute = new Execute();
		cetusDAO= new CetusDAO();
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	// public void init(ServletConfig config) throws ServletException {
	// // TODO Auto-generated method stub
	// }

	/**
	 * @see Servlet#getServletInfo()
	 */
	// public String getServletInfo() {
	// // TODO Auto-generated method stub
	// return null;
	// }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	// protected void service(HttpServletRequest request, HttpServletResponse
	// response) throws ServletException, IOException {
	// // TODO Auto-generated method stub
	// }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Testing DB connection
		// String contextPath = getServletContext().getRealPath("/");
		// System.out.println(contextPath);
		// String FilePathuser=contextPath+"usercode.c";
		// String FilePathex=contextPath+"examplecode.c";
		//
		// System.out.println(FilePathuser);
		// System.out.println(FilePathex);
		//
		// File userfile = new File(FilePathuser);
		// File exfile = new File(FilePathex);
		//
		// userfile.createNewFile();
		// exfile.createNewFile();
		// System.out.println("shows this content in console");
		// try {
		// Insert request values to DB then run Cetus on the code
		// and save the output in DB and show the result to the user
		// userDAO.getConnection();
		// User user = new User();
		// userDAO.getConnection();
		// response.getWriter().println("connected to DB");
		// userDAO.insertUserRequest(user);
		// response.getWriter().println("inserted to DB");
		// } catch (Exception e) {
		// System.out.println("UserServlet connection to DB problem");
		// response.getWriter().println("DB connection or insert issue");
		// e.printStackTrace();
		// }
		// response.getWriter().println("<p>Show me if the Mysql Connection has been
		// made</p>");
		// response.getWriter().println("<p>Show me if Cetus runs without any problem
		// just by passing the input file to it?</p>");
		
		response.getWriter().append("served at: ").append(request.getContextPath());
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/index.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("static-access")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * Based on the received request initialize the user object and store it in DB,
		 * translate the input file with Cetus, Save the results in DB, and redirect the
		 * user to the output page.
		 */
		File inputFile = null, openMpFile = null;

		// user input file- worked on local application server, the remote tomcat reads
		// the input file by analyzing the request
		// String inputCode = request.getParameter("inputCode");
		String RedirectPage = null;

		/*
		 * Getting required info to create the paths that files and folders should be
		 * written to.
		 */
		///dynamicCaRV
		String pathWebcontent = request.getContextPath();
		// System.out.println("UserServlet- pathWebcontent is :\n" + pathWebcontent);

		// Get the address of the project (for example: http://localhost:8080/MyApp/)
		// and assign it to the basePath variable.
		//http://localhost:8080/dynamicCaRV/
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ pathWebcontent + "/";
		// System.out.println("UserServlet- basePath is :\n" + basePath);

		// pageContext.setAttribute("basePath", basePath); 
		//C:\Users\13022\Desktop\VM_share\apache-tomcat-9.0.41\apache-tomcat-9.0.41\wtpwebapps\dynamicCaRV\
		String contextPath = getServletContext().getRealPath("/");
		// System.out.println(contextPath);

		/*
		 * inputPrg stores whatever file address/path that should be passed to Cetus. it
		 * can the path to the file uploaded, the path to the code written by the user,
		 * or the example files passed to Cetus.
		 */
		String inputPrg = null;

		/*
		 * checks the request received to find all filenames and put them in the list.
		 * Getting the passed parameters from the request:
		 */
		//checks to see if the parameter has been set? It comes from output
		String exec= request.getParameter("executeOnly");
		
		String dbRowIdParameter= request.getParameter("DBRowId"); //introduced from Carv.jsp
		//int dbRowId = Integer.parseInt(dbRowIdParameter);
		//System.out.println("DB row id is : "+ dbRowId);
		if (dbRowIdParameter != null && !dbRowIdParameter.isEmpty()) {
		    try {
		        int dbRowId = Integer.parseInt(dbRowIdParameter);
		        System.out.println("DB row id is : "+ dbRowId); //DB row id. we use it to save the related info in the same id
		    } catch (NumberFormatException e) {
		        // Handle the case where the parameter is not a valid integer.
		        // You may want to provide a default value or show an error message.
		        e.printStackTrace(); // This line is optional; you can choose how to handle the exception.
		    }}
		
		String carvPhase= request.getParameter("CaRVPhase"); //This parameters can be set to Capture or Replay
		String expsection= request.getParameter("expsectionpara")+ "\n";//passing experimental section for saving in Carv Replay DB
	    System.out.println("UserServlet- expsection passed is:\n" + expsection);
		// String inputCode = request.getParameter("inputCode") + "\n";
		// write your own code
		String inputProgramCode = request.getParameter("usercode") + "\n";
		System.out.println("UserServlet- User code is:\n" + inputProgramCode);
		// our examples
		String inputExampleCode = request.getParameter("examplecode") + "\n";
		// System.out.println("UserServlet- example code is:\n" + inputExampleCode);

//		 based on the radio button chosen the correct path would be set in this var.
		String codeRadios = request.getParameter("codeRadios");
		System.out.println("UserServlet- codeRadios:\n" + codeRadios);

		if (codeRadios.equals("file")) {
			String compileType = request.getParameter("action");
			if(compileType.equals("NewCompile")) {
				inputPrg = getServletContext().getRealPath("/")+"modifiedcode.c";
			}else {
			String uploadPath = contextPath;
			String fileName1 = "";
			UploadDetail details = null;
			List<UploadDetail> fileList = new ArrayList<UploadDetail>();

			for (Part part : request.getParts()) { // returns a collection of Part objects
				fileName1 = extractFileName(part);
				System.out.println(fileName1);
				/* inputPrg = contextPath + fileName1; */
				details = new UploadDetail();
				details.setFileName(fileName1);
				long size = part.getSize() / 1024;
				details.setFileSize(size); // getSize() returns the size of upload data, in bytes.
				// refines the fileName in case it is an absolute path
				fileName1 = new File(fileName1).getName();
				if (fileName1 != null || fileName1 != "") {
					try {
						// part.write(uploadPath + File.separator + fileName1);
						part.write(uploadPath + fileName1);

						// part.write(uploadPath + File.separator + "SimpleLoop.c");
						// System.out.println(uploadPath + File.separator + "SimpleLoop.c");
						details.setUploadStatus("Success");

						// passing the input file to cetus
						if (fileName1 != null || fileName1 != "") {
							// inputPrg +=  contextPath + fileName1 +" ";
							inputPrg = contextPath + fileName1;
						}
					} catch (IOException ioObj) {
						details.setUploadStatus("Failure : " + ioObj.getMessage());
					}
					fileList.add(details);
				}
			}
			request.setAttribute("uploadedFiles", fileList);
			//remove comments and empty lines from the file (whole path to the file) returns nothing,writes back to the file.
			removeCommentsEmptyLines(inputPrg);
			}

			// else handle this situation
			// inputPrg = contextPath + fileName1;
		} else if (codeRadios.equals("code")) {
			if (inputProgramCode != null && !inputProgramCode.trim().isEmpty()) {
				String FilePathuser = contextPath + "usercode.c";
				File userfile = new File(FilePathuser);
				FileOutputStream fos = new FileOutputStream(userfile);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
				//userfile.createNewFile();
				//FileWriter writer = new FileWriter(userfile);
				//saved line by line
				inputProgramCode=removeCommentsEmptyLinesfromContent(inputProgramCode);
				System.out.println("\n\n inputProgramCode"+inputProgramCode);
				bw.write(inputProgramCode+"\n");
				//bw.newLine();
				bw.close();
				
				/*
				 * writer.write(inputProgramCode+"\n"); writer.flush(); writer.close();
				 */
				inputPrg = FilePathuser;
			}
			// else handle this situation
		} else if(codeRadios.equals("passedFile")) {  //if file is passed we dont want to show it's content to the user. the user already knows what is in it
			if (inputProgramCode != null && !inputProgramCode.trim().isEmpty()) {
			String FilePathuser = contextPath + "modifiedcode.c";
			//create a file object
			File modifiedfile = new File(FilePathuser);
		    // Check if the file exists
	        if (modifiedfile.exists()) {
	            // Delete the existing file
	            if (modifiedfile.delete()) {
	                System.out.println("Existing file deleted successfully.");
	            } else {
	                System.err.println("Unable to delete existing file.");
	                return; // Exit the program if unable to delete the file
	            }
	        }
			FileOutputStream fos = new FileOutputStream(modifiedfile);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
	
			inputProgramCode=removeCommentsEmptyLinesfromContent(inputProgramCode);
			System.out.println("\n\n inputProgramCode"+inputProgramCode);
			bw.write(inputProgramCode+" \n");
			//bw.newLine();
			bw.close();
			inputPrg = FilePathuser;
			}
		}else if (codeRadios.equals("example")) {
			if (inputExampleCode != null && !inputExampleCode.trim().isEmpty()) {
				String FilePathex = contextPath + "examplecode.c";
				File exfile = new File(FilePathex);
				exfile.createNewFile();
				FileWriter writer = new FileWriter(exfile);
				//replaces all comments before sending it to Cetus- Cetus messes with comments
				inputExampleCode=removeCommentsEmptyLinesfromContent(inputExampleCode);
				writer.write(inputExampleCode);
				writer.flush();
				writer.close();
				inputPrg = FilePathex;
			}
			// else handle this situation
		}

		String[] cetusOptionSet = new String[20];
		// To add user selections in case they are not null to the args list
		StringBuilder str = new StringBuilder();

		// Run Cetus automatically or with customized options?
		String gridRadios = request.getParameter("gridRadios");
		System.out.println("UserServlet- gridRadios, Parallelization Type is " + gridRadios);
		// Setting Cetus Options,Storing all passed parameters by the request
		// Default settings for iCetus based on the server settings
		// cetusOptionSet[0] = "-preprocessor=cpp.exe"; // for windows
		
		cetusOptionSet[0] = "-preprocessor=cpp -C -I."; // for linux and Mac
		cetusOptionSet[1] = "-verbosity=2";
		String ddt = request.getParameter("ddt");
		cetusOptionSet[2] = ddt;
		String range = request.getParameter("range");
		cetusOptionSet[3] = range;
		String alias = request.getParameter("alias");
		cetusOptionSet[4] = alias;
		String ploops = request.getParameter("ploops");
		cetusOptionSet[5] = ploops;
		String privatize = request.getParameter("privatize");
		cetusOptionSet[6] = privatize;
		String reduction = request.getParameter("reduction");
		cetusOptionSet[7] = reduction;
		String induction = request.getParameter("induction");
		cetusOptionSet[8] = induction;
		String profiling = request.getParameter("profiling");
		cetusOptionSet[9] = profiling;
		String profiler = request.getParameter("profiler");
		if (profiler==null ){
			cetusOptionSet[9] = profiling;
		} else if (profiler!= null && profiler.equals("serialprofiler")) {
			cetusOptionSet[9] = "-profile-loops=1";
		}else {cetusOptionSet[9] = "";}

		String eliminateBranch = request.getParameter("eliminateBranch");
		cetusOptionSet[10] = eliminateBranch;

		String profitableOmp = request.getParameter("profitableOmp");
		cetusOptionSet[11] = profitableOmp;

		String callgraph = request.getParameter("checkbox_callgraph");
		cetusOptionSet[12] = callgraph;

		String normalize_loops = request.getParameter("checkbox_normalize_loops");
		cetusOptionSet[13] = normalize_loops;

		String normalize_rtn_stmt = request.getParameter("checkbox_normalize_rtn_stmt");
		cetusOptionSet[14] = normalize_rtn_stmt;

		String tsingle_call = request.getParameter("checkbox_tsingle_call");
		cetusOptionSet[15] = tsingle_call;

		String tsingle_declarator = request.getParameter("checkbox_tsingle_declarator");
		cetusOptionSet[16] = tsingle_declarator;

		String tsingle_rtn = request.getParameter("checkbox_tsingle_rtn");
		cetusOptionSet[17] = tsingle_rtn;
		
		String codeExecuter= request.getParameter("codeExecuter");
		String paracodeExecuter= request.getParameter("paracodeExecuter");
		
		// if the optins we got from the user are not null or empty then save them
		for (int i = 0; i < 20; i++) {
			if (cetusOptionSet[i] != null && !cetusOptionSet[i].trim().isEmpty()) {
				System.out.println(cetusOptionSet[i]);
				str.append(cetusOptionSet[i] + ", ");
			}
		}
		//https://www.youtube.com/watch?v=TkJ2dFtD0ho
		if(gridRadios.equals("AskGPT")) {
			String expSection = request.getParameter("expsectionpara");
			// Split the expSection string into lines
			//StringBuilder concatenatedLines = 
			// Convert the StringBuilder to a single string
			String expSectionresult = textToString(expSection);//concatenatedLines.toString().trim(); // Trim to remove trailing space
		    String liveInData = request.getParameter("liveinpara");
		    String liveOutData = request.getParameter("liveoutpara");
		    String message = "Optimize this C code section using OpenMP. Return only the optimized code without any explanation. " + expSectionresult+
		    		"These are live-in variable in the code section type:name:size"+textToString(liveInData)+ 
		    		"These are live-out variables from the code section that their values should not change because of optimization: "+textToString(liveOutData);
		    String gptFinalResponse="";
		    String url = "https://api.openai.com/v1/chat/completions";
	        String apiKey = "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"; // API key goes here
	        String model = "gpt-3.5-turbo"; // current model of chatgpt api
	     // Measure the time before sending the request
	        long startTime = System.currentTimeMillis();
	        try {
	            // Create the HTTP POST request
	            URL obj = new URL(url);
	            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	            con.setRequestMethod("POST");
	            con.setRequestProperty("Authorization", "Bearer " + apiKey);
	            con.setRequestProperty("Content-Type", "application/json");

	            // Build the request body
	            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}]}";
	            con.setDoOutput(true);
	            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
	            writer.write(body);
	            writer.flush();
	            writer.close();

	            // Get the response
	            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	            String inputLine;
	            StringBuffer gptresponse = new StringBuffer();
	            while ((inputLine = in.readLine()) != null) {
	            	gptresponse.append(inputLine);
	            }
	            in.close();

	        // returns the extracted contents of the response.
	           gptFinalResponse= extractContentFromResponse(gptresponse.toString());
		    // Measure the time after receiving the response
		       long endTime = System.currentTimeMillis();
	        // Calculate the elapsed time
	           long elapsedTime = endTime - startTime;
	        // Convert elapsed time to seconds
	           double elapsedTimeInSeconds = elapsedTime / 1000.0;
	           System.out.println( gptFinalResponse);
	           System.out.printf( "gpt request took %.2f (s) to be responded",elapsedTimeInSeconds );
	           request.setAttribute("ResponseTime", elapsedTimeInSeconds);
	           request.setAttribute("gptResponse", gptFinalResponse);

	        } catch (IOException e) {
	            throw new RuntimeException(e);
	        }
	    


			//Send Request to GPT API
//		    String gptUrl = "https://api.openai.com/v1/chat/completions";
//		    String apiKey = "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";//"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"; //API added https://platform.openai.com/api-keys
//		    String requestBody = "{ \"model\": \"gpt-3.5-turbo\", \"prompt\": \"" + message + "\" }";
//		    System.out.println("Request Body: " + requestBody);
//		    String apiUrl = gptUrl;
//		    String connectionmessage= "test";
//		    try {
//	            URL url = new URL(apiUrl);
//	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//	            connection.setRequestMethod("GET");
//
//	            int responseCode = connection.getResponseCode();
//	            if (responseCode == HttpURLConnection.HTTP_OK) {
//	                System.out.println("API endpoint is operational (HTTP Status 200 OK)");
//	                connectionmessage= "API endpoint is operational (HTTP Status 200 OK)";
//	            } else {
//	                System.out.println("API endpoint is not operational (HTTP Status " + responseCode + ")");
//	                connectionmessage= "API endpoint is not operational (HTTP Status " + responseCode + ")";
//	            }
//	        } catch (IOException e) {
//	            System.out.println("Failed to access the API endpoint: " + e.getMessage());
//	            connectionmessage= "Failed to access the API endpoint: " + e.getMessage();
//	        }
//		    
//		   try { 
//		    HttpURLConnection connection = (HttpURLConnection) new URL(gptUrl).openConnection();
//		    connection.setRequestMethod("POST");
//		    connection.setRequestProperty("Authorization", "Bearer " + apiKey);
//		    connection.setRequestProperty("Content-Type", "application/json");
//		    connection.setDoOutput(true);
////sending the JSON-formatted request body to the server over the HTTP connection established earlier. 
//		    try (OutputStream os = connection.getOutputStream()) {
//		        byte[] input = requestBody.getBytes("utf-8");
//		        os.write(input, 0, input.length);
//		    }
//		 // Code to handle response from GPT API
//		    //reads the response from the server line by line and appends
//		    try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
//		        StringBuilder responseBuilder = new StringBuilder();
//		        String responseLine = null;
//		        while ((responseLine = br.readLine()) != null) {
//		            responseBuilder.append(responseLine.trim());
//		        }
//		        String gptResponse = responseBuilder.toString();
//			    System.out.println("Response from OpenAI API: " + gptResponse);
//			    request.setAttribute("gptResponse", connectionmessage +gptResponse);
//		        //br.close();
//		    }
//		  // Code to extract optimized code from response
//		    }catch (IOException e) {
//		        System.out.println("Error reading response from OpenAI API: " + e.getMessage());
//		        e.printStackTrace(); // Log stack trace for debugging
//		    }
		   RedirectPage = "/WEB-INF/views/carv.jsp";
		    
		}
		
		

		System.out.println("UserServlet- input programs " + inputPrg);
		
		// "-outdir=C:\\Users\\13022\\Desktop\\VM_share\\apache-tomcat-9.0.41\\apache-tomcat-9.0.41\\webapps\\data\\cetus_output\\";
		// Setting Cetus options that will be passed to Cetus
		String output = "-outdir=" + contextPath + "cetus_output";


		// setting the user instance 
		User user = new User();
		//setting input file path
		System.out.println("Setting input file path");
		user.setInputCode(inputPrg);
		
		//returns the path
		Path codeName = Path.of(inputPrg);
		//reads file content to a string to be saved in DB
		System.out.println("Reading input file content from file path");
		String userCode1 = Files.readString(codeName);
		//System.out.println("code is read");
	
		user.setInputContent(userCode1);
		
		// "C:\\Users\\13022\\Desktop\\VM_share\\apache-tomcat-9.0.41\\apache-tomcat-9.0.41\\webapps\\data\\cetus_output\\";
		// Setting the directory where Cetus-output is stored
		System.out.println("Setting output file path");
		String outputFolder = contextPath + "cetus_output";
		File theDir = new File(outputFolder);
		if (!theDir.exists()) {
			theDir.mkdirs();
		}

		// Setting the directory to save info related to Cetus passes
		System.out.println("Setting debugger file path");
		String cetusDebuggerReport = contextPath + "cetusDebuggerReport";
		File theDebuggerDir = new File(cetusDebuggerReport);
		if (!theDebuggerDir.exists()) {
			theDebuggerDir.mkdirs();
		}
		// String cetusAnalysisReport =
		// "C:\\Users\\13022\\Desktop\\VM_share\\apache-tomcat-9.0.41\\apache-tomcat-9.0.41\\webapps\\data\\cetusAnalysisReport\\";
		// Setting the directory to dump Cetus messages to it
		System.out.println("Setting Analyses file path");
		String cetusAnalysisReport = contextPath + "cetusAnalysisReport";
		File theAnalysisDir = new File(cetusAnalysisReport);
		if (!theAnalysisDir.exists()) {
			theAnalysisDir.mkdirs();
		}



		// Running Cetus in auto mode
		System.out.println("setting Cetus options");
		
		if (gridRadios.equals("AskCetus")) {
			String[] args = null;
			args = new String[6];
			cetusOptionSet[1] = "-verbosity=2";
			args[0] = cetusOptionSet[0]; // preprocessor
			args[1] = cetusOptionSet[1]; // verbosity set to 4
			args[2] = "-callgraph";
			args[3] = "-ompGen=4"; // keeps all pragmas
			args[4] = output;
			args[5] = inputPrg;

//			System.out.println("Cetus options in " + gridRadios + " mode are set to: ");
//			for (int i = 0; i < args.length; i++) {
//				System.out.println("args[" + i + "]" + args[i]);
//			}
			 // Measure the time before sending the request
	        long startTime = System.currentTimeMillis();
			// getting the filename from the path
			File f = new File(inputPrg);
			String fileName = f.getName();
			System.out.println("\n Here is the file name " + fileName);
			Path inputPath = Path.of(inputPrg); //should be saved in DB
			String inputConetnt = Files.readString(inputPath); //should be saved in DB
			System.out.println("\n Here is the content of the input file " + inputConetnt);
			// Save original out stream.
			PrintStream originalOut = System.out;
			// Save original err stream.
			PrintStream originalErr = System.err;

			// Create a new file output stream.

			PrintStream fileOut = new PrintStream(cetusDebuggerReport + "/" + fileName); 
			// Create a new file error stream.
			// PrintStream fileErr = new PrintStream("./err.txt");
			PrintStream fileErr = new PrintStream(cetusAnalysisReport + "/" + fileName);

			// Redirect standard out to file.
			System.setOut(fileOut);
			// Redirect standard err to file.
			System.setErr(fileErr);

			Driver driver = new Driver();
			// System.out.println("UserServlet- going inside Cetus Driver.main \n");
			// System.out.println("Cetus options in auto mode are set to: " +
			// Arrays.toString(args));
			// Running Cetus on the application
			driver.main(args);
			// System.out.println("UserServlet- back from Cetus Driver \n");
			// back to original output
			System.setOut(originalOut);
			System.setErr(originalErr);
			// remove empty lines from Cetus output file
			removeCommentsEmptyLines(outputFolder + "/" + fileName);

			System.out.println("\n Setting DB fields ");
			user.setCetusOptionSet(Arrays.toString(args));
			user.setFkId(Integer.parseInt(dbRowIdParameter));
			user.setInputCode(inputPrg);
			user.setInputContent(inputConetnt);
			user.setCetusOutput(outputFolder + "/" + fileName);
			user.setCetusDebuggerReport(cetusDebuggerReport + "/" + fileName);
			user.setCetusAnalysisReport(cetusAnalysisReport + "/" + fileName);
			// to send file content to DB, not just file paths
			Path outputCetus = Path.of(outputFolder + "/" + fileName);
			Path passesCetus = Path.of(cetusDebuggerReport + "/" + fileName);
			Path analysisCetus = Path.of(cetusAnalysisReport + "/" + fileName);
			String outputContent = Files.readString(outputCetus);
			String passesContent = Files.readString(passesCetus);
			String analysisContent = Files.readString(analysisCetus);
			user.setCetusOutputContent(outputContent);
			user.setCetusPassesContent(passesContent);
			user.setCetusAnalysisConetent(analysisContent);
			String expSection = extractExperimentalSection(outputContent);
	        // Print extracted code section
	        System.out.println("Extracted Code Section:\n" + expSection);
			user.setExperimentalSection(expSection); //experimental section should be set
			request.setAttribute("carvExpSection", expSection);
			System.out.println("\n All DB fields for AskCetus pass is created.");
			// save Cetus output results in DB
			try {
				// register the user using DAO layer in the DB
				// The limit is 16 MB for packet sizes.
				cetusDAO.insertCetusResults(user);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		    // Measure the time after receiving the response
		       long endTime = System.currentTimeMillis();
	        // Calculate the elapsed time
	           long elapsedTime = endTime - startTime;
	        // Convert elapsed time to seconds
	           double elapsedTimeInSeconds = elapsedTime / 1000.0;
	           System.out.printf( "Cetus request took %.2f (s) to be responded",elapsedTimeInSeconds );
	           request.setAttribute("ResponseTime", elapsedTimeInSeconds);
			
			
			RedirectPage = "/WEB-INF/views/carv.jsp";
			// insert the request to Cetus DB including the userDB index, path to input, input content,Cetus options, path to output, output content, exp section
			
		}else if (gridRadios.equals("CaRV")) {
			String[] args = null;
			args = new String[7];
			cetusOptionSet[1] = "-verbosity=2";
			args[0] = cetusOptionSet[0]; // preprocessor
			args[1] = cetusOptionSet[1]; // verbosity set to 4
			args[2] = "-callgraph";
			args[3] = "-ompGen=4"; // keeps all pragmas
			args[4] = "-save-experimental-section";
			args[5] = output;
			args[6] = inputPrg;

			System.out.println("Cetus options in " + gridRadios + " mode are set to: ");
			for (int i = 0; i < args.length; i++) {
				System.out.println("args[" + i + "]" + args[i]);
			}

			//Run CaRV on input file, save all the generated output files and their paths in DB so that in CaRV View you can display the results to the user.
			user.setCetusOptionSet(Arrays.toString(args));
			System.out.println("Cetus options in " + gridRadios +" mode are set to: " + Arrays.toString(args));
			// saving the path to teh input file and cetus options passed to Cetus in DB
			try {
				// Insert request values to DB then run Cetus on the code
				// and save the output in DB and show the result to the user
				userDAO.insertUserRequest(user);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// System.out.println("UserServlet connection to DB problem");
				e.printStackTrace();
			}

			// getting the filename from the path
			File f = new File(inputPrg);
			String fileName = f.getName();
			System.out.println("\n Here is the file name "+fileName);
			Path inputPath = Path.of(inputPrg);
			String inputConetnt = Files.readString(inputPath);
			System.out.println("\n Here is the content of teh input file "+inputConetnt );
			// Save original out stream.
			PrintStream originalOut = System.out;
			// Save original err stream.
			PrintStream originalErr = System.err;

			// Create a new file output stream.

			PrintStream fileOut = new PrintStream(cetusDebuggerReport + "/" + fileName);
			// Create a new file error stream.
			// PrintStream fileErr = new PrintStream("./err.txt");
			PrintStream fileErr = new PrintStream(cetusAnalysisReport + "/" + fileName);

			// Redirect standard out to file.
			System.setOut(fileOut);
			// Redirect standard err to file.
			System.setErr(fileErr);

			Driver driver = new Driver();
			// System.out.println("UserServlet- going inside Cetus Driver.main \n");
			//System.out.println("Cetus options in auto mode are set to: " + Arrays.toString(args));
			//Running Cetus on the application
			driver.main(args);
			// System.out.println("UserServlet- back from Cetus Driver \n");
			// back to original output
			System.setOut(originalOut);
			System.setErr(originalErr);
			//remove empty lines from Cetus output file
			removeCommentsEmptyLines(outputFolder + "/" + fileName);
			
			System.out.println("\n Setting DB fields ");
			user.setCetusOutput(outputFolder + "/" + fileName);
			user.setCetusDebuggerReport(cetusDebuggerReport + "/" + fileName);
			user.setCetusAnalysisReport(cetusAnalysisReport + "/" + fileName);
			System.out.println("\n Set DB fields ");
			
			// to send file content to DB, not just file paths
			Path outputCetus = Path.of(outputFolder + "/" + fileName);
			Path passesCetus = Path.of(cetusDebuggerReport + "/" + fileName);
			Path analysisCetus = Path.of(cetusAnalysisReport + "/" + fileName);
			String outputConetnt = Files.readString(outputCetus);
			String passesContent = Files.readString(passesCetus);
			String analysisContent = Files.readString(analysisCetus);

			// user.setInputContent(userCode);
			// to print file content from DB
			// System.out.println("UserServlet- outputConetnt \n"+outputConetnt);
			// System.out.println("UserServlet- passesContent \n"+passesContent);
			// System.out.println("UserServlet- analysisContent \n"+analysisContent);

			user.setCetusOutputContent(outputConetnt);
			user.setCetusPassesContent(passesContent);
			user.setCetusAnalysisConetent(analysisContent);
			// save Cetus output results in DB
			try {
				// register the user using DAO layer in the DB
				// The limit is 16 MB for packet sizes.
				// insert Cetus output
				// insert Cetus passes
				// inserts Cetus Analysis
				userDAO.insertCetusResults(user);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			RedirectPage = "/WEB-INF/views/carv.jsp";

		} else if (gridRadios.equals("auto")) {// || gridRadios.equals("CaRV")) {
			String[] args = null;

			if ( profiler!= null && profiler.equals("serialprofiler")) {
				args = new String[7]; 
				  cetusOptionSet[1] ="-verbosity=2"; 
				  args[0] = cetusOptionSet[0]; // preprocessor 
				  args[1] = cetusOptionSet[1]; // verbosity set to 4 
				  args[2] = "-callgraph"; 
				  args[3] = "-ompGen=4"; 
				  args[4] = "-profile-loops=1"; 
				  args[5] = output; 
				  args[6] = inputPrg;
			}else{
				 args = new String[6]; 
				  cetusOptionSet[1] ="-verbosity=2"; 
				  args[0] = cetusOptionSet[0]; // preprocessor 
				  args[1] = cetusOptionSet[1]; // verbosity set to 4 
				 // args[1]= "-save-experimental-section";
				  args[2] = "-callgraph"; 
				  args[3] = "-ompGen=4"; 
				  args[4] = output; 
				  args[5] = inputPrg; 
			}
			

			  
			System.out.println("Cetus options are set");
	
			//str.append("-callgraph" + ", ");
			//str.append("-ompGen=4"+ ", ");
			//str.append(output + ", ");
			// str.append(inputCode);
			//str.append(inputPrg);
			// splitting the stringbuilder and saving them on args
			//String[] args = str.toString().split(", ");
			// System.out.println("separating the items from stringbuilder: " +
			// Arrays.toString(strings));
			for (int i = 0; i < args.length; i++) {
				System.out.println("args[" + i + "]" + args[i]);
			}
			
			
			
			 //comment out omp library to get serial execution time. 
			 modifyFile(inputPrg, "#include <omp.h>","//#include <omp.h>");
			/*
			 * for (int i=0; i<inputFiles.length; i++) { if(inputFiles[i].length()>0) {
			 * System.out.println("inputs "+inputFiles[i]); args[i+5]=inputFiles[i];
			 * System.out.println("args["+ i +"]"+" = "+inputFiles[i]); } }
			 */
			// args[3]=inputFiles[0];
			// args[4]=inputFiles[1];
			// setting cetus options set by the user for the user object
			user.setCetusOptionSet(Arrays.toString(args));
			System.out.println("Cetus options in " + gridRadios +" mode are set to: " + Arrays.toString(args));
			// saving the path to teh input file and cetus options passed to Cetus in DB
			try {
				// Insert request values to DB then run Cetus on the code
				// and save the output in DB and show the result to the user
				userDAO.insertUserRequest(user);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// System.out.println("UserServlet connection to DB problem");
				e.printStackTrace();
			}

			// getting the filename from the path
			File f = new File(inputPrg);
			String fileName = f.getName();
			System.out.println("\n Here is the file name "+fileName);
			// Save original out stream.
			PrintStream originalOut = System.out;
			// Save original err stream.
			PrintStream originalErr = System.err;

			// Create a new file output stream.

			PrintStream fileOut = new PrintStream(cetusDebuggerReport + "/" + fileName);
			// Create a new file error stream.
			// PrintStream fileErr = new PrintStream("./err.txt");
			PrintStream fileErr = new PrintStream(cetusAnalysisReport + "/" + fileName);

			// Redirect standard out to file.
			System.setOut(fileOut);
			// Redirect standard err to file.
			System.setErr(fileErr);

			Driver driver = new Driver();
			// System.out.println("UserServlet- going inside Cetus Driver.main \n");
			//System.out.println("Cetus options in auto mode are set to: " + Arrays.toString(args));
			//Running Cetus on the application
			driver.main(args);
			// System.out.println("UserServlet- back from Cetus Driver \n");
			// back to original output
			System.setOut(originalOut);
			System.setErr(originalErr);
			//remove empty lines from Cetus output file
			removeCommentsEmptyLines(outputFolder + "/" + fileName);
			
			System.out.println("\n Setting DB fields ");
			user.setCetusOutput(outputFolder + "/" + fileName);
			user.setCetusDebuggerReport(cetusDebuggerReport + "/" + fileName);
			user.setCetusAnalysisReport(cetusAnalysisReport + "/" + fileName);
			System.out.println("\n Set DB fields ");
			
			// to send file content to DB, not just file paths
			Path outputCetus = Path.of(outputFolder + "/" + fileName);
			Path passesCetus = Path.of(cetusDebuggerReport + "/" + fileName);
			Path analysisCetus = Path.of(cetusAnalysisReport + "/" + fileName);
			String outputConetnt = Files.readString(outputCetus);
			// System.out.println("[UserServlet.java] Cetus output in Servlet: " +
			// outputConetnt);
			String passesContent = Files.readString(passesCetus);
			String analysisContent = Files.readString(analysisCetus);

			// user.setInputContent(userCode);
			// to print file content from DB
			// System.out.println("UserServlet- outputConetnt \n"+outputConetnt);
			// System.out.println("UserServlet- passesContent \n"+passesContent);
			// System.out.println("UserServlet- analysisContent \n"+analysisContent);

			user.setCetusOutputContent(outputConetnt);
			// System.out.println("[UserServlet.java] user.getCetusOutputContent(): " +
			// user.getCetusOutputContent());
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////			
			/*
			 * Our goal is to add #include <omp.h> to the header part and add the number of
			 * threads we are asking to right after include part
			 */
			String outputContent = user.getCetusOutputContent();
			String inclidx = null;
			String inclompheader = "#include <omp.h>\r\n";
			int hasincl = outputContent.indexOf("#include", 0); // !=-1? true: false; //true
			System.out.println("[UserServlet.java]-Auto mode, hasincl: " + hasincl);
			String newString = null;

			if (hasincl > 0) {
//				has include headers
				// Create a new string
				newString = outputContent.substring(0, hasincl) + inclompheader + outputContent.substring(hasincl - 1);
				System.out.println("[UserServlet.java]-Auto mode, has incl: " + newString);
			} else {
//				has no include headers
				newString = inclompheader + outputContent.substring(0);
				System.out.println("[UserServlet.java]-Auto mode, new output content " + newString);

			}

			// set newString as the new output content
			user.setCetusOutputContent(newString);
			// write it down to the file
			// String outputConetnt = Files.readString(outputCetus);
			Files.writeString(outputCetus, newString, StandardOpenOption.WRITE);

////////////////////////////////////////////////////////////////////////////			

			user.setCetusPassesContent(passesContent);

			// time the execution of the input and output			
			String execution = executeResultSeqPara(user);
			String profile="";
			String executionResult="";
			System.out.println("codeExecuter "+codeExecuter+" ***********");
			System.out.println("paracodeExecuter "+paracodeExecuter+" ***********");
			//if the user asks for the profiler to run on the code-runs on the serial code and the parallel code
			//so the result of execution of the codes are already provided
			if (profiler!= null && profiler.equals("serialprofiler")) {
				profile = executionResultSeqPara(user);//produces the execution results and profiling info
			}else if(profiler== null && codeExecuter!=null && codeExecuter.equals("serialExec") && paracodeExecuter!=null && paracodeExecuter.equals("parallelExec")){
				executionResult = executionResultSeqPara(user);
		    }else if ( profiler== null && codeExecuter!=null && codeExecuter.equals("serialExec")){ //if the user has asked only for the result of execution to be printed
				executionResult= executionResultSeq(user);//produces only serial execution results
			}else if (profiler== null && paracodeExecuter!=null && paracodeExecuter.equals("parallelExec")){
				executionResult= executionResultPara(user);//produces only parallel execution results
			}
			// set user analysis content
			//String analysis = analysisContent.concat(execution);
			String analysis = execution+ profile + executionResult;
			System.out.println("Analysis saved in DB: \n"+analysis);
			// System.out.println("\n analysis content : "+analysis);
			user.setCetusAnalysisConetent(analysis);

			// in case of error
			System.out.println("*******************************************" + analysisContent.indexOf("Error")); // returns
																													// the
																													// index
																													// of
																													// the
																													// first
																													// Error
			String theERrr = null;
			int isFound = analysisContent.indexOf("Error", 0); // !=-1? true: false; //true
			System.out.println("[UserServlet.java]-Auto mode, Error isFound: " + isFound);
			// System.out.println("[UserServlet.java]-Auto mode, Error isFound content:
			// "+analysisContent.substring(isFound) );
			if (isFound > 0) {
				// Err is found
				// set page that should be redirected to
				System.out.println("Err is found"); // cetus output should not be provided to the user.just give the
													// error to the user.
				// move the index to the first found error+6
				////////// System.out.println("[UserServlet.java]-analysisContent,
				// auto"+analysisContent );

				int fromindex = analysisContent.indexOf("Exception", isFound);
				System.out.println("[UserServlet.java]-fromindex, auto " + fromindex);
				if (fromindex > 0) {
					// print from "Exception Type:" to * for the user to show the error to the user
					int toindex = analysisContent.indexOf("*", fromindex);
					System.out.println("[UserServlet.java]-fromindex, auto" + toindex);

					// System.out.println("[UserServlet.java]-fromindex>toindex"+(fromindex>toindex)
					// );
					// System.out.println("[UserServlet.java]-toindex>analysisContent.length()"+(toindex>analysisContent.length())
					// );

					if ((fromindex > 0 && toindex < 0) || (fromindex > 0 && toindex > analysisContent.length())) {// FormatedAnalysis
																													// ==
																													// null
																													// ||
																													// FormatedAnalysis.isEmpty()
																													// ||
																													// FormatedAnalysis.trim().isEmpty()){
						theERrr = analysisContent.substring(fromindex);
						System.out.println("[UserServlet.java]-the Error:" + theERrr);
						RedirectPage = "/WEB-INF/views/erroroutput.jsp";
					} else if ((fromindex > 0 && toindex > 0 && fromindex < toindex
							&& toindex <= analysisContent.length())) {
						theERrr = analysisContent.substring(fromindex, toindex);
						System.out.println("[UserServlet.java]-theError, two indexes" + theERrr);
					}
					RedirectPage = "/WEB-INF/views/erroroutput.jsp";
				} else if (fromindex < 0) { // to capture "Error: reading Cetus info URL failed:
											// https://engineering.purdue.edu/Cetus/cetusinfo.txt"
					theERrr = analysisContent.substring(isFound);
					System.out.println(theERrr);
					RedirectPage = "/WEB-INF/views/useroutput.jsp";
				}
				// theERrr = analysisContent.substring(fromindex,toindex );
				// System.out.println(theERrr);
				// setting request attributes for passing to JSP files
				request.setAttribute("theERrror", theERrr);

			} else {
				// no Err
				// set Query attributes if query is requested
				/*
				 * String codeQuery= request.getParameter("queries");
				 * System.out.println("requested query is"+ codeQuery);
				 * request.setAttribute("requestedQuery", codeQuery);
				 */
				// request.setAttribute("cetus", "auto");
				RedirectPage = "/WEB-INF/views/useroutput.jsp";
				// System.out.println("No Err is found" );
			}
			
			try {
				// register the user using DAO layer in the DB
				userDAO.insertCetusResults(user);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (gridRadios.equals("semi-auto")){
			//System.out.println("printing str"+str.toString());
			String compileType = request.getParameter("action");
			if (compileType.equals("ReCompile")) {
				System.out.println("\n\n recompile identified");
				// usercode is saved
				// what parallelization options should run on it.
				// pass them to str and the rest will be done automatically. get it from - to
				// -output?
				String setOptions = request.getParameter("allCetusOptions");

				int fromindex = 0;
				int toindex = 0;
				str.setLength(0);// to reset the stringuilder
				while (toindex < setOptions.length()-1 && fromindex >= 0) {

					fromindex = setOptions.indexOf("-", toindex);
					System.out.println(fromindex);
					toindex = setOptions.indexOf(",", fromindex);
					System.out.println(toindex);
				if (fromindex < 0 || fromindex > toindex || toindex > setOptions.length() || toindex < 0) {
							  System.out.println("Here are suboptions" + str.toString());
				  } else { 
					System.out.println(setOptions.substring(fromindex, toindex ));
					str.append(setOptions.substring(fromindex, toindex )).append(", ");
				  }
				}
				
				System.out.println("Here are suboptions" + str.toString());
				
				//System.out.println("set options passed to servlet" + setOptions);
				
				//str.append(setOptions);// setting the options we brought to servlet
				// if action is Recompile then the request should go to useroutput.jsp
			}
			 //comment out omp library to get serial execution time. 
			 modifyFile(inputPrg, "#include <omp.h>","//#include <omp.h>");
			str.append(output + ", ");
			// str.append(inputCode);
			str.append(inputPrg);
			// splitting the stringbuilder and saving them on args
			String[] args = str.toString().split(", ");
			// System.out.println("separating the items from stringbuilder: " +
			// Arrays.toString(strings));
			for (int i = 0; i < args.length; i++) {
				System.out.println("args[" + i + "]" + args[i]);
			}

			// System.out.println("Cetus options in semi-auto mode are set to: " +
			// Arrays.toString(args));

			user.setCetusOptionSet(Arrays.toString(args));
			// System.out.println("Cetus options in auto mode are set to: " +
			// Arrays.toString(args));

			try {
				// Insert request values to DB then run Cetus on the code
				// and save the output in DB and show the result to the user
				userDAO.insertUserRequest(user);
				System.out.println("Servlet- User input is inserted to DB" );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// getting the filename from the path
			File f = new File(inputPrg);
			String fileName = f.getName();

			// Save original out stream.
			PrintStream originalOut = System.out;
			// Save original err stream.
			PrintStream originalErr = System.err;

			// Create a new file output stream.
			PrintStream fileOut = new PrintStream(cetusDebuggerReport + "/" + fileName);
			// Create a new file error stream.
			PrintStream fileErr = new PrintStream(cetusAnalysisReport + "/" + fileName);

			// Redirect standard out to file.
			System.setOut(fileOut);
			// Redirect standard err to file.
			System.setErr(fileErr);
			Driver driver = new Driver();
			driver.main(args);
			//System.out.println("Servlet- Cetus has returned results" );
			// back to original output
			System.setOut(originalOut);
			System.setErr(originalErr);
			
			//remove empty lines from Cetus output file
			removeCommentsEmptyLines(outputFolder + "/" + fileName);

			user.setCetusOutput(outputFolder + "/" + fileName);
			user.setCetusDebuggerReport(cetusDebuggerReport + "/" + fileName);
			user.setCetusAnalysisReport(cetusAnalysisReport + "/" + fileName);

			// to send file content to DB.
			Path outputCetus = Path.of(outputFolder + "/" + fileName);
			Path passesCetus = Path.of(cetusDebuggerReport + "/" + fileName);
			Path analysisCetus = Path.of(cetusAnalysisReport + "/" + fileName);
			String outputConetnt = Files.readString(outputCetus);
			String passesContent = Files.readString(passesCetus);
			String analysisContent = Files.readString(analysisCetus);

			System.out.println("*******************************************" + analysisContent.indexOf("Error")); // returns
																													// the
																													// index
																													// of
																													// the
																													// first
																													// Error

			int isFound = analysisContent.indexOf("Error", 0); // !=-1? true: false; //true
			System.out.println("[UserServlet.java]-Error isFound: " + isFound);
			String theERrr = null;
			if (isFound > 0) {
				// Err is found
				// set page that should be redirected to

				System.out.println("Err is found"); // cetus output should not be provided to the user.just give the
													// error to the user.
				// System.out.println("[UserServlet.java]-analysisContent"+analysisContent );
				// move the index to the first found error+6
				// "Exception Type:"
				int fromindex = analysisContent.indexOf("Exception", isFound);
				System.out.println("[UserServlet.java]-fromindex" + fromindex);
				if (fromindex > 0) {
					// print from "Exception Type:" to * for the user to show the error to the user
					int toindex = analysisContent.indexOf("*", fromindex);
					System.out.println("[UserServlet.java]-toindex" + toindex);

					if (toindex < 0) {
						theERrr = analysisContent.substring(fromindex);
						System.out.println("[UserServlet.java]-theError" + theERrr);
					} else if (toindex > 0 && fromindex < toindex && toindex < analysisContent.length()) {
						theERrr = analysisContent.substring(fromindex, toindex);
						System.out.println("[UserServlet.java]-theError with two indexes" + theERrr);
					}

				} else if (fromindex < 0) {
					theERrr = analysisContent.substring(isFound);
				}

				System.out.println(theERrr);
				// setting request attributes for passing to JSP files
				request.setAttribute("theERrror", theERrr);
				RedirectPage = "/WEB-INF/views/erroroutput.jsp";
			} else {
				// no Err
				// request.setAttribute("cetus", "customized");
				RedirectPage = "/WEB-INF/views/useroutput.jsp";
				// System.out.println("No Err is found" );
			}

			// user.setInputContent(userCode);
			// to print file content from DB
			// System.out.println("UserServlet- outputConetnt \n" + outputConetnt);
			// System.out.println("UserServlet- passesContent \n" + passesContent);
			// System.out.println("UserServlet- analysisContent \n" + analysisContent);

			user.setCetusOutputContent(outputConetnt);
			user.setCetusPassesContent(passesContent);

			String outputContent = user.getCetusOutputContent();
			String inclompheader = "#include <omp.h>\r\n";
			int hasincl = outputContent.indexOf("#include", 0); // !=-1? true: false; //true
			System.out.println("[UserServlet.java]-Auto mode, hasincl: " + hasincl);
			String newString = null;

			if (hasincl > 0) {
//				has include headers
				// Create a new string
				newString = outputContent.substring(0, hasincl) + inclompheader + outputContent.substring(hasincl - 1);
				System.out.println("[UserServlet.java]-Auto mode, has incl: " + newString);
			} else {
//				has no include headers
				newString = inclompheader + outputContent.substring(0);
				System.out.println("[UserServlet.java]-Auto mode, new output content " + newString);

			}

			// set newString as the new output content
			user.setCetusOutputContent(newString);
			// write it down to the file
			// String outputConetnt = Files.readString(outputCetus);
			Files.writeString(outputCetus, newString, StandardOpenOption.WRITE);

			// execute input and output
			/*
			 * int numThreads = 4; System.out.println("Input file path: " +
			 * user.getInputCode()); System.out.println("Output file path: " +
			 * user.getCetusOutput()); // inputFile= File(user.getInputCode()); inputFile =
			 * new File(user.getInputCode()); openMpFile = new File(user.getCetusOutput());
			 * File inputOutFile = execute.compileCFile(inputFile, 0); File OutputOutFile =
			 * execute.compileCFile(openMpFile, 1); double seqRunTime =
			 * execute.runExe(inputOutFile, 0); double parRunTime =
			 * execute.runExe(OutputOutFile, numThreads); String execution =
			 * execute.calculateSpeedup(seqRunTime, parRunTime, numThreads);
			 */
			String execution = executeResultSeqPara(user);
			String profile="";
			String executionResult="";
			System.out.println("codeExecuter"+codeExecuter+"***********");
			System.out.println("paracodeExecuter"+paracodeExecuter+"***********");
			//if the user asks for the profiler to run on the code-runs on the serial code and the parallel code
			//so the result of execution of the codes are already provided
			if (profiler!= null && profiler.equals("serialprofiler")) {
				profile = executionResultSeqPara(user);//produces the execution results and profiling info
			}
			/*
			 * else if(serialprofiler!=null && serialprofiler.equals("serialprofiler") &&
			 * parallelprofiler!=null && parallelprofiler.equals("parallelprofiler")){ //run
			 * the profiler }else if(serialprofiler!=null &&
			 * serialprofiler.equals("serialprofiler")){ //run the profiler only on the
			 * serial code }
			 */
			/*
			 * else if(parallelprofiler!=null &&
			 * parallelprofiler.equals("parallelprofiler")){
			 * 
			 * }
			 */
			else if(profiler== null && codeExecuter!=null && codeExecuter.equals("serialExec") && paracodeExecuter!=null && paracodeExecuter.equals("parallelExec")){
				executionResult = executionResultSeqPara(user);
		    }else if ( profiler== null && codeExecuter!=null && codeExecuter.equals("serialExec")){ //if the user has asked only for the result of execution to be printed
				executionResult= executionResultSeq(user);//produces only serial execution results
			}else if (profiler== null && paracodeExecuter!=null && paracodeExecuter.equals("parallelExec")){
				executionResult= executionResultPara(user);//produces only parallel execution results
			}
			// set user analysis content
			//String analysis = analysisContent.concat(execution);
			String analysis = execution+ profile + executionResult;
			// System.out.println("\n analysis content : "+analysis);
			user.setCetusAnalysisConetent(analysis);
			// user.setCetusAnalysisConetent(analysisContent);
			
			// save Cetus output results in DB
			try {
				// register the user using DAO layer in the DB
				//The limit is 16 MB for packet sizes.
				//insert Cetus output
				//insert Cetus passes
				//inserts Cetus Analysis
				userDAO.insertCetusResults(user);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if ( /* exec.equals("Execute") && */  gridRadios.equals("Execute")) {
			//just execute the code, dont compile it uisng Cetus, and return the results to output
			//System.out.println("already here-what's next?");
			
			// execute input and output
						
						  int numThreads = 4; 
						  //System.out.println("Input file path: " +  user.getInputCode()); 
						  //System.out.println("Output file path: " +	  user.getCetusOutput()); // inputFile= File(user.getInputCode()); 
						  String handParllelizedFilePath =  inputPrg; //file path on the server
						  System.out.println("HandParallelized File path? " + handParllelizedFilePath ); 
						  File execFile = new File(inputPrg);
						  //if omp.h is included then compile and execute parallel and serial code this way.
						  					  
						  
						 //comment out omp library to get serial execution time. 
						 modifyFile(inputPrg, "#include <omp.h>","//#include <omp.h>");
						 File serialFile =   execute.compileCFile(execFile, 0); 
						 System.out.println("handparallelized code compiled  sequantially " ); 
						 double seqRunTime =  execute.runExe(serialFile, 0); 
						 String SerialResult= execute.executionResult(serialFile, 0);
						 System.out.println("handparallelized code executed sequantially "+ seqRunTime );
						  //Time to compile and run the parallel code
						
						modifyFile(inputPrg, "//#include <omp.h>","#include <omp.h>"); 
						 File paraFile = execute.compileCFile(execFile, 1);
						 //System.out.println("handparallelized code compiled in parallel  \n" );
										  
						  //Run with 4 threads
						// double parRunTime0 = execute.runExe(paraFile, 0);
						// String parResult0= execute.executionResult(paraFile, 0);
						//System.out.println("parRunTime0   \n"+ parRunTime0  );
						 double parRunTime1 = execute.runExe(paraFile, 1);
						 System.out.println("parRunTime1   \n"+ parRunTime1  );
							double parRunTime4 = execute.runExe(paraFile, 4);
							System.out.println("parRunTime4   \n"+ parRunTime4  );
//							double parRunTime8 = execute.runExe(paraFile, 8);
							String parResult= execute.executionResult(paraFile, 4);
//							System.out.println("parRunTime8   \n"+ parRunTime8 );
							//System.out.println("handparallelized code executed in parallel "+ parRunTime); 
							String execution = /*
												 * execute.calculateSpeedup(seqRunTime, parRunTime0,
												 * 0)+"\n--------------\n"+
												 */execute.calculateSpeedup(seqRunTime, parRunTime1, 1)
									+ "\n--------------\n" + execute.calculateSpeedup(seqRunTime, parRunTime4, 4)
//									+ "\n--------------\n" + execute.calculateSpeedup(seqRunTime, parRunTime8, 8)
									+ "[ExecutionResultSerial] \n" + SerialResult
									+ /* "\n-----parallel code 0 threads---------\n"+parResult0+ */"[ExecutionResultParallel]\n------ 4 threads--------\n"+ parResult; 
							//System.out.println("here is the speedup "+ execution );
							//insert to DB- hand parallelized file path as input. and the execution result to analysis
							
							 user.setCetusAnalysisReport("");
							 user.setCetusDebuggerReport("");
							 user.setCetusOptionSet("");
							 user.setCetusOutputContent("");
							 user.setCetusOutput("");
							 user.setCetusPassesContent("");
							 user.setFileOutput("");
							 user.setInputCode(inputPrg);
							 user.setInputContent(userCode1);
							 user.setCetusAnalysisConetent(execution);

							 
							 try {
									// Insert request values to DB then run Cetus on the code
									// and save the output in DB and show the result to the user
									userDAO.insertUserRequest(user);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									// System.out.println("UserServlet connection to DB problem");
									e.printStackTrace();
								}
							try {
									// register the user using DAO layer in the DB
									userDAO.insertCetusResults(user);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							 
							 
							 
			
			RedirectPage = "/WEB-INF/views/execoutput.jsp";
		}else if(gridRadios.equals("NoCompile")) {
			//Recompile.save the passed code to a file. when there's a quote for define comamnads or strings the code needs to be corrected if passed as the string. save it as it is passed to a file. and then set the right parameters.
			
			
			RedirectPage = "/WEB-INF/views/index.jsp";
			
		}else if(gridRadios.equals("CaRVExecute")) {
			
			//check a flag to see if which hase should be executed
			if (carvPhase.equals("Capture")) {
				System.out.println("Capture phase");
				//if capture phase should be executed, compile and execute the entire code with omp.h library added to it and and compiling with -fopenmp, tehn execute it using 4 threads.
				//Compiling the file with -fopenmp flag, and running it with 4 thredas if it is parallel.
				int numThreads = 4; 
				File execFile = new File(inputPrg);
				File paraFile = execute.compileCFile(execFile, 1); //1compile using -fopenmp
				//double parRunTime4 = execute.runExe(paraFile, 4); //4run it using 4 threads
				//System.out.println("parRunTime4   \n"+ parRunTime4  ); //run time
				String parResult= execute.executionResult(paraFile, numThreads);//4 cmd results of execution that should be saved for the user.
				//set the execution string as showing this is the Capture result
				String execution = "[Capture] \n"+parResult +"\n[EndCapture]\n\n";
				System.out.println("Capture Phase Execution Results:   \n"+ execution  ); 
				//save it in DB at the current capture request- <Capture>, <EndCapture>
				user.setCetusAnalysisConetent(execution);
				try {
						// register the user using DAO layer in the DB
						userDAO.updateCetusResults(user);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}else if(carvPhase.equals("Replay")){
			System.out.println("Replay phase");
			//if replay phase is executed. replace the define parameter, and then compile and execute the code how you compiled and executed the capture phase file.
			int numThreads = 4; 
			CarvreplayDAO carvreplayDAO = new CarvreplayDAO();
			int lastReplayId=0;
			//gets the last replay Id the one taht will be inserted to DB will be lastReplayId+1
			try {
				// register the user using DAO layer in the DB
				lastReplayId =carvreplayDAO.getLastReplayId();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lastReplayId++; //since we will insert the replay into the DB later, to get the correct id, one should be added to id.
			File execFile = new File(inputPrg);
			String execFilePath = execFile.getAbsolutePath();
			modifyFile(execFilePath, "#define Initial","#define Experimental"); 
			//compile the file

			File paraFile = execute.compileCFile(execFile, 1); 
			String parResult= execute.executionResult(paraFile, numThreads);
			String execution = "\n==========================================\n[Replay"+lastReplayId+"] \n"+parResult + "\n[EndReplay"+lastReplayId+"]\n\n";
			System.out.println("Replay Phase Execution Results:   \n"+ execution  ); 
//			System.out.println("execFilePath is:"+ execFilePath);
//			System.out.println("inputPrg is:"+ inputPrg);
			//set the execution string to show it is replay results.
			//save it in DB with the current capture request- <Replay>, <EndReplay>
			
			//I am not saving the inputPrg in DB
			user.setCetusAnalysisConetent(execution);
			try {
					// register the user using DAO layer in the DB
					userDAO.updateCetusResults(user);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			Carvreplay replay= new Carvreplay();
			//int lastreplayID= carvreplayDAO.getLastReplayId()
			//int replayId= replay.getReplayid();
			//System.out.println("carv-replay-id:::"+ replayId);
			replay.setUserid(Integer.parseInt(dbRowIdParameter)); //user-id
			System.out.println("Cetus-user-id:::"+ dbRowIdParameter);
			replay.setReplayfilepath(execFilePath); 
			System.out.println("Replay File Path:::"+ execFilePath);
			Path replayFile = Path.of(execFilePath);
			String replayFileContent = Files.readString(replayFile, StandardCharsets.US_ASCII);
			System.out.println("Replay File Content:::"+ replayFileContent);
            replay.setReplayfilecontent(replayFileContent);
            replay.setReplayexpsection(expsection); 
            System.out.println("Replay Experimental Section:::"+ expsection);
            request.setAttribute("carvExpSection", expsection); //passing expsection to jsp page after each replay
            replay.setReplayexecutionresults(execution);
            System.out.println("Replay File Execution results:::"+ execution);
            
            try {
				// register the user using DAO layer in the DB
				carvreplayDAO.insertReplayRequest(replay);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}else if(carvPhase.equals("Input")){
			System.out.println("carvPhase-Input Run phase");
			//if replay phase is executed. replace the define parameter, and then compile and execute the code how you compiled and executed the capture phase file.
			int numThreads = 4; 
			File execFile = new File(inputPrg);
			String execFilePath = execFile.getAbsolutePath();
			modifyFile(execFilePath, "#include <stdio.h>","#include <stdio.h> \n#include <omp.h>"); 
			//compile the file

			File paraFile = execute.compileCFile(execFile, 1); 
			String parResult= execute.executionResult(paraFile, numThreads);
			String execution = "\n[Input] \n"+parResult + "\n[EndInput]\n\n";
			System.out.println("Input Execution Results:   \n"+ execution  ); 
//			System.out.println("execFilePath is:"+ execFilePath);
//			System.out.println("inputPrg is:"+ inputPrg);
			//set the execution string to show it is replay results.
			//save it in DB with the current capture request- <Replay>, <EndReplay>
			
			user.setCetusAnalysisConetent(execution);
			try {
					// register the user using DAO layer in the DB
					userDAO.updateCetusResults(user);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			RedirectPage = "/WEB-INF/views/carv.jsp";
		}



//if Analysiscontent contains "Cetus Error Detected" then redirect it to the page showing the error

		// Read more:
		// https://javarevisited.blogspot.com/2016/10/how-to-check-if-string-contains-another-substring-in-java-indexof-example.html#ixzz6wge3cNGB
		// the goal is to send back to the user the Cetus output and the Cetus Debugger
		// Report
		// the next step would be filtering the output sent back to the user to make it
		// more user friendly
		RequestDispatcher dispatcher = request.getRequestDispatcher(RedirectPage);
		dispatcher.forward(request, response);

		// response.sendRedirect("employeedetails.jsp");
	}

	/**
	 * @param txt
	 * @return
	 */
	private String textToString(String txt) {
		String[] lines = txt.split("\\r\\n");
		// Create a StringBuilder to store the concatenated lines
		StringBuilder concatenatedLines = new StringBuilder();
		// Iterate through the lines array and concatenate each line
		for (String line : lines) {
		    concatenatedLines.append(line).append(" "); // Append each line followed by a space
		}
		return concatenatedLines.toString().trim();
	}
	
//	private String extractExperimentalSection(String content) {
//		String expSection = "";
//
//        // Define regex pattern for extracting code section
//        String regex = "#pragma experimental section start name=null\\s*(.*?)\\s*#pragma experimental section stop name=null";
//        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
//        Matcher matcher = pattern.matcher(content);
//
//        // If a match is found, extract the code section
//        if (matcher.find()) {
//            expSection = matcher.group(1).trim();
//        }
//
//        return expSection;
//	}

	// Method to extract code section without using regex
    public static String extractExperimentalSection(String content) {
        String expSection = "";

        // Find the start and stop indices of the pragma statements
        int startIndex = content.indexOf("#pragma experimental section start name= null");
        int stopIndex = content.indexOf("#pragma experimental section stop name= null");

        // If both start and stop indices are found
        if (startIndex != -1 && stopIndex != -1) {
            // Extract the code section
            expSection = content.substring(startIndex+ "#pragma experimental section start name= null".length(), stopIndex);
        }

        return expSection;
    }

	/**
	 * 
	 * @param user
	 * @return the execution result of running the parallel code
	 */
    private String executionResultPara(User user) {
    	File openMpFile;
		int numThreads = 0;
		String execution="";
		StringBuilder exec= new StringBuilder("");
		numThreads= Runtime.getRuntime().availableProcessors();
		int halfNumThreads= numThreads/2;
		
		System.out.println("profiler halfNumThreads: " + halfNumThreads);
		System.out.println("Output file path: " + user.getCetusOutput());
		openMpFile = new File(user.getCetusOutput());

		//compiling the ouput file in parallel
		File OutputOutFilePara = execute.compileCFile(openMpFile, 1);
		exec.append("\n[Profiler]\n");
		if ((OutputOutFilePara != null) && OutputOutFilePara.exists() ) {
			exec.append("\n[ParallelCode] Parallel code compiled successfully.\n");
		} else {
			exec.append("\n[ParallelCode] Compilation of the parallel code failed (Compile-time Error).\n");
		}
		
		String parRun= execute.executionResult(OutputOutFilePara, halfNumThreads);
		if(parRun!=null ||parRun!="") {
			exec.append("\n[ParallelCode] Parallel code executed successfully.\n"+parRun+"\n");
		}else {
			exec.append("\n[ParallelCode] Execution of the parallel code failed (Run-time Error).\n");
		}
		System.out.println("Execution result in servlet"+exec.toString());
		execution = exec.toString();
		return execution;
		
	}

/**
 * 
 * @param user
 * @return the execution result of running the serial code
 */
	
	private String executionResultSeq(User user) {
		File inputFile;
		String execution=""; //returns
		StringBuilder exec= new StringBuilder("");
		System.out.println("Input file path: " + user.getInputCode());
		inputFile = new File(user.getInputCode());
		File inputOutFile = execute.compileCFile(inputFile, 0);
		exec.append("\n[Profiler]\n");
		if ( (inputOutFile != null) && inputOutFile.exists() ) {
			exec.append("[Execution] Serial code compiled successfully.\n");
		} else {
			exec.append( "[Execution] Compilation of the serial code failed (Compile-time Error).\n");
		}	
		
		String seqRun = execute.executionResult(inputOutFile, 0);
		if(seqRun!=null ||seqRun!="") {
			exec.append("\n[SerialCode] Serial code executed successfully.\n"+seqRun+"\n");
		}else {
			exec.append("\n[SerialCode] Execution of the serial code failed (Run-time Error).\n");
		}
		execution = exec.toString();
		return execution;
	}

	/**
	 * @param user . The parameter passes the serial input code as well as the parallel output code to be compiled and then executed.
	 * @return timing the execution of the serial code and the parallel code on 4 codes.
	 */
	private String executeResultSeqPara(User user) {
		File inputFile;
		File openMpFile;
		int numThreads = 0;
		String execution="";
		StringBuilder exec= new StringBuilder("");
		numThreads= Runtime.getRuntime().availableProcessors();
		int halfNumThreads= numThreads/2;
		System.out.println("Input file path: " + user.getInputCode());
		System.out.println("Output file path: " + user.getCetusOutput());
		// inputFile= File(user.getInputCode());
		inputFile = new File(user.getInputCode());
		openMpFile = new File(user.getCetusOutput());
		File inputOutFile = execute.compileCFile(inputFile, 0);
		if ( (inputOutFile != null) && inputOutFile.exists() ) {
			exec.append("[Execution] Serial code compiled successfully.\n");
		
		} else {
			exec.append( "[Execution] Compilation of the serial code failed (Compile-time Error).\n");
		}
		File OutputOutFile = execute.compileCFile(openMpFile, 1);
		if ((OutputOutFile != null) && OutputOutFile.exists() ) {
			exec.append("[Execution] Parallel code compiled successfully.\n");
		} else {
			exec.append("[Execution] Compilation of the parallel code failed (Compile-time Error).\n");
		}
		double seqRunTime = execute.runExe(inputOutFile, 0);
		
		if(seqRunTime>0) {
			exec.append("[Execution] Serial code executed successfully.\n");
		}else {
			exec.append( "[Execution] Execution of the serial code failed (Run-time Error).\n");
		}
		double parRunTimeHalfThread= execute.runExe(OutputOutFile, halfNumThreads);
//		double parRunTime = execute.runExe(OutputOutFile, numThreads);
		if(parRunTimeHalfThread>0) {
			exec.append("[Execution] Parallel code executed successfully.\n");
		}else {
			exec.append( "[Execution] Execution of the parallel code failed (Run-time Error).\n");
		}
		
		exec.append("\n--------------\n").append(execute.calculateSpeedup(seqRunTime, parRunTimeHalfThread,
				halfNumThreads))/*
								 * .append("\n--------------\n").append( execute.calculateSpeedup(seqRunTime,
								 * parRunTime, numThreads))
								 */;
		execution = exec.toString();
		return execution;
	}
	/**
	 * 
	 * @param user, passing the input and output code to the program
	 * @return String, reporting on the execution result of the program- if profiler is set then it shows profiling report of the program.
	 */
	private String executionResultSeqPara(User user) {
		//File inputFile;
		File openMpFile;
		int numThreads = 0;
		String execution="";
		StringBuilder exec= new StringBuilder("");
		numThreads= Runtime.getRuntime().availableProcessors();
		int halfNumThreads= numThreads/2;
		
		System.out.println("profiler halfNumThreads: " + halfNumThreads);
		System.out.println("Output file path: " + user.getCetusOutput());
		// inputFile= File(user.getInputCode());
		//inputFile = new File(user.getInputCode());
		openMpFile = new File(user.getCetusOutput());
		exec.append("\n[Profiler]\n");

		//compile output file in serial- it has loop directives and if executed in serial shows the profiling info of the sequential code
		File OutputOutFile = execute.compileCFile(openMpFile, 0);
		if ((OutputOutFile != null) && OutputOutFile.exists() ) {
			exec.append("\n[SerialCode] Serial code compiled successfully.\n");
		} else {
			exec.append("\n[SerialCode] Compilation of the serial code failed (Compile-time Error).\n");
		}
		//profiling the serial code
		String seqRun = execute.executionResult(OutputOutFile, 0);
				if(seqRun!=null ||seqRun!="") {
					exec.append("\n[SerialCode] Serial code executed successfully.\n"+seqRun+"\n");
				}else {
					exec.append("\n[SerialCode] Execution of the serial code failed (Run-time Error).\n");
				}
		//compiling the ouput file in parallel
		File OutputOutFilePara = execute.compileCFile(openMpFile, 1);
		if ((OutputOutFilePara != null) && OutputOutFilePara.exists() ) {
			exec.append("\n[ParallelCode] Parallel code compiled successfully.\n");
		} else {
			exec.append("\n[ParallelCode] Compilation of the parallel code failed (Compile-time Error).\n");
		}
		
		//profiling the parallel code
		String parRun= execute.executionResult(OutputOutFilePara, halfNumThreads);
		if(parRun!=null ||parRun!="") {
			exec.append("\n[ParallelCode] Parallel code executed successfully.\n"+parRun+"\n");
		}else {
			exec.append("\n[ParallelCode] Execution of the parallel code failed (Run-time Error).\n");
		}
		System.out.println("Execution result in servlet"+exec.toString());
		execution = exec.toString();
		return execution;
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	// protected void doPut(HttpServletRequest request, HttpServletResponse
	// response) throws ServletException, IOException {
	// // TODO Auto-generated method stub
	// }

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	// protected void doDelete(HttpServletRequest request, HttpServletResponse
	// response) throws ServletException, IOException {
	// // TODO Auto-generated method stub
	// }

	/**
	 * @see HttpServlet#doHead(HttpServletRequest, HttpServletResponse)
	 */
	// protected void doHead(HttpServletRequest request, HttpServletResponse
	// response) throws ServletException, IOException {
	// // TODO Auto-generated method stub
	// }

	/**
	 * Extracts file name from HTTP header content-disposition
	 */
	/***** Helper Method #1 - This Method Is Used To Read The File Names *****/
	private String extractFileName(Part part) {
		// String fileName = "",
		// contentDisposition = part.getHeader("content-disposition");
		// String[] items = contentDisposition.split(";");
		// for (String item : items) {
		// if (item.trim().startsWith("filename")) {
		// fileName = item.substring(item.indexOf("=") + 2, item.length() - 1);
		// }
		// }
		// return fileName;
		String contentDisp = part.getHeader("content-disposition");
		System.out.println("content-disposition header= " + contentDisp);
		String[] tokens = contentDisp.split(";");
		for (String token : tokens) {
			if (token.trim().startsWith("filename")) {
				return token.substring(token.indexOf("=") + 2, token.length() - 1);
			}
		}
		return "";
	}

	public String[] makeHTMLqualified(String[] lines, StringBuilder sb) {

		for (int i = 0; i < lines.length; i++) {
			if (lines[i].contains("&")) {
				lines[i] = lines[i].replaceAll("&", "&amp;");
			}
			if (lines[i].contains("<")) {
				lines[i] = lines[i].replaceAll("<", "&lt;");
			}
			if (lines[i].contains("<=")) {
				lines[i] = lines[i].replaceAll("<=", "&le;");
			}
			if (lines[i].contains(">")) {
				lines[i] = lines[i].replaceAll(">", "&gt;");
			}
			if (lines[i].contains(">=")) {
				lines[i] = lines[i].replaceAll(">=", "&ge;");
			}
			if (lines[i].contains("\"")) {
				lines[i] = lines[i].replaceAll("\"", "&quot;");
			}

			if (lines[i].contains("'")) {
				lines[i] = lines[i].replaceAll("'", "&apos;");
			}
					
			 if (lines[i].contains("\\n")) { 
				 //System.out.println("matched"+lines[i]);
				 lines[i] = lines[i].replaceAll("\\\\n", "&#92;n"); 
				// System.out.println("matched statement"+lines[i]);
			}
			 if (lines[i].contains("\\0")) { 
				 //System.out.println("matched"+lines[i]);
				 lines[i] = lines[i].replaceAll("\\\\0", "&#92;0"); 
				// System.out.println("matched statement"+lines[i]);
			}
				if (lines[i].contains("/")) {
					lines[i] = lines[i].replaceAll("/", "&#47;");
				}
				/*
				 * if (lines[i].contains("\\n")) { lines[i] = lines[i].replaceAll("\\n",
				 * "&#10;"); }
				 */
			
			/*
			 * if (lines[i].contains("=")) { lines[i] = lines[i].replaceAll("=", "&#61;"); }
			 */
		}

		for (String s : lines) {
			/*
			 * if ( s.contains("#pragma loop name")) {
			 * sb.append("+\"<i style=\'color:orange\'>").append(s).append("</i>").append(
			 * "<br>\"") ; } else if ( s.contains("#pragma omp")) {
			 * sb.append("+\"<i style=\'color:blue\'>").append(s).append("</i>").append(
			 * "<br>\"") ; } else if ( s.contains("#pragma cetus")) {
			 * sb.append("+\"<i style=\'color:grey\'>").append(s).append("</i>").append(
			 * "<br>\"") ; }else if (!s.equals("")) {
			 * sb.append("+\"").append(s).append("<br>\""); }
			 */
			
			  if (!s.equals("") ) {
			  sb.append("+\"").append(s.trim()).append("<br>\"").append(System.getProperty("line.separator")); }
			 
		}

		return lines;

	}
	
	public String[] makeHTMLqualifiedNoTriming(String[] lines, StringBuilder sb) {

		for (int i = 0; i < lines.length; i++) {
			if (lines[i].contains("&")) {
				lines[i] = lines[i].replaceAll("&", "&amp;");
			}
			if (lines[i].contains("<")) {
				lines[i] = lines[i].replaceAll("<", "&lt;");
			}
			if (lines[i].contains("<=")) {
				lines[i] = lines[i].replaceAll("<=", "&le;");
			}
			if (lines[i].contains(">")) {
				lines[i] = lines[i].replaceAll(">", "&gt;");
			}
			if (lines[i].contains(">=")) {
				lines[i] = lines[i].replaceAll(">=", "&ge;");
			}
			if (lines[i].contains("\"")) {
				lines[i] = lines[i].replaceAll("\"", "&quot;");
			}

			if (lines[i].contains("'")) {
				lines[i] = lines[i].replaceAll("'", "&apos;");
			}
					
			 if (lines[i].contains("\\n")) { 
				 //System.out.println("matched"+lines[i]);
				 lines[i] = lines[i].replaceAll("\\\\n", "&#92;n"); 
				// System.out.println("matched statement"+lines[i]);
			}
			 if (lines[i].contains("\\0")) { 
				 //System.out.println("matched"+lines[i]);
				 lines[i] = lines[i].replaceAll("\\\\0", "&#92;0"); 
				// System.out.println("matched statement"+lines[i]);
			}
			 if (lines[i].contains("\\t")) { 
				 //System.out.println("matched"+lines[i]);
				 lines[i] = lines[i].replaceAll("\\\\t", "&#92;t"); 
				// System.out.println("matched statement"+lines[i]);
			}
				if (lines[i].contains("/")) {
					lines[i] = lines[i].replaceAll("/", "&#47;");
				}
				/*
				 * if (lines[i].contains("\\n")) { lines[i] = lines[i].replaceAll("\\n",
				 * "&#10;"); }
				 */
			
			/*
			 * if (lines[i].contains("=")) { lines[i] = lines[i].replaceAll("=", "&#61;"); }
			 */
		}

		for (String s : lines) {
			/*
			 * if ( s.contains("#pragma loop name")) {
			 * sb.append("+\"<i style=\'color:orange\'>").append(s).append("</i>").append(
			 * "<br>\"") ; } else if ( s.contains("#pragma omp")) {
			 * sb.append("+\"<i style=\'color:blue\'>").append(s).append("</i>").append(
			 * "<br>\"") ; } else if ( s.contains("#pragma cetus")) {
			 * sb.append("+\"<i style=\'color:grey\'>").append(s).append("</i>").append(
			 * "<br>\"") ; }else if (!s.equals("")) {
			 * sb.append("+\"").append(s).append("<br>\""); }
			 */
			
			  if (!s.equals("")) {
			  sb.append("+\"<pre>").append(s).append("</pre>\""); }
			 
		}

		return lines;

	}
	
	//to make URLEncoder.encode	- we create new lines and new stringbuilder give it lines and teh sb as the parameter & we get the string back out of it. two array of strings are like pointers for each other.any chnages to one would be reflected in the other one.
	//https://en.wikipedia.org/wiki/Percent-encoding			
	public String percentEncoding(String[] lines, StringBuilder sb) {
		
		for (int j = 0; j < lines.length; j++) {
 			//System.out.println(" \n beforeURICFG  " +  lines[j]);
 		
		if (lines[j].contains(" ")) {
			lines[j] = lines[j].replaceAll(" ", "%20");
		} 
		if (lines[j].contains("=")) {
			lines[j] = lines[j].replaceAll("\\=", "%3D");
		}
		 if (lines[j].contains("&quot;")) {
			lines[j] = lines[j].replaceAll("&quot;", "%22");
		}
		 if (lines[j].contains("&lt;")) {
			lines[j] = lines[j].replaceAll("&lt;", "%3C");
		}
		 if (lines[j].contains("&gt;")) {
			lines[j] = lines[j].replaceAll("&gt;", "%3E");
		}
		if (lines[j].contains("{")) {
			lines[j] = lines[j].replaceAll("\\{", "%7B");
		}

		 if (lines[j].contains("}")) {
			lines[j] = lines[j].replaceAll("\\}", "%7D");
		}
		 if (lines[j].contains("[")) {
			lines[j] = lines[j].replaceAll("\\[", "%5B");
		}
		 if (lines[j].contains("]")) {
			lines[j] = lines[j].replaceAll("\\]", "%5D");
		}
		 if (lines[j].contains(";")) {
			lines[j] = lines[j].replaceAll(";", "%3B");
		}
		 if (lines[j].contains(",")) {
			lines[j] = lines[j].replaceAll(",", "%2C");
		}
		 if (lines[j].contains("+")) {
			lines[j] = lines[j].replaceAll("\\+", "%2B");
		}
		 if (lines[j].contains("*")) {
			lines[j] = lines[j].replaceAll("\\*", "%2A");
		}
/* 		 if (lines[j].contains("(")) {
			lines[j] = lines[j].replaceAll("\\(", "%28");
		}
		 if (lines[j].contains(")")) {
			lines[j] = lines[j].replaceAll("\\)", "%29");
		} */
		 if (lines[j].contains("\\")) {
			lines[j] = lines[j].replaceAll("\\\\", "%5C");
		}
		 if (lines[j].contains("\"")) {
			lines[j] = lines[j].replaceAll("\"", "%22");
		}

		 if (lines[j].contains("<")) {
			lines[j] = lines[j].replaceAll("<", "%3C");
		}
		 if (lines[j].contains(">")) {
			lines[j] = lines[j].replaceAll(">", "%3E");
		}
		 //System.out.println(" \n AfterURICFG " +  lines[j]);
	} 
 
		for (String l : lines) {
			
			if (!l.equals("")) {
				sb.append(l);
			}
		}
		
				
		return sb.toString();
	}

//	returns all the set options except the -outdir and the input in this form: -preprocessor=cpp -C -I., -verbosity=4, 
	public StringBuilder seperateOptions(String Options, StringBuilder sb) {
		String subOptions = "";
		int fromindex = Options.indexOf(".,", 0);
		int toindex = Options.indexOf("-outdir", fromindex);
		subOptions = Options.substring(fromindex + 2, toindex - 1);
		System.out.println("\nsubOptions: " + subOptions);

		sb = new StringBuilder();
		sb.append("-preprocessor=cpp -C -I.,");
		fromindex = 0;
		toindex = 0;

		while (toindex < subOptions.length()-1 && fromindex >= 0) {
		  if (fromindex < 0 || fromindex > toindex || toindex > subOptions.length() || toindex < 0) {
			  System.out.println("Here are suboptions" + sb.toString());
		  } else { 
			fromindex = subOptions.indexOf("-", toindex);
			System.out.println(fromindex);
			toindex = subOptions.indexOf(",", fromindex);
			System.out.println(toindex);
			System.out.println(subOptions.substring(fromindex, toindex ));
			sb.append(" ").append(subOptions.substring(fromindex, toindex )).append(",");
		  }
		}
		System.out.println("Here are suboptions" + sb.toString());
		subOptions = sb.toString();
		return sb;

	}
	
	public void removeCommentsEmptyLines(String filePath) throws IOException {
		Path inputPath = Path.of(filePath);
		String inputContent = Files.readString(inputPath, StandardCharsets.US_ASCII);
		// remove one line comments except URLS
		inputContent = inputContent.replaceAll("^//.*", "");
		inputContent = inputContent.replaceAll("[^:]//.*", "");
		//System.out.println("File Content- one line comments replaced: " + inputContent);
		inputContent = inputContent.replaceAll("(?s)/\\*.*?\\*/", "");
		//System.out.println("File Content replacing multiple line comments: "+ inputContent);
		inputContent = inputContent.replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
		//System.out.println("File Content- empty lines replaced: " + inputContent);
		// writing file content back to the file.
		File inputfile = new File(filePath);
		inputfile.createNewFile();
		FileWriter writer = new FileWriter(inputfile);
		writer.write(inputContent);
		writer.flush();
		writer.close();
		//re-indent the code
		/*
		 * try { Process p = Runtime.getRuntime().exec("clang-format -i "+filePath); int
		 * exitCode = p.waitFor(); if (exitCode != 0) { throw new
		 * Exception("Clang format with exitcode" + exitCode); } } catch (Exception ex)
		 * { System.out.println("Clang Error Message: " + ex.toString()); }
		 */
		/* System.out.println("\n File located in " + filePath + " is overwritten."); */
	}
	
	public String removeCommentsEmptyLinesfromContent(String code){
		//replace single line comments //
		code = code.replaceAll("(?s)^//.*", ""); //removes patterns like //
		code = code.replaceAll("(?s)[^:]//.*", "");//remove comments not links like  http://
		//System.out.println("File Content- one line comments replaced: " + code);
		//replace single line and multiple line comments like /* */
		//code= code.replaceAll("/\\*.*?\\*/", "");
		//code= code.replaceAll("/\\*(?:.|[\\n\\r])*?\\*/","");
		code= code.replaceAll("(?s)/\\*.*?\\*/", "");
		//System.out.println("File Content replacing multiple line comments: "+ code);
		//replace empty lines
		code = code.replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
		//System.out.println("File Content- empty lines replaced: " + code);
		return code;
	}
	
    //This method extracts the response expected from chatgpt and returns it.
   public static String extractContentFromResponse(String response) {
       int startMarker = response.indexOf("content")+11; // Marker for where the content starts.
       int endMarker = response.indexOf("\"", startMarker); // Marker for where the content ends.
       return response.substring(startMarker, endMarker); // Returns the substring containing only the response.
   }
   
   
    static void modifyFile(String filePath, String oldString, String newString)
    {
        File fileToBeModified = new File(filePath);
         
        String oldContent = "";
         
        BufferedReader reader = null;
         
        FileWriter writer = null;
         
        try
        {
            reader = new BufferedReader(new FileReader(fileToBeModified));
             
            //Reading all the lines of input text file into oldContent
             
            String line = reader.readLine();
             
            while (line != null) 
            {
                oldContent = oldContent + line + System.lineSeparator();
                 
                line = reader.readLine();
            }
             
            //Replacing oldString with newString in the oldContent
             
            String newContent = oldContent.replaceAll(oldString, newString);
             
            //Rewriting the input text file with newContent
             
            writer = new FileWriter(fileToBeModified);
             
            writer.write(newContent);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                //Closing the resources
                 
                reader.close();
                 
                writer.close();
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
    }   

}