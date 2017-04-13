/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Audio;

import Resource.FilePath;
import com.moandjiezana.toml.Toml;
import java.io.File;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class Radio {
    public static void loadRadio(String input)
    {
        loadStation(input);
    }
    
    public static void loadStation(String input)
    {
        Toml toml = new Toml().read(new File(FilePath.RadioStations));
        String url = toml.getString("Drive Radio");
        System.out.println(url);
    }
}
