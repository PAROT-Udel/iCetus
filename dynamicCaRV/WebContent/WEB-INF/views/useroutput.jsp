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

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>

<!-- Global site tag (gtag.js) - Google Analytics -->
<script async
	src="https://www.googletagmanager.com/gtag/js?id=G-J9XNTHF4C4"></script>
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
	
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
	integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm"
	crossorigin="anonymous">

<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
	integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
	crossorigin="anonymous"></script>
	<!-- Pageloader	-->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<!-- 	for popovers -->
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"
	integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q"
	crossorigin="anonymous"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"
	integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
	crossorigin="anonymous"></script>
	<!-- code prettifier -->
 <script src="https://cdn.jsdelivr.net/gh/google/code-prettify@master/loader/run_prettify.js?lang=c&amp;skin=default"></script> 



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

	width: 200px;
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

span:target{color:blue;
font-weight: bold;
  }
</style>
<style>
#textarea , #formattedAnalysis , #cetusinput, #cetusoutput{
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

<title>Cetus Output</title>


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
	}
%>





<%
//reads from the files saved on the server -These are all file paths that we need for redaing them and writing to them in case of recompiling
String inputContent = user.getFileOutput(resultSet.getString("input_code"));
System.out.println("\n\n\n inputContent"+inputContent);
String outputContent = user.getFileOutput(resultSet.getString("cetus_output_filepath"));
System.out.println("\n\n\n outputContent"+outputContent);

//reading Analysis file content from the server not the Database file content.
String analysisContent = user.getFileOutput(resultSet.getString("cetus_Analysis_filepath"));
//only execution results are included in "cetus_Analysis_content"
String analysis2= resultSet.getString("cetus_Analysis_content");//analysisContent;//resultSet.getString("cetus_Analysis_content");//why I am saving analysis content in another string ? for teh performance results
if (analysis2 == null) {
    System.out.println("The retrieved analysis content is null. Please increase the size of GLOBAL max_allowed_packet.");
}
//Output the results
System.out.println("Content from file: " + analysisContent);
System.out.println("Content from database: " + analysis2);		
//Path analysisCetus = Path.of(resultSet.getString("cetus_Analysis_filepath"));
//String analysisContent = Files.readString(analysisCetus);

//cetus_passes_filepath contains the file path to the debugger that has a large size. 
//cetus_passes_content is a large DB field. remove it to get rid of most problems.
String passesContent = (resultSet.getString("cetus_passes_content"));//user.getFileOutput(resultSet.getString("cetus_passes_filepath"));
//String analysis2=(resultSet.getString("cetus_Analysis_content"));
String cetusOptions= (resultSet.getString("cetus_option_set"));
//System.out.println("\n\n\n cetusOptions set"+cetusOptions);
//System.out.println("analysis content from DB not from the file: "+analysis2);
String inputPath = resultSet.getString("input_code");

//get the filename from the file path
Path p = Paths.get(inputPath);
String file = p.getFileName().toString();
System.out.println("\n\n \n inputfile name:" + file);

StringBuilder sb= new StringBuilder();
sb = servlet.seperateOptions(cetusOptions, sb);
String allOptions= sb.toString();
//System.out.println("\n\n \n AllOptions:" + sb.toString());



String cetusOutContenet = null;
String FormatedAnalysis = null;
String DDTfinalString = null;
String outputFinalString = null;
String RangefinalString = null;
String cfgfinalString = null;
String ipafinalString = null;
String reductionfinalString = null;
String branchEfinalString = null;
String callGfinalString = null;
String ivfinalString = null;
String outputcetusFinalString = null;
String inputFinalString = null;
String privatefinalString = null;
String outputContent1 = null;
String cfghelp = null;
String callGhelp =null;
String ddthelp= null;
String callgOutput= null;
String profilerhelp= null;
String exeResulthelp=null;
%>
<%
/*Cetus Input in div section =========================================================================================*/
System.out.println("Creating Cetus input");
//StringBuilder ffinalinputStringBuilder = new StringBuilder("");
StringBuilder finalinputStringBuilder = new StringBuilder("");
/*Output Cleaning*/
//save it line by line
/*use the pipe (|) to match comments  replaces all comments*/
//inputContent = inputContent.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");
//replaces all empty lines
//inputContent = inputContent.replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
//System.out.println("\n\n \n outputcontent:" + outputContent);
String[] inlines = inputContent.split("\n");

inlines= servlet.makeHTMLqualified(inlines,finalinputStringBuilder);
/* for (int i = 0; i < inlines.length; i++) {
	System.out.println("inlines["+i+"]:  "  +inlines[i] );
	//finalinputStringBuilder.append(inlines[i]);
	 if (!inlines[i].equals("")) {
	finalinputStringBuilder.append("+\"").append(inlines[i].trim()).append("<br>\"").append(System.getProperty("line.separator"));
	}
} */

//System.getProperty("line.separator")
//based on how each line starts or what it contains format it, 
//now remove some parts from the code
/*  for (int i = 0; i < inlines.length; i++) {
	 	if ( inlines[i].contains("cetus")) {
		inlines[i] = "";
	} 
}  */
/* Creating the string for the textarea */
/* for (String s : inlines) {
	if (!s.equals("")) {
		finalinputStringBuilder.append(s).append(System.getProperty("line.separator"));
	}
} */

inputFinalString = finalinputStringBuilder.toString();
//inputFinalString= inputFinalString.replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
System.out.println("\n\n \n inputFinalString:" + inputFinalString);
//System.out.println("\n\n \n output:" + outputContent);
System.out.println("Cetus input created");
/* Cetus Output for queries, div section ===================================================================================== */
System.out.println("Creating Cetus output");
String outputDivString = null;
StringBuilder finaldivStringBuilder = new StringBuilder("");
String outputContent2 = null;
String option = null;
/*Output Cleaning*/
//save it line by line
/*use the pipe (|) to match comments  replaces all comments*/
//outputContent2 = outputContent.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");
//replaces all empty lines
outputContent2 = outputContent.replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
//cetusOutContenet=outputContent;
//System.out.println("\n\n \n outputcontent:" + outputContent);
String[] dlines = outputContent2.split("\n");
//System.getProperty("line.separator")

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
//outputDivString= outputDivString.replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
//System.out.println("\n\n \n div scetion content :" + outputDivString);
System.out.println("Cetus output created");
/* Cetus Output for callgraph to make links===========================call graph output========================================================== */
System.out.println("\n Creating Call graph");
//I reverse the sattements and then start from ( and then go forward to the first space to extract the procedure names.
//the goal is that when the user clicks on the procedure name in callgraph section, the relative method name gets highlighted and gets the focus. 
int procedureFromIndex= 0;
int procedureToIndex=0;
String procedureName=null;
StringBuilder sbsrcLink= new StringBuilder("");
String[] callgoutlines = outputDivString.split("\n");
StringBuilder input1 = new StringBuilder();
for (int i = 0; i < callgoutlines.length; i++) {
//System.out.println("\n callgoutlines " + i +" "+ callgoutlines[i]);

//look for function names
if(callgoutlines[i].contains("for")||callgoutlines[i].contains("#pragma")||callgoutlines[i].contains("while")||callgoutlines[i].contains("do")){
	//no changes}
}else{
	String reverse = new StringBuffer(callgoutlines[i]).reverse().toString();
	System.out.println("reverse"+ reverse);

procedureFromIndex = reverse.indexOf("(", 0);
//System.out.println("procFromIndex"+procedureFromIndex);
	if (procedureFromIndex>=0){
		procedureToIndex = reverse.indexOf(" ", procedureFromIndex);
	//System.out.println("procToIndex"+procedureToIndex);
	}
	if (procedureFromIndex>=0 && procedureToIndex>=0 && procedureToIndex>procedureFromIndex){
		procedureName = reverse.substring(procedureFromIndex+1, procedureToIndex);
		//System.out.println("\n reverse procedure name in line"+i +"="+ procedureName);
		if(!procedureName.equals("(") && !procedureName.contains("=")&& !procedureName.contains(">") && !procedureName.contains("<")&& !procedureName.contains(")")){
		//new stringBuilder
		input1.append(procedureName);
		input1.reverse();
		 procedureName= input1.toString();
		 System.out.println("\n reverse procedure name in line"+i +"="+ procedureName);
		 input1.setLength(0);
		//System.out.println("\n procedure name in line"+i +"="+ procedureName);
		if(procedureName.length()>0){
		callgoutlines[i]=callgoutlines[i].replace(procedureName, "<span id=\'"+procedureName+"\'>"+procedureName+"</span>");}
		//System.out.println("\n callgoutlines"+i+"="+callgoutlines[i]);
		}
	}
}
}
for (String s : callgoutlines) {
	if (!s.equals("")) {
		sbsrcLink.append(s);
	} 
}

callgOutput=sbsrcLink.toString();
//System.out.println("\n callgOutput "+callgOutput);
System.out.println("\n Call graph Created");
/* Cetus Output for queries, div section ===========================Reduction========================================================== */
System.out.println("Creating color coding for output of Reduction Analysis");
 String colorReduction = outputDivString;
String[] Rlines = colorReduction.split("\n");
StringBuilder finalreductionStringBuilder = new StringBuilder("");

for (int i = 0; i < Rlines.length; i++) {
	if (Rlines[i].contains("#pragma omp") && Rlines[i].contains("reduction")) {
		Rlines[i] = Rlines[i].replaceAll("reduction", "<span class='highlight-purple'>reduction</span>");
	}
}
for (String s : Rlines) {
	if (!s.equals("")) {
		finalreductionStringBuilder.append(s).append(System.getProperty("line.separator"));
	}
}
colorReduction = finalreductionStringBuilder.toString();
//System.out.println("\n colorReduction:" + colorReduction); 

System.out.println("Color-coded output for Reduction Analysis created");
/* Cetus Output for queries, div section ===========================Private Vars================================================ */
System.out.println("Creating color coding for output of private var Analysis");

 String colorPrivate = outputDivString;
String[] Plines = colorPrivate.split("\n");
StringBuilder finalPrivateStringBuilder = new StringBuilder("");

