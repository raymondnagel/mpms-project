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
public class TTMatch
{
    private int id = -1;    
    private int winnerId = -1;
    private int loserId = -1;
    private int pointsExchanged = 0;
    private int winnerNewRating = 0;
    private int loserNewRating = 0;
    private Date date = null;
    
    public static TTMatch makeMatch(Player winner, Player loser, int pointsEx)
    {
        TTMatch match = new TTMatch();
        match.winnerId = winner.getId();
        match.loserId = loser.getId();
        match.date = Global.getTodayDate();
        match.pointsExchanged = pointsEx;
        match.winnerNewRating = winner.getClubRating();
        match.loserNewRating = loser.getClubRating();
        return match;
    }      
    
    public static TTMatch fromDatabase(int matchId) throws SQLException
    {
        // Extract TTMatch data from the database:
        ResultSet results = MpmsMain.Database.extractTTMatch(matchId);
        results.first();
        
        // Create a new TTMatch object with the extracted data:
        TTMatch match = new TTMatch();
        match.id = results.getInt("id");
        match.winnerId = results.getInt("winner_id");
        match.loserId = results.getInt("loser_id");
        match.date = results.getTimestamp("date");
        match.pointsExchanged = results.getInt("points_ex");
        match.winnerNewRating = results.getInt("winner_new_rating");
        match.loserNewRating = results.getInt("loser_new_rating");
        
        return match;
    }
    
    public static List<TTMatch> getMatchesFromResults(ResultSet results)
    {
        try {
            ArrayList<TTMatch> matches = new ArrayList<>();
            if (results == null) return matches;
            results.first();
            
            if (results.isFirst())   
                while (!results.isAfterLast())
                {
                    TTMatch match = new TTMatch();
                    match.id = results.getInt("id");
                    match.winnerId = results.getInt("winner_id");
                    match.loserId = results.getInt("loser_id");
                    match.date = results.getTimestamp("date");
                    match.pointsExchanged = results.getInt("points_ex");
                    match.winnerNewRating = results.getInt("winner_new_rating");
                    match.loserNewRating = results.getInt("loser_new_rating");
                    results.next();                
                }
            return matches;
            
        } catch (SQLException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the winnerId
     */
    public int getWinnerId() {
        return winnerId;
    }

    /**
     * @return the loserId
     */
    public int getLoserId() {
        return loserId;
    }

    /**
     * @return the pointsExchanged
     */
    public int getPointsExchanged() {
        return pointsExchanged;
    }

    /**
     * @return the winnerNewRating
     */
    public int getWinnerNewRating() {
        return winnerNewRating;
    }

    /**
     * @return the loserNewRating
     */
    public int getLoserNewRating() {
        return loserNewRating;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

}
