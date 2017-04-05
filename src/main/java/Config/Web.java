/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

/**
 *
 * @author liaoyilin
 */
public class Web {
    
    private String defnum = "&num=1";
    private String site = "&as_sitesearch=";
    private static int count = 0;
    private final static String songLyrics = "http://www.genius.com/";
    
    public static List<SearchResult> search(String customsite, String num, String input) throws UnsupportedEncodingException, IOException 
    {
        List<SearchResult> results = new ArrayList<SearchResult>();
        
        String google = "http://www.google.com/search?q=";
        String charset = "UTF-8";
        String userAgent = "DiscordBot";
        
        Elements links = Jsoup.connect(google+ URLEncoder.encode(input,charset)+ num + customsite).timeout(0).userAgent(userAgent).get().select(".g>.r>a");
        
        for( Element link : links )
        {
            String title = link.text();
            String url = link.absUrl("href");
            
            //decode link from link of google.
            String urlLink = URLDecoder.decode(url.substring(url.indexOf("=")+1 , url.indexOf("&")) , charset);
            
            if( ! urlLink.startsWith("http") )
            {
                System.out.println("Not url: "+urlLink);
                continue;    //ads/news etc
            }
            results.add(new SearchResult(title, null, urlLink, null));
            count++;
        }
        
        System.out.println("** Web Search --> " + input + " : " + count +" results");
        return results;
    }
    /*
    public static String getLyrics(String customsite, String input)
    {
        Document doc = Jsoup.connect(songLyrics+ "/"+band.replace(" ", "-").toLowerCase()+"/"+songTitle.replace(" ", "-").toLowerCase()+"-lyrics/").get();
        String title = doc.title();
    }*/
}
