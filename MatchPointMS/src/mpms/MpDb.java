/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpms;

import mpms.data.Player;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mpms.data.Charge;
import mpms.data.Lesson;
import mpms.data.TTMatch;
import mpms.data.Transact;
import mpms.data.Visit;
import org.apache.derby.tools.ij;


/**
 *
 * @author rnagel
 */
public class MpDb
{   
    // The abstract DatabaseExecution class provides some control over SQLExceptions that happen
    // during any database operation. Override operation() method to implement.
    // Run with attemptStatement() to return true if the operation completed without an Exception.
    // Run with attemptQuery() to return a ResultSet from the operation.
    private abstract class DatabaseExecution
    {
        protected abstract ResultSet operation() throws SQLException, Exception;

        protected boolean sqlExceptionIgnored(SQLException sqlEx)
        {
            return sqlEx.getMessage().contains("The statement was aborted because it would have caused a duplicate key value in a unique or primary key constraint or unique index");            
        }
        
        public boolean attemptStatement()
        {            
            try
            {
                this.operation();
                return true;
            }
            catch (SQLException ex)
            {
                if (!sqlExceptionIgnored(ex))
                {
                    System.err.println("SQL Exception: " + ex);
                }
                return false;
            }
            catch (Exception ex)
            {
                System.err.println(ex);
                return false;
            }
        }
        
