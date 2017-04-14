/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Utility;

import com.mashape.unirest.http.Unirest;
import java.awt.Color;
import java.util.Random;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class UtilTool {

    /**
     * Convert bytes value to human readable form.
     * See: http://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java
     * @param bytes for the bytes value to be converted
     * @param si Choose between SI or Binary
     * @return
     */
    public static String convertBytes(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) {
            return bytes + " B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static Color setColor() {
        Random colorpicker = new Random();
        int red;
        int green;
        int blue;
        red = colorpicker.nextInt(255) + 1;
        green = colorpicker.nextInt(255) + 1;
        blue = colorpicker.nextInt(255) + 1;
        return new Color(red, green, blue);
    }
    
    /**
     * Return a string of formatted duration in Hour/Min/Sec format
     * @param duration
     * @return
     */
    public static String formatDuration(Long duration)
    {
        String formatted = "";
        if(duration != null)
        {
            String hours = Long.toString(duration/3600000), 
                    minutes = Long.toString((duration/60000)%60), 
                    seconds = Long.toString((duration/1000)%60);
            
            if(Integer.parseInt(hours) < 10){
                hours = "0" + hours + ":";
            }
            if(Integer.parseInt(minutes) < 10){
                minutes = "0" + minutes + ":";
            }
            if(Integer.parseInt(seconds) < 10){
                seconds = "0" + seconds;
            }
            
            if("00:".equals(hours))
                hours = "";
            
            formatted = hours + minutes + seconds;
        }
        return formatted;
    }
    
}
