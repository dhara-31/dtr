package com.test.testing12345.other;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;


public class FileCkUtil {
    public static String readAssetsFile(Activity activity) {
        return null;
    }

    public static String createTextFile(String str, String str2) {
        String str3 = Environment.getExternalStorageDirectory() + File.separator + str;
        File file = new File(str3);
        if (!file.isDirectory()) {
            file.mkdir();
        }
        File file2 = new File(str3 + File.separator + str2 + ".json");
        if (!file2.exists()) {
            try {
                file2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file2.getPath();
    }

    public static void writeData(String str, String str2) {
        File file = new File(str);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
            outputStreamWriter.write(str2);
            outputStreamWriter.flush();
            outputStreamWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String loadJSONFromAsset(Activity activity, Context context, String str) {
        try {
            InputStream open = context.getAssets().open(str);
            byte[] bArr = new byte[open.available()];
            open.read(bArr);
            open.close();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                return new String(bArr, StandardCharsets.UTF_8);
            }else
            {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