for (int i = 0; i < Plines.length; i++) {
	if (Plines[i].contains("#pragma omp") && Plines[i].contains("private")) {

		Plines[i] = Plines[i].replaceAll("private", "<span class='highlight-blue'>private</span>");
	}
}
for (String s : Plines) {
	if (!s.equals("")) {
		finalPrivateStringBuilder.append(s).append(System.getProperty("line.separator"));
	}
}
colorPrivate = finalPrivateStringBuilder.toString();
//System.out.println("\n\n \n colorPrivate:" + colorPrivate); 
System.out.println("Color-coded output for private var Analysis created");
/* Cetus Output for queries, div section ====================================Dependencies================================================= */
//show me loops that are not parallelized
System.out.println("Creating color coding for output -displays loops that show dependency");
String colorDep = outputDivString;
String[] deplines = colorDep.split("\n");
StringBuilder finalDepStringBuilder = new StringBuilder("");

for (int i = 0; i < deplines.length; i++) {
	if ((deplines[i].contains("for (") && deplines[i-1].contains("omp parallel"))||(deplines[i].contains("for(") && deplines[i-1].contains("omp parallel")) ) {
			//System.out.println("\n\n \n contains omp parallel for :" + deplines[i]); 
			deplines[i] = deplines[i];
		}else if ((deplines[i].contains("for (") && !deplines[i-1].contains("omp parallel"))||(deplines[i].contains("for(") && !deplines[i-1].contains("omp parallel"))){
			//System.out.println("\n\n \n does not contains omp parallel for :" + deplines[i]);
			deplines[i] = deplines[i].replaceAll("for", "<span class='highlight-yellow'>for</span>");
		}
	}

for (String s : deplines) {
	if(!s.equals("")) {
		finalDepStringBuilder.append(s).append(System.getProperty("line.separator"));
	}
}

colorDep = finalDepStringBuilder.toString();
//System.out.println("\n\n \n colorDep:" + colorDep); 
System.out.println("Color-coded output for loops with dependency Created");
/* Cetus Output ===================================================================================== */
//seems like loop names are created two times and added to the profiler info. why such a thing i shappening?
		//get the profiling code tested.because for some codes it gives segmentation core error. Make sure profiling info appears correctly in iCetus.
System.out.println("\nCreating Cetus-output without Cetus pragmas-top right ");	
StringBuilder finalcetusStringBuilder = new StringBuilder("");

/*Output Cleaning*/
//save it line by line
/*use the pipe (|) to match comments  replaces all comments*/
//outputContent1 = outputContent.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");
//replaces all empty lines
outputContent1 = outputContent.replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
//cetusOutContenet=outputContent;
System.out.println("\n\n \n outputcontent1:" + outputContent1);
String[] clines = outputContent1.split("\n");
//System.getProperty("line.separator")
//based on how each line starts or what it contains format it, 
//now remove Range test parts from the code

for (int i = 0; i < clines.length; i++) {
	if (clines[i].contains(" cetus ")) {
		clines[i] = "";
	}
/* 	if (clines[i].contains("#pragma loop name")) {
		clines[i] = "";
	} */
}


/* Creating the string for the textarea */
/* for (String s : clines) {
	if (!s.equals("")) {
		finalcetusStringBuilder.append(s).append(System.getProperty("line.separator"));
	}
} */

/*  for (String s : clines) {
	if ( s.contains("#pragma loop name")) {
		finalcetusStringBuilder.append("+\"<i style=\'color:orange\'>").append(s).append("</i>").append("<br>\"")		;
	} else if ( s.contains("#pragma omp")) {
		finalcetusStringBuilder.append("+\"<i style=\'color:blue\'>").append(s).append("</i>").append("<br>\"")		;
	} else if ( s.contains("#pragma cetus")) {
		finalcetusStringBuilder.append("+\"<i style=\'color:grey\'>").append(s).append("</i>").append("<br>\"")		;
	}else if (!s.equals("")) {
		finalcetusStringBuilder.append("+\"").append(s).append("<br>\"");
	}
} 
 */
clines= servlet.makeHTMLqualified(clines,finalcetusStringBuilder);

outputcetusFinalString = finalcetusStringBuilder.toString(); //use if when the user want to change output
//outputcetusFinalString = outputcetusFinalString.replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
//System.out.println("\n\n \n profiling output: " + outputcetusFinalString);
System.out.println("\n Cetus-output without Cetus pragmas-top right created");	
/* Output for queries for textareas===================================================================================== */
System.out.println("\n Creating Cetus-output for textarea ");	
StringBuilder finalStringBuilder = new StringBuilder("");

/*Output Cleaning*/
//save it line by line
/*use the pipe (|) to match comments  replaces all comments*/
//outputContent = outputContent.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");
//replaces all empty lines
//outputContent = outputContent.replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
//cetusOutContenet=outputContent;
//System.out.println("\n\n \n outputcontent:" + outputContent);
String[] lines = outputContent.split("\n");
//System.getProperty("line.separator")
//based on how each line starts or what it contains format it, 
//now remove Range test parts from the code
/*  for (int i = 0; i < lines.length; i++) {
	 	if ( lines[i].contains("\"")) {
		lines[i] = lines[i].replaceAll("\"", "\\\"");
	} 
}  */
/* Creating the string for the textarea */
for (String s : lines) {
	if (!s.equals("")) {
		finalStringBuilder.append(" " + "+\"\\n ").append(s).append("\"").append(System.getProperty("line.separator"));
	}
}

outputFinalString = finalStringBuilder.toString();
//outputFinalString = outputFinalString.replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
//System.out.println("\n\n \n output: " + outputFinalString);
System.out.println("\n Cetus-output for textarea Created");

/* ===================================================================================== */
%>
<%
/* Extract Execution time  from Analysis================================================== */
System.out.println("\n Creating Execution report ");	
String exefinalString=null;
if (analysis2 == null) {
    System.out.println("The retrieved analysis content is null. Please increase the size of GLOBAL max_allowed_packet.");
} else {
StringBuilder exefinalStringBuilder = new StringBuilder("");
//System.out.println("\n\n"+ analysis2);   //debug
int exefromidx = analysis2.indexOf("[Execution]", 0);
System.out.println("from index \t"+exefromidx);
//print from "Exception Type:" to * for the user to show the error to the user
int exetoidx = analysis2.indexOf("[Profiler]", exefromidx);
System.out.println("to profiler index \t"+exetoidx);
int exectoidxx= analysis2.indexOf("[ParallelCode]", exefromidx);
System.out.println("to [ParallelCode] index \t"+exectoidxx);
if (exefromidx < 0 ) {// FormatedAnalysis == null || FormatedAnalysis.isEmpty() || FormatedAnalysis.trim().isEmpty()){ 
	// System.out.println("No Result");
	exefinalString = "+\"Calculating speedup and efficiency failed!\"";
} else if (exetoidx<0 && exectoidxx<0){
	FormatedAnalysis = analysis2.substring(exefromidx);
	System.out.println("Execution report \t"+FormatedAnalysis);
} else if (exetoidx<0 && exectoidxx>0){
	FormatedAnalysis = analysis2.substring(exefromidx,exectoidxx);
	System.out.println("Execution report \t"+FormatedAnalysis);
}else{
	FormatedAnalysis = analysis2.substring(exefromidx,exetoidx);
	System.out.println("Execution report \t"+FormatedAnalysis);
	//System.out.println("Range Analysis output \t \t"+FormatedAnalysis);
}
	//save it line by line
	String[] exelines = FormatedAnalysis.split("\n");
	//System.getProperty("line.separator")
   exelines= servlet.makeHTMLqualified(exelines,exefinalStringBuilder);
	/* Creating thestring for the textarea */
/* 	for (String s : exelines) {
		if (!s.equals("")) {
	exefinalStringBuilder.append(" " + "+\"\\n ").append(s).append("\"")
			.append(System.getProperty("line.separator"));
		}
	} */

	exefinalString =  exefinalStringBuilder.toString() ;
}
	System.out.println("\n \n\n Execution: " + exefinalString);
//}
/* [Execution] Sequential code running time in seconds = 0.107
[Execution] Parallel code running time in seconds = 0.086
[Execution] Number of threads used = 4
[Execution] Speedup (sequential RunTime/parrallel RunTime)= 1.244
[Execution] Efficiency (speed up/ number of Threads) = 0.311 */
System.out.println("\n Execution report created");
%>
<%
/* Extract Execution result  from Analysis================================================== */
System.out.println("\n Creating Execution result report ");	
System.out.println("\nCetus Analysis report \t"+analysis2);
String exeResultfinalString=null;
if (analysis2 == null) {
    System.out.println("The retrieved analysis content is null. Please increase the size of GLOBAL max_allowed_packet.");
} else {
StringBuilder exeResultfinalStringBuilder = new StringBuilder("");
int exeresfromidx = analysis2.indexOf("[SerialCode]", 0);
System.out.println("\nexeresfromidx \t"+exeresfromidx);
int exerestoidx = analysis2.indexOf("[ExecutionResult] Standard error of the program", exeresfromidx);
System.out.println("\nexerestoidx \t"+exerestoidx);
if (exeresfromidx < 0 ) {
	exeResultfinalString = "\n[ExecutionResult] Execution result of the serial code is not requested by the user.\n";
	System.out.println("\n\n\nExecution result report \t"+exeResultfinalString);
	exeResulthelp= "+\" \"";
	FormatedAnalysis= exeResultfinalString;
} else {
	FormatedAnalysis = analysis2.substring(exeresfromidx,exerestoidx );
	exeResulthelp= "+\"The result displayed here is what is printed on standard output after compiling and running the serial code. \"";
	System.out.println("\n\n\nExecution result report \t"+FormatedAnalysis);
}
    int exepararesfromidx = analysis2.indexOf("[ParallelCode] Parallel code compiled successfully.", 0);
    System.out.println("\nexepararesfromidx \t"+exepararesfromidx);
	int exepararestoidx = analysis2.indexOf("[ExecutionResult] Standard error of the program", exepararesfromidx);
	System.out.println("\nexepararestoidx \t"+exepararestoidx);
	if (exepararesfromidx < 0 ) {
		exeResultfinalString =  "[ExecutionResult] Execution result of the parallel code is not requested by the user.\n";
		exeResulthelp= "+\" \"";
		FormatedAnalysis=FormatedAnalysis + exeResultfinalString;
	} else {
		FormatedAnalysis = FormatedAnalysis+"\n\n"+ analysis2.substring(exepararesfromidx,exepararestoidx );
		exeResulthelp= exeResulthelp+"+\"The parallel code is compiled and run (on 4 cores) to produce a result. \"";
		System.out.println("\n\n\nExecution result report \t"+FormatedAnalysis);
	}
		
	//save it line by line
	String[] exereslines = FormatedAnalysis.split("\n");
	exereslines= servlet.makeHTMLqualifiedNoTriming(exereslines,exeResultfinalStringBuilder);

	exeResultfinalString = exeResultfinalStringBuilder.toString();
}
	System.out.println("\n \n\n Execution: " + exeResultfinalString);
