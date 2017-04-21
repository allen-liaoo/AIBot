/* 
 * AIBot by AlienIdeology
 * 
 * WebScraper
 * Jsoup web scraping for lyrics and IMDb informations
 */
package Utility;

import Resource.Emoji;
import Resource.Constants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.core.EmbedBuilder;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

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
        String lyricsURL = Constants.LYRICSURL + input.substring(0, 1).toUpperCase() + input.substring(1).replace(" ", "-").toLowerCase();
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
     * Get specific informations from a IMDb Title Link.
     * @param result from Search#IMDbSearch, only used the link.
     * @return EmbedBuilder
     * @throws IOException
     */
    public static EmbedBuilder getIMDbInfo(SearchResult result) throws IOException {
        Document doc = Jsoup.connect(result.getLink()).timeout(0).get();
        
        //Title, Content Rating, Duration, Genre, Release Date
        Elements info = doc.select("div#main_top>.title-overview>div.heroic-overview>div.vital")
                .select("div.title_block>div.title_bar_wrapper");
        
        String title = info.select("h1").get(0).text();
        Elements sub = info.select("div.subtext");
        String contentRating = sub.text().split(" | ")[0];
        String duration = sub.select("time").text();
        String genre = sub.select(".itemprop").text().replaceAll(" ", ", ");
        String releaseDate = sub.select("a[title=\"See more release dates\"]").text();
        //System.out.println(title + "\n" + contentRating + "\t" + duration + "\t" + genre + "\t" + releaseDate);
        
        //Plot, Crew Credit
        Elements plot = doc.select("div#main_top>.title-overview>div.heroic-overview>div.plot_summary_wrapper");
        
        String summary = plot.select("div.summary_text").text();
        String director = plot.select("span[itemprop=director]").text();
        String stars = plot.select("span[itemprop=actors").text();
        //System.out.println(summary + "\n" + director + "\t" + stars);
        
        //Ratings
        Elements rate = doc.select("div#main_top>.title-overview>div.heroic-overview>div.vital")
                .select("div.title_block>div.title_bar_wrapper>div.ratings_wrapper>div.imdbRating");
        
        String rating = "**" + rate.select("div.ratingValue").text() + "**";
        String rates = " | " + rate.select("a").text() + " rates";
        String metaScore = plot.select("div.titleReviewBar>div.titleReviewBarItem>a").text();
        //System.out.println(rating + "\t" + rates + "\t" + metaScore);
        
        //Nominations
        String top = "**" + doc.select("div#main_bottom>div#titleAwardsRanks>strong").text() + "**";
        String middle = " | ";
        String nomination = doc.select("div#main_bottom>div#titleAwardsRanks>span[itemprop=awards]").text(); //doc.select("div#main_bottom>div#titleAwardsRanks").text();
        //System.out.println(top + "\t" + nomination);
        
        //Assign "None" to null datas
        //if("".equals(title))
        if("".equals(summary))
            summary = "None";
        if("".equals(director))
            director = "None";
        if("".equals(stars))
            stars = "None";
        if("".equals(metaScore))
            metaScore = "None";
        if("****".equals(top))
            top = "";
        else
            top += middle;
        if("".equals(nomination))
            nomination = "No nomination or awards.";
        
        //Build MessageEmbed
        EmbedBuilder imdb = new EmbedBuilder();
        imdb.setColor(UtilNum.randomColor());
        imdb.setThumbnail(result.getThumbnail());
        imdb.setAuthor(Emoji.search + " IMDb Search", result.getLink(), null);
        imdb.addField(Emoji.film_projector + " Title", title, true);
        imdb.addField("Rating", contentRating, true);
        imdb.addField(Emoji.film_frames + " Duration", duration, true);
        imdb.addField("Genre", genre, true);
        imdb.addField(Emoji.date + " Release Date", releaseDate, true);
        
        imdb.addField("Director(s)", director, true);
        imdb.addField(Emoji.stars + " Stars", stars, false);
        
        imdb.addField(Emoji.star + "IMDb Rating", rating + rates, true);
        imdb.addField("MetaScore", metaScore, true);
        
        imdb.addField(Emoji.trophy + " Nomination and Awards", top + nomination, true);
        
        imdb.addField(Emoji.book + " Plot", summary, true);
        
        return imdb;
    }
    
    /**
     * IMDb Thumbnail getter
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
    
    /**
     * YouTube Thumbnail Getter
     * @param link
     * @throws IOException
     */
    public static String getYouTubeThumbNail(String link) throws IOException {
        link = link.split("\\?v=")[1];
        link = "http://img.youtube.com/vi/" + link + "/0.jpg";
        Document doc = Jsoup.parse(link);
        
        String img = doc.select("img").attr("src");
        return link;
    }
}
