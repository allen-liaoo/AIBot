/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package audio;

import constants.FilePath;
import org.json.JSONException;
import org.json.JSONTokener;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * loadFm -> loadLibrary(ArrayList of libraries) -> Add fmSongs to Songs[] -> Generate random fmSongs
 * https://TEMP.discord.fm/libraries/json
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class FM {
    
    public final static String FM_base_url = "https://temp.discord.fm";
    public final static String FM_libraries_url = FM_base_url + "/libraries/json";
    public final static String FM_library_url = FM_base_url + "/libraries/?/json";

    private List<PlayList> discordfm;
    private ArrayList<PlayList> local;

    public FM() {
        discordfm = new ArrayList<>();
        local = new ArrayList<>();
    }

    public void loadDiscordFM()
    {
        try {
            JSONArray array = Unirest
                    .get(FM_libraries_url)
                    .header("Content-Type", "application/json")
                    .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:53.0) Gecko/20100101 Firefox/53.0")
                    .asJson().getBody().getArray();

            /* Get specific songs from libraries in Discord FM */
            for(int i = 0; i < array.length(); i++)
            {
                JSONObject lib = array.getJSONObject(i);
                String name = lib.get("name").toString();

                /* Get songs in Discord FM Libraries */
                JSONArray array2 = Unirest
                        .get(FM_library_url.replace("?", lib.get("id").toString()))
                        .header("Content-Type", "application/json")
                        .asJson().getBody().getArray();

                List<String> songs = new ArrayList<>();
                for(int j = 0; j < array2.length(); j ++) {
                    JSONObject jo = array2.getJSONObject(i);
                    songs.add("https://www.youtube.com/watch?v=" + jo.get("identifier").toString());
                }

                discordfm.add(new PlayList(name, songs));
            }

        } catch (UnirestException | JSONException ex) {
            System.out.println("Exception while loading Discord FM: " + ex.getMessage());
        }

    }

    public void loadLocalLibraries()
    {
        try {
            InputStream targetStream = new FileInputStream(new File(FilePath.LP_List));
            JSONTokener jsonTokener = new JSONTokener(targetStream);
            JSONArray array = new JSONArray(jsonTokener);

            /* Get specific songs from Local Play Lists */
            for(int i = 0; i < array.length(); i++) {
                JSONObject lib = array.getJSONObject(i);
                String name = lib.get("name").toString();

                /* Get songs in play lists */
                InputStream stream = new FileInputStream(new File(FilePath.LP + name + ".json"));
                JSONTokener tokener = new JSONTokener(stream);
                JSONArray array2 = new JSONArray(tokener);

                List<String> songs = new ArrayList<>();
                for(int j = 0; j < array2.length(); j ++) {
                    JSONObject jo = array2.getJSONObject(j);
                    songs.add("https://www.youtube.com/watch?v=" + jo.get("id").toString());
                }
                local.add(new PlayList(name, songs));
            }

        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }

    }

    public PlayList getSongs(String keyword) {
        for(PlayList pl : discordfm) {
            if(pl.getName().toLowerCase().contains(keyword.toLowerCase()))
                return pl;
        }
        for(PlayList pl2 : local) {
            if(pl2.getName().toLowerCase().contains(keyword.toLowerCase()))
                return pl2;
        }
        return null;
    }

    public List<PlayList> getDiscordFM() {
        return discordfm;
    }

    public List<PlayList> getLocalLibraries() {
        return local;
    }

    public class PlayList
    {
        private final String name;
        private List<String> songs;

        public PlayList(String name, List<String> songs) {
            this.name = name;
            this.songs = songs;
        }

        public String getName() {
            return name;
        }

        public List<String> getSongs() {
            return songs;
        }

        public void setSongs(List<String> songs) {
            this.songs = songs;
        }
    }

}
    