//}

System.out.println("\n Execution result report created");
%>

<%
/* Extract profiling result from Analysis================================================== */
System.out.println("\n Creating profiling report ");
System.out.println("\n\n\nCetus Analysis report \t"+analysis2);
String prffinalString=null;
if (analysis2 == null) {
    System.out.println("The retrieved analysis content is null. Please increase the size of GLOBAL max_allowed_packet.");
} else {
StringBuilder prffinalStringBuilder = new StringBuilder("");
int prffromidx = analysis2.indexOf("[ExecutionResult] Standard error of the program", 0);
System.out.println("\nprffromidx: \n"+prffromidx);
int prftoidx = analysis2.indexOf("[ParallelCode]", prffromidx);
System.out.println("\nprftoidx: \n"+prftoidx);
if (prffromidx < 0 ) {
	prffinalString = "\nProfiling report of the serial code is not requested by the user.\n";
	FormatedAnalysis="[SerialCodeProfiler] \n"+prffinalString;
	profilerhelp= "+\" \"";
	System.out.println("\nprofiler FormatedAnalysis: \n"+FormatedAnalysis);
} else if (prftoidx < 0 ) {
	prffinalString = "\nProfiling report of the serial code is not requested by the user.\n";
	FormatedAnalysis = "[SerialCodeProfiler] "+prffinalString;
	System.out.println("\nprofiler FormatedAnalysis: \n"+FormatedAnalysis);
}else{
	System.out.println("what is printing?"+analysis2.substring(prffromidx+57, prftoidx ).trim());
	if(analysis2.substring(prffromidx+57, prftoidx ).trim()!=""){
	FormatedAnalysis = "[SerialCodeProfiler] \n"+analysis2.substring(prffromidx+57, prftoidx );
	}else{
		FormatedAnalysis ="\nProfiling report of the serial code is not requested by the user.\n";
	}
	System.out.println("\nprofiler FormatedAnalysis: \n"+FormatedAnalysis);
	}
//System.out.println("\n\n\nExecution result report \t"+FormatedAnalysis);
int prfparafromidx = analysis2.indexOf("[ExecutionResult] Standard error of the program ", prffromidx+10);
System.out.println("\nprfparafromidx \n"+prfparafromidx);
//int prfparatoidx = analysis2.indexOf("[ExecutionResult] Standard error of the program", prfparafromidx);
if (prfparafromidx < 0 ) {
	prffinalString = "\nProfiling report of the parallel code is not requested by the user.\n";
	FormatedAnalysis=FormatedAnalysis+ "[ParallelCodeProfiler] "+prffinalString;
	profilerhelp= "+\" \"";
	System.out.println("\nprofiler FormatedAnalysis: \n"+FormatedAnalysis);
} else {
	System.out.println("analysis2.substring(prfparafromidx+57 ).trim()"+ analysis2.substring(prfparafromidx+57 ).trim());
	if(analysis2.substring(prfparafromidx+57 ).trim()==null || analysis2.substring(prfparafromidx+57 ).trim()==""){
		FormatedAnalysis = FormatedAnalysis+"\nProfiling report of the parallel code is not requested by the user.\n";
	}else{
	FormatedAnalysis = FormatedAnalysis+"\n-----------------------------------\n"+"[ParallelCodeProfiler-4 Cores] \n"+ analysis2.substring(prfparafromidx+57 );}
	profilerhelp="+\"The profiler identifies the loops that consume most of the program's execution time. It does so by instrumenting the loops. \" +"+
			"\"We display first the profiling information for the serial code and then for the parallel code. \" +"+
			"\"In the <b>NAME</b> column, you can see the name that has been assigned to each loop. According to the function and the order in which the loop occurs, this assignment is given. The <b>INVOKED</b> column shows how many times the main function has invoked the loop. The <b>ELAPSED TIME(S)</b> column shows how long it took for the loop to execute. In the last row, you can see how long it took to execute the main function (PROGRAM).\"";
	System.out.println("\n\n\nProfiling report:: \n"+FormatedAnalysis);
	}
			
	//save it line by line
	String[] prflines = FormatedAnalysis.split("\n");
	prflines= servlet.makeHTMLqualifiedNoTriming(prflines,prffinalStringBuilder);

	prffinalString = prffinalStringBuilder.toString();
}
	System.out.println("\n \n\n Profiling report: " + prffinalString);
	
//}

System.out.println("\n Profiling report created");
%>
<!-- DDT----------------------------------------------------- -->
<%
System.out.println("\n Creating DDT ");	
if (analysisContent == null) {
    System.out.println("The retrieved analysis content is null. Please increase the size of GLOBAL max_allowed_packet.");
    DDTfinalString =null;
}else{
	/*DDT Analysis  */
StringBuilder ddtfinalStringBuilder = new StringBuilder("");
int fromindex = analysisContent.indexOf("[DDT]", 0);
//print from "Exception Type:" to * for the user to show the error to the user
int toindex = analysisContent.indexOf("[Reduction]", fromindex);
//This Substring method throws IndexOutOfBoundsException If the beginIndex is less than zero or greater than the length of String (beginIndex<0||> length of String).
//beginIndex is inclusive and endIndex is exclusive while getting the substring.
//It throws IndexOutOfBoundsException If 
//the beginIndex is less than zero OR 
//beginIndex > endIndex OR 
//endIndex is greater than the length of String.
if (fromindex < 0 || fromindex > toindex || toindex > analysisContent.length() || toindex < 0) {// FormatedAnalysis == null || FormatedAnalysis.isEmpty() || FormatedAnalysis.trim().isEmpty()){ 
	// System.out.println("No Result");
	DDTfinalString = "+\"No Result\"";
	ddthelp= "+\" \"";
} else {
	FormatedAnalysis = analysisContent.substring(fromindex, toindex);
	//System.out.println(FormatedAnalysis);

	//save it line by line
	String[] ddtlines = FormatedAnalysis.split("\n");
	//System.getProperty("line.separator")
	//based on how each line starts or what it contains format it, 
	//now remove Range test parts from the code
	for (int j = 0; j < ddtlines.length; j++) {
		//		System.out.println("Line "+ i+ "===="+lines[i]);
		if (ddtlines[j].startsWith("[RangeDomain]") || ddtlines[j].startsWith("[RangeTest]")
		|| ddtlines[j].startsWith("**") || ddtlines[j].startsWith("Testing")
		|| ddtlines[j].startsWith("Dependence") || ddtlines[j].contains("dependence")
		|| ddtlines[j].startsWith(">") || ddtlines[j].startsWith("<") || ddtlines[j].startsWith("=")
		|| ddtlines[j].startsWith("*") || ddtlines[j].startsWith("Arc Info") || ddtlines[j].startsWith("[DDT]")) {
	ddtlines[j] = "";
		}
		//replace AccessType: 0 depType: 2 depVector: =
		//direction vectors		"*", "<", "=", ">" depVector: *
		/*  depType
		 * 1 - Flow (True) Dependence
		 * 2 - Anti Dependence
		 * 3 - Output Dependence
		 * 4 - Input Dependence
		 */
		if (ddtlines[j].contains("AccessType")) {
	/* 		lines[j] = lines[j].replaceAll("ArcSource:", "\nArcSource:");
			lines[j] = lines[j].replaceAll("ArcSink:", "\nArcSink:"); */
	ddtlines[j] = ddtlines[j].replaceAll("AccessType: 0", "AccessType:Write");
	ddtlines[j] = ddtlines[j].replaceAll("AccessType: 1", "AccessType:Read");
	ddtlines[j] = ddtlines[j].replaceAll("depType: 1", "depType:Flow-Dependence");
	ddtlines[j] = ddtlines[j].replaceAll("depType: 2", "depType:Anti-Dependence");
	ddtlines[j] = ddtlines[j].replaceAll("depType: 3", "depType:Output-Dependence");
	ddtlines[j] = ddtlines[j].replaceAll("depType: 4", "depType:Input-Dependence");
		}
		

 		if (ddtlines[j].contains("ArcSource: ArrayAccess:")) {
			ddtlines[j] = ddtlines[j].replaceAll("ArcSource: ArrayAccess:", "{");
		}
		if (ddtlines[j].contains("ArrayAccess:")) {
			ddtlines[j] = ddtlines[j].replaceAll("ArrayAccess:", "");
				}
		if (ddtlines[j].contains("AccessType:")) {
	ddtlines[j] = ddtlines[j].replaceAll("AccessType:", ", ");
		}
		if (ddtlines[j].contains("ArcSink:")) {
	ddtlines[j] = ddtlines[j].replaceAll("ArcSink:", "} -> {");
		}
		if (ddtlines[j].contains("depType:")) {
	ddtlines[j] = ddtlines[j].replaceAll("depType:", "}  ");
		}
		if (ddtlines[j].contains("depVector:")) {
	ddtlines[j] = ddtlines[j].replaceAll("depVector:", " , ");
		} 
		if (!ddtlines[j].startsWith("{ ")) {
			ddtlines[j] = "";
		}
		//System.out.println("the line:"+ ddtlines[j]);
		String getFileName= null;
		//find text in between these characters
		int from = ddtlines[j].indexOf("{ ", 0);
		int to = ddtlines[j].indexOf(":", from);
		
		if (from < 0 || from > to|| to > ddtlines[j].length() || to < 0) {
			// do nothing
		} else {
		getFileName = ddtlines[j].substring(from+2, to+1);
		//System.out.println("Filename in the string:"+ getFileName); //examplecode.c: there's no space before file name
		ddtlines[j] = ddtlines[j].replaceAll(getFileName, "");
		//System.out.println("after replacing filename line is:"+ ddtlines[j]);
		ddtlines[j] =getFileName+" "+ddtlines[j];
		//System.out.println("after adding filename to the line :"+ ddtlines[j]);
		}

	
	}
	
	
	
	//now creating the new string to pass to the user
	/* for (String s : lines) {
		if (!s.equals("")) {
	finalStringBuilder.append(s).append(System.getProperty("line.separator"));
		}
	} */
ddtlines= servlet.makeHTMLqualified(ddtlines,ddtfinalStringBuilder);

DDTfinalString = ddtfinalStringBuilder.toString();

//----------System.out.println("DDTfinalString"+DDTfinalString);
//if (DDTfinalString != null || DDTfinalString != ""|| DDTfinalString!=" " || DDTfinalString.length()!=0 )

if (DDTfinalString == null || DDTfinalString.isEmpty() || DDTfinalString.trim().isEmpty()|| DDTfinalString.length()==0){
	ddthelp= "+\" \"";
	DDTfinalString = "+\"No dependency is reported.\"";
}else{
	ddthelp= "+\" Data dependence analysis reports on the file name that contains the dependency, the dependence source data info ( containing the loop name, source data element, access type ), the dependence sink data info (containing the loop name, sink data element, access type), the type of dependency, and the direction vector.  \"";
}

System.out.println("\n\n \n DDT " + DDTfinalString);
	//FormatedAnalysis = DDTfinalString;
System.out.println("\n  DDT created");	
}
}
/*===================================================================================  */
%>
<%
System.out.println("\n  Creating Range Analysis");	
	/*Range Analysis  */
