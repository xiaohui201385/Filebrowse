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
    public JSONArray getAll(){
        JSONArray jsonArray = new JSONArray();
        try {
            List<FileType> all = fileTypeService.getAll();
            for(int i = 0 ; i < all.size();i++){
                jsonArray.put(i, all.get(i).getName());
            }
            return jsonArray;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray; 
    }
}
