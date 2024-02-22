<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.regex.*"%>
<%@page import="java.nio.file.Path"%>
<%@page import="java.nio.file.Paths"%>
<%@page import="java.nio.file.Files"%>
<%@page import="cetus.registration.model.User"%>
<%@page import="cetus.registration.controller.UserServlet"%>
<%@page import="java.io.UnsupportedEncodingException"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.List" %>
<%@page import="cetus.registration.dao.CarvreplayDAO" %>
<%@page import="cetus.registration.model.Carvreplay" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>

<!-- Global site tag (gtag.js) - Google Analytics -->
<script async 	src="https://www.googletagmanager.com/gtag/js?id=G-J9XNTHF4C4"></script>
<script>
	window.dataLayer = window.dataLayer || [];
	function gtag() {
		dataLayer.push(arguments);
	}
	gtag('js', new Date());

	gtag('config', 'G-J9XNTHF4C4');
</script>

<link
	href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css"
	rel="stylesheet" type="text/css" />
	
<!-- <link rel="stylesheet" 	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" -->
<!-- 	integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" -->
<!-- 	crossorigin="anonymous"> -->

<!-- <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" -->
<!-- 	integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" -->
<!-- 	crossorigin="anonymous"></script> -->
	<!-- Pageloader	-->
<!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script> -->
<!-- 	for popovers -->
<!-- <script 	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" -->
<!-- 	integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" -->
<!-- 	crossorigin="anonymous"></script> -->
	
<!-- <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>  -->
	<!-- code prettifier -->
<!--  <script src="https://cdn.jsdelivr.net/gh/google/code-prettify@master/loader/run_prettify.js?lang=c&amp;skin=default"></script>  -->

<!-- <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script> -->
<!-- <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js"></script> -->
<!-- <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script> -->
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">


<!-- Bootstrap CSS and JavaScript files along with jQuery (which is required by Bootstrap) -->
<!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"> -->
<!-- <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script> -->
<!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script> -->
<!-- <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script> -->
<!-- <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script> -->

<style type="text/css">
.form-row, .form-group {
	display: block;
	margin-top: 0px;
	margin-bottom: 0px;
	margin-left: 15px;
	margin-right: 5px;
	padding: 2px;
}

.col-form-label {
	vertical-align: middle;
	font-size: 16px;
	width: 350px;
}

.col-form-label1 {
	vertical-align: middle;
	font-size: 16px;
	width: 450px;
}

.row {
	display: table;
	width: 100%;
	padding: 2px;
}

/* Create two equal columns that sits next to each other */
.column {
	flex: 49%;
	width: 50%;
	display: table-cell;
	padding: 5px;
	height: 500px;
}

.columnselect {
	/* flex: 49%; */
	width: 50%;
	display: table-cell;
	padding: 5px;
	height: 50px;
}

button {
	margin-left: 0px;
	margin-right: 0px;
	padding: 2px;
	width: 150px;
	
}
.buttonwide {

	width: 210px;
}

.buttonextrawide {

	width: 260px;
}

.boxsizingBorder {
	-webkit-box-sizing: border-box;
	-moz-box-sizing: border-box;
	box-sizing: border-box;
}

mark {
	background-color: yellow;
	color: black;
}

.highlight-purple {
	background-color: rgba(190, 89, 190, .25);
	padding: .1rem 0;
}

.highlight-green {
	padding: .1rem 0;
	background-color: rgba(22, 198, 179, .25);
}

.highlight-blue {
	padding: .1rem 0;
	background-color: rgba(31, 128, 232, .25);
}

.highlight-yellow {
	padding: .1rem 0;
	background-color: rgba(243, 186, 18, .25);
}

.highlighted-text {
    color: blue;
}

.highlighted-text-grey{
	color: grey;
}

span:target{color:blue;
font-weight: bold;
  }
</style>
<style>
#textarea , #formattedAnalysis , #cetusinput, #cetusoutput, #ExecutionResultDiv, #AdvisorDiv, #pluggedininput{
	word-wrap: break-word;
	cursor: text;
	overflow: auto;
	height: 415px;
	overflow: auto;
	resize: both;
	-moz-box-shadow: inset 0px 1px 2px #ccc;
	-webkit-box-shadow: inset 0px 1px 2px #ccc;
	/* box-shadow: inset 0px 1px 2px #ccc; */
	border-radius: 10px;
	padding: 25px;
	width: 50%;
	box-sizing: border-box;
	text-rendering: auto;
 	color: -internal-light-dark(black, white);
 	letter-spacing: normal;
	word-spacing: normal;
	text-transform: none;
	text-indent: 0px;
	text-shadow: none;
	display: inline-block;
	text-align: start;
	appearance: auto;
	background-color: #FFFFFF;
	-webkit-rtl-ordering: logical;
	flex-direction: column;
	font: 400 16px Arial;
	border-width: 1px;
	border-style: solid;
	border-color: -internal-light-dark(rgb(118, 118, 118),
		rgb(133, 133, 133));
	border-image: initial;
	line-height: 1.4;
/* 	white-space: pre-line; /* for /n */ */
}

#expsection, #replayExp, #replayPerf, #expsectionLLM{
	word-wrap: break-word;
	cursor: text;
	overflow: auto;
	height: 275px;
	resize: both;
	-moz-box-shadow: inset 0px 1px 2px #ccc;
	-webkit-box-shadow: inset 0px 1px 2px #ccc;
	/* box-shadow: inset 0px 1px 2px #ccc; */
	border-radius: 10px;
	padding: 15px;
	width: 50%;
	box-sizing: border-box;
	text-rendering: auto;
 	color: -internal-light-dark(black, white);
 	letter-spacing: normal;
	word-spacing: normal;
	text-transform: none;
	text-indent: 0px;
	text-shadow: none;
	display: inline-block;
	text-align: start;
	appearance: auto;
	background-color: #FFFFFF;
	-webkit-rtl-ordering: logical;
	flex-direction: column;
	font: 400 16px Arial;
	border-width: 1px;
	border-style: solid;
	border-color: -internal-light-dark(rgb(118, 118, 118),
		rgb(133, 133, 133));
	border-image: initial;
	line-height: 1.2;
/* 	white-space: pre-line; /* for /n */ */
}

#liveinexpsection, #liveoutexpsection{
	word-wrap: break-word;
	cursor: text;
	overflow: auto;
	height: 65px;
	overflow: auto;
	resize: both;
	-moz-box-shadow: inset 0px 1px 2px #ccc;
	-webkit-box-shadow: inset 0px 1px 2px #ccc;
	/* box-shadow: inset 0px 1px 2px #ccc; */
	border-radius: 10px;
	padding: 15px;
	width: 50%;
	box-sizing: border-box;
	text-rendering: auto;
 	color: -internal-light-dark(black, white);
 	letter-spacing: normal;
	word-spacing: normal;
	text-transform: none;
	text-indent: 0px;
	text-shadow: none;
	display: inline-block;
	text-align: start;
	appearance: auto;
	background-color: #FFFFFF;
	-webkit-rtl-ordering: logical;
	flex-direction: column;
	font: 400 14px Arial;
	border-width: 1px;
	border-style: solid;
	border-color: -internal-light-dark(rgb(118, 118, 118),
		rgb(133, 133, 133));
	border-image: initial;
	line-height: 1.2;
/* 	white-space: pre-line; /* for /n */ */
}
.container {
	counter-reset: line;
	    width: 100%;
    padding-right: 0px;
    padding-left: 0px;
    margin-right: auto;
    margin-left: auto;
}

.container .lineNum {
	display: block;
	line-height: 1.5rem;
}

.container .lineNum:before {
	counter-increment: line;
	content: counter(line);
	display: inline-block;
	margin-right: 0em;
}


/* pageloader */
#pageloader
{
  background: rgba( 255, 255, 255, 0.8 );
  display: none;
  height: 100%;
  position: fixed;
  width: 100%;
  z-index: 9999;
}

#pageloader img
{
  left: 50%;
  margin-left: -32px;
  margin-top: -32px;
  position: absolute;
  top: 50%;
}
#pageloader label
{
  left: 42%;
  margin-left: -32px;
  margin-top: -32px;
  position: absolute;
  top: 60%;
  font-weight:bold;
}
</style>


<%
	String pathWebcontent=request.getContextPath();
%>

 <link rel="stylesheet" type="text/css" href="<%=pathWebcontent%>/resources/css/prettify.css" />
<script type="text/javascript" src="<%=pathWebcontent%>/resources/js/prettify.js"></script>

<!--put line numbers on every line instead of just every fifth line  -->
<style>


