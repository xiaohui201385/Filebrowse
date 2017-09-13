package org.filebrowse.dao;

import java.util.List;

import org.filebrowse.entity.FileType;
import org.springframework.beans.factory.annotation.Autowired;
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
    
}
