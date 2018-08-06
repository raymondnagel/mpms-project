/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mpms.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mpms.Global;
import static mpms.Global.DATE_FORMAT;
import mpms.MpmsMain;
import mpms.data.Player.PLAYER_LIST_MODE;
import mpms.data.Player.SEX;
import org.jdom2.Element;

/**
 *
 * @author rnagel
 */
public class Player {
    public enum PLAYER_LIST_MODE {ById, ByLastName}
    public enum SEX {Male, Female}
    
    private int id = 0;    
    private Date since = null;
    private String password = null;
    private String firstName = null;
    private String lastName = null;    
    private SEX sex = SEX.Male;    
    private Date birthDate = null;    
    private Integer clubRating = null;   
    private Double ratingAcc = 0.0;
    private String phone = null;
    private String email = null;
    private String address = null;
    private String city = null;
    private String state = null;
    private String zip = null;    
    private Integer sponsorId = null;
    private double balance = 0.00;
    private int visits = 0;
    private String position = null;

    public static Player createNew()
    {
        Player player = new Player();
        
        player.since = Global.getTodayDate();        
        
        return player;
    }
   
    public static Player fromDatabase(int playerId) throws SQLException
    {
        // Extract Player data from the database:
        ResultSet results = MpmsMain.Database.extractPlayer(playerId);
        results.first();
        
        // Create a new Player object with the extracted data:
        Player player = new Player();
        player.id = results.getInt("id");
        player.since = results.getTimestamp("since");
        player.password = results.getString("password");
        player.firstName = results.getString("first_name");
        player.lastName = results.getString("last_name");
        player.sex = results.getString("sex").equalsIgnoreCase("F") ? SEX.Female : SEX.Male;
        player.birthDate = results.getTimestamp("birth_date");
        player.clubRating = results.getObject("club_rating", Integer.class);       
        player.ratingAcc = results.getDouble("rating_acc");     
        player.phone = results.getString("phone");
        player.email = results.getString("email");
        player.address = results.getString("address");
        player.city = results.getString("city");
        player.state = results.getString("state");
        player.zip = results.getString("zip");
        player.sponsorId = results.getObject("sponsor_id", Integer.class);
        player.balance = results.getDouble("balance");
        player.visits = results.getInt("visits");
        player.position = results.getString("position");
        
        return player;
    }
    
