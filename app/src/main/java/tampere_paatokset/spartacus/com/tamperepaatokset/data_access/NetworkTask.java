package tampere_paatokset.spartacus.com.tamperepaatokset.data_access;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Eetu on 2.10.2015.
 */
public class NetworkTask extends AsyncTask<String, Void, Void> {

    DataAccess.NetworkListener listener_;

    String response = null;
    private DataAccess.NetworkListener.RequestType type_;

    public void setNetworkListener(DataAccess.NetworkListener listener){

        listener_ = listener;
    }

    @Override
    protected Void doInBackground(String... urls){

        if(listener_ == null){
            Log.e("NetworkTask", "No listener was set!");
            response = null;
        }

        // params comes from the execute() call: params[0] is the url.
        try {
            if (urls[0].equals("POST")) {
                response = HttpURLConnectionHandler.sendPost(urls);
                APICache.store(urls[0], response);
            } else if (urls[0].equals("GET")) {
                response = HttpURLConnectionHandler.sendGet(urls[1]);
                APICache.store(urls[0], response);
            }

        } catch (Exception e) {

            response = null;
        }

        return null;
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(Void v) {

        //Log.i("NetworkTask", "onPostExecute");
        listener_.DataAvailable(response, type_);
    }

}
