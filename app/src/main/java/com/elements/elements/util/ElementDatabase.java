package com.elements.elements.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;

import com.elements.elements.element.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giancarlo on 20/12/2015.
 * Handler to get all elements from the database
 */
public class ElementDatabase {
    public static List<Element> getAll(Context c) {
        List<Element> l = new ArrayList<>();

        TypedArray ta = c.getResources().obtainTypedArray(getArrayIdentifier(c, "elements"));
        int n = ta.length();
        for (int i = 0; i < n; ++i) {
            try {
                int id = ta.getResourceId(i, 0);
                String[] array = c.getResources().getStringArray(id);

                Element element = new Element();
                element.setName(array[0]);
                element.setSymbol(array[1]);
                l.add(element);
            } catch(Exception e) {
                Log.i("ELEMENT", "Error parsing element " + i);
            }
        }

        return l;
    }

    public static int getArrayIdentifier(Context context, String name) {
        return context.getResources().getIdentifier(name, "array", context.getPackageName());
    }
}
