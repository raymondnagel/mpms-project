/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpms.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mpms.Global;
import mpms.MpmsMain;

/**
 *
 * @author rnagel
 */
public class Lesson
{
    private int id = -1;    
    private int playerId = -1;
    private Date date = null;
    
    public static Lesson makeLesson(Player player)
    {
        Lesson lesson = new Lesson();
        lesson.playerId = player.getId();
        lesson.date = Global.getTodayDate();
        return lesson;
    }        
    
    public static Lesson fromDatabase(int lessonId) throws SQLException
    {
        // Extract Lesson data from the database:
        ResultSet results = MpmsMain.Database.extractLesson(lessonId);
        results.first();
        
        // Create a new Lesson object with the extracted data:
        Lesson lesson = new Lesson();
        lesson.id = results.getInt("id");
        lesson.playerId = results.getInt("player_id");
        lesson.date = results.getTimestamp("date");
        
        return lesson;
    }
    
    public static List<Lesson> getLessonsFromResults(ResultSet results)
    {
        try {
            ArrayList<Lesson> lessons = new ArrayList<>();
            if (results == null) return lessons;
            results.first();
            
            if (results.isFirst())   
                while (!results.isAfterLast())
                {
                    Lesson lesson = new Lesson();
                    lesson.id = results.getInt("id");
                    lesson.playerId = results.getInt("player_id");
                    lesson.date = results.getTimestamp("date");
                    lessons.add(lesson);
                    results.next();                
                }
            return lessons;
            
        } catch (SQLException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * @return the id
     */
    public int getId()
    {
        return id;
    }

    /**
     * @return the playerId
     */
    public int getPlayerId()
    {
        return playerId;
    }

    /**
     * @return the date
     */
    public Date getDate()
    {
        return date;
    }
}
