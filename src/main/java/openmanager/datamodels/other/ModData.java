package openmanager.datamodels.other;

public class ModData {
    private String modName,siteURL,description,authors;

    public ModData(){
    }

    public String getAuthors() {
        return authors;
    }

    public String getDescription() {
        return description;
    }

    public String getModName() {
        return modName;
    }

    public String getSiteURL() {
        return siteURL;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setModName(String modName) {
        this.modName = modName;
    }

    public void setSiteURL(String siteURL) {
        this.siteURL = siteURL;
    }
}
