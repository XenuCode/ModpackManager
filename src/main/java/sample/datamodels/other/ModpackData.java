package sample.datamodels.other;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class ModpackData {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public String name,description,directLink,version,fileName,imageLink;
    //public String[] imageLinks;
    public int id,downloads;
    //public java.sql.Timestamp lastUpdated; //I need to map it in Jackson
    int phraseMach;
    public ModpackData() {

    }
    public ModpackData(String name, String description, String version, String directLink, int id,String fileName,String imageLink,int downloads, java.sql.Timestamp lastUpdated)
    {
        this.name = name;
        this.description = description;
        this.directLink = directLink;
        this.version = version;
        this.id =id;
        this.fileName = fileName;
        this.imageLink =imageLink;
        this.downloads=downloads;
        //this.lastUpdated = lastUpdated;

    }

    public void setPhraseMach(int phraseMach) {
        this.phraseMach = phraseMach;
    }

    public int getPhraseMach()
    {
        return this.phraseMach;
    }
}