package garye.utils.jhy.common;

import android.content.Context;
import android.content.SharedPreferences;

public class JPreferenceManager {

    public static String getString(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences("gagye", Context.MODE_PRIVATE);
        return sharedPref.getString(key,"");
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences("gagye", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
