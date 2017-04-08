/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resource;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

/**
 *
 * @author liaoyilin
 */
public class Search {
    
    private static String defnum = "&num=";
    private static String defsite = "&as_sitesearch=";
    private static int count = 0;
    private final static String songLyrics = "http://www.genius.com/";
    
    public static List<SearchResult> search(String customsite, String num, String input) throws UnsupportedEncodingException, IOException 
    {
        List<SearchResult> results = new ArrayList<SearchResult>();
        
        String google = "http://www.google.com/search?q=";
        String charset = "UTF-8";
        String userAgent = "DiscordBot";
        
        Elements links = Jsoup.connect(google+ URLEncoder.encode(input,charset)+ defnum + num + customsite)
                .timeout(0)
                .userAgent(userAgent)
                .get()
                .select(".g>.r>a");
        
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
            results.add(new SearchResult(title, null, urlLink, null, null));
            count++;
        }
        
        System.out.println("** Web Search --> " + input + " : " + count +" results");
        return results;
    }
    
    public static List<SearchResult> youtubeSearch(String num, String input) throws UnsupportedEncodingException, IOException 
    {
        List<SearchResult> results = new ArrayList<SearchResult>();
        
        String ytsite = "https://www.youtube.com/results?search_query=";
        Document doc = Jsoup.connect(ytsite + input).timeout(0).get();
        String title = doc.title();
        
        Elements p = doc.select("div#results").select("ol.item-section").select("li");
        
        for( Element n : p )
        {
            Elements block = n.select("div.yt-lockup-content");
            String urltitle = block.select(".yt-lockup-title").select(".yt-uix-tile-link").text();
            String url = "https://www.youtube.com" + block.select(".yt-lockup-title").select("a").attr("href");
            String author = block.select(".yt-lockup-byline").select("a").text();
            String text = block.select(".yt-lockup-description").text();
            
            results.add(new SearchResult(urltitle, author, url, text, null));
            count++;
        }
        
        System.out.println("** YouTube Search --> " + input + " : " + count +" results");
        return results;
    }
    
    public static List<SearchResult> lyricsSearch(String input) throws UnsupportedEncodingException, IOException 
    {
        List<SearchResult> results = new ArrayList<SearchResult>();
        
        String lyricsite = "https://genius.com/search?q=" + input.replaceAll(" ", "-");
        Document doc = Jsoup.connect(lyricsite).timeout(0).get();
        String title = doc.title();
        
        Elements p = doc.select("div.search_results_container").get(0).select("ul.search_results").select("li");
        for( Element n : p )
        {
            String urltitle = n.select("span.song_title").text();
            String url = n.select("a.song_link").attr("href");
            String author = n.select(".artist_name").text();
            
            results.add(new SearchResult(urltitle, author, url, null, null));
            count++;
        }
        
        System.out.println("** Lyrics Search --> " + input + " : " + count +" results");
        return results;
    }
}
