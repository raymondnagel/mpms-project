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
public class Transact
{
    private int id = -1;    
    private int playerId = -1;
    private Date date = null;
    private double amount = 0.00;
    private String description = null;
    
    public static Transact makePayment(Player player, double amount, String description)
    {
        // Every transaction reduces the balance:
        player.modifyBalance(-amount);        
        player.updateToDatabase();
        
        // Create a new Transact object:
        Transact transact = new Transact();
        transact.playerId = player.getId();
        transact.date = Global.getTodayExactTime();
        transact.amount = -amount;
        transact.description = "[PAYMENT] " + description;        
        MpmsMain.Database.insertTransact(transact);
        return transact;
    }        
    
    public static Transact makeRefund(Player player, double amount, String description)
    {
        // Every transaction reduces the balance:
        player.modifyBalance(-amount);        
        player.updateToDatabase();
        
        // Create a new Transact object:
        Transact transact = new Transact();
        transact.playerId = player.getId();
        transact.date = Global.getTodayExactTime();
        transact.amount = -amount;
        transact.description = "[REFUND] " + description;        
        MpmsMain.Database.insertTransact(transact);
        return transact;
    }
    
    public static void arrangeTransfer(Player fromPlayer, Player toPlayer, double amount, String description)
    {
        // fromPlayer has his balance decreased:
        fromPlayer.modifyBalance(-amount);        
        fromPlayer.updateToDatabase();
        Transact fromTrans = new Transact();
        fromTrans.playerId = fromPlayer.getId();
        fromTrans.date = Global.getTodayExactTime();
        fromTrans.amount = -amount;
        fromTrans.description = "[-TRANSFER] " + description + "to [" + toPlayer.toString() + "]";        
        MpmsMain.Database.insertTransact(fromTrans);
        
        // toPlayer has his balance increased:
        toPlayer.modifyBalance(amount);        
        toPlayer.updateToDatabase();
        Transact toTrans = new Transact();
        toTrans.playerId = toPlayer.getId();
        toTrans.date = Global.getTodayExactTime();
        toTrans.amount = amount;
        toTrans.description = "[+TRANSFER] " + description + "from [" + fromPlayer.toString() + "]";     
        MpmsMain.Database.insertTransact(toTrans);
    }
    
    public static Transact fromDatabase(int transactId) throws SQLException
    {
        // Extract Transact data from the database:
        ResultSet results = MpmsMain.Database.extractTransact(transactId);
        results.first();
        
        // Create a new Transact object with the extracted data:
        Transact transact = new Transact();
        transact.id = results.getInt("id");
        transact.playerId = results.getInt("player_id");
        transact.date = results.getTimestamp("date");
        transact.amount = results.getDouble("amount");
        transact.description = results.getString("description");
        
        return transact;
    }
    
    public static List<Transact> getTransactsFromResults(ResultSet results)
    {
        try {
            ArrayList<Transact> transacts = new ArrayList<>();
            if (results == null) return transacts;
            results.first();
            
            if (results.isFirst())   
                while (!results.isAfterLast())
                {
                    Transact transact = new Transact();
                    transact.id = results.getInt("id");
                    transact.playerId = results.getInt("player_id");
                    transact.date = results.getTimestamp("date");
                    transact.amount = results.getDouble("amount");
                    transact.description = results.getString("description");
                    transacts.add(transact);
                    results.next();                
                }
            return transacts;
            
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

    /**
     * @return the amount
     */
    public double getAmount()
    {
        return amount;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }
}
