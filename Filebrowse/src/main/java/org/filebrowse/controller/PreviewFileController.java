package org.filebrowse.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.filebrowse.entity.FileType;
import org.filebrowse.entity.PreviewFile;
import org.filebrowse.service.FileTypeService;
import org.filebrowse.service.PreviewFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping("/")
    public String home() {
        return "index";
    }

    @ResponseBody
    @RequestMapping("/typeList")
    public List<PreviewFile> getListByType(@RequestParam(value = "type", required = false) String type) {
        if (type == null) {
            type = "通知";
        }
        FileType byName = fileTypeService.getByName(type);
        List<PreviewFile> listByType = previewFileService.getListByType(byName.getId());
        return listByType;
    }

    @RequestMapping("/preview-fileupload")
    public String fileUpload(@RequestParam(value = "file", required = true) MultipartFile file,
            @RequestParam(value = "type", required = true) String type, HttpServletResponse response)
            throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("UTF-8");
        System.out.println(file.getOriginalFilename());
        InputStream is = file.getInputStream();
        // 上传文件所处的路径
        String location = "D:/PreviewFile/" + "/" + type + "/" + file.getOriginalFilename();
        System.out.println(location);
        File tempFile = new File(location);
        if (!tempFile.getParentFile().exists()) {
            if (!tempFile.getParentFile().mkdirs()) {
                System.out.println("create dir failue");
            }
        }
        System.out.println(tempFile.getAbsolutePath());
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
        FileType byName = fileTypeService.getByName(type);
        PreviewFile previewFile = new PreviewFile(file.getOriginalFilename(), new Date(), location, byName.getId());
        previewFileService.insertOne(previewFile);
        response.getWriter().println("<script>alert('上传成功');window.location.href='index';</script>");
        return "index";
    }

    @RequestMapping("/downloadFile")
    public String previewDownload(@RequestParam(value = "fileName", required = true) String fileName,
            @RequestParam(value = "createTime", required = true) String createTime,HttpServletResponse response) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            PreviewFile result = previewFileService.getByNameAndDate(fileName, format.parse(createTime));
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

}
