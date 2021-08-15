package sample;

import sample.datamodels.other.ModData;

import java.io.File;
import java.io.IOException;

public class GetModInfo {
    public ModData GetSingleModInfo(String fileToRead) throws IOException {
        ModData mod = new ModData();

        //System.out.println(result.getString("description"));
        //System.out.println(result.contains("version"));
        File file=new File("C:\\Users\\xenu\\Desktop\\mods.toml");

        return mod;


    }
}
