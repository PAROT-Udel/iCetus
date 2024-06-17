<%@page import="cetus.registration.controller.UserServlet"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>

<head>
<!-- Global site tag (gtag.js) - Google Analytics -->
<script async
	src="https://www.googletagmanager.com/gtag/js?id=G-J9XNTHF4C4"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());

  gtag('config', 'G-J9XNTHF4C4');
</script>

<meta charset="ISO-8859-1">
<!-- Forcing a Page to Load from the Server -->
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Expires" content="-1">
<meta http-equiv="CACHE-CONTROL" content="NO-CACHE">

<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
	integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm"
	crossorigin="anonymous">

<link
	href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css"
	rel="stylesheet" type="text/css" />

<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
	integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
	crossorigin="anonymous"></script>
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"
	integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q"
	crossorigin="anonymous"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"
	integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
	crossorigin="anonymous"></script>



<style type="text/css">
.div1 {
	text-align: justify;
	text-justify: inter-word;
	/* 	height: 100px; */
}

.jumbotron {
	padding: 0rem 0rem;
}

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
	/* font-family: "Times New Roman", Times, serif; */
	padding: 0rem 0rem;
}

.col-form-label1 {
	vertical-align: middle;
	font-size: 16px;
	width: 90%;
	/* font-family: "Times New Roman", Times, serif; */
}

textArea{font-size: 15px;}

/* #user_code{
	word-wrap: break-word;
	cursor: text;
	overflow: auto;
	height: 215px;
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
} */

b {
	font-size: 18px;
}

#file_input {
	width: 400px;
}

select {
	width: 500px;
	margin-top: 5px;
	margin-bottom: 5px;
	border-radius: 5px;
	border-color: black;
	font-weight: normal;
}

input {
	margin-top: 5px;
	margin-bottom: 5px;
	margin-left: 20px;
	/* margin-right: 5px; */
}

button {
	margin-left: 40px;
	margin-right: 5px;
	padding: 2px;
	width: 150px;
}

.row {
	display: flex;
	margin-right: 0px;
	padding: 2px;
}

/* Create two equal columns that sits next to each other */
.column1 {
	flex: 49%;
	padding: 5px;
	height: 390px;
}

.column2 {
	flex: 49%;
	padding: 5px;
	height: 430px;
}

.form-example-input {
	/*          margin-left:5px;  */
	
}

ul {
	/*          float:left;  */
	/*          margin:5px;  */
	
}
/*         select.selectlist option.defaultoption */
/* 		{ */
/*     		background-color: #0000FF; */
/* 		} */
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


<title>iCetus project</title>

</head>

<body onload="load()">

	<%
		UserServlet servlet = new UserServlet();
		String pathWebcontent=request.getContextPath();
		// Get the address of the project (for example: http://localhost:8080/MyApp/) and assign it to the basePath variable.  /cetusWeb
		//System.out.println("index.jsp, "+ pathWebcontent);

		String basePath = request.getScheme()+"://" +request.getServerName()+":"+request.getServerPort()+pathWebcontent+"/" ;
		// http://icetus.ece.udel.edu:80/cetusWeb/
		//System.out.println("index.jsp, " + basePath);
		//System.out.println(basePath +"iCetus.png");

		/* String absolutePathToIndexJSP = servletContext.getRealPath("/index.jsp");
		 */
		//Put the "project path basePath" into the pageContext and read it later with the EL expression.
		pageContext.setAttribute("basePath", basePath);
		 
		//Setting attributes for new compilation of the code
		String codeType= "";
		codeType = request.getParameter("codeRadios"); //=code
		System.out.println("\n codeType "+codeType );

		String codeContent= "";
		codeContent=request.getParameter("usercode"); //source code

		System.out.println("\ncodeContent: "+codeContent ); //was coming with line.separators. trim used to delete them
		//changing the code to html format
		/* ================================================================  */
		if(codeContent!= null && !codeContent.trim().isEmpty()){
		StringBuilder codeStringBuilder = new StringBuilder("");

		codeContent = codeContent.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");
		//replaces all empty lines
		codeContent = codeContent.replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
		String[] inlines = codeContent.split("\n");
		/* inlines= servlet.makeHTMLqualified(inlines,codeStringBuilder);
		codeContent = codeStringBuilder.toString();
		System.out.println("codeString"+codeContent); */
		//StringBuilder finalreductionStringBuilder = new StringBuilder("");

/* 		for (int i = 0; i < inlines.length; i++) {

			if (inlines[i].contains("\"") ) {
				inlines[i] = inlines[i].replaceAll(" \"", " \'");
				inlines[i] = inlines[i].replaceAll("\"", "\' ");
			}

		} */
		for (String s : inlines) {
			System.out.print("\ncodelines: "+ s.trim()); 

			if (!s.equals("")) {
		codeStringBuilder.append(" " + "+\"\\n ").append(s.trim() + "\"");
			}
		}
		codeContent=codeStringBuilder.toString();
		//codeContent=codeContent.replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
		System.out.println("\n \ncodeContent: "+codeContent); 
		}else{
		codeContent="";
		System.out.println("codeContent:"+codeContent); }
		/* ================================================================  */
		String actionType= "";
		actionType=request.getParameter("action"); //NewCompile
		System.out.println("\n actionType "+actionType);
		/* if it is NewCompile only user source should be set */
		//gridRadios NoCompile
	%>
	
	<div id="pageloader">
   <img id="pageloaderimg" src="<%=pathWebcontent%>/resources/img/bubble.gif" width=auto height=auto frameBorder="0" alt="Please be patient while your file is being processed..." />
   <label id="pageloaderimg" for="pageloaderimg">Please be patient while your file is being processed...</label>
</div>
	
	<base href="<%=basePath%>">
	<nav class="navbar navbar-dark bg-primary navbar-expand-lg">
		<ul class="navbar-nav mr-auto">
			<li class="nav-item "><a class="nav-link active"
				href="<%=pathWebcontent%>/" style="color: white; font-weight: bold;">Home
			</a></li>
			<li class="nav-item"><a class="nav-link  " style='color: white;'
				href="<%=pathWebcontent%>/about.jsp">About us</a></li>
			<li class="nav-item"><a class="nav-link " style='color: white'
				href="<%=pathWebcontent%>/project.jsp">About the project</a></li>

		</ul>

	</nav>


	<div class="jumbotron div1">
		<p style="font-size: 24px;">
			<img src="<%=pathWebcontent%>/resources/img/iCetus.png" width="70"
				height="70" Style="vertical-align: middle; margin: 10px 10px;"
				class="d-inline-block " alt="iCetus"> iCetus, A
			Source-to-Source Compiler Infrastructure for C Programs
		</p>
		<%-- 				<p style="font-size: 24px;">
			<img src="${pageContext.request.contextPath}/iCetus.png"
				width="70" height="70"
				Style="vertical-align: middle; margin: 10px 10px;"
				class="d-inline-block " alt="iCetus"> iCetus, A
			Source-to-Source Compiler Infrastructure for C Programs
		</p> --%>
	</div>
	


	<!--<form id="myForm" autocomplete="off" action="<%= request.getContextPath() %>/register" method="post" >  -->
	<form id="myForm" autocomplete="off"
		action="<%=request.getContextPath()%>/" method="post"
		enctype="multipart/form-data">

