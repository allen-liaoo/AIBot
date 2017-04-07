/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resource;

import java.util.List;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class SearchResult {
    private String title;
    private String author;
    private String link;
    private String text;
    private List<String> lyrics;
   
    /**
     * Null SearchResult Constructor
     */
    public SearchResult()
    {
        this.title = null;
        this.author = null;
        this.link = null;
        this.text = null;
    }
    
    /**
    * @param title SearchResult title
    * @param link SearchResult link
    * @param author SearchResult lyrics author/artist
    * @param text SearchResult description
    * @param lyrics SearchResult lyrics
    */
    public SearchResult(String title, String author, String link, String text, List<String> lyrics)
    {
        this.title = title;
        this.author = author;
        this.link = link;
        this.text = text;
        this.lyrics = lyrics;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getAuthor() 
    {
        return author;
    }

    public void setAuthor(String info) 
    {
        this.author = info;
    }

    public String getLink() 
    {
        return link;
    }

    public void setLink(String link) 
    {
        this.link = link;
    }

    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public List<String> getLyrics() {
        return lyrics;
    }

    public void setLyrics(List<String> lyrics) {
        this.lyrics = lyrics;
    }
}
