package tampere_paatokset.spartacus.com.tamperepaatokset.data_access;

import android.os.Build;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Eetu on 1.10.2015.
 */
public class DataAccess {

    static Executor task_executor = null;

    //Registers listener and executes GET Method. After completion executes listeners callback function.
    public static void requestData(NetworkListener listener, String path, NetworkListener.RequestType type) {

        if(task_executor == null){

            //task_executor = Executors.newCachedThreadPool();
            task_executor = Executors.newFixedThreadPool(30);
        }

        Log.i("DataAccess:requestData", "path=" + path);

        NetworkTask task = new NetworkTask();
        task.setNetworkListener(listener);

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
            task.executeOnExecutor(task_executor, "GET", path);
        } else {
            task.execute(path);
        }

        //task.execute(path);
    }

    //Registers listener and executes GET Method. After completion executes listeners callback function.
    public static void requestMeetingData(NetworkListener listener, String date1, String date2, String organization) {

        String path = "http://ktweb.tampere.fi/ktwebbin/dbisa.dll/ktwebscr/epj_kokl.htm";

        if(task_executor == null){
            //task_executor = Executors.newCachedThreadPool();
            task_executor = Executors.newFixedThreadPool(30);
        }

        Log.i("DataAccess:requestData", date1 + date2 + organization);

        NetworkTask task = new NetworkTask();
        task.setNetworkListener(listener);

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
            task.executeOnExecutor(task_executor, "POST", path, date1, date2, organization);
        } else {
            task.execute(path, date1, date2, organization);
        }

        //task.execute(path);
    }


    //Registers listener and executes GET Method. After completion executes listeners callback function.
    public static void requestImageData(NetworkListener listener, String path, NetworkListener.RequestType type) {


        Log.i("DataAccess:requestData", "path=" + path);

        ImageDownloadTask task = new ImageDownloadTask();
        task.setNetworkListener(listener, type);

        task.execute(path);
    }

    //private static List<NetworkListener> listeners;

    public interface NetworkListener{

        enum RequestType{

            MEETING,
            VIDEO,
            AGENDAS,
            VIDEO_PREVIEW,
            AGENDA_ITEM,
            POLICY_MAKERS,
            POLICY_MAKER, IMAGE
        }

        void DataAvailable(String data, RequestType type);
        void BinaryDataAvailable(Object data, RequestType type);
    }
    /*
    //Adds new listener to array
    void registerListener(NetworkListener listener){

        if(listeners == null){

            listeners = new ArrayList<NetworkListener>();
        }
        listeners.add(listener);
    }
    */

    //http://dev.hel.fi/paatokset/v1/agenda_item/
    //Function for testing REST API Connectivity
    public static boolean testConnection(NetworkListener listener){

        NetworkTask task = new NetworkTask();
        task.setNetworkListener(listener);

        task.execute("http://dev.hel.fi/paatokset/v1/meeting/");
        //task.execute("http://dev.hel.fi/paatokset/v1/agenda_item/");
        /*
        try{

            String response = HttpURLConnectionHandler.sendGet("http://dev.hel.fi/paatokset/v1/agenda_item/");

            return true;
        }catch (Exception e){


        }
        */
        return false;
    }

}
