/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Audio;

import Resource.Emoji;
import Resource.FilePath;
import Utility.SmartLogger;
import com.moandjiezana.toml.Toml;
import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class Radio {
    
    public static void loadRadio(String input, MessageReceivedEvent e)
    {
        String link = loadStation(input);
        
        if(link == null)
        {
            e.getChannel().sendMessage(Emoji.error + " Cannot find radio station called `" + input + "`.").queue();
            return;
        }
        
        Music.play(link, e);
        
        //Log
        SmartLogger.commandLog(e, "FM#loadFM", "Fm loaded");
        System.out.println("Radio#loadRadio --> " + link);
    }
    
    public static String loadStation(String input)
    {
        Toml toml = new Toml().read(new File(FilePath.RadioStations));
        return toml.getString("\"" + input + "\"");
    }
    
    public static String getStations()
    {
        Toml toml = new Toml().read(new File(FilePath.RadioStations));
        
        Map<String, Object> map = toml.toMap();
        Iterator<String> names = map.keySet().iterator();
        String rstation = "";
        
        while(names.hasNext())
        {
            String station = names.next();
            rstation += station.substring(1, station.length() - 1).replaceAll("\"", "");
            if (names.hasNext()) {
                rstation += ", ";
            }
        }
        
        return rstation;
    }
    
}
