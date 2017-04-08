/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Setting;

import Main.Main;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class SmartLogger {

    public static Logger startLogger = Logger.getLogger(Main.class.getName());
    public static Logger errorLogger = Logger.getLogger("Error");
    public static Logger commandLogger = Logger.getLogger("Command");
    public static String LogMain = "/Users/liaoyilin/NetBeansProjects/DiscordBot/src/main/java/Setting/LogMain.txt";
    public static String LogError = "/Users/liaoyilin/NetBeansProjects/DiscordBot/src/main/java/Setting/LogError.txt";
    public static String LogCommand = "/Users/liaoyilin/NetBeansProjects/DiscordBot/src/main/java/Setting/LogCommand.txt";
    
    /**
     * Logging when bot status changed
     * @param msg the message for logging
     */
    public static void updateLog(String msg) {
        try {
            FileHandler fh = new FileHandler(LogMain, true);
            startLogger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            startLogger.setUseParentHandlers(false);
            
            startLogger.info(msg);
            fh.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Logging when an exception is thrown
     * @param ex the Exception for logging
     * @param event the event for getting guild name/author name
     * @param at the exception source (class name)
     * @param cause cause of the exception
     */
    public static void errorLog(Exception ex, MessageReceivedEvent event, String at, String cause) {
        try {
            FileHandler fhe = new FileHandler(LogError, true);
            errorLogger.addHandler(fhe);
            SimpleFormatter formatter = new SimpleFormatter();
            fhe.setFormatter(formatter);
            errorLogger.setUseParentHandlers(false);
            
            String from;
            if(event.getChannelType() == event.getChannelType().TEXT)
                from = " guild: " + event.getGuild().getName();
            else if (event.getChannelType() == event.getChannelType().PRIVATE)
                from = " PM: " + event.getAuthor().getName();
            else
                from = ": Unknown (Probably local source)";
            
            Logger.getGlobal().log(Level.WARNING, "Error in " + at + " from" + from);
            errorLogger.log(Level.WARNING, "From" + from + "\n\t Cause: " + at + " -> " + cause, ex);
            fhe.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    /**
     * Logging when a command is called.
     * @param event the event for getting guild name/author name
     * @param command the command called
     * @param description the description of this command call
     */
    public static void commandLog(MessageReceivedEvent event, String command, String description) {
         try {
            FileHandler fhc = new FileHandler(LogCommand, true);
            commandLogger.addHandler(fhc);
            SimpleFormatter formatter = new SimpleFormatter();
            fhc.setFormatter(formatter);
            commandLogger.setUseParentHandlers(false);
            
            String from;
            if(event.getChannelType() == event.getChannelType().TEXT)
                from = " guild : " + event.getGuild().getName();
            else if (event.getChannelType() == event.getChannelType().PRIVATE)
                from = " PM: " +event.getAuthor().getName();
            else
                from = ": Unknown (Probably local source)";
            
            commandLogger.log(Level.INFO, "From" + from + "\n\tCommand: " + command + "\tDescription: " + description);
            fhc.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
