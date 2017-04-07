/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Audio;

import java.io.IOException;
 
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
 
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class Lyrics {
   private final static String songLyricsURL = "https://genius.com/Christina-perri-arms-lyrics";
 
   public static String getSongLyrics( String band, String songTitle) throws IOException 
   {
        List<String> lyrics= new ArrayList<String>();
 
        Document doc = Jsoup.connect(songLyricsURL).get();
        String title = doc.title();
        System.out.println(title);
        String lyricText = "";
        int count = 0;
        Element p = doc.select(".lyrics").select("p").get(0);
        doc.select("br").append("");
        doc.select("p").prepend("\\n\\n");
        
        for (Node e : p.childNodes()) 
        {
            if(e.hasAttr("data-id"))
            {
                Element el = p.select("[data-id]").get(count);
                for (Node en : el.childNodes()) 
                {
                    //System.out.println(en.toString());
                    lyrics.add(en.toString());
                    if (e instanceof TextNode) {
                        lyrics.add(((TextNode)e).getWholeText());
                        //System.out.println(((TextNode)en).getWholeText());
                    }
                }
                count ++;
            }
            
            if (e instanceof TextNode) {
                lyrics.add(((TextNode)e).getWholeText());
                //System.out.println(((TextNode)e).getWholeText());
            }
        }
        
        for(String s : lyrics)
        {
            if(s.equals("<br>"))
                continue;
            lyricText += s + "\n";
        }
        //lyricText.replaceAll("\n", "");
        System.out.println(lyricText);
        
     return title;
   }
 
   public static void main(String[] args) throws IOException {
      System.out.println(Lyrics.getSongLyrics("U2", "With or Without You"));
    }
}