//String RangefinalString=null;
if (analysisContent == null) {
    System.out.println("The retrieved analysis content is null. Please increase the size of GLOBAL max_allowed_packet.");
    RangefinalString =null;
}else{	
StringBuilder rangefinalStringBuilder = new StringBuilder("");
int fromidx = analysisContent.indexOf("[RangeAnalysis] Range Domain for Procedure main", 0);
//print from "Exception Type:" to * for the user to show the error to the user
int toidx = analysisContent.indexOf("[RangeDomain]", fromidx);
if (fromidx < 0 || fromidx > toidx || toidx > analysisContent.length() || toidx < 0) {// FormatedAnalysis == null || FormatedAnalysis.isEmpty() || FormatedAnalysis.trim().isEmpty()){ 
	// System.out.println("No Result");
	RangefinalString = "+\"No Result\"";
} else {
	FormatedAnalysis = analysisContent.substring(fromidx, toidx);
	//System.out.println("Range Analysis output \t \t"+FormatedAnalysis);

	//save it line by line
	String[] rangelines = FormatedAnalysis.split("\n");
	//System.getProperty("line.separator")
rangelines= servlet.makeHTMLqualified(rangelines,rangefinalStringBuilder);
	/* Creating thestring for the textarea */
/* 	for (String s : rangelines) {
		if (!s.equals("")) {
	rangefinalStringBuilder.append(" " + "+\"\\n ").append(s).append("\"")
			.append(System.getProperty("line.separator"));
		}
	} */

	RangefinalString = rangefinalStringBuilder.toString();
	//System.out.println("\n \n\n Range " + RangefinalString);
	System.out.println("\n Range Analysis created");	
}
}
/*===================================================================================  */
%>
<%
System.out.println("\n  Creating Control Flow Graph");
/*Control Flow Graph*/
if (analysisContent == null) {
    System.out.println("The retrieved analysis content is null. Please increase the size of GLOBAL max_allowed_packet.");
    cfgfinalString =null;
}else{	
StringBuilder cfgfinalStringBuilder = new StringBuilder("");
StringBuilder sbbb = new StringBuilder("");

int fidx = analysisContent.indexOf("digraph G {", 0);

int tidx = analysisContent.indexOf("[RangeAnalysis]", fidx);
if (fidx < 0 || fidx > tidx || tidx > analysisContent.length() || tidx < 0) {// FormatedAnalysis == null || FormatedAnalysis.isEmpty() || FormatedAnalysis.trim().isEmpty()){ 
	
	cfgfinalString = "+\"No Result\"";
	cfghelp= "+\" \"";
} else {
	FormatedAnalysis = analysisContent.substring(fidx, tidx);
	//System.out.println("cfg output \t \t"+FormatedAnalysis);

	//save it line by line
	String[] cfglines = FormatedAnalysis.split("\n");

	
		for (int j = 0; j < cfglines.length; j++) {
		//System.out.println(" beforeCFG " +  cfglines[j]);
		if (cfglines[j].contains("->")) {
	cfglines[j] = cfglines[j].replaceAll("->", " -> ");
 	//System.out.println("afterCFG " +  cfglines[j]);
		}
	}
		String[] uriCfglines = cfglines;
		
		for (int j = 0; j < uriCfglines.length; j++) {
		//System.out.println(" uriCfglines " +  cfglines[j]);
			}
		
		cfglines= servlet.makeHTMLqualified(cfglines,cfgfinalStringBuilder);
		cfgfinalString = cfgfinalStringBuilder.toString();
	
String 	keepDigraph = servlet.percentEncoding(uriCfglines, sbbb);

String graphWebsiteLink= "https://dreampuf.github.io/GraphvizOnline/#"+keepDigraph;
//System.out.println("web link: "+ graphWebsiteLink);
//add link to get the graph to the Analysis section
String addLink= "+\"<br>Check out the <a href=\'"+graphWebsiteLink+"\' target=\'_blank\'>CFG Graph</a>\"";
cfgfinalString+=addLink;

cfghelp= "+\"Digraph text is provided so that you can feed it to any tool that can generate the CFG graph for you. Also, a link has been provided to create the CFG graph of your input code for you. <br>Control flow graph (CFG) is a directed graph that shows all the paths that can be traversed during program execution. Edges in CFG portray control flow paths and the nodes in CFG portray basic blocks. Using CFG one can easily locate inaccessible codes of a program and syntactic structures such as loops are easy to find as well. \"";
System.out.println("\n Control Flow Graph created");
}
}
/*===================================================================================  */
%>
<%
System.out.println("\n Creating IPA-Points to ");
	/*IPA-Points to*/
if (analysisContent == null) {
    System.out.println("The retrieved analysis content is null. Please increase the size of GLOBAL max_allowed_packet.");
    ipafinalString =null;
}else{		
StringBuilder ipafinalStringBuilder = new StringBuilder("");

int idxf = analysisContent.indexOf("[IPA:PointsTo] Domain ", 0);
//print from "Exception Type:" to * for the user to show the error to the user
int idxt = analysisContent.indexOf("[RangeAnalysis]", idxf);

if (idxf < 0 || idxf > idxt || idxt > analysisContent.length() || idxt < 0) {// FormatedAnalysis == null || FormatedAnalysis.isEmpty() || FormatedAnalysis.trim().isEmpty()){ 
	ipafinalString = "+\"No Result\"";
} else {
	FormatedAnalysis = analysisContent.substring(idxf, idxt);
	//System.out.println("IPA output \t \t"+FormatedAnalysis);

	//save it line by line
	String[] ipalines = FormatedAnalysis.split("\n");
	//System.getProperty("line.separator")

	
	ipalines= servlet.makeHTMLqualified(ipalines,ipafinalStringBuilder);
	/* Creating the string for the textarea */
/* 	for (String s : ipalines) {
		if (!s.equals("")) {
	ipafinalStringBuilder.append(" " + "+\"\\n ").append(s).append("\"")
			.append(System.getProperty("line.separator"));
		}
	} */

	ipafinalString = ipafinalStringBuilder.toString();
	//System.out.println("\n\n \n" + ipafinalString);
	System.out.println("\n IPA-Points to created");
}
}
/*===================================================================================  */
%>
<%
System.out.println("\n Creating Reduction ");
	/*Reduction*/
if (analysisContent == null) {
    System.out.println("The retrieved analysis content is null. Please increase the size of GLOBAL max_allowed_packet.");
    reductionfinalString =null;
}else{	
StringBuilder reductionfinalStringBuilder = new StringBuilder("");

int fix = analysisContent.indexOf("[Reduction] candidate ", 0);
//print from "Exception Type:" to * for the user to show the error to the user
int tix = analysisContent.indexOf("[LinkSymbol]", fix);

if (fix < 0 || fix > tix || tix > analysisContent.length() || tix < 0) {// FormatedAnalysis == null || FormatedAnalysis.isEmpty() || FormatedAnalysis.trim().isEmpty()){ 
	reductionfinalString = "+\"No Result\"";
} else {
	FormatedAnalysis = analysisContent.substring(fix, tix);
	//System.out.println("Reduction output \t \t"+FormatedAnalysis);

	//save it line by line
	String[] reductionlines = FormatedAnalysis.split("\n");
	//System.getProperty("line.separator")
	for (int j = 0; j < reductionlines.length; j++) {
		if (reductionlines[j].startsWith("[Reduction]")) {
	reductionlines[j] = reductionlines[j].replace("[Reduction]", "");
/* 	if(reductionlines[j].contains("reduction")){
		reductionlines[j] = reductionlines[j].replace("reduction", "<b>"+"reduction"+"</b>");
	} */
	//pvlines[j] = pvlines[j].replaceAll("n\"]", "n]");
		}
		if (reductionlines[j].startsWith("[RangeDomain]")) {
	reductionlines[j] = "";
		}
/* 		if (reductionlines[j].startsWith("[ReductionTransform]")) {
					reductionlines[j] = "";
		} */
	}
	
	
	
	reductionlines= servlet.makeHTMLqualified(reductionlines,reductionfinalStringBuilder);
	/* Creating the string for the textarea */
/* 	for (String s : reductionlines) {
		if (!s.equals("")) {
	reductionfinalStringBuilder.append(" " + "+\"\\n ").append(s).append("\"")
			.append(System.getProperty("line.separator"));
		}
	} */

	reductionfinalString = reductionfinalStringBuilder.toString();
	//System.out.println("\n\n \n" + reductionfinalString);
	System.out.println("\n Reduction created");
}
}
/* 
[Reduction] candidate = ( + : i )
[Reduction] candidate = ( + : sum )
[Reduction] candidate: i
[Reduction] i is referenced in the non-reduction statement!
[Reduction] candidate: sum
[Reduction] reduction = {(+:sum)}

[LinkSymbol] 11 updates in 0.00 seconds */
/*===================================================================================  */
%>
<%
System.out.println("\n Creating Branch Eliminator");
	/*Branch Eliminator*/
