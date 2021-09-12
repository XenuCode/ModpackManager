package sample;

import com.fasterxml.jackson.databind.ObjectMapper;
import sample.datamodels.jsonObjects.minecraftLauncher.FullConfig;
import sample.datamodels.jsonObjects.minecraftLauncher.LauncherProfiles;

import java.io.IOException;
import java.nio.file.Paths;

public class MinecraftLauncherProfileController {
    public static void addNewProfile(String name,String lastVersionId,String gameDir,String javaArgs,String profileType,String profileKey) {
        try {
            // create object mapper instance
            ObjectMapper mapper = new ObjectMapper();

            // convert JSON string to Book object
            try {
                FullConfig config = mapper.readValue(Paths.get(System.getenv("APPDATA")+"/.minecraft/launcher_profiles.json").toFile(), FullConfig.class);
                LauncherProfiles profile = new LauncherProfiles();
                profile.name=name;
                profile.lastVersionId=lastVersionId;
                profile.gameDir=gameDir;
                profile.javaArgs=javaArgs;
                profile.type=profileType;
                config.profiles.put(profileKey,profile);

                mapper.writeValue(Paths.get(System.getenv("APPDATA")+"/.minecraft/launcher_profiles.json").toFile(), config);
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