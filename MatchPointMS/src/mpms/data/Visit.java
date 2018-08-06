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
public class Visit
{
    private int id = -1;    
    private int playerId = -1;
    private Date date = null;
    
    public static Visit makeVisit(Player player)
    {
        Visit visit = new Visit();
        visit.playerId = player.getId();
        visit.date = Global.getTodayDate();
        return visit;
    }      
    
    public static Visit fromDatabase(int visitId) throws SQLException
    {
        // Extract Visit data from the database:
        ResultSet results = MpmsMain.Database.extractVisit(visitId);
        results.first();
        
        // Create a new Visit object with the extracted data:
        Visit visit = new Visit();
        visit.id = results.getInt("id");
        visit.playerId = results.getInt("player_id");
        visit.date = results.getTimestamp("date");
        
        return visit;
    }
    
    public static List<Visit> getVisitsFromResults(ResultSet results)
    {
        try {
            ArrayList<Visit> visits = new ArrayList<>();
            if (results == null) return visits;
            results.first();
            
            if (results.isFirst())   
                while (!results.isAfterLast())
                {
                    Visit visit = new Visit();
                    visit.id = results.getInt("id");
                    visit.playerId = results.getInt("player_id");
                    visit.date = results.getTimestamp("date");
                    visits.add(visit);
                    results.next();                
                }
            return visits;
            
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