if (analysisContent == null) {
    System.out.println("The retrieved analysis content is null. Please increase the size of GLOBAL max_allowed_packet.");
    branchEfinalString =null;
}else{
StringBuilder branchEfinalStringBuilder = new StringBuilder("");

int befindx = analysisContent.indexOf("[BranchEliminator] Removed Branches", 0);
//print from "Exception Type:" to * for the user to show the error to the user
int betindx = analysisContent.indexOf("[LinkSymbol]", befindx);
if (befindx < 0 || befindx > betindx || betindx > analysisContent.length() || betindx < 0) {// FormatedAnalysis == null || FormatedAnalysis.isEmpty() || FormatedAnalysis.trim().isEmpty()){ 
	branchEfinalString = "+\"No Result\"";
} else {
	FormatedAnalysis = analysisContent.substring(befindx, betindx);
	//System.out.println("Branch Eliminator output \t \t"+FormatedAnalysis);

	//save it line by line
	String[] branchElines = FormatedAnalysis.split("\n");
	//System.getProperty("line.separator")
	for (int j = 0; j < branchElines.length; j++) {
		if (branchElines[j].startsWith("[BranchEliminator]")) {
	branchElines[j] = branchElines[j].replace("[BranchEliminator]", "");
	//pvlines[j] = pvlines[j].replaceAll("n\"]", "n]");
		}
	}
	
	branchElines= servlet.makeHTMLqualified(branchElines,branchEfinalStringBuilder);
	/* Creating the string for the textarea */
/* 	for (String s : branchElines) {
		if (!s.equals("")) {
	branchEfinalStringBuilder.append(" " + "+\"\\n ").append(s).append("\"")
			.append(System.getProperty("line.separator"));
		}
	} */

	branchEfinalString = branchEfinalStringBuilder.toString();
	//System.out.println("\n\n \n" + branchEfinalString);
	System.out.println("\n Branch Eliminator created");
}
}
/* 
[BranchEliminator] Removed Branches:
[BranchEliminator] IF    : 0
[BranchEliminator] FOR   : 0
[BranchEliminator] WHILE : 0
[BranchEliminator] DO    : 0
[BranchEliminator] SWITCH: 0
[BranchEliminator] CASE  : 0
[LinkSymbol] 11 updates in 0.00 seconds */
/*===================================================================================  */
%>
<%
System.out.println("\n Creating Call Graph");
	/*Call Graph*/

StringBuilder callGfinalStringBuilder = new StringBuilder("");
StringBuilder sbWithLinks = new StringBuilder("");
if (passesContent==null || passesContent.isEmpty()){
// int callgfindx = passesContent.indexOf("digraph {", 0);
// //System.out.println("call graph callgfindx from\t \t" + callgfindx);

// //Ending Index Calculation
// int callgindexx = passesContent.indexOf("[SingleDeclarator]", callgfindx);
// //System.out.println("call graph callgindexx to\t \t" + callgindexx);

// int callgindex = passesContent.indexOf("[SingleCall]", callgfindx);
// //System.out.println("call graph callgindex to\t \t" + callgindex);

// int callgtindx = passesContent.indexOf("[IVSubstitution]", callgfindx);
//System.out.println("call graph callgtindx to\t \t" + callgtindx);

/* System.out.println("length \t \t"+passesContent.length());
System.out.println("callgfindx>callgtindx \t \t"+ (callgfindx>callgtindx));
System.out.println("callgfindx>callgindex \t \t"+(callgfindx>callgindex));
System.out.println("callgfindx>callgindexx \t \t"+(callgfindx>callgindexx));
System.out.println("callgindexx>passesContent.length() \t \t"+(callgindexx>passesContent.length()));
System.out.println("callgtindx>passesContent.length() \t \t"+(callgtindx>passesContent.length()));
System.out.println("callgindex>passesContent.length() \t \t"+(callgindex>passesContent.length())); */

//if (callgfindx < 0) {
	callGfinalString = "+\"No Result\"";
	callGhelp= "+\" \"";
	//System.out.println("\n\n \n" + callGfinalString);
} else {

// 	if (callgindexx > 0 && callgindexx < callgtindx && callgindexx < callgindex) {
// 		FormatedAnalysis = passesContent.substring(callgfindx, callgindexx);
// 	} else if (callgindex > 0 && callgindex < callgtindx) {
// 		FormatedAnalysis = passesContent.substring(callgfindx, callgindex);
// 	} else if (callgtindx > 0) {
// 		FormatedAnalysis = passesContent.substring(callgfindx, callgtindx);
// 	}
	FormatedAnalysis= passesContent;
	String[] callGlines = FormatedAnalysis.split("\n");
	 for (String line : callGlines) {
         System.out.println(line);
     }
	for (int j = 0; j < callGlines.length; j++) {

		if (callGlines[j].contains("size") || callGlines[j].contains("orientation")
		|| callGlines[j].startsWith("[LoopNormalization]") || callGlines[j].startsWith("[NormalizeReturn]")
		|| callGlines[j].trim().length()==0) {
	callGlines[j] = "".trim();
		}
		//System.out.println("Call graph lines befote HTMLized: "+ callGlines[j]);
	}
	
	callGlines= servlet.makeHTMLqualified(callGlines,callGfinalStringBuilder);
	System.out.println("\n\n callGlines: ");
	 for (String line : callGlines) {
         System.out.println(line);
     }
/* 	for (String s : callGlines) {
		if (!s.equals("")) {
	callGfinalStringBuilder.append(" " + "+\"\\n ").append(s).append("\"")
			.append(System.getProperty("line.separator"));
		}
	} */

	callGfinalString = callGfinalStringBuilder.toString();
	System.out.println("\n\n callGfinalString: "+ callGfinalString);
	
	String[] copyCallGlines=callGlines;
	StringBuilder callGStringBuilder = new StringBuilder("");
	System.out.println("\n\ncopyCallGlines: ");
	 for (String line : copyCallGlines) {
         System.out.println(line);
     }
	String 	percentEncodedDigraph = servlet.percentEncoding(copyCallGlines, callGStringBuilder);
	System.out.println("\n\npercentEncodedDigraph: "+ percentEncodedDigraph);
	
	String graphWebsiteLink= "https://dreampuf.github.io/GraphvizOnline/#"+percentEncodedDigraph;
	System.out.println("\n\nweb link: "+ graphWebsiteLink);
    // Split the long URL string into multiple parts
//     StringBuilder addLinkBuilder = new StringBuilder();
//     int len = graphWebsiteLink.length();
//         int chunkSize= 80;
//     int numChunks = (int) Math.ceil((double) len / chunkSize);
//     String[] chunks = new String[numChunks];
//     //String[] parts;// = splitLongString(graphWebsiteLink, 80);
//     for (int i = 0; i < numChunks; i++) {
//         int start = i * chunkSize;
//         int end = Math.min(start + chunkSize, len);
//         chunks[i] = graphWebsiteLink.substring(start, end);
//     }
//     String[] parts=chunks;
//  // Append each part to the StringBuilder
//     for (String part : parts) {
//         addLinkBuilder.append(part);
//     }
    //graphWebsiteLink=addLinkBuilder.toString();
	//add link to get the graph to the Analysis section
	String addLink= "+\"<br>Check out the <a href=\'"+graphWebsiteLink+"\' target=\'_blank\'>Call Graph</a>\"";
    
	callGfinalString+= addLink;//addLinkBuilder.toString();//

	callGhelp= "+\"Digraph text is provided so that you can feed it to any tool that can generate the Call graph for you. Also, a link has been provided to create the Call graph of your input code for you. <br> \"";
	//find procedure names and replace them with <a tag
	//find all procedure names on the callgraph 
	//replace those procedure names with <a tags >
	String linkProceduresToCalls = callGfinalStringBuilder.toString();
	String replaceProcCalls=null;
	int procFromIndex= 0;
	int procToIndex=0;
	int procfidx=0;
	int proctidx=0;
	String procName=null;
	String[] lpclines = linkProceduresToCalls.split("\n");
	String[] copylpclines =lpclines;
	for (int j = 0; j < lpclines.length; j++) {
		lpclines[j]=lpclines[j].replace("{ }","{  }");
		System.out.println("\n lpclines"+j +"="+ lpclines[j]);
		procFromIndex = lpclines[j].indexOf("+\"", 0);
		
		System.out.println("procFromIndex"+procFromIndex);
		if (procFromIndex>=0){
		procToIndex = lpclines[j].indexOf("-&gt;", procFromIndex);
		System.out.println("procToIndex"+procToIndex);
		}
		if (procFromIndex>=0 && procToIndex >=0 && procToIndex >procFromIndex){
			procName = lpclines[j].substring(procFromIndex+2, procToIndex-1);
			System.out.println("\n procedure name in line"+j +"="+ procName);
			lpclines[j]=lpclines[j].replace(procName, "<a href=\'#"+procName+"\'>"+procName+"</a>");
			System.out.println("\n lpclines"+j +"="+ lpclines[j]);
			//the second half of the string to be processed.
			 procFromIndex = lpclines[j].indexOf(" { ", procToIndex);
			System.out.println("\n second func procFromIndex"+procFromIndex);
			procToIndex = lpclines[j].indexOf("};", procFromIndex);
			System.out.println("\n second func procToIndex"+procToIndex);
			
			procfidx=lpclines[j].indexOf(" ", procFromIndex+1);
			System.out.println("\n space found at index"+"="+procfidx);
			while (procfidx< procToIndex-2 ){
								proctidx=lpclines[j].indexOf(" ", procfidx+1);	
				System.out.println("\n space found at index"+"="+ proctidx);
				procName = lpclines[j].substring(procfidx+1, proctidx);
				System.out.println("\n procedure name in line"+j+"="+procName);
				if(!procName.trim().isEmpty() && !procName.equals(null)&& !procName.contains("<") ){
				copylpclines[j]=copylpclines[j].replace(procName,"<a href=\'#"+procName+"\'>"+procName+"</a>");
				System.out.println("\n lpclines"+j+"="+lpclines[j]); 
				procfidx= lpclines[j].indexOf(" ", proctidx);
				System.out.println("\n procfidx update to "+procfidx);
				procToIndex = lpclines[j].indexOf("};", procfidx);
				System.out.println("\n procToIndex "+procToIndex);
				System.out.println("\n copylpclines "+j+"="+copylpclines[j]);
				}else{
					procfidx= lpclines[j].indexOf(" ", proctidx);
					System.out.println("\n procfidx update to "+procfidx);
					procToIndex = lpclines[j].indexOf("};", procfidx);
					System.out.println("\n procToIndex "+procToIndex);
				}
				//procfidx=proctidx;
				//System.out.println("\n proctidx"+j+"="+proctidx);
			}
			//procName = lpclines[j].substring(procFromIndex+3, procToIndex-1);
			//System.out.println("\n procedure name in line"+j+"="+procName);
			//if(!procName.trim().isEmpty() && !procName.equals(null) ){
			//System.out.println("\n procedure name in line"+"="+procName);
			//lpclines[j]=lpclines[j].replace(procName,"<a href=\'#"+procName+"\'>"+procName+"</a>");
			//System.out.println("\n lpclines"+j+"="+lpclines[j]); 
			//}
		}
	}
	
	for (String s : copylpclines) {
		if (!s.equals("")) {
			sbWithLinks.append(s);
		}
	}
	//System.out.println("new output"+sbWithLinks.toString());
	callGfinalString=sbWithLinks.toString()+addLink;
	System.out.println("\n\n \n" + callGfinalString);
	System.out.println("\n Call Graph created");
}


