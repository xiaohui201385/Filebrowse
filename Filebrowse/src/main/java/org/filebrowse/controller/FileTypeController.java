package org.filebrowse.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.filebrowse.entity.FileType;
import org.filebrowse.entity.PreviewFile;
import org.filebrowse.service.FileTypeService;
import org.filebrowse.service.PreviewFileService;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileTypeController {

    
    @Autowired
    FileTypeService fileTypeService;
    
    @Autowired
    PreviewFileService previewFileService;
    
    @RequestMapping("/types")
    @ResponseBody
    public List<FileType> getAll(){
        List<FileType> all = fileTypeService.getAll();
        return all; 
    }
    
    @RequestMapping("/types-after")
    @ResponseBody
    public List<FileType> getAfterAdd(@RequestParam(value="typeName",required=true)String typeName ){
        List<FileType> byName = fileTypeService.getByName(typeName);
        if(byName==null||byName.size()<1){
            
            File file=new File("C:/Program Files/Microsoft Office Web Apps/OpenFromUrlWeb/docview/"+typeName+"/");
            file.mkdirs();
        }
        long start=new Date().getTime();
        List<FileType> byNewName=null;
        while (true) {
        	byNewName = fileTypeService.getByName(typeName);
        	if (byNewName!=null&&byNewName.size()>0) {
				break;
			}
			long end = new Date().getTime();
			long last=end-start;
			if(last>=20000){
				break;
			}
		}
       if (byNewName==null) {
		return null;
       }
        return fileTypeService.getAll();
    }
    
}
