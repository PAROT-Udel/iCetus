/**
 * To connect to MySQL DB and Insert User info into the DB
 */
package cetus.registration.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//import com.mysql.cj.xdevapi.Statement;

import cetus.registration.model.User;

/**
 * @author 13022
 *
 */
public class UserDAO {
	int generatedKey = 0;

	 public int insertUserRequest(User user) throws ClassNotFoundException {
	        String INSERT_USERS_SQL = "INSERT INTO users.user" +
	            "  ( input_code, cetus_option_set, input_content) VALUES " + 
	            " ( ?,?,?);";

	        int result = 0;

	        Class.forName("com.mysql.cj.jdbc.Driver");
//Connecting to MySQl DB: users Schema/database
	        try (Connection connection = DriverManager
	            .getConnection("jdbc:mysql://localhost:3306/users?useSSL=false", "root", "Bezanberim");

	            // Step 2:Create a statement using connection object
	            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL, Statement.RETURN_GENERATED_KEYS)) {
	            preparedStatement.setString(1, user.getInputCode());
	            preparedStatement.setString(2, user.getCetusOptionSet());
	            preparedStatement.setString(3, user.getInputContent());
//	            System.out.println(preparedStatement);
	            // Step 3: Execute the query or update query
	            result = preparedStatement.executeUpdate();
//	            int UserIdNumber= preparedStatement.executeUpdate(INSERT_USERS_SQL, Statement.RETURN_GENERATED_KEYS);

	            ResultSet rs = preparedStatement.getGeneratedKeys();
//              getting the last id in DB
	            
	            if (rs.next()) {
	                generatedKey = rs.getInt(1);
	            }
	             
//	            System.out.println("Inserted record's ID in DB is: " + generatedKey);
//	            System.out.println("printing generated user id key: "+ preparedStatement.getGeneratedKeys());
	            
	        } catch (SQLException e) {
	            // process sql exception
	            printSQLException(e);
	        }
//	        } finally {
//	            if (rs != null) {
//	                try {
//	                	rs.close();
//	                } catch (SQLException e) { /* Ignored */}
//	            }
//	            if (preparedStatement != null) {
//	                try {
//	                	preparedStatement.close();
//	                } catch (SQLException e) { /* Ignored */}
//	            }
//	            if (connection != null) {
//	                try {
//	                	connection.close();
//	                } catch (SQLException e) { /* Ignored */}
//	            }
//	        }
	        return result;
	    }

	    private void printSQLException(SQLException ex) {
	        for (Throwable e: ex) {
	            if (e instanceof SQLException) {
	                e.printStackTrace(System.err);
	                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
	                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
	                System.err.println("Message: " + e.getMessage());
	                Throwable t = ex.getCause();
	                while (t != null) {
	                    System.out.println("Cause: " + t);
	                    t = t.getCause();
	                }
	            }
	        }
	    }
	    
	    
	    
		 public int insertCetusResults(User user) throws ClassNotFoundException {
		        String UPDATE_USERS_SQL = "UPDATE users.user" +
		            "  SET cetus_output_filepath = ?, cetus_passes_filepath = ? , cetus_Analysis_filepath = ? , cetus_output_content=? , cetus_passes_content=?, cetus_Analysis_content=? " + 
		            " where id = ?;";

		        int result = 0;
//		        System.out.println("To update the related record Inserted record's ID in DB is: " + generatedKey);
		        Class.forName("com.mysql.cj.jdbc.Driver");
//              Connecting to MySQl DB: users Schema/database
		        try (Connection connection = DriverManager
		            .getConnection("jdbc:mysql://localhost:3306/users?useSSL=false", "root", "Bezanberim");
		        		  
		            // Step 2:Create a statement using connection object
		            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USERS_SQL)) {
		            preparedStatement.setString(1, user.getCetusOutput());
		            preparedStatement.setString(2, user.getCetusDebuggerReport());
		            preparedStatement.setString(3, user.getCetusAnalysisReport());
		            preparedStatement.setString(4, user.getCetusOutputContent());
		            preparedStatement.setString(5, user.getCetusPassesContent());
		            preparedStatement.setString(6, user.getCetusAnalysisConetent());
		            preparedStatement.setInt(7, generatedKey);

//		            System.out.println(preparedStatement);
		            // Step 3: Execute the query or update query
		            result = preparedStatement.executeUpdate();
		          

		        } catch (SQLException e) {
		            // process sql exception
		            printSQLException(e);
		        }
		        return result;
		    }

//		    private void printSQLExceptionn(SQLException ex) {
//		        for (Throwable e: ex) {
//		            if (e instanceof SQLException) {
//		                e.printStackTrace(System.err);
//		                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
//		                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
//		                System.err.println("Message: " + e.getMessage());
//		                Throwable t = ex.getCause();
//		                while (t != null) {
//		                    System.out.println("Cause: " + t);
//		                    t = t.getCause();
//		                }
//		            }
//		        }
//		    }
		    
		    
			 public int updateCetusResults(User user) throws ClassNotFoundException {
			        String UPDATE_USERS_SQL = "UPDATE users.user" +
			            "  SET cetus_Analysis_content= CONCAT(cetus_Analysis_content, ?) " + 
			            " where id = ?;";

			        int result = 0;
//			        System.out.println("To update the related record Inserted record's ID in DB is: " + generatedKey);
			        Class.forName("com.mysql.cj.jdbc.Driver");
//	              Connecting to MySQl DB: users Schema/database
			        try (Connection connection = DriverManager
			            .getConnection("jdbc:mysql://localhost:3306/users?useSSL=false", "root", "Bezanberim");
			        		  
			            // Step 2:Create a statement using connection object
			            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USERS_SQL)) {
			            preparedStatement.setString(1, user.getCetusAnalysisConetent());
			            preparedStatement.setInt(2, generatedKey);

//			            System.out.println(preparedStatement);
			            // Step 3: Execute the query or update query
			            result = preparedStatement.executeUpdate();
			          

			        } catch (SQLException e) {
			            // process sql exception
			            printSQLException(e);
			        }
			        return result;
			    }

//			    private void printSQLExceptionn(SQLException ex) {
//			        for (Throwable e: ex) {
//			            if (e instanceof SQLException) {
//			                e.printStackTrace(System.err);
//			                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
//			                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
//			                System.err.println("Message: " + e.getMessage());
//			                Throwable t = ex.getCause();
//			                while (t != null) {
//			                    System.out.println("Cause: " + t);
//			                    t = t.getCause();
//			                }
//			            }
//			        }
//			    }
	}
