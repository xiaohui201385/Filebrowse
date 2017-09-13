package org.filebrowse.controller;

import java.util.List;

import org.filebrowse.entity.FileType;
import org.filebrowse.service.FileTypeService;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileTypeController {

    
    @Autowired
    FileTypeService fileTypeService;
    
    @RequestMapping("/types")
    @ResponseBody
    public List<FileType> getAll(){
        List<FileType> all = fileTypeService.getAll();
        return all; 
    }
}
