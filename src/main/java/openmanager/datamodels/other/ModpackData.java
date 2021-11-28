package openmanager.datamodels.other;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class ModpackData {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public String name,description,directLink,version,fileName,imageLink,minecraftVersion,UUID;
    public int id;
    //public String[] imageLinks;
    public int downloads;
    @JsonIgnore
    public java.sql.Timestamp lastUpdated; //I need to map it in Jackson
    int phraseMach;
    public ModpackData() {

    }
    public ModpackData(String name, String description, String version, String directLink, String UUID,String fileName,String imageLink,int downloads, java.sql.Timestamp lastUpdated,String minecraftVersion)
    {
        this.name = name;
        this.description = description;
        this.directLink = directLink;
        this.version = version;
        this.UUID = String.valueOf(id);
        this.fileName = fileName;
        this.imageLink =imageLink;
        this.downloads=downloads;
        //this.lastUpdated = lastUpdated;

        this.minecraftVersion = minecraftVersion;
    }


    public void setPhraseMach(int phraseMach) {
        this.phraseMach = phraseMach;
    }

    public int getPhraseMach()
    {
        return this.phraseMach;
    }
}