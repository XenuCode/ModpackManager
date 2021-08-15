package sample.languageControll;

import com.fasterxml.jackson.databind.ObjectMapper;
import sample.datamodels.jsonObjects.minecraftLauncher.FullConfig;
import sample.datamodels.jsonObjects.minecraftLauncher.localisation.Language;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LanguageController {

    String pathToLanguageFolder;
    public void GetTranslationsFromServer()
    {
        File pathToTranslationsFolder=new File(pathToLanguageFolder);
    }
    public LanguageController(String pathToLanguageFolder)
    {
        this.pathToLanguageFolder = pathToLanguageFolder;
        File directory = new File(pathToLanguageFolder);
        directory.mkdirs();
    }
    public void GetListOfLanguagesAvailable() throws IOException {
        File languageFolder = new File(pathToLanguageFolder);
        File[] translationFiles= languageFolder.listFiles();
        System.out.println("Language files found:");
        for (File file:translationFiles) {
            System.out.println(file.getName());
        }

        ObjectMapper mapper = new ObjectMapper();
        HashMap<String,Language> languages = new HashMap<>();
        for (File translation:translationFiles) {
            languages.put(mapper.readValue(Paths.get(translation.toString()).toFile(), Language.class).language,mapper.readValue(Paths.get(translation.toString()).toFile(), Language.class));
        }
        System.out.println(languages.get("ENG").modpackName);
    }
    public void changeLanguage(String LanguageCode){

    }
}
