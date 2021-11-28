package openmanager;

import javafx.scene.control.ProgressBar;
import openmanager.datamodels.other.ModpackData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class HttpStuff {
    public static void downloadModpackImage(ModpackData nowModpack)
    {
        String APPLICATION_DATA_PATH;
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

        ReadableByteChannel readableByteChannel = null;
        try {
            File directory = new File(APPLICATION_DATA_PATH+"/OpenManager/Modpacks/"+nowModpack.UUID+"/images/background.png");
            if(!directory.isFile()){
                File directory2 = new File(APPLICATION_DATA_PATH+"/OpenManager/Modpacks/"+nowModpack.UUID+"/images/");
                directory2.mkdirs();
                System.out.println("Downloading");
                readableByteChannel = Channels.newChannel(new URL(nowModpack.imageLink).openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(APPLICATION_DATA_PATH+"/OpenManager/Modpacks/"+nowModpack.UUID+"/images/background.png");
                FileChannel fileChannel = fileOutputStream.getChannel();
                fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
                System.out.println("Done !");
            }
            System.out.println("Found file no need to download");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void downloadModpackFilesHTTP(String downloadPath, URL modpackURL, ProgressBar bar, ModpackData currentModpack)
    {

        try {
            URLConnection con = modpackURL.openConnection();
            int fileSize = con.getContentLength();
            ReadableByteChannel rbc = null;

            rbc = Channels.newChannel(con.getInputStream());

            ReadableConsumerByteChannel rcbc = new ReadableConsumerByteChannel(rbc,(b)->{
                System.out.println("Read  "+b +"/"+fileSize +"   ||   "+ (float) b/fileSize);
                bar.setProgress((float) b/fileSize);
            });
            FileOutputStream fos = new FileOutputStream(downloadPath+"/"+currentModpack.fileName);
            fos.getChannel().transferFrom(rcbc, 0, Long.MAX_VALUE);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static int getRemoteFileSize(URL fileURL)
    {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) fileURL.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int contentLength = connection.getContentLength();
        return contentLength;
    }

}