package model;

import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * Bean to represents the lesson timetable.
 * @author Tarek Foyz Ahmed
 */
public class LessonTimetable {

  private Connection connection = null;
  
  private ResultSet resultSet = null;
  private Statement statement = null;
  
  private Map<String,Lesson> lessons = null;
  
  private DataSource dataSource = null;
    
    public LessonTimetable() {
        // You don't need to make any changes to the try/catch code below
        try {
            // Obtain our environment naming context
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            // Look up our data source
            dataSource = (DataSource)envCtx.lookup("jdbc/LessonDatabase");
        }
            catch(Exception e) {
            System.out.println("Exception message is " + e.getMessage());
        }
        
        try {
            // Connect to the database - you can use this connection to 
            // create and prepare statements, and you don't need to worry about closing it
            connection = dataSource.getConnection();
        
             try {
                if (connection != null) {
                    lessons = new HashMap<String, Lesson>();
                    
                    statement = connection.createStatement();                   
                   
                    String getAllLessonsQuery = "SELECT * FROM lessons";
                    statement.executeQuery(getAllLessonsQuery);
                    
                    resultSet = statement.getResultSet();
                    
                    String lessonID = "";
                    String description = "";
                    Timestamp startDateTime = null;
                    Timestamp endDateTime = null;
                    int level = 0;
         
                    while(resultSet.next()){
                        lessonID = resultSet.getString("lessonid");
                        description = resultSet.getString("description");
                        startDateTime = resultSet.getTimestamp("startDateTime");
                        endDateTime = resultSet.getTimestamp("EndDateTime");
                        level = resultSet.getInt("level");
                        
                        Lesson lesson = new Lesson(description, startDateTime, endDateTime, level, lessonID);
                        lessons.put(lessonID, lesson);
                    }
                    connection.close();
                }
            }catch(SQLException e) {
                System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
            }
             
          }catch(Exception e){
             System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
          }
    }
    
    /**
     * Returns the Lesson with the id @param itemID.
     * @param itemID - the id of the lesson to return.
     * @return 
     */
    public Lesson getLesson(String itemID) {
        return (Lesson)this.lessons.get(itemID);
    }

    /**
     * Return a Map of all lessons.
     * @return 
     */
    public Map getLessons() {
        return this.lessons;
    }
}
