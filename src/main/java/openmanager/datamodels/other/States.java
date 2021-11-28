package openmanager.datamodels.other;

public class States {
    String readyState="ready";
    String downloadingState="downloading";
    String decompressionState="decompressing";
    String modsDecompressionState="extracting mods";
    String engineInstallationState="installing engine";
    String backupState="making backup";
    String finishedState="finished";
    String errorState="FATAL ERROR";

    public States(String readyState,String downloadingState, String decompressionState,String modsDecompressionState,String engineInstallationState, String backupState, String  finishedState,String errorState)
    {
        this.readyState=readyState;
        this.downloadingState=downloadingState;
        this.decompressionState=decompressionState;
        this.modsDecompressionState = modsDecompressionState;
        this.engineInstallationState = engineInstallationState;
        this.backupState=backupState;
        this.finishedState=finishedState;
    }
    public String getReadyState()
    {
        return readyState;
    }
    public String getDownloadingState()
    {
        return downloadingState;
    }
    public String getDecompressionState()
    {
        return decompressionState;
    }
    public String getBackupState()
    {
        return backupState;
    }
    public String getFinishedState()
    {
        return finishedState;
    }
    public String getEngineInstallationState()
    {
        return engineInstallationState;
    }
    public String getModsDecompressionState()
    {
        return modsDecompressionState;
    }
    public String getErrorState()
    {
        return errorState;
    }
}
