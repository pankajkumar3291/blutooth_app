package dvr.com.bluetoothapp.Util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Created by android on 19/5/18.
 */

public class MyUtil {

    public static void hideKeyBoardActivity(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        assert inputManager != null;
        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

    }


    public static void setCorrection(Context context, String correction) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("correct", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("correction", correction);
        editor.apply();


    }

    public static String getCorrection(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("correct", Context.MODE_PRIVATE);
        return sharedPreferences.getString("correction", "0");
    }

    public static void createCachedFile(Context context, String fileName, String content) throws IOException {

        File cacheFile = new File(context.getCacheDir() + File.separator + fileName);
        cacheFile.createNewFile();

        FileOutputStream fos = new FileOutputStream(cacheFile);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF8");
        PrintWriter pw = new PrintWriter(osw);

        pw.println(content);

        pw.flush();
        pw.close();
    }
    public static void setLocation(Context context, String location) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("location", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("location", location);
        editor.apply();

    }
    public static String getLocation(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("location", Context.MODE_PRIVATE);
        return sharedPreferences.getString("location", "");


    }


    public static void setBy(Context context, String by) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("by", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("by", by);
        editor.apply();
    }


    public static String getBy(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("by", Context.MODE_PRIVATE);
        return sharedPreferences.getString("by", "");


    }


}