// 	}else if (passesContent==null){
// 		callGfinalString="";
// 	}
	/* int procFromIndex= 0;
	int procToIndex=2;
	while(procFromIndex>=0 && procToIndex>=0 && procFromIndex<procToIndex ){
	procFromIndex = linkProceduresToCalls.indexOf("+\"", procToIndex);
	//System.out.println("call graph callgfindx from\t \t" + callgfindx);
	String procNames=null;
	procToIndex = linkProceduresToCalls.indexOf("-&gt;", procFromIndex);
	if (procToIndex>=0){
		procNames = linkProceduresToCalls.substring(procFromIndex+2, procToIndex-1);
		replaceProcCalls=linkProceduresToCalls.replace(procName, "<a href=\"#"+procName+"\"> ");
		System.out.println("\nprocedure Name:" + procName);
		System.out.println("\nprocedure Name:" + linkProceduresToCalls);
		System.out.println("\nreplaceProcCalls:" + replaceProcCalls);
	}else{
		//do nothing
	} */
//	}
	//System.out.println("\n\n \n" + callGfinalString);

//}
/*===================================================================================  */
%>
<%
System.out.println("\n Creating Induction Variable");
	/*Induction Variable*/
if (analysisContent == null) {
    System.out.println("The retrieved analysis content is null. Please increase the size of GLOBAL max_allowed_packet.");
    ivfinalString =null;
}else{	
StringBuilder ivfinalStringBuilder = new StringBuilder("");

int ivfindx = analysisContent.indexOf("[IVSubstitution] Found", 0);
//System.out.println("\n\n \nInduction Variable start " + ivfindx);
int ivtindx = analysisContent.indexOf("[LinkSymbol]", ivfindx);
//System.out.println("\n\n \n Induction Variable stop " + ivtindx);
//System.out.println("\n\n \n" + analysisContent);

if (ivfindx < 0 || ivfindx > ivtindx || ivtindx > analysisContent.length() || ivtindx < 0) {
	ivfinalString = "+\"No Result\"";
} else {

	FormatedAnalysis = analysisContent.substring(ivfindx, ivtindx);
System.out.println("IV output \t \t"+FormatedAnalysis);

	String[] ivlines = FormatedAnalysis.split("\n");
	//[IVSubstitution] Successfully replaced
	for (int j = 0; j < ivlines.length; j++) {
		if (!ivlines[j].startsWith("[IVSubstitution] Successfully replaced")) {
			ivlines[j] = "";
		}
		
	}
	
	ivlines= servlet.makeHTMLqualified(ivlines,ivfinalStringBuilder);


/* 	for (String s : ivlines) {
		if (!s.equals("")) {
	//ivfinalStringBuilder.append(" " + "+\"\\n ").append(s).append("\"").append(System.getProperty("line.separator"));
			ivfinalStringBuilder.append("+\"").append(s).append("<br>\"").append(System.getProperty("line.separator"));
		}
	} */

ivfinalString = ivfinalStringBuilder.toString();
System.out.println("\n\n \n" + ivfinalString);
System.out.println("\n Induction Variable created");
}
}
/* 
[IVSubstitution] Analysis result:
#1 for (i=1; i<n; i ++ ) ivs=[], variants=[sum]
[IVSubstitution] Transformation starts...
[LinkSymbol] 11 updates in 0.00 seconds */
/*===================================================================================  */
%>
<%
System.out.println("\n Creating private var");
	/*private var*/
	
if (analysisContent == null) {
    System.out.println("The retrieved analysis content is null. Please increase the size of GLOBAL max_allowed_packet.");
    privatefinalString =null;
}else{	
StringBuilder privatefinalStringBuilder = new StringBuilder("");
String[] pvlines = analysisContent.split("\n");

for (int j = 0; j < pvlines.length; j++) {
	if (!pvlines[j].startsWith("[ArrayPrivatization]")) {
		pvlines[j] = "";
	}
	if (pvlines[j].startsWith("[ArrayPrivatization] Procedure:")) {
		pvlines[j] = "";
	}
	if (pvlines[j].startsWith("[ArrayPrivatization] ......")) {
		pvlines[j] = "";
	}
}

for (int j = 0; j < pvlines.length; j++) {
	if (pvlines[j].startsWith("[ArrayPrivatization]")) {
		pvlines[j] = pvlines[j].replace("[ArrayPrivatization]", "");
		//pvlines[j] = pvlines[j].replaceAll("n\"]", "n]");
	}
}


pvlines= servlet.makeHTMLqualified(pvlines,privatefinalStringBuilder);
/* for (String s : pvlines) {
	if (!s.equals("")) {
		privatefinalStringBuilder.append(" " + "+\"\\n ").append(s).append("\"")
		.append(System.getProperty("line.separator"));
	}
}
 */
privatefinalString = privatefinalStringBuilder.toString();
//System.out.println("\n\n \n" + privatefinalString);
System.out.println("\n Private var created");
System.out.println("\n outputContent"+ outputContent);
}

String setMessage="";
setMessage = request.getParameter("action");
/*===================================================================================  */
%>
<script>

	function load(){
		var divInput= document.getElementById("cetusinput");
		divInput.innerHTML = ""<%=inputFinalString%>; 
		var divoutput= document.getElementById("cetusoutput");
		divoutput.innerHTML = ""<%=outputcetusFinalString%>; 
		/* prettify the code */
		PR.prettyPrint();
				
		document.getElementById("ReCompile").disabled = true;
		document.getElementById("NewCompile").disabled = true;
		if("<%=setMessage%>" === "ReCompile" ){
			document.getElementById("recompiled").innerHTML="Code successfully recompiled!";
		}else{
			document.getElementById("recompiled").innerHTML="<br>";
			}
		}
	</script>
</head>
<body  onload="load()"  Style="background-color: rgba(192, 192, 192, 0.2);">
<% 
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
<%--  <jsp:include page="../../download.jsp" flush="true" >
  <jsp:param name="filenamePara" value="<%=outputFileName %>" />
<jsp:param name="filepathPara" value= "<%=pathToFile %>" /> 
</jsp:include>  --%>


	<div id="pageloader">
   <img id="pageloaderimg" src="<%=pathWebcontent%>/resources/img/bubble.gif" width=auto height=auto frameBorder="0" alt="processing..." />
   <label id="pageloaderimg" for="pageloaderimg">Please be patient while the process is being completed...</label>
</div>    
	<nav class="navbar navbar-dark bg-primary">
		<a class="navbar-brand" href="#">
			<p style="font-size: 24px;">
				<img src="<%=request.getContextPath()%>/resources/img/iCetus.png"
					width="70" height="70"
					Style="vertical-align: middle; margin: 10px 10px;"
					class="d-inline-block " alt="iCetus"> iCetus, A
				Source-to-Source Compiler Infrastructure for C Programs
			</p>
		</a>
	</nav>
	<br>

	<button type="button" class="btn btn-primary" style="align: left; margin-left:20px;"
		onclick="location.href = '<%=request.getContextPath()%>/';">Back</button> 
		<button type="button" class="btn btn-primary"  id="cetusFlags" style="align: right; margin-right:20px; margin-left: auto; float: right;"
		onclick="cetus_report()">Show Flags</button> 
		<br>
	<%-- <div style="align: left;margin: 10px 10px;font-weight:bold;">Cetus Options:</div> <%=allOptions %> --%>
	
<%-- 	<label for="cetusOptions" style="margin: 5px 20px;font-weight:bold;vertical-align: middle;">Cetus Options:</label>
<textarea id="cetusOptions" name="cetusOptions" rows="1" style="width:100%;margin: 5px 20px;" ><%=allOptions %></textarea> --%>

<!-- <iframe src="https://dreampuf.github.io/GraphvizOnline/#digraph%20G%20%7B%20node0%20%5Blabel%3D%220%20%5B%5D%20FLOW%20ENTRY%20%22%5D%20node1%20%5Blabel%3D%226%20%5Bi%3Dn%2C%201000000%3C%3Dn%3C%3Di%5D%20FOREXIT%20%22%5D%20node2%20%5Blabel%3D%228%20%5B_ret_val_0%3D0%2C%20i%3Dn%2C%201000000%3C%3Dn%3C%3Di%5D%20return%20_ret_val_0%3B%20%22%5D%20node3%20%5Blabel%3D%221%20%5B%5D%20n%3D1000000%3B%20%22%5D%20node4%20%5Blabel%3D%222%20%5Bn%3D1000000%5D%20i%3D1%3B%20%22%5D%20node5%20%5Blabel%3D%223%20%5B1%3C%3Di%3C%3Dn%2C%20n%3D1000000%5D%20i%3Cn%3B%20%22%5D%20node6%20%5Blabel%3D%224%20%5B1%3C%3Di%3C%3D(-1%2Bn)%2C%20n%3D1000000%5D%20sum%3D(sum%2Ba%5Bi%5D)%3B%20%22%5D%20node7%20%5Blabel%3D%225%20%5B1%3C%3Di%3C%3D(-1%2Bn)%2C%20n%3D1000000%5D%20i%3D(i%2B1)%3B%20%22%5D%20node8%20%5Blabel%3D%227%20%5Bi%3Dn%2C%201000000%3C%3Dn%3C%3Di%5D%20_ret_val_0%3D0%3B%20%22%5D%20node0%20-%3E%20node3%3B%20node1%20-%3E%20node8%3B%20node3%20-%3E%20node4%3B%20node4%20-%3E%20node5%3B%20node5%20-%3E%20node1%3B%20node5%20-%3E%20node6%3B%20node6%20-%3E%20node7%3B%20node7%20-%3E%20node5%3B%20node8%20-%3E%20node2%3B%20%7D" height="600" width="800" title="Iframe Example"></iframe>
 --><form id="outputForm" autocomplete="off"
		action="<%=request.getContextPath()%>/" method="post"
		enctype="multipart/form-data">
		<table style="width: 100%">
