package org.filebrowse.dao;

import static org.mockito.Matchers.intThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.filebrowse.entity.FileType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


@Repository
public class FileTypeDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private final static RowMapper<FileType> rowMapper = new BeanPropertyRowMapper<FileType>(FileType.class);

    public List<FileType> getByName(String type) {
        String sql = "select * from file_type where name = ?";
        return jdbcTemplate.query(sql, rowMapper, type);
    }
    
    public List<FileType> getAll(){
        String sql = "select * from file_type";
        return jdbcTemplate.query(sql, rowMapper);
    }
    
    public List<FileType> getById(Integer id){
        String sql="select * from file_type where id=?";
        return jdbcTemplate.query(sql, rowMapper, id);
    }
    
    public int delAll(){
        String sql="delete from file_type";
        return jdbcTemplate.update(sql);
    }
    
    public int addOne(FileType fileType){
        String sql="insert into file_type(id,name) values(?,?)";
        return jdbcTemplate.update(sql, fileType.getId(),fileType.getName());
    }
    
    public int addOneDefault(FileType fileType){
        String sql="insert into file_type(name) values(?)";
        return jdbcTemplate.update(sql,fileType.getName());
    }
    
    public void addList(List<FileType> fileTypes){
        String sql="insert into file_type(id,name) values(?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                int id = fileTypes.get(i).getId();
                String name=fileTypes.get(i).getName();
                ps.setInt(1, id);
                ps.setString(2, name);
            }
            
            @Override
            public int getBatchSize() {
                return fileTypes.size();
            }
        });
    }
    
    public int delByName(String name){
        String sql="delete from file_type where name=?";
        return jdbcTemplate.update(sql, name);
    }
    
}
