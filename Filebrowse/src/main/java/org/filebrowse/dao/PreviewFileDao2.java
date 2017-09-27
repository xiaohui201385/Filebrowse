package org.filebrowse.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.filebrowse.db.JDBCTools;
import org.filebrowse.db.JdbcDaoImpl;
import org.filebrowse.entity.PreviewFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;


public class PreviewFileDao2 extends JdbcDaoImpl<PreviewFile>{

	
	public int insertOne(PreviewFile file){
		String sql="insert into PreviewFile(file_name,create_time,location,type) values(?,?,?,?)";
		Connection connection=JDBCTools.getConnection();
		return update(connection, sql, file.getFileName(),file.getCreateTime(),file.getLocation(),file.getType());
	}
	
	public List<PreviewFile> getAllByOrder(){
		
		String sql="select * from PreviewFile order by create_time desc";
		Connection connection=JDBCTools.getConnection();
		return getForList(connection, sql);
	}
	
	public List<PreviewFile> getListByType(int type){
	    String sql = "select * from PreviewFile where type=? order by create_time desc";
	    Connection connection=JDBCTools.getConnection();
	    return getForList(connection, sql, type);
	}
	
	public List<PreviewFile> getByNameAndDate(String name,Date date){
	    System.out.println(date.toString());
	    String sql = "select * from PreviewFile where file_name=? and create_time=?";
	    Connection connection=JDBCTools.getConnection();
	    return getForList(connection, sql, name,date);
	}
	
	public List<PreviewFile> getByLocation(String location){
		String sql="select * from PreviewFile where location = ?";
		Connection connection=JDBCTools.getConnection();
		return getForList(connection, sql, location);
	}
	
	public int updateByLocation(String location,Date date){
		String sql="update PreviewFile set create_time = ? where location = ?";
		Connection connection=JDBCTools.getConnection();
		return update(connection, sql, date,location);
	}
	
	public List<PreviewFile> getByNameLike(String str){
	    String param="%"+str+"%";
	    System.out.println(param);
	    String sql="select * from PreviewFile where file_name like ? order by create_time desc";
	    Connection connection=JDBCTools.getConnection();
	    return getForList(connection, sql, param);
	}
	
	public int delByType(List<Integer> typeId){
	    String sql="delete from PreviewFile where type not in (?)";
	    Connection connection=JDBCTools.getConnection();
	    return update(connection, sql, typeId);
	}
	
	public int delAll(){
	    String sql="delete from PreviewFile";
	    Connection connection=JDBCTools.getConnection();
	    return update(connection, sql);
	}
	
	public void addList(List<PreviewFile> previewFiles){
	    String sql="insert into PreviewFile(file_name,create_time,location,type) values(?,?,?,?)";
	    Connection connection=JDBCTools.getConnection();
	    for(PreviewFile previewFile:previewFiles){
	        update(connection, sql, previewFile.getFileName(),previewFile.getCreateTime(),previewFile.getLocation(),previewFile.getType());
	    }
	}
	
	public int delByLocation(String location){
	    String sql="delete from PreviewFile where location=?";
	    Connection connection=JDBCTools.getConnection();
	    return update(connection, sql, location);
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
	
	
	
}
