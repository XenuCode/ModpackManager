package sample.languageControll;

import java.io.File;

public class LanguageController {

    String pathToLanguageFolder;
    public void GetTranslationsFromServer()
    {
        File pathToTranslationsFolder=new File(System.getenv("APPDATA")+"\\ModpackManager");
    }
    public void GetListOfLanguagesAvailable()
    {

    }
}
