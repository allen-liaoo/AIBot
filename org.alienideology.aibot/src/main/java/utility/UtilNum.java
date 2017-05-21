/* 
 * AIBot by AlienIdeology
 * 
 * UtilNum
 * All utilities required for this bot
 */
package utility;

import java.io.*;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class UtilNum {
    
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
     * Check if a number is between a range
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

    public static int getLineCount(String filepath) throws IOException {
        int lines = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(filepath)));
            while (reader.readLine() != null) { lines ++; }
            reader.close();
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw new IOException();
        }
        return lines;
    }
}
