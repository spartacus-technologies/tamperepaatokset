package tampere_paatokset.spartacus.com.tamperepaatokset;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import tampere_paatokset.spartacus.com.tamperepaatokset.data_access.DataAccess;

/**
 * Created by jani on 7.11.15.
 */

class URLHandler extends WebViewClient implements DataAccess.NetworkListener {

    private WebView webView_;
    public DataAccess.NetworkListener netWorkListener_;


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        /*
        if (shouldKeepInWebView(url)) {
            view.loadUrl(url);
        }
        else {
            Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            view.getContext().startActivity(i);
        }
    */

        webView_ = view;

        DataAccess.requestData(netWorkListener_, url, DataAccess.NetworkListener.RequestType.IMAGE);









        return(true);
    }

    private boolean shouldKeepInWebView(String url) {
        return(true); // or false, or use regex, or whatever
    }

    @Override
    public void DataAvailable(String data, RequestType type) {

    }

    @Override
    public void BinaryDataAvailable(Object data, RequestType type) {

    }
}