<p class="formfield">

   <div class="row" id="cetus_row" style="display: none;">
   <label for="cetusOptions" style="margin: 5px 40px;font-weight:bold;">Cetus Options:</label>
   <textarea id="cetusOptions" name="cetusOptions" rows="2" style="width:98%;margin-left:40px;" ><%=allOptions %></textarea> 
   </div>
   
<!--    <div class="row" id="note" > -->
<!--    <textarea id="dbNote" name="dbNote" rows="1" style="width:98%;margin-left:40px;" >Queries cannot be executed on the application due to the large size of the debugger file. The necessary information to run queries is not stored in the DB. Please increase the size of GLOBAL max_allowed_packet. </textarea>  -->
<!--    </div> -->
  <%--<%=allOptions %>  --%>

<!--Set Recompile parameters, as it goes directly to servlet -->
	    <input type="hidden" id="usercode" name="usercode" > <!--  modified input code-->
	    <input type="hidden" id="codeRadios" name="codeRadios" value="code"> <!-- type of file -->
	    <input type="hidden" id="gridRadios" name="gridRadios" value="semi-auto"> <!-- parallelization type -->
	    <input type="hidden" id="allCetusOptions" name="allCetusOptions" value="<%=allOptions %>"> 
	     <input type="hidden" id="executeOnly" name="executeOnly" value=""> <!-- if the code should only be executed and not compiled set the vale to execute -->
<%-- 	     <input type="hidden" id="filenamePara" name="filenamePara" value="<%=outputFileName %>">
	     <input type="hidden" id="filepathPara" name="filepathPara" value="<%=pathToFile %>"> --%>
<%-- <%=allOptions%> --%>
</p>

	<div align="center">
	
			<!-- --------------------  -->
			<!-- Cetus Input & Output  -->
			<!-- --------------------  -->
			<div class="row">

				<div class="column">

					
					<div class="form-row">
						<div align="left">
							Database Entry id:
							<%=resultSet.getInt("id")%></div>
					</div>
					<div class="form-row">
						<div align="left">
							<h4 id="modifiableInput">Input <%=file %></h4>
						</div>
					</div>

					 <div class="form-row">
						<%-- 						<div align="left">
							Cetus Input File Path: <br><%=resultSet.getString("input_code")%></div> --%>
					</div>

					<%-- <div class="form-row">
<div align="left"> View Cetus Input File: <a href=<%=resultSet.getString("input_code") %> target=_blank>View Cetus Input File </a></div>
</div>
<div class="form-row">
<div align="left"> View Cetus Input File: <%=resultSet.getString("input_content") %> View Cetus Input File </div>
</div> --%>

 					<div class="form-row">
						<div align="left">
							<!-- Cetus Input File Content: <br> -->
<%-- 							<textarea rows="15" readonly=" readonly" WRAP="off"
								style="border-radius: 10px; padding: 25px; width: 100%; box-sizing: border-box;"><%=user.getFileOutput(resultSet.getString("input_code"))%></textarea> --%>
						</div>
					</div> 
						<div class="form-row">
							<div align="left">
								<div class="container" >
								<!-- class="prettyprint lang-c" -->
<!--  								<code  id="cetusinput" name="cetusinput" contentEditable=true rows="12" onclick="EnableCompileButtons()" contentEditable
										style="border-radius: 10px; width: 100%; padding: 25px; box-sizing: border-box;" onclick="EnableCompileButtons()">  -->
 								<code class="prettyprint lang-c" id="cetusinput"  rows="12"
										style="border-radius: 10px; width: 100%; padding: 25px; box-sizing: border-box;" >
									 </code> 
									<!-- </code>  -->
								</div>
							</div>
						</div>


				</div>

				<div class="column">

					<div class="form-row">
						<div align="left">
							<div align="left" class="form-row" id="recompiled" style="margin-left: 0px;"></div>
						</div>
					</div>

					<div class="form-row">
						<div align="left">
							<h4 id="modifiable">Output</h4>
						</div>
					</div>

					<div class="form-row">
						<%-- 						<div align="left">
							Cetus Output File Path: <br><%=resultSet.getString("cetus_output_filepath")%></div> --%>
					</div>

					<%-- <div class="form-row">
<div align="left"> View Cetus Passes Report: <a href=<%=resultSet.getString("cetus_output_filepath") %> target=_blank>View Cetus Output File </a></div>
</div> --%>

 					<div class="form-row">
						<div align="left">
							<!-- Cetus output Content: <br> -->
							<%-- <textarea rows="15" readonly=" readonly" WRAP="off"
								style="border-radius: 10px; padding: 25px; width: 100%; box-sizing: border-box;"><%=user.getFileOutput(resultSet.getString("cetus_output_filepath"))%></textarea> --%>
							
						</div>
					</div> 
					
						<div class="form-row">
							<div align="left">
								<div class="container">
								<!-- class="prettyprint lang-c " -->
							 	<!-- <code  id="cetusoutput" style="border-radius: 10px; width: 100%; padding: 25px; box-sizing: border-box;"> contentEditable   -->
 								<code class="prettyprint lang-c" id="cetusoutput" rows="12"
										style="border-radius: 10px; width: 100%; padding: 25px; box-sizing: border-box;">
									</code>  
									<!--  </code> -->
									
									
								</div>
							</div>
						</div>


				</div>
			</div>


			<!-- --------------------------  -->
			<!-- Add Query Select list here  -->
			<!-- --------------------------  -->
			<!-- showing the options if the required info are there to be extracted -->
			<div class="row">

				<div class="columnselect">
					<div class="form-row">
<!-- same name has been assigned to get the parameter on the servlet section. input code should be rewritten- cetus options in case of ReCompile should be passed.
 -->
 				 <button type="button" id="modifyInput"  class="btn btn-primary" onclick="changeInput()" >Modify Input</button>&nbsp;<span><i
						data-content="To modify the input code."
						data-placement="top" class="fa fa-question-circle"></i></span> 			
 					<button type="submit" id="ReCompile" name="action" value="ReCompile" class="btn btn-primary" onclick="setParasforservlet()" >ReCompile</button>&nbsp;<span ><i id="helpReCompile"
						data-content="Compiling the modified code with the same parallelization options."
						data-placement="top" class="fa fa-question-circle"></i></span>
					<button type="submit" id="NewCompile" name="action" value="NewCompile" class="btn btn-primary" onclick="setParasforIndex()">New Compilation</button>&nbsp;<span ><i id="helpNewCompile"
						data-content="Compiling the modified code with a new set of parallelization options."
						data-placement="top" class="fa fa-question-circle"></i></span>
					<br>
					<span align="left"  id="codeModified" style="margin-left: 0px;"></span>	
					</div>
				</div>
</form>

				<div class="columnselect">
				<div class="form-row">
				<div align="left">
				 <button type="button" id="modifyOutput"  class="btn btn-primary buttonwide" onclick="changeOutput()" >Modify Output</button>&nbsp;<span><i
						data-content="To hand parallelize the code or simply modify OpenMP directives use this button. Correctness of the output is not guaranteed and it is all developer's responsibility."
						data-placement="top" class="fa fa-question-circle"></i></span> 
				<button type="submit" name="action" id="displayExecTime"  class="btn btn-primary buttonwide" onclick="setParasforExecutionTime()" >Execute the Code</button>&nbsp;<span><i
						data-content="Displays the execution time of the serial code and the parallel code after making chnages to it. "
						data-placement="top" class="fa fa-question-circle"></i></span> 
<!-- 						 <button type="submit" name="action" id="downloadOutput"  class="btn btn-primary buttonwide" onclick="document.forms[0].action = 'download.jsp'; return true;"  >Download output</button>&nbsp;<span><i
						data-content="Downloads output file from the server. "
						data-placement="top" class="fa fa-question-circle"></i></span>  -->
						<a href="<%=pathWebcontent%>/download.jsp">Download output file</a>  
				<!-- <button type="button" id="displayExecResult"  class="btn btn-primary buttonwide" onclick="changeOutput()" >Display Execution Result</button>&nbsp;<span><i
						data-content="To ensure the code has been parallelized correctly, it displays execution result of the serial and the parralel code."
						data-placement="top" class="fa fa-question-circle"></i></span>  -->
				</div>
				
				</div>
					<div class="form-row">
						<div align="left">
							<label class="form-check-label" for="queries"> <b>Run
									a Query on your program</b>
							</label> <select class="form-select" name="queries" id="queries"
								onchange="showResult()">
								<option value="0" selected>Select your query</option>
								<option value="DDT" class="highlight-yellow">Show me
									data dependencies</option>
								<option value="Range">Show me variable ranges</option>
								<option value="CFG">Show me Control Flow of the program</option>
								<option value="IPA">Show me points to analysis</option>
								<option value="Reduction" class="highlight-purple">Show
									me Reduction analysis</option>
								<option value="InductionV" >Show
									me Induction Variable analysis</option>
								<option value="BranchEliminator">Show me branches that
									can be eliminated</option>
  								<option value="callGraph">Show me the callgraph</option> 
								<option value="private" class="highlight-blue">Show me
									private variable analysis</option>
								<option value="execute" >Show me
									speedup and efficiency analysis</option>
								<option value="executionResult" >Show me
									execution result of the program</option>
								<option value="profile" >Show me
									profiling info </option>	
								
							</select>
						</div>
					</div>
				</div>

			</div>

			<!-- --------------------------  -->
			<!-- Cetus Output with Cetus Pargmas
			 Cetus Query Result         style="display: none;"  -->
			<!-- --------------------------  -->

			<div class="row" id="queryResult" style="display: none;">

				<div class="column">
				<!-- 	<div class="container"> -->
						<!-- set to create the space and line numbers -->
						<div class="form-row">
							<div align="left">
								<h4>Output with Loop Names</h4>
							</div>
						</div>
