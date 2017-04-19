/* 
 * AIBot by AlienIdeology
 * 
 * UtilTool
 * All utilities required for this bot
 */
package Utility;

import java.util.concurrent.TimeUnit;
import java.awt.Color;
import java.util.Locale;
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
     * Generate a number between start and end
     * @param start
     * @param end
     * @return
     */
    public static int randomNum(int start, int end) {
        
        if(end < start) {
            int temp = end;
            end = start;
            start = temp;
        }
        
        return (int) Math.floor(Math.random() * (end - start + 1) + start);
    }
    
    /**
     * Generate a number between start and end
     * @param start
     * @param end
     * @return
     */
    public static Long randomNum(Long start, Long end) {
        
        if(end < start) {
            Long temp = end;
            end = start;
            start = temp;
        }
        
        return (long) (Math.random() * (end - start + 1) + start);
    }
    
    /**
     * Capitalize every split tokens. Use capSplits("_", Enum.type) to format Enum
     * @param regex
     * @param input
     * @return
     */
    public static String capSplits(String regex, String input) {
        String[] splitting = input.split(regex);
        String splitted = "";
        for (String s : splitting) {
            splitted += s.substring(0, 1).toUpperCase(Locale.ENGLISH) + s.substring(1).toLowerCase(Locale.ENGLISH) + " ";
        }
        return splitted;
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
    
    /**
     * Return a string of formatted time in "00 Hour, 00 Minutes, 00 Seconds" format
     * @param time
     * @return 
     */
    public static String formatTime(Long time) 
    {
        TimeUnit u = TimeUnit.MILLISECONDS;
        long days = u.toDays(time) % 7;
        long hours = u.toHours(time) % 24;
        long minutes = u.toMinutes(time) % 60;
        long seconds = u.toSeconds(time) % 60;
        String day = "", hour = "", minute = "", second = "";

        if(days > 0)
            day = String.format("%2d day(s), ", days);
        if(hours > 0)
            hour = String.format("%2d hour(s), ", hours);
        if(minutes > 0)
            minute = String.format("%2d minute(s), ", minutes);
        if(seconds > 0)
            second = String.format("%2d second(s)", seconds);
        
        return day + hour + minute + second + " ";
    }
    
}
