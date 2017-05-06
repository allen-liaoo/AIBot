/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package audio;

import main.AIBot;
import constants.Emoji;
import constants.FilePath;
import Setting.Prefix;
import system.AILogger;
import utility.UtilBot;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * loadFm -> loadLibrary(ArrayList of libraries) -> Add fmSongs to Songs[] -> Generate random fmSongs
 https://TEMP.discord.fm/libraries/json
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class FM {
    
    public final static String FM_base_url = "https://temp.discord.fm";
    public final static String FM_libraries_url = FM_base_url + "/libraries/json";
    public final static String FM_library_url = FM_base_url + "/libraries/?/json";
    
    public static void loadFm(String input, MessageReceivedEvent e) throws UnirestException, IOException
    {
        JSONArray array = loadLibrary(input);
        String[] local = loadLocalLibrary(input);
        
        if(!Music.checkMode(e, audio.PlayerMode.FM))
            return;
        
        if(array == null && local == null) {
            e.getChannel().sendMessage(Emoji.ERROR + " Playlist not found. \nUse `" + Prefix.DIF_PREFIX + "fm` for available playlists.").queue();
            return;
        }
        
        if(local != null) {
            for(int i = 0; i < local.length; i ++)
            {
                AIBot.getGuild(e.getGuild()).getScheduler().addFMSong(local[i]);
            }
        }
        
        else if(array != null)
        {
            for(int i = 0; i < array.length(); i ++)
            {
                JSONObject jo = array.getJSONObject(i);

                try {
                    AIBot.getGuild(e.getGuild()).getScheduler().addFMSong("https://www.youtube.com/watch?v=" + jo.get("identifier").toString());
                } catch (org.json.JSONException jsex) {
                    e.getChannel().sendMessage(Emoji.ERROR + " Playlist not found. \nUse `" + Prefix.DIF_PREFIX + "fm` for available playlists.").queue();
                    return;
                }
            }
        }
        
        //Prevent user from calling FM outside of voice channel
        Connection.connect(e, false);
        if(!e.getMember().getVoiceState().inVoiceChannel())
            return;
        
        AIBot.getGuild(e.getGuild()).getScheduler().autoFM();
        
        //Log
        AILogger.commandLog(e, "FM#loadFM", "Fm loaded");
    }
    
    
    public static JSONArray loadLibrary(String input) throws UnirestException, IOException 
    {
        String id = "";
        JSONArray array = Unirest
                .get(FM_libraries_url)
                .header("User-Agent", "AIBot")
                .asJson().getBody().getArray();
        
        //Get specific libraries from DFM
        for(int i = 0; i < array.length(); i++)
        {
            JSONObject lib = array.getJSONObject(i);
            String name = lib.get("name").toString();
            
            if(name.toLowerCase().contains(input.toLowerCase()))
            {
                id = lib.get("id").toString();
                break;
            }
        }
        
        //Get songs in playlist
        JSONArray array2 = Unirest
                .get(FM_library_url.replace("?", id))
                .header("User-Agent", "AIBot")
                .asJson().getBody().getArray();
        
        System.out.println("FM#loadFm --> " + FM_library_url.replace("?", id));
        
        return array2;
    }
    
    public static String[] getLibrary() throws UnirestException
    {
        UtilBot.setUnirestCookie();
        
        JSONArray array = Unirest.get(FM_libraries_url).header("User-Agent", "AIBot").asJson().getBody().getArray();
        String[] libs = new String[array.length()];
        
        for(int i = 0; i < array.length(); i++)
        {
            JSONObject lib = array.getJSONObject(i);
            libs[i] = lib.get("name").toString();
        }
        
        return libs;
    }
    
    public static String[] loadLocalLibrary(String input) throws IOException
    {
        //Load A List of Play Lists
        BufferedReader br = new BufferedReader(new FileReader(FilePath.LP_List));
        String pl;
        
        while((pl = br.readLine()) != null) {
            if(pl.toLowerCase().contains(input.toLowerCase()))
                break;
        }
        
        try {
            //Load Play Lists
            BufferedReader br2 = new BufferedReader(new FileReader(FilePath.LP + pl + ".txt"));
            String line, songString = "";

            while((line = br2.readLine()) != null) {
                songString += line + " ";
            }

            String[] songs = songString.split(" ");
            return songs;
        } catch (FileNotFoundException fnfe) {
            return null;
        }
    }
    
    public static String getLocalLibrary() throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(FilePath.LP_List));
        String pl = "", plString = "";
        
        while((pl = br.readLine()) != null)
        {
            plString += pl + ", ";
        }
        plString = plString.substring(0, plString.length() - 2);
        
        return plString;
    }

}
    
