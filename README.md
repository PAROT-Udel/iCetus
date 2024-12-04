# iCetus: An Interactive Parallelization Tool integrated with CaRV and GPT4

**iCetus** is an advanced tool designed to enhance and accelerate the parallelization process of scientific applications. It extends the capabilities of the **Cetus compiler**, a state-of-the-art source-to-source compiler for automatic parallelization, by integrating interactive features that allow users to guide and fine-tune the parallelization process.

---

## Development of iCetus

**iCetus** has been developed as a Java-based web application using the following technologies and tools:

### Frontend
- **HTML**: Version 5.0  
- **CSS**: Version 2.0  
- **JavaScript**: Version 1.0  

### Backend
- **Java**: Version 11.0.2  
- **Servlet API**: Version 3  
- **JSP (JavaServer Pages)**: Version 2.2  
- **JSTL (JavaServer Pages Standard Tag Library)**: Version 1.2  
- **OpenMP**: Version 3  
- **GCC**: Version 9.2.0  

### Server
- **Apache Tomcat**: Version 9.0.41  

### Database
- **MySQL Connector**: Version 8.0  

### Development Environment
- **Eclipse IDE**

---

## How to Run iCetus

To run **iCetus**, follow these steps:

1. **Ensure MySQL is Running**:
   - Start the MySQL server to support the database connections required by iCetus.

2. **Deploy iCetus on Tomcat**:
   - Deploy the iCetus web application to the Apache Tomcat server (v9.0.41).

3. **Run the Application**:
   - Start the Tomcat server.
   - Access the iCetus application through the configured server URL in your browser.
  
---
  
### Note
To access **GPT**, the API key must be added to the code in `src/cetus/registration/controller/UserServlet.java`.

A placeholder has been provided for this purpose.