<!--Set Recompile parameters  -->
<input type="hidden" id="action" name="action" value="">
<!-- <input type="hidden" id="codeRadios" name="codeRadios" value=""> type of file
 -->



		<%-- <form id="myForm" autocomplete="off" action="<%= request.getContextPath() %>/" method="post" enctype="multipart/mixed"> --%>

		<div class="row">

			<div class="column1">

				<input type="hidden" id="refreshed" value="no">


				<!--   Radio Buttons for code input -->
				<div class="form-row">
					<div class="form-group col-md-6">
						<legend class="col-form-label  ">
							<b>Enter Your Input Program </b>&nbsp;<span><i
								data-content="The file/code passed to the compiler should be self-contained. Make your file self-contained by including within it all header files that contain needed definitions. Plesae notice only the header files written by the developer should be written into the file/code. The header files that come with the compiler are recognized by Cetus."
								data-placement="right" class="fa fa-question-circle"></i></span>
						</legend>
						<div class="form-check">
						
							<input class="form-check-input" type="radio" name="codeRadios"
								id="file" style="align: left;" onclick="handleCodeRadioClick();"
								value="file" autocomplete="off" required> <label
								class="form-check-label" for="file" id="fileLabel"> Upload a file </label> <input
								id="file_input" autocomplete="off" name="inputCode" type="file" accept=".c"  multiple/>
								<!-- accept=".c,.png,.pdf" to restrict file types -->
							<!--The size of the file in bytes.  -->
						</div>
						<div class="form-check">
							<input class="form-check-input" type="radio" name="codeRadios"
								id="code" style="align: left;" onclick="handleCodeRadioClick();"
								autocomplete="off" value="code"> <label id="codeLabel"
								class="form-check-label" for="code"> Write your code </label>
							<!--                             <textarea rows="10" cols="62" WRAP="hard" id="user_code" name="usercode"></textarea> -->
						</div>
						<div class="form-check">
							<input class="form-check-input" type="radio" name="codeRadios"
								id="example" style="align: left;"
								onclick="handleCodeRadioClick();" autocomplete="off"
								value="example"> <label class="form-check-label" id="exampleLabel"
								for="example"> Run our examples( Modifiable ) </label>

							<!--  Examples added to Cetus                             -->
							<ul>
								<li style="list-style-type: none;"><input
									class="form-example-input" type="radio" name="exampleRadios"
									id="examplecode4" style="align: left;"
									onclick="handleExampleRadioClick(this.value);"
									autocomplete="off" value="ScalarPriv.c"> <label
									class="form-check-label" for="examplecode4"
									id="examplecode4Label"> Scalar Privatization </label></li>

								<li style="list-style-type: none"><input
									class="form-example-input" type="radio" name="exampleRadios"
									id="examplecode1" style="align: left;"
									onclick="handleExampleRadioClick(this.value);"
									autocomplete="off" value="ArrayPriv.c"> <label
									class="form-check-label" for="examplecode1"
									id="examplecode1Label"> Array Privatization </label></li>

								<li style="list-style-type: none"><input
									class="form-example-input" type="radio" name="exampleRadios"
									id="examplecode5" style="align: left;"
									onclick="handleExampleRadioClick(this.value);"
									autocomplete="off" value="ScalarRed.c"> <label
									class="form-check-label" for="examplecode5"
									id="examplecode5Label"> Scalar Reduction </label></li>

								<li style="list-style-type: none"><input
									class="form-example-input" type="radio" name="exampleRadios"
									id="examplecode2" style="align: left;"
									onclick="handleExampleRadioClick(this.value);"
									autocomplete="off" value="ArrayRed.c"> <label
									class="form-check-label" for="examplecode2"
									id="examplecode2Label"> Array Reduction </label></li>

								<li style="list-style-type: none"><input
									class="form-example-input" type="radio" name="exampleRadios"
									id="examplecode3" style="align: left;"
									onclick="handleExampleRadioClick(this.value);"
									autocomplete="off" value="Induction.c"> <label
									class="form-check-label" for="examplecode3"
									id="examplecode3Label"> Induction Variable </label></li>

								<li style="list-style-type: none"><input
									class="form-example-input" type="radio" name="exampleRadios"
									id="examplecode6" style="align: left;"
									onclick="handleExampleRadioClick(this.value);"
									autocomplete="off" value="SimpleLoop.c"> <label
									class="form-check-label" for="examplecode6"
									id="examplecode6Label"> Parallelizable Loop </label></li>
									
								<li style="list-style-type: none"><input
									class="form-example-input" type="radio" name="exampleRadios"
									id="examplecode7" style="align: left;"
									onclick="handleExampleRadioClick(this.value);"
									autocomplete="off" value="ArrayAntiDep.c"> <label
									class="form-check-label" for="examplecode7"
									id="examplecode7Label"> Array Anti-Dependency </label></li>	
									
								<li style="list-style-type: none"><input
									class="form-example-input" type="radio" name="exampleRadios"
									id="examplecode8" style="align: left;"
									onclick="handleExampleRadioClick(this.value);"
									autocomplete="off" value="ArrayFlowDep.c"> <label
									class="form-check-label" for="examplecode8"
									id="examplecode8Label"> Array Flow Dependency </label></li>	
									
								<li style="list-style-type: none"><input
									class="form-example-input" type="radio" name="exampleRadios"
									id="examplecode9" style="align: left;"
									onclick="handleExampleRadioClick(this.value);"
									autocomplete="off" value="ArrayOutputDep.c"> <label
									class="form-check-label" for="examplecode9"
									id="examplecode9Label"> Array output Dependency </label></li>	
									
<!-- 								<li style="list-style-type: none"><input -->
<!-- 									class="form-example-input" type="radio" name="exampleRadios" -->
<!-- 									id="examplecode10" style="align: left;" -->
<!-- 									onclick="handleExampleRadioClick(this.value);" -->
<!-- 									autocomplete="off" value="CaRVSimpleLoop.c"> <label -->
<!-- 									class="form-check-label" for="examplecode10" -->
<!-- 									id="examplecode10Label"> CaRV - Parallelizable Loop  </label></li>	 -->
									
							</ul>
							<!-- 							<textarea rows="10" cols="62" WRAP="hard" id="example_code" name="examplecode" readOnly="false" contenteditable="true" placeholder="The content of example files will appear here..."></textarea> -->
							<!-- Path to the files: C:\Users\13022\Desktop\VM_share\cetusWeb\resources\examples -->
						</div>
					</div>
				</div>

				<div class="form-row">
					<div class="form-group col-md-6">
						<legend class="col-form-label">
							<b>Opt for an operation </b>
						</legend>
						<div class="form-check">
							<input class="form-check-input" type="radio" name="gridRadios"
								id="radioAuto" style="align: left;"
								onclick="handleRadioClick();" autocomplete="off" value="auto"
								checked> <label class="form-check-label" for="radioAuto">
								Use the default parallelization options </label>
						</div>
						<div class="form-check">
							<input class="form-check-input" type="radio" name="gridRadios"
								id="radioSemiAuto" style="align: left;"
								onclick="handleRadioClick();" autocomplete="off"
								value="semi-auto"> <label class="form-check-label"
								for="radioSemiAuto"> Customize the parallelization options </label>
						</div>
						<div class="form-check">
							<input class="form-check-input" type="radio" name="gridRadios"
								id="radioNoCompile" style="align: left;"
								onclick="handleRadioClick();" autocomplete="off"
								value="Execute"> <label class="form-check-label"
								for="radioNoCompile"> Execute parallelized code </label>
						</div>
						<div class="form-check">
							<input class="form-check-input" type="radio" name="gridRadios"
								id="radioCaRV" style="align: left;"
								onclick="handleRadioClick();" autocomplete="off" value="CaRV"> 
								<label class="form-check-label" for="radioCaRV">
								Apply the CaRV tool to the program </label> &nbsp;<span><i
