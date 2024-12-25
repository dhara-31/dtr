package com.test.testing12345.custom.utils;

import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import java.io.Closeable;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

 public final class JsonCkUtils {
    private static final String EMPTY_STRING = "";
    private static final String INTEGER_CLASS_NAME = "Integer";
    private static final String STRING_CLASS_NAME = "String";
    private static final String TAG = "JsonUtils";

     public static List<Object> jsonStrToList(String str) {
        ArrayList arrayList = new ArrayList();
        JsonReader jsonReader = new JsonReader(new StringReader(str));
        try {
            jsonReader.beginArray();
            while (jsonReader.hasNext()) {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String nextName = jsonReader.nextName();
                    if (nextName.equals(INTEGER_CLASS_NAME)) {
                        arrayList.add(Integer.valueOf(jsonReader.nextInt()));
                    } else if (nextName.equals(STRING_CLASS_NAME)) {
                        arrayList.add(jsonReader.nextString());
                    } else {
                        String str2 = TAG;
                        Log.w(str2, "Invalid name: " + nextName);
                        jsonReader.skipValue();
                    }
                }
                jsonReader.endObject();
            }
            jsonReader.endArray();
            close(jsonReader);
            return arrayList;
        } catch (IOException unused) {
            close(jsonReader);
            return Collections.emptyList();
        } catch (Throwable th) {
            close(jsonReader);
            throw th;
        }
    }

    public static String listToJsonStr(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);
            try {
                jsonWriter.beginArray();
                for (Object obj : list) {
                    jsonWriter.beginObject();
                    if (obj instanceof Integer) {
                        jsonWriter.name(INTEGER_CLASS_NAME).value((Integer) obj);
                    } else if (obj instanceof String) {
                        jsonWriter.name(STRING_CLASS_NAME).value((String) obj);
                    }
                    jsonWriter.endObject();
                }
                jsonWriter.endArray();
                return stringWriter.toString();
            } catch (IOException unused) {
            } finally {
                close(jsonWriter);
            }
        }
        return "";
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
            }
        }
    }
}
