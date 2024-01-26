<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.SQLException"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<body>

<h1>Retrieve data from database </h1>
<table border="0">
<tr>
<td>cetus_output</td>
<td>cetus_debugger_report</td>
</tr>
<%
try{
int generatedKey = 0;
String SELECT_USERS_SQL = "select * from users.user;";
int result = 0;
Class.forName("com.mysql.cj.jdbc.Driver");
Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users?useSSL=false", "root", "Bezanberim");
	  
// Step 2:Create a statement using connection object
PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USERS_SQL);
// Step 3: Execute the query or update query
result = preparedStatement.executeUpdate();
ResultSet resultSet = preparedStatement.getGeneratedKeys();
// if(rs.last()){
//     generatedKey=rs.getInt("id");
// }

if (resultSet.next()) {
    generatedKey = resultSet.getInt(1);
}
 

%>
<tr>
<td><%=resultSet.getString("cetus_output") %></td>
<td><%=resultSet.getString("cetus_debugger_report") %></td>
</tr>

<%
connection.close();
} catch (Exception e) {
e.printStackTrace();
}
%>
</table>
</body>
</html>