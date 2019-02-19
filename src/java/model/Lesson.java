package model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Bean to represent the Lesson.
 * @author Tarek Foyz Ahmed
 */
public class Lesson {

    protected String description;
    protected String startTime;
    protected String date;
    protected String endTime;
    protected int level;
    
    protected String startDateTime;
    protected String endDateTime;
    
    protected String id;
    
    // There is no no-arguments constructor, so at the moment this class can't be instantiated directly from JSPX using 'useBean'
    public Lesson(String description, Timestamp startDateTime, Timestamp endDateTime, int level, String id) {
        this.description = description;
        this.level = level;
        this.id = id;
        
        // Use the Calendar class to convert between a Date object and formatted strings.
        Calendar c = Calendar.getInstance();
        c.setTime(startDateTime);
        // Get the details from this Date object
        SimpleDateFormat fulldateformatter = new SimpleDateFormat("yyyy, MM, dd, kk, mm");
        
        SimpleDateFormat dateformatter = new SimpleDateFormat("E, dd MMM, yyyy");
        this.date = dateformatter.format(c.getTime());
        
        
        dateformatter = new SimpleDateFormat("kk:mm");
        this.startTime = dateformatter.format(c.getTime());
        this.startDateTime = fulldateformatter.format(c.getTime());
        
        // Extract the details from the 'endDateTime' Date object
        c.setTime(endDateTime);
        dateformatter = new SimpleDateFormat("kk:mm");
        this.endTime = dateformatter.format(c.getTime());
        this.endDateTime = fulldateformatter.format(c.getTime());
    }
    
    // We can make a copy of a lesson object, for example to store in a session.
    // This is done by populating the parameters of the new lesson using the accessor (getter) methods of the original.
    public Lesson(Lesson lesson) {
        this.description = lesson.getDescription();
        this.id = lesson.getId();
        this.level = lesson.level;
        this.date = lesson.getDate();
        this.startTime = lesson.getStartTime();
        this.endTime = lesson.getEndTime();
    }

    /**
     * Returns the lesson id.
     * @return a String lesson id.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the lesson description.
     * @return a String description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the date.
     * @return a String date.
     */
    public String getDate() {
        return this.date;
    }

    /**
     * Returns the start time.
     * @return a String start time.
     */
    public String getStartTime() {
        return this.startTime;
    }

    /**
     * Returns the end time.
     * @return a String end time.
     */
    public String getEndTime() {
        return this.endTime;
    }
    
        /**
     * Returns the start date time.
     * @return a String start time.
     */
    public String getStartDateTime() {
        return this.startDateTime;
    }

    /**
     * Returns the end date time.
     * @return a String end time.
     */
    public String getEndDateTime() {
        return this.endDateTime;
    }
    
    /**
     * Returns the level.
     * @return a String level.
     */
    public int getLevel() {
        return this.level;
    }
}
