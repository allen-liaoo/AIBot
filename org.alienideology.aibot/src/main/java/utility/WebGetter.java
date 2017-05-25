package utility;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author AlienIdeology
 */
public class WebGetter {

    public final static String asciiArtUrl = "http://artii.herokuapp.com/";

    /**
     * Get a string from of a ascii font.
     * @param ascii
     * @param font
     * @return
     */
    public static String getAsciiArt(String ascii, String font) {
        try {
            StringBuilder url = new StringBuilder(asciiArtUrl).append("make").append("?text=").append(ascii.replaceAll(" ", "+"))
                    .append(font==null||font.isEmpty()?"":"&font="+font);
            return Unirest.get(url.toString()).asString().getBody();
        } catch (UnirestException e) {
            return "Fail to get the ascii art.";
        }
    }

    /**
     * Get a list of ascii fonts names.
     * @return
     */
    public static List<String> getAsciiFonts() {
        String url = asciiArtUrl + "fonts_list";
        List<String> fontList = new ArrayList<>();
        try {
            String list = Unirest.get(url).asString().getBody();

            fontList = Arrays.stream(list.split("\n")).collect(Collectors.toList());

        } catch (UnirestException e) {
            e.printStackTrace();
        }

        //fontList.forEach(System.out::println);
        return fontList;
    }

}
