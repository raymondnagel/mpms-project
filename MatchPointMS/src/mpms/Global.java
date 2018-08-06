/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mpms;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import mpms.sound.Sound;
import mpms.sound.SoundManager;

/**
 *
 * @author rnagel
 */
public class Global {
    private static final double[] lessonRates = {12.00, 10.00, 8.00};
    public static String AUTHENTICATION_PASSWORD = "admin";
    public static NumberFormat DOLLARS = NumberFormat.getCurrencyInstance(Locale.US);    
    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
    public static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("h:mm");
    public static SimpleDateFormat TIME_AND_DATE = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    public static SimpleDateFormat EXACT_TIME_AND_DATE = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    public static SimpleDateFormat TIME_DATE_FOR_FILE = new SimpleDateFormat("MM-dd-yyyy HH-mm");
    public static SimpleDateFormat WEEK_LESSON_FORMAT = new SimpleDateFormat("E h:mm");
    public static SoundManager soundManager = new SoundManager(50);
    
    public static double roundDollars(double dollars)
    {
        return Double.parseDouble(new DecimalFormat("#.##").format(dollars));
    }
    
    public static Date getTodayDate()
    {
        Calendar now = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        today.clear();
        today.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE));        
        return today.getTime();
    }
    public static Date getTodayExactTime()
    {        
        Calendar today = Calendar.getInstance();       
        return today.getTime();
    }
    
    public static Date getWeekStart(Date forDate)
    {        
        Calendar forCal = Calendar.getInstance();
        forCal.setTime(forDate);
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(forCal.get(Calendar.YEAR), forCal.get(Calendar.MONTH), forCal.get(Calendar.DATE));
        int weekDay = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DAY_OF_WEEK, -weekDay);
        return cal.getTime();
    }
    
    public static boolean isSameDay(Date date1, Date date2)
    {
        Calendar cal1 = new GregorianCalendar();
        cal1.setTime(date1);
        Calendar cal2 = new GregorianCalendar();
        cal2.setTime(date2);
        
        return (cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH) && 
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) && 
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) );        
    }    
    
    public static Integer parseNullableInt(String expression)
    {        
        if (expression == null || expression.isEmpty())
            return null;
        try {
            int value = Integer.parseInt(expression);
            return value;
        } catch (NumberFormatException ex)
        {
            return null;
        }
    }
    
    public static MultiTypeTableModel getTableModelFromResultSet(ResultSet results)
    {
        try
        {
            MultiTypeTableModel model = new MultiTypeTableModel();
            ResultSetMetaData meta = results.getMetaData();
            int cols = meta.getColumnCount();
            for (int c = 1; c <= cols; c++)
            {
                model.addColumn(meta.getColumnName(c));
            }
            
            if (results.isFirst())
            {
                while (!results.isAfterLast())
                {
                    Object[] row = new Object[cols];
                    for (int c = 1; c <= cols; c++)
                    {
                        row[c-1] = results.getObject(c);
                    }
                    model.addRow(row);
                    results.next();                
                }
            }    
            
            return model;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Global.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }        
    
    // Similar to the above method, this one inverts the amounts for all records
    // with [PAYMENT] in the description column; the end result being that all
    // payments are positive values and all refunds remain negative values: so
    // that they can be summed for a revenue total.
    // Note that [+/-TRANSFER] amounts stay the same.
    public static MultiTypeTableModel getLedgerModelFromResultSet(ResultSet results)
    {
        try
        {
            MultiTypeTableModel model = new MultiTypeTableModel();
            ResultSetMetaData meta = results.getMetaData();
            int cols = meta.getColumnCount();
            
            int amtCol = -1;
            int descCol = -1;
            
            for (int c = 1; c <= cols; c++)
            {
                if (meta.getColumnName(c).equalsIgnoreCase("AMOUNT"))
                    amtCol = c;
                if (meta.getColumnName(c).equalsIgnoreCase("DESCRIPTION"))
                    descCol = c;
                
                model.addColumn(meta.getColumnName(c));
            }
            
            if (results.isFirst())
            {
                while (!results.isAfterLast())
                {
                    Object[] row = new Object[cols];
                    for (int c = 1; c <= cols; c++)
                    {
                        row[c-1] = results.getObject(c);
                    }
                    
                    String description = (String)row[descCol-1];
                    Double amount = (Double)row[amtCol-1];
                    if (description.toUpperCase().contains("[PAYMENT]"))
                    {
                        row[amtCol-1] = -amount;
                    }
                    
                    model.addRow(row);
                    results.next();                
                }
            }    
            
            return model;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Global.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }        
    
    public static double getPriceForLessonIndex(int lessonIndex)
    {
        return lessonRates[lessonIndex-1];
    }
    
    public static double normalizeMoneyAmount(String txtAmount)
    {
        double amount = Double.parseDouble(txtAmount.replace("$", ""));
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(amount));
    }
    
    public static void playSound(String soundName)
    {
        soundManager.playSound(new Sound("res/sounds/" + soundName + ".wav"));
    }
}
