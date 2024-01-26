<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ page isELIgnored="false"%>

<%    
//String pathWebcontent = request.getContextPath();
//String contextPath = getServletContext().getRealPath("/");
 String filename =  session.getAttribute("passesfilenamePara").toString();
 System.out.println("filename "+filename );
 String filepath =  session.getAttribute("passesfilepathPara").toString();
 System.out.println("filepath "+filepath );
 
  response.setContentType("APPLICATION/OCTET-STREAM");   
  response.setHeader("Content-Disposition","attachment; filename=\"" + filename + "\"");   
  
  java.io.FileInputStream fileInputStream=new java.io.FileInputStream(filepath + filename);  
            
  int i;   
  while ((i=fileInputStream.read()) != -1) {  
    out.write(i);   
  }   
  fileInputStream.close(); 
  
%>   