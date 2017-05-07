/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package audio;

import audio.AudioTrackWrapper.TrackType;
import constants.Emoji;
import constants.FilePath;
import org.json.JSONObject;
import org.json.JSONTokener;
import setting.Prefix;
import system.AILogger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class Radio {
    
    public static void loadRadio(String input, MessageReceivedEvent e)
    {
        String link = findStationNameByName(input);
        link = getStationURLByName(link);
        
        if(link == null) {
            e.getTextChannel().sendMessage(Emoji.ERROR + " Playlist not found. \nUse `" + Prefix.DIF_PREFIX + "radio` for available playlists.").queue();
            return;
        }
        
        Music.play(link, e, TrackType.RADIO);
        
        //Log
        AILogger.commandLog(e, "FM#loadFM", "Fm loaded");
        System.out.println("Radio#loadRadio --> " + link);
    }

    /**
     * Get station name by keyword
     * @param input
     * @return
     */
    public static String findStationNameByName(String input)
    {
        LinkedList stations = new LinkedList(getRadioStations().keySet());
        for(Object obj : stations) {
            if(obj.toString().toLowerCase().contains(input.toLowerCase())) {
                return obj.toString();
            }
        }
        return null;
    }

    /**
     * Get url by exact name
     * @param input
     * @return
     */
    public static String getStationURLByName(String input)
    {
        Map stations = getRadioStations();
        return stations.containsKey(input) ? stations.get(input).toString() : null;
    }
    
    public static String getStations()
    {
        LinkedList stations = new LinkedList(getRadioStations().keySet());
        Collections.sort(stations);
        String rs = "```";
        for(int i = 0; i < stations.size(); i++) {
            Object obj = stations.get(i);
            rs += obj.equals(stations.getLast()) ? obj : obj + ", ";
        }
        
        return rs+"```";
    }

    /**
     * @return a map of radio stations `<name, url>`
     */
    public static Map getRadioStations()
    {
        Map map = null;
        try {
            InputStream targetStream = new FileInputStream(new File(FilePath.RadioStation));
            JSONTokener jsonTokener = new JSONTokener(targetStream);
            JSONObject json = new JSONObject(jsonTokener);
            map = json.toMap();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        return map;
    }
    
}
