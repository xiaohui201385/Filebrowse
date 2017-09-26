package org.filebrowse.service;

import java.util.List;

import org.filebrowse.dao.FileTypeDao;
import org.filebrowse.entity.FileType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileTypeService {

    @Autowired
    FileTypeDao dao;
    
    public List<FileType> getByName(String type){
        return dao.getByName(type);
    }
    
    public List<FileType> getAll(){
        return dao.getAll();
    }
    
    public List<FileType> getById(Integer id){
        return dao.getById(id);
    }
    
    public int delAll(){
        return dao.delAll();
    }
    
    public int addOne(FileType fileType){
        return dao.addOne(fileType);
    }
    
    public void addList(List<FileType> fileTypes) {
        dao.addList(fileTypes);
    }
    
    public int addOneDefault(FileType fileType){
        return dao.addOneDefault(fileType);
    }
    
    public int delByName(String name){
        return dao.delByName(name);
    }
}
