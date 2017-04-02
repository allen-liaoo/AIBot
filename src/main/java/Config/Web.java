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
    
    public static void searchSite(String customsite, String num, String input, MessageReceivedEvent e) throws UnsupportedEncodingException, IOException {
        
        String google = "http://www.google.com/search?q=";
        String charset = "UTF-8";
        String userAgent = "DiscordBot";
        
        Elements links = Jsoup.connect(google+ URLEncoder.encode(input,charset)+ num + customsite).timeout(0).userAgent(userAgent).get().select(".g>.r>a");
        
        String tempSite = "";
        if(!"".equals(customsite))
            tempSite = "on `" + customsite.substring(15);
        else 
            tempSite = "via `Google Search Engine";
        
        final String tempString = Emoji.search + " This is the result for `" + input + "` " + tempSite + "`:";
            
        e.getChannel().sendMessage("Searching........").complete().editMessage(tempString).complete();
        
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
            
            final String resultTemp = "**" + title + "**\n" + urlLink;
            
            e.getChannel().sendMessage(resultTemp).queue();
            
            count++;
        }
        
        System.out.println("** Web --> Done : "+ count +" results");
    }
    
    public static void searchImage(String customsite, String num, String input, MessageReceivedEvent e) throws UnsupportedEncodingException, IOException {
        
        String google = "http://www.google.com/search?q=";
        String charset = "UTF-8";
        String userAgent = "DiscordBot";
        
        Elements links = Jsoup.connect(google+ URLEncoder.encode(input,charset)+ num + customsite).timeout(0).userAgent(userAgent).get().select(".g>.r>a");
            
        e.getChannel().sendMessage("Searching........").complete().editMessage(Emoji.search + " Image!").complete();
        
        for( Element link : links )
        {
            String url = link.absUrl("href");
            
            //decode link from link of google.
            String urlLink = URLDecoder.decode(url.substring(url.indexOf("=")+1 , url.indexOf("&")) , charset);
            
            if( ! urlLink.startsWith("http") )
            {
                System.out.println("Not url: "+urlLink);
                continue;    //ads/news etc
            }
            
            final String resultTemp = urlLink;
            
            e.getChannel().sendMessage(resultTemp).queue();
            
            count++;
        }
        
        System.out.println("** Web --> Done : "+ count +" results");
    }
}
