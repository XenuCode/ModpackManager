package openmanager.modpackInsider;

import com.moandjiezana.toml.Toml;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import openmanager.datamodels.other.ModData;

import java.io.File;

public class ModAnalyzer {

    public static void analyzeMod(String modpath){
        extractSingleModData(modpath);
        ModData modData = new ModData();
        Toml tomlj = new Toml().read(new File(System.getenv("APPDATA")+"/"+"OpenManager"+"/"+"temp"+"/"+"META-INF"+"/"+"mods.toml"));
        modData.setModName(tomlj.getTables("mods").get(0).getString("displayName"));
        modData.setAuthors(tomlj.getTables("mods").get(0).getString("authors"));
        modData.setDescription(tomlj.getTables("mods").get(0).getString("description"));
        modData.setSiteURL(tomlj.getTables("mods").get(0).getString("displayURL"));
        new File(System.getenv("APPDATA")+"/"+"OpenManager"+"/"+"temp"+"/"+"META-INF"+"/"+"mods.toml").delete();
        System.out.println(modData.getModName());
    }

    private static void extractSingleModData(String modpath){
        File file = new File(System.getenv("APPDATA")+"/"+"OpenManager"+"/"+"temp");
        if(file.isDirectory())
        {
            try {
                new ZipFile(modpath).extractFile("META-INF/mods.toml",System.getenv("APPDATA")+"/"+"OpenManager"+"/"+"temp");
            } catch (ZipException e) {
                e.printStackTrace();
            }
        }
        else
            file.mkdirs();



    }

}
