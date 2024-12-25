package com.test.testing12345.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StorageCkUtils {
    private static StorageCkUtils instance;
    private static PackageManager packageManager;
    private Context context;
    private SharedPreferences preferences;

    private StorageCkUtils() {
    }

    public static void init(Context context) {
        StorageCkUtils storageCkUtils = new StorageCkUtils();
        instance = storageCkUtils;
        storageCkUtils.context = context;
        storageCkUtils.preferences = PrefCk.getSharedPreferences(context);
        packageManager = context.getPackageManager();
    }

    public static StorageCkUtils getInstance() {
        StorageCkUtils storageCkUtils = instance;
        if (storageCkUtils != null) {
            return storageCkUtils;
        }
        throw new RuntimeException("Please initialize StorageUtils first");
    }

    public static <E> String deserialize(E e) {
        return new Gson().toJson(e);
    }

    public static <E> String deserializeList(List<E> list) {
        return new Gson().toJson(list);
    }

    public static <T> T serializeObject(String str, Class<T> cls) {
        Type type = TypeToken.getParameterized(cls, cls).getType();
        if (str == null) {
            return null;
        }
        try {
            return (T) new Gson().fromJson(str, type);
        } catch (JsonSyntaxException unused) {
            return null;
        }
    }

    public static <T> ArrayList<T> serializeList(String str, Class<T> cls) {
        Type type = TypeToken.getParameterized(ArrayList.class, cls).getType();
        if (TextUtils.isEmpty(str)) {
            return new ArrayList<>();
        }
        try {
            return (ArrayList) new Gson().fromJson(str, type);
        } catch (JsonSyntaxException unused) {
            return new ArrayList<>();
        }
    }
}
