package org.filebrowse.service;

import java.util.Date;
import java.util.List;

import org.filebrowse.dao.PreviewFileDao;
import org.filebrowse.entity.PreviewFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PreviewFileService {

	@Autowired
	PreviewFileDao previewFileDao;
	
	public int insertOne(PreviewFile file){
		return previewFileDao.insertOne(file);
	}
	
	public List<PreviewFile> getAllByOrder(){
		return previewFileDao.getAllByOrder();
	}
	
	public List<PreviewFile> getListByType(int type){
	    return previewFileDao.getListByType(type);
	}
	
	public List<PreviewFile> getByNameAndDate(String name,Date date){
	    return previewFileDao.getByNameAndDate(name, date);
	}
	
	public List<PreviewFile> getByLocation(String location){
		return previewFileDao.getByLocation(location);
	}
	
	public int updateByLocation(String location,Date date){
		return previewFileDao.updateByLocation(location, date);
	}
	
	public List<PreviewFile> getByNameLike(String string){
	    return previewFileDao.getByNameLike(string);
	}

//	public List<PreviewFile> getListByName(String name){
//		return previewFileDao.getListByName(name);
//	}
//	
//	public List<PreviewFile> getListByTime(Date time)
//	{
//		return previewFileDao.getListByTime(time);
//	}
//	
//	public List<PreviewFile> getListByLevel(Integer level){
//		return previewFileDao.getListByLevel(level);
//	}
	
}
