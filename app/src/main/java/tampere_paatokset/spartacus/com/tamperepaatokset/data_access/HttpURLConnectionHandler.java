package tampere_paatokset.spartacus.com.tamperepaatokset.data_access;

/**
 * Created by Eetu on 2.10.2015.
 * http://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/
 */

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class HttpURLConnectionHandler {

    private final static String USER_AGENT = "Mozilla/5.0";

    private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "windows-1252"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "windows-1252"));
        }

        return result.toString();
    }

    // HTTP GET request
    public static String sendGet(String url) throws Exception {

        //String url = "http://www.google.com/search?q=mkyong";
        Log.i("Getting url", url);
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        //System.out.println(response.toString());

        return response.toString();
    }

    // HTTP GET request
    public static String sendPost(String... urls) throws Exception {

        String param_url = urls[1];
        String param_date1 = urls[2];
        String param_date2 = urls[3];
        String param_organization = urls[4];

        Log.i("Getting url", param_url);


        HashMap<String, String> postDataParams = new HashMap<String, String>(4);
        postDataParams.put("oper", "where");
        postDataParams.put("kirjaamo", param_organization);
        postDataParams.put("spvm1", param_date1);
        postDataParams.put("spvm2", param_date2);


        URL url;
        String response = "";
        try {
            url = new URL(param_url);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "windows-1252"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            byte char1;
            List<Byte> array;

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                List<Byte> data = new ArrayList<>();
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((char1= (byte) br.read()) != -1) {

                    //array = data.getBytes();
                    if(data.size() > 2635){

                        //String tmp = data.substring(data.length() > 11 ? data.length() - 10 : 0, data.length());
                        //array = tmp.getBytes();

                        //Log.i("TAG", "size=" + data.length() + " ascii=" + (char)char1 + " int=" + char1);
                    }

                    data.add(char1);
                }

                response = EncodingFormatter(data);
            }
            else {
                response="";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;




    }

    private static String EncodingFormatter(List<Byte> data) {

        String response = "";
        Integer couter = 0;
        for(byte char1 : data){

            couter++;

            if(couter > 2635) {

                Log.i("Jeejee", "jeejee");
            }
            switch (char1){

                    //ä:
                    case (byte)0xE4:

                        response += 0xC3  + 0x83 + 0xC6 + 0x92 + 0xC3 + 0x82 + 0xC2 + 0xA4;

                    break;
                    //ö:
                    case (byte)0xF6:

                        response += (0xC3 + 0x83 + 0xC6 + 0x92 + 0xC3 + 0x82 + 0xC2 + 0xA4);
                    break;

                    default:
                        response += (char)char1;


                }

        }

        return response;
    }
}

