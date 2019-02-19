package model;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author Tarek Foyz Ahmed
 */
public class LessonSelection  {
    private HashMap<String, Lesson> chosenLessons;
    private int ownerID;
    
    private DataSource dataSource = null;
    
    private ResultSet resultSet = null;
    private Statement statement = null;

    public LessonSelection(int owner) {
        
        chosenLessons = new HashMap<String, Lesson>();
        this.ownerID = owner;

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
        
        // Connect to the database - this is a pooled connection, so you don't need to close it afterwards
        try {

            Connection connection = dataSource.getConnection();

             try {
                if (connection != null) {
                    statement = connection.createStatement();
                    
                    // Query to find rows in the 'lessons_booked' table which relate to the 'clientid'
                    resultSet = statement.executeQuery("SELECT lessons.lessonid, lessons.description, lessons.startDateTime, lessons.endDateTime, lessons.level FROM lessons INNER JOIN lessons_booked WHERE lessons.lessonid = lessons_booked.lessonid AND lessons_booked.clientid = " + owner);

                    String lessonID = "";
                    String description = "";
                    Timestamp startDateTime = null;
                    Timestamp endDateTime = null;
                    int level = 0;
                    
                    // For each result, create a new lesson object and add to LessonSelection data structure
                    while(resultSet.next()){
                        lessonID = resultSet.getString("lessonid");
                        description = resultSet.getString("description");
                        startDateTime = resultSet.getTimestamp("startDateTime");
                        endDateTime = resultSet.getTimestamp("EndDateTime");
                        level = resultSet.getInt("level");
                        Lesson lesson = new Lesson(description, startDateTime, endDateTime, level, lessonID);
                        addLesson(lesson);
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
     * Returns the set of all the chosen lessons.
     * @return 
     */
    public Set<Entry <String, Lesson>> getItems() {
        return chosenLessons.entrySet();
    }
    
    public String getItemsAsJSON(){
        String json = "";
        Object[] lessonKeys = chosenLessons.keySet().toArray();
        
        for(Object selection: lessonKeys){
            Lesson l = getLesson((String)selection);
            String temp = "{ "
                + "title: " + l.getDescription() + ", "
                + "start: " + l.getStartDateTime() + ", "
                + "end: " + l.getEndDateTime() + " },";
            json += temp;
        }
        return json;
    }

    /**
     * Adds a lesson object to the lesson selections map.
     * @param l 
     */
    public void addLesson(Lesson l) {
        Lesson i = new Lesson(l);
        this.chosenLessons.put(l.getId(), i);
    }
    
    /**
     * Removes the lesson with the identifier @param id from the lesson selection map.
     * @param id 
     */
    public void removeLesson(String id){
        this.chosenLessons.remove(id);
    }

    /**
     * Returns the lesson with the identifier @param id from the lesson selection map.
     * @param id
     * @return 
     */
    public Lesson getLesson(String id){
        return this.chosenLessons.get(id);
    }
    
    /**
     * Returns the size of the lesson selection map.
     * @return 
     */
    public int getNumChosen(){
        return this.chosenLessons.size();
    }

    /**
     * Returns the identifier of the owner of this lesson selection object.
     * @return 
     */
    public int getOwner() {
        return this.ownerID;
    }
    
    /**
     * Updates the selection on the database.
     */
    public void updateBooking() {
        // A tip: here is how you can get the ids of any lessons that are currently selected
        Object[] lessonKeys = chosenLessons.keySet().toArray();
        
        // Connect to the database - this is a pooled connection, so you don't need to close it afterwards
        try {

            Connection connection = dataSource.getConnection();

            try {
                if (connection != null) {
                    statement = connection.createStatement();
                    
                    statement.executeUpdate("DELETE FROM lessons_booked WHERE clientid = " + ownerID);

                    for(Object selection : lessonKeys){
                        statement.executeUpdate("INSERT INTO lessons_booked VALUES (" + ownerID + ", \"" + (String)selection + "\")");
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
}
