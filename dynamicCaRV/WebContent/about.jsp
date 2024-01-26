<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
<!-- Global site tag (gtag.js) - Google Analytics -->
<script async src="https://www.googletagmanager.com/gtag/js?id=G-J9XNTHF4C4"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());

  gtag('config', 'G-J9XNTHF4C4');
</script>

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

<style type="text/css">
.div1 {
	text-align: justify;
	text-justify: inter-word;
/* 	height: 100px; */
}

.jumbotron {
	padding: 0rem 0rem;
}
</style>

<title>About our team</title>

</head>
<body>
	<%
String pathWebcontent = request.getContextPath();
System.out.print(pathWebcontent);
	
// Get the address of the project (for example: http://localhost:8080/MyApp/) and assign it to the basePath variable.
String basePath = request.getScheme()+"://" +request.getServerName()+":"+request.getServerPort()+pathWebcontent+"/" ;
//Put the "project path basePath" into the pageContext and read it later with the EL expression.
pageContext.setAttribute("basePath", basePath);
%>
	<base href="<%=basePath%>">
	<nav class="navbar navbar-dark bg-primary navbar-expand-lg">
		<!--         <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" -->
		<!--       aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation"> -->
		<!--       <span class="navbar-toggler-icon"></span> -->
		<!--         </button> -->
		<div class="collapse navbar-collapse" id="navbarSupportedContent">
			<ul class="navbar-nav mr-auto">
				<li class="nav-item "><a class="nav-link "
					href="<%=pathWebcontent%>/" style="color: white;">Home </a></li>
				<li class="nav-item"><a class="nav-link active "
					style='color: white; font-weight: bold;'
					href="<%=pathWebcontent%>/about.jsp">About us</a></li>
		        <li class="nav-item">
         			 <a class="nav-link " style='color: white' href="<%=pathWebcontent%>/project.jsp">About the project</a>
      		    </li> 

			</ul>
		</div>
	</nav>



	<div class="jumbotron div1" >
	<div style="font-size:20px;"><img src="<%=request.getContextPath()%>/resources/img/iCetus.png" width="70" height="70" Style="vertical-align:middle;margin:10px 10px;"
                class="d-inline-block " alt="iCetus">ABOUT OUR TEAM</div>
	
		
	</div>

	<div class="container" style="margin-left: 50px">

		<div class="row">
			<div class="col-md-2">
				<img src="<%=pathWebcontent%>/resources/img/Rudi.jpg" align="Left" class="img-rounded"
					alt="Rudolf Eigenmann" style="width: 100%">

			</div>
			<div class="col-md-10">
				<br>
				<p align="left">
					<b>Rudolf Eigenmann</b></br> University of Delaware (Electrical
					Engineering/ Computer Science)</br> Optimizing Compilers, High-performance computing,
					Cyberinfrastructure</br>
					</br> <i>Website:</i><a href="https://www.eecis.udel.edu/~eigenman"
						target="_blank"> https://www.eecis.udel.edu/~eigenman</a></br> <i>Email:</i>
					eigenman [at] udel [dot] edu</br>
				</p>
			</div>
		</div>


		<hr>
		<div class="row">
			<div class="col-md-2">
				<img src="<%=pathWebcontent%>/resources/img/parinaz.jpg" class="img-rounded" alt="Parinaz Barakhshan"
					style="width: 100%" />
			</div>
			<div class="col-md-10">
				<br>
				<p align="left">
					<b>Parinaz Barakhshan</b></br> University of Delaware (Computer
					Engineering)</br> Optimizing Compilers, High-performance computing, Programming methodologies</br>
					<br> <i>Website:</i><a href="https://sites.udel.edu/parinazb/"
						target="_blank"> https://sites.udel.edu/parinazb/</a></br> <i>Email:</i>
					parinazb [at] udel [dot] edu</br>
				</p>
			</div>
		</div>
</body>
</html>