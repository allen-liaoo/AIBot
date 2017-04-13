/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Audio;

import Main.Main;
import Resource.Emoji;
import Setting.Prefix;
import Utility.SmartLogger;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * loadFm -> loadLibrary(ArrayList of libraries) -> Add fmSongs to Songs[] -> Generate random fmSongs
 https://temp.discord.fm/libraries/json
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class FM {
    
    public final static String FM_base_url = "https://temp.discord.fm";
    public final static String FM_libraries_url = FM_base_url + "/libraries/json";
    public final static String FM_library_url = FM_base_url + "/libraries/?/json";
    
    public static void loadFm(String input, MessageReceivedEvent e) throws UnirestException
    {
        //Parse JSON string using JSON parser. 
        JSONArray array = loadLibrary(input);
        if(array == null)
        {
            e.getChannel().sendMessage(Emoji.error + " Cannot find playlist called `" + input + "`.").queue();
            return;
        }
        
        Main.guilds.get(e.getGuild().getId()).getScheduler().fmSongs = new String[array.length()];
        for(int i = 0; i < array.length(); i ++)
        {
            JSONObject jo = array.getJSONObject(i);
            
            try {
                Main.guilds.get(e.getGuild().getId()).getScheduler().fmSongs[i] = "https://www.youtube.com/watch?v=" + jo.get("identifier").toString();
            } catch (org.json.JSONException jsex) {
                e.getChannel().sendMessage(Emoji.error + " Playlist not found. \nUse `" + Prefix.DIF_PREFIX + "fm` for available playlists.").queue();
                return;
            }
        }
        AudioConnection.connect(e, false);
        Main.guilds.get(e.getGuild().getId()).getScheduler().autoPlay();
        
        //Log
        SmartLogger.commandLog(e, "FM#loadFM", "Fm loaded");
    }
    
    
    public static JSONArray loadLibrary(String input) throws UnirestException 
    {
        String id = "";
        
        setUnirestCookie();
        
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
        setUnirestCookie();
        
        JSONArray array = Unirest.get(FM_libraries_url).header("User-Agent", "AIBot").asJson().getBody().getArray();
        String[] libs = new String[array.length()];
        
        for(int i = 0; i < array.length(); i++)
        {
            JSONObject lib = array.getJSONObject(i);
            libs[i] = lib.get("name").toString();
        }
        
        return libs;
    }
    
    public static void setUnirestCookie()
    {
        //Set Unirest cookie
        RequestConfig globalConfig = RequestConfig.custom()
        .setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();

        HttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
        Unirest.setHttpClient(httpclient);
    }
}
    
