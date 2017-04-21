/* 
 * AIBot by AlienIdeology
 * 
 * UtilNum
 * All utilities required for this bot
 */
package Utility;

import java.awt.Color;
import java.util.Random;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class UtilNum {

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
     * Check if a number is between an range
     * @param x the value of x
     * @param lower the value of lower
     * @param upper the value of upper
     * @return the boolean
     */
    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    /**
     * Check if a String is an integer
     * @param s the String to be check
     * @return
     */
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }    
}
