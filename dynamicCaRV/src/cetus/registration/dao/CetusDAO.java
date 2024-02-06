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
public class CetusDAO {
	int generatedKey = 0;

	public int insertCetusResults(User user) throws ClassNotFoundException {
		String INSERT_USERS_SQL = "INSERT INTO users.cetus"
				+ "  ( user_id, input_path, input_content, output_path, output_content, cetus_options,passes_path,passes_content, analysis_path, analysis_content, exp_section) VALUES "
				+ " ( ?,?,?,?,?,?,?,?,?,?,?);";
		int result = 0;

		Class.forName("com.mysql.cj.jdbc.Driver");
//Connecting to MySQl DB: users Schema/database
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users?useSSL=false",
				"root", "Bezanberim");

				// Step 2:Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL,
						Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, user.getFkId());
			preparedStatement.setString(2, user.getInputCode());
			preparedStatement.setString(3, user.getInputContent());
			preparedStatement.setString(4, user.getCetusOutput());
			preparedStatement.setString(5, user.getCetusOutputContent());
			preparedStatement.setString(6, user.getCetusOptionSet());
			preparedStatement.setString(7, user.getCetusDebuggerReport()); // path
			preparedStatement.setString(8, user.getCetusPassesContent()); // content
			preparedStatement.setString(9, user.getCetusAnalysisReport());// path
			preparedStatement.setString(10, user.getCetusAnalysisConetent()); // content
			preparedStatement.setString(11, user.getExperimentalSection());
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

		return result;
	}

	private void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
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
}