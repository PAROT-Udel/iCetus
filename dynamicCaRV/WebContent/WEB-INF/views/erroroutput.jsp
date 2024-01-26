<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.lang.String"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.*"%>
<%@page import="cetus.registration.model.User"%>
<%
	//the user will just see the processed html file. nothing about database connection details is shown to the user.
User user = new User();

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
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
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
<!-- for textarea with line numbers -->

<script src="https://code.jquery.com/jquery-1.12.4.min.js"
	integrity="sha384-nvAa0+6Qg9clwYCGGPpDQLVpLNn0fRaROjHqs13t4Ggj3Ez50XnGQqc/r8MhnRDZ"
	crossorigin="anonymous"></script>
<script src="jquery-linedtextarea.js"></script>
<link href="jquery-linedtextarea.css" rel="stylesheet">
<script type="text/javascript" src="ta-ln.min.js"></script>

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
	/* font-family: "Times New Roman", Times, serif; */
}

.col-form-label1 {
	vertical-align: middle;
	font-size: 16px;
	width: 450px;
	/* font-family: "Times New Roman", Times, serif; */
}

.row {
	display: flex;
	/*             margin-right: 15px; */
	padding: 2px;

	/*             height:500px; */
}

/* Create two equal columns that sits next to each other */
.column {
	flex: 49%;
	padding: 5px;
	height: 850px;
}

button {
	margin-left: 10px;
	margin-right: 5px;
	padding: 2px;
	width: 150px;
}

.boxsizingBorder {
	-webkit-box-sizing: border-box;
	-moz-box-sizing: border-box;
	box-sizing: border-box;
}

div.LN_area {
	/* Functionality */
	height: 250px;
}

/*        #myInput { */
/* Style */
/*         color: #000;
        background: #FFF;
        border: 1px solid grey; */

/* Functionality */
/*   padding-top: 0.25rem; */
/*height: 800px;  */
/*      }  */

/* textarea {
  background: url(http://i.imgur.com/2cOaJ.png);
  background-attachment: local;
  background-repeat: no-repeat;
  padding-left: 35px;
  padding-top: 30px;
  border-color: #ccc;

  font-size: 13px;
  line-height: 14px;
}

.textarea-wrapper {
  display: inline-block;
  background-image: linear-gradient(#F1F1F1 50%, #F9F9F9 50%);
  background-size: 300% 32px;
  background-position: left 10px;
} */
/* textarea {
  width: 100%;
  min-height: 100px;
  background: url(http://i.imgur.com/2cOaJ.png) top 17.5px left / auto no-repeat, 
              linear-gradient(#F1F1F1 50%, #F9F9F9 50%) top left / 100% 32px;
  border: 1px solid #CCC;
  box-sizing: border-box;
  padding: 0px 0 0 30px;
  resize: vertical;
  line-height: 16.5px;
  font-size: 14px;
} */
</style>

<title>Cetus Error</title>

</head>

<body Style="background-color: rgba(192, 192, 192, 0.2);">

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

	<button type="button" class="btn btn-primary" style="align: left;"
		onclick="history.go(-1);">Back</button>


	<table style="width: 100%">
		<%
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
			String theErrr = (String) request.getAttribute("theERrror");
		//System.out.println(theErrr);  
		String replaced = theErrr.replaceAll("\\s+at ", "<br>&nbsp; &nbsp; &nbsp; &nbsp;  at ");
		//System.out.println(replaced);
		%>


		<div class="row">

			<div class="column">

				<div class="form-row"></div>
				<div class="form-row">
					<div align="left">
						Database Entry id:
						<%=resultSet.getInt("id")%></div>
				</div>
				<div class="form-row">
					<div align="left">
						<h4>Cetus Input</h4>
					</div>
				</div>

				<div class="form-row">
					<%-- 					<div align="left">
						Cetus Input File Path: <br><%=resultSet.getString("input_code") %></div> --%>
				</div>

				<%-- <div class="form-row">
<div align="left"> View Cetus Input File: <a href=<%=resultSet.getString("input_code") %> target=_blank>View Cetus Input File </a></div>
</div>
<div class="form-row">
<div align="left"> View Cetus Input File: <%=resultSet.getString("input_content") %> View Cetus Input File </div>
</div> --%>

				<div class="form-row">
					<div align="left">
						Cetus Input File Content: <br>
						<div>
							<textarea id="myInput" rows="25" readonly=" readonly" WRAP="off"
								style="border-radius: 10px; padding: 25px; width: 100%; box-sizing: border-box;"><%=user.getFileOutput(resultSet.getString("input_code"))%></textarea>
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
						<h4>Cetus Error</h4>
					</div>
				</div>

				<div class="form-row">
					<%-- 					<div align="left">
						Cetus Output File Path: <br><%=resultSet.getString("cetus_output_filepath") %></div> --%>
				</div>


				<div class="form-row">
					<div align="left">
						Cetus Error Message: <br>
						<div class="textarea-wrapper">
							<textarea rows="25" readonly=" readonly" WRAP="off"
								style="border-radius: 10px; padding: 5px; width: 100%; box-sizing: border-box;"><%=theErrr%></textarea>
						</div>
					</div>
				</div>


			</div>
		</div>
		<%
			connection.close();
		} catch (Exception e) {
		e.printStackTrace();
		}
		%>
	</table>


	<!-- 	<script type="text/javascript"
		src="https://www.cssscript.com/demo/line-numbers-texarea/ta-ln.min.js"></script> -->
	<script type="text/javascript">
		var input = document.getElementById("myInput");

		LNPrefix(input);
		input.addEventListener("input", LNPrefix.bind(this, input));
		
		function LNPrefix(d) {
			var f = d.parentElement, c = d.value.split(/\r?\n/).length + 10;
			//textarea  set line-height here if numbers dont match line numbers
			d.style.cssText = "width:95%;resize:none;line-height: 1.55;border-radius: 10px; box-sizing: border-box;height:620px;padding-top: 10x;";
			f.classList.add("LN_area");
			//numbers
			f.style.cssText = "overflow:hidden;height:620px;";
			function g(j, h) {
				var i = document.createElement("div");
				i.innerText = h;
				i.classList.add("LN_n");
				i.style.cssText = "text-align:right;padding-right:.5rem;";
				j.appendChild(i)
			}
			var b = document.getElementsByClassName("LN_sb")[0];
			if (b) {
				f.removeChild(b)
			}
			var e = document.createElement("div");
			e.classList.add("LN_sb");
			e.style.cssText = "padding-top:.375rem;display:inline-block;float:left;width:auto;";
			f.insertBefore(e, d);
			for (var a = 0; a < c; a++) {
				g(document.getElementsByClassName("LN_sb")[0], a + 1)
			}
			input.addEventListener("scroll", function(i) {
				var h = this.parentElement.children[0].style, j = h.margin
						- this.scrollTop;
				h.marginTop = String(j) + "px";
				this.parentElement.style.overflow = "hidden"
			})
		};
	</script>
</body>
</html>