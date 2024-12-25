package  com.test.testing12345.other;

import android.app.Activity;
import android.content.Context;

import com.test.testing12345.adsclass.DKeboaApplication;
import com.test.testing12345.event.EventCk;
import com.test.testing12345.adsclass.StylishFontCkModel;

import java.util.ArrayList;

public class SelectingCkFontStyle {
   public static ArrayList<StylishFontCkModel> fetchingJsonFromAssets(Activity activity, Context context, String str) {
       ArrayList<StylishFontCkModel> serializeList = StorageCkUtils.serializeList(FileCkUtil.loadJSONFromAsset(activity, context, str), StylishFontCkModel.class);
       if (serializeList != null) {
           return serializeList;
       }
       return new ArrayList<>();
   }

   public static void setSelectedFont(Context context, ArrayList<StylishFontCkModel> arrayList, int i) {
       PrefCk.getSharedPreferences(context);
       PrefCk.setSelectedFontObject( StorageCkUtils.deserialize(arrayList.get(i)));
   }

   public static StylishFontCkModel getSelectedFont(Context context) {
       PrefCk.getSharedPreferences(context);
       return (StylishFontCkModel)  StorageCkUtils.serializeObject(PrefCk.getSelectedFontObject(), StylishFontCkModel.class);
   }

   public static CharSequence changingFont(EventCk eventCk) {
       char c;
       if (eventCk.getTextToCommit().length() - 1 >= 0) {
           c = eventCk.getTextToCommit().charAt(eventCk.getTextToCommit().length() - 1);
       } else {
           c = eventCk.getTextToCommit().charAt(eventCk.getTextToCommit().length());
       }
       PrefCk.getSharedPreferences(DKeboaApplication.getInstance().getApplicationContext());
       StylishFontCkModel stylishFontCkModel = (StylishFontCkModel)  StorageCkUtils.serializeObject(PrefCk.getSelectedFontObject(), StylishFontCkModel.class);
       if (stylishFontCkModel != null) {
           return stylishFontCkModel.getStyledCharacter(c);
       }
       return eventCk.getTextToCommit();
   }

   public static String changeNumericFontStyle(char c) {
       PrefCk.getSharedPreferences(DKeboaApplication.getInstance().getApplicationContext());
       StylishFontCkModel stylishFontCkModel = (StylishFontCkModel)  StorageCkUtils.serializeObject(PrefCk.getSelectedFontObject(), StylishFontCkModel.class);
       if (stylishFontCkModel != null) {
           return stylishFontCkModel.getStyledNumber(c);
       }
       return String.valueOf(c);
   }
}
