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

/**
 *
 * @author liaoyilin
 */
public class Web {
    
    private String defnum = "&num=1";
    private String site = "&as_sitesearch=";
    private static int count = 0;
    
    public static List<SearchResult> searchSite(String customsite, String num, String input, MessageReceivedEvent e) throws UnsupportedEncodingException, IOException 
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
            results.add(new SearchResult(title, urlLink, null, null));
            count++;
        }
        
        System.out.println("** Web Search --> " + input + " : " + count +" results");
        return results;
    }
}
