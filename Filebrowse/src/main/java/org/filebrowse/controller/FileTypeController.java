package org.filebrowse.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.filebrowse.entity.FileType;
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
        File[] typeList = getTypeList();
        previewFileService.delAll();
        fileTypeService.delAll();
        initType(typeList);
        List<FileType> byName = fileTypeService.getByName(typeName);
        if(byName==null||byName.size()<1){
            File file=new File("C:/Program Files/Microsoft Office Web Apps/OpenFromUrlWeb/docview/"+typeName+"/");
            file.mkdirs();
        }
        return fileTypeService.getAll();
    }
    
    
    private List<FileType> initType(File[] listFiles) {
        List<FileType> fileTypes=new ArrayList<>();
        if(listFiles.length>0){
            for(int i=0;i<listFiles.length;i++){
                File listFile=listFiles[i];
                String name=listFile.getName();
                FileType fileType=new FileType(i+1, name);
                fileTypes.add(fileType);
            }
        }
        fileTypeService.addList(fileTypes);
        return fileTypes;
    }
    
    private File[] getTypeList(){
        File file=new File("C:/Program Files/Microsoft Office Web Apps/OpenFromUrlWeb/docview/");
        if(!file.exists()){
            file.mkdirs();
        }
        File[] listFiles = file.listFiles();
        return listFiles;
    }
}
