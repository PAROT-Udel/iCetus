<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.regex.*"%>
<%@page import="java.nio.file.Path"%>
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
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"
	integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q"
	crossorigin="anonymous"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"
	integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
	crossorigin="anonymous"></script>
<!-- code prettifier -->
<!-- <script src="https://cdn.jsdelivr.net/gh/google/code-prettify@master/loader/run_prettify.js?lang=c&amp;skin=default"></script> -->
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

span:target {
	color: blue;
	font-weight: bold;
}
</style>
<style>
#serialexe, #paraexe, #input, #executionReport {
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

	/* white-space: pre-line; /* for /n */ */
}


#serialexe, #paraexe{
	white-space: pre;
	font: 300 14px Arial;
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
</style>


<%
	String pathWebcontent = request.getContextPath();
%>

<link rel="stylesheet" type="text/css"
	href="<%=pathWebcontent%>/resources/css/prettify.css" />
<script type="text/javascript"
	src="<%=pathWebcontent%>/resources/js/prettify.js"></script>

<!--put line numbers on every line instead of just every fifth line  -->
<style>
.pln {
	color: #000
} /* plain text */
@media screen {
	.str {
		color: #080
	} /* string content */
	.kwd {
		color: #008
	} /* a keyword */
	.com {
		color: #800
	} /* a comment */
	.typ {
		color: #606
	} /* a type name */
	.lit {
		color: #066
	} /* a literal value */
	/* punctuation, lisp open bracket, lisp close bracket */
	.pun, .opn, .clo {
		color: #660
	}
	.tag {
		color: #008
	} /* a markup tag name */
	.atn {
		color: #606
	} /* a markup attribute name */
	.atv {
		color: #080
	} /* a markup attribute value */
	.dec, .var {
		color: #606
	} /* a declaration; a variable name */
	.fun {
		color: red
	} /* a function name */
}

/* Use higher contrast and text-weight for printable form. */
@media print , projection {
	.str {
		color: #060
	}
	.kwd {
		color: #006;
		font-weight: bold
	}
	.com {
		color: #600;
		font-style: italic
	}
	.typ {
		color: #404;
		font-weight: bold
	}
	.lit {
		color: #044
	}
	.pun, .opn, .clo {
		color: #440
	}
	.tag {
		color: #006;
		font-weight: bold
	}
	.atn {
		color: #404
	}
	.atv {
		color: #060
	}
}

/* Put a border around prettyprinted code snippets. */
/* code.prettyprint { padding: 2px; border: 1px solid #888 } */
code {
	color: black
}

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
 -->
<!-- <style>
li.L0, li.L1, li.L2, li.L3,
li.L5, li.L6, li.L7, li.L8 {
  list-style-type: decimal !important;
}
</style> -->


<html>
<head>
<meta charset="ISO-8859-1">

<title>Execution Output</title>

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
String inputContent = resultSet.getString("input_content");
System.out.println("\n inputContent"+inputContent);
String executionContent = resultSet.getString("cetus_Analysis_content");
System.out.println("\n executionContent"+executionContent);
%>

<%
String inputFinalString = null;
/* Create Input for div section =========================================================================================*/
System.out.println("Creating input");
StringBuilder finalinputStringBuilder = new StringBuilder("");

String[] inlines = inputContent.split("\n");

