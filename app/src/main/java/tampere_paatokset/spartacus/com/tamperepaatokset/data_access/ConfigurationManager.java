package tampere_paatokset.spartacus.com.tamperepaatokset.data_access;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Eetu on 27.10.2015.
 */
public class ConfigurationManager {

    private static final String settings_path = "HKIPaatokset";

    static SharedPreferences pref = null;
    static SharedPreferences.Editor editor = null;



    public static void initialize(Context context){

        pref = context.getSharedPreferences(settings_path, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    //Setters
    //=======

    //Possible values for video source:
    // 'MP4'
    // 'OGV'
    // 'HKI'
    public static void setVideoSource(String source){

        editor.putString("VideoMode", source);
        editor.commit();
    }



    //Getters
    //=======

    // 'MP4'
    // 'OGV'
    // 'HKI'
    public static String getVideoSource(){

        return pref.getString("VideoMode", "MP4");
    }

}