<!-- 						<div class="form-row">
							<div align="left">
								<div class="container">
								<textarea contentEditable id="formattedOutput"  rows="15" 
									WRAP="off"
									style="border-radius: 10px; padding: 25px; width: 100%; box-sizing: border-box; "></textarea>
									readonly=" readonly"
									</div>
							</div>
						</div>  -->
						<div class="form-row">
							<div align="left">
								<div class="container">
								
									<div id="textarea" contentEditable=false rows="12"
										style="border-radius: 10px; width: 100%; padding: 25px; box-sizing: border-box;">
									</div>
								
								</div>
							</div>
						</div>

					<!-- </div> -->

				</div>

				<div class="column">


					<div class="form-row">
						<div align="left">
							<h4>Query Result</h4>
						</div>
					</div>

<!-- 					<div class="form-row">
						<div align="left">

							<textarea id="formattedAnalysis" rows="15" readonly=" readonly"
								WRAP="off"
								style="border-radius: 10px; padding: 25px; width: 100%; box-sizing: border-box;"></textarea>
						</div>
					</div> -->
						<div class="form-row">
							<div align="left">
								<div class="container">
									<div id="formattedAnalysis" rows="12"
										style="border-radius: 10px; width: 100%; padding: 25px; box-sizing: border-box;">
									</div>
								</div>
							</div>
						</div>
						
					<div class="form-row">
						<div align="left">
							<h5>Explain query results to me</h5>
						</div>
					</div>

						<div class="form-row">
							<div align="left">
							
									<div id="explainAnalysis" rows="5"
										style="border-radius: 10px; width: 100%; padding: 5px; box-sizing: border-box;text-align: justify; text-justify: inter-word;">
									</div>
								
							</div>
						</div>






				</div>

			</div>

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
								style="border-radius: 10px; padding: 25px; width: 100%; box-sizing: border-box;"><%=passesContent%></textarea>
<!-- 						String passesContent = user.getFileOutput(resultSet.getString("cetus_passes_filepath")); -->
						</div>
					</div>
<a href="<%=pathWebcontent%>/downloadPasses.jsp">Download Cetus Passes File</a>  
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
<a href="<%=pathWebcontent%>/downloadAnalysis.jsp">Download Cetus Analysis File</a>  
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
			
			
//hovering over question mark shows the popover contents	
$(function() {
			$('.fa').popover({
				trigger : "hover"
			});
})
		

		
function showResult() {

      document.getElementById("queryResult").style.display="";
/* 	  var userOutput= document.getElementById("formattedOutput");*/
 	  var divInput= document.getElementById("cetusinput");
<%--  	 divInput.innerHTML = ""<%=inputFinalString%>;  --%>
 	  
 	  var divOutput=document.getElementById("textarea");
	  var userAnalysis= document.getElementById("formattedAnalysis");
	  var helpAnalysis= document.getElementById("explainAnalysis");
	  var selectOption= document.getElementById("queries");
	  <%-- var output= ""<%=outputFinalString%>;  --%>
	 //if timers are added then output should ebe read from DB
	  divOutput.innerHTML= ""<%=outputDivString%>; 
	  // if DDT is selected then Cetus output and Cetus analysis should be set
	  if (selectOption.value === "DDT") {
	      //option="DDT";
	      divOutput.innerHTML= ""<%=colorDep%>;
		   userAnalysis.innerHTML = ""<%=DDTfinalString%>;
		   helpAnalysis.innerHTML = ""<%=ddthelp%>;
		}else if (selectOption.value === "Range") {
			//userOutput.value = output; 
			userAnalysis.innerHTML = ""<%=RangefinalString%>;
			helpAnalysis.innerHTML = "  ";
		}else if (selectOption.value === "CFG") {
			//userOutput.value = output; 
			//
			userAnalysis.innerHTML = ""<%=cfgfinalString%>;
			helpAnalysis.innerHTML = ""<%=cfghelp%>;
		}else if (selectOption.value === "IPA") {
			//userOutput.value = output; 
			userAnalysis.innerHTML = ""<%=ipafinalString%>;
			helpAnalysis.innerHTML = " ";
		}else if (selectOption.value === "Reduction") {
			//userOutput.value = output; 
	 		divOutput.innerHTML= ""<%=colorReduction%>; 
			//option="Reduction";
			userAnalysis.innerHTML = ""<%=reductionfinalString%>;
			helpAnalysis.innerHTML = " ";
		}else if (selectOption.value === "InductionV") {
			//userOutput.value = output;
			//option="InductionV";
			userAnalysis.innerHTML = ""<%=ivfinalString%>;
			helpAnalysis.innerHTML = " ";
		}else if (selectOption.value === "BranchEliminator") {
			//userOutput.value = output; 
			userAnalysis.innerHTML = ""<%=branchEfinalString%>;
			helpAnalysis.innerHTML = " ";
 		} else if (selectOption.value === "callGraph") {
// 			//userOutput.value = output; 
<%-- 			divOutput.innerHTML = ""<%=callgOutput%>; --%>
 			userAnalysis.innerHTML = ""<%=callGfinalString%>;  
 			helpAnalysis.innerHTML = ""<%=callGhelp%>; 
		}else if (selectOption.value === "private") {
			//userOutput.value = output;
 		divOutput.innerHTML= ""<%=colorPrivate%>; 
			//option="private";
			userAnalysis.innerHTML = ""<%=privatefinalString%>;
			helpAnalysis.innerHTML = " ";
		}else if (selectOption.value === "execute") {
			//userOutput.value = output;
	 		userAnalysis.innerHTML = ""<%=exefinalString%>;
	 		helpAnalysis.innerHTML = " ";
		}else if (selectOption.value === "executionResult") {
			//userOutput.value = output;
	 		userAnalysis.innerHTML = ""<%=exeResultfinalString%>;
	 		helpAnalysis.innerHTML = ""<%=exeResulthelp%>;
		}else if (selectOption.value === "profile") {
			//userOutput.value = output;
	 		userAnalysis.innerHTML = ""<%=prffinalString%>;
	 		helpAnalysis.innerHTML = ""<%=profilerhelp%>;
		}else if (selectOption.value === "0") {
			document.getElementById("queryResult").style.display="none";
			//userOutput.value = ""; 
			userAnalysis.innerHTML = "";
		}
	}
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
  <%-- 	divoutput.textContents = ""<%=outputContent%>;   --%>
		<%-- 	divoutput.innerHTML = ""<%=outputDivString%>;  --%>
<%-- 		//divoutput.value = ""<%=outputDivString%>.innerText; --%>
 		divoutput.innerHTML = ""<%=outputDivString%>; 
		<%--  divoutput.innerHTML = ""<%=outputcetusFinalString%>;   --%> 
		/* prettify the code */
		PR.prettyPrint();
				
		document.getElementById("ReCompile").disabled = true;
		document.getElementById("NewCompile").disabled = true;
		document.getElementById("displayExecTime").disabled = true;
		if("<%=setMessage%>" === "ReCompile" ){
			document.getElementById("recompiled").innerHTML="Code successfully recompiled!";
		}else{
			document.getElementById("recompiled").innerHTML="<br>";
			}
		} 
	
	
<%--  	function EnableCompileButtons(){
		var divInput= document.getElementById("cetusinput");
		divInput.classList.remove('prettyprint', 'lang-c'); 
	    divInput.innerHTML = ""<%=inputFinalString%>; 
		document.getElementById("ReCompile").disabled = false;
		document.getElementById("NewCompile").disabled = false;
		document.getElementById("codeModified").innerHTML= "Code can be modified!";
		document.getElementById("cetusoutput").style.background= "rgba(192, 192, 192, 0.2)";		
	}  --%>
 	
 	//make it editable. and enable other buttons.
 	function changeInput(){
 		var divInput= document.getElementById("cetusinput");
 				var att = document.createAttribute("contentEditable");       // Create a "class" attribute
		att.value = "true";                           // Set the value of the class attribute
		divInput.setAttributeNode(att);  // Add the class attribute to output to be editable
		divInput.classList.remove('prettyprint', 'lang-c'); 
	    divInput.innerHTML = ""<%=inputFinalString%>; 
		document.getElementById("ReCompile").disabled = false;
		document.getElementById("NewCompile").disabled = false;
		document.getElementById("modifyOutput").disabled = true;
		document.getElementById("codeModified").innerHTML= "Code can be modified!";
		divInput.style.background="rgba(255,255,255, 0.9)";
		document.getElementById("cetusoutput").style.background= "rgba(192, 192, 192, 0.2)";				
 	}
	
	function setParasforservlet(){
//ReCompile- Compiling the modified code with the same parallelization options.
//save modified input file as a a file- passfile
//pass the file path as the inputprg to the servlet.
		var divInput= document.getElementById("cetusinput");
		var inputParameter= document.getElementById("usercode");
		inputParameter.value= divInput.innerText;
		document.getElementById("codeRadios").value="passedFile"; /* "code"; */
		document.getElementById("gridRadios").value="semi-auto";
	}
	
	function setParasforIndex(){
//NewCompile
//save modified input file as a a file-pass file- save the input in the servlet. codeRadios:passedFile
//pass the file path as the inputprg to the servlet.

		var divInput= document.getElementById("cetusinput");
		var inputParameter= document.getElementById("usercode");
		inputParameter.value= divInput.innerText;
		document.getElementById("codeRadios").value="passedFile";
		document.getElementById("gridRadios").value="NoCompile"; 
	<%-- 	document.getElementById("allCetusOptions").value= <%=allOptions %>; --%>
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
	
	function changeOutput(){
		var divInput= document.getElementById("cetusinput");
		var divOutput= document.getElementById("cetusoutput");
		divOutput.style.background="rgba(255,255,255, 0.9)";
		document.getElementById("cetusinput").style.background= "rgba(192, 192, 192, 0.2)";
		var att = document.createAttribute("contentEditable");       // Create a "class" attribute
		att.value = "true";                           // Set the value of the class attribute
		divOutput.setAttributeNode(att);  // Add the class attribute to output to be editable
		divOutput.classList.remove('prettyprint', 'lang-c'); 
		divOutput.innerHTML = ""<%=outputcetusFinalString%>; 
		document.getElementById("ReCompile").disabled = true; //hide these elements
		document.getElementById("NewCompile").disabled = true;
		//document.getElementById("helpReCompile").removeAttribute("class");
		//document.getElementById("helpNewCompile").removeAttribute("class");
		document.getElementById("modifiable").innerHTML="Output (Modifiable)";
		document.getElementById("modifiableInput").innerHTML="Input";
		document.getElementById("displayExecTime").disabled = false;
		document.getElementById("modifyInput").disabled = true;
	}

</script>



</body>
</html>
<%
	connection.close();
} catch (Exception e) {
e.printStackTrace();
}
%>
