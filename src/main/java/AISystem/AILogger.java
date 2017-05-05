/* 
 * AIBot by AlienIdeology
 * 
 * AILogger
 * Log when commands called, error occurred, or bot joined a server
 */
package AISystem;

import Listener.CommandListener;
import Main.Main;
import Constants.FilePath;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONObject;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class AILogger {

    public static Logger startLogger = Logger.getLogger(Main.class.getName());
    public static Logger errorLogger = Logger.getLogger("Error");
    public static Logger commandLogger = Logger.getLogger("Command");
    
    /**
     * Logging when bot status changed
     * @param msg the message for logging
     */
    public static void updateLog(String msg) {
        try {
            FileHandler fh = new FileHandler(FilePath.LogMain, true);
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
     * @param obj the event/class for getting guild name/author name
     * @param at the exception source (class name)
     * @param cause cause of the exception
     */
    public static void errorLog(Exception ex, Object obj, String at, String cause) {
        try {
            FileHandler fhe = new FileHandler(FilePath.LogError, true);
            errorLogger.addHandler(fhe);
            SimpleFormatter formatter = new SimpleFormatter();
            fhe.setFormatter(formatter);
            errorLogger.setUseParentHandlers(false);

            /* Get where the exception is from */
            String from;
            if(obj instanceof MessageReceivedEvent) {
                MessageReceivedEvent event = (MessageReceivedEvent) obj;
                if (event.getChannelType() == event.getChannelType().TEXT)
                    from = "guild: " + event.getGuild().getName();
                else if (event.getChannelType() == event.getChannelType().PRIVATE)
                    from = "PM: " + event.getAuthor().getName();
                else
                    from = ": Unknown (From unknown channel type.)";
            } else if(obj != null) {
                from = "Class: " + obj.toString();
            } else {
                from = "Unknown";
            }

            /* Log the exception in local file */
            Logger.getGlobal().log(Level.WARNING, "Error in " + at + " from " + from);
            errorLogger.log(Level.WARNING, "From" + from + "\n\t Cause: " + at + " -> " + cause, ex);
            fhe.close();

            /* Log the exception in AIBot Server #bug_report */
            CommandListener.handleExceptionLog(ex, (MessageReceivedEvent) obj, toHasteBin(stackToString(ex)));

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
            FileHandler fhc = new FileHandler(FilePath.LogCommand, true);
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
    
    /**
     * Return the stack trace of an exception
     * @param ex
     * @return
     */
    public static String stackToString(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter w = new PrintWriter(sw);
        ex.printStackTrace(w);
        return sw.toString();
    }

    /**
     * Paste a String to HasteBin and return the URL
     *
     * @param message The string to be sent in the body of the POST request
     * @return A formatted URL which links to the pasted file
     */
    public static String toHasteBin(String message) {
        try {
            System.out.println("ayy");

            JsonNode obj = Unirest.post("https://hastebin.com/documents")
                    .header("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)")
                    .header("Content-Type", "text/plain")
                    .body(message)
                    .asJson()
                    .getBody();

            return "https://hastebin.com/" + obj.getObject().getString("key");

        } catch (UnirestException e) {
            AILogger.errorLog(e, null, "Pasting to HasteBin", "Probably server side");
        }
        return null;
    }

}
