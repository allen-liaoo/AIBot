/* 
 * AIBot by AlienIdeology
 * 
 * Prefix
 * Accessing prefix
 */
package Setting;

import Main.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;


/**
 *
 * @author liaoyilin
 */
public class Prefix {
    public static String DIF_PREFIX = "=";
    
    public static String getDefaultPrefix()
    {   
        return DIF_PREFIX;
    }
    /*
    public static String getGuildPrefix(String id)
    {
        //String serverPrefix = reader(id);
        
        if("".equals(serverPrefix))
            return DIF_PREFIX;
        else
            return serverPrefix;
    }
    
    
    public static void setPrefix(String p, MessageReceivedEvent e)
    {
        writer(e.getGuild().getId(), p);
        e.getChannel().sendMessage(Emoji.E_success + " The Prefix has been changed to `" + p + "`.").queue();
    }
    
    public static String reader(String id)
    {
        String line = "", ID = "", prefix = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/Users/liaoyilin/NetBeansProjects/DiscordBot/src/main/java/Config/GuildSetting.rtf"));
            
            while((line = reader.readLine() ) != null)
            {
                if(line.length() > 19)
                {
                    if((id).equals(line.substring(0, 18)))
                    {
                        ID = line.substring(0, 18);
                        prefix = line.substring(19);
                        break;
                    }
                }
                    
            }

            reader.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found.");
            fnfe.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }
        return prefix;
    }
    
    public static void writer(String id, String prefix)
    {
        try {
            FileWriter writer = new FileWriter ("/Users/liaoyilin/NetBeansProjects/DiscordBot/src/main/java/Config/GuildSetting.rtf", true);
            writer.write("\n" + id + " " + prefix);
            writer.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found.");
            fnfe.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
*/
}
