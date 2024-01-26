<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
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

<meta charset="ISO-8859-1">
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
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

<style>
.div1 {
	text-align: justify;
	text-justify: inter-word;
	/* 	height: 100px; */
}

.jumbotron.div1 {
	padding: 1rem 1rem;
}

container{
padding: 1rem 1rem;
}
</style>

<title>About the Project</title>

</head>
<body>
	<%
		String pathWebcontent = request.getContextPath();
	System.out.print(pathWebcontent);

	// Get the address of the project (for example: http://localhost:8080/MyApp/) and assign it to the basePath variable.
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + pathWebcontent
			+ "/";
	//Put the "project path basePath" into the pageContext and read it later with the EL expression.
	pageContext.setAttribute("basePath", basePath);
	%>
	<nav class="navbar navbar-dark bg-primary navbar-expand-lg">
		<div class="collapse navbar-collapse" id="navbarSupportedContent">
			<ul class="navbar-nav mr-auto">
				<li class="nav-item "><a class="nav-link "
					href="<%=pathWebcontent%>/"
					style="font-weight: bold; color: white;">Home </a></li>
				<li class="nav-item"><a class="nav-link " style='color: white'
					href="<%=pathWebcontent%>/about.jsp">About us</a></li>
				<li class="nav-item"><a class="nav-link active"
					style='color: white' href="<%=pathWebcontent%>/project.jsp">About
						the project</a></li>
			</ul>
		</div>
	</nav>



	<div class="jumbotron div1">
		<div style="font-size: 20px;">
			<img src="<%=request.getContextPath()%>/resources/img/iCetus.png"
				width="70" height="70"
				Style="vertical-align: middle; margin: 10px 10px;"
				class="d-inline-block " alt="iCetus">ABOUT THE PROJECT
		</div>


	</div>

	<div class="container div1" style="margin-left: 50px;text-align: justify;width:90%;">
		
		<p>
		<h3>Our Motivation:</h3>
		Today's computers are all Multicores. With parallelization techniques, one can convert sequential code into multi-threaded or vectorized code to simultaneously use multiple processors' power in a modern shared-memory Architecture. 
		</p>
		
		<p>
		<h3>Our Goal:</h3>
		 Our aim for the iCetus tool is to involve the user in the decisions that compilers struggle with. User feedback is being factored into program parallelization. 
		 To that end, iCetus provides the  user  with  information  about  how  the  compiler  analyzes, transforms, and parallelizes the program, as well as displaying 
		 the speedup gained from applying such  optimization to the code. It offers a user interface for controlling program parallelization, based on this information. 
		 Doing so combines user knowledge and classical compiler capabilities. 
		</p>

		<p>iCetus can be used for self-paced learning of different
			parallelization techniques. <!-- We are currently working on making Cetus
			accessible through a web-portal for convenient code generation and
			testing on computational resources of the national
			CyberInfrastructure (CI), and engaging the community in the
			development process and the usage of Cetus. --></p>
			
		<p>
		<h4>Cetus, the Compiler Engine:</h4>	
		The underneath compiler infrastructure used in this project is <a href="https://engineering.purdue.edu/Cetus/" page=_blank>Cetus</a>. Cetus
		is a source-to-source compiler research infrastructure supported by
		the National Science Foundation(NSF). Cetus	assists domain experts and researchers in efficiently parallelizing
		their existing C/C++ applications using OpenMP parallel programming
		model. It represents one of several software infrastructures that
		support research and development of program analysis, optimization,
		and translation techniques.</p>
	</div>





</body>
</html>