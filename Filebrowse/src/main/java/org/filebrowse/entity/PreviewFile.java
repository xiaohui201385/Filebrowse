package org.filebrowse.entity;

import java.util.Date;

public class PreviewFile {
    private Integer id;

    private String fileName;

    private Date createTime;

    private String location;

    private Integer level;

    private Long clicknum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getClicknum() {
        return clicknum;
    }

    public void setClicknum(Long clicknum) {
        this.clicknum = clicknum;
    }
    
    public PreviewFile() {
		// TODO Auto-generated constructor stub
	}

	public PreviewFile(String fileName, Date createTime, String location, Integer level, Long clicknum) {
		super();
		this.fileName = fileName;
		this.createTime = createTime;
		this.location = location;
		this.level = level;
		this.clicknum = clicknum;
	}
    
    
}