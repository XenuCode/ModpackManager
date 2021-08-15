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
    public static float getAverageMatch(List<ModpackData> modpacks)
    {
        int sum=0;
        for (ModpackData modpack:modpacks) {
        sum=+modpack.getPhraseMach();
        }
        sum/=(float)modpacks.size();
        return sum;
    }
}
