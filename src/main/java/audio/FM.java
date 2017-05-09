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
import org.json.JSONTokener;
import setting.Prefix;
import system.AILogger;
import utility.UtilBot;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.*;

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
                AIBot.getGuild(e.getGuild()).getGuildPlayer().addFMSong(local[i]);
            }
        }
        
        else if(array != null)
        {
            for(int i = 0; i < array.length(); i ++)
            {
                JSONObject jo = array.getJSONObject(i);

                try {
                    AIBot.getGuild(e.getGuild()).getGuildPlayer().addFMSong("https://www.youtube.com/watch?v=" + jo.get("identifier").toString());
                } catch (org.json.JSONException jex) {
                    e.getChannel().sendMessage(Emoji.ERROR + " Playlist not found. \nUse `" + Prefix.DIF_PREFIX + "fm` for available playlists.").queue();
                    return;
                }
            }
        }
        
        //Prevent user from calling FM outside of voice channel
        if(!e.getMember().getVoiceState().inVoiceChannel())
            return;

        Music.connect(e, false);
        AIBot.getGuild(e.getGuild()).getGuildPlayer().autoFM();
        
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
        String libraries[] = getLocalLibrary().split(", ");
        String library = "";

        //Find the correct play list
        for(String name : libraries) {
            if(name.toLowerCase().contains(input.toLowerCase())) {
                library = name;
                break;
            }
        }

        //Load the library
        try {
            InputStream targetStream = new FileInputStream(new File(FilePath.LP+library+".json"));
            JSONTokener jsonTokener = new JSONTokener(targetStream);
            JSONArray array = new JSONArray(jsonTokener);
            String url[] = new String[array.length()];

            for(int i = 0; i < array.length(); i++){
                JSONObject track = array.getJSONObject(i);
                url[i] = "https://www.youtube.com/watch?v="+track.getString("id");
            }

            return url;
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        return null;
    }
    
    public static String getLocalLibrary() throws IOException
    {
        String libraries= "";
        try {
            InputStream targetStream = new FileInputStream(new File(FilePath.LP_List));
            JSONTokener jsonTokener = new JSONTokener(targetStream);
            JSONArray array = new JSONArray(jsonTokener);

            for(int i = 0; i < array.length(); i++){
                JSONObject library = array.getJSONObject(i);
                libraries += i==array.length()-1 ? library.getString("name") : library.getString("name")+", ";
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        return libraries;
    }

}
    
