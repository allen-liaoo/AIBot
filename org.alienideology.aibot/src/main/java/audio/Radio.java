/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package audio;

import constants.FilePath;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class Radio {

    private List<RadioStation> radioStations;

    public Radio() {
        radioStations = new ArrayList<>();
    }

    /**
     * Load Radio Stations into a list of RadioStation from json file
     */
    public void loadRadioStations()
    {
        LinkedHashMap<String, Object> map = null;
        try {
            InputStream targetStream = new FileInputStream(new File(FilePath.RadioStation));
            JSONTokener jsonTokener = new JSONTokener(targetStream);
            JSONObject json = new JSONObject(jsonTokener);
            map = new LinkedHashMap<>(json.toMap());
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }

        ArrayList<String> names = new ArrayList<>(map.keySet());
        ArrayList<Object> urls = new ArrayList<>();
        urls.addAll(map.values());

        for(int i = 0; i < map.size(); i++) {
            radioStations.add(new RadioStation(names.get(i), urls.get(i).toString()));
        }
        radioStations.sort(Comparator.comparing(RadioStation::getName));
    }

    /**
     * Get a url from keyword
     * @param keyword
     * @return
     */
    public String getUrl(String keyword) {
        Optional<RadioStation> ifAny = radioStations.stream().filter(rs -> rs.getName().toLowerCase().contains(keyword.toLowerCase()))
                .findAny();
        return ifAny.map(RadioStation::getUrl).orElse(null);
    }

    public List<RadioStation> getRadioStations() {
        return radioStations;
    }

    public class RadioStation
    {
        private final String name;
        private final String url;

        public RadioStation(String name, String url) {
            this.name = name;
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }
    }
    
}
