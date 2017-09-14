package org.filebrowse.service;

import java.util.List;

import org.filebrowse.entity.PageMessage;
import org.springframework.stereotype.Service;

@Service
public class PageService <T>{

    private PageMessage<T> pageInfo=new PageMessage<>();
    
    public PageService(){
    }
    
    public PageMessage<T> getPageInfo() {
		return pageInfo;
	}
    
    public void startPage(int pageNum,int pageSize,List<T> list){
        int fromIndex=(pageNum-1)*pageSize;
        if(fromIndex>list.size()) {
        	fromIndex=list.size();
        }
        int toIndex=fromIndex+pageSize;
        if(toIndex>list.size()){
        	toIndex=list.size();
        }
        List<T> result = list.subList(fromIndex, toIndex);
        int pages=0;
        if(list.size()%pageSize==0){
            pages=list.size()/pageSize;
        }else{
            pages=list.size()/pageSize+1;
        }
        pageInfo.setPageNum(pageNum);
        pageInfo.setPages(pages);
        pageInfo.setList(result);
    }
}

