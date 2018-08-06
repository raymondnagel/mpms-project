/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpms;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Ray
 */
public class MpDbConsole {
    
    private final static MpDb database = new MpDb();
    private final static Scanner scanner = new Scanner(System.in);
    private final static ArrayList<String> reservedCommands = new ArrayList<>();
    
    public static void main(String args[]) {
        initReservedCommands();
        startDatabase(false);
        consoleLoop();        
    }
    
    private static void initReservedCommands()
    {
        reservedCommands.add("EXIT");
        reservedCommands.add("IMPORT XML");
    }
    
    public static void consoleLoop()
    {
        String input;
        do
        {
            output("MatchPointDB>");
            input = input();
            processInput(input);
        } while(!input.equalsIgnoreCase("EXIT"));
    }
    
    private static void processInput(String input)
    {
        if (!reservedCommands.contains(input))
        {
            database.runScriptString(input);
        }
        else
        {
            if (input.equalsIgnoreCase("IMPORT XML"))
            {
                output("This command has been disabled by the administrator.");
            }
        }
    }
    
    private static void output(String text)
    {
        System.out.println(text);
    }
    private static String input()
    {
        return scanner.nextLine().trim().toUpperCase();
    }
    
    private static void startDatabase(boolean resetToLegacy)
    {
        database.openConnection();
        database.createDatabase(resetToLegacy);
        database.prepareStatements();
    }
        
}
