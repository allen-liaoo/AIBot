/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Utility;

import java.util.List;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class AIPages<T> {
    
    private List<T> list;
    private int pageSize, pages;
    
    public AIPages(List<T> list, int pageSize) {
        this.list = list;
        this.pageSize = pageSize;
        calculatePages();
    }
    
    private void calculatePages() {
        if(list.size() % pageSize == 0) {
            pages = list.size() / pageSize;
        } else  {
            pages = list.size() / pageSize + 1;
        }
    }
    
    public List getPage(int page) {
        int endPage = pageSize * page;
        
        return list.subList(pageSize * (page-1), endPage > list.size() ? list.size() : endPage);
    }

    public List getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
