package org.filebrowse.dao;

import static org.mockito.Matchers.intThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.filebrowse.db.JDBCTools;
import org.filebrowse.db.JdbcDaoImpl;
import org.filebrowse.entity.FileType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


public class FileTypeDao2 extends JdbcDaoImpl<FileType>{



    public List<FileType> getByName(String type) {
        String sql = "select * from file_type where name = ?";
        Connection connection=JDBCTools.getConnection();
        return getForList(connection, sql, type);
    }
    
    public List<FileType> getAll(){
        String sql = "select * from file_type";
        Connection connection=JDBCTools.getConnection();
        return getForList(connection, sql);
    }
    
    public List<FileType> getById(Integer id){
        String sql="select * from file_type where id=?";
        Connection connection=JDBCTools.getConnection();
        return getForList(connection, sql, id);
    }
    
    public int delAll(){
        String sql="delete from file_type";
        Connection connection=JDBCTools.getConnection();
        return update(connection, sql);
    }
    
    public int addOne(FileType fileType){
        String sql="insert into file_type(id,name) values(?,?)";
        Connection connection=JDBCTools.getConnection();
        return update(connection, sql, fileType.getId(),fileType.getName());
    }
    
    public int addOneDefault(FileType fileType){
        String sql="insert into file_type(name) values(?)";
        Connection connection=JDBCTools.getConnection();
        return update(connection, sql, fileType.getName());
    }
    
    public void addList(List<FileType> fileTypes){
        String sql="insert into file_type(id,name) values(?,?)";
        Connection connection=JDBCTools.getConnection();
        for(FileType fileType:fileTypes){
            update(connection, sql, fileType.getId(),fileType.getName());
        }
        
    }
    
    public int delByName(String name){
        String sql="delete from file_type where name=?";
        Connection connection=JDBCTools.getConnection();
        return update(connection, sql, name);
    }
    
}
