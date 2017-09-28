package org.filebrowse.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.filebrowse.entity.FileType;
import org.filebrowse.entity.PageMessage;
import org.filebrowse.entity.PreviewFile;
import org.filebrowse.service.FileTypeService;
import org.filebrowse.service.PageService;
import org.filebrowse.service.PreviewFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PreviewFileController {

    @Autowired
    PreviewFileService previewFileService;

    @Autowired
    FileTypeService fileTypeService;

    @RequestMapping("/index")
    public String index() {
        
        return "index";
    }
    
    @RequestMapping("/index2")
    public String index2() {
        
        return "index2";
    }

    @RequestMapping("/")
    public String home() {
        
        return "index";
    }

    
    
    

    @ResponseBody
    @RequestMapping(value="/typeList",method=RequestMethod.POST)
    public PageMessage<PreviewFile> getListByType(@RequestParam(value = "type", required = false) String type,@RequestParam(value="pageNum",required=false)Integer pn
    		,HttpServletResponse response) throws IOException {
        if (type == null) {
            List<FileType> all = fileTypeService.getAll();
            if(all!=null&all.size()>0){
                type = fileTypeService.getAll().get(0).getName();
                
            }
            else{
                type="error";
            }
        }
        
        if (fileTypeService.getByName(type).size()<1) {
        	return null;
		}
        FileType byName = new FileType(1, null);
        if(fileTypeService.getByName(type)!=null&&fileTypeService.getByName(type).size()>0){
            byName=fileTypeService.getByName(type).get(0);
        }
        if(pn==null){
            pn=1;
        }
        List<PreviewFile> listByType = previewFileService.getListByType(byName.getId());
        PageService<PreviewFile> pageService=new PageService<>();
        pageService.startPage(pn, 20, listByType);
        PageMessage<PreviewFile> pageInfo = pageService.getPageInfo();
        return pageInfo;
    }
    
    @ResponseBody
    @RequestMapping(value="/search",method=RequestMethod.POST)
    public PageMessage<PreviewFile> getListBySearchLike(@RequestParam("string")String string,@RequestParam(value="pageNum",required=false)Integer pn){
    	if(pn==null){
            pn=1;
        }
    	char[] strChar=string.toCharArray();
    	List<PreviewFile> byNameLike=new ArrayList<>();
    	for(int i=0;i<strChar.length;i++){
    	    List<PreviewFile> list = previewFileService.getByNameLike(String.valueOf(strChar[i]));
    	    for(PreviewFile pf:list){
    	        if(!byNameLike.contains(pf)){
    	            byNameLike.add(pf);
    	        }
    	    }
    	}
        for(PreviewFile file:byNameLike){
            List<FileType> byId = fileTypeService.getById(file.getType());
            file.setTypeName(byId.get(0).getName());
        }
        PageService<PreviewFile> pageService=new PageService<>();
        pageService.startPage(pn, 20, byNameLike);
        PageMessage<PreviewFile> pageInfo = pageService.getPageInfo();
        return pageInfo;
    }

    
    @RequestMapping("/preview-fileupload")
    public String fileUpload(@RequestParam(value = "file", required = true) MultipartFile file,
            @RequestParam(value = "type", required = true) String type, HttpServletResponse response)
            throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("UTF-8");
        String tempStr=file.getOriginalFilename();
        String[] split = tempStr.split("\\\\");
        String fileTempName=split[split.length-1];
        String hz=fileTempName.substring(fileTempName.lastIndexOf("."));
        if(hz.contains("wps")) {
        	hz=".doc";
        }
        String fn=fileTempName.substring(0, fileTempName.lastIndexOf("."));
        fileTempName=fn+hz;
        InputStream is = file.getInputStream();
        // 上传文件所处的路径
        String location = "C:/Program Files/Microsoft Office Web Apps/OpenFromUrlWeb/docview/" +  type + "/" + fileTempName;
        
        File tempFile = new File(location);
        if (!tempFile.getParentFile().exists()) {
            if (!tempFile.getParentFile().mkdirs()) {
                System.out.println("create dir failue");
            }
        }
        if (!tempFile.exists()) {
            tempFile.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(tempFile);
        byte[] buffer = new byte[512];
        int byteToRead = -1;
        while ((byteToRead = is.read(buffer)) != -1) {
            out.write(buffer, 0, byteToRead);
        }

        is.close();
        out.close();
        long start=new Date().getTime();
        List<PreviewFile> byLocation=null;
        while (true) {
        	 byLocation = previewFileService.getByLocation(location);
        	 if (byLocation!=null&&byLocation.size()>0) {
				break;
			}
			long end = new Date().getTime();
			long last=end-start;
			if(last>=20000){
				break;
			}
		}
        
        
        if (byLocation==null) {
        	response.getWriter().println("<script>alert('上传失败');window.location.href='index';</script>");
            return "index";
		}else {
			response.getWriter().println("<script>alert('上传成功');window.location.href='index';</script>");
	        return "index";
		}
    }
    @ResponseBody
    @RequestMapping(value="/downloadFile",method=RequestMethod.POST)
    public String previewDownload(@RequestParam(value = "fileName", required = true) String fileName,
            @RequestParam(value = "id", required = true) int id,HttpServletResponse response) {
    	
        try {
        	List<PreviewFile> previewName = previewFileService.getByNameAndDate(fileName, id);
            
        	if (previewName==null ||previewName.size()<1) {
        		response.getWriter().println("<script>alert('not file foud');window.location.href='index';</script>");
    		}else {
    			PreviewFile result = previewName.get(0);
                String realPath=result.getLocation();
                File file = new File(realPath);
                FileInputStream fis=new FileInputStream(file);
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/force-download");
                response.addHeader("Content-Disposition",
                      "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
                ServletOutputStream sos = response.getOutputStream();
                byte[] buffer=new byte[1024];
                int len=-1;
                while((len=fis.read(buffer))!=-1){
                    sos.write(buffer, 0, len);
                }
                sos.close();
                fis.close();
    		}
        	
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

}
