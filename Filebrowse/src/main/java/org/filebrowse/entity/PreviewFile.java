package org.filebrowse.entity;

import java.util.Date;

public class PreviewFile {
    private Integer id;

    private String fileName;

    private Date createTime;

    private String location;
    
    private int type;
    
    private String typeName;
    
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    
    public String getTypeName() {
        return typeName;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public int getType() {
        return type;
    }
    

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

    
    public PreviewFile() {
		// TODO Auto-generated constructor stub
	}

	public PreviewFile(String fileName, Date createTime, String location,int type) {
		super();
		this.fileName = fileName;
		this.createTime = createTime;
		this.location = location;
		this.type=type;
	}
    
    
}