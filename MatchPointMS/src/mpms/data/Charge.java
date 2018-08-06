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
public class Charge
{
    private int id = -1;    
    private int playerId = -1;
    private Date date = null;
    private double amount = 0.00;
    private String description = null;

    public static Charge makeCharge(Player player, double amount, String description)
    {
        // If there is a sponsor for this player, charge the sponsor instead:
        Player sponsor = player.getSponsor();
        if (sponsor != null)
        {
            return makeCharge(sponsor, amount, description + " (for " + player.toString() + ")");
        }        
        
        // Otherwise, charge this player:
        player.modifyBalance(amount);        
        player.updateToDatabase();
        
        // Create the Charge object:
        Charge charge = new Charge();
        charge.playerId = player.getId();
        charge.date = Global.getTodayExactTime();
        charge.amount = amount;
        charge.description = "[CHARGE] " + description;        
        MpmsMain.Database.insertCharge(charge);
        return charge;
    }        
    
    public static Charge fromDatabase(int chargeId) throws SQLException
    {
        // Extract Charge data from the database:
        ResultSet results = MpmsMain.Database.extractCharge(chargeId);
        results.first();
        
        // Create a new Charge object with the extracted data:
        Charge charge = new Charge();
        charge.id = results.getInt("id");
        charge.playerId = results.getInt("player_id");
        charge.date = results.getTimestamp("date");
        charge.amount = results.getDouble("amount");
        charge.description = results.getString("description");
        
        return charge;
    }
    
    public static List<Charge> getChargesFromResults(ResultSet results)
    {
        try {
            ArrayList<Charge> charges = new ArrayList<>();
            if (results == null) return charges;
            results.first();
            
            if (results.isFirst())            
                while (!results.isAfterLast())
                {
                    Charge charge = new Charge();
                    charge.id = results.getInt("id");
                    charge.playerId = results.getInt("player_id");
                    charge.date = results.getTimestamp("date");
                    charge.amount = results.getDouble("amount");
                    charge.description = results.getString("description");
                    charges.add(charge);
                    results.next();                
                }
            return charges;
            
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