data-content="Accelerate your code optimization process with the CaRV tool by specifying experimental sections. Begin by marking the desired code segment with '#pragma experimental section start' and conclude it with '#pragma experimental section stop'. This feature enables you to apply multiple optimizations to a chosen section and easily compare the results. "
data-placement="top" class="fa fa-question-circle"></i></span>
						</div>
						<!-- <div class="form-check">
							<input class="form-check-input" type="radio" name="gridRadios"
								id="radioQuery" style="align: left;"
								onclick="handleRadioClick();" autocomplete="off" value="Query">
							<label class="form-check-label" for="radioQuery"> Run a
								Query on your program </label> 
								<select class="form-select" name="queries" id="queries">
								<option selected>Select your query</option>
								<option value="DDT">show me data dependencies</option>
								<option value="Range">show me variable ranges</option>
    							    <option value="opel">Opel</option>
   								<option value="audi">Audi</option>
							</select>
						</div> -->
						
 						<div class="form-check">
						<br>
						</div> 
					</div>

				</div>
			</div>
			<div class="column1">


				<br>
				<textarea rows="12" cols="150" WRAP="hard" id="user_code"
					name="usercode" class="usercode"
					style="border-radius: 10px; padding: 15px; margin: 10px; width: 98%; box-sizing: border-box;"
					placeholder="/* Write your code here... */"></textarea>

				<textarea rows="12" cols="62" WRAP="hard" id="example_code"
					name="examplecode"
					style="border-radius: 10px; padding: 15px; margin: 10px; width: 98%; box-sizing: border-box;"
					placeholder="/* The content of example files will appear here...*/"></textarea>

			</div>
		</div>
		<br>
		<br>
		<br>
		<br>
		<!-- ---------------------------------------------------------------------------------------------------------------------------------- -->
		<div class="row" id="myDIV"
			style="background-color: rgba(192, 192, 192, 0.2);">

			<div class="column2">

				<div class="form-row">
					<p>
						<b style="margin-left: 15px;">Customize Cetus Options</b>
					</p>
				</div>
				
				<div class="form-row">
					<p>
						<div style="margin-left: 15px;width:85%;">If no option is selected in this section, Cetus would run with its default options.</div>
					</p>
				</div>

				<div class="form-row">
					<input id="check_callgraph" type="checkbox"
						name="checkbox_callgraph" value="-callgraph" autocomplete="off">
					<label class="col-form-label1" for="check_callgraph"
						id="labelCallgraph"
						style="width:85%; box-sizing: border-box;">Print
						the Static Call graph (-callgraph) </label>
					<!-- 						<div class="tooltip">Hover over me
 							 <span class="tooltiptext">Here is the Tooltip text</span>
						</div> -->
					<%-- 						<button type="button" class="btn btn-secondary" data-toggle="tooltip" data-html="true" title="<em>Tooltip</em> <u>with</u> <b>HTML</b>">
						  ?
						</button> --%>

				</div>

				<div class="form-row">
					<input id="check_normalize_loops" type="checkbox"
						name="checkbox_normalize_loops" value="-normalize-loops"
						autocomplete="off"> <label class="col-form-label1"
						for="check_normalize_loops" id="label_normalize_loops"
						style="width: 80%; box-sizing: border-box;">Normalize for
						loops so they begin at 0 and have a step of 1(-normalize-loops)</label>
				</div>

				<div class="form-row">
					<input id="check_normalize-rtn-stmt" type="checkbox"
						name="checkbox_normalize_rtn_stmt" value="-normalize-return-stmt"
						autocomplete="off"> <label class="col-form-label1"
						for="check_normalize-rtn-stmt" id="label_normalize-rtn-stmt"
						style="width: 90%; box-sizing: border-box;">Normalize
						return statements for all procedures (-normalize-return-stmt)</label>
				</div>

				<div class="form-row">

					<input id="check_tsingle-call" type="checkbox"
						name="checkbox_tsingle_call" value="-tsingle-call"
						autocomplete="off"> <label class="col-form-label1"
						for="check_tsingle-call" id="label_tsingle-call">Transform
						all statements so they contain at most one function call
						(-tsingle-call)</label>
				</div>

				<div class="form-row">

					<input id="check_tsingle-declarator" type="checkbox"
						name="checkbox_tsingle_declarator" value="-tsingle-declarator"
						autocomplete="off"> <label class="col-form-label1"
						for="check_tsingle-declarator" id="label_tsingle-declarator">Transform
						all variable declarations so they contain at most one declarator
						(-tsingle-declarator)</label>
				</div>

				<div class="form-row">
					<input id="check_tsingle-rtn" type="checkbox"
						name="checkbox_tsingle_rtn" value="-tsingle-return"
						autocomplete="off"> <label class="col-form-label1"
						for="check_tsingle-rtn" id="label_tsingle-rtn">Transform
						all procedures so they have a single return statement
						(-tsingle-return)</label>
				</div>

				<div class="form-row">
					<p></p>
				</div>

				<!-- <input type="button" value = "Refresh" onclick="history.go(0)" /> -->
			</div>
			<div class="column2">

				<!--             <div class="form-row"> -->
				<!--                     <p></p> -->
				<!--                 </div> -->

				<!--              <div class="form-row"> -->
				<!--                     <p></p> -->
				<!--                 </div> -->

				<!--  				<div class="form-row"> -->

				<!-- 					<label for="inlineFormCustomSelectVerbosity" id="labelVerbosity" class="col-form-label">Verbosity:
                    </label> -->
				<!-- 					<select id="inlineFormCustomSelectVerbosity" name="verbosity"
						class="selectlist">
						<option value="" selected>Choose the Degree of Status
							Messages</option>
						        <option value="-ddt=0">Disable Data Dependence Testing </option>
						<option value="-verbosity=0">-verbosity=0</option>
						<option value="-verbosity=1">-verbosity=1</option>
						<option value="-verbosity=2">-verbosity=2</option>
						<option value="-verbosity=3">-verbosity=3</option>
						<option value="-verbosity=4">-verbosity=4- Default</option>
					</select>&nbsp;<span><i data-content="Degree of status messages (0-4) that you wish to see" data-placement="top" class="fa fa-question-circle"></i></span>
				</div>  -->


				<!-- Options initialized implicitly before parsing command line: [-parser=cetus.base.grammars.CetusCParser, -induction=3, -outdir=cetus_output, -preprocessor=cpp -C -I., -privatize=2, -reduction=2, -verbosity=0, -ddt=2, -parallelize-loops=1, -ompGen=1, -alias=1, -range=1, -teliminate-branch=1, -profitable-omp=1]
 -->
				<div class="form-row">
					<!-- <label for="inlineFormCustomSelect" id="labelDDT" class="col-form-label">Data Dependence Test:
                    </label> -->
					<select id="inlineFormCustomSelect" name="ddt">
						<option value="" selected>Data Dependence Test (Range
							Test - Default)</option>
						<!--         <option value="-ddt=0">Disable Data Dependence Testing </option> -->
						<option value="-ddt=1">Perform Banerjee-Wolfe test</option>
						<option value="-ddt=2">Perform Range test - Default</option>
					</select>&nbsp;<span><i
						data-content="Data dependence analysis tries to establish dependence relations between scalar variables or between array accesses in a program."
						data-placement="top" class="fa fa-question-circle"></i></span>
				</div>


				<div class="form-row">
					<!-- <label for="inlineFormCustomSelectRange" id="labelRange" class="col-form-label">Range Analysis:
                    </label> -->
					<select id="inlineFormCustomSelectRange" name="range">
						<option value="" selected>Range Analysis Type (Local
							Range Computation - Default)</option>
						<option value="-range=0">Disable range computation</option>
						<option value="-range=1">Perform local range computation
							- Default</option>
						<option value="-range=2">Perform inter-procedural
							computation</option>
					</select>&nbsp;<span><i
						data-content="Range Analysis collects, at each program statement, a map from integer-typed scalar variables to their symbolic value ranges, represented by a symbolic lower bound and an upper bound. "
						data-placement="top" class="fa fa-question-circle"></i></span>
				</div>

				<div class="form-row">
					<!-- <label for="inlineFormCustomSelectAlias" id="labelAlias" class="col-form-label">Alias Analysis:
                    </label> -->
					<select id="inlineFormCustomSelectAlias" name="alias">
						<option value="" selected>Alias Analysis Type (Advanced
							Interprocedural Analysis - Default)</option>
						<option value="-alias=0">Assume all locations are aliased
						</option>
						<option value="-alias=1">Advanced interprocedural
							analysis - Default</option>
						<option value="-alias=2">Assume no alias exists when
							points-to analysis is too conservative</option>
						<option value="-alias=3">Assume no alias exists</option>
					</select>&nbsp;<span><i
						data-content="Alias analysis is used to identify sets of program variable names that may refer to the same memory location during program execution. In C programs, aliases are created through the use of pointers as well as reference parameter passing. "
						data-placement="top" class="fa fa-question-circle"></i></span>
				</div>

				<div class="form-row">
					<!--                     <label for="inlineFormCustomSelectPloops" id="labelPloops" class="col-form-label">Loop -->
					<!--                         Parallelization: </label>  -->
					<select id="inlineFormCustomSelectPloops" name="ploops">
						<option value="" selected>Loop Parallelization Format
							(Parallelize outermost loops - Default)</option>
						<option value="-parallelize-loops=0">Do not parallelize</option>
						<option value="-parallelize-loops=1">Parallelize
							outermost loops - Default</option>
						<option value="-parallelize-loops=2">Parallelize all
							loops in nests</option>
						<option value="-parallelize-loops=3">Parallelize
							outermost loops with report</option>
						<option value="-parallelize-loops=4">Parallelize all
							loops with report</option>
					</select>&nbsp;<span><i
						data-content="Annotates loops with Parallelization decisions"
						data-placement="top" class="fa fa-question-circle"></i></span>
				</div>

				<div class="form-row">
					<!-- <label for="inlineFormCustomSelectprivatize" id="labelprivatize" class="col-form-label">
                        Privatization: </label> -->
					<select id="inlineFormCustomSelectprivatize" name="privatize">
						<option value="" selected>Privatization Type (Scalar &
							Array Privatization - Default)</option>
						<option value="-privatize=0">Disable privatization</option>
						<option value="-privatize=1">Perform only scalar
							privatization</option>
						<option value="-privatize=2">Perform scalar and array
							privatization - Default</option>
					</select>&nbsp;<span><i
						data-content="Privatization analysis tries to find privatizable variables (scalars and arrays) which are written first then read in a loop body. "
						data-placement="top" class="fa fa-question-circle"></i></span>
				</div>

				<div class="form-row">
					<!-- <label for="inlineFormCustomSelectReduction" id="labelReduction" class="col-form-label">Reduction
                        variable analysis: </label> -->
					<select id="inlineFormCustomSelectReduction" name="reduction">
						<option value="" selected>Reduction Analysis Type (Array
							Reduction Analysis - Default)</option>
						<option value="-reduction=0">Disable reduction</option>
						<option value="-reduction=1">Perform only scalar
							reduction analysis</option>
						<option value="-reduction=2">Perform array reduction
							analysis and transformation - Default</option>
					</select>&nbsp;<span><i
						data-content="Reduction pass performs reduction recognition for each ForLoop. it supports scalar (sum += ...), ArrayAccess (A[i] += ...), and AccessExpression (A->x += ...) for reduction variable. "
						data-placement="top" class="fa fa-question-circle"></i></span>
				</div>

				<div class="form-row">
					<!-- <label for="inlineFormCustomSelectInduction" id="labelInduction" class="col-form-label">Induction
                        variable substitution: </label> -->
					<select id="inlineFormCustomSelectInduction" name="induction">
						<option value="" selected>Induction Variable Substitution
							Method (Runtime Test - Default)</option>
						<option value="-induction=0">Disable induction variable
							recognition & substitution</option>
						<option value="-induction=1">Perform substitution of
							linear induction variables</option>
						<option value="-induction=2">Perform substitution of
							generalized induction variables</option>
						<option value="-induction=3">Perform insertion of runtime
							test for zero-trip loops - Default</option>
					</select>&nbsp;<span><i
						data-content="Induction variable (IV) substitution pass recognizes and substitutes induction variables in loops that take the form of iv = iv + expr."
						data-placement="top" class="fa fa-question-circle"></i></span>
				</div>

				<div class="form-row">
					<!-- <label for="inlineFormCustomSelectProfiling" id="labelProfiling" class="col-form-label">Insert
                        loop-profiling calls to: </label> -->
					<select id="inlineFormCustomSelectProfiling" name="profiling">
						<option value="" selected>Apply Loop-Profiling Calls to</option>
						<option value="-profile-loops=1">Every loop</option>
						<option value="-profile-loops=2">Outermost loop</option>
						<option value="-profile-loops=3">Every omp parallel</option>
						<option value="-profile-loops=4">Outermost omp parallel</option>
					</select>&nbsp;<span><i data-content="Inserts loop-profiling calls"
						data-placement="top" class="fa fa-question-circle"></i></span>
				</div>

				<div class="form-row">
					<!-- <label for="inlineFormCustomSelectEliminateBranch" id="labelEliminateBranch"
                        class="col-form-label">Eliminate unreachable branch targets: </label> -->
					<select id="inlineFormCustomSelectEliminateBranch"
						name="eliminateBranch">
						<option value="" selected>Eliminate unreachable branch
							targets (Enable - Default)</option>
						<option value="-teliminate-branch=0">Disable</option>
						<option value="-teliminate-branch=1">Enable - Default</option>
						<option value="-teliminate-branch=2">Leave old statements
							as comments</option>
					</select>&nbsp;<span><i
						data-content="Eliminates unreachable branch targets"
						data-placement="top" class="fa fa-question-circle"></i></span>
				</div>

				<div class="form-row">
					<!-- <label for="inlineFormCustomSelectProfitableOmp" id="labelProfitableOmp"
                        class="col-form-label">Select
                        profitable omp parallel region: </label> -->
					<select id="inlineFormCustomSelectProfitableOmp"
						name="profitableOmp">
						<option value="" selected>Profitable OMP Parallel Region
							(Model-based loop selection - Default)</option>
						<option value="-profitable-omp=0">Disable</option>
						<option value="-profitable-omp=1">Model-based loop
							selection - Default</option>
						<option value="-profitable-omp=2">Profile-based loop
							selection</option>
					</select>&nbsp;<span><i
						data-content="Inserts runtime for selecting profitable omp parallel region"
						data-placement="top" class="fa fa-question-circle"></i></span>
				</div>
			</div>
		</div>
		<br>
		<br>
		


