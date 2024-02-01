/**
 * 
 */
package cetus.registration.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cetus.registration.model.Carvreplay;

/**
 * @author 13022
 *
 */
public class CarvreplayDAO {
	int generatedKey = 0;
	
	//to get the last replay id so that we can add the replay id to execution results we rae saving in user database
	public int getLastReplayId()throws SQLException, ClassNotFoundException {
		int lastReplayId = 0;
		String QUERY_SQL = "SELECT carv_replay_id FROM users.carvreplays ORDER BY carv_replay_id DESC LIMIT 1;" ;
	    // Load the JDBC driver
        Class.forName("com.mysql.cj.jdbc.Driver");
        //Connecting to MySQl DB: users Schema/database
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users?useSSL=false", "root", "Bezanberim");
            // Step 2:Create a statement using connection object
        	PreparedStatement preparedStatement = connection.prepareStatement(QUERY_SQL);
            //System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet resultSet = preparedStatement.executeQuery()) {

                // Retrieve the last replay ID from the result set
                if (resultSet.next()) {
                    lastReplayId = resultSet.getInt("carv_replay_id");
                }
            } catch (SQLException e) {
                // Handle SQL exceptions
                printSQLException(e);
            }

            return lastReplayId;
	}
	
	
	public int insertReplayRequest(Carvreplay replay) throws ClassNotFoundException {
        String INSERT_REPLAY_SQL = "INSERT INTO users.carvreplays" +
            "  ( user_id, datafile_path, datafile_content, experimental_section, execution_results) VALUES " + 
            " ( ?,?,?,?,?);";

        int result = 0;

        Class.forName("com.mysql.cj.jdbc.Driver");
//Connecting to MySQl DB: users Schema/database
        try (Connection connection = DriverManager
            .getConnection("jdbc:mysql://localhost:3306/users?useSSL=false", "root", "Bezanberim");

            // Step 2:Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_REPLAY_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, replay.getUserid());
            preparedStatement.setString(2, replay.getReplayfilepath());
            preparedStatement.setString(3, replay.getReplayfilecontent());
            preparedStatement.setString(4, replay.getReplayexpsection());
            preparedStatement.setString(5, replay.getReplayexecutionresults());
//            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            result = preparedStatement.executeUpdate();
//            int UserIdNumber= preparedStatement.executeUpdate(INSERT_USERS_SQL, Statement.RETURN_GENERATED_KEYS);

            ResultSet rs = preparedStatement.getGeneratedKeys();
//          getting the last id in DB
            
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }
             
//            System.out.println("Inserted record's ID in DB is: " + generatedKey);
//            System.out.println("printing generated user id key: "+ preparedStatement.getGeneratedKeys());
            
        } catch (SQLException e) {
            // process sql exception
            printSQLException(e);
        }

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
    
    
    public static List<Carvreplay> getReplays(int userId) throws SQLException {
        List<Carvreplay> replays = new ArrayList<>();

        try (Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/users?useSSL=false", "root", "Bezanberim");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users.carvreplays WHERE user_id = ? ORDER BY carv_replay_id DESC");
        ) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int replayid = resultSet.getInt("carv_replay_id");
                String replayfilepath= resultSet.getString("datafile_path");
                String replayfilecontent = resultSet.getString("datafile_content");
                String experimentalSection = resultSet.getString("experimental_section");
                String executionResults = resultSet.getString("execution_results");
                
                Carvreplay replay = new Carvreplay(replayid, userId, replayfilepath, replayfilecontent, experimentalSection, executionResults);//replayfilepath, replayfilecontent,
                		
                replays.add(replay);
            }
        }

        return replays;
    }
	
}