    public static List<Player> allFromDatabase(PLAYER_LIST_MODE mode)
    {
        try {
            
            ArrayList<Player> players = new ArrayList<>();
            ResultSet results;
            switch (mode)
            {
                case ByLastName:
                    results = MpmsMain.Database.extractAllPlayersByLastName();
                    break;
                default:
                    results = MpmsMain.Database.extractAllPlayersById();                    
            }
            
            results.first();
            while (!results.isAfterLast())
            {
                Player player = new Player();
                player.id = results.getInt("id");
                player.since = results.getTimestamp("since");
                player.password = results.getString("password");
                player.firstName = results.getString("first_name");
                player.lastName = results.getString("last_name");
                player.sex = results.getString("sex").equalsIgnoreCase("F") ? SEX.Female : SEX.Male;
                player.birthDate = results.getTimestamp("birth_date");
                player.clubRating = results.getObject("club_rating", Integer.class);  
                player.ratingAcc = results.getDouble("rating_acc");
                player.phone = results.getString("phone");
                player.email = results.getString("email");
                player.address = results.getString("address");
                player.city = results.getString("city");
                player.state = results.getString("state");
                player.zip = results.getString("zip");
                player.sponsorId = results.getObject("sponsor_id", Integer.class);
                player.balance = results.getDouble("balance");
                player.visits = results.getInt("visits");
                player.position = results.getString("position");
                players.add(player);
                results.next();                
            }
            return players;
            
        } catch (SQLException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static List<Player> allTodayFromDatabase()
    {
        try {
            
            ArrayList<Player> players = new ArrayList<>();
            ResultSet results;
            results = MpmsMain.Database.extractAllPlayersForDate(Global.getTodayDate());
            
            results.first();
            while (!results.isAfterLast())
            {
                Player player = new Player();
                player.id = results.getInt("id");
                player.since = results.getTimestamp("since");
                player.password = results.getString("password");
                player.firstName = results.getString("first_name");
                player.lastName = results.getString("last_name");
                player.sex = results.getString("sex").equalsIgnoreCase("F") ? SEX.Female : SEX.Male;
                player.birthDate = results.getTimestamp("birth_date");
                player.clubRating = results.getObject("club_rating", Integer.class);  
                player.ratingAcc = results.getDouble("rating_acc");
                player.phone = results.getString("phone");
                player.email = results.getString("email");
                player.address = results.getString("address");
                player.city = results.getString("city");
                player.state = results.getString("state");
                player.zip = results.getString("zip");
                player.sponsorId = results.getObject("sponsor_id", Integer.class);
                player.balance = results.getDouble("balance");
                player.visits = results.getInt("visits");
                player.position = results.getString("position");
                players.add(player);
                results.next();                
            }
            return players;
            
        } catch (SQLException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public int getId()
    {
        return this.id;
    }
    
    public Date getSince()
    {
        return this.since;
    }
    
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }    
    public void setName(String firstName, String lastName)
    {
        setFirstName(firstName);
        setLastName(lastName);
    }
        
    public String getFirstName()
    {
        return this.firstName;
    }
    public String getLastName()
    {
        return this.lastName;
    }
    public String getName()
    {
        return getFirstName() + " " + getLastName();
    }
    public String getLastFirst()
    {
        return getLastName() + ", " + getFirstName();
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    public String getPassword()
    {
        return this.password;
    }
    
    public void setAddress(String address)
    {
        this.address = address;
    }
    public String getAddress()
    {
        return this.address;
    }
    
    public void setCity(String city)
    {
        this.city = city;
    }
    public String getCity()
    {
        return this.city;
    }
    
    public void setState(String state)
    {
        this.state = state;
    }
    public String getState()
    {
        return this.state;
    }
    
    public void setZip(String zip)
    {
        this.zip = zip;
    }
    public String getZip()
    {
        return this.zip;
    }
    
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    public String getPhone()
    {
        return this.phone;
    }
    
    public void setEmail(String email)
    {
        this.email = email;
    }
    public String getEmail()
    {
        return this.email;
    }
    
    public void setBirthDate(String month, String date, String year)
    {
        try {
            this.birthDate = DATE_FORMAT.parse(month + "/" + date + "/" + year);
        } catch (ParseException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    public Date getBirthDate()
    {
        return this.birthDate;
    }
    public int getAge()
    {
        Calendar cal = new GregorianCalendar();
        int age = cal.get(Calendar.YEAR);
        cal.setTime(birthDate);
        age -= cal.get(Calendar.YEAR);
        return age;
    }
    
    public void setSex(SEX sex)
    {
        this.sex = sex;
    }
    public SEX getSex()
    {
        return this.sex;
    }
    
    public void setSponsorId(Integer id)
    {
        this.sponsorId = id;
    }
    public Integer getSponsorId()
    {
        return sponsorId;
    }
    public Player getSponsor()
    {        
        try {
            if (sponsorId != null)
                return Player.fromDatabase(sponsorId);
            else
                return null;
        } catch (SQLException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    
    public void setBalance(double balance)
    {        
        this.balance = Global.roundDollars(balance);
    }
    public void modifyBalance(double amount)
    {                
        if (amount == 0)
            return;
        this.balance = Global.roundDollars(this.balance + amount);        
    }    
    public double getBalance()
    {
        return this.balance;
    }    
    
    public void setClubRating(Integer rating)
    {
        this.clubRating = rating;        
    }
    public Integer getClubRating()
    {
        return this.clubRating;
    }
    
    public void setRatingAcc(Double ratingAcc)
    {
        this.ratingAcc = ratingAcc;        
    }
    public Double getRatingAcc()
    {
        return this.ratingAcc;
    }
    
    public void setPosition(String special)
    {
        this.position = special;       
    }
    public String getPosition()
    {
        return this.position;
    }
    
    public void setVisits(int visits)
    {
        this.visits = visits;
    }
    public int getVisits()
    {
        return this.visits;
    }
    public void addVisits(int visits)
    {
        this.visits += visits;
    }
    public void spendVisit()
    {
        this.visits--;
    }
       
    
    public Object[] getPlayerRowData()
    {
        Object[] data = new Object[12];
        data[0] = this.id;
        data[1] = this.lastName;
        data[2] = this.firstName;
        data[3] = this.clubRating;
        data[4] = this.ratingAcc;
        data[5] = Global.DATE_FORMAT.format(birthDate);
        data[6] = this.sex;
        data[7] = this.phone;
        data[8] = this.email;
        data[9] = this.position;
        data[10] = this.getSponsor();
        data[11] = Global.roundDollars(this.balance);
        return data;
    }
    
    public Object[] getCheckInRowData()
    {            
        Object[] data = new Object[8];
        data[0] = this.id;
        data[1] = this.lastName;
        data[2] = this.firstName;
        //data[3] = this.rating;        
        data[4] = this.sex;
        data[5] = this.getAge();
        data[6] = Global.DOLLARS.format(Global.roundDollars(this.balance));        
        data[7] = this.visits;
        return data;
    }
        
    @Override
    public String toString()
    {
        return "[" + getId() + "] " + getLastFirst();
    }
    
    public List<Charge> getChargeRecords()
    {
        ResultSet results = MpmsMain.Database.extractAllChargesForPlayer(id);
        return Charge.getChargesFromResults(results);
    }
    
    public List<Transact> getTransactRecords()
    {
        ResultSet results = MpmsMain.Database.extractAllTransactsForPlayer(id);
        return Transact.getTransactsFromResults(results);
    }
    
    public List<Visit> getVisitRecords()
    {
        ResultSet results = MpmsMain.Database.extractAllVisitsForPlayer(id);
        return Visit.getVisitsFromResults(results);
    }
    
    public List<Lesson> getLessonRecords()
    {
        ResultSet results = MpmsMain.Database.extractAllLessonsForPlayer(id);
        return Lesson.getLessonsFromResults(results);
    }
    
    public Boolean checkedInToday()
    {
        return MpmsMain.Database.checkVisitedToday(this);
    }
    
    public Boolean hadLessonToday()
    {
        return MpmsMain.Database.checkHadLessonToday(this);
    }
    
    public Integer getLessonsThisWeek()
    {
        return MpmsMain.Database.checkWeeklyLessonCount(this);
    }
    
    public void updateToDatabase()
    {
        MpmsMain.Database.updatePlayer(this);
    }
}