inlines= servlet.makeHTMLqualified(inlines,finalinputStringBuilder);
/* for (int i = 0; i < inlines.length; i++) {
	//System.out.println("inlines["+i+"]:  "  +inlines[i] );
	//finalinputStringBuilder.append(inlines[i]);
	 if (!inlines[i].equals("")) {
	finalinputStringBuilder.append(inlines[i].trim()).append("\n").append(System.getProperty("line.separator"));
	}
} */
inputFinalString = finalinputStringBuilder.toString();
//inputFinalString= inputFinalString.replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
//System.out.println("\n\n \n input:" + inputFinalString);
//System.out.println("\n\n \n output:" + outputContent);
System.out.println("\n Input created\n" + inputFinalString);
%>
<%
/* Getting Execution time from Analysis================================================== */
System.out.println("\n Creating Execution report ");	
String exefinalString=null;
String 	executionTime = null;
StringBuilder exefinalStringBuilder = new StringBuilder("");
int exefromidx = executionContent.indexOf("[Execution]", 0);
int exetoidx= executionContent.indexOf("[ExecutionResultSerial]", exefromidx);
//System.out.println("from index \t"+exefromidx);
//print from "Exception Type:" to * for the user to show the error to the user
//int exetoidx = analysisContent.indexOf("[RangeDomain]", fromidx);
if (exefromidx < 0 ) {// FormatedAnalysis == null || FormatedAnalysis.isEmpty() || FormatedAnalysis.trim().isEmpty()){ 
	// System.out.println("No Result");
	exefinalString = "+\"Calculating speedup and efficiency failed!\"";
} else {
executionTime = executionContent.substring(exefromidx, exetoidx);
	
String[] exelines = executionTime.split("\n");

exelines= servlet.makeHTMLqualified(exelines,exefinalStringBuilder);

}
	exefinalString = exefinalStringBuilder.toString();
	System.out.println("\n \n\n Execution: " + exefinalString);

System.out.println("\n Execution report created");

%>

<%
/* Getting Execution result of serial code from Analysis================================================== */
System.out.println("\n Creating Execution report ");	
String serialexefinalString=null;
String 	executionResult = null;
StringBuilder serialexefinalStringBuilder = new StringBuilder("");

int serialexefromidx = executionContent.indexOf("[ExecutionResultSerial]", 0);
System.out.println("\n \n\n FRom: " + serialexefromidx);
int serialexetoidx= executionContent.indexOf("[ExecutionResultParallel]", serialexefromidx);
System.out.println("\n \n\n To: " + serialexetoidx);
//System.out.println("from index \t"+exefromidx);
//print from "Exception Type:" to * for the user to show the error to the user
//int exetoidx = analysisContent.indexOf("[RangeDomain]", fromidx);
if (serialexefromidx < 0 ) {// FormatedAnalysis == null || FormatedAnalysis.isEmpty() || FormatedAnalysis.trim().isEmpty()){ 
	// System.out.println("No Result");
	serialexefinalString = "+\"Printing the execution output failed!\"";
} else {
	executionResult = executionContent.substring(serialexefromidx, serialexetoidx);
	//System.out.println("\n \n\n Execution Result: " + executionResult);
	
String[] serialexelines = executionResult.split("\n");

serialexelines= servlet.makeHTMLqualifiedNoTriming(serialexelines,serialexefinalStringBuilder);

}
serialexefinalString = serialexefinalStringBuilder.toString();
	System.out.println("\n \n\n Execution Result: " + serialexefinalString);

System.out.println("\n Execution Result of the serial code created");

%>

<%
/* Getting Execution result of Parallel4Cores code from Analysis================================================== */
System.out.println("\n Creating Execution report ");	
String paraexefinalString=null;
String 	paraexecutionResult = null;
StringBuilder paraexefinalStringBuilder = new StringBuilder("");

int paraexefromidx = executionContent.indexOf("[ExecutionResultParallel]", 0);
//System.out.println("\n \n\n FRom: " + paraexefromidx);
//int serialexetoidx= executionContent.indexOf("[ExecutionResultParallel]", serialexefromidx);
//System.out.println("\n \n\n To: " + serialexetoidx);
//System.out.println("from index \t"+exefromidx);
//print from "Exception Type:" to * for the user to show the error to the user
//int exetoidx = analysisContent.indexOf("[RangeDomain]", fromidx);
if (paraexefromidx < 0 ) {// FormatedAnalysis == null || FormatedAnalysis.isEmpty() || FormatedAnalysis.trim().isEmpty()){ 
	// System.out.println("No Result");
	paraexefinalString = "+\"Printing the execution output failed!\"";
} else {
	paraexecutionResult = executionContent.substring(paraexefromidx);
	//System.out.println("\n \n\n Execution Result: " + paraexecutionResult);
	
String[] paraexelines = paraexecutionResult.split("\n");

paraexelines= servlet.makeHTMLqualifiedNoTriming(paraexelines,paraexefinalStringBuilder);
}
paraexefinalString = paraexefinalStringBuilder.toString();
	System.out.println("\n \n\n Execution Result: " + paraexefinalString);

