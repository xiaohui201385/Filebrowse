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

	
	public int insertOne(PreviewFile file) throws SQLException{
		String sql="insert into PreviewFile(file_name,create_time,location,type) values(?,?,?,?)";
		Connection connection=JDBCTools.getConnection();
		int update = update(connection, sql, file.getFileName(),file.getCreateTime(),file.getLocation(),file.getType());
		connection.close();
		System.out.println("insert running...");
		return update;
	}
	
	public List<PreviewFile> getAllByOrder() throws SQLException{
		String sql="select * from PreviewFile order by create_time desc";
		Connection connection=JDBCTools.getConnection();
		List<PreviewFile> result = getForList(connection, sql);
		connection.close();
		return result;
	}
	
	public List<PreviewFile> getListByType(int type) throws SQLException{
	    String sql = "select * from PreviewFile where type=? order by create_time desc";
	    Connection connection=JDBCTools.getConnection();
	    List<PreviewFile> result = getForList(connection, sql, type);
	    connection.close();
	    return result;
	}
	
	public List<PreviewFile> getByNameAndDate(String name,Date date) throws SQLException{
	    System.out.println(date.toString());
	    String sql = "select * from PreviewFile where file_name=? and create_time=?";
	    Connection connection=JDBCTools.getConnection();
	    List<PreviewFile> result = getForList(connection, sql, name,date);
	    connection.close();
	    return result;
	}
	
	public List<PreviewFile> getByLocation(String location) throws SQLException{
		String sql="select * from PreviewFile where location = ?";
		Connection connection=JDBCTools.getConnection();
		List<PreviewFile> result = getForList(connection, sql, location);
		connection.close();
		return result;
	}
	
	public int updateByLocation(String location,Date date) throws SQLException{
		String sql="update PreviewFile set create_time = ? where location = ?";
		Connection connection=JDBCTools.getConnection();
		int update = update(connection, sql, date,location);
		connection.close();
		return update;
	}
	
	public List<PreviewFile> getByNameLike(String str) throws SQLException{
	    String param="%"+str+"%";
	    System.out.println(param);
	    String sql="select * from PreviewFile where file_name like ? order by create_time desc";
	    Connection connection=JDBCTools.getConnection();
	    List<PreviewFile> result = getForList(connection, sql, param);
	    connection.close();
	    return result;
	}
	
	public int delByType(List<Integer> typeId) throws SQLException{
	    String sql="delete from PreviewFile where type not in (?)";
	    Connection connection=JDBCTools.getConnection();
	    int update = update(connection, sql, typeId);
	    connection.close();
	    return update;
	}
	
	public int delAll() throws SQLException{
	    String sql="delete from PreviewFile";
	    Connection connection=JDBCTools.getConnection();
	    int update = update(connection, sql);
	    connection.close();
	    return update;
	}
	
	public void addList(List<PreviewFile> previewFiles) throws SQLException{
	    String sql="insert into PreviewFile(file_name,create_time,location,type) values(?,?,?,?)";
	    Connection connection=JDBCTools.getConnection();
	    for(PreviewFile previewFile:previewFiles){
	        update(connection, sql, previewFile.getFileName(),previewFile.getCreateTime(),previewFile.getLocation(),previewFile.getType());
	    }
	    
	    connection.close();
	}
	
	public int delByLocation(String location) throws SQLException{
	    String sql="delete from PreviewFile where location=?";
	    Connection connection=JDBCTools.getConnection();
	    int update = update(connection, sql, location);
	    connection.close();
	    return update;
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
