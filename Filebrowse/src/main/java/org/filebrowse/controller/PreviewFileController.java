package org.filebrowse.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.filebrowse.entity.PreviewFile;
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
	
	@RequestMapping("/index")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/")
	public String home(){
		return "index";
	}

	@RequestMapping("/preview-fileupload")
	public String fileUpload(@RequestParam(value = "file", required = true) MultipartFile file,
			@RequestParam(value = "filegrade", required = false) String levelStr,HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("UTF-8");
		System.out.println(file.getOriginalFilename());
		System.out.println(levelStr);
		InputStream is = file.getInputStream();
		// 上传文件所处的路径
		String location="C:/PreviewFile/"+file.getOriginalFilename();
		System.out.println(location);
		File tempFile = new File(location);
		if(!tempFile.getParentFile().exists()){
			if(!tempFile.getParentFile().mkdirs()){
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

		Integer level = Integer.parseInt(levelStr);
		PreviewFile previewFile = new PreviewFile(file.getOriginalFilename(), new Date(), tempFile.getAbsolutePath(),
				level, new Long(0));
		
		previewFileService.insertOne(previewFile);
		response.getWriter().println("<script>alert('上传成功');window.location.href='index';</script>");
		return "index";
	}
	
	@ResponseBody
	@RequestMapping("/showAll")
	public List<PreviewFile> getAll(){
		return previewFileService.getAllByOrder();
	}
	
	
	@ResponseBody
	@RequestMapping("/queryLike")
	public List<PreviewFile> queryLike(@RequestParam("string")String string){
		System.out.println(string);
		return previewFileService.getListBySearch(string);
	}
	
	@ResponseBody
	@RequestMapping("/clickUpdate")
	public int updateClick(@RequestParam("num")Integer num , @RequestParam("id")Integer id){
		return previewFileService.updateClickNum((num+1), id);
	}
}
