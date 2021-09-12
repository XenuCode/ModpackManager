package sample;

import javafx.collections.ObservableList;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import sample.datamodels.other.ModpackData;
import java.util.Comparator;
import java.util.List;

public class SearchControl {
    public static List<ModpackData> sortData(List<ModpackData> modpacks, boolean ascending)
    {
        if(ascending)
        {
            modpacks.sort(Comparator.comparing(ModpackData::getPhraseMach).reversed());
        }else
        {
            modpacks.sort(Comparator.comparing(ModpackData::getPhraseMach));
        }
        return modpacks;
    }
    public static List<ModpackData> calculatePhraseMatch(List<ModpackData> modpacks,String phrase)
    {
        for (ModpackData modpack:modpacks) {
            modpack.setPhraseMach(FuzzySearch.weightedRatio(modpack.name,phrase));
        }
        return modpacks;
    }
    private static float getAverageMatch(List<ModpackData> modpacks)
    {
        float sum=0;
        for (ModpackData modpack:modpacks) {
        sum=+modpack.getPhraseMach();
        }
        sum/=(float)modpacks.size();
        return sum*10;
    }
    public static ObservableList<ModpackData> getObservableList(ObservableList<ModpackData> list, List<ModpackData> modpacks)
    {
        float averageMatch= SearchControl.getAverageMatch(modpacks); //heck yeah, calling this method will be just prefect once repo gets bigger, like 2K + or something... poor cpu
        System.out.println("avarage: "+averageMatch);                            //I will definitely need to make it threaded and maybe use some other algorithm for this
        list.clear();
        for (int i = 0; i <modpacks.size(); i++) {
            if(i>=10 || modpacks.get(i).getPhraseMach()<= 40)
            {
                break;
            }
            list.add(modpacks.get(i));
        }
        return list;
    }
}
