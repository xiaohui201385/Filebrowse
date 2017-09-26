package org.filebrowse.entity;

public class FileType {

    private Integer id;
    
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileType(Integer id, String name) {
        super();
        this.id = id;
        this.name = name;
    }
    
    public FileType(String name){
        super();
        this.name=name;
    }
    
    public FileType() {
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public boolean equals(Object obj) {
        FileType fileType=(FileType) obj;
        if(this.name.equals(fileType.getName())){
            return true;
        }else{
            return false;
        }
    }
}
