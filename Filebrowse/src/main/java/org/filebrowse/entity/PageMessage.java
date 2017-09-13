package org.filebrowse.entity;

import java.util.List;

public class PageMessage<T> {
    
    private int pageNum;
    
    private int pages;
    
    private List<T> list;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

	@Override
	public String toString() {
		return "PageMessage [pageNum=" + pageNum + ", pages=" + pages + ", list=" + list + "]";
	}
    
    
}
	