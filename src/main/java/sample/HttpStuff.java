package sample;

import javafx.scene.control.ProgressBar;
import sample.datamodels.other.ModpackData;

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
        ReadableByteChannel readableByteChannel = null;
        try {
            File directory = new File(System.getenv("APPDATA")+"/ModpackManager/Modpacks/"+nowModpack.id+"/images/background.png");
            if(!directory.isFile()){
                File directory2 = new File(System.getenv("APPDATA")+"/ModpackManager/Modpacks/"+nowModpack.id+"/images/");
                directory2.mkdirs();
                System.out.println("Downloading");
                readableByteChannel = Channels.newChannel(new URL(nowModpack.imageLink).openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(System.getenv("APPDATA")+"/ModpackManager/Modpacks/"+nowModpack.id+"/images/background.png");
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