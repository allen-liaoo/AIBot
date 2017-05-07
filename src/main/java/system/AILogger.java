/* 
 * AIBot by AlienIdeology
 * 
 * AILogger
 * Log when commands called, error occurred, or bot joined a server
 */
package system;

import constants.Emoji;
import constants.Global;
import main.AIBot;
import constants.FilePath;

import java.awt.*;
import java.io.*;
import java.time.Instant;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.ErrorResponseException;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class AILogger {

    public static Logger startLogger = Logger.getLogger(AIBot.class.getName());
    public static Logger errorLogger = Logger.getLogger("Error");
    public static Logger commandLogger = Logger.getLogger("command");

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

                /* Log the exception in AIBot Server #bug_report */
                handleExceptionLog(ex, event, toHasteBin(stackToString(ex)));
            } else if(obj != null) {
                from = "Class: " + obj.toString();
            } else {
                from = "Unknown";
            }

            /* Log the exception in local file */
            Logger.getGlobal().log(Level.WARNING, "Error in " + at + " from " + from);
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
            
            commandLogger.log(Level.INFO, "From" + from + "\n\tcommand: " + command + "\tDescription: " + description);
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

    /**
     * Log the exception to AIBot server's #bug-report
     * @param ex
     * @param e
     * @param hasteBinURL
     */
    public static void handleExceptionLog(Exception ex, MessageReceivedEvent e, String hasteBinURL)
    {
        String user = "", id = "", from = "", from2 = "None", channel = "";
        EmbedBuilder error = new EmbedBuilder().setAuthor(ex.getClass().getName(), hasteBinURL, Global.B_AVATAR)
            .setColor(Color.RED).setTimestamp(Instant.now())
            .addField("User", e.getAuthor().getName()+" #"+e.getAuthor().getDiscriminator()+" ("+e.getAuthor().getId()+")", true);

        if (e.isFromType(ChannelType.TEXT)) {
            from = "Guild";
            from2 = e.getGuild().getName()+"("+e.getGuild().getId()+")";
            channel = e.getChannel().getName()+" ("+e.getChannel().getId()+")";
        } else if (e.isFromType(ChannelType.PRIVATE)) {
            from = "PM";
            from2 = e.getAuthor().getName();
            channel = e.getPrivateChannel().getName()+" ("+e.getPrivateChannel().getId()+")";
        }

        error.addField(from, from2, true)
        .addField("Channel", channel, true)
        .addField("Stack Trace", "**[" + hasteBinURL + "]("+hasteBinURL+")**", true);
        AIBot.jda.getTextChannelById(Global.B_SERVER_ERROR).sendMessage(error.build()).queue();
    }

    /**
     * Handle ErrorResponse.
     * @param ere
     * @param e
     * @return true if the response if handled
     */
    public static boolean errorResponseHandler(ErrorResponseException ere, MessageReceivedEvent e) {
        boolean handled = true;
        String error = Emoji.ERROR + " ";
        switch (ere.getErrorResponse()) {
            case CANNOT_SEND_TO_USER:
                e.getChannel().sendMessage(error+"I can not send message to "+e.getAuthor().getName()).queue();
                break;
            case EMBED_DISABLED:
                e.getChannel().sendMessage(error+"Please enable embed so I can talk freely.").queue();
                break;
            case INVALID_BULK_DELETE:
            case INVALID_BULK_DELETE_MESSAGE_AGE:
                e.getChannel().sendMessage(error+"Error while deleting messages.\n" +
                        "The messages might be too old (older than 2 weeks).").queue();
                break;
            case MISSING_ACCESS:
                e.getChannel().sendMessage(error+"Missing access.").queue();
                break;
            case UNKNOWN_GUILD:
            case UNKNOWN_CHANNEL:
            case UNKNOWN_MEMBER:
                errorLog(ere,e,"ErrorResponseHandler","Unknown guild,channel,or member");
                break;
            default:
                handled = false;
                break;
        }

        return handled;
    }
}
