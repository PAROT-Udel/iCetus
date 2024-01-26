<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<body>

<c:forEach var="alibFile" items="${libFiles}">
    <p> ${alibFile} </p>
</c:forEach>

</body>
</html>