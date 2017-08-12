package com.squareapp.taskreminder;

/**
 * Created by Valentin Purrucker on 11.08.2017.
 */

import android.content.Context;
import android.graphics.Typeface;
import java.util.Hashtable;


public class FontCache {

    private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();

    public static Typeface get(String name, Context context) {
        Typeface tf = fontCache.get(name);
        if(tf == null) {
            try {
                tf = Typeface.createFromAsset(context.getAssets(), name);
            }
            catch (Exception e) {
                return null;
            }
            fontCache.put(name, tf);
        }
        return tf;
    }
}
