package model;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;


/**
 * Bean to represent the Users.
 * @author Tarek Foyz Ahmed
 */
public class Users {
  
    private ResultSet resultSet = null;
    private PreparedStatement preparedStatement = null;
    DataSource dataSource = null;
   
    public Users() {
        try {
            // Obtain the environment naming context
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            // Look up the data source
            dataSource = (DataSource)envCtx.lookup("jdbc/LessonDatabase");
        }
            catch(Exception e) {
            System.out.println("Exception message is " + e.getMessage());
        }
    }

    /**
     * Checks if a username and password combination is valid. 
     * @param nameInput
     * @param passwordInput
     * @return int clientid if the combination does exists in the database, otherwise returns -1.
     */
    public int isValid(String nameInput, String passwordInput) {
        int clientID = -1;

        try {
            
            Connection connection = dataSource.getConnection();
            if (connection != null) {
                preparedStatement = connection.prepareStatement("SELECT clientid FROM clients WHERE username = ? AND password = ?");
                
                preparedStatement.setString(1, nameInput);
                preparedStatement.setString(2, passwordInput);
                
                resultSet = preparedStatement.executeQuery();
                
                while(resultSet.next()){
                    clientID = resultSet.getInt("clientid");
                }
                connection.close();
            }
            else {
                return -1;
            }
            
            connection.close();
        } catch(SQLException e) {
            System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
            return -1;
        }
        
        return clientID;
    }
    
    /**
     * Adds a new user to the clients table in the database.
     * @param nameInput
     * @param passwordInput 
     */
    public void addUser(String nameInput, String passwordInput) {
         try {
            Connection connection = dataSource.getConnection();

            if (connection != null) {
                preparedStatement = connection.prepareStatement("INSERT INTO clients (username, password) VALUES (?, ?)");
                preparedStatement.setString(1, nameInput);
                preparedStatement.setString(2, passwordInput);
                preparedStatement.executeUpdate();
                connection.close();
            }
         }
         catch(SQLException e) {
            System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
         }
    }
    
    /**
     * Checks if a username already exists or not to determine if it is valid.
     * @param username
     * @return true if username doesn't exist, false if it does.
     */
    public boolean isValidUsername(String username){
        boolean isValid = false;
        
         try {
            Connection connection = dataSource.getConnection();

            if (connection != null) {
                preparedStatement = connection.prepareStatement("SELECT COUNT(1) FROM clients WHERE username = ?");
                preparedStatement.setString(1, username);
                
                resultSet = preparedStatement.executeQuery();
                
                int count = 1; 
                
                if(resultSet.next()){
                    count = resultSet.getInt(1);
                }
                
                connection.close();
                
                if(count == 0){
                    isValid = true;
                }else{
                    isValid = false;
                }
            }
         }
         catch(SQLException e) {
            System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
         }
         
         return isValid;
    }
}
