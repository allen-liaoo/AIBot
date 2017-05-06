package utility

import com.mashape.unirest.http.Unirest
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * Created by liaoyilin on 4/29/17.
 */
class LyricsSearch {

    static String[] getSongLyrics(){
        Document doc = Jsoup.connect("https://genius.com/Charlie-puth-attention-lyrics").get()
        String html = doc.html()


        UtilBot.setUnirestCookie()
        System.out.println(Unirest.get("https://genius.com/Charlie-puth-attention-lyrics")
                .header("User-Agent", "AIBot").getBody())

        System.out.println(html.find("(?<=<p>)(?s)(.*?)(?=<\\/p>)")?.replaceAll("(?s)<.*?>", "").replaceAll("\\n","\n"))
        return html.find("(?<=<p>)(?s)(.*?)(?=<\\/p>)")?.replaceAll("(?s)<.*?>", "").split("\n")
    }
}
