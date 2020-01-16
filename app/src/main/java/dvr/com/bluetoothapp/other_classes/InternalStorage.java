package dvr.com.bluetoothapp.other_classes;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class InternalStorage {

    private InternalStorage() {}

    public static void writeObject(Context context, String key, Object object) throws IOException {
        FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
        fos.close();
    }

    public static Object readObject(Context context, String key) throws IOException,
            ClassNotFoundException {
        FileInputStream fis = context.openFileInput(key);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        return object;
    }

    public static boolean deleteFolder(Context context,String dir)throws IOException,
            ClassNotFoundException{
        File myDir = context.getFilesDir();
        File mi = new  File(myDir,dir);
        if (mi.exists())
        {
            mi.delete();
            return true;
        }
        else
        {
            return false;
        }
    }
    public static boolean isFileContains(Context context,String dir)
    {
        File myDir = context.getFilesDir();
        File mi = new  File(myDir,dir);
        if (mi.exists())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}