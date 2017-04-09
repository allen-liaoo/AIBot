/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Connection.Response;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class WebScraper {
    
    /**
     * Lyrics getter from Genius.com
     * @param input the value of lyrics URI, works with Search.lyricsSearch
     * @throws IOException
     * @throws HttpStatusException
     */
    public static String[] getSongLyrics(String input) throws IOException, HttpStatusException {
        List<String> lyrics = new ArrayList<String>();
        String lyricsURL = Info.LYRICSURL + input.substring(0, 1).toUpperCase() + input.substring(1).replace(" ", "-").toLowerCase();
        Document doc = Jsoup.connect(input).get();
        int count = 0;
        Element p = doc.select(".lyrics").select("p").get(0);
        doc.select("br").append("");
        for (Node e : p.childNodes()) {
            if (e.hasAttr("data-id")) {
                Element el = p.select("[data-id]").get(count);
                for (Node en : el.childNodes()) {
                    lyrics.add(en.toString());
                    if (e instanceof TextNode) {
                        lyrics.add(((TextNode) e).getWholeText());
                    }
                }
                count++;
            }
            if (e instanceof TextNode) {
                lyrics.add(((TextNode) e).getWholeText());
            }
        }
        //Cleanup lyrics
        String[] lyricsText = new String[lyrics.size()];
        for (int i = 0; i < lyricsText.length; i++) {
            //Ignore line breaks
            if (lyrics.get(i).equals("<br>")) {
                lyricsText[i] = "\n";
            } else {
                lyricsText[i] = lyrics.get(i);
            }
            //Delete <i> / </i> nodes, multiple lines breaks
            lyricsText[i] = lyricsText[i].replaceAll("<i>", "");
            lyricsText[i] = lyricsText[i].replaceAll("</i>", "");
            //lyricsText[i] = lyricsText[i].replaceAll("[\r\n]+", "\n");
            //Align the texts by deleting " " infront of the line, if any
            if (lyricsText[i].startsWith(" ")) {
                lyricsText[i] = lyricsText[i].replaceFirst(" ", "");
            }
        }
        return lyricsText;
    }
    
    /**
     * Thumbnail getter from IMDb
     * @param result the value of SearchResult to add thumbnail
     * @throws IOException
     */
    public static void getIMDbThumbNail(SearchResult result) throws IOException {
        Document doc = Jsoup.connect(result.getLink()).timeout(0).get();
        String pic = doc.select("div#main_top>.title-overview>div.heroic-overview>div.vital>div.slate_wrapper>div.poster").select("a>img").attr("src");
        //Change getter for special thumbnails
        if ("".equals(pic)) {
            try {
                pic = doc.select("div#content-2-wide").get(0).select("div#main_top").get(0).select(".title-overview").get(0).select(".heroic-overview").get(0).select(".minPosterWithPlotSummaryHeight").get(0).select("div.poster").select("a").select("img").attr("src");
            } catch (IndexOutOfBoundsException ioobe) {
                //Initialize pic direcctly if there is no thumbnail for the result
                pic = "http://ia.media-imdb.com/images/G/01/imdb/images/nopicture/32x44/film-3119741174._CB522736599_.png";
            }
        }
        result.setThumbnail(pic);
    }    
}