.pln { color: #000 }  /* plain text */

@media screen {
  .str { color: #080 }  /* string content */
  .kwd { color: #008 }  /* a keyword */
  .com { color: #800 }  /* a comment */
  .typ { color: #606 }  /* a type name */
  .lit { color: #066 }  /* a literal value */
  /* punctuation, lisp open bracket, lisp close bracket */
  .pun, .opn, .clo { color: #660 }
  .tag { color: #008 }  /* a markup tag name */
  .atn { color: #606 }  /* a markup attribute name */
  .atv { color: #080 }  /* a markup attribute value */
  .dec, .var { color: #606 }  /* a declaration; a variable name */
  .fun { color: red }  /* a function name */
}

/* Use higher contrast and text-weight for printable form. */
@media print, projection {
  .str { color: #060 }
  .kwd { color: #006; font-weight: bold }
  .com { color: #600; font-style: italic }
  .typ { color: #404; font-weight: bold }
  .lit { color: #044 }
  .pun, .opn, .clo { color: #440 }
  .tag { color: #006; font-weight: bold }
  .atn { color: #404 }
  .atv { color: #060 }
}

/* Put a border around prettyprinted code snippets. */
/* code.prettyprint { padding: 2px; border: 1px solid #888 } */
code{color: black}

.formfield * {
  vertical-align: middle;
}

.tooltip {
	position: relative;
	display: inline-block;
	border-bottom: 1px dotted black;
}

.tooltip .tooltiptext {
	visibility: hidden;
	width: 120px;
	background-color: #555;
	color: #fff;
	text-align: center;
	border-radius: 6px;
	padding: 5px 0;
	position: absolute;
	z-index: 1;
	bottom: 125%;
	left: 50%;
	margin-left: -60px;
	opacity: 0;
	transition: opacity 0.3s;
}

.tooltip .tooltiptext::after {
	content: "";
	position: absolute;
	top: 100%;
	left: 50%;
	margin-left: -5px;
	border-width: 5px;
	border-style: solid;
	border-color: #555 transparent transparent transparent;
}

.tooltip:hover .tooltiptext {
	visibility: visible;
	opacity: 1;
}

.collapsible-section {
      margin-bottom: 10px;
}

        .collapsible-content {
            display: none;
        }
</style>
<!-- line numbers on every line
 --><!-- <style>
li.L0, li.L1, li.L2, li.L3,
li.L5, li.L6, li.L7, li.L8 {
  list-style-type: decimal !important;
}
</style> -->
	

<html>
<head>
<meta charset="ISO-8859-1">

<title>CaRV Output</title>

<%
	//the user will just see the processed html file. nothing about database connection details is shown to the user.
User user = new User();

UserServlet servlet = new UserServlet();

// String id = request.getParameter("userid");
int generatedKey = 0;
String driver = "com.mysql.cj.jdbc.Driver";
String connectionUrl = "jdbc:mysql://localhost:3306/";
String database = "users";
String userid = "root";
String password = "Bezanberim";
try {
	Class.forName(driver);
} catch (ClassNotFoundException e) {
	e.printStackTrace();
}
Connection connection = null;
Statement statement = null;
ResultSet resultSet = null;


try {
	connection = DriverManager.getConnection(connectionUrl + database, userid, password);
	statement = connection.createStatement();
	String sql = "select * from users.user ORDER BY id DESC LIMIT 1;";
	
	resultSet = statement.executeQuery(sql);
	
	if (resultSet.next()) {
		generatedKey = resultSet.getInt(1);
		System.out.println("Here is the DB row id: "+ generatedKey);
	}
	
// database = 	"carvreplays";
// ResultSet resultSetReplays = null;
// 		connection = DriverManager.getConnection(connectionUrl + database, userid, password);
// 		statement = connection.createStatement();
// 		// Execute the query to fetch replays
// 		String replaysql = "select * from users.carvreplays WHERE user_id ="+ generatedKey+" ORDER BY carv_replay_id DESC ;";
// 		resultSetReplays = statement.executeQuery(replaysql);
// 		while (resultSet.next()) {
// 			int id = resultSetReplays.getInt("carv_replay_id"); // Replace with your actual column name
// 	        String replayExperimentalSection = resultSetReplays.getString("experimental_section"); // Replace with your actual column name
// 	        String replayExecutionResults = resultSetReplays.getString("execution_results");
// 		}	
%>
<!-- Since all the replays are still related to the initial CaRV process, we are holding that process Id -->
<c:set var="userId" value="<%=generatedKey%>" /> <!-- Replace with your actual user ID. saving user ID as an attribute in the page context --> 

<%
    List<Carvreplay> replayList = CarvreplayDAO.getReplays((int) pageContext.getAttribute("userId"));
	//Set a Java variable
    pageContext.setAttribute("replayList", replayList);	
	
%>

<!-- Iterate through replayList and generate hiddenHTML fields dynamically -->
    <%
        for (Carvreplay replay : replayList) {
    %>
            <div id="replayDetails_<%= replay.getReplayid() %>" style="display: none;">
                <div>
                    <label for="replayId_<%= replay.getReplayid() %>">Replay ID:</label>
                    <input type="text" id="replayId_<%= replay.getReplayid() %>" value="<%= replay.getReplayid() %>" readonly>
                </div>
                <div>
                    <label for="fileContent_<%= replay.getReplayid() %>">File Content:</label>
                    <textarea id="fileContent_<%= replay.getReplayid() %>" readonly><%= replay.getReplayfilecontent() %></textarea>
                </div>
                <div>
                    <label for="expContent_<%= replay.getReplayid() %>">Experimental Section:</label>
                    <textarea id="expContent_<%= replay.getReplayid() %>" readonly><%= replay.getReplayexpsection() %></textarea>
                </div>
                <div>
                    <label for="executionContent_<%= replay.getReplayid() %>">Execution Result:</label>
                    <textarea id="executionContent_<%= replay.getReplayid() %>" readonly><%= replay.getReplayexecutionresults() %></textarea>
                </div>
                <!-- Add more fields as needed -->
            </div>
    <%
        }
    %>

<script>
    // Set replayList as a global variable
    var replayList = ${JSON.stringify(replayList)};// ${replayList}; // Assuming replayList is a JSON array // ${replayList}; 
</script>

<%

String gptOutput=(String)request.getAttribute("gptResponse");
if(gptOutput!= null){
gptOutput= gptOutput.replace("\\n", "<br>");
}
request.setAttribute("gptOutput", gptOutput);//when gpt is executed on the exp section

Double ResponseTime= (Double)request.getAttribute("ResponseTime");
if (ResponseTime == null) {
    ResponseTime = null; // or whatever default value you want to set
}
String carvExpSection= (String)request.getAttribute("carvExpSection");
System.out.println("\n\n\n carvExpSection  "+carvExpSection);
//int ReplayIndex=0;
//reads from the files saved on the server -These are all file paths that we need for redaing them and writing to them in case of recompiling
String inputContent = user.getFileOutput(resultSet.getString("input_code"));
//System.out.println("\n\n\n inputContent"+inputContent);
request.setAttribute("inputContent", inputContent);//To be used in JavaScript code		
		
String outputContent = user.getFileOutput(resultSet.getString("cetus_output_filepath"));
//System.out.println("\n\n\n outputContent"+outputContent);
//reading Analysis file content from the server not the Database file content.
String analysisContent = user.getFileOutput(resultSet.getString("cetus_Analysis_filepath"));
String analysis2=resultSet.getString("cetus_Analysis_content");//why I am saving analysis content in another string ? for teh performance results
//Path analysisCetus = Path.of(resultSet.getString("cetus_Analysis_filepath"));
//String analysisContent = Files.readString(analysisCetus);
String passesContent = user.getFileOutput(resultSet.getString("cetus_passes_filepath"));
//String analysis2=(resultSet.getString("cetus_Analysis_content"));
String cetusOptions= (resultSet.getString("cetus_option_set"));
//System.out.println("\n\n\n cetusOptions set"+cetusOptions);
//System.out.println("analysis content from DB not from the file: "+analysis2);
String inputPath = resultSet.getString("input_code");

//get the filename from the file path
Path p = Paths.get(inputPath);
String file = p.getFileName().toString();
//System.out.println("\n\n \n inputfile name:" + file);

StringBuilder sb= new StringBuilder();
sb = servlet.seperateOptions(cetusOptions, sb);
String allOptions= sb.toString();
//System.out.println("\n\n \n AllOptions:" + sb.toString());


String inputFinalString = null;
String cetusOutContenet = null;
String FormatedAnalysis = null;
String DDTfinalString = null;
String outputFinalString = null;
String outputcetusFinalString = null;
String ExperimentalSectionFinalString= null;
String ExperimentalSectionInputFinalString=null;
String extractedText=null;
String extractedInputText=null;
String expFinalString =null;
// String RangefinalString = null;
// String cfgfinalString = null;
// String ipafinalString = null;
// String reductionfinalString = null;
// String branchEfinalString = null;
// String callGfinalString = null;
// String ivfinalString = null;
// 
// 
// String privatefinalString = null;
// String outputContent1 = null;
// String cfghelp = null;
// String callGhelp =null;
// String ddthelp= null;
// String callgOutput= null;
// String profilerhelp= null;
// String exeResulthelp=null;
%>
<%
/*Cetus Input in div section =========================================================================================*/
System.out.println("Creating Cetus input");
StringBuilder finalinputStringBuilder = new StringBuilder("");
String[] inlines = inputContent.split("\n");
inlines= servlet.makeHTMLqualified(inlines,finalinputStringBuilder);
inputFinalString = finalinputStringBuilder.toString();
request.setAttribute("inputFinalString", inputFinalString);//To be used in JavaScripot code		
System.out.println("\n\n \n inputFinalString:" + inputFinalString);
System.out.println("Cetus input created");

//make carvExpSection ready for html view===============================================================================
if(carvExpSection !=null){
System.out.println("Creating CarvExpSection");
System.out.println("carvExpSection: "+ carvExpSection);
StringBuilder finalExpStringBuilder = new StringBuilder("");
String[] Explines = carvExpSection.split("\n");
Explines= servlet.makeHTMLqualified(Explines,finalExpStringBuilder );
expFinalString = finalExpStringBuilder.toString().substring(1); //removing the first +
request.setAttribute("expFinalString", expFinalString);//To be used in JavaScripot code		
//expFinalString= "+\""+expFinalString+"\""; 
System.out.println("\n\n \n expFinalString:" + expFinalString);
System.out.println("carvExpSection created");
}else{request.setAttribute("expFinalString", null);}

/* Cetus Output for queries, div section ===================================================================================== */
System.out.println("Creating Cetus output");
String outputDivString = null;
StringBuilder finaldivStringBuilder = new StringBuilder("");
String outputContent2 = null;
String option = null;

//replaces all empty lines
outputContent2 = outputContent.replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
//System.out.println("\n\n \n outputcontent:" + outputContent);
String[] dlines = outputContent2.split("\n");
//Make it HTML Ready

 for (int i = 0; i < dlines.length; i++) {
	if (dlines[i].contains("&")) {
		dlines[i] = dlines[i].replaceAll("&", "&amp;");
	}
	if (dlines[i].contains("<")) {
		dlines[i] = dlines[i].replaceAll("<", "&lt;");
	}
	if (dlines[i].contains("<=")) {
		dlines[i] = dlines[i].replaceAll("<=", "&le;");
	}
	if (dlines[i].contains(">")) {
		dlines[i] = dlines[i].replaceAll(">", "&gt;");
	}
	if (dlines[i].contains(">=")) {
		dlines[i] = dlines[i].replaceAll(">=", "&ge;");
	}
	if (dlines[i].contains("\"")) {
		dlines[i] = dlines[i].replaceAll("\"", "&quot;");
	}
	if (dlines[i].contains("'")) {
		dlines[i] = dlines[i].replaceAll("'", " &apos;");
	}	
	 if (dlines[i].contains("\\n")) { 
		 //System.out.println("matched"+lines[i]);
		 dlines[i] = dlines[i].replaceAll("\\\\n", "&#92;n"); 
		// System.out.println("matched statement"+lines[i]);
	}
	
 	if (dlines[i].contains("\\0")) { 
	 //System.out.println("matched"+lines[i]);
	 dlines[i] = dlines[i].replaceAll("\\\\0", "&#92;0"); 
	// System.out.println("matched statement"+lines[i]);
	}

	} 

for (String s : dlines) {
	if ( s.contains("#pragma loop name")) {
		finaldivStringBuilder.append("+\"<i style=\'color:orange\'>").append(s).append("</i>").append("<br>\"")
		.append(System.getProperty("line.separator"));
	} else if ( s.contains("#pragma omp")) {
		finaldivStringBuilder.append("+\"<i style=\'color:blue\'>").append(s).append("</i>").append("<br>\"")
		.append(System.getProperty("line.separator"));
	} else if ( s.contains("#pragma cetus")) {
		finaldivStringBuilder.append("+\"<i style=\'color:grey\'>").append(s).append("</i>").append("<br>\"")
		.append(System.getProperty("line.separator"));
	}else if (!s.equals("")) {
		finaldivStringBuilder.append("+\"").append(s).append("<br>\"").append(System.getProperty("line.separator"));
	}
}

outputDivString = finaldivStringBuilder.toString();
//System.out.println("\n\n \n div scetion content :" + outputDivString);
System.out.println("Cetus output created");

//Extract the Experimental Section from the Output in code element===============================================================================================
System.out.println("Output String: "+ outputDivString);

int expStartIdx = outputDivString.indexOf("gettimeofday(&amp;startexp, NULL);", 0);
System.out.println("index of the start of the experimental section is: "+ expStartIdx);
int expStopIdx = outputDivString.indexOf("#pragma experimental section stop", expStartIdx);
System.out.println("index of the end of the experimental section is: "+ expStartIdx);

if (expStartIdx >0 && expStopIdx >0) {
    // Ensure endIndex is greater than startIndex
    if (expStopIdx > expStartIdx && expStopIdx <= outputDivString.length()) {
        // Extract the text between the specified indices
        extractedText = outputDivString.substring((expStartIdx+38), (expStopIdx-1)).trim();

        // Print the extracted text
        System.out.println("Extracted Text:" + extractedText);
        ExperimentalSectionFinalString="+\""+extractedText+"\"";   //This is the Experimental section 
        
    } else {
    	ExperimentalSectionFinalString="+ \"The user has not specified the correct placement of the start and end pragmas in the input code to define the experimental section.\""; 
        System.out.println("Invalid indices. End index should be greater than start index and within the string length.");
    }
} else {
	ExperimentalSectionFinalString="+ \"The user has not specified an experimental section in the input code.\""; 
    System.out.println("The user has not specified an experimental section.");
}

//Extract the Experimental Section from the Input in code elemnet===============================================================================================

// System.out.println("Input String: "+ inputFinalString );

// int expInputStartIdx = inputFinalString.indexOf("#pragma experimental section start", 0);
// System.out.println("index of the start of the experimental section in Input is: "+ expInputStartIdx);
// int newlineIndex = inputFinalString.indexOf("\n", expInputStartIdx);
// int StartLength= newlineIndex - expInputStartIdx;
// System.out.println("Length is: "+ StartLength); //length of the start of the experimental section

// int expInputStopIdx = inputFinalString.indexOf("#pragma experimental section stop", expInputStartIdx);
// System.out.println("index of the end of the experimental section in Input is: "+ expInputStopIdx);

// if (expInputStartIdx >0 && expInputStopIdx >0) {
//     // Ensure endIndex is greater than startIndex
//     if (expInputStopIdx > expInputStartIdx && expInputStopIdx <= inputFinalString.length()) {
//         // Extract the text between the specified indices
//         extractedInputText = inputFinalString.substring((expInputStartIdx+StartLength), (expInputStopIdx-2)).trim();

//         // Print the extracted text
//         System.out.println("Extracted Experimental section in Input Text: " + extractedInputText );
//         ExperimentalSectionInputFinalString="+\""+extractedInputText+"\"";   //This is the Experimental section 
        
//     } else {
//     	ExperimentalSectionInputFinalString="+ \"The user has not specified the correct placement of the start and end pragmas in the input code to define the experimental section.\""; 
//         System.out.println("Invalid indices. End index should be greater than start index and within the string length.");
//     }
// } else {
// 	ExperimentalSectionFinalString="+ \"The user has not specified an experimental section in the input code.\""; 
//     System.out.println("The user has not specified an experimental section.");
// }


//Extract the live-out variables from the Output=================================================================================================

String liveOutExtractedLine =null;
String startString= "#pragma experimental section stop";
int startexpIdx = outputDivString.indexOf(startString);
//if the line is found
if (startexpIdx != -1) {
    // Find the end of the line
    int endexpIdx = outputDivString.indexOf('\n', startexpIdx + startString.length());

    // If the end of the line is found, extract the substring
    if (endexpIdx != -1) {
    	//extract the live-out section
    	liveOutExtractedLine = "\""+outputDivString.substring(startexpIdx, endexpIdx).trim();
    }else{
    	liveOutExtractedLine = "";
    }
}

//Extract the live-in variables from the Output=================================================================================================

String liveInExtractedLine =null;
String startexpString= "#pragma experimental section start";
int startexpIndex = outputDivString.indexOf(startexpString);
//if the line is found
if (startexpIndex != -1) {
  // Find the end of the line
  int endexpIndex = outputDivString.indexOf('\n', startexpIndex + startexpString.length());

  // If the end of the line is found, extract the substring
  if (endexpIndex  != -1) {
  	//extract the live-out section
  	liveInExtractedLine = "\""+outputDivString.substring(startexpIndex, endexpIndex).trim();
  }else{
	liveInExtractedLine = "";
  }
}
//extract Execution Results from the cetus_Analysis_content======================================================================================//
System.out.println("Analysis Results "+analysis2);
String exeResultfinalString=null;
StringBuilder exeResultfinalStringBuilder = new StringBuilder("");
//int exeresfromidx = analysis2.indexOf("[Capture]", 0);

int captureIndex = analysis2.indexOf("[Capture]", 0);
int indexIndex = analysis2.indexOf("[Input]", 0);
//the execution result can start with Input of with Capture, it can also include both of these , or none of these.
int exeresfromidx=0;  //from index. It can start with [Capture] or [Input]
if (captureIndex >0 && indexIndex <0) {
    exeresfromidx = captureIndex;
} else if (indexIndex >0 && captureIndex <0) {
    exeresfromidx = indexIndex;
}else if (captureIndex >0 && indexIndex >0){
	exeresfromidx = Math.min(captureIndex, indexIndex);
}else if (captureIndex <0 && indexIndex <0){
	exeresfromidx=-1;
}

if (exeresfromidx < 0 ) {
	exeResultfinalString = "\nExecution result will be provided here.\n";
	//System.out.println("\n\n\nExecution result report \t"+exeResultfinalString);
	//exeResulthelp= "+\" \"";
	FormatedAnalysis= exeResultfinalString;
} else {
	FormatedAnalysis = analysis2.substring(exeresfromidx );
	//exeResulthelp= "+\"The result displayed here is what is printed on standard output after compiling and running the serial code. \"";
	System.out.println("\n\n\nExecution result report \t"+FormatedAnalysis);
}
//System.out.println("\n \n\n Before changes Execution: " + exeResultfinalString);
//save it line by line
String[] exereslines = FormatedAnalysis.split("\n");
exereslines= servlet.makeHTMLqualifiedNoTriming(exereslines,exeResultfinalStringBuilder);

exeResultfinalString = exeResultfinalStringBuilder.toString();
System.out.println("\n \n\n Execution: " + exeResultfinalString);
%>

<script>

	function load(){
		var divInput= document.getElementById("cetusinput");
		divInput.innerHTML = ""<%=inputFinalString%>; 
		var divoutput= document.getElementById("cetusoutput");
		divoutput.innerHTML = ""<%=outputDivString%>; 
		/* prettify the code */
		PR.prettyPrint();
				
</script>
</head>
<body  onload="load()"  Style="background-color: rgba(192, 192, 192, 0.2);">
<% 
Path inputFilePath= Paths.get(resultSet.getString("input_code"));
System.out.println("inputFilePath "+inputFilePath);

String inputFileName= inputFilePath.getFileName().toString();
System.out.println("inputFileName "+inputFileName);

String pathToInputFile= resultSet.getString("input_code").replace(inputFileName, "");
System.out.println("pathToFile "+pathToInputFile);

session.setAttribute("inputnamePara", inputFileName); //original input is in the inputFileName. pluggedinInput file is named modifiedcode.c
session.setAttribute("inputpathPara",pathToInputFile);


Path outputFilePath= Paths.get(resultSet.getString("cetus_output_filepath"));
System.out.println("outputFilePath "+outputFilePath);

String outputFileName= outputFilePath.getFileName().toString();
System.out.println("outputFileName "+outputFileName);

String pathToFile= resultSet.getString("cetus_output_filepath").replace(outputFileName, "");
System.out.println("pathToFile "+pathToFile);

session.setAttribute("filenamePara", outputFileName);
session.setAttribute("filepathPara",pathToFile);

//set analysis file
Path analysisFilePath= Paths.get(resultSet.getString("cetus_Analysis_filepath"));
System.out.println("analysisFilePath "+analysisFilePath);

String analysisFileName= analysisFilePath.getFileName().toString();
System.out.println("analysisFileName "+analysisFileName);

String analysispathToFile= resultSet.getString("cetus_Analysis_filepath").replace(analysisFileName, "");
System.out.println("analysispathToFile "+analysispathToFile);

session.setAttribute("analysisfilenamePara", analysisFileName);
session.setAttribute("analysisfilepathPara", analysispathToFile);

//set passes file
Path passesFilePath= Paths.get(resultSet.getString("cetus_passes_filepath"));
System.out.println("passesFilePath "+passesFilePath);

String passesFileName= passesFilePath.getFileName().toString();
System.out.println("passesFileName "+passesFileName);

String passespathToFile= resultSet.getString("cetus_passes_filepath").replace(passesFileName, "");
System.out.println("passespathToFile "+passespathToFile);

session.setAttribute("passesfilenamePara", passesFileName);
session.setAttribute("passesfilepathPara", passespathToFile);

/* request.setAttribute("filenamePara", outputFileName);
request.setAttribute("filepathPara",pathToFile);  */
%>
	<div id="pageloader">
		<img id="pageloaderimg"
			src="<%=pathWebcontent%>/resources/img/bubble.gif" width=auto
			height=auto frameBorder="0" alt="processing..." /> <label
			id="pageloaderimg" for="pageloaderimg">Please be patient
			while the process is being completed...</label>
	</div>
	<nav class="navbar navbar-dark bg-primary">
		<a class="navbar-brand" href="#">
			<p style="font-size: 24px;">
				<img src="<%=request.getContextPath()%>/resources/img/iCetus.png"
					width="70" height="70"
					Style="vertical-align: middle; margin: 10px 10px;"
					class="d-inline-block " alt="iCetus"> iCetus, A Source-to-Source Compiler Infrastructure for C Programs
			</p>
		</a>
	</nav>
	<br>

	<button type="button" class="btn btn-primary"
		style="align: left; margin-left: 20px;"
		onclick="location.href = '<%=request.getContextPath()%>/';">Back</button>
	<button type="button" class="btn btn-primary" id="cetusFlags"
		style="align: right; margin-right: 20px; margin-left: auto; float: right;"
		onclick="cetus_report()">Show Flags</button>
	<br>

	<form id="outputForm" autocomplete="off" action="<%=request.getContextPath()%>/" method="post" enctype="multipart/form-data">
		<table style="width: 100%">
			<p class="formfield">
			<div class="row" id="cetus_row" style="display: none;">
				<label for="cetusOptions"
					style="margin: 5px 40px; font-weight: bold;">Cetus Options:</label>
				<textarea id="cetusOptions" name="cetusOptions" rows="2"
					style="width: 98%; margin-left: 40px;"><%=allOptions%></textarea>
			</div>
			<%--<%=allOptions %>  --%>

			<!--Set Recompile parameters  -->
			<input type="hidden" id="usercode" name="usercode">
			<!--  modified input code-->
			<input type="hidden" id="codeRadios" name="codeRadios" value="code">
			<!-- type of file -->
			<input type="hidden" id="gridRadios" name="gridRadios"
				value="semi-auto">
			<!-- parallelization type -->
			<input type="hidden" id="allCetusOptions" name="allCetusOptions"
				value="<%=allOptions%>">
			<input type="hidden" id="executeOnly" name="executeOnly" value="">
			
			<!-- CaRVPhase refers to the phase that the code is going through. It can be Capture,Replay, Input. -->
			<input type="hidden" id="CaRVPhase" name="CaRVPhase" value="">
			
			<input type="hidden" id="DBRowId" name="DBRowId" value="<%=generatedKey%>">
			
			<input type="hidden" id="expsectionpara" name="expsectionpara"
				value="">
			<input type="hidden" id="liveinpara" name="liveinpara"
				value="">
			<input type="hidden" id="liveoutpara" name="liveoutpara"
				value="">	
			<!-- if the code should only be executed and not compiled set the vale to execute -->
			<%-- 	     <input type="hidden" id="filenamePara" name="filenamePara" value="<%=outputFileName %>">
	     <input type="hidden" id="filepathPara" name="filepathPara" value="<%=pathToFile %>"> --%>
			<%-- <%=allOptions%> --%>
			</p>

			<div align="center">

				<!-- --------------------  -->
				<!-- CaRV Input & Output  -->
				<!-- --------------------  -->
				<div class="row">
					<div class="column">
						<div class="form-row">
							<div align="left">
								Database Entry ID:
								<%=resultSet.getInt("id")%></div>
						</div>
						<div class="form-row">
							<div align="left">
								<h4 id="modifiableInput">
									Input File (<%=file%>)
								</h4>
							</div>
						</div>
						<div class="form-row">
							<div align="left">
								<div class="container">
									<!-- class="prettyprint lang-c" -->
									<!--  								<code  id="cetusinput" name="cetusinput" contentEditable=true rows="12" onclick="EnableCompileButtons()" contentEditable
										style="border-radius: 10px; width: 100%; padding: 25px; box-sizing: border-box;" onclick="EnableCompileButtons()">  -->
									<code class="prettyprint lang-c" id="cetusinput" rows="12"
										style="border-radius: 10px; width: 100%; padding: 25px; box-sizing: border-box; max-width: 47vw; overflow: scroll;">
									</code>
									<code class="prettyprint lang-c" id="pluggedininput" rows="12"
										style="border-radius: 10px; width: 100%; padding: 25px; box-sizing: border-box; max-width: 47vw; overflow: scroll;">
									</code>
								</div>
							</div>
						</div>
					</div>

					<div class="column">
						<div class="form-row">
							<div align="left">
							    <br>
							</div>
						</div>
						<div class="form-row">
							<div align="left">
								<h4 id="modifiableOutput">Experimental Section</h4> <h6 id="ShowgptResponseTime"><%=ResponseTime%></h6>
							</div>
						</div>
						<div class="form-row">
							<div align="left">
								<div class="container">
									<code class="prettyprint lang-c" id="cetusoutput" rows="12"
										style="border-radius: 10px; width: 100%; padding: 25px; box-sizing: border-box;">
									</code>
									<code class="highlighted-text" id="liveinexpsection" rows="3"
										style="border-radius: 10px; width: 100%; padding: 10px; box-sizing: border-box;">
									</code>
									<code class="prettyprint lang-c" id="expsection" rows="8"
										style="border-radius: 10px; width: 100%; padding: 10px; box-sizing: border-box;">
									</code>
									<code class="highlighted-text" id="liveoutexpsection" rows="3"
										style="border-radius: 10px; width: 100%; padding: 10px; box-sizing: border-box;">
									</code>

								</div>
							</div>
						</div>
					</div>
				</div>


				<!-- --------------------------  -->
				<!--  Execution Buttons  -->
				<!-- --------------------------  -->
				<div class="row">

					<div class="columnselect">
					<a id="downloadinput" href="<%=pathWebcontent%>/downloadInput.jsp">Download Input file</a>
					<a id="downloadpluggedinput" href="<%=pathWebcontent%>/downloadPluggedInput.jsp">Download Input file</a>
						<div class="form-row">
							<!-- same name has been assigned to get the parameter on the servlet section. input code should be rewritten- cetus options in case of ReCompile should be passed.
 -->
 							<button type="submit" id="executeInput" class="btn btn-primary"
								onclick="setInputparasforservlet()">Run Input</button>
							&nbsp;<span><i data-content="To execute the input code."
								data-placement="top" class="fa fa-question-circle"></i></span>
								
							<button type="button" id="copyToInput" class="btn btn-primary buttonwide"
								onclick="copytoInputFunction()">Copy Exp section into Input</button>
							&nbsp;<span><i data-content="To copy the experimental section to the input code."
								data-placement="top" class="fa fa-question-circle"></i></span>
								
<!-- 							<button type="button" id="modifyInput" class="btn btn-primary" -->
<!-- 								onclick="changeInput()">Modify Input</button> -->
<!-- 							&nbsp;<span><i data-content="To modify the input code." -->
<!-- 								data-placement="top" class="fa fa-question-circle"></i></span> -->
<!-- 							<button type="submit" id="ReCompile" name="action" -->
<!-- 								value="ReCompile" class="btn btn-primary" -->
<!-- 								onclick="setParasforservlet()">ReCompile</button> -->
<!-- 							&nbsp;<span><i id="helpReCompile" -->
<!-- 								data-content="Compiling the modified code with the same parallelization options." -->
<!-- 								data-placement="top" class="fa fa-question-circle"></i></span> -->
<!-- 							<button type="submit" id="NewCompile" name="action" -->
<!-- 								value="NewCompile" class="btn btn-primary" -->
<!-- 								onclick="setParasforIndex()">New Compilation</button> -->
<!-- 							&nbsp;<span><i id="helpNewCompile" -->
<!-- 								data-content="Compiling the modified code with a new set of parallelization options." -->
<!-- 								data-placement="top" class="fa fa-question-circle"></i></span> <br> -->
<!-- 							<span align="left" id="codeModified" style="margin-left: 0px;"></span> -->
						</div>
					</div>
					</form>

					<div class="columnselect">

						<a href="<%=pathWebcontent%>/download.jsp">Download output file</a>
	
						<div class="form-row">
							<div align="left">
								
								<button type="submit" id="Capture" name="action" value="Capture"
									class="btn btn-primary" onclick="setCaptureParasforservlet()">Capture</button>
								&nbsp;<span><i id="helpCapture"
									data-content="Compile and execute the output file in Capture mode."
									data-placement="top" class="fa fa-question-circle"></i></span>

								<button type="button" id="modifyExpSection"
									class="btn btn-primary buttonwide" onclick="changeExpSection()">Modify
									Exp Section</button>
								&nbsp;<span><i
									data-content="Use this button to manually further optimize the experimental section of the code. Then run the code in Replay mode to check its validity."
									data-placement="top" class="fa fa-question-circle"></i></span>

								<button type="submit" id="Replay" name="action"
									value="Replay" class="btn btn-primary buttonwide"
									onclick="setReplayParasforservlet()">Replay & Validate</button>
								&nbsp;<span><i id="helpReplay"
									data-content="Compile and execute the experimental section in Replay mode. It also reports on the verification of the applied optimization."
									data-placement="top" class="fa fa-question-circle"></i></span>
							</div>
						</div>

						<div class="form-row">
							<div align="left">
								<button type="submit" id="AskCetus" name="action" value="AskCetus"
									class="btn btn-primary" onclick="setDefaultCetusParasforservlet()">Ask Cetus</button>
								&nbsp;<span><i id="helpAskCetus"
									data-content="Cetus Auto-parallelizer offers parallelization techniques applicable to the experimental section."
									data-placement="top" class="fa fa-question-circle"></i></span>
									
								<button type="submit" id="AskGPT" name="action" value="AskGPT"
									class="btn btn-primary" onclick="setPromptParasforservlet()">Ask GPT</button>&nbsp;<span><i id="helpAskGPT"
									data-content="GPT offers optimization techniques applicable to your code."
									data-placement="top" class="fa fa-question-circle"></i></span>
								<!-- to use the API use this function "setPromptParasforservlet()" -->
								
								<button type="button" id="displayoutput" class="btn btn-primary buttonwide" onclick="displayOutputFunction()">Display Output</button>&nbsp;<span>
								<i id="helpdisplayoutput" data-content="Use this button to chcek out the generated Output file."
									data-placement="top" class="fa fa-question-circle"></i></span>
							</div>
						</div>
					</div>
				</div>
				<!-- --------------------------  -->
				<!-- Obtained Results and  and Replay History    
				<!-- --------------------------  -->

						<div class="row" id="queryResult" >

							<div class="column" style="max-width: 50%;">
								<div class="form-row">
									<div align="left">
										<h4>Obtained Results</h4>
									</div>
								</div>
								<div class="form-row">
									<div align="left">
										<div class="container">
											<div id="ExecutionResultDiv" contentEditable=false rows="12"
												style="border-radius: 10px; width: 100%; max-width: 47vw; padding: 25px; box-sizing: border-box; overflow: scroll;white-space: pre-wrap; word-wrap: break-word;">
											</div>
										</div>
									</div>
								</div>
								</div>
							<div class="column" >
								<div class="form-row">
									<div align="left">
										<h4>Replay History</h4>
									</div>
								</div>
								<div class="form-row">
								<div align="left">
								<label for="replaySelector">Select a Replay:</label> 
							    <select id="replaySelector" onclick="updateReplayDetails()">
							        <!-- Populate the dropdown dynamically based on the data retrieved from the database -->
								    <c:forEach var="replay" items="${replayList}">
								        <option value="${replay.replayid}">
								        	${replay.replayid}
								        </option> 
								    </c:forEach>
							    </select>	
							    </div>
							    <div  align="left" class="form-row">
							    	<code class="prettyprint lang-c" id="replayExp" rows="10"
										style="border-radius: 10px; width: 100%; padding: 10px; box-sizing: border-box; height: 380px;">
									</code>
									<code class="" id="replayPerf" rows="7"
										style="border-radius: 10px; width: 100%; padding: 10px; box-sizing: border-box;"> 
									</code> 
								</div>
								</div>
							</div>
						</div>
				<!-- --------------------------  -->
				<!-- Advisor div -->
				<!-- --------------------------  -->
<!-- 					<div class="row" id="LLMResult" > -->
<!-- 							<div class="column"> -->
<!-- 								<div class="form-row"> -->
<!-- 									<div align="left"> -->
<!--  										<h4>Advisor</h4> --> 
<!-- 									</div> -->
<!-- 								</div> -->
<!-- 								<div class="form-row"> -->
<!-- 									<div align="left"> -->
<!-- 										<div class="container"> -->
<!--  											<div id="AdvisorDiv" rows="12" --> 
<%-- 												style="border-radius: 10px; width: 100%; padding: 25px; box-sizing: border-box;"><pre><code><%= request.getAttribute("gptResponse") %></code></pre> --%> 
<!--  											</div> --> 
<%-- 									<code class="prettyprint lang-c" id="expsectionLLM" rows="8" --%>
<%-- 										style="border-radius: 10px; width: 100%; padding: 10px; box-sizing: border-box;"><%= request.getAttribute("gptOutput") %> --%>
<%-- 									</code> --%>
<!-- 										<iframe src="https://chat.openai.com/" width="800" height="800"></iframe>  //  there isn't an iframe-compatible version of GPT provided by OpenAI or any other organization. --> 
<!-- 										</div> -->
<!-- 									</div> -->
<!-- 								</div> -->
<!-- 							</div> -->
<!-- 						</div> -->

						<!-- -------------------  -->
						<!-- Add some space here  -->
						<!-- -------------------  -->

						<div class="row">
							<br>
						</div>

						<!-- ----------------------------------------------------  -->
						<!-- Add Cetus passes and Cetus Analysis Full report here  -->
						<!-- ----------------------------------------------------  -->

						<div class="row" id="result_row" style="display: none;">

							<div class="column">

								<div class="form-row">
									<div align="left">
										<h4>Cetus Passes</h4>
									</div>
								</div>
								<%-- 					<div class="form-row">
						<div align="left">
							Cetus Passes File Path: <br><%=resultSet.getString("cetus_passes_filepath")%></div>
					</div> --%>
								<%-- <div class="form-row">
<div align="left"> View Cetus Passes Report: <a href=<%=resultSet.getString("cetus_passes_filepath") %> target=_blank>View Cetus Passes Report </a></div>
</div> --%>

								<div class="form-row">
									<div align="left">
										<!-- Cetus Passes Report Content: <br> -->
										<textarea rows="15" readonly=" readonly" WRAP="off"
											style="border-radius: 10px; padding: 25px; width: 100%; box-sizing: border-box;"><%=user.getFileOutput(resultSet.getString("cetus_passes_filepath"))%></textarea>
									</div>
								</div>
								<a href="<%=pathWebcontent%>/downloadPasses.jsp">Download
									Cetus Passes File</a>
							</div>
							<div class="column">

								<div class="form-row">

									<div align="left">
										<h4>Cetus Analysis</h4>
									</div>
								</div>
								<%-- 					<div class="form-row">
						<div align="left">
							Cetus Analysis Report Path: <br><%=resultSet.getString("cetus_Analysis_filepath")%></div>
					</div> --%>
								<%-- <div class="form-row">
<div align="left"> View Cetus Analysis Report: <a href=<%=resultSet.getString("cetus_Analysis_filepath") %> target=_blank>View Cetus Passes Report </a></div>
</div> --%>

								<div class="form-row">
									<div align="left">
										<!-- Cetus Analysis Report Content: <br> -->
										<textarea rows="15" readonly=" readonly" WRAP="off"
											style="border-radius: 10px; padding: 25px; width: 100%; box-sizing: border-box;"><%=user.getFileOutput(resultSet.getString("cetus_Analysis_filepath"))%></textarea>
										<!-- user.getFileOutput(resultSet.getString("cetus_Analysis_filepath")) -->
									</div>
								</div>
								<a href="<%=pathWebcontent%>/downloadAnalysis.jsp">Download
									Cetus Analysis File</a>
							</div>
						</div>

						<!-- <div class="row"  > -->
						<br>
						<button type="button" class="btn btn-primary btn-lg btn-block"
							style="align: left; padding: 10px;" id="btn_result"
							onclick="btn_report()">Show Detailed Report</button>
						<br>
						<!-- </div> -->
		</table>
		<script>
//page loader functionality
$(document).ready(function(){
		$("#outputForm").on("submit", function(){
			$("#pageloader").fadeIn();
		});//submit
});//document ready
			


/* 	hovering over question mark shows the popover contents	 */
$(function() {
		$('.fa').popover({
			trigger : "hover"
		});
	})
	


</script>


<script>
		
	function btn_report() {
		var row_display = document.getElementById("result_row").style.display;
		if (row_display == "none") {
			document.getElementById("result_row").style.display = "";
			document.getElementById("btn_result").textContent = "Hide Detailed Report";

		} else {
			document.getElementById("result_row").style.display = "none";
			document.getElementById("btn_result").textContent = "Show Detailed Report";
		}

	}
	
	function cetus_report(){
		var flags_display = document.getElementById("cetus_row").style.display;
		if (flags_display == "none") {
			document.getElementById("cetus_row").style.display = "";
			document.getElementById("cetusFlags").textContent = "Hide Flags";

		} else {
			document.getElementById("cetus_row").style.display = "none";
			document.getElementById("cetusFlags").textContent = "Show Flags";
		}
	}
	
 	function load(){
		var divInput= document.getElementById("cetusinput");
		divInput.innerHTML = ""<%=inputFinalString%>; 
		var divoutput= document.getElementById("cetusoutput");
		divoutput.innerHTML = ""<%=outputDivString%>; 
		// Hide the div
		divoutput.style.display = "none";
		/* prettify the code */
		PR.prettyPrint();
<%--   	    var expFinalString = <%=expFinalString%>; //null  or strats with +"" --%>
 	   	var expFinalSection = <%=expFinalString%>;
		var expgptoutput= "<%=gptOutput%>";
 		console.log("expFinalSection:"+expFinalSection);
//  		console.log("expFinalString:"+expFinalString);
		var expoutput= document.getElementById("expsection");
        // Retrieve the attribute from the request object
  	
    	
        if(expgptoutput !==null && expgptoutput !== "null"){
         	expoutput.innerHTML = "<%=gptOutput%>";  //if gpt output is back then load it to exp section
        }else if (expFinalSection !== null) { 
             expoutput.innerHTML = expFinalSection;
        }else{
        	expoutput.innerHTML = ""<%=ExperimentalSectionFinalString%>;
        }

        
		document.getElementById("liveinexpsection").innerHTML = "<span class=\"highlighted-text-grey\">"+"//Live-in data of the experimental section is displayed in the format type:name[:size]."+"</span><br>"+<%=liveInExtractedLine%>;
		document.getElementById("liveoutexpsection").innerHTML = "<span class=\"highlighted-text-grey\">"+"//Live-out data of the experimental section is displayed in the format type:name[:size]."+"</span><br>"+<%=liveOutExtractedLine%>;
	
		//expoutput.innerHTML = "<span class=\"highlighted-text-grey\">"+"//Live-in and live-out data are displayed in the format type:name[:size]."+"</span>"; 
<%-- 		expoutput.innerHTML += "<br><span class=\"highlighted-text\"> /*"+<%=liveInExtractedLine%>+"*/</span><br>";  --%>
<%-- 		expoutput.innerHTML += ""<%=ExperimentalSectionFinalString%>;  --%>
<%-- 		expoutput.innerHTML += "<br><span class=\"highlighted-text\"> /*"+<%=liveOutExtractedLine%>+" */</span>";  --%>
 		var executionResult=document.getElementById("ExecutionResultDiv");
 		executionResult.innerHTML = ""<%=exeResultfinalString%>; //Result of Execution in Capture and REplay wil be added here
 		document.getElementById("pluggedininput").style.display = "none"; //hide
		document.getElementById("downloadinput").style.display = "";
		document.getElementById("downloadpluggedinput").style.display = "none";//hide
		document.getElementById("replayExp").style.display = "";
 		document.getElementById("replayPerf").style.display = "none";
		//document.getElementById("replaySelector").addEventListener("change", updateOtherFields);
 	// Execute the function as soon as the page loads
 		  processContentChanges();		
	
		} 

	function setParasforExecutionTime(){  
		var divOutput= document.getElementById("cetusoutput");
		//read the file instead of the text that shows in the 
		var inputParameter= document.getElementById("usercode");
		inputParameter.value=  divOutput.innerText+"\n"; 
		//console.log("Last output\n \n "+divOutput.innerText);
		document.getElementById("codeRadios").value="code";
		document.getElementById("gridRadios").value="Execute"; 
		document.getElementById("executeOnly").value="Execute"; 
	}
	
	function copytoInputFunction(){
		var divInput= document.getElementById("cetusinput");
		var exp= document.getElementById("expsection");
		//get the content of teh Experimental section code element
		//modify the content of experimental section in the input application file 
		//display the content of the new input file in teh code element
		//save the new input code as a new entry to the user table. as it can be compiled with Cetus or Carv Later.
		//var experimental= exp.textContent;  //experimental section
		var modifiedCode = exp.innerHTML;             // Get the content of the experimental section code element
		console.log("modifiedCode "+ modifiedCode);
		// Your existing code to get the start and end indexes of the experimental section
		var inputContent= ""<%= request.getAttribute("inputFinalString") %>;
		var expStartIdx = inputContent.indexOf("#pragma experimental section start", 0);
		console.log("expStartIdx  ",expStartIdx);
		var brIndex = inputContent.indexOf("<br>", expStartIdx); 
		var length= brIndex + 4 - expStartIdx;
		var expStopIdx = inputContent.indexOf("#pragma experimental section stop", expStartIdx);
		console.log("expStopIdx  ",expStopIdx );
	    // Replace the experimental section in divOutput with the modified code<br>/br>
	    var newInput = inputContent.substring(0,expStartIdx+length) +  
	                  modifiedCode + 
	                  inputContent.substring(expStopIdx);
	    console.log("newInput  ",newInput );
	    //divInput.innerHTML = newInput; //copied the content of code element to code element. 
	    //now save it in the file
// 		var inputParameter= document.getElementById("usercode"); //setting the usercode field that is the hidden file to what should be passed. in this case is the output is set to be passed for execution
// 		inputParameter.value= newInput+"\n"; 
		
// 		document.getElementById("codeRadios").value= "passedFile"; //It's a file we are passing to the servlet
// 		document.getElementById("gridRadios").value= "CaRV"; //write a new RedirectPage to "/WEB-INF/views/CaRV.jsp";

	//create the div for a new input code
	document.getElementById("pluggedininput").innerHTML= newInput+"\n";  //I made this field for For tracking the main input file. but what if teh user wants to execute the file?

	var displayText = document.getElementById("copyToInput").textContent;
	
	if(displayText === "Copy Exp section into Input"){
		document.getElementById("cetusinput").style.display = "none"; //hide
		document.getElementById("pluggedininput").style.display = ""; //show
		document.getElementById("copyToInput").textContent = "Load the main Input file";
		document.getElementById("pluggedininput").classList.add('prettyprint', 'lang-c'); 
		document.getElementById("downloadinput").style.display = "none";
		//document.getElementById("downloadpluggedinput").style.display = ""; //The file will only be created on the server after Running the Input
		
	}else if(displayText ==="Load the main Input file"){
		document.getElementById("cetusinput").style.display = "";
		document.getElementById("pluggedininput").style.display = "none"; //hide
		document.getElementById("copyToInput").textContent = "Copy Exp section into Input";
		document.getElementById("downloadinput").style.display = "";
		document.getElementById("downloadpluggedinput").style.display = "none";//hide
	}
	
	//hide the previous input
	}
	//Running Input file, which input should be executed teh main input or the plugged-in input. It depends on which one is not hidden.
	function setInputparasforservlet(){
		var divInput= document.getElementById("cetusinput");
		var divpluggedinInput= document.getElementById("pluggedininput");
		var inputParameter= document.getElementById("usercode"); //setting the usercode field that is the hidden file to what should be passed. in this case is the output is set to be passed for execution
		//inputParameter.value=  divInput.innerText+"\n"; 
		
		if (document.getElementById("cetusinput").style.display === "") { //is showing
		    inputParameter.value = document.getElementById("cetusinput").innerText+"\n";
		}else if (document.getElementById("pluggedininput").style.display === ""){
			inputParameter.value = document.getElementById("pluggedininput").innerText+"\n";
		}
		
		document.getElementById("codeRadios").value="passedFile"; //It's a file we are passing to the servlet
		document.getElementById("gridRadios").value="CaRVExecute"; 
		document.getElementById("CaRVPhase").value="Input";
	}
	
	function setCaptureParasforservlet(){

		var divOutput= document.getElementById("cetusoutput");
		var inputParameter= document.getElementById("usercode"); //setting the usercode field that is the hidden file to what should be passed. in this case is the output is set to be passed for execution
		inputParameter.value=  divOutput.innerText+"\n"; 
		
		document.getElementById("codeRadios").value="passedFile"; //It's a file we are passing to the servlet
		document.getElementById("gridRadios").value="CaRVExecute"; //write a new RedirectPage to "/WEB-INF/views/CaRV.jsp";
		document.getElementById("CaRVPhase").value="Capture"; // CaRVPhase can be set to Cature or Replay.
		document.getElementById("DBRowId").value= generatedKey;
	}
	
	function setReplayParasforservlet(){
		var divOutput= document.getElementById("cetusoutput");
		var outputDivString = divOutput.innerText;
		var expOutput= document.getElementById("expsection");
		var modifiedCode = expOutput.innerText;  //exp section code
		console.log("modifiedCode: \n "+modifiedCode);
		// Your existing code to get the start and end indexes of the experimental section
		var expStartIdx = outputDivString.indexOf("gettimeofday(&startexp, NULL);", 0);
		var expStopIdx = outputDivString.indexOf("#pragma experimental section stop", expStartIdx);
		
	    // Replace the experimental section in divOutput with the modified code
	    var newOutput = outputDivString.substring(0, expStartIdx) + "gettimeofday(&startexp, NULL);\n"+
	                    modifiedCode + "\n"+
	                    outputDivString.substring(expStopIdx);
		
	    divOutput.innerText = newOutput;
	    
		var inputParameter= document.getElementById("usercode"); //setting the usercode field that is the hidden file to what should be passed. in this case is the output is set to be passed for execution
		inputParameter.value=  divOutput.innerText+"\n"; 
		var expParameter= document.getElementById("expsectionpara"); //setting the exp section that should be passed
		expParameter.value=  modifiedCode; //the experimental section
		

		document.getElementById("codeRadios").value="passedFile"; //It's a file we are passing to the servlet
		document.getElementById("gridRadios").value="CaRVExecute"; //write a new RedirectPage to "/WEB-INF/views/CaRV.jsp";
		document.getElementById("CaRVPhase").value="Replay"; // CaRVPhase can be set to Cature or Replay.
		//ReplayIndex++;
	}

	
	//Ask Cetus
	function setDefaultCetusParasforservlet(){
		//get input code
		var divInput= document.getElementById("cetusinput");
		var inputString = divInput.innerText;
		//get experimental  section
		var exp= document.getElementById("expsection");
		var expString = exp.innerText;  //exp section code
		
		//replace experiemntal section in the input code
		var expStartIdx = inputString.indexOf("#pragma experimental section start", 0);
		var expStopIdx = inputString.indexOf("#pragma experimental section stop", expStartIdx);
	    var newInput = inputString.substring(0, expStartIdx) + "#pragma experimental section start\n"+
	    				expString + "\n"+
	    				inputString.substring(expStopIdx);
		//pass the file 
		document.getElementById("usercode").value= newInput+"\n"; //should be checked
		document.getElementById("codeRadios").value="passedFile"; //should be checked
		document.getElementById("gridRadios").value="AskCetus";   //should be checked //it should be all about developing this pass 
																  //which is equal to running "auto" but then saving it to CetusDB.
			
	}

	
	//Ask GPT for API
	function setPromptParasforservlet(){
		var exp= document.getElementById("expsection");
		var expString = exp.innerText; //exp section
		var livein= document.getElementById("liveinexpsection");
		var liveinString = livein.innerText; //live-ins
		var liveout= document.getElementById("liveoutexpsection");
		var liveoutString = liveout.innerText; //live-outs		
		//setting  parameters that should be passed to server
		var expParameter= document.getElementById("expsectionpara"); //setting the exp section that should be passed
		expParameter.value=  expString; //the experimental section
		var liveinParameter= document.getElementById("liveinpara"); //setting the exp section that should be passed
		liveinParameter.value=  liveinString; //the experimental section
		var liveoutParameter= document.getElementById("liveoutpara"); //setting the exp section that should be passed
		liveoutParameter.value=  liveoutString; //the experimental section
		document.getElementById("gridRadios").value="AskGPT";
		document.getElementById("usercode").value= "Ask GPT";
	}
	
	//openning up a chat.openai.com window and passing the encoded prompt to it does not work.logging in is needed before passing the prompt. and the login or sign up links do not work.
//     function openAskGPT() {
//         // Define the prompt to be sent to GPT
//         var prompt = "";
//         var exp= document.getElementById("expsection");
// 		var expString = exp.innerText; //exp section
// 		var livein= document.getElementById("liveinexpsection");
// 		var liveinString = livein.innerText; //live-ins
// 		var liveout= document.getElementById("liveoutexpsection");
// 		var liveoutString = liveout.innerText; //live-outs		
// 		prompt= "you have the role of C code optimizer. The optimizations you suggest are for C codes and it can be related to data structure changes,"+ 
// 		"algorithmic changes, or adding OpenMP pragmas for parallelizing teh code."+
// 		" Here is the code section that needs to be optimized:"+  expString +
// 		" I also provide you the live-in variables that are used in that code section. In the form of ‘type of variable: variable name: variable size’."+
// 		"For example ‘In=int:i,int:l,double:q:10,’  means live-in variables are int i; int l; double q[10]; "+ "Here are live-in variables: "+ liveinString +
// 		"I also provide live-out variables in the same format as live-in variables. Here is an example:  Out=double:q:10,double:sx,double:sy,"+
// 		"Notice that live-out variables are the variables that their values should not change during the optimization process. "+"Here are live-out variables:"+
// 		liveoutString+ "Give me only the optimized version of the code. No explanation is needed. Give me the entire code and do not shorten the code." ;
		
//         // Encode the prompt to be included in the URL
//         var encodedPrompt = encodeURIComponent(prompt);
//         // Construct the URL with the encoded prompt
//         var url = "https://chat.openai.com/?prompt=" + encodedPrompt;

//         // Open the URL in a new window
//         window.open(url, "_blank");
//     }
	
	function changeExpSection(){
		var divInput= document.getElementById("cetusinput");
		var divOutput= document.getElementById("cetusoutput");
		divOutput.style.display = "none";
		var divExp= document.getElementById("expsection");
		divExp.style.display = "";
		divExp.style.background="rgba(255,255,255, 0.9)";
		document.getElementById("cetusinput").style.background= "rgba(192, 192, 192, 0.2)";
		var att = document.createAttribute("contentEditable");       // Create a "class" attribute
		att.value = "true";                           // Set the value of the class attribute
		divExp.setAttributeNode(att);  // Add the class attribute to output to be editable
		divExp.classList.remove('prettyprint', 'lang-c'); 
<%-- 		divOutput.innerHTML = ""<%=outputcetusFinalString%>;  --%>
		document.getElementById("modifiableOutput").innerHTML="Experimental Section (Modifiable)";
	}
	

	
	function displayOutputFunction(){
		var expoutput= document.getElementById("expsection");  //experimental section
		var divOutput= document.getElementById("cetusoutput"); //CaRV output
		var displayText = document.getElementById("displayoutput").textContent;
		
		if(displayText === "Display Output"){
			expoutput.style.display = "none"; //hide
			divOutput.style.display = "";
			document.getElementById("liveinexpsection").style.display = "none";
			document.getElementById("liveoutexpsection").style.display = "none";
			document.getElementById("displayoutput").textContent = "Display Experimental Section";
			document.getElementById("modifiableOutput").innerHTML="CaRV Output";
			document.getElementById("cetusoutput").classList.add('prettyprint', 'lang-c'); 
			
		}else if(displayText ==="Display Experimental Section"){
			expoutput.style.display = "";
			document.getElementById("liveinexpsection").style.display = "";
			document.getElementById("liveoutexpsection").style.display = "";
			divOutput.style.display = "none";
			document.getElementById("displayoutput").textContent = "Display Output";
			document.getElementById("modifiableOutput").innerHTML="Experimental Section";
		}
		PR.prettyPrint();
	}

	function processContentChanges() {
	    var content = document.getElementById("ExecutionResultDiv");
	    // Get the inner HTML content of the Execution results
	    var text = content.innerHTML; 
		var index;

	    // Regular expressions to identify sections
 	    var captureRegex = /<pre>\[Capture\]([\s\S]*?)\[EndCapture\]<\/pre>/g;// /\[Capture\]([\s\S]*?)\[EndCapture\]/g;
	    var replayRegex = /<pre>\[Replay(\d+)\]([\s\S]*?)\[EndReplay\1\]<\/pre>/g;///\[Replay\]([\s\S]*?)\[EndReplay\]/g;
	    var inputRegex = /<pre>\[Input\]([\s\S]*?)\[EndInput\]<\/pre>/g;///\[Input\]([\s\S]*?)\[EndInput\]/g;
	    console.log(captureRegex);
	 

	    // Create collapsible elements for each section
	    // Replaces the regex pattern identified by the collapsible.
	    text = text.replace(captureRegex, function (match, content) {
	    	index+="C";
	   	    return createCollapsibleSection("Capture", content, index);
	    });

// 	    text = text.replace(replayRegex, function (match, content) {
// 	    	index+="R";
// 	      return createCollapsibleSection("Replay", content, index);
// 	    });

   		text = text.replace(replayRegex, function (match, replayNumber, content) {
        index += "R";
        return createCollapsibleSection("Replay" + replayNumber, content, index);
    	});
   		
	    text = text.replace(inputRegex, function (match, content) {
	    	index+="I";
	      return createCollapsibleSection("Input", content, index);
	    });
	    // Update the content with collapsible sections
	    console.log("text:::: "+text);
	    content.innerHTML = text;
	  }

	
	
	 function createCollapsibleSection(title, content, index) {
		 content = content.trim();//.replace(/\n/g, '<br>'); // Replace newline characters with <br> tags
		 
		 var sectionId = title.toLowerCase() + 'Section'+ index;
		 
		    return '<div class="collapsible-section">' +
			        '<button class="btn btn-link buttonextrawide" type="button" style="text-align: left;" onclick="toggleCollapse(\'' + sectionId + '\')">' +
			        '[' + title + '] [End' + title + ']' +
			        '</button>' +
			        '<div id="' + sectionId + '" class="collapsible-content">' +
		             content +
		             '</div>' +
		             '</div>';
	}
	 
	 function toggleCollapse(sectionId) {
		    var section = document.getElementById(sectionId);
		    if (section.style.display === 'none' || section.style.display === '') {
		        section.style.display = 'block';
		    } else {
		        section.style.display = 'none';
		    }
		}
	 processContentChanges();

</script>

<!-- JavaScript block to handle replay selection -->
<script>
    // Function to update other fields based on the selected replay
    function updateReplayDetails() {
    	 //console.log("updateOtherFields function called");
    
        var replaySelector = document.getElementById("replaySelector");
        var selectedReplayId = replaySelector.value; //parseInt(replaySelector.value,10);//to convert a string to an integer
        console.log("replay Id ", selectedReplayId);
        // Access replayList from page context
<%--         var replayList = <%=  (List<Carvreplay>)pageContext.getAttribute("replayList") %>; --%>
        
        // Find the selected replay in the replayList
//          var selectedReplay = replayList.find(function(replay) {
//              return replay.replayid === parseInt(replaySelector.value,10);//selectedReplayId;
//          });
//         console.log("replayList is loaded");
//         console.log("Selected Replay: ", selectedReplay);
//         console.log("Selected Replay exp: ", selectedReplay.replayexpsection);
//         console.log("Selected Replay results: ", selectedReplay.replayexecutionresults);
       // Update other fields on the webpage based on the selected replay
//         document.getElementById("replayExp").innerText = selectedReplayId.toString(); //selectedReplay.replayexpsection;
//         document.getElementById("replayPerf").innerText = "replay perf"; //selectedReplay.replayexecutionresults;
        
       //Update HTML elements with the values from the selected replay
       //document.getElementById("replayId").value = document.getElementById("replayId_" + selectedReplayId).value;
       //document.getElementById("fileContent").value = document.getElementById("fileContent_" + selectedReplayId).value;
       	document.getElementById("replayExp").style.display = "";
 		//document.getElementById("replayPerf").style.display = "";
        document.getElementById("replayExp").innerHTML = document.getElementById("expContent_" + selectedReplayId).value.replace(/\n/g, '<br>');
        //document.getElementById("replayPerf").innerHTML = document.getElementById("executionContent_" + selectedReplayId).value.replace(/\n/g, '<br>');
        
     // Find the corresponding button with the same replay number in its label
        //var button = document.querySelector('[Replay' + selectedReplayId + '] [EndReplay'+ selectedReplayId +']');
        //[Replay88] [EndReplay88]
      // If the button is found, click on it
     // Construct the regex pattern for matching the div section id id="replay90SectionundefinedCR" 
        var regexPattern = new RegExp('replay' + selectedReplayId + 'Section.*');
     // Find all div sections with ids starting with replay
//         var allSections = document.querySelectorAll('[id^="replay"]');

//      // Hide all such sections
//      allSections.forEach(function (section) {
//          section.style.display = 'none';
//      });
     // Find the div section that matches the pattern
        var matchingSection = document.querySelector('[id^="replay' + selectedReplayId + 'Section"]');

        if (matchingSection) {
            // Show the content of the matching section
            matchingSection.style.display = 'block';
        }
        
    }
 // Call the function when you want to show the selected replay
   // showSelectedReplay();
    // Attach the updateOtherFields function to the change event of the replaySelector
//     document.getElementById("replaySelector").addEventListener("click", function() {
//     console.log("change event triggered");
//     //updateReplayDetails();
// });
</script>


<!-- Add the necessary Bootstrap and jQuery scripts -->
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
<script 	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"
	integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q"
	crossorigin="anonymous"></script>
	
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script> 
	<!-- code prettifier -->
<script src="https://cdn.jsdelivr.net/gh/google/code-prettify@master/loader/run_prettify.js?lang=c&amp;skin=default"></script> 

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<!-- <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js"></script> -->
<!-- <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script> -->

<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<!-- Bootstrap JS and Popper.js -->

<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>

<%
	connection.close();
} catch (Exception e) {
e.printStackTrace();
}
%>
