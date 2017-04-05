/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Config;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class SearchResult {
    private String title;
    private String info;
    private String link;
    private String text;
   
    /**
     * Null SearchResult Constructor
     */
    public SearchResult()
    {
        this.title = null;
        this.info = null;
        this.link = null;
        this.text = null;
    }
    
    /**
    * @param title SearchResult title
    * @param link SearchResult link
    * @param info SearchResult description
    * @param text SearchResult text, such as lyrics
    */
    public SearchResult(String title, String link, String info, String text)
    {
        this.title = title;
        this.link = link;
        this.info = info;
        this.text = text;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getInfo() 
    {
        return info;
    }

    public void setInfo(String info) 
    {
        this.info = info;
    }

    public String getLink() 
    {
        return link;
    }

    public void setLink(String link) 
    {
        this.link = link;
    }
}
