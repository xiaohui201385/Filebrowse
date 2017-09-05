package org.filebrowse.dao;


import java.util.Date;
import java.util.List;

import org.filebrowse.entity.PreviewFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PreviewFileDao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private final static RowMapper<PreviewFile> rowMapper=new BeanPropertyRowMapper<PreviewFile>(PreviewFile.class);
	
	public int insertOne(PreviewFile file){
		String sql="insert into PreviewFile(file_name,create_time,location,level,clickNum) values(?,?,?,?,?)";
		
		return jdbcTemplate.update(sql, file.getFileName(),file.getCreateTime(),file.getLocation(),file.getLevel(),file.getClicknum());
	}
	
	public List<PreviewFile> getAllByOrder(){
		
		String sql="select * from PreviewFile order by create_time desc,clickNum desc,level desc";
		
		return jdbcTemplate.query(sql, rowMapper);
	}
	
//	public List<PreviewFile> getListByName(String name){
//		String sql = "select * from PreviewFile where file_name like '%?%'";
//		return jdbcTemplate.query(sql, rowMapper, name);
//	}
//	
//	public List<PreviewFile> getListByTime(Date time){
//		String sql ="select * from PreviewFile where time like '%?%'";
//		return jdbcTemplate.query(sql, rowMapper, time);
//	}
//	
//	public List<PreviewFile> getListByLevel(Integer level){
//		String sql="select * from PreviewFile where level=?";
//		return jdbcTemplate.query(sql, rowMapper, level);
//	}
	
	public List<PreviewFile> getListBySearch(String str1,String str2,String str3){
		str1="%"+str1+"%";
		System.out.println(str1);
		str2="%"+str2+"%";
		System.out.println(str2);
		String sql="select * from PreviewFile where file_name like ? or create_time like binary ? or level=? order by create_time desc,clickNum desc,level desc";
		return jdbcTemplate.query(sql, rowMapper,str1,str2,str3);
	}
	
	public int updateClickNum(Integer num,Integer id){
		String sql="update PreviewFile set clickNum=? where id=?";
		return jdbcTemplate.update(sql, num,id);
	}
	
	
}