        public ResultSet attemptQuery()
        {            
            try
            {
                return this.operation();                
            }
            catch (SQLException ex)
            {
                if (!sqlExceptionIgnored(ex))
                {
                    //HawkeyeFx.appError("An SQL Exception occurred", ex, true, null);
                }
                return null;
            }
            catch (Exception ex)
            {
                //HawkeyeFx.appError("An unexpected error occurred", ex, true, null);
                Logger.getLogger(MpDb.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    // The MpDb class performs operations on a single private Connection:
    private Connection connection = null;
    private String connectionString = null;
        
    // Updates:
    private PreparedStatement updatePlayer;
    
    // Insertions:
    private PreparedStatement insertPlayer;
    private PreparedStatement insertLesson;
    private PreparedStatement insertVisit;
    private PreparedStatement insertTTMatch;
    private PreparedStatement insertCharge;
    private PreparedStatement insertTransact;
    
    // Extractions:
    private PreparedStatement extractVisitorsForDate = null;
    private PreparedStatement extractRoster = null;
    
    private PreparedStatement extractAllPlayersById = null;
    private PreparedStatement extractAllPlayersByLastName = null;
    private PreparedStatement extractAllPlayersByDate = null;
    private PreparedStatement extractPlayer = null;
            
    private PreparedStatement extractAllCharges = null;
    private PreparedStatement extractAllTransacts = null;
    private PreparedStatement extractAllVisits = null;
    private PreparedStatement extractAllLessons = null;        
    
    private PreparedStatement extractAllChargesForPlayer = null;
    private PreparedStatement extractAllTransactsForPlayer = null;
    private PreparedStatement extractAllVisitsForPlayer = null;
    private PreparedStatement extractAllLessonsForPlayer = null;    
    
    private PreparedStatement extractCharge = null;
    private PreparedStatement extractTransact = null;
    private PreparedStatement extractVisit = null;
    private PreparedStatement extractTTMatch = null;
    private PreparedStatement extractLesson = null;    
    
    private PreparedStatement extractPlayerTransactHistory = null;
    private PreparedStatement extractLedger = null;
    
    // Info/Boolean Checks:
    private PreparedStatement checkVisitedToday = null;
    private PreparedStatement checkHadLessonToday = null;
    private PreparedStatement checkWeeklyLessonCount = null;
    
    
    // ==================================================================================================
    // METHODS FOR STANDARD DATABASE OPERATION
    // ==================================================================================================
    
    // Creates the database using the open connection.
    // Technically, the database always exists on disk; but this method
    // creates all necessary tables and initializes some entries.
    // If reset=true, all existing tables are dropped before re-creating them,
    // ensuring that the database is reset to a default "factory" condition.
    public boolean createDatabase(boolean reset)
    {
        File scriptFile;

        if (reset)
        {
            scriptFile = new File("res/sql/erase_mpdb.sql");
            List<String> results = runScriptFilePerStatement(scriptFile);
            for (int r = 0; r < results.size(); r++)
            {
                System.out.println(results.get(r));
            }
        }

        scriptFile = new File("res/sql/create_mpdb.sql");
        List<String> results = runScriptFilePerStatement(scriptFile);
        for (int r = 0; r < results.size(); r++)
        {
            System.out.println(results.get(r));
        }

        return true;
    }
        
    // Creates PrepardStatements to use for frequent queries/updates.
    public boolean prepareStatements()
    {                  
        try {
            // Update:
            updatePlayer = connection.prepareStatement("UPDATE Player SET "
                    + "since=?, password=?, first_name=?, last_name=?, sex=?, birth_date=?, club_rating=?, rating_acc=?, phone=?, email=?, address=?, city=?, state=?, zip=?, sponsor_id=?, balance=?, visits=?, position=? "
                    + "WHERE id=?");
            
            // Insertion:
            insertPlayer = connection.prepareStatement("INSERT INTO Player "
                    + "(since, password, first_name, last_name, sex, birth_date, club_rating, rating_acc, phone, email, address, city, state, zip, sponsor_id, balance, visits, position) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            insertLesson = connection.prepareStatement("INSERT INTO Lesson "
                    + "(player_id, date) "
                    + "VALUES (?, ?)");
            insertVisit = connection.prepareStatement("INSERT INTO Visit "
                    + "(player_id, date) "
                    + "VALUES (?, ?)");
            insertTTMatch = connection.prepareStatement("INSERT INTO TTMatch "
                    + "(winner_id, loser_id, points_ex, date, winner_new_rating, loser_new_rating) "
                    + "VALUES (?, ?, ?, ?, ?, ?)");
            insertCharge = connection.prepareStatement("INSERT INTO Charge "
                    + "(player_id, date, amount, description) "
                    + "VALUES (?, ?, ?, ?)");
            insertTransact = connection.prepareStatement("INSERT INTO Transact "
                    + "(player_id, date, amount, description) "
                    + "VALUES (?, ?, ?, ?)");
            
            // Extraction:
            extractVisitorsForDate = connection.prepareStatement("SELECT " +
                                                        "p.id, p.first_name, p.last_name, p.sex, p.position, p.since " +
                                                        "FROM Player AS p JOIN Visit AS v " +
                                                        "ON p.id=v.player_id " +
                                                        "WHERE v.date=? " +
                                                        "ORDER BY p.id", 
                                                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);                        
            extractRoster = connection.prepareStatement("SELECT " +
                                                        "p.id, p.last_name, p.first_name, p.sex, p.birth_date, p.club_rating, p.rating_acc, p.phone, " + 
                                                        "p.email, p.sponsor_id, p.since, p.balance, p.visits, p.position " +
                                                        "FROM Player AS p " +
                                                        "ORDER BY p.last_name", 
                                                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            extractAllPlayersById = connection.prepareStatement("SELECT " +
                                                        "p.id, p.since, p.password, p.first_name, p.last_name, p.sex, p.birth_date, p.club_rating, p.rating_acc, p.phone, " + 
                                                        "p.email, p.address, p.city, p.state, p.zip, p.sponsor_id, p.balance, p.visits, p.position " +
                                                        "FROM Player AS p " +
                                                        "ORDER BY p.id", 
                                                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            extractAllPlayersByLastName = connection.prepareStatement("SELECT " +
                                                        "p.id, p.since, p.password, p.first_name, p.last_name, p.sex, p.birth_date, p.club_rating, p.rating_acc, p.phone, " + 
                                                        "p.email, p.address, p.city, p.state, p.zip, p.sponsor_id, p.balance, p.visits, p.position " +
                                                        "FROM Player AS p " +
                                                        "ORDER BY p.last_name, p.first_name", 
                                                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            extractAllPlayersByDate = connection.prepareStatement("SELECT " +
                                                        "p.id, p.since, p.password, p.first_name, p.last_name, p.sex, p.birth_date, p.club_rating, p.rating_acc, p.phone, " + 
                                                        "p.email, p.address, p.city, p.state, p.zip, p.sponsor_id, p.balance, p.visits, p.position " +
                                                        "FROM Player AS p JOIN Visit AS v " +
                                                        "ON p.id=v.player_id " +
                                                        "WHERE v.date=? " +
                                                        "ORDER BY p.last_name", 
                                                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            extractPlayer = connection.prepareStatement("SELECT " +
                                                        "p.id, p.since, p.password, p.first_name, p.last_name, p.sex, p.birth_date, p.club_rating, p.rating_acc, p.phone, " + 
                                                        "p.email, p.address, p.city, p.state, p.zip, p.sponsor_id, p.balance, p.visits, p.position " +
                                                        "FROM Player AS p " +
                                                        "WHERE p.id=?", 
                                                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            extractAllLessons = connection.prepareStatement("SELECT " +
                                                        "l.id, l.player_id, l.date " +
                                                        "FROM Lesson AS l " +
                                                        "ORDER BY l.date", 
                                                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            extractAllVisits = connection.prepareStatement("SELECT " +
                                                        "v.id, v.player_id, v.date " +
                                                        "FROM Visit AS v " +
                                                        "ORDER BY v.date",
                                                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            extractAllCharges = connection.prepareStatement("SELECT " +
                                                        "c.id, c.player_id, c.date, c.amount, c.description " +
                                                        "FROM Charge AS c " +
                                                        "ORDER BY c.date",
                                                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            extractAllTransacts = connection.prepareStatement("SELECT " +
                                                        "t.id, t.player_id, t.date, t.amount, t.description " +
                                                        "FROM Transact AS t " +
                                                        "ORDER BY t.date", 
                                                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            extractAllLessonsForPlayer = connection.prepareStatement("SELECT " +
                                                        "l.id, l.player_id, l.date " +
                                                        "FROM Lesson AS l " +
                                                        "WHERE l.player_id=? " +
                                                        "ORDER BY l.date",
                                                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            extractAllVisitsForPlayer = connection.prepareStatement("SELECT " +
                                                        "v.id, v.player_id, v.date " +
                                                        "FROM Visit AS v " +
                                                        "WHERE v.player_id=? " +
                                                        "ORDER BY v.date",
                                                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            extractAllChargesForPlayer = connection.prepareStatement("SELECT " +
                                                        "c.id, c.player_id, c.date, c.amount, c.description " +
                                                        "FROM Charge AS c " +
                                                        "WHERE c.player_id=? " +
                                                        "ORDER BY c.date",
                                                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            extractAllTransactsForPlayer = connection.prepareStatement("SELECT " +
                                                        "t.id, t.player_id, t.date, t.amount, t.description " +
                                                        "FROM Transact AS t " +
                                                        "WHERE t.player_id=? " +
                                                        "ORDER BY t.date", 
                                                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            extractLesson = connection.prepareStatement("SELECT " +
                                                        "l.id, l.player_id, l.date " +
                                                        "FROM Lesson AS l " +
                                                        "WHERE l.id=?", 
                                                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            extractVisit = connection.prepareStatement("SELECT " +
                                                        "v.id, v.player_id, v.date " +
                                                        "FROM Visit AS v " +
                                                        "WHERE v.id=?", 
                                                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            extractTTMatch = connection.prepareStatement("SELECT " +
                                                        "m.id, m.winner_id, m.loser_id, m.date, m.points_ex, m.winner_new_rating, m.loser_new_rating " +
                                                        "FROM TTMatch AS m " +
                                                        "WHERE m.id=?", 
                                                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            extractCharge = connection.prepareStatement("SELECT " +
                                                        "c.id, c.player_id, c.date, c.amount, c.description " +
                                                        "FROM Charge AS c " +
                                                        "WHERE c.id=?", 
                                                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            extractTransact = connection.prepareStatement("SELECT " +
                                                        "t.id, t.player_id, t.date, t.amount, t.description " +
                                                        "FROM Transact AS t " +
                                                        "WHERE t.id=?", 
                                                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            extractPlayerTransactHistory = connection.prepareStatement("SELECT date, amount, description FROM Charge WHERE player_id=? UNION " +
                                                                       "SELECT date, amount, description FROM Transact WHERE player_id=? " +
                                                                       "ORDER BY date, description",
                                                                       ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            extractLedger = connection.prepareStatement("SELECT * FROM Transact WHERE YEAR(date)=? " +
                                                        "ORDER BY date, description",
                                                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            // Info/Boolean Checks:
            checkVisitedToday = connection.prepareStatement("SELECT COUNT (*) as rowcount " +
                                                            "FROM Visit AS v " +
                                                            "WHERE v.player_id=? AND v.date=?",
                                                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            checkHadLessonToday = connection.prepareStatement("SELECT COUNT (*) as rowcount " +
                                                              "FROM Lesson AS l " +
                                                              "WHERE l.player_id=? AND l.date=?",
                                                              ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            checkWeeklyLessonCount = connection.prepareStatement("SELECT COUNT (*) as rowcount " +
                                                                 "FROM Lesson AS l " +
                                                                 "WHERE l.player_id=? AND l.date>?",
                                                                 ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(MpDb.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    // Standard database operation methods:
    // Open connection for this database instance.
    public boolean openConnection()
    {
        try
        {
            String driver = "org.apache.derby.jdbc.EmbeddedDriver";
            Class.forName(driver).newInstance();
            connectionString = "jdbc:derby:res/mpdb;create=true";
            connection = DriverManager.getConnection(connectionString, "matchpoint", "w41dn3r");
            //HawkeyeFx.appMessage("Connected to MpDb (" + mode.name() + ") successfully!", null, true, null);            
            return true;
        }
        catch (Exception ex)
        {
            Logger.getLogger(MpDb.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    // Close connection for this database instance.
    public boolean closeConnection()
    {
        try
        {
            connection.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(MpDb.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    // Get the Connection object used by this instance:
    public Connection getConnection()
    {
        return this.connection;
    }
    
    // Get the Connection string that was used to connect:
    public String getConnectionString()
    {
        return this.connectionString;
    }
    
    // Execute a query and return the results:    
    public ResultSet executeQuery(String sqlQuery) throws SQLException
    {
        while (sqlQuery.endsWith(";"))
        {
            sqlQuery = sqlQuery.substring(0, sqlQuery.length()-1);
        }
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet results = statement.executeQuery(sqlQuery);
        results.first();
        return results;
    }

    
    // ==================================================================================================
    // METHODS FOR UPDATING DATA
    // ==================================================================================================
    
    // Update an existing Player in the database:
    public void updatePlayer(Player player)
    {
        // Attempt Player table update:
        new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {
                // Insert Player record:                
                // 1      2         3           4          5    6           7       8      9      10     11       12    13     14   15          16       17      18             (19)
                // since, password, first_name, last_name, sex, birth_date, rating, usatt, phone, email, address, city, state, zip, sponsor_id, balance, visits, position       (id)
                updatePlayer.setTimestamp(1, dateToTimestamp(player.getSince()));
                updatePlayer.setString(2, player.getPassword());
                updatePlayer.setString(3, player.getFirstName());
                updatePlayer.setString(4, player.getLastName());
                updatePlayer.setString(5, player.getSex().name().charAt(0)+"");
                updatePlayer.setTimestamp(6, dateToTimestamp(player.getBirthDate()));                
                updatePlayer.setObject(7, player.getClubRating(), JDBCType.INTEGER);                
                updatePlayer.setDouble(8, player.getRatingAcc());
                updatePlayer.setString(9, player.getPhone());
                updatePlayer.setString(10, player.getEmail());
                updatePlayer.setString(11, player.getAddress());
                updatePlayer.setString(12, player.getCity());
                updatePlayer.setString(13, player.getState());
                updatePlayer.setString(14, player.getZip());
                updatePlayer.setObject(15, player.getSponsorId(), JDBCType.INTEGER);
                updatePlayer.setDouble(16, Global.roundDollars(player.getBalance()));
                updatePlayer.setInt(17, player.getVisits());
                updatePlayer.setString(18, player.getPosition());
                updatePlayer.setInt(19, player.getId());
                updatePlayer.executeUpdate();
                return null;
            }
        }.attemptStatement();                
    }
    
    // ==================================================================================================
    // METHODS FOR INSERTING DATA
    // ==================================================================================================
    
    // Insert a Player into the database:
    public void insertPlayer(Player player)
    {        
        // Attempt Player table insertion:
        new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {
                // Insert Player record:                
                // 1      2         3           4          5    6           7       8      9      10     11       12    13     14   15          16       17      18
                // since, password, first_name, last_name, sex, birth_date, rating, usatt, phone, email, address, city, state, zip, sponsor_id, balance, visits, position
                insertPlayer.setTimestamp(1, dateToTimestamp(player.getSince()));
                insertPlayer.setString(2, player.getPassword());
                insertPlayer.setString(3, player.getFirstName());
                insertPlayer.setString(4, player.getLastName());
                insertPlayer.setString(5, player.getSex().name().charAt(0)+"");
                insertPlayer.setTimestamp(6, dateToTimestamp(player.getBirthDate()));                
                insertPlayer.setObject(7, player.getClubRating(), JDBCType.INTEGER);                
                insertPlayer.setObject(8, player.getRatingAcc(), JDBCType.DOUBLE);
                insertPlayer.setString(9, player.getPhone());
                insertPlayer.setString(10, player.getEmail());
                insertPlayer.setString(11, player.getAddress());
                insertPlayer.setString(12, player.getCity());
                insertPlayer.setString(13, player.getState());
                insertPlayer.setString(14, player.getZip());
                insertPlayer.setObject(15, player.getSponsorId(), JDBCType.INTEGER);
                insertPlayer.setDouble(16, Global.roundDollars(player.getBalance()));
                insertPlayer.setInt(17, player.getVisits());
                insertPlayer.setString(18, player.getPosition());
                insertPlayer.executeUpdate();
                return null;
            }
        }.attemptStatement();                        
    }             
    
    // Insert a Charge into the database:
    public void insertCharge(Charge charge)
    {        
        // Attempt Charge table insertion:
        new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {
                // Insert Charge record:                
                // 1          2     3       4
                // player_id, date, amount, description
                insertCharge.setInt(1, charge.getPlayerId());
                insertCharge.setTimestamp(2, dateToTimestamp(charge.getDate()));
                insertCharge.setDouble(3, Global.roundDollars(charge.getAmount()));                
                insertCharge.setString(4, charge.getDescription());
                insertCharge.executeUpdate();
                return null;
            }
        }.attemptStatement();                        
    }             
    
    // Insert a Transact into the database:
    public void insertTransact(Transact transact)
    {        
        // Attempt Transact table insertion:
        new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {
                // Insert Transact record:                
                // 1          2     3       4
                // player_id, date, amount, description
                insertTransact.setInt(1, transact.getPlayerId());
                insertTransact.setTimestamp(2, dateToTimestamp(transact.getDate()));
                insertTransact.setDouble(3, Global.roundDollars(transact.getAmount()));                
                insertTransact.setString(4, transact.getDescription());
                insertTransact.executeUpdate();
                return null;
            }
        }.attemptStatement();                        
    }            
    
    // Insert a Visit into the database:
    public void insertVisit(Visit visit)
    {        
        // Attempt Visit table insertion:
        new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {
                // Insert Visit record:                
                // 1          2
                // player_id, date
                insertVisit.setInt(1, visit.getPlayerId());
                insertVisit.setTimestamp(2, dateToTimestamp(visit.getDate()));
                insertVisit.executeUpdate();
                return null;
            }
        }.attemptStatement();                        
    }             
    
    // Insert a TTMatch into the database:
    public void insertTTMatch(TTMatch match)
    {        
        // Attempt TTMatch table insertion:
        new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {
                // Insert TTMatch record:                
                // 1          2         3          4     5                  6
                // winner_id, loser_id, points_ex, date, winner_new_rating, loser_new_rating
                insertTTMatch.setInt(1, match.getWinnerId());
                insertTTMatch.setInt(2, match.getLoserId());
                insertTTMatch.setInt(3, match.getPointsExchanged());
                insertTTMatch.setTimestamp(4, dateToTimestamp(match.getDate()));
                insertTTMatch.setInt(5, match.getWinnerNewRating());
                insertTTMatch.setInt(6, match.getLoserNewRating());
                insertTTMatch.executeUpdate();
                return null;
            }
        }.attemptStatement();                        
    }             
    
    // Insert a Lesson into the database:
    public void insertLesson(Lesson lesson)
    {        
        // Attempt Lesson table insertion:
        new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {
                // Insert Lesson record:                
                // 1          2
                // player_id, date
                insertLesson.setInt(1, lesson.getPlayerId());
                insertLesson.setTimestamp(2, dateToTimestamp(lesson.getDate()));
                insertLesson.executeUpdate();
                return null;
            }
        }.attemptStatement();                        
    }             
    
    
    // ==================================================================================================
    // METHODS FOR EXTRACTING DATA
    // ==================================================================================================
    
    // Returns a ResultSet with certain fields for players checked in on a particular date:
    public ResultSet extractVisitorsForDate(Date date)
    {        
        ResultSet playerData = new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {           
                extractVisitorsForDate.setTimestamp(1, dateToTimestamp(date));
                return extractVisitorsForDate.executeQuery();                
            }
        }.attemptQuery();        
        
        return playerData;
    }    
    
    // Returns a ResultSet with certain fields for all players:
    public ResultSet extractRoster()
    {        
        ResultSet playerData = new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {           
                return extractRoster.executeQuery();                
            }
        }.attemptQuery();        
        
        return playerData;
    }    
    
    // Returns a ResultSet with certain fields for players checked in on a particular date:
    public ResultSet extractAllPlayersForDate(Date date)
    {        
        ResultSet playerData = new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {           
                extractAllPlayersByDate.setTimestamp(1, dateToTimestamp(date));
                return extractAllPlayersByDate.executeQuery();                
            }
        }.attemptQuery();        
        
        return playerData;
    }    
    
    // Returns a ResultSet with data for all players, ordered by Id:
    public ResultSet extractAllPlayersById()
    {        
        ResultSet playerData = new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {                
                return extractAllPlayersById.executeQuery();                
            }
        }.attemptQuery();        
        
        return playerData;
    }
    
    // Returns a ResultSet with data for all players, ordered by Last Name:
    public ResultSet extractAllPlayersByLastName()
    {        
        ResultSet playerData = new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {                
                return extractAllPlayersByLastName.executeQuery();                
            }
        }.attemptQuery();        
        
        return playerData;
    }
        
    // Returns a ResultSet used to build a Player object:
    public ResultSet extractPlayer(int playerId)
    {        
        ResultSet playerData = new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {
                extractPlayer.setInt(1, playerId);
                return extractPlayer.executeQuery();                
            }
        }.attemptQuery();        
        
        return playerData;
    }
    
    
    // Returns a ResultSet with data for all charges 
    public ResultSet extractAllCharges(int playerId)
    {        
        ResultSet chargeData = new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {
                return extractAllCharges.executeQuery();                
            }
        }.attemptQuery();        
        
        return chargeData;
    }
    
    // Returns a ResultSet with data for all transacts 
    public ResultSet extractAllTransacts(int playerId)
    {        
        ResultSet transactData = new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {
                return extractAllTransacts.executeQuery();                
            }
        }.attemptQuery();        
        
        return transactData;
    }
    
    // Returns a ResultSet with data for all visits 
    public ResultSet extractAllVisits(int playerId)
    {        
        ResultSet visitData = new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {
                return extractAllVisits.executeQuery();                
            }
        }.attemptQuery();        
        
        return visitData;
    }
            
    // Returns a ResultSet with data for all lessons 
    public ResultSet extractAllLessons(int playerId)
    {        
        ResultSet lessonData = new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {
                return extractAllLessons.executeQuery();                
            }
        }.attemptQuery();        
        
        return lessonData;
    }
    
        
    // Returns a ResultSet with data for all charges relating to a Player:
    public ResultSet extractAllChargesForPlayer(int playerId)
    {        
        ResultSet chargeData = new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {     
                extractAllChargesForPlayer.setInt(1, playerId);
                return extractAllChargesForPlayer.executeQuery();                
            }
        }.attemptQuery();        
        
        return chargeData;
    }
    
    // Returns a ResultSet with data for all transacts relating to a Player:
    public ResultSet extractAllTransactsForPlayer(int playerId)
    {        
        ResultSet transactData = new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {     
                extractAllTransactsForPlayer.setInt(1, playerId);
                return extractAllTransactsForPlayer.executeQuery();                
            }
        }.attemptQuery();        
        
        return transactData;
    }
    
    // Returns a ResultSet with data for all visits relating to a Player:
    public ResultSet extractAllVisitsForPlayer(int playerId)
    {        
        ResultSet visitData = new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {     
                extractAllVisitsForPlayer.setInt(1, playerId);
                return extractAllVisitsForPlayer.executeQuery();                
            }
        }.attemptQuery();        
        
        return visitData;
    }
            
    // Returns a ResultSet with data for all lessons relating to a Player:
    public ResultSet extractAllLessonsForPlayer(int playerId)
    {        
        ResultSet lessonData = new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {     
                extractAllLessonsForPlayer.setInt(1, playerId);
                return extractAllLessonsForPlayer.executeQuery();                
            }
        }.attemptQuery();        
        
        return lessonData;
    }
    
    
    // Returns a ResultSet used to build a Charge object:
    public ResultSet extractCharge(int chargeId)
    {        
        ResultSet chargeData = new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {
                extractCharge.setInt(1, chargeId);
                return extractCharge.executeQuery();                
            }
        }.attemptQuery();        
        
        return chargeData;
    }
    
    // Returns a ResultSet used to build a Transact object:
    public ResultSet extractTransact(int transactId)
    {        
        ResultSet transactData = new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {
                extractTransact.setInt(1, transactId);
                return extractTransact.executeQuery();                
            }
        }.attemptQuery();        
        
        return transactData;
    }
    
    // Returns a ResultSet used to build a Visit object:
    public ResultSet extractVisit(int visitId)
    {        
        ResultSet visitData = new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {
                extractVisit.setInt(1, visitId);
                return extractVisit.executeQuery();                
            }
        }.attemptQuery();        
        
        return visitData;
    }
    
    // Returns a ResultSet used to build a TTMatch object:
    public ResultSet extractTTMatch(int matchId)
    {        
        ResultSet matchData = new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {
                extractTTMatch.setInt(1, matchId);
                return extractTTMatch.executeQuery();                
            }
        }.attemptQuery();        
        
        return matchData;
    }
    
    // Returns a ResultSet used to build a Lesson object:
    public ResultSet extractLesson(int lessonId)
    {        
        ResultSet lessonData = new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {
                extractLesson.setInt(1, lessonId);
                return extractLesson.executeQuery();                
            }
        }.attemptQuery();        
        
        return lessonData;
    }
    
    // Returns a ResultSet used to build a Player's Charge/Transaction History:
    public ResultSet extractPlayerTransactHistory(int playerId)
    {        
        ResultSet historyData = new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {
                extractPlayerTransactHistory.setInt(1, playerId);
                extractPlayerTransactHistory.setInt(2, playerId);
                return extractPlayerTransactHistory.executeQuery();                
            }
        }.attemptQuery();        
        
        return historyData;
    }
    
    // Returns a ResultSet used to build the MatchPoint Ledger:
    public ResultSet extractLedger(int year)
    {        
        ResultSet ledgerData = new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {
                extractLedger.setInt(1, year);
                return extractLedger.executeQuery();                
            }
        }.attemptQuery();        
        
        return ledgerData;
    }
    
    
    // ==================================================================================================
    // INFO / BOOLEAN CHECK METHODS
    // ==================================================================================================
    
    public boolean checkVisitedToday(Player player)
    {
        ResultSet results = new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {
                checkVisitedToday.setInt(1, player.getId());
                checkVisitedToday.setTimestamp(2, dateToTimestamp(Global.getTodayDate()));
                return checkVisitedToday.executeQuery();
            }
        }.attemptQuery();        
        try
        {
            results.first();
            return results.getInt("rowcount") > 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(MpDb.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public Boolean checkHadLessonToday(Player player)
    {
        ResultSet results = new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {
                checkHadLessonToday.setInt(1, player.getId());
                checkHadLessonToday.setTimestamp(2, dateToTimestamp(Global.getTodayDate()));
                return checkHadLessonToday.executeQuery();
            }
        }.attemptQuery();        
        try
        {
            results.first();
            return results.getInt("rowcount") > 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(MpDb.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public Integer checkWeeklyLessonCount(Player player)
    {
        ResultSet results = new DatabaseExecution()
        {
            @Override
            protected ResultSet operation() throws SQLException, Exception
            {
                checkWeeklyLessonCount.setInt(1, player.getId());
                checkWeeklyLessonCount.setTimestamp(2, dateToTimestamp(Global.getWeekStart(Global.getTodayDate())));
                return checkWeeklyLessonCount.executeQuery();
            }
        }.attemptQuery();        
        try
        {
            results.first();
            return results.getInt("rowcount");
        }
        catch (SQLException ex)
        {
            Logger.getLogger(MpDb.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    // ==================================================================================================
    // UTILITY METHODS
    // ==================================================================================================
    
    // Runs an entire SQL script file in IJ, returning false for any non-zero result code.
    public boolean runScriptFile(File scriptFile)
    {
        FileInputStream fileStream = null;
        try
        {
            fileStream = new FileInputStream(scriptFile);
            int result = ij.runScript(connection, fileStream, "UTF-8", System.out, "UTF-8");            
            return (result == 0);
        }
        catch (FileNotFoundException | UnsupportedEncodingException e)
        {
            return false;
        }
        finally
        {
            if (fileStream != null)
            {
                try
                {
                    fileStream.close();
                }
                catch (IOException e)
                {
                }
            }
        }
    }

    // Runs an SQL script file statement-by-statement in IJ, returning a list of non-zero results.
    public ArrayList<String> runScriptFilePerStatement(File scriptFile)
    {
        String[] chunks = ReadWriteTextFile.getContents(scriptFile).split(";");
        ArrayList<String> statements = new ArrayList<>();

        // Get individual statements from file:
        for (int s = 0; s < chunks.length; s++)
        {
            if (!chunks[s].trim().isEmpty())
            {
                statements.add((chunks[s] + ";").trim());
            }
        }

        // Examine each statement by line, and discard comment lines:
        for (int s = 0; s < statements.size(); s++)
        {
            String[] lines = statements.get(s).split("\n");
            for (int n = 0; n < lines.length; n++)
            {
                if (lines[n].trim().startsWith("--"))
                {
                    lines[n] = "";
                }
            }
            statements.set(s, StringHelper.join(lines, ""));
        }

        ArrayList<String> results = new ArrayList<>();

        for (int s = 0; s < statements.size(); s++)
        {
            try
            {
                InputStream stream = new ByteArrayInputStream(statements.get(s).getBytes(StandardCharsets.UTF_8));
                int code = ij.runScript(connection, stream, "UTF-8", System.out, "UTF-8");
                stream.close();

                if (code != 0)
                {
                    results.add("CODE " + code + ": " + statements.get(s));
                }

            }
            catch (Exception ex)
            {
                Logger.getLogger(MpDb.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return results;
    }

    // Runs an SQL script contained in a string and returns the result code, or -1 if an Exception occurred.
    public int runScriptString(String script)
    {
        try
        {
            InputStream stream = new ByteArrayInputStream(script.getBytes(StandardCharsets.UTF_8));
            int code = ij.runScript(connection, stream, "UTF-8", System.out, "UTF-8");
            stream.close();
            return code;
        }
        catch (Exception ex)
        {
            Logger.getLogger(MpDb.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    // Outputs a column-aligned table showing column titles and result set data;
    // also outputs to the designated file if it is not null.
    public static void showResultSet(String title, ResultSet rs, int columnSize, File outputFile)
    {
        try
        {
            ArrayList<char[]> table = new ArrayList<>();
            
            System.out.println("\n-------------------------| " + title + " |-------------------------\n");
            ResultSetMetaData meta = rs.getMetaData();
            int columns = meta.getColumnCount();

            char[] row = createRow(columns * columnSize);
            for (int c = 1; c <= columns; c++)
            {
                insertCell(row, meta.getColumnLabel(c), (c - 1) * columnSize);
            }
            table.add(row);
            rs.first();
            while (!rs.isAfterLast())
            {
                if (rs.getRow() > 0)
                {
                    row = createRow(columns * columnSize);
                    for (int c = 1; c <= columns; c++)
                    {

                        String text = rs.getObject(c) == null ? "NULL" : rs.getObject(c).toString();
                        insertCell(row, text, (c - 1) * columnSize);
                    }
                    table.add(row);
                }
                rs.next();
            }

            for (int r = 0; r < table.size(); r++)
            {
                System.out.println(table.get(r));
            }

            if (outputFile != null)
            {
                try
                {
                    if (!outputFile.exists())
                    {
                        outputFile.createNewFile();
                    }
                    StringBuilder fileText = new StringBuilder();
                    for (int r = 0; r < table.size(); r++)
                    {
                        fileText = fileText.append(table.get(r)).append("\n");
                    }
                    ReadWriteTextFile.setContents(outputFile, fileText.toString());
                }
                catch (IOException ex)
                {
                    Logger.getLogger(MpDb.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        catch (SQLException ex)
        {
            Logger.getLogger(MpDb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Returns a character array of all spaces with [size] length.
    public static char[] createRow(int size)
    {
        char[] row = new char[size];
        for (int c = 0; c < row.length; c++)
        {
            row[c] = ' ';
        }
        return row;
    }
    
    // Inserts a piece of text at the specified position in a character row.
    // If text is too long for the column, it may be truncated by text in the
    // following column.
    public static char[] insertCell(char[] record, String text, int pos)
    {
        int len = 0;
        while (pos < record.length && len < text.length())
        {
            record[pos] = text.charAt(len);
            pos++;
            len++;
        }
        return record;
    }

    // Converts a simple data object into an appropriate String form
    // to be used in SQL statements/queries. null becomes "NULL".
    // A single value expressed as text, number, or timestamp (Date).
    public static String getInsertableValue(Object obj)
    {
        if (obj == null)
        {
            return "NULL";
        }

        if (obj instanceof Date)
        {
            return "'" + ((Date)obj).toString() + "'";
        }
        if (obj instanceof String)
        {
            return "'" + obj.toString() + "'";
        }
        else
        {
            return obj.toString();
        }
    }

    // Converts a Date to an SQL Timestamp:
    public static Timestamp dateToTimestamp(Date date)
    {
        return new Timestamp(date.getTime());
    }
    
    
}
