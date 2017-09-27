package org.filebrowse.dao;


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



    public List<FileType> getByName(String type) throws SQLException {
        String sql = "select * from file_type where name = ?";
        Connection connection=JDBCTools.getConnection();
        List<FileType> result = getForList(connection, sql, type);
        connection.close();
        return result;
    }
    
    public List<FileType> getAll() throws SQLException{
        String sql = "select * from file_type";
        Connection connection=JDBCTools.getConnection();
        List<FileType> result = getForList(connection, sql);
        connection.close();
        return result;
    }
    
    public List<FileType> getById(Integer id) throws SQLException{
        String sql="select * from file_type where id=?";
        Connection connection=JDBCTools.getConnection();
        List<FileType> result = getForList(connection, sql, id);
        connection.close();
        return result;
    }
    
    public int delAll() throws SQLException{
        String sql="delete from file_type";
        Connection connection=JDBCTools.getConnection();
        int update = update(connection, sql);
        connection.close();
        return update;
    }
    
    public int addOne(FileType fileType) throws SQLException{
        String sql="insert into file_type(id,name) values(?,?)";
        Connection connection=JDBCTools.getConnection();
        int update = update(connection, sql, fileType.getId(),fileType.getName());
        connection.close();
        return update;
    }
    
    public int addOneDefault(FileType fileType) throws SQLException{
        String sql="insert into file_type(name) values(?)";
        Connection connection=JDBCTools.getConnection();
        int update = update(connection, sql, fileType.getName());
        connection.close();
        return update;
    }
    
    public void addList(List<FileType> fileTypes) throws SQLException{
        String sql="insert into file_type(id,name) values(?,?)";
        Connection connection=JDBCTools.getConnection();
        for(FileType fileType:fileTypes){
            update(connection, sql, fileType.getId(),fileType.getName());
        }
        
        connection.close();
    }
    
    public int delByName(String name) throws SQLException{
        String sql="delete from file_type where name=?";
        Connection connection=JDBCTools.getConnection();
        int update = update(connection, sql, name);
        connection.close();
        return update;
    }
    
}