System.out.println("\n Execution Result of the parallel code created");

%>
<script>

	function load(){
		var divInput= document.getElementById("input");
		divInput.innerHTML = ""<%=inputFinalString%>; 
		var divoutput= document.getElementById("executionReport");
		divoutput.innerHTML = ""<%=exefinalString%>; 
		var serialexe=document.getElementById("serialexe");
		serialexe.innerHTML =""<%=serialexefinalString %>;
		var paraexe=document.getElementById("paraexe");
		paraexe.innerHTML =""<%=paraexefinalString %>;
		/* prettify the code */
		PR.prettyPrint();
	</script>

</head>
<body onload="load()"
	Style="background-color: rgba(192, 192, 192, 0.2);">


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

	<button type="button" class="btn btn-primary"
		style="align: left; margin-left: 20px;"
		onclick="location.href = '<%=request.getContextPath()%>/';">Back</button>
<br>

		    <!-- --------------------  -->
			<!--  Input & Execution Report  -->
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
							<h4 id="modifiableInput">Parallel code</h4>
						</div>
					</div>

					 <div class="form-row">
						
					</div>

					
 					<div class="form-row">
						<div align="left">
							
						</div>
					</div> 
						<div class="form-row">
							<div align="left">
								<div class="container" >
					<!-- 			contentEditable -->
										<code class="prettyprint lang-c" id="input"  rows="12"
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
						<!-- 	<div align="left" class="form-row" id="recompiled" style="margin-left: 0px;"></div> -->
						<br>
						</div>
					</div>

					<div class="form-row">
						<div align="left">
							<h4 id="modifiable">Speedup and Efficiency Analysis</h4>
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
							<!-- 	<code  id="cetusoutput" style="border-radius: 10px; width: 100%; padding: 25px; box-sizing: border-box;">  -->
								<div id="executionReport" contentEditable rows="12"
										style="border-radius: 10px; width: 100%; padding: 25px; box-sizing: border-box;">
									</div> 
									<!-- </code> -->
									
									
								</div>
							</div>
						</div>


				</div>
			</div>
			
			<!-- --------------------  -->
			<!--  Serial & Parallel Execution Results -->
			<!-- --------------------  -->
			<div class="row">

				<div class="column">

					
					<div class="form-row">
						<div align="left">
						</div>
					</div>
					<div class="form-row">
						<div align="left">
							<h4 id="modifiableInput">Execution result of the serial code</h4>
						</div>
					</div>

						<div class="form-row">
							<div align="left">
								<div class="container" >
										<div id="serialexe"  rows="12"
										style="border-radius: 10px; width: 100%; padding: 25px; box-sizing: border-box;" >
									</div> 
								
								</div>
							</div>
						</div>


				</div>

				<div class="column">

	
					<div class="form-row">
						<div align="left">
							<h4 id="modifiable">Execution result of the Parallel code(4 cores)</h4>
						</div>
					</div>

			
						<div class="form-row">
							<div align="left">
								<div class="container">

								<div id="paraexe"  rows="12"
										style="border-radius: 10px; width: 100%; padding: 25px; box-sizing: border-box;">
									</div> 								
								</div>
							</div>
						</div>
				</div>
			</div>
			
<script>

	function load(){
		var divInput= document.getElementById("input");
		divInput.innerHTML = ""<%=inputFinalString%>; 
		var divoutput= document.getElementById("executionReport");
		divoutput.innerHTML = ""<%=exefinalString%>; 
		var serialexe=document.getElementById("serialexe");
		serialexe.innerHTML =""<%=serialexefinalString %>;
		var paraexe=document.getElementById("paraexe");
		paraexe.innerHTML =""<%=paraexefinalString %>;
		/* prettify the code */
		PR.prettyPrint();
				
<%-- 		document.getElementById("ReCompile").disabled = true;
		document.getElementById("NewCompile").disabled = true;
		if("<%=setMessage%>" === "ReCompile" ){
			document.getElementById("recompiled").innerHTML="Code successfully recompiled!";
		}else{
			document.getElementById("recompiled").innerHTML="<br>";
			} --%>
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