<!-- 		<br id="addspace"> -->
<!-- <div class="form-row">
<input type="checkbox" id="tuner" name="tuner" value="tuner" disabled>
<label for="tuner" > Run auto-tuner on the input code</label>&nbsp;<span><i
data-content="The best combination of compiler options for parallelizing the given code would be returned to the user."
data-placement="top" class="fa fa-question-circle"></i></span>
</div> -->
 <div class="form-row">
<input type="checkbox" id="profiler" name="profiler" value="serialprofiler" >
<label for="profiler" >Profile the code </label>&nbsp;<span><i
data-content="Choose this option if serial code is being input into the program. profiling information serves to aid program optimization. The profiler identifies the loops that consume most of the program's execution time. It does so by instrumenting the loops. Note that instrumenting the loops causes overhead because of the libraries and functions that are added to the code to time the loops.
Profiling gives you a good idea of how long each loop takes to execute. While profiling is active, the total execution time of the program includes some overhead. To get an accurate execution time, disable profiling."
data-placement="top" class="fa fa-question-circle"></i></span>
</div> 
 <div class="form-row">
<input type="checkbox" id="codeExecuter" name="codeExecuter" value="serialExec" >
<label for="codeExecuter" >Display execution result of the serial code </label>&nbsp;<span><i
data-content="Choose this option if correctness of the code should be verified.The output of the program would be displayed. Verify that it matches the output obtained by running the parallel code."
data-placement="top" class="fa fa-question-circle"></i></span>
</div> 
 <div class="form-row">
