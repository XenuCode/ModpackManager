package openmanager.repo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import openmanager.datamodels.other.ModpackData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.List;

public class RepoControl extends Thread {

    private static String APPLICATION_DATA_PATH;
    public List<ModpackData> modpacks =null;
    HashMap<String,ModpackData> modpackHashMap = new HashMap<>();

    public RepoControl(){
        if(System.getenv("HOME")==null)
        {
            APPLICATION_DATA_PATH= System.getenv("APPDATA");
            System.out.println("detected system: windows" + System.getenv("APPDATA"));
        }
        else
        {
            APPLICATION_DATA_PATH= System.getenv("HOME");
            System.out.println("detected system: linux" + System.getenv("HOME"));
        }
    }
    // this is sick :D
    /**
     * returns nothing, downloads and then loads up repo data from local and public repository and does it all asynchronously
     */
    public void asyncGetModpackRepo()
    {
        new Thread(new Runnable() {
            public void run()
            {
                System.out.println("STARTED");
                getModpackRepo();
                System.out.println("FINISHED DOWNLOADING");
                loadFromPublicRepo();
                //loadFromLocalRepo();
                System.out.println("closing popup");
            }
        }).start();
    }


    public  boolean getModpackRepo()
    {
        ReadableByteChannel readableByteChannel;
        try {
            System.out.println("WHAT");
            readableByteChannel = Channels.newChannel(new URL("http://www.wholesomecraft.pl/api/getrepo.php").openStream());
            File directory = new File(APPLICATION_DATA_PATH+"/OpenManager/public/repo");
            if(!directory.isDirectory())
            {
                directory.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(APPLICATION_DATA_PATH+"/OpenManager/public/repo.json");
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    private static boolean getModpackRepo(int offset,int limit)
    {
        ReadableByteChannel readableByteChannel;
        try {
            readableByteChannel = Channels.newChannel(new URL("http://www.wholesomecraft.pl/api/getrepo.php?limit="+limit+"&offset="+offset).openStream());
            File directory = new File(APPLICATION_DATA_PATH+"/OpenManager/repo");
            if(!directory.isDirectory())
            {
                directory.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(APPLICATION_DATA_PATH+"/OpenManager/repo"+"/repo.json");
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    private static boolean getModpackRepo(int limit)
    {
        ReadableByteChannel readableByteChannel;
        try {
            readableByteChannel = Channels.newChannel(new URL("http://www.wholesomecraft.pl/api/getrepo.php?limit="+limit).openStream());
            File directory = new File(APPLICATION_DATA_PATH+"/OpenManager/repo");
            if(!directory.isDirectory())
            {
                directory.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(APPLICATION_DATA_PATH+"/OpenManager/repo"+"/repo.json");
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    private boolean loadFromLocalRepo()
    {
        ObjectMapper deserializerLol =new ObjectMapper().configure(DeserializationFeature.WRAP_EXCEPTIONS, false);
        File file = new File(APPLICATION_DATA_PATH+"/OpenManager/local/repo.json");
        try {
            modpacks.addAll(deserializerLol.readValue(file, new TypeReference<List<ModpackData>>(){}));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean loadFromPublicRepo()
    {
        try {
            ObjectMapper deserializerLol =new ObjectMapper().configure(DeserializationFeature.WRAP_EXCEPTIONS, false);
            File file = new File(APPLICATION_DATA_PATH+"/OpenManager/public/repo.json");
            modpacks = deserializerLol.readValue(file, new TypeReference<List<ModpackData>>(){});
            System.out.println(modpacks.get(0).name);
        } catch (IOException e) {
            System.out.println("NOT GOOD");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
