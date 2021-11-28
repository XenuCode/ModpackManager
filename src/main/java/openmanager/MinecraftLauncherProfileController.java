package openmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import openmanager.datamodels.jsonObjects.minecraftLauncher.FullConfig;
import openmanager.datamodels.jsonObjects.minecraftLauncher.LauncherProfiles;

import java.io.IOException;
import java.nio.file.Paths;

public class MinecraftLauncherProfileController {
    private static String APPLICATION_DATA_PATH;
    public MinecraftLauncherProfileController(){
        if(System.getenv("HOME")==null)
        {
            APPLICATION_DATA_PATH= System.getenv("APPDATA");
            System.out.println("detected system: windows" + APPLICATION_DATA_PATH);
        }
        else
        {
            APPLICATION_DATA_PATH= System.getenv("HOME");
            System.out.println("detected system: linux" + System.getenv("HOME"));
        }
    }
    public static void addNewProfile(String name,String minecraftVersion,String gameDir,String javaArgs,String profileType,String profileKey) {
        try {
            // create object mapper instance
            ObjectMapper mapper = new ObjectMapper();

            // convert JSON string to FullConfig object
            try {
                FullConfig config = mapper.readValue(Paths.get(APPLICATION_DATA_PATH+"/.minecraft/launcher_profiles.json").toFile(), FullConfig.class);
                LauncherProfiles profile = new LauncherProfiles();
                profile.name=name;
                profile.lastVersionId=minecraftVersion;
                profile.gameDir=gameDir;
                profile.javaArgs=javaArgs;
                profile.type=profileType;
                config.profiles.put(profileKey,profile);

                mapper.writeValue(Paths.get(APPLICATION_DATA_PATH+"/.minecraft/launcher_profiles.json").toFile(), config);
                System.out.println();
            } catch (IOException e) {
                e.printStackTrace();
            }


            System.out.println("ez");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}