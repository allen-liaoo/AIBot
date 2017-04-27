/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package AISystem;

import java.util.List;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class AIPages<T> {
    
    //A list of generic objects
    private List<T> list;
    
    //pageSize is the size of a page
    //pages is the number of pages
    private int pageSize, pages;
    
    private final int DEFUALT_PAGE_SIZE = 10;
    
    /**
     * Default constructor with known page size
     * @param list
     * @param pageSize
     */
    public AIPages(List<T> list, int pageSize) {
        this.list = list;
        this.pageSize = pageSize;
        calculatePages();
    }
    
    /**
     * Constructor with unknown page size
     * @param list
     */
    public AIPages(List<T> list) {
        this.list = list;
        this.pageSize = 0;
        calculatePageSize();
        calculatePages();
    }
    
    /**
     * Constructor with null values.
     */
    public AIPages() {
        this.list = null;
        this.pageSize = 0;
    }
    
    /**
     * Calculate pages for an unknown list.
     */
    private void calculatePages() {
        if(list == null) return;
        
        if(list.size() % pageSize == 0) {
            pages = list.size() / pageSize;
        } else {
            pages = list.size() / pageSize + 1;
        }
    }
    
    /**
     * Calculate page size for an unknown list.
     */
    private void calculatePageSize() {
        if(list == null) return;
        
        int max = Integer.MIN_VALUE;
        
        for(int i = 1; i <= list.size(); i++) {
            if(list.size() % i > 3) continue;
            if((list.size() % i + i) > DEFUALT_PAGE_SIZE) continue;
            if(i > max) max = i;
        }
        
        pageSize = max;
    }
    
    /**
     * Add an item to this list
     * @param obj the object of this list
     * @return 
     */
    public boolean addItem(T obj) {
        return list.add(obj);
    }
    
    /**
     * Set the page size of this object
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        if(pageSize > list.size())
            this.pageSize = list.size();
        else
            this.pageSize = pageSize;
    }
    
    /**
     * Get the page of a specific index
     * @param page
     * @return
     */
    public List<T> getPage(int page) {
        if(page > pages) throw new IndexOutOfBoundsException("Pages: " + pages + " List Size: " + list.size());
        
        int endPage = pageSize * page;
        return list.subList(pageSize * (page-1), endPage > list.size() ? list.size() : endPage);
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPages() {
        return pages;
    }
}
