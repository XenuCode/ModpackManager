package sample.datamodels.jsonObjects.minecraftLauncher;

import java.util.Map;

public class FullConfig {
    public String clientToken;
    public LauncherVersion launcherVersion;
    public Map<String,LauncherProfiles> profiles;
    public LauncherSettings settings;
}