<input type="checkbox" id="paracodeExecuter" name="paracodeExecuter" value="parallelExec" >
<label for="paracodeExecuter" >Display execution result of the parallel output on 4 Cores </label>&nbsp;<span><i
data-content="Choose this option if correctness of the code should be verified."
data-placement="top" class="fa fa-question-circle"></i></span>
</div>  
<!-- <div class="form-row">
<input type="radio" id="profiler" name="profiler" value="parallelprofiler" >
<label for="profiler" >Profile the parallel code </label>&nbsp;<span><i
data-content="Choose this option if parallel code is being input into the program. profiling information serves to aid program optimization. The profiler identifies the loops that consume most of the program's execution time. It does so by instrumenting the loops."
data-placement="top" class="fa fa-question-circle"></i></span>
</div> -->
		<br id="addspace">
		<button type="reset" class="btn btn-primary" onclick="history.go(0)">Reset</button>
		<button type="submit" class="btn btn-primary">Submit</button>
	</form>
</body>

</html>


<script>
/* 	 $(function () {
	 $('[data-toggle="tooltip"]').tooltip()
	 })  */


	
/* 	hovering over question mark shows the popover contents	 */
$(function() {
		$('.fa').popover({
			trigger : "hover"
		});
	})
	
	$(document).ready(function(){
		  $("#myForm").on("submit", function(){
		    $("#pageloader").fadeIn();
		  });//submit
		});//document ready	

	function load() {
		var e = document.getElementById("refreshed");
		if (e.value == "no"){
			e.value = "yes";
	
		}else {
			e.value = "no";
			
			location.reload();
		}

		
		if ("<%=actionType%>" === "NewCompile"  ){ 

<%-- console.log(<%=actionType%> == compileType); --%> //NewCompile is not defined
<%-- console.log("<%=actionType%>" == "NewCompile"); //true
 			
 			console.log(<%=actionType%> === null); //true
 			console.log(<%=actionType%> == null); //true --%>
			 document.getElementById("inlineFormCustomSelect").style.display = "none";
			//             document.getElementById("labelDDT").style.display = "none";
			//             document.getElementById("labelRange").style.display = "none";
			document.getElementById("inlineFormCustomSelectRange").style.display = "none";
			//             document.getElementById("labelAlias").style.display = "none";
			document.getElementById("inlineFormCustomSelectAlias").style.display = "none";
			//             document.getElementById("labelPloops").style.display = "none";
			document.getElementById("inlineFormCustomSelectPloops").style.display = "none";
			//             document.getElementById("labelprivatize").style.display = "none";
			document.getElementById("inlineFormCustomSelectprivatize").style.display = "none";
			//             document.getElementById("labelReduction").style.display = "none";
			document.getElementById("inlineFormCustomSelectReduction").style.display = "none";
			//             document.getElementById("labelInduction").style.display = "none";
			document.getElementById("inlineFormCustomSelectInduction").style.display = "none";
			//             document.getElementById("labelProfiling").style.display = "none";
			document.getElementById("inlineFormCustomSelectProfiling").style.display = "none";
			//             document.getElementById("labelEliminateBranch").style.display = "none";
			document.getElementById("inlineFormCustomSelectEliminateBranch").style.display = "none";
			//             document.getElementById("labelProfitableOmp").style.display = "none";
			document.getElementById("inlineFormCustomSelectProfitableOmp").style.display = "none";
			document.getElementById("labelCallgraph").style.display = "none";
			document.getElementById("check_callgraph").style.display = "none";
			document.getElementById("label_normalize-rtn-stmt").style.display = "none";
			document.getElementById("check_normalize-rtn-stmt").style.display = "none";
			document.getElementById("label_tsingle-call").style.display = "none";
			document.getElementById("check_tsingle-call").style.display = "none";
			document.getElementById("label_tsingle-declarator").style.display = "none";
			document.getElementById("check_tsingle-declarator").style.display = "none";
			document.getElementById("label_tsingle-rtn").style.display = "none";
			document.getElementById("check_tsingle-rtn").style.display = "none";
			document.getElementById("label_normalize_loops").style.display = "none";
			document.getElementById("check_normalize_loops").style.display = "none";
			document.getElementById("file_input").style.display = "none";
			document.getElementById("examplecode1").style.display = "none";
			document.getElementById("examplecode2").style.display = "none";
			document.getElementById("examplecode3").style.display = "none";
			document.getElementById("examplecode4").style.display = "none";
			document.getElementById("examplecode5").style.display = "none";
			document.getElementById("examplecode6").style.display = "none";
			document.getElementById("examplecode7").style.display = "none";
			document.getElementById("examplecode8").style.display = "none";
			document.getElementById("examplecode9").style.display = "none";
// 			document.getElementById("examplecode10").style.display = "none";
			document.getElementById("examplecode1Label").style.display = "none";
			document.getElementById("examplecode2Label").style.display = "none";
			document.getElementById("examplecode3Label").style.display = "none";
			document.getElementById("examplecode4Label").style.display = "none";
			document.getElementById("examplecode5Label").style.display = "none";
			document.getElementById("examplecode6Label").style.display = "none";
			document.getElementById("examplecode7Label").style.display = "none";
			document.getElementById("examplecode8Label").style.display = "none";
			document.getElementById("examplecode9Label").style.display = "none";
// 			document.getElementById("examplecode10Label").style.display = "none";
			document.getElementById("example_code").style.display = "none";
			document.getElementById("myDIV").style.display = "none";
			document.getElementById("user_code").style.display = "none";
			/* document.getElementById("queries").style.display = "none"; */   //on load style is null for queries
			document.getElementById("addspace").style.display = "none"; 
/* 			document.getElementById("user_code").style.display = "none";
 */<%--  		    document.getElementById("code").checked = true;  
 		 	document.getElementById("user_code").style.display = "";
			document.getElementById("user_code").value= ""<%=codeContent%>; 
			document.getElementById("user_code").contentEditable = true;  
			choose file and fill it out--%>
			document.getElementById("file").checked = true;  
			//document.getElementById("file_input").style.display = "";
			<%-- //document.getElementById("file_input").value = "<%=getServletContext().getRealPath("/")%>modifiedcode.c";	 --%>
			//set a parameter for servlet to read the file path and put it into inputprg
			document.getElementById("action").value ="NewCompile";
			//document.getElementById("file").disabled = true;
			document.getElementById("codeLabel").style.display = "none";
			document.getElementById("exampleLabel").style.display = "none";
			document.getElementById("code").disabled = true;
			document.getElementById("example").disabled = true;
			document.getElementById("code").style.display = "none";
			document.getElementById("example").style.display = "none";
			document.getElementById("fileLabel").innerHTML="The input program, with your modifications, is already on the server. Select the operation."
 			
 		}else  { 
			
			document.getElementById("inlineFormCustomSelect").style.display = "none";
			//             document.getElementById("labelDDT").style.display = "none";
			//             document.getElementById("labelRange").style.display = "none";
			document.getElementById("inlineFormCustomSelectRange").style.display = "none";
			//             document.getElementById("labelAlias").style.display = "none";
			document.getElementById("inlineFormCustomSelectAlias").style.display = "none";
			//             document.getElementById("labelPloops").style.display = "none";
			document.getElementById("inlineFormCustomSelectPloops").style.display = "none";
			//             document.getElementById("labelprivatize").style.display = "none";
			document.getElementById("inlineFormCustomSelectprivatize").style.display = "none";
			//             document.getElementById("labelReduction").style.display = "none";
			document.getElementById("inlineFormCustomSelectReduction").style.display = "none";
			//             document.getElementById("labelInduction").style.display = "none";
			document.getElementById("inlineFormCustomSelectInduction").style.display = "none";
			//             document.getElementById("labelProfiling").style.display = "none";
			document.getElementById("inlineFormCustomSelectProfiling").style.display = "none";
			//             document.getElementById("labelEliminateBranch").style.display = "none";
			document.getElementById("inlineFormCustomSelectEliminateBranch").style.display = "none";
			//             document.getElementById("labelProfitableOmp").style.display = "none";
			document.getElementById("inlineFormCustomSelectProfitableOmp").style.display = "none";
			document.getElementById("labelCallgraph").style.display = "none";
			document.getElementById("check_callgraph").style.display = "none";
			document.getElementById("label_normalize-rtn-stmt").style.display = "none";
			document.getElementById("check_normalize-rtn-stmt").style.display = "none";
			document.getElementById("label_tsingle-call").style.display = "none";
			document.getElementById("check_tsingle-call").style.display = "none";
			document.getElementById("label_tsingle-declarator").style.display = "none";
			document.getElementById("check_tsingle-declarator").style.display = "none";
			document.getElementById("label_tsingle-rtn").style.display = "none";
			document.getElementById("check_tsingle-rtn").style.display = "none";
			document.getElementById("label_normalize_loops").style.display = "none";
			document.getElementById("check_normalize_loops").style.display = "none";
			document.getElementById("user_code").style.display = "none";
			//             document.getElementById("userCodeLabel").style.display = "none";
			document.getElementById("file_input").style.display = "none";
			document.getElementById("examplecode1").style.display = "none";
			document.getElementById("examplecode2").style.display = "none";
			document.getElementById("examplecode3").style.display = "none";
			document.getElementById("examplecode4").style.display = "none";
			document.getElementById("examplecode5").style.display = "none";
			document.getElementById("examplecode6").style.display = "none";
			document.getElementById("examplecode7").style.display = "none";
			document.getElementById("examplecode8").style.display = "none";
			document.getElementById("examplecode9").style.display = "none";
// 			document.getElementById("examplecode10").style.display = "none";
			document.getElementById("examplecode1Label").style.display = "none";
			document.getElementById("examplecode2Label").style.display = "none";
			document.getElementById("examplecode3Label").style.display = "none";
			document.getElementById("examplecode4Label").style.display = "none";
			document.getElementById("examplecode5Label").style.display = "none";
			document.getElementById("examplecode6Label").style.display = "none";
			document.getElementById("examplecode7Label").style.display = "none";
			document.getElementById("examplecode8Label").style.display = "none";
			document.getElementById("examplecode9Label").style.display = "none";
// 			document.getElementById("examplecode10Label").style.display = "none";
			document.getElementById("example_code").style.display = "none";
			//              document.getElementByClassName("form-check-input").checked = false;
			//              document.getElementByClassName("form-example-input").checked = false;
			document.getElementById("myDIV").style.display = "none";
			//document.getElementById("queries").style.display = "none";
			document.getElementById("addspace").style.display = "none";
			

 		}
	}
		

	
	function handleCodeRadioClick() {
		var inputFile = document.getElementById("file");
		var inputCode = document.getElementById("code");
		var exampleFile = document.getElementById("example");
		if (inputFile.checked) {
			document.getElementById("file_input").style.display = "";
			document.getElementById("user_code").style.display = "none";
			document.getElementById("examplecode1").style.display = "none";
			document.getElementById("examplecode2").style.display = "none";
			document.getElementById("examplecode3").style.display = "none";
			document.getElementById("examplecode4").style.display = "none";
			document.getElementById("examplecode5").style.display = "none";
			document.getElementById("examplecode6").style.display = "none";
			document.getElementById("examplecode7").style.display = "none";
			document.getElementById("examplecode8").style.display = "none";
			document.getElementById("examplecode9").style.display = "none";
// 			document.getElementById("examplecode10").style.display = "none";
			document.getElementById("examplecode1Label").style.display = "none";
			document.getElementById("examplecode2Label").style.display = "none";
			document.getElementById("examplecode3Label").style.display = "none";
			document.getElementById("examplecode4Label").style.display = "none";
			document.getElementById("examplecode5Label").style.display = "none";
			document.getElementById("examplecode6Label").style.display = "none";
			document.getElementById("examplecode7Label").style.display = "none";
			document.getElementById("examplecode8Label").style.display = "none";
			document.getElementById("examplecode9Label").style.display = "none";
// 			document.getElementById("examplecode10Label").style.display = "none";
			document.getElementById("example_code").style.display = "none";
		} else if (inputCode.checked) {
			document.getElementById("user_code").style.display = "";
			document.getElementById("file_input").style.display = "none";
			document.getElementById("examplecode1").style.display = "none";
			document.getElementById("examplecode2").style.display = "none";
			document.getElementById("examplecode3").style.display = "none";
			document.getElementById("examplecode4").style.display = "none";
			document.getElementById("examplecode5").style.display = "none";
			document.getElementById("examplecode6").style.display = "none";
			document.getElementById("examplecode7").style.display = "none";
			document.getElementById("examplecode8").style.display = "none";
			document.getElementById("examplecode9").style.display = "none";
// 			document.getElementById("examplecode10").style.display = "none";
			document.getElementById("examplecode1Label").style.display = "none";
			document.getElementById("examplecode2Label").style.display = "none";
			document.getElementById("examplecode3Label").style.display = "none";
			document.getElementById("examplecode4Label").style.display = "none";
			document.getElementById("examplecode5Label").style.display = "none";
			document.getElementById("examplecode6Label").style.display = "none";
			document.getElementById("examplecode7Label").style.display = "none";
			document.getElementById("examplecode8Label").style.display = "none";
			document.getElementById("examplecode9Label").style.display = "none";
// 			document.getElementById("examplecode10Label").style.display = "none";
			document.getElementById("example_code").style.display = "none";
			/*             document.getElementById("example_code").readOnly = "false";
			 document.getElementById("user_code").readOnly = "false";*/
			document.getElementById("user_code").contentEditable = true;
			document.getElementById("example_code").contentEditable = true;
		} else if (exampleFile.checked) {
			document.getElementById("file_input").style.display = "none";
			document.getElementById("user_code").style.display = "none";
			document.getElementById("examplecode1").style.display = "";
			document.getElementById("examplecode2").style.display = "";
			document.getElementById("examplecode3").style.display = "";
			document.getElementById("examplecode4").style.display = "";
			document.getElementById("examplecode5").style.display = "";
			document.getElementById("examplecode6").style.display = "";
			document.getElementById("examplecode7").style.display = "";
			document.getElementById("examplecode8").style.display = "";
			document.getElementById("examplecode9").style.display = "";
// 			document.getElementById("examplecode10").style.display = "";
			document.getElementById("examplecode1Label").style.display = "";
			document.getElementById("examplecode2Label").style.display = "";
			document.getElementById("examplecode3Label").style.display = "";
			document.getElementById("examplecode4Label").style.display = "";
			document.getElementById("examplecode5Label").style.display = "";
			document.getElementById("examplecode6Label").style.display = "";
			document.getElementById("examplecode7Label").style.display = "";
			document.getElementById("examplecode8Label").style.display = "";
			document.getElementById("examplecode9Label").style.display = "";
// 			document.getElementById("examplecode10Label").style.display = "";
			document.getElementById("example_code").style.display = "none";
			/*             document.getElementById("example_code").readOnly = "false";
			 document.getElementById("user_code").readOnly = "false";*/
			document.getElementById("user_code").contentEditable = true;
			document.getElementById("example_code").contentEditable = true;
		}
	}

	/*Loads the selected example code to the page  */
	function handleExampleRadioClick(filename) {
		//var filepath="C:\\Users\\13022\\git\\cetusWebRepo\\cetusWeb\\resources\\examples\\";
		document.getElementById("example_code").style.display = "";
		document.getElementById("example_code").readOnly = "false";
		var textArea = document.getElementById("example_code");

		var ex1 = document.getElementById("examplecode1");
		var ex2 = document.getElementById("examplecode2");
		var ex3 = document.getElementById("examplecode3");
		var ex4 = document.getElementById("examplecode4");
		var ex5 = document.getElementById("examplecode5");
		var ex6 = document.getElementById("examplecode6");
		var ex7 = document.getElementById("examplecode7");
		var ex8 = document.getElementById("examplecode8");
		var ex9 = document.getElementById("examplecode9");
// 		var ex10 = document.getElementById("examplecode10");

		if (ex1.checked) {
			textArea.value = "/* The variable t is an array used temporarily during a single iteration of the outer loop. No value of t is used in an iteration other than the one that produced it. Without privatization, executing different iterations in parallel would create conflicts on accesses to t.  Declaring t private gives each thread a separate storage space, avoiding these conflicts.*/\n\n  int main(){\n  int n=10000; \n  float a[n][n], b[n][n], t[n];\n  int i, j;\n\n  for (i=1; i<n; i++) { \n    for (j=1; j<n; j++) {  \n      t[j] = a[i][j]+b[i][j]; \n    }\n    for (j=1; j<n; j++) {\n      b[i][j] =  t[j] + (t[j]*2); \n    } \n  }\n \n    return 0;\n}\n";
			textArea.contentEditable = true;
			textArea.readOnly = false;
		} else if (ex2.checked) {
			textArea.value = "/* The loop contains an array reduction operation*/\n\n  int main(){\n  float a[1000000], sum[1000000];\n  int i, n, tab[1000000];\n \n  /* define content of sum and a */ \n \n  for (i=1; i<1000000; i++) {\n    sum[tab[i]] = sum[tab[i]] + a[i];\n  }\n \n    return 0;\n}\n";
			textArea.contentEditable = true;
			textArea.readOnly = false;
		} else if (ex3.checked) {
			textArea.value = "/* The first loop includes a basic, linear induction variable ind. The second loop includes a more generalized induction variable, which uses a linear induction variable as the increment.*/\n\n  int main(){\n  float a[1000000], b[1000000];\n   int i, n, ind, ind2;\n \n  n = 1000000;\n  ind = 123;\n  for (i=1; i<n; i++) {\n    ind = ind + 2;\n    a[ind] = b[i];\n  }\n \n  ind2 = 5;\n  ind = 234;\n  for (i=1; i<n; i++) {\n    ind = ind + 2;\n    ind2 = ind2 + ind;\n    a[ind2] = b[i];\n  }\n \n    return 0;\n}\n";
			textArea.contentEditable = true;
			textArea.readOnly = false;
		} else if (ex4.checked) {
			textArea.value = "/* The variable t is used temporarily during a single loop iteration. No value of t is used in an iteration other than the one that produced it. Without privatization, executing different iterations in parallel would create conflicts on accesses to t.  Declaring t private gives each thread a separate storage space, avoiding these conflicts.*/\n\n  int main(){\n  float a[1000000], b[1000000], t;\n  int i, n;\n  n = 1000000;\n\n  for (i=1; i<n; i++) {\n    t = a[i]+b[i];\n    b[i] =  t + t*t;\n  }\n \n    return 0;\n}\n";
			textArea.contentEditable = true;
			textArea.readOnly = false;
		} else if (ex5.checked) {
			textArea.value = "/* Scalar, Additive Reduction */\n\n  int main(){\n  float a[1000000], sum;\n  int i, n;\n  n = 1000000;\n\n  for (i=1; i<n; i++) {\n    sum = sum + a[i];\n  }\n \n    return 0;\n}\n";
			textArea.contentEditable = true;
			textArea.readOnly = false;
		} else if (ex6.checked) {
			textArea.value = "/* Very Simple Parallelizable Loop Example */\n\n  int main(){\n  float a[1000000], b[1000000];\n  int i;\n \n  for (i=1; i<1000000; i++) {\n    a[i]= b[i];\n  }\n \n    return 0;\n}\n";
			textArea.contentEditable = true;
			textArea.readOnly = false;
		}else if (ex7.checked) {
			textArea.value = "/*In this loop, there is an antidependency between the variable a(i) and the variable a(i+2). That is, you must be sure that the instruction that uses a(i+2) does so before the previous one redefines it.*/\n\n  int main(){\n  double a[1000000], b[1000000];\n  int j = 0;\n  int n = 1000000;\n  int e = 2;\n  int c = 3;\n \n  for (j = 0; j < n; j++) {\n    a[j] = j + 1000;\n    b[j] = j + 1;\n  }\n \n  for (j = 0; j < n; j++) {\n    a[j] = b[j] * e; \n    b[j] = a[j+2] * c;\n  }\n \n    return 0;\n}\n";
			textArea.contentEditable = true;
			textArea.readOnly = false;
		}else if (ex8.checked) {
			textArea.value = "/*In this case, there is a dependency problem. The value of a(i+1) depends on the value of a(i), the value of A(i+2) depends on A(i+1), and so on; every iteration depends on the result of a previous one. Dependencies that extend back to a previous iteration (like this one), are loop carried flow dependencies or backward dependencies. */\n\n  int main(){\n  double a[1000000],b[1000000];\n  int j = 0;\n  int n = 1000000;\n \n  for (j = 0; j < n; j++) {\n    a[j] = j + 1000;\n    b[j] = j * 2;\n  }\n \n  for (j = 1; j < n; j++) {\n    a[j] = a[j - 1] + b[j];\n  }\n \n    return 0;\n}\n";
			textArea.contentEditable = true;
			textArea.readOnly = false;
		}else if (ex9.checked) {
			textArea.value = "/* An output dependence occurs when a location in memory is written to before that same location is written to again in another statement.*/\n\n  int main(){\n  double a[1000000];\n  int j = 0;\n  int n = 1000000;\n \n  for (j = 0; j < n; j++) {\n    a[j] = j + 1000;\n  }\n \n  for (j = 0; j < n; j++) {\n    a[j] = j;\n    a[j+1] = 5;\n  }\n \n    return 0;\n}\n";
			textArea.contentEditable = true;
			textArea.readOnly = false;
		}
//  		else if (ex10.checked) {
// 			textArea.value = "/* Apply the CaRV Tool to optimize the specified experimental section in this parallelizable loop. */\n\n  void main(){\n  float a[1000000], b[1000000];\n  int i;\n \n  #pragma experimental section start\n  for (i=1; i<1000000; i++) {\n    a[i]= b[i];\n  }\n  #pragma experimental section stop\n  testprocedure();\n    return ;\n}\n int testprocedure(){\n return 0; \n }\n\n";
// 			textArea.contentEditable = true;
// 			textArea.readOnly = false;
// 		} 
		/*         document.getElementById("example_code").readOnly = "false";*/
		/*         document.getElementById("user_code").contentEditable = true;
		 document.getElementById("example_code").contentEditable = true;  */
	}
	/* Running Cetus in Auto mode or customized mode */
	function handleRadioClick() {
		var semiAuto = document.getElementById("radioSemiAuto");
		var Auto = document.getElementById("radioAuto");
		var Exec = document.getElementById("radioNoCompile");
		var CaRV = document.getElementById("radioCaRV");
		//var query = document.getElementById("queries");
		/* Customize Cetus settings */
		if (semiAuto.checked) {
			document.getElementById("myDIV").style.display = "";
			document.getElementById("inlineFormCustomSelect").style.display = "";
			document.getElementById("inlineFormCustomSelectRange").style.display = "";
			document.getElementById("inlineFormCustomSelectAlias").style.display = "";
			document.getElementById("inlineFormCustomSelectPloops").style.display = "";
			document.getElementById("inlineFormCustomSelectprivatize").style.display = "";
			document.getElementById("inlineFormCustomSelectReduction").style.display = "";
			document.getElementById("inlineFormCustomSelectInduction").style.display = "";
			document.getElementById("inlineFormCustomSelectProfiling").style.display = "";
			document.getElementById("inlineFormCustomSelectEliminateBranch").style.display = "";
			document.getElementById("inlineFormCustomSelectProfitableOmp").style.display = "";
			document.getElementById("labelCallgraph").style.display = "";
			document.getElementById("check_callgraph").style.display = "";
			document.getElementById("label_normalize-rtn-stmt").style.display = "";
			document.getElementById("check_normalize-rtn-stmt").style.display = "";
			document.getElementById("label_tsingle-call").style.display = "";
			document.getElementById("check_tsingle-call").style.display = "";
			document.getElementById("label_tsingle-declarator").style.display = "";
			document.getElementById("check_tsingle-declarator").style.display = "";
			document.getElementById("label_tsingle-rtn").style.display = "";
			document.getElementById("check_tsingle-rtn").style.display = "";
			document.getElementById("label_normalize_loops").style.display = "";
			document.getElementById("check_normalize_loops").style.display = "";
			//document.getElementById("queries").style.display = "none";
			document.getElementById("addspace").style.display = "none";

			/* Running Cetus with default options,not showing the menu to the user */
		} else if (Auto.checked || Exec.checked || CaRV.checked) {
			document.getElementById("myDIV").style.display = "none";
			document.getElementById("inlineFormCustomSelect").style.display = "none";
			document.getElementById("inlineFormCustomSelectRange").style.display = "none";
			document.getElementById("inlineFormCustomSelectAlias").style.display = "none";
			document.getElementById("inlineFormCustomSelectPloops").style.display = "none";
			document.getElementById("inlineFormCustomSelectprivatize").style.display = "none";
			document.getElementById("inlineFormCustomSelectReduction").style.display = "none";
			document.getElementById("inlineFormCustomSelectInduction").style.display = "none";
			document.getElementById("inlineFormCustomSelectProfiling").style.display = "none";
			document.getElementById("inlineFormCustomSelectEliminateBranch").style.display = "none";
			document.getElementById("inlineFormCustomSelectProfitableOmp").style.display = "none";
			document.getElementById("labelCallgraph").style.display = "none";
			document.getElementById("check_callgraph").style.display = "none";
			document.getElementById("label_normalize-rtn-stmt").style.display = "none";
			document.getElementById("check_normalize-rtn-stmt").style.display = "none";
			document.getElementById("label_tsingle-call").style.display = "none";
			document.getElementById("check_tsingle-call").style.display = "none";
			document.getElementById("label_tsingle-declarator").style.display = "none";
			document.getElementById("check_tsingle-declarator").style.display = "none";
			document.getElementById("label_tsingle-rtn").style.display = "none";
			document.getElementById("check_tsingle-rtn").style.display = "none";
			document.getElementById("label_normalize_loops").style.display = "none";
			document.getElementById("check_normalize_loops").style.display = "none";
			//document.getElementById("queries").style.display = "none";
			document.getElementById("addspace").style.display = "none";

		} else {
			//document.getElementById("queries").style.display = "";
			document.getElementById("addspace").style.display = "";
			document.getElementById("myDIV").style.display = "none";
			document.getElementById("inlineFormCustomSelect").style.display = "none";
			document.getElementById("inlineFormCustomSelectRange").style.display = "none";
			document.getElementById("inlineFormCustomSelectAlias").style.display = "none";
			document.getElementById("inlineFormCustomSelectPloops").style.display = "none";
			document.getElementById("inlineFormCustomSelectprivatize").style.display = "none";
			document.getElementById("inlineFormCustomSelectReduction").style.display = "none";
			document.getElementById("inlineFormCustomSelectInduction").style.display = "none";
			document.getElementById("inlineFormCustomSelectProfiling").style.display = "none";
			document.getElementById("inlineFormCustomSelectEliminateBranch").style.display = "none";
			document.getElementById("inlineFormCustomSelectProfitableOmp").style.display = "none";
			document.getElementById("labelCallgraph").style.display = "none";
			document.getElementById("check_callgraph").style.display = "none";
			document.getElementById("label_normalize-rtn-stmt").style.display = "none";
			document.getElementById("check_normalize-rtn-stmt").style.display = "none";
			document.getElementById("label_tsingle-call").style.display = "none";
			document.getElementById("check_tsingle-call").style.display = "none";
			document.getElementById("label_tsingle-declarator").style.display = "none";
			document.getElementById("check_tsingle-declarator").style.display = "none";
			document.getElementById("label_tsingle-rtn").style.display = "none";
			document.getElementById("check_tsingle-rtn").style.display = "none";
			document.getElementById("label_normalize_loops").style.display = "none";
			document.getElementById("check_normalize_loops").style.display = "none";

		}

	}
</script>