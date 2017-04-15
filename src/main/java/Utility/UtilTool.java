/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Utility;

import java.util.concurrent.TimeUnit;
import java.awt.Color;
import java.util.Random;

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

    /**
     * Generate Random Color
     * @return Color
     */
    public static Color randomColor() {
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
        TimeUnit u = TimeUnit.MILLISECONDS;
        long hours = u.toHours(duration) % 24;
        long minutes = u.toMinutes(duration) % 60;
        long seconds = u.toSeconds(duration) % 60;

        if (hours > 0)
          return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        else
          return String.format("%02d:%02d", minutes, seconds);
    }
    
}
